import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        FileReader fileReader;
        long startTime = System.currentTimeMillis();
        String url = "https://developers.google.com/";
        String outPath = "C:/Users/Demch/OneDrive/Рабочий стол/";
        SvgImporter svg = new SvgImporter(url, outPath);
        try {
            svg.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        svg.getInfo();
        svg.write();
        double spentTime = (double) (System.currentTimeMillis() - startTime) / 1000;
        String second = spentTime > 2 ? " seconds" : " second";
        System.out.println("Processing time: " + spentTime + second);
    }
}
