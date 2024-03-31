import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Svg_Exporter {
    private Map<String, List<byte[]>> images = new HashMap<>();

    String path;


    public Svg_Exporter(SvgImporter svgImporter, String outPath) {
        this.images = svgImporter.images;
        this.path = outPath;
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
                imgExporter(entry.getValue(), entry.getKey(),newPath);
            }
        } else {
            System.out.println("No elements!!");
        }
    }
//TODO переделать для работы с байт кодом листа
private void imgExporter(List<byte[]> list, String format, String path) {
    for (int i = 0; i < list.size(); i++) {

            byte[] imageData = list.get(i);
            try {
                // Преобразование массива байтов в изображение
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));

                // Генерация уникального имени файла
                String fileName = path + format + "_file_" + (i + 1) + "_" + ( + 1) + "." + format;

                // Сохранение изображения на диск
                ImageIO.write(image, format, new File(fileName));
            } catch (IOException e) {
                throw new RuntimeException("Ошибка при записи изображения: " + e.getMessage());
            }

    }
}

}
