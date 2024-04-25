package parsel;

/**
 * this class to check your address if it has a truncated form with the pages of the processed supplement
 * your reference
 * */
public class URL_Corrector {
    private static String webUrl;

    public URL_Corrector(String webUrl) {
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
