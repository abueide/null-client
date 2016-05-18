package abueide.jtox.util;

public class Util {

    public static String getAppData() {
        String osName = System.getProperty("os.name").toUpperCase();
        String appDataDir = System.getProperty("user.dir");

        if (osName.contains("WIN"))
            appDataDir = System.getenv("APPDATA");

        else if (osName.contains("MAC"))
            appDataDir = System.getProperty("user.home") + "/Library/Application Support/";

        else if (osName.contains("NUX"))
            appDataDir = System.getProperty("user.home") + "/.config/";

        return appDataDir + "jTox/";
    }

}
