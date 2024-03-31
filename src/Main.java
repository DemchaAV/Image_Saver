import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        FileReader fileReader;
        long startTime = System.currentTimeMillis();
        String url = "https://www.amayzon.co/";
        String path = "C:/Users/Demch/OneDrive/Рабочий стол/";
        SvgImporter svg = new SvgImporter(url, path);
        try {
            svg.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        svg.getInfo();
        svg.write();
//        System.out.println("Created " + svg.images.size() + (svg.getSvg_list().size() > 1 ? " an objects" : " object"));
        double spentTime = (double) (System.currentTimeMillis() - startTime) / 1000;
        String second = spentTime > 2 ? " seconds" : " second";
        System.out.println("Processing time: " + spentTime + second);
    }
}
