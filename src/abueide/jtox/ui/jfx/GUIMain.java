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
        new ProfileSelectionWindow(Util.getProfiles()).launch(primaryStage);
    }

    @Override
    public void init() throws Exception {
        //Check for preferences, set them to default if they don't exist
        Globals.PREF.remove(Globals.PROFILE_DIR);
        if (Globals.PREF.get(Globals.APPDATA_DIR, null) == null) {
            Globals.PREF.put(Globals.APPDATA_DIR, Util.getAppData());
        }
        if (Globals.PREF.get(Globals.PROFILE_DIR, null) == null) {
            Globals.PREF.put(Globals.PROFILE_DIR, Globals.PREF.get(Globals.APPDATA_DIR, null) + "profiles/");
        }

        //Get app data directory from preferences, return default if it doesn't exist.
        File appDir = new File(Globals.PREF.get(Globals.APPDATA_DIR, null));
        File profileDir = new File(Globals.PREF.get(Globals.PROFILE_DIR, null));

        //Create app data directory if it doesn't exist
        if (!appDir.exists()) {
            try {
                appDir.mkdirs();
            } catch (SecurityException se) {
                System.out.println("Security Exception: Could not create app data folder.");
            }
        }
        if (!profileDir.exists()) {
            try {
                profileDir.mkdirs();
            } catch (SecurityException se) {
                System.out.println("Security Exception: Could not create app data folder.");
            }
        }

        }

    @Override
    public void stop() {

    }

}
