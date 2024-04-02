package parsel;

/**
 * этот клас для проверки вашего адресса если она имеет обрезанную форму с страници обрабатываемой дополняет
 * вашу ссылку
 */
public class URL_Checker {
    private static String webUrl;

    public URL_Checker(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getAbsolutURl(String pathOut) {
        String absolutPath;
        pathOut = pathOut.replace('\\', '/');
        final String wwww = "www.";
        final String https = "https://";
        absolutPath = pathOut.startsWith("//") ? pathOut.substring(2) : pathOut;
        absolutPath = absolutPath.startsWith("/") ? absolutPath.substring(1) : absolutPath;
        absolutPath = absolutPath.startsWith(wwww) ? https + absolutPath : absolutPath;
        absolutPath = absolutPath.startsWith(https) ? absolutPath : webUrl + absolutPath;
        String imageFormat;
        if (absolutPath.indexOf(".") < absolutPath.length() - 6) {
            imageFormat = absolutPath.substring(absolutPath.lastIndexOf('.'));
            if (absolutPath.substring(0, absolutPath.length() - imageFormat.length()).contains(imageFormat)) {
                absolutPath = absolutPath.substring(0, absolutPath.length() - imageFormat.length());
                absolutPath = absolutPath.substring(0, absolutPath.lastIndexOf(imageFormat) + imageFormat.length());
            }
        }

        return absolutPath;
    }
}
