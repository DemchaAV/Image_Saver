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

public class SvgImporter {
    private FileReader file;
    private boolean isUrl = false;
    ImageSrc src;
    private URLConnection urlConnection = null;
    private URL url = null;
    private boolean pathStatus = false;
    private final String webUrl;
    Map<String, List<List<String>>> images = new HashMap<>();
    private boolean startSvg = false;
    private boolean startImgClass = false;
    private List<String> currentObject;

    public SvgImporter(FileReader file) {
        this.file = file;
        this.webUrl = null;
        src = new ImageSrc();
    }

    public SvgImporter(String webUrl) {
        this.webUrl = webUrl;
        this.isUrl = true;
        src = new ImageSrc();

        try {
            this.url = new URL(webUrl);
            this.urlConnection = this.url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed URL: " + webUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("I/O Error opening connection", e);
        }
    }

    public void read() throws IOException {
        BufferedReader reader = isUrl ? new BufferedReader(new InputStreamReader(urlConnection.getInputStream())) : new BufferedReader(file);
        String line;
        while ((line = reader.readLine()) != null) {
            isImage(line);
        }
        reader.close();
    }

    private void isImage2(String line) {
        if (!startImgClass && startSvg) {
            svgCreator(line);
        } else if (!startImgClass) {
            startSvg = isSvgStarts(line);
            if (!startSvg && startImgClass) {
                if (src.get() == null) {
                    src.put(line);
                } else {
                    executeSrc(src.pull());
                }
            } else {
                startImgClass = src.put(line);
            }

        } else {
            startImgClass = src.put(line);
        }
    }

    private void isImage(String line) {
        if (!startImgClass) {
            if (startSvg) {
                svgCreator(line);
            } else {
                startSvg = isSvgStarts(line);
            }
        }
        if (!startSvg) {
            if (startImgClass) {
                if (src.get() == null) {
                    src.put(line);
                } else {
                    executeSrc(src.pull());
                }
            } else {
                startImgClass = src.put(line);
                if (src.get() != null) {
                    executeSrc(src.pull());
                }
            }
        }
    }

    private void executeSrc(String src) {
        if (src != null && !src.isEmpty()) {
            src = src.startsWith("//www.") ? "https://" + src.substring(2) : src;
            src = src.startsWith("https://") || src.startsWith("www.") ? src : this.webUrl + src.substring(1);
            String key = src.substring(src.lastIndexOf(".") + 1);
            keyChecker(key);
            currentObject = getListImageAsObject(src);
            images.get(key).add(currentObject);
        }
    }


    private boolean isSvgStarts(String line) {
        final String startSvg = "<svg ";
        if (line.contains(startSvg)) {
            currentObject = new ArrayList<>();
            String key = "svg";
            keyChecker(key);
            images.get(key).add(currentObject);
            svgCreator(line);
            return true;
        }
        return false;
    }

    private void keyChecker(String key) {
        images.putIfAbsent(key, new ArrayList<>());
    }

    private void svgCreator(String currentLine) {
        final String endsSvg = "</svg>";
        String key = "svg";
        if (currentLine.contains(endsSvg)) {
            currentLine = checkQuotes(currentLine);
            String newLine = currentLine.substring(0, currentLine.indexOf(endsSvg) + endsSvg.length());
            newLine = isPathStatus(newLine);
            currentObject.add(newLine);
            startSvg = false;
        } else {
            String newLine = checkQuotes(currentLine);
            newLine = isPathStatus(newLine);
            currentObject.add(newLine);
        }
    }

    public String isPathStatus(String line) {
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

    private List<String> getListImageAsObject(String webUrl) {
        System.out.println(webUrl);
        List<String> svg = new ArrayList<>();
        try {
            URL url = new URL(webUrl);
            URLConnection urlConnection = url.openConnection();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    svg.add(line);
                }
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL: " + webUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("I/O Error reading from " + webUrl);

        }
        return svg;
    }

    public static void main(String[] args) {
        // Example usage of SvgImporter
    }
}
