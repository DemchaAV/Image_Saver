import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Svg_Exporter {
    private Map<String, List<List<String>>> images = new HashMap<>();

    String path;


    public Svg_Exporter(SvgImporter svgImporter, String outPath) {
        this.images = svgImporter.images;
        this.path = outPath;
    }
    void write() {
        File directory = null;
        String foldeer;
        if (!images.isEmpty()) {

            for (Map.Entry<String, List<List<String>>> entry : images.entrySet()) {
                foldeer = entry.getKey();
                String newPath = path + "/" + foldeer + "/";
                directory = new File(newPath);
                if (!directory.exists()) {
                    directory.mkdirs(); // Создаём все необходимые родительские каталоги

                }
                imgExporter(entry.getValue(), entry.getKey(),newPath);
            }
        } else {
            System.out.println("No elements!!");
        }
    }

    private void imgExporter(List<List<String>> list, String format, String path) {
        for (int i = 0; i < list.size(); i++) {
            String fileName = path + format + "_file_" + (i + 1) + "." + format;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                for (String curLine : list.get(i)) {
                    writer.write(curLine);
                    writer.newLine();
                }
            } catch (IOException e) {
                throw new RuntimeException("Ошибка при записи файла: " + fileName, e);
            }
        }
    }

}
