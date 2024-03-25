import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        FileReader fileReader;
        //resourse for downloading your file

        String url = "https://www.amayzon.co/";

        String path = "C:/Users/Demch/OneDrive/Рабочий стол/amazon/";
//        fileReader = new FileReader(url);
        Svg_importer svgImporter = new Svg_importer(url);
        try {
            svgImporter.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(svgImporter.svg_list.size());
        Svg_Exporter svgExporter = new Svg_Exporter(svgImporter,path);
        svgExporter.write();
    }
    }
