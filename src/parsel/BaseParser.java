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
     * Это входной метод для запуски нашего класса и вычесления, и определения наших ссылок
     * src
     *
     * @param line входные данный для работы нашего класса
     * @return возращает true если обнаружен img класс и false если клас был закрыт ">"
     */
    public boolean put(String line) {
        return put(0, line);
    }

    /**
     * метод для работы с текущей позиции
     *
     * @param line            - обрабатываемая линия
     * @param currentPosition - текущая позиция
     * @return возращает выполняемый implement метод execute
     */
    public boolean put(String line, int currentPosition) {
        return put(currentPosition, line);
    }

    /**
     * метод для работы с текущей позиции
     *
     * @param line            - обрабатываемая линия
     * @param currentPosition - текущая позиция
     * @return возращает выполняемый implement метод execute
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
     * Мтод относиться к Entery методу put который при переопределению данный выполняет
     * обработку условий
     *
     * @return возвращейт true если метод нашел и полностью определил нашу искомый блок
     */
    abstract public boolean execute();

    /**
     * Метода ищет совпадение нашей строки  искомого елемента с текущей позиции в загруженой строки класса с помощью метода
     * put, зачение будет за искомым эелементо тоесть  элемент indexOf = "как"
     * строка line = "Привет как дела!" значемение return в даном слущаи будет 10 сразу после слова "как"
     *
     * @param indexOf - элемент за которым мы хотим узнать индекс
     * @return елси элемент отсутствует в нашей line может вернуть  отризательное значение -1;
     */
    int indexFrom(String indexOf) {
        if (line.indexOf(indexOf, currentPosition) == -1) {
            return -1;
        }
        return line.indexOf(indexOf, currentPosition) + indexOf.length();
    }

    /**
     * Возращает нам линию с тикущей позицией обработки в строке
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
     * перед запуском следуюет забрать текущее положения в строки
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
     * метод принимает искомый аргумент в качестве String и возращает tue если он присутствует
     * в обрабатываемой строке с текущей позиции
     *
     * @param str
     * @return true если присутствуе false  в противном случаи
     */
    protected boolean contains(String str) {
        return getLine().contains(str);
    }
}
