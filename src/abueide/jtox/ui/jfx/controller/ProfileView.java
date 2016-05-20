package abueide.jtox.ui.jfx.controller;

import abueide.jtox.tox.data.Friend;
import abueide.jtox.tox.data.Message;
import abueide.jtox.tox.data.Profile;
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
public class ProfileView implements Initializable {

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

    Profile profile;

    public ProfileView(Profile profile) {
        this.profile = profile;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GlyphsDude.setIcon(sendButton, FontAwesomeIcon.COMMENT);

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
                            } else if (!t.getName().isEmpty()) {
                                setText(t.getName());
                            } else {
                                setText(t.getPublicKey());
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
            if (event.getCode() == KeyCode.ENTER) sendMessage(messageField.getText());
        });
        addFriendField.setOnKeyPressed((event) -> {
            Friend friend = new Friend();
            friend.setPublicKey(addFriendField.getText());
            if (event.getCode() == KeyCode.ENTER) profile.addFriend(friend);
            friendsView.setItems(FXCollections.observableList(profile.getFriends()));
        });

        friendsView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            chatHistory.clear();
            profile.getMessages(newValue).forEach((message) ->
                    chatHistory.appendText(String.format("[%s] %s: %s\n", message.getTime(), message.getSender(), message.getMessage())));
        });
    }

    ;

    private void sendMessage(String message) {
        profile.sendMessage(new Message(profile.getPublicKey(), friendsView.getSelectionModel().getSelectedItem().getPublicKey(), message));
        chatHistory.appendText(message + "\n");
        messageField.clear();
    }

}
