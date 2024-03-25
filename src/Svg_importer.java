import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Svg_importer {
    FileReader file;
    private boolean isUrl = false;
    private URLConnection urlConnection = null;
    private URL url = null;
    String path;
    List<List<String>> svg_list = new ArrayList<>();
    private final String start_svg = "<svg ";
    boolean start = false;
    private final String end_svg = "</svg>";
    private final String img_svg = ".svg\">";
    int counter = -1;
    boolean pathStatus = false;
    String webUrl;

    public Svg_importer(FileReader file) {
        this.file = file;
        path = file.getEncoding();
    }

    public Svg_importer(String webUrl) {
        isUrl = true;
        this.webUrl = webUrl;

        try {
            url = new URL(webUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        try {
            urlConnection = url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void read() throws IOException {
        BufferedReader reader = null;
        if (isUrl) {
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        } else {
            reader = new BufferedReader(file);
        }
        String line;
        while ((line = reader.readLine()) != null) {

            if (start) {
                svg_creator(line);

            } else {
                isContainImg_svg(line);
                start = isSvg_starts(line);

            }
            reader.lines();
        }
        reader.close();

    }

    boolean isContainImg_svg(String line) {
        String img_start_svg = "src=\"";
        if (line.contains(img_start_svg) && line.contains(img_svg)) {
            String srcValue = line.substring(line.indexOf(img_start_svg) + img_start_svg.length(), line.indexOf("\"", line.indexOf(img_start_svg) + img_start_svg.length()));
            if (srcValue.endsWith(".svg")) { // Проверяем, что ссылка заканчивается на .svg
                String fullUrl = srcValue.startsWith("http") ? srcValue : this.webUrl + srcValue; // Обрабатываем абсолютный и относительный URL
                svg_list.add(getList(fullUrl)); // Добавляем содержимое SVG в последний список
            }
            return true;
        }
        return false;
    }

    boolean isSvg_starts(String line) {
        if (!line.contains(start_svg)) {
            return false;
        } else {
            ++counter;
            svg_list.add(new ArrayList<>());
            svg_creator(line);
            return true;
        }
    }

    void svg_creator(String currentLine) {
        if (currentLine.contains(start_svg)) {
            int index = currentLine.indexOf(start_svg);
            seekingEnd(currentLine.substring(index));
        } else {
            seekingEnd(currentLine);
        }
    }

    private List<String> getList(String webUrl) {
        List<String> svg = new ArrayList<>();
        // Использование try-with-resources для автоматического закрытия BufferedReader
        try {
            URL url = new URL(webUrl);
            URLConnection urlConnection = url.openConnection();
            // BufferedReader автоматически закроется благодаря try-with-resources
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    svg.add(line);
                }
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Некорректный URL: " + webUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка ввода/вывода при чтении данных с " + webUrl, e);
        }
        return svg;
    }

    boolean seekingEnd(String currentLine) {
        if (currentLine.contains(end_svg)) { // Checking if the line contains the end_svg string
            int index = currentLine.indexOf(end_svg) + end_svg.length(); // Finding the index where the end_svg ends
            String newLine = currentLine.substring(0, index); // Extracting the substring from the beginning to the end_svg
            svg_list.get(counter).add(newLine); // Adding the substring to the list
            start = false; // Updating the start flag
            return start; // Returning the updated start flag
        } else {
            if (pathStatus) {
                pathStatus = isPathStatus(currentLine); // Checking if the line indicates a path status
                return false;
            } else {

                return isPathStatus(currentLine); // Returning false as the end condition is not met
            }
        }
    }


    boolean isPathStatus(String line) {
        if (pathStatus) {
            if (line.contains("\"/>")) {
                line = line.replace("\"/>", "\"></path>");
            }
            svg_list.get(counter).add(line); // Adding the line to the list
            return false;

        } else {
            pathStatus = line.contains("<path ");
            if(pathStatus){
                if (line.contains("\"/>")) {
                    line = line.replace("\"/>", "\"></path>");
                    svg_list.get(counter).add(line);
                    return false;
                }
            }
            if(line.contains("role=presentation aria-hidden=true")){
               line =  line.replace("role=presentation aria-hidden=true","role=\"presentation\" aria-hidden=\"true\"");
            }
            svg_list.get(counter).add(line); // Adding the line to the list
            return false;
        }

    }


}


