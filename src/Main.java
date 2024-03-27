import org.jsoup.nodes.Document;

import javax.imageio.ImageReader;
import javax.swing.text.html.ImageView;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        FileReader fileReader;
        //resourse for downloading your file
        long startTime = System.currentTimeMillis();
        String url = "https://www.amayzon.co/";
//        String path_in_file = "C:\\Users\\Demch\\OneDrive\\Рабочий стол\\Google for Developers - from AI and Cloud to Mobile and Web.html";

        String path = "C:/Users/Demch/OneDrive/Рабочий стол/svg_amazon";
//        fileReader = new FileReader(path_in_file);
        SvgImporter svgImporter = new SvgImporter(url);
        try {
            svgImporter.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        svgImporter.images.forEach((key, folder)-> System.out.println(key + ": "+ folder.size() + " objects"));
        Svg_Exporter svgExporter = new Svg_Exporter(svgImporter,path);
        svgExporter.write();
//        System.out.println("Created " + svgImporter.images.size() + (svgImporter.getSvg_list().size() > 1 ? " an objects" : " object"));
        double spentTime = (double) (System.currentTimeMillis() - startTime) / 1000;
        String second = spentTime > 2 ? " seconds" : " second";
        System.out.println("Processing time: " + spentTime + second);
    }
    }
