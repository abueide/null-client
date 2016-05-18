package abueide.jtox.ui.jfx.controller;

import abueide.jtox.tox.Profile;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import abueide.jtox.tox.Profile;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileWindow implements Initializable {

    private Profile profile;

    @FXML
    TextField profileName;
    @FXML
    PasswordField encryptionKey;
    @FXML
    Button saveButton;
    @FXML
    Button cancelButton;

    public ProfileWindow() {
        this.profile = new Profile();
    }

    public ProfileWindow(Profile profile) {
        this.profile = profile;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        profileName.setText(profile.getName());
        profileName.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) save();
        });
        encryptionKey.setDisable(true);
        encryptionKey.setPromptText("Not Available");
        encryptionKey.setText(profile.getEncryptionKey());
        encryptionKey.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) save();
        });
        cancelButton.setOnAction(e -> cancelButton.getScene().getWindow().hide());
        saveButton.setOnAction(e -> save());
    }

    private void save() {
        profile.setName(profileName.getText());
        profile.setEncryptionKey(encryptionKey.getText());
        saveButton.getScene().getWindow().hide();
    }

    public Profile display() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("abueide/jtox/ui/jfx/graphical/ProfileWindow.fxml"));
        loader.setController(this);
        Parent root;
        try {
            root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("jTox - Edit Profile");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return profile;
    }

}
