import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Svg_Exporter {
    List<List<String>> svg_list = new ArrayList<>();
    String path;


    public Svg_Exporter(Svg_importer svgImporter, String path) {
        this.svg_list = svgImporter.svg_list;
        this.path = path;
    }
    void write() {
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs(); // Создаём все необходимые родительские каталоги
        }

        for (int i = 0; i < svg_list.size(); i++) {
            String fileName = path + "svg_file_" + (i + 1) + ".svg";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                for (String curLine : svg_list.get(i)) {
                    writer.write(curLine);
                    writer.newLine();
                }
            } catch (IOException e) {
                throw new RuntimeException("Ошибка при записи файла: " + fileName, e);
            }
        }
    }

}
