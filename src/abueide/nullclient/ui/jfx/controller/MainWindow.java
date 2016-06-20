package abueide.nullclient.ui.jfx.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import abueide.nullclient.data.Profile;

public class MainWindow implements Initializable {

    @FXML
    TabPane tabPane;

    @FXML
    Label username;
    @FXML
    Label rp;
    @FXML
    Label ip;

    List<Profile> profiles;

    public MainWindow(List<Profile> profiles) {
        this.profiles = profiles;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        for (Profile profile : profiles) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("abueide/nullclient/ui/jfx/graphical/ProfileTab.fxml"));
                loader.setController(new ProfileTab(profile));
                Tab tab = new Tab();
                tab.setContent(loader.load());
                tab.setText(profile.getName());
                tabPane.getTabs().add(tab);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void display() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("abueide/nullclient/ui/jfx/graphical/MainWindow.fxml"));
        loader.setController(this);
        Parent root;
        try {
            root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Null Client");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
