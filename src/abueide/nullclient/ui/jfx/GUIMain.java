package abueide.nullclient.ui.jfx;

import abueide.nullclient.ui.jfx.controller.LoadProfileController;
import abueide.nullclient.ui.jfx.model.LoadProfileModel;
import abueide.nullclient.util.Globals;
import abueide.nullclient.util.Util;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew Bueide on 5/20/16.
 */
public class GUIMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //Startup stuff
        List<String> dirList = new ArrayList<>();
        dirList.add(Globals.PREF.get(Globals.PROFILE_DIR, null));
        new LoadProfileController(new LoadProfileModel(dirList)).launch(primaryStage);
    }

    @Override
    public void init() throws Exception {
        //Check for preferences, set them to default if they don't exist
        Globals.PREF.clear();
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
