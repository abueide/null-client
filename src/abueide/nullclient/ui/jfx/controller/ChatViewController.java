package abueide.nullclient.ui.jfx.controller;

import abueide.nullclient.data.Message;
import abueide.nullclient.data.Profile;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;
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

/**
 * Created by Andrew Bueide on 6/22/16.
 */
public class ChatViewController implements Initializable {

    @FXML
    ListView<Friend> friendsView;
    @FXML
    TextArea chatHistory;
    @FXML
    TextField messageField;
    @FXML
    TextField addFriendField;

    @FXML
    Button sendButton;

    Profile profile;


    public ChatViewController(Profile profile) {
        this.profile = profile;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        GlyphsDude.setIcon(sendButton, FontAwesomeIcon.COMMENT);

        ContextMenu friendsListContext = new ContextMenu();
        MenuItem deleteFriend = new MenuItem("Delete");
        deleteFriend.setOnAction(e -> {
            friendsView.getSelectionModel().getSelectedItems().forEach((friend) -> friend.delete());
            friendsView.setItems(FXCollections.observableList(profile.getChatClient().getFriends()));
        });
        friendsListContext.getItems().addAll(deleteFriend);

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
        friendsView.setItems(FXCollections.observableList(profile.getChatClient().getFriends()));
        friendsView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                chatHistory.clear();
                profile.getMessages(newValue).forEach((message) ->
                        chatHistory.appendText(String.format("[%s] %s: %s\n", message.getTime(), message.getSender(), message.getMessage())));
            }
        });

        sendButton.setOnAction(e -> sendMessage(messageField.getText()));

        messageField.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER && !messageField.getText().isEmpty()) {
                sendMessage(messageField.getText());
            }
        });

        addFriendField.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                profile.getChatClient().addFriendByName(addFriendField.getText());
                friendsView.setItems(FXCollections.observableList(profile.getChatClient().getFriends()));
                System.out.println("Added friend");
            } else {
                List<Friend> friends = profile.getChatClient().getFriends();
                List<Friend> shownFriends = new ArrayList<Friend>();
                String s = addFriendField.getText().toLowerCase();
                for (Friend friend : friends) {
                    try {
                        if (friend.getName().toLowerCase().contains(s)) {
                            shownFriends.add(friend);
                        }
                    } catch (NullPointerException npe) {

                    }
                }
                friendsView.setItems(FXCollections.observableList(shownFriends));
                System.out.println("filtering list");
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

    private void updateChatHistory() {
        chatHistory.clear();
        for (Message message : profile.getMessages(friendsView.getSelectionModel().getSelectedItem())) {
            chatHistory.appendText(String.format("[%s] %s: %s\n", message.getTime(), message.getSender(), message.getMessage()));
        }
    }
}