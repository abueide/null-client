package abueide.jtox.util;

import abueide.jtox.tox.Profile;
import abueide.jtox.util.database.DataBase;

import java.util.prefs.Preferences;

public class Globals {

    //Preference Keys
    public static final String APPDATA_DIR = "APPDATA_DIR";
    public static final String JTOX_DB = "JTOX_DB";
    public static final String JTOX_NODE = "/abueide/jTox";

    public static final Preferences PREF = Preferences.userRoot().node(JTOX_NODE);

    public static DataBase jtoxdb;

    //SQLite Statements
    public static String createTableStatement() {
        return "create table if not exists profiles("
                + "id integer primary key,"
                + "name text,"
                + "encryption_key text);"
                + "create table if not exists messages ("
                + "id integer primary key,"
                + "message text);";
    }

    public static String insertProfile(Profile profile) {
        return "insert into profiles (name, encryption_key) values('" +
                profile.getName() +
                "', '" +
                profile.getEncryptionKey() +
                "');";
    }

    public static String editProfile(Profile profile) {
        return "UPDATE profiles SET name = '" + profile.getName() + "', encryption_key = '" + profile.getEncryptionKey() + "' WHERE id = " + profile.getuid() + ";";
    }

    public static String deleteProfile(Profile profile) {
        return "DELETE FROM profiles WHERE id=" + profile.getuid() + ";";
    }
}
