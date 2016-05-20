package abueide.jtox.util;

import abueide.jtox.tox.data.Profile;
import abueide.jtox.util.database.DataBase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public static List<Profile> getProfiles() {
        List<Profile> profiles = new ArrayList<>();
        File dir = new File(Globals.PREF.get(Globals.PROFILE_DIR, null));
        for (File file : dir.listFiles((dir1, filename) -> {return filename.endsWith(".jtox");})) {
            profiles.add(new Profile(new DataBase(file.getAbsolutePath())));
        }
        return profiles;
    }

    public static DataBase createDataBase(String dir, String name, String ext){
        File file = new File(String.format("%s%s.%s", dir, name, ext));
        int i = 1;
        while(file.isFile()){
            file = new File(String.format("%s%s" + "(" + i + ")"+".%s", dir, name, ext));
            i++;
        }
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException e){
            e.printStackTrace();
        }

        return new DataBase(file.getAbsolutePath());
    }

}
