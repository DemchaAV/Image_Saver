import java.util.ArrayList;
import java.util.List;

public class ImageSrc {
    private String src;
    private boolean isUsable = false;
    private boolean imageStatus = false;
    private boolean srcStatus = false;
    private boolean imageStatusDone = true;
    private final String srcStarter = "src=\"";
    private final String starter = "<img";
    private final String ender = "</img>";
    private List<String> srcList;
    private final String enderShort = "/>";

    boolean put(String line) {
        if (imageStatusDone && isUsable && !srcStatus) {
            return false;
        }
        if (imageStatus) {
            srcParser(line);
            return imageStatus;
        } else {
            imageStatus = isImage(line);
            if (imageStatus) {
                isUsable = true;
                srcParser(line);
            }
            return imageStatus;
        }
    }

    boolean isImage(String line) {
        imageStatus = line.contains(starter) ? true : false;
        imageStatusDone = !imageStatus;
        return imageStatus;
    }

    private void srcParser(String line) {
        if (line.contains(srcStarter) || srcStatus) {
            int index = line.contains(srcStarter) ? line.indexOf(srcStarter) + srcStarter.length() : 0;
            line = line.contains(srcStarter) ? line.substring(index) : line;
            if (line.contains("\"")) {
                src = src == null ? line.substring(0, line.indexOf("\"")) : src + line.substring(0, line.indexOf("\""));
//                curPosition
                imageStatus = false;
                srcStatus = true;
                imageStatusDone = true;
            } else {
                src = src == null ? line : src + line;
                srcStatus = true;
            }
        } else {
            imageStatusDone = isEndImageClass(line);
        }
    }

    private boolean isEndImageClass(String line) {
        if (line.contains(ender) || line.contains(enderShort)) {
            return true;
        } else {
            return false;
        }
    }

    void showInfo() {
        System.out.println("Image class naeden? " + imageStatus);
        System.out.println("Src class naeden? " + srcStatus);
        System.out.println("Image class zakonchen? " + imageStatusDone);

    }

    public String get() {
        if (imageStatusDone && srcStatus) {
            return src;
        }
        return null;
    }

    public String pull() {
        if (imageStatusDone && srcStatus) {
            String pullSrc = src;
            reset();
            return pullSrc;
        }
        return null;
    }

    void reset() {
        src = null;
        isUsable = false;
        imageStatus = false;
        srcStatus = false;
        imageStatusDone = true;
    }


}

class Test {
    public static void main(String[] args) {
        ImageSrc src = new ImageSrc();
        List<String> image = new ArrayList<>();
        image.add("56435gfdh");
        image.add("  <img");
        image.add("");
        image.add("style=\"display: block;-webkit-user-select: none;margin: auto;cursor: zoom-in;background-color: hsl(0, 0%, 90%);transition: background-color 300ms;\"");
        //image.add("src=\"https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEgX98TKIsaJF7D4wnq7YBOuMjtYH-6D5Kgm7m7VbRek7cQIGN7TNVtJMDIbSiEG5KgcGyGpgGxEOz7u9v-WhQASrQrjvCQF8-RQ7PsZpA6djqK7RA7mXrnt6aYiac8voLef_mhP-s_TucPVEP1vvmUBjspmjA2RdrbvIqVwYXQJZ1fwPyamJIxXTrgMVmg/s1600/image1.png\"");
        image.add("src=\"https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEgX98TKIsaJF7D4wnq7YBOuMjtYH-6D5Kgm7m7VbRek");
        image.add("7cQIGN7TNVtJMDIbSiEG5KgcGyGpgGxEOz7u9v-WhQASrQrjvCQF8-RQ7PsZpA6djqK7RA7mXrnt6aYiac8voLef_mhP-s_TucPVEP1vvmUBjspm");
        image.add("jA2RdrbvIqVwYXQJZ1fwPyamJIxXTrgMVmg/s1600/image1.png\"");
        image.add("width=\"924\" height=\"924\"></img>");
        src.showInfo();
        for (int i = 0; i < image.size(); i++) {
            src.put(image.get(i));
        }
        System.out.println(src.get());
        src.put("gdsg");
        System.out.println("Postawil");
        System.out.println(src.pull());
        System.out.println(src.get());


    }
}
