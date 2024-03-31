import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SvgImporter {
    private FileReader file;
    private boolean isUrl = false;
    private Svg_Exporter export;
    ImageParser src;
    URL_Checker url_checker;
    private int currentPosition = 0;
    ByteWorker byteWorker;
    private boolean svgStatus = false;
    Parser svgParser = new Parser("<svg", "</svg>", true);
    private URLConnection urlConnection = null;
    private URL url = null;
    private boolean pathStatus = false;
    private String webUrl;
    Map<String, List<byte[]>> images = new HashMap<>();
    String pathOut;

    public SvgImporter(FileReader file) {
        this.file = file;
        this.webUrl = null;
        src = new ImageParser("<img", "</img>", ">");
    }

    public SvgImporter(String webUrl, String pathOut) {
        this.webUrl = webUrl.replace('\\', '/');
        this.webUrl = this.webUrl.charAt(webUrl.length() - 1) == '/' ? webUrl : webUrl + "/";
        this.isUrl = true;
        url_checker = new URL_Checker(this.webUrl);
        src = new ImageParser("<img", "</img>", ">");
        this.pathOut = getAbsolutPath(pathOut);
        this.byteWorker = new ByteWorker(pathOut);
        System.out.println(this.pathOut);
        try {
            this.url = new URL(webUrl);
            this.urlConnection = this.url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed URL: " + webUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("I/O Error opening connection", e);
        }
    }

    boolean isSVG(String line) {
        svgParser.put(line);
        if (svgParser.doneStatus) {
            String key = "svg";
            keyChecker(key);
            currentPosition = src.currentPosition;
            line = checkQuotes(isPathStatus(svgParser.pull()));
            images.get(key).add(line.getBytes());
        }
        return svgParser.startStatus;
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
                svgStatus = !isSVG(line);
            } else {
                src.put(line);
            }
        }
        reader.close();
        List<String> links = src.pull();
        //Загружаем наши ссылки с те в байт коде, и определяем нагу imagest map
        downloadLinks(links);
    }

    public void write() {

        for (Map.Entry<String, List<byte[]>> entry : images.entrySet()) {
            String path = this.pathOut.replace('\\', '/') + entry.getKey() + "/";
            entry.getValue().forEach((x) ->
                    byteWorker.writer(x, path));
        }

        export = new Svg_Exporter(this, pathOut);
        export.write();
        System.out.println("Objects created in:  " + pathOut);
    }
    private void downloadLinks(List<String> imagesLinks) {
        //Проверяем все ли ссылки у нас сылки имеют абсолютный путь, если не все ссылки убдут исправленны
        imagesLinks = imagesLinks.stream().map(url_checker::getAbsolutURl).collect(Collectors.toList());
        // TODO Убрать строку печати всех ссылко
        imagesLinks.forEach(System.out::println);
        byte[] currentList;
        String key;

        for (int i = 0; i < imagesLinks.size(); i++) {
            key = defineFileFormat(imagesLinks.get(i));
            currentList = byteWorker.loaderImageFromWeb(imagesLinks.get(i));
            putInMap(key, currentList);
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

    private String isPathStatus(String line) {
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

    public void getInfo() {
        this.images.forEach((key, folder) -> System.out.println(key + ": " + folder.size() + (folder.size() > 1 ? " objects" : "object")));
    }
}