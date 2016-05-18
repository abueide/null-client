package abueide.jtox.ui.jfx.controller;

import abueide.jtox.tox.Profile;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;
import abueide.jtox.tox.Profile;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Andrew Bueide on 5/16/16.
 */
public class ProfileView implements Initializable{

    @FXML Button addFriend;
    @FXML Button delFriend;
    @FXML
    Button sendButton;
    @FXML
    ListView<Profile> friendsView;
    @FXML
    TextArea chatHistory;
    @FXML
    TextField messageField;

    Profile profile;

    public ProfileView(Profile profile){
        this.profile = profile;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GlyphsDude.setIcon(sendButton, FontAwesomeIcon.COMMENT);
        GlyphsDude.setIcon(addFriend, FontAwesomeIcon.PLUS);
        GlyphsDude.setIcon(delFriend, FontAwesomeIcon.MINUS);

        friendsView.setCellFactory(new Callback<ListView<Profile>, ListCell<Profile>>() {

            @Override
            public ListCell<Profile> call(ListView<Profile> p) {

                ListCell<Profile> cell = new ListCell<Profile>() {

                    @Override
                    protected void updateItem(Profile t, boolean bln) {
                        super.updateItem(t, bln);
                        if (t != null) {
                            setText(t.getName());
                        } else {
                            setText("");
                        }
                    }

                };

                return cell;
            }
        });

        messageField.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) sendMessage(messageField.getText());
        });
        sendButton.setOnAction(e -> sendMessage(messageField.getText()));

    }

    private void sendMessage(String message){
        chatHistory.appendText(message + "\n");
        messageField.clear();
    }

}
