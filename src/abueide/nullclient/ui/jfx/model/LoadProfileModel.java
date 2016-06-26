package abueide.nullclient.ui.jfx.model;

import abueide.nullclient.data.Profile;
import abueide.nullclient.util.Util;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew Bueide on 6/23/16.
 */
public class LoadProfileModel {

    private ListProperty<String> profileDirsProperty = new SimpleListProperty<>();
    private ListProperty<Profile> profilesProperty = new SimpleListProperty<>();

    public LoadProfileModel(String profileDir) {
        profileDirsProperty = new SimpleListProperty<String>(FXCollections.observableArrayList());
        profileDirsProperty.addListener((observable, oldValue, newValue) -> updateProfiles());
        profilesProperty = new SimpleListProperty<Profile>(FXCollections.observableArrayList());
        setDir(profileDir);
    }

    public LoadProfileModel(List<String> profileDirs) {
        profileDirsProperty = new SimpleListProperty<String>(FXCollections.observableArrayList());
        profileDirsProperty.addListener((observable, oldValue, newValue) -> updateProfiles());
        profilesProperty = new SimpleListProperty<Profile>(FXCollections.observableArrayList());
        setDirs(profileDirs);
    }

    public ListProperty<String> getProfileDirsProperty() {
        return profileDirsProperty;
    }

    public ListProperty<Profile> getProfilesProperty() {
        return profilesProperty;
    }

    public void setDir(String dir) {
        List<String> dirList = new ArrayList<>();
        dirList.add(dir);
        profileDirsProperty.set(FXCollections.observableArrayList(dirList));
    }

    public void setDirs(List<String> dirs) {
        profileDirsProperty.set(FXCollections.observableArrayList(dirs));
    }

    public void addDir(String dir) {
        profileDirsProperty.add(dir);
    }

    public void delDir(String dir) {
        profileDirsProperty.remove(dir);
    }

    public void addProfile(Profile profile) {
        profilesProperty.add(profile);
    }

    public void delProfile(Profile profile) {
        profilesProperty.remove(profile);
    }

    public void updateProfiles() {
        profilesProperty.clear();
        for (String string : profileDirsProperty.get()) {
            Util.getProfiles(string).forEach(this::addProfile);
        }
    }
}
