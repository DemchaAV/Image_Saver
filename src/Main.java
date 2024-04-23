import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        String url = "https://www.ebay.co.uk/";
        String outPath = "C:/Users/Demch/OneDrive/Рабочий стол/test_image_downloader/";
        Images_Importer svg = new Images_Importer(url, outPath);
        svg.read();
        svg.write();
        double spentTime = (double) (System.currentTimeMillis() - startTime) / 1000;
        String second = spentTime > 2 ? " seconds" : " second";
        System.out.println("Processing time: " + spentTime + second);
    }
}