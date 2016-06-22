package abueide.nullclient.ui.jfx.controller;

import abueide.nullclient.data.Message;
import abueide.nullclient.data.Profile;
import abueide.nullclient.util.Globals;
import com.github.theholywaffle.lolchatapi.ChatMode;
import com.github.theholywaffle.lolchatapi.ChatServer;
import com.github.theholywaffle.lolchatapi.FriendRequestPolicy;
import com.github.theholywaffle.lolchatapi.LolChat;
import com.github.theholywaffle.lolchatapi.listeners.ChatListener;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;
import com.gvaneyck.rtmp.LoLRTMPSClient;
import com.gvaneyck.rtmp.encoding.TypedObject;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.jivesoftware.smack.Chat;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by Andrew Bueide on 5/16/16.
 */
public class ProfileTab implements Initializable {

    @FXML
    HBox contentPane;
    @FXML Button homeButton;
    @FXML Button profileButton;
    @FXML Button playButton;
    @FXML Button storeButton;

    @FXML
    Label username;
    @FXML
    Label rp;
    @FXML
    Label ip;

    Profile profile;
    LoLRTMPSClient client;
    TypedObject summoner;
    LolChat lolChat;

    FXMLLoader loader;

    public ProfileTab(Profile profile) {
        this.profile = profile;
        login();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        openChat();

        username.setText(profile.getName());
        rp.setText("RP: " + summoner.getDouble("rpBalance").intValue());
        ip.setText("IP: " + summoner.getDouble("ipBalance").intValue());

        homeButton.setOnAction(e -> openChat());
        storeButton.setOnAction(e -> openStore());
    }

    private void login() {
        //League of Legends Server
        try {
            client = new LoLRTMPSClient(profile.getRegion(), Globals.LEAGUE_CLIENT_VERSION, profile.getName(), profile.getPassword());
            client.connectAndLogin();
            System.out.println("Logged into login server!");
            int id = client.invoke("clientFacadeService",
                    "getLoginDataPacketForUser", new Object[]{});
            summoner = client.getResult(id).getTO("data").getTO("body");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Chat Server
        lolChat = new LolChat(ChatServer.NA2, FriendRequestPolicy.ACCEPT_ALL);
        if(lolChat.login(profile.getName(), profile.getPassword())){
            System.out.println("Logged into chat server!");
        }

        /*lolChat.addChatListener((friend, message) -> {
            if(friendsView.getSelectionModel().getSelectedItem().getName().equalsIgnoreCase(friend.getName())) {
                profile.saveMessage(new Message(friend.getName(), profile.getName(), message, true));
                updateChatHistory();
            }

        });*/
    }

    private void openChat(){
        try {
            loader = new FXMLLoader(getClass().getClassLoader().getResource("abueide/nullclient/ui/jfx/graphical/ChatView.fxml"));
            loader.setController(new ChatView(profile, lolChat));
            contentPane.getChildren().clear();
            contentPane.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openStore(){
        try {
            int id = client.invoke("loginService", "getStoreUrl", new Object[]{});
            loader = new FXMLLoader(getClass().getClassLoader().getResource("abueide/nullclient/ui/jfx/graphical/StoreView.fxml"));
            loader.setController(new StoreView(client.getResult(id).getTO("data").getString("body")));
            contentPane.getChildren().clear();
            contentPane.getChildren().add(loader.load());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
