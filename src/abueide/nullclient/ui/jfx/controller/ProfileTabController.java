package abueide.nullclient.ui.jfx.controller;

import abueide.nullclient.data.Profile;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Andrew Bueide on 5/16/16.
 */
public class ProfileTabController implements Initializable {

    @FXML
    HBox contentPane;
    @FXML
    Button homeButton;
    @FXML
    Button profileButton;
    @FXML
    Button playButton;
    @FXML
    Button storeButton;

    @FXML
    Label username;
    @FXML
    Label rp;
    @FXML
    Label ip;

    Profile profile;

    FXMLLoader loader;

    public ProfileTabController(Profile profile) {
        this.profile = profile;
        this.profile.connectAndLogin();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        openChat();

        username.setText(profile.getName());
        rp.setText("RP: " + profile.getSummoner().getDouble("rpBalance").intValue());
        ip.setText("IP: " + profile.getSummoner().getDouble("ipBalance").intValue());

        homeButton.setOnAction(e -> openChat());
        storeButton.setOnAction(e -> openStore());
    }

    private void openChat() {
        try {
            loader = new FXMLLoader(getClass().getClassLoader().getResource("abueide/nullclient/ui/jfx/view/ChatView.fxml"));
            loader.setController(new ChatViewController(profile));
            contentPane.getChildren().clear();
            contentPane.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openStore() {
        try {
            int id = profile.getClient().invoke("loginService", "getStoreUrl", new Object[]{});
            loader = new FXMLLoader(getClass().getClassLoader().getResource("abueide/nullclient/ui/jfx/view/StoreView.fxml"));
            String storeURL = profile.getClient().getResult(id).getTO("data").getString("body");
            loader.setController(new StoreViewController(storeURL));
            contentPane.getChildren().clear();
            contentPane.getChildren().add(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
