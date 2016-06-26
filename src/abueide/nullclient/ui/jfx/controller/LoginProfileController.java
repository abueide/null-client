package abueide.nullclient.ui.jfx.controller;

import abueide.nullclient.data.Profile;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Andrew Bueide on 6/23/16.
 */
public class LoginProfileController implements Initializable {

    @FXML
    Label usernameLabel;
    @FXML
    PasswordField passwordField;
    @FXML
    CheckBox savePasswordCheck;
    @FXML
    Button loginButton;

    Profile profile;

    public LoginProfileController(Profile profile) {
        this.profile = profile;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameLabel.setText(profile.getName() + " - " + profile.getRegion().region);
        loginButton.setOnAction(e -> {

        });
    }
}
