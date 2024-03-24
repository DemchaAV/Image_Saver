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
    void write(){
        BufferedWriter writer;
        FileWriter file;
        File fileDirectory;
        String curLine;
        for (int i = 0; i < svg_list.size(); i++) {
            try {
                fileDirectory = new File(path);
                if (!fileDirectory.exists()) {
                    fileDirectory.mkdir();
                }
                file = new FileWriter(path+"svg_file_"+i+1+".svg");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            writer = new BufferedWriter(file);
            for (int j = 0; j < svg_list.get(i).size(); j++) {
               curLine= svg_list.get(i).get(j);
                try {
                    writer.write(curLine);
                    writer.newLine();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
