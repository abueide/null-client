package abueide.jtox.ui.jfx.controller;

import abueide.jtox.tox.Profile;
import abueide.jtox.util.Globals;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import javafx.util.Callback;
import abueide.jtox.tox.Profile;
import abueide.jtox.util.Globals;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProfileSelectionWindow implements Initializable {

    @FXML
    Button createProfileButton;
    @FXML
    Button deleteProfileButton;
    @FXML
    Button editProfileButton;
    @FXML
    Button loadProfileButton;
    @FXML
    Button settingsButton;
    @FXML
    ListView<Profile> profileListView;

    List<Profile> profiles;

    public ProfileSelectionWindow(List<Profile> profiles) {
        this.profiles = profiles;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        GlyphsDude.setIcon(createProfileButton, FontAwesomeIcon.PLUS);
        GlyphsDude.setIcon(deleteProfileButton, FontAwesomeIcon.TRASH);
        GlyphsDude.setIcon(editProfileButton, FontAwesomeIcon.PENCIL);
        GlyphsDude.setIcon(settingsButton, FontAwesomeIcon.COG);
        GlyphsDude.setIcon(loadProfileButton, FontAwesomeIcon.SIGN_IN);

        profileListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        // holy shit this took forever to find and I have no clue how it works.
        // If some one can do this better that would be nice.
        profileListView.setCellFactory(new Callback<ListView<Profile>, ListCell<Profile>>() {

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
        profileListView.setItems(FXCollections.observableList(profiles));

        // Set actions
        createProfileButton.setOnAction(e -> createProfile());
        deleteProfileButton.setOnAction(e -> deleteProfile());
        editProfileButton.setOnAction(e -> editProfile());
        settingsButton.setOnAction(e -> settingsWindow());
        loadProfileButton.setOnAction(e -> loadProfile());

    }

    // setOnAction methods
    private void createProfile() {
        Profile p = new ProfileWindow(new Profile()).display();
        Globals.jtoxdb.executeStatement(Globals.insertProfile(p),
                "Successfully inserted " + p.getName() + " into table profiles");
        updateProfileListView();
    }

    private void deleteProfile() {
        for (Profile profile : profileListView.getSelectionModel().getSelectedItems()) {
            Globals.jtoxdb.executeStatement(Globals.deleteProfile(profile), "Successfully deleted profile");
        }
        updateProfileListView();

    }

    private void editProfile() {
        Profile p;
        for (Profile profile : profileListView.getSelectionModel().getSelectedItems()) {
            p = new ProfileWindow(profile).display();
            Globals.jtoxdb.executeStatement(Globals.editProfile(p), "Successfully edited profile");
        }
        updateProfileListView();
    }

    private void settingsWindow(){
        new ProfileSelectionSettingsWindow();
        updateProfileListView();
    }

    private void loadProfile() {
        new ChatView(profileListView.getSelectionModel().getSelectedItems()).display();
        loadProfileButton.getScene().getWindow().hide();
    }

    // Window Methods
    public void launch(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("abueide/jtox/ui/jfx/graphical/ProfileSelectionWindow.fxml"));
        loader.setController(this);
        Parent root;
        try {
            root = loader.load();
            primaryStage.setTitle("jTox - Select Profile");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void display() {
        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("abueide/jtox/ui/jfx/graphical/ProfileSelectionWindow.fxml"));
        loader.setController(this);
        Parent root;
        try {
            root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("jTox - Select Profile");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateProfileListView() {
        profiles = Globals.jtoxdb.getProfiles();
        profileListView.setItems(FXCollections.observableList(profiles));
    }

    // Getters + Setters
    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
        profileListView.setItems(FXCollections.observableList(this.profiles));
    }

}
