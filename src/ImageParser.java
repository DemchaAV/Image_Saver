import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class ImageParser extends BaseParser {
    private final String enderShort;
    private List<String> srcList;
    private Parser parser;

    public ImageParser() {
        this("<img", "</img>", ">");
    }
    /**
     * данный метод используем если мы знаем что теги будут заканчиваться двумя тегами
     *
     * @param starter открывающий тег
     * @param ender   закрывающий тег
     */
    public ImageParser(String starter, String ender) {
        this(starter, ender, null);
    }

    /**
     * Используем данный конструктор если закрывающий трек может варироваться нескольких видов
     *
     * @param starter    открывающий тег
     * @param ender      зкрывающий трек например полный
     * @param enderShort закрывающий тег например короткий
     *                   Внутринний парсер с стандартным started ender
     */
    public ImageParser(String starter, String ender, String enderShort) {
        super(starter, ender);
        this.parser = new Parser("src=\"", "\"");
        this.enderShort = enderShort;
    }

    /**
     * Используем данный конструктор если закрывающий трек может варироваться нескольких видов
     *
     * @param starter    открывающий тег
     * @param ender      зкрывающий трек например полный
     * @param enderShort закрывающий тег например короткий
     * @param parser     Внутринний парсер если мы хотим парсить внутри блока image
     */
    public ImageParser(String starter, String ender, String enderShort, Parser parser) {
        super(starter, ender);
        this.parser = parser;
        this.enderShort = enderShort;
    }


    @Override
    public boolean execute() {
        if (startStatus) {
            return handingStart();
        } else {
            return handingNotStart();
        }
    }

    private boolean handingNotStart() {
        startStatus = contains(starter);
        if (startStatus) {
            return handingStart();
        } else {
            return false;
        }
    }

    private boolean handingStart() {
        if (parser.doneStatus) {
            currentPosition = parser.currentPosition;
            addSrc(parser.pull());
            return doneStatus;
        } else {
            if(!startStatus){
            currentPosition = indexFrom(starter);}
            parser.put(currentPosition, line);
            if (parser.doneStatus){
                currentPosition = parser.currentPosition;
                addSrc(parser.pull());
                startStatus = false;
                put(currentPosition,line);
            }
            return doneStatus;
        }
    }

    /**
     * данный метод при наличии ссылку
     * src добавляет в наш список ссылк, так же сбрасывает текущие состояние нашей src = null;
     */
    private void addSrc(String src) {
        if (srcList == null) {
            srcList = new ArrayList<>();
            srcList.add(src);
        } else {
            srcList.add(src);
        }
    }

    /**
     * Данный метод в отличии от метода pull возращает нам данные нашиш ссылок src если они
     * существую, так же null если класс не завершил свою работу над строкой не дойдя до конца, или
     * при работе не было найдена ссылка
     *
     * @return
     */
    public List<String> get() {
        if (doneStatus) {
            return srcList;
        }
        return null;
    }

    /**
     * Метод который возращает наши искомы ссылки src если они существуют, может вернуть null если
     * ссылки не найденны или программа не дошла до конца нашей строки, а так же
     * сбрасывает все данный до исходного состояния для последущей работы над новой строкой
     *
     * @return
     */
    public List<String> pull() {
        if (srcList != null) {
            List<String> pullSrcList = srcList;
            resetAll();
            srcList = null;
            return pullSrcList;
        } else {
            return null;
        }

    }

}

/**
 * этот клас для проверки вашего адресса если она имеет обрезанную форму с страници обрабатываемой дополняет
 * вашу ссылку
 */
class URL_Checker {
    private static  String webUrl;

    public URL_Checker(String webUrl) {
        this.webUrl = webUrl;
    }
    public  String getAbsolutURl(String pathOut) {
        String absolutPath;
        pathOut = pathOut.replace('\\', '/');
        final String wwww = "www.";
        final String https = "https://";
        absolutPath = pathOut.startsWith("//") ? pathOut.substring(2) : pathOut;
        absolutPath =absolutPath.startsWith("/") ?absolutPath.substring(1) : absolutPath;
        absolutPath = absolutPath.startsWith(wwww) ? https + absolutPath : absolutPath;
        absolutPath = absolutPath.startsWith(https) ? absolutPath : webUrl + absolutPath;

        return absolutPath;
    }
}
