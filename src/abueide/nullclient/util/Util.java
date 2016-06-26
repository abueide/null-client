package abueide.nullclient.util;

import abueide.nullclient.data.Profile;
import abueide.nullclient.util.database.DataBase;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew Bueide on 5/20/16.
 */
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

        return appDataDir + "abueide/nullclient/";
    }

    public static List<Profile> getProfiles(String path) {
        List<Profile> profiles = new ArrayList<>();
        File dir = new File(path);
        for (File file : dir.listFiles((dir1, filename) -> {
            return filename.endsWith(Globals.DB_EXT);
        })) {
            profiles.add(new Profile(new DataBase(file.getAbsolutePath())));
        }
        return profiles;
    }

    public static DataBase createDataBase(String dir, String name) {
        File file = new File(String.format("%s%s.%s", dir, name, Globals.DB_EXT));
        int i = 1;
        while (file.isFile()) {
            file = new File(String.format("%s%s" + "(" + i + ")" + ".%s", dir, name, Globals.DB_EXT));
            i++;
        }
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new DataBase(file.getAbsolutePath());
    }

    public static DataBase createProfile(String name, String password, String status, String region) {
        DataBase d = Util.createDataBase(Globals.PREF.get(Globals.PROFILE_DIR, null), name);
        try {
            PreparedStatement stmt = d.getConnection().prepareStatement("insert into profile (name, password, status, region) values(?, ?, ?, ?)");
            stmt.setString(1, name);
            stmt.setString(2, password);
            stmt.setString(3, status);
            stmt.setString(4, region);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return d;
    }
}
