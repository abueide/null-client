package abueide.nullclient.util.javafx;

import abueide.nullclient.data.Profile;
import abueide.nullclient.ui.jfx.controller.ProfileTabController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;

import java.io.IOException;

/**
 * Created by Andrew Bueide on 6/26/16.
 */
public class ProfileTab extends Tab {

    private Profile profile;

    public ProfileTab(Profile profile) {
        super(profile.getName());
        this.profile = profile;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("abueide/nullclient/ui/jfx/view/ProfileTab.fxml"));
            loader.setController(new ProfileTabController(profile));
                /*FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("abueide/nullclient/ui/jfx/view/LoginProfile.fxml"));
                loader.setController(new LoginProfileController(profile));*/
            this.setText(profile.getName());
            this.setClosable(true);
            this.setContent(loader.load());
            this.setOnClosed(e -> {
                this.disconnect();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        profile.disconnect();
    }
}
