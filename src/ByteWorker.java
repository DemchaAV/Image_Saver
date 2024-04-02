import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class ByteWorker {
    public byte[] loaderImageFromWeb(String webUrl) {
        ByteArrayOutputStream buffer;
        try {
            URL url = new URL(webUrl);
            URLConnection urlConnection = url.openConnection();
            try (InputStream inputStream = urlConnection.getInputStream()) {
                buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[1024];
                while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL: " + webUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("I/O Error reading from " + webUrl);
        }
        return buffer.toByteArray();

    }

    public void writer(byte[] imgByte, String outPath, String fileName) {
        // Предположим, что outPath - это путь к каталогу, и мы добавим имя файла для сохранения изображения
        File directory = new File(outPath);
        if (!directory.exists()) {
            directory.mkdirs(); // Создаём все необходимые родительские каталоги
        }
        File outputFile = new File(directory, fileName); // Создаём объект файла в указанном каталоге с именем fileName


        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(imgByte);
            System.out.println("Изображение сохранено на диск: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}