package abueide.jtox.util;

import java.util.prefs.Preferences;

public class Globals {

    //Preference Keys
    public static final String APPDATA_DIR = "APPDATA_DIR";
    public static final String PROFILE_DIR = "PROFILE_DIR";
    public static final String JTOX_NODE = "/abueide/jTox";
    public static final String DB_EXT = "jtox";

    public static final Preferences PREF = Preferences.userRoot().node(JTOX_NODE);

    //SQLite Statements
    public static String createTableStatement() {
        return "create table if not exists profile("
                + "name text,"
                + "status text,"
                + "public_key text,"
                + "private_key text"
                + ");"

                + "create table if not exists friends("
                + "id integer primary key,"
                + "name text,"
                + "alias text,"
                + "status text,"
                + "public_key text"
                + ");"

                + "create table if not exists messages ("
                + "timestamp datetime default current_timestamp,"
                + "sender text,"
                + "receiver text,"
                + "message text,"
                + "sent integer"
                + ");";
    }
}
