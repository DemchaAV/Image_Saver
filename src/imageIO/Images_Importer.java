package imageIO;

import parsel.ImageParser;
import parsel.MultiParser;
import parsel.URL_Checker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Images_Importer {
    private FileReader file;
    private boolean isUrl = false;
    private Image_Exporter export;
    ImageParser src;
    URL_Checker url_checker;
    private int currentPosition = 0;
    ByteWorker byteWorker;
    private boolean svgStatus = false;
    MultiParser svgParse = new MultiParser("<svg", "</svg>", true);
    private URLConnection urlConnection = null;
    private URL url = null;
    private boolean pathStatus = false;
    private String webUrl;
    Map<String, List<byte[]>> images = new HashMap<>();
    String pathOut;

    public Images_Importer(FileReader file) {
        this.file = file;
        this.webUrl = null;
        src = new ImageParser("<img", "</img>", ">");
    }

    public Images_Importer(String webUrl, String pathOut) {
        this.webUrl = webUrl.replace('\\', '/');
        this.webUrl = this.webUrl.charAt(webUrl.length() - 1) == '/' ? webUrl : webUrl + "/";
        this.isUrl = true;
        url_checker = new URL_Checker(this.webUrl);
        src = new ImageParser("<img", "</img>", ">");
        this.pathOut = getAbsolutPath(pathOut);
        this.byteWorker = new ByteWorker();
        try {
            this.url = new URL(webUrl);
            this.urlConnection = this.url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed URL: " + webUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("I/O Error opening connection", e);
        }
    }


    public String getAbsolutPath(String pathOut) {
        String absolutPath;
        pathOut = pathOut.replace('\\', '/');
        final String wwww = "www.";
        final String https = "https://";
        absolutPath = this.webUrl.startsWith(https) ? this.webUrl.substring(this.webUrl.indexOf(https) + https.length()) : this.webUrl;
        absolutPath = absolutPath.startsWith(wwww) ? absolutPath.substring(absolutPath.indexOf(wwww) + wwww.length()) : absolutPath;
        absolutPath = absolutPath.contains("/") ? absolutPath.substring(0, absolutPath.lastIndexOf(".")) : absolutPath;
        pathOut = pathOut.replace('/', '\\');
        pathOut = pathOut.charAt(pathOut.length() - 1) == '\\' ? pathOut : pathOut + "\\";

        return pathOut + absolutPath + "\\";
    }

    public void read() throws IOException {
        BufferedReader reader = isUrl ? new BufferedReader(new InputStreamReader(urlConnection.getInputStream())) : new BufferedReader(file);
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("<svg") || svgStatus) {
                svgStatus = svgParse.put(line);
                setSvg(svgParse.pull().stream().map(this::checkQuotes).map(this::pathStatus).collect(Collectors.toList()));
                src.put(line);
            } else {
                src.put(line);
            }
        }
        reader.close();
        List<String> links = src.pull();
        int num = images.get("svg") != null ? images.get("svg").size() : 0;
        System.out.println("Defined " + (links.size() + num) + " objects!");
        //Загружаем наши ссылки с те в байт коде, и определяем нагу imagest map
        downloadLinks(links);
    }

    public void write() {
        for (Map.Entry<String, List<byte[]>> entry : images.entrySet()) {
            String path = this.pathOut.replace('\\', '/') + entry.getKey() + "/";
            System.out.println("Files: " + entry.getKey() + " are " + entry.getValue().size());
            AtomicInteger number = new AtomicInteger(1);
            for (byte[] file : entry.getValue()) {
                String fileName = "img_" + number.getAndIncrement() + "." + entry.getKey();
                byteWorker.writer(file, path, fileName);
            }
        }
    }
    private void downloadLinks(List<String> imagesLinks) {
        //Проверяем все ли ссылки у нас сылки имеют абсолютный путь, если не все ссылки убдут исправленны
        imagesLinks = imagesLinks.stream().map(url_checker::getAbsolutURl).collect(Collectors.toList());
        byte[] currentList;
        String key;

        for (int i = 0; i < imagesLinks.size(); i++) {
            key = defineFileFormat(imagesLinks.get(i));
            currentList = byteWorker.loaderImageFromWeb(imagesLinks.get(i));
            putInMap(key, currentList);
            System.out.println("Link " + imagesLinks.get(i) + " has download");
        }
    }

    private void setSvg(List<String> listSvg) {
        for (int i = 0; i < listSvg.size(); i++) {
            putInMap("svg", listSvg.get(i).getBytes());
        }
    }

    private String defineFileFormat(String link) {
        String fileFormat;
        fileFormat = link.lastIndexOf('.') < link.length() - 6 ? "webp" : (link.substring(link.lastIndexOf('.') + 1));
        return fileFormat;
    }

    private void keyChecker(String key) {
        images.putIfAbsent(key, new ArrayList<>());
    }

    private String pathStatus(String line) {
        final String startsPath = "<path";
        final String endsPath = "/>";
        if (pathStatus && line.contains(endsPath)) {
            line = line.replace(endsPath, "></path>");
            pathStatus = false;
        } else if (line.contains(startsPath)) {
            pathStatus = true;
        }
        return line;
    }

    private String checkQuotes(String currentLine) {
        final String rolePresentation = "role=presentation";
        final String ariaHiddenTrue = "aria-hidden=true";
        if (currentLine.contains(rolePresentation)) {
            currentLine = currentLine.replace(rolePresentation, "role=\"presentation\"");
        }
        if (currentLine.contains(ariaHiddenTrue)) {
            currentLine = currentLine.replace(ariaHiddenTrue, "aria-hidden=\"true\"");
        }
        return currentLine;
    }

    private void putInMap(String key, byte[] byteList) {
        keyChecker(key);
        images.get(key).add(byteList);
    }

}
