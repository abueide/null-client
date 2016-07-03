package abueide.nullclient.util;

import java.util.prefs.Preferences;

/**
 * Created by Andrew Bueide on 5/20/16.
 */
public class Globals {

    //Preference Keys
    public static final String APPDATA_DIR = "APPDATA_DIR";
    public static final String PROFILE_DIR = "PROFILE_DIR";
    public static final String nullclient_NODE = "/abueide/nullclient";
    public static final String DB_EXT = "sqlite";
    public static final String LEAGUE_CLIENT_VERSION = "6.13.xx";

    public static final Preferences PREF = Preferences.userRoot().node(nullclient_NODE);

    //SQLite Statements
    public static String createTableStatement() {
        return "create table if not exists profile("
                + "name text,"
                + "password text,"
                + "status text,"
                + "region text"
                + ");"

                + "create table if not exists friends("
                + "id integer primary key,"
                + "name text,"
                + "alias text,"
                + "status text"
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
