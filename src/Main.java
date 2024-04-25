import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//        long startTime = System.currentTimeMillis();
//        String url = "https://www.ebay.co.uk/";
//        String outPath = "C:/Users/Demch/OneDrive/Рабочий стол/test_image_downloader/";
//        imageIO.Images_Importer svg = new imageIO.Images_Importer(url, outPath);
//        svg.read();
//        svg.write();
//        double spentTime = (double) (System.currentTimeMillis() - startTime) / 1000;
//        String second = spentTime > 2 ? " seconds" : " second";
//        System.out.println("Processing time: " + spentTime + second);

        String startPosition = "2h";
        int rows = 15;
        int columns = 25;
        String endPosition = calculateEndPosition(startPosition, rows, columns);
        System.out.println(endPosition);
    }

    private static String calculateEndPosition(String startPosition, int rows, int columns) {
        int startRow = Integer.parseInt(startPosition.substring(0,1));
        System.out.println("Strat row: " + startRow);
        char startColumn = startPosition.charAt(1);

        int endRow = (startRow + rows -1) % 8+1 ;  // Modulo to wrap around, adjust indexing
        int endColumnIndex = (startColumn - 'a' + columns) % 8;  // Calculate column offset
        char endColumn = (char)('a' + endColumnIndex);

        return endRow + String.valueOf(endColumn);
    }



    }


