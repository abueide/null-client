package abueide.nullclient.ui.jfx.controller;

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

import abueide.nullclient.util.Globals;
import abueide.nullclient.util.Util;

public class ProfileSelectionSettingsWindow implements Initializable {

    @FXML
    Button dirButton1;
    @FXML
    Button dirButton2;
    @FXML
    Button cancelButton;
    @FXML
    Button defaultButton;
    @FXML
    Button saveButton;
    @FXML
    TextField appDataDir;
    @FXML
    TextField databaseDir;


    public ProfileSelectionSettingsWindow() {
        display();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        appDataDir.setText(Globals.PREF.get(Globals.APPDATA_DIR, null));
        appDataDir.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) save();
        });
//        databaseDir.setText(Globals.PREF.get(Globals.nullclient_DB, null));
        databaseDir.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) save();
        });
        dirButton1.setOnAction(e -> {
        });
        dirButton2.setOnAction(e -> {
        });
        cancelButton.setOnAction(e -> cancelButton.getScene().getWindow().hide());
        defaultButton.setOnAction(e -> {
            appDataDir.setText(Util.getAppData());
            databaseDir.setText(Util.getAppData() + "nullclient.sqlite3");
        });
        saveButton.setOnAction(e -> save());
    }

    private void save(){
        Globals.PREF.put(Globals.APPDATA_DIR, appDataDir.getText());
//        Globals.PREF.put(Globals.nullclient_DB, databaseDir.getText());
//        Globals.nullclientdb.close();
//        Globals.nullclientdb = new DataBase(Globals.PREF.get(Globals.nullclient_DB, null));
        saveButton.getScene().getWindow().hide();
    }

    private void display() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("abueide/nullclient/ui/jfx/graphical/ProfileSelectionSettingsWindow.fxml"));
        loader.setController(this);
        Parent root;
        try {
            root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Null Client Profile Selection Settings");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
