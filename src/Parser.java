import java.util.ArrayList;
import java.util.List;

public class Parser extends BaseParser {
    private String src;
    private boolean wrap = false;

    /**
     * This class provide parser  enter method is put, has to different types, with starter position, and enter line and without
     *
     * @param starter -  how our parts starts
     * @param ender   - String element how our pars should finish
     * @param wrap    - if you want you starter and ender in your out
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
     * Определяет нашу стартовую точку для определения src
     * или ищет концовку если если стартовая точка была определена
     * если при вызове метода e у нас не был старт статус, или не  метода не срабатывает
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
     * определяем окончание нашего src если он был предварительно найден, и
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