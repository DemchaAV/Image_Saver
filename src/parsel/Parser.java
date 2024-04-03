package parsel;

public class Parser extends BaseParser {
    private String src;
    private boolean wrap = false;

    /**
     * This class provide parser which can defined the first passage in line. The enter method is put, has
     * to different types, with starter position and enter line, and without
     * @param starter -  how our parts starts
     * @param ender   - String element how our pars should finish
     * @param wrap    - if you want starter and ender in your out for example
     *                new Parser("start","finish",true)
     *                inPut "I start this game and will finish in an hour!"
     *                outPut "start this game and will finish"
     */
    public Parser(String starter, String ender, boolean wrap) {
        super(starter, ender);
        this.wrap = wrap;
    }

    public Parser(String starter, String ender) {
        super(starter, ender);
    }

    @Override
    public boolean execute() {
        if (startStatus) {
            parserStart();
            return doneStatus;
        } else {
            if (contains(starter)) {
                parserStart();
                return doneStatus;
            } else {
                currentPosition = -1;
                return doneStatus;
            }
        }
    }

    /**
     * Determines our starting point for src definition
     * or searches for the end if a start point has been defined
     * if we did not have a start status when calling method e, or the method fails
     */
    private void parserStart() {
        if (!startStatus && !contains(starter)) {
            return;
        }
        if (startStatus) {
            doneStatus = difendingEnd();
        } else {
            if (startStatus = contains(starter)) {
                startPosition = indexFrom(starter);
                currentPosition = startPosition;
                doneStatus = difendingEnd();
            }
        }
    }

    /**
     * determine the termination of our src if it was previously found, and
     *
     * @return
     */
    private boolean difendingEnd() {
        if (contains(ender)) {
            endPosition = line.indexOf(ender, currentPosition);
            src = src == null ? line.substring(startPosition, endPosition) : (src + line.substring(startPosition, endPosition));
            currentPosition = endPosition;
            return doneStatus = true;
        } else {
            src = src == null ? line.substring(startPosition) : src + line.substring(startPosition);
            currentPosition = 0;
            startPosition = 0;
            return doneStatus;
        }
    }

    public String pull() {
        if (startStatus && doneStatus) {
            String pullSrc = src;
            resetAll();
            src = null;

            return wrap ? starter + pullSrc + ender : pullSrc;
        } else {
            return null;
        }
    }

}