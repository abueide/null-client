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

/*    private void login() {
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
*//*        try {
            final LeagueConnection c = new LeagueConnection(profile.getRegion());
            c.getAccountQueue().addAccount(new LeagueAccount(
                    ServerInfo.NA, "6.12.xx", profile.getName(), profile.getPassword()));
            Map<LeagueAccount, LeagueException> exceptions = c.getAccountQueue().connectAll();
            if(exceptions != null) {
                for(LeagueAccount account : exceptions.keySet()) {
                    System.out.println(account + " error: " + exceptions.get(account));
                }
                return;
            }
            c.getGameService().
            LeagueSummoner test = new LeagueSummoner();
           // System.out.println(c.getClientFacadeService().test());
        } catch (LeagueException e) {
            e.printStackTrace();
        }*//*
        //Chat Server
        lolChat = new LolChat(ChatServer.NA2, FriendRequestPolicy.ACCEPT_ALL);
        if(lolChat.login(profile.getName(), profile.getPassword())){
            System.out.println("Logged into chat server!");
        }

        *//*lolChat.addChatListener((friend, message) -> {
            if(friendsView.getSelectionModel().getSelectedItem().getName().equalsIgnoreCase(friend.getName())) {
                profile.saveMessage(new Message(friend.getName(), profile.getName(), message, true));
                updateChatHistory();
            }

        });*//*
    }*/

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
            loader.setController(new StoreViewController(profile.getClient().getResult(id).getTO("data").getString("body")));
            contentPane.getChildren().clear();
            contentPane.getChildren().add(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
