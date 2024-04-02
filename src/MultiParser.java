import java.util.ArrayList;
import java.util.List;

public class MultiParser {
    private List<String> parsList;
    private Parser parser;
    private int lineSize;
    private boolean doneStatus = false;

    public MultiParser(Parser parser) {
        this.parser = parser;
        this.parsList = new ArrayList<>();
    }

    public MultiParser(String starter, String ender, boolean wrap) {
        this(new Parser(starter, ender, wrap));
    }

    public MultiParser(String starter, String ender) {
        this(new Parser(starter, ender));

    }

    public boolean put(String line) {
        return put(line, 0);
    }

    public boolean put(String line, int currentPosition) {
        if (line == null || currentPosition == -1 || currentPosition > line.length() || line.length() == 0) {
            return doneStatus = true;
        }
        if (!doneStatus) {
            execute(line, currentPosition);
        }
        return doneStatus;
    }

    private boolean execute(String line, int currentPosition) {
        while (!doneStatus) {
            parser.put(currentPosition, line);
            if (parser.doneStatus) {
                int cur = parser.getCurrentPosition();
                parsList.add(parser.pull());
                return put(line, cur);
            } else {
                doneStatus = parser.startStatus;
                return doneStatus;

            }
        }
        return doneStatus;
    }

    boolean status() {
        return doneStatus;
    }

    public List<String> pull() {
        List<String> pullParsList = new ArrayList<>(this.parsList);
        doneStatus = false;
        this.parsList.clear();
        return pullParsList;
    }
}

