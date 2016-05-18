package abueide.jtox.ui.jfx;

import abueide.jtox.ui.jfx.controller.ProfileSelectionWindow;
import abueide.jtox.util.Globals;
import abueide.jtox.util.database.DataBase;
import javafx.application.Application;
import javafx.stage.Stage;
import abueide.jtox.ui.jfx.controller.ProfileSelectionWindow;
import abueide.jtox.util.Globals;
import abueide.jtox.util.Util;
import abueide.jtox.util.database.DataBase;

import java.io.File;

public class GUIMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //Startup stuff
        new ProfileSelectionWindow(Globals.jtoxdb.getProfiles()).launch(primaryStage);
    }

    @Override
    public void init() throws Exception {
        //Check for preferences, set them to default if they don't exist
        if (Globals.PREF.get(Globals.APPDATA_DIR, null) == null) {
            Globals.PREF.put(Globals.APPDATA_DIR, Util.getAppData());
        }
        if (Globals.PREF.get(Globals.JTOX_DB, null) == null) {
            Globals.PREF.put(Globals.JTOX_DB, Globals.PREF.get(Globals.APPDATA_DIR, null) + "jTox.sqlite3");
        }

        //Get app data directory from preferences, return default if it doesn't exist.
        File appDir = new File(Globals.PREF.get(Globals.APPDATA_DIR, null));

        //Create app data directory if it doesn't exist
        if (!appDir.exists()) {
            try {
                appDir.mkdirs();
            } catch (SecurityException se) {
                System.out.println("Security Exception: Could not create app data folder.");
            }
        }
        Globals.jtoxdb = new DataBase(Globals.PREF.get(Globals.JTOX_DB, null));
    }

    @Override
    public void stop() {
        Globals.jtoxdb.close();
    }

}
