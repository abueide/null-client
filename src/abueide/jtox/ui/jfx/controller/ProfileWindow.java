package abueide.jtox.ui.jfx.controller;

import abueide.jtox.tox.data.Profile;
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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileWindow implements Initializable {

    private boolean newProfile = false;
    private Profile profile;

    @FXML
    TextField profileNameField;
    @FXML
    TextField statusField;
    @FXML
    PasswordField encryptionKey;
    @FXML
    Button saveButton;
    @FXML
    Button cancelButton;

    public ProfileWindow() {
        newProfile = true;
    }

    public ProfileWindow(Profile profile) {
        this.profile = profile;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        if(!newProfile){
            profileNameField.setText(profile.getName());
        }

        encryptionKey.setDisable(true);
        encryptionKey.setPromptText("Not Available");

        cancelButton.setOnAction(e -> cancelButton.getScene().getWindow().hide());

        saveButton.setOnAction(e -> save());

        profileNameField.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) save();
        });
        statusField.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) save();
        });
        encryptionKey.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) save();
        });
    }

    private void save() {
        String name = profileNameField.getText();
        String status = statusField.getText();
        if(newProfile){
            new Profile(name, status);
        }else {
            profile.setName(name);
            profile.setStatus(status);
        }
        saveButton.getScene().getWindow().hide();
    }

    public void display() {
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
    }

}
