package abueide.nullclient.ui.jfx.controller;

import abueide.nullclient.data.Friend;
import abueide.nullclient.data.Message;
import abueide.nullclient.data.Profile;
import abueide.nullclient.util.Globals;
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
import java.util.ResourceBundle;

/**
 * Created by Andrew Bueide on 5/16/16.
 */
public class ProfileTab implements Initializable {

    @FXML
    Button sendButton;
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


    public ProfileTab(Profile profile) {
        this.profile = profile;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        login();

        rp.setText("RP: " + summoner.getDouble("rpBalance").intValue());
        ip.setText("IP: " + summoner.getDouble("ipBalance").intValue());

        //System.out.println(profile.getName() + ":" + profile.getPassword());

        GlyphsDude.setIcon(sendButton, FontAwesomeIcon.COMMENT);

        username.setText(profile.getName());

        ContextMenu friendsListContext = new ContextMenu();
        MenuItem editFriend = new MenuItem("Edit Contact");
        MenuItem deleteFriend = new MenuItem("Delete");
        editFriend.setOnAction(e -> {
            friendsView.getSelectionModel().getSelectedItems().forEach((friend) -> profile.updateFriend(new FriendWindow(friend).display()));
            friendsView.setItems(FXCollections.observableList(profile.getFriends()));
        });
        deleteFriend.setOnAction(e -> {
            friendsView.getSelectionModel().getSelectedItems().forEach((friend) -> profile.deleteFriend(friend));
            friendsView.setItems(FXCollections.observableList(profile.getFriends()));
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
                            if (!t.getAlias().isEmpty()) {
                                setText(t.getAlias());
                            } else {
                                setText(t.getName());
                            }
                        } else {
                            setText("");
                        }
                    }

                };
                cell.setContextMenu(friendsListContext);
                return cell;
            }
        });
        friendsView.setItems(FXCollections.observableList(profile.getFriends()));

        sendButton.setOnAction(e -> sendMessage(messageField.getText()));

        messageField.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER && !messageField.getText().isEmpty())
                sendMessage(messageField.getText());
        });
        addFriendField.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                Friend friend = new Friend();
                friend.setName(addFriendField.getText());
                profile.addFriend(friend);
                friendsView.setItems(FXCollections.observableList(profile.getFriends()));
                addFriendField.clear();
            }
        });

        friendsView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                chatHistory.clear();
                profile.getMessages(newValue).forEach((message) ->
                        chatHistory.appendText(String.format("[%s] %s: %s\n", message.getTime(), message.getSender(), message.getMessage())));
            }
        });
    }

    private void sendMessage(String message) {
        profile.sendMessage(new Message(profile.getName(), friendsView.getSelectionModel().getSelectedItem().getName(), message));
        chatHistory.appendText(message + "\n");
        messageField.clear();
    }

    private void login(){
        try {
            client = new LoLRTMPSClient(profile.getRegion(), Globals.LEAGUE_CLIENT_VERSION, profile.getName(), profile.getPassword());
            client.connectAndLogin();
            System.out.println("Logged in");
            int id = client.invoke("clientFacadeService",
                    "getLoginDataPacketForUser", new Object[] {});
            summoner = client.getResult(id).getTO("data").getTO("body");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
