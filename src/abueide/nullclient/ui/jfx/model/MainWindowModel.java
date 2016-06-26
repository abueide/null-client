package abueide.nullclient.ui.jfx.model;

import abueide.nullclient.data.Profile;
import abueide.nullclient.util.javafx.ProfileTab;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.util.List;

/**
 * Created by Andrew Bueide on 6/23/16.
 */
public class MainWindowModel {

    private ListProperty<ProfileTab> profileTabs;

    public MainWindowModel(List<Profile> profiles) {
        this.profileTabs = new SimpleListProperty<ProfileTab>(FXCollections.observableArrayList());
        for (Profile profile : profiles) {
            ProfileTab tab = new ProfileTab(profile);
            tab.setOnClosed(e -> {
                disconnectTab(tab);
            });
            this.addTab(tab);
        }
    }

    public void addTab(ProfileTab tab) {
        this.profileTabs.add(tab);
    }

    public void deleteTab(ProfileTab tab) {
        this.profileTabs.remove(tab);
    }

    public void disconnectTab(ProfileTab tab) {
        tab.disconnect();
        this.deleteTab(tab);
    }

    public ListProperty<ProfileTab> getTabs() {
        return this.profileTabs;
    }
}
