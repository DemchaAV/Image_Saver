import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Svg_Exporter {
    private Map<String, List<byte[]>> images = new HashMap<>();

    String path;


    public Svg_Exporter(SvgImporter svgImporter) {
        this.images = svgImporter.images;
        this.path = svgImporter.pathOut;
    }
    void write() {
        File directory = null;
        String foldeer;
        if (!images.isEmpty()) {

            for (Map.Entry<String, List<byte[]>> entry : images.entrySet()) {
                foldeer = entry.getKey();
                String newPath = path + "/" + foldeer + "/";
                directory = new File(newPath);
                if (!directory.exists()) {
                    directory.mkdirs(); // Создаём все необходимые родительские каталоги

                }
                for(byte [] data: entry.getValue()){
                    imgExporter(data, entry.getKey(),newPath);
                }

            }
        } else {
            System.out.println("No elements!!");
        }
    }

    //TODO convert to work with sheet byte code
private void imgExporter(byte[] imageData, String format, String path) {


    try {
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));
        String fileName = path + "image_" + "." + format;
                ImageIO.write(image, format, new File(fileName));
            } catch (IOException e) {
        throw new RuntimeException("Error while recording an image: " + e.getMessage());
            }


}

}
