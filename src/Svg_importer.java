import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Svg_importer {
    FileReader file;
    String path;
    List<List<String>> svg_list = new ArrayList<>();
    private final String start_svg = "<svg ";
    boolean start = false;
    private final String end_svg = "</svg>";
    int counter = -1;

    public Svg_importer(FileReader file) {
        this.file = file;
        path = file.getEncoding();
    }

    public void read() throws IOException {
        BufferedReader reader = new BufferedReader(file);
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains(start_svg)) {
                start = true;
                ++counter;
                svg_list.add(new ArrayList<>());
            }
            if (start) {
                if (line.contains(end_svg)) {
                    svg_list.get(counter).add(line.substring(0, line.indexOf(end_svg) + 6)); // +6 to include "</svg>"
                    start = false; // Reset start as we have reached the end of an SVG
                } else {
                    svg_list.get(counter).add(line);
                }
            }
        }
        reader.close();
    }
}
