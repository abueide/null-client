package abueide.jtox.ui.jfx.controller;

/**
 * Created by gratin on 5/20/16.
 */

import abueide.jtox.tox.data.Friend;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FriendWindow implements Initializable {

    private Friend friend;

    @FXML
    TextField aliasField;
    @FXML
    TextField publicKeyField;
    @FXML
    Button clearLogsButton;
    @FXML
    Button resendFriendRequestButton;
    @FXML
    Button cancelButton;

    @FXML
    Button saveButton;

    public FriendWindow(Friend friend) {
        this.friend = friend;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        aliasField.setText(friend.getAlias());
        publicKeyField.setText(friend.getPublicKey());

        cancelButton.setOnAction(e -> cancelButton.getScene().getWindow().hide());

        saveButton.setOnAction(e -> save());

        aliasField.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) save();
        });
        publicKeyField.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) save();
        });
    }

    private void save() {
        friend.setAlias(aliasField.getText());
        friend.setPublicKey(publicKeyField.getText());
        saveButton.getScene().getWindow().hide();
    }

    public Friend display() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("abueide/jtox/ui/jfx/graphical/FriendWindow.fxml"));
        loader.setController(this);
        Parent root;
        try {
            root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("jTox - Edit Contact");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return friend;
    }
}

