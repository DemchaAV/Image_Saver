package parsel;

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
     * We use this constructor if the closing track can be warehoused by several types
     *
     * @param starter opening tag
     * @param ender trekking track for example full
     * @param enderShort closing tag for example short
     *   In-line parser with standard started ender
     */
    public ImageParser(String starter, String ender, String enderShort) {
        super(starter, ender);
        this.parser = new Parser("src=\"", "\"");
        this.enderShort = enderShort;
    }

    /**
     * We use this constructor if the closing track can be warehoused by several types
     *
     * @param starter opening tag
     * @param ender trekking track for example full
     * @param enderShort closing tag for example short
     * @param parser Internal parser if we want to par inside image block
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
        if (startStatus = contains(starter)) {
            currentPosition = indexFrom(starter);
        }
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
     * this method if there is a reference
     * src adds a link to our list, resets the current state of our src = null;
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
     * This method, unlike the pull method, returns the data of our src references if they
     * exist, as well as null if the class did not finish its work on the string before reaching the end, or
     * the reference was not found during the work
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
     * Method that returns our src references if they exist, may return null if
     * references are not found or the programme has not reached the end of our line, and also
     * resets all the given references to the initial state for further work on a new line.
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

