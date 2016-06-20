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
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;

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
    Button sendButton;
    @FXML Button profileButton;
    @FXML Button playButton;
    @FXML Button storeButton;
    @FXML
    ListView<Friend> friendsView;
    @FXML
    TextArea chatHistory;
    @FXML
    TextField messageField;
    @FXML
    TextField addFriendField;

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

    public ProfileTab(Profile profile) {
        this.profile = profile;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        login();

        rp.setText("RP: " + summoner.getDouble("rpBalance").intValue());
        ip.setText("IP: " + summoner.getDouble("ipBalance").intValue());

        GlyphsDude.setIcon(sendButton, FontAwesomeIcon.COMMENT);

        username.setText(profile.getName());

        ContextMenu friendsListContext = new ContextMenu();
        MenuItem editFriend = new MenuItem("Edit Contact");
        MenuItem deleteFriend = new MenuItem("Delete");
        /*editFriend.setOnAction(e -> {
            friendsView.getSelectionModel().getSelectedItems().forEach((friend) -> profile.updateFriend(new FriendWindow(friend).display()));
            friendsView.setItems(FXCollections.observableList(profile.getFriends()));
        });*/
        deleteFriend.setOnAction(e -> {
            friendsView.getSelectionModel().getSelectedItems().forEach((friend) -> friend.delete());
            friendsView.setItems(FXCollections.observableList(lolChat.getFriends()));
        });
        friendsListContext.getItems().addAll(editFriend, deleteFriend);

        friendsView.setCellFactory(new Callback<ListView<Friend>, ListCell<Friend>>() {

            @Override
            public ListCell<Friend> call(ListView<Friend> p) {

                ListCell<Friend> cell = new ListCell<Friend>() {

                    @Override
                    protected void updateItem(Friend t, boolean bln) {
                        super.updateItem(t, bln);
                        if (t != null) {
                                setText(t.getName());
                        } else {
                            setText("");
                        }
                    }

                };
                cell.setContextMenu(friendsListContext);
                return cell;
            }
        });
        friendsView.setItems(FXCollections.observableList(lolChat.getFriends()));

        sendButton.setOnAction(e -> sendMessage(messageField.getText()));
        storeButton.setOnAction(e -> openStore());

        messageField.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER && !messageField.getText().isEmpty())
                sendMessage(messageField.getText());
        });

        addFriendField.setOnKeyPressed(e -> {
            List<Friend> friends = lolChat.getFriends();
            List<Friend> shownFriends = new ArrayList<Friend>();
            String s = addFriendField.getText().toLowerCase();
            for(Friend friend : friends){
                try{
                    if(friend.getName().toLowerCase().contains(s) && (friend.getChatMode().equals(ChatMode.AVAILABLE) || friend.getChatMode().equals(ChatMode.AWAY) || friend.getChatMode().equals(ChatMode.BUSY))) {
                        shownFriends.add(friend);
                    }
                }catch(NullPointerException npe){

                }
            }

            friendsView.setItems(FXCollections.observableList(shownFriends));
        });
        addFriendField.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                lolChat.addFriendByName(addFriendField.getText());
                friendsView.setItems(FXCollections.observableList(lolChat.getFriends()));
            }
        });

        friendsView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                chatHistory.clear();
                /*profile.getMessages(newValue).forEach((message) ->
                        chatHistory.appendText(String.format("[%s] %s: %s\n", message.getTime(), message.getSender(), message.getMessage())));*/
            }
        });
    }

    private void sendMessage(String message) {
        Friend friend = friendsView.getSelectionModel().getSelectedItem();
        friend.sendMessage(message);
        profile.saveMessage(new Message(profile.getName(), friend.getName(), message, true));
        updateChatHistory();
        messageField.clear();
    }

    private void updateChatHistory(){
        chatHistory.clear();
        for(Message message : profile.getMessages(friendsView.getSelectionModel().getSelectedItem())){
            chatHistory.appendText(String.format("[%s] %s: %s\n", message.getTime(), message.getSender(), message.getMessage()));
        }
    }

    private void openStore(){
        try {
            int id = client.invoke("loginService", "getStoreUrl", new Object[]{});
            System.out.println(client.getResult(id).getTO("data").getString("body"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void login() {
        //League of Legends Server
        try {
            client = new LoLRTMPSClient(profile.getRegion(), Globals.LEAGUE_CLIENT_VERSION, profile.getName(), profile.getPassword());
            client.connectAndLogin();
            System.out.println("Logged in");
            int id = client.invoke("clientFacadeService",
                    "getLoginDataPacketForUser", new Object[]{});
            summoner = client.getResult(id).getTO("data").getTO("body");
        } catch (Exception e) {
            e.printStackTrace();
        }

        lolChat = new LolChat(ChatServer.NA2, FriendRequestPolicy.ACCEPT_ALL);
        lolChat.addChatListener((friend, message) -> {
            if(friendsView.getSelectionModel().getSelectedItem().getName().equalsIgnoreCase(friend.getName())) {
                profile.saveMessage(new Message(friend.getName(), profile.getName(), message, true));
                updateChatHistory();
            }

        });
        if(lolChat.login(profile.getName(), profile.getPassword())){
            System.out.println("Connected!");
        }
    }

}
