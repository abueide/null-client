package abueide.nullclient.ui.jfx.controller;

import abueide.nullclient.data.Profile;
import com.gvaneyck.rtmp.ServerInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Andrew Bueide on 5/20/16.
 */
public class EditProfileController implements Initializable {

    private boolean newProfile = false;
    private Profile profile;

    @FXML
    TextField profileNameField;
    @FXML
    TextField statusField;
    @FXML
    PasswordField passwordField;
    @FXML
    ComboBox<ServerInfo> regionCombo;
    @FXML
    CheckBox savePasswordCheck;
    @FXML
    Button saveButton;
    @FXML
    Button cancelButton;

    public EditProfileController() {
        newProfile = true;
    }

    public EditProfileController(Profile profile) {
        this.profile = profile;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        ObservableList<ServerInfo> serverList = FXCollections.observableArrayList(ServerInfo.servers);

        regionCombo.setItems(serverList);
        regionCombo.getSelectionModel().select(0);


        statusField.setDisable(true);
        passwordField.setDisable(true);

        if (!newProfile) {
            profileNameField.setText(profile.getName());
            regionCombo.getSelectionModel().select(profile.getRegion());
            if (!profile.getPassword().isEmpty()) {
                passwordField.setDisable(false);
                passwordField.setText(profile.getPassword());
                savePasswordCheck.selectedProperty().set(true);
            }
        }
        savePasswordCheck.setOnAction(e -> passwordField.setDisable(!passwordField.isDisabled()));

        cancelButton.setOnAction(e -> cancelButton.getScene().getWindow().hide());

        saveButton.setOnAction(e -> save());

        profileNameField.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) save();
        });
        statusField.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) save();
        });
        passwordField.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) save();
        });
    }

    private void save() {
        String name = profileNameField.getText();
        String status = statusField.getText();
        String password;
        if (savePasswordCheck.isSelected()) {
            password = passwordField.getText();
        } else {
            password = "";
        }
        if (newProfile) {
            new Profile(name, password, status, regionCombo.getSelectionModel().getSelectedItem().region);
        } else {
            profile.setName(name);
            profile.setStatus(status);
            profile.setPassword(password);
            profile.setRegion(regionCombo.getSelectionModel().getSelectedItem().region);
        }
        saveButton.getScene().getWindow().hide();
    }

    public void display() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("abueide/nullclient/ui/jfx/view/EditProfile.fxml"));
        loader.setController(this);
        Parent root;
        try {
            root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Null Client Edit Profile");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
