package parsel;

abstract class BaseParser {
    public String line;
    protected int currentPosition = 0;
    protected int lineSize;
    protected int startPosition;
    protected int endPosition;
    protected boolean startStatus = false;
    protected boolean doneStatus = false;
    protected String starter;
    protected String ender;

    public BaseParser(String starter, String ender) {
        this.starter = starter;
        this.ender = ender;
    }

    /**
     * This is the input method to run our class and calculate and define our references
     * src
     *
     * @param line input given to run our class
     * @return returns true if img class is detected and false if the class was closed ">"
     */
    public boolean put(String line) {
        return put(0, line);
    }

    /**
     * method for working from the current position
     *
     * @param line - line to be processed
     * @param currentPosition - current position
     * @return returns the execute method to be executed
     */
    public boolean put(String line, int currentPosition) {
        return put(currentPosition, line);
    }

    /**
     * method for working from the current position
     *
     * @param line - line to be processed
     * @param currentPosition - current position
     * @return returns the execute method to be executed
     */
    public boolean put(int currentPosition, String line) {
        if (currentPosition == -1 || line == null || currentPosition >= line.length()) {
            return false;
        }
        this.line = line;
        this.currentPosition = currentPosition;
        this.lineSize = line.length();
        return execute();
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    /**
     * Mtod refers to Entery method put which, when overridden, performs
     * condition handling
     *
     * @return returns true if the method has found and fully defined our sought block
     */
    abstract public boolean execute();

    /**
     * method searches for a match of our string of the searched element with the current position in the loaded class string using the method
     * put, the value will be after the searched element i.e. indexOf element = "as"
     * line line = "Hi how are you!" the return value in this case will be 10 right after the word "as".
     *
     * @param indexOf - element for which we want to know the index
     * @return if the element is missing in our line can return the value -1;
     */
    int indexFrom(String indexOf) {
        if (line.indexOf(indexOf, currentPosition) == -1) {
            return -1;
        }
        return line.indexOf(indexOf, currentPosition) + indexOf.length();
    }

    /**
     * Returns us the line with the ticking position of the processing in the string
     *
     * @return
     */
    private String getLine() {
        if (currentPosition < line.length() && currentPosition > -1) {
            return line.substring(currentPosition);
        } else {
            return "";
        }
    }

    /**
     * before startup it is necessary to get the current position in strings
     *
     * @ getCurrentPosition();
     */
    protected void resetAll() {
        line = null;
        currentPosition = 0;
        lineSize = 0;
        startPosition = 0;
        endPosition = 0;
        startStatus = false;
        doneStatus = false;

    }

    /**
     * method accepts the searched argument as String and returns tue if it is present
     * in the processed string from the current position
     *
     * @param str
     * @return true if present false otherwise
     */
    protected boolean contains(String str) {
        return getLine().contains(str);
    }
}
