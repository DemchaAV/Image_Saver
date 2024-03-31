import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class ByteWorker {
    private String outPath;

    public ByteWorker(String outPath) {
        this.outPath = outPath;
    }

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

    public void writer(byte[] imgByte,String outPath){
        System.out.println(outPath);
        File directory = null;
        directory = new File(outPath);
        if (!directory.exists()) {
            directory.mkdirs(); // Создаём все необходимые родительские каталоги

        }

        try (FileOutputStream fos = new FileOutputStream(outPath)) {
                fos.write(imgByte);
                System.out.println("Изображение сохранено на диск: downloaded_image" + ( + 1));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

}
