package abueide.jtox.ui.jfx.controller;

import abueide.jtox.tox.data.Profile;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ChatView implements Initializable {

    @FXML
    TabPane tabPane;

    List<Profile> profiles;

    public ChatView(List<Profile> profiles) {
        this.profiles = profiles;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        for (Profile profile : profiles) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("abueide/jtox/ui/jfx/graphical/ProfileView.fxml"));
                loader.setController(new ProfileView(profile));
                Tab tabby = new Tab();
                tabby.setText(profile.getName());
                tabby.setContent(loader.load());
                tabPane.getTabs().add(tabby);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void display() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("abueide/jtox/ui/jfx/graphical/ChatView.fxml"));
        loader.setController(this);
        Parent root;
        try {
            root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("jTox");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
