import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        FileReader fileReader;
        String path = "C:/Users/Demch/OneDrive/Рабочий стол/svg_files_sony/";
        try {
            fileReader = new FileReader("C:/Users/Demch/OneDrive/Рабочий стол/PlayStation® Official Site_ Consoles, Games, Accessories & More.html");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Svg_importer svgImporter = new Svg_importer(fileReader);
        try {
            svgImporter.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        svgImporter.svg_list.forEach(System.out::println);
        System.out.println(svgImporter.svg_list.size());
        System.out.println(fileReader.getEncoding());
        Svg_Exporter svgExporter = new Svg_Exporter(svgImporter,path);
        svgExporter.write();
    }
    }
