package abueide.nullclient.ui.jfx.controller;

import abueide.nullclient.ui.jfx.model.MainWindowModel;
import abueide.nullclient.util.javafx.ProfileTab;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Andrew Bueide on 5/20/16.
 */
public class MainWindowController implements Initializable {

    @FXML
    private TabPane tabPane;

    private static Scene scene;

    private MainWindowModel model;

    public MainWindowController(MainWindowModel model) {
        this.model = model;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tabPane.getTabs().setAll(model.getTabs());
        model.getTabs().addListener((observable, oldValue, newValue) -> {
            tabPane.getTabs().setAll(newValue);
        });
    }

    private void closeTab(Tab tab) {
        EventHandler<Event> handler = tab.getOnClosed();
        if (null != handler) {
            handler.handle(null);
        } else {
            tab.getTabPane().getTabs().remove(tab);
        }
    }

    public void disconnectAll() {
        model.getTabs().forEach(ProfileTab::disconnect);
    }

    public void display() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("abueide/nullclient/ui/jfx/view/MainWindow.fxml"));
        loader.setController(this);
        Parent root;
        try {
            root = loader.load();
            scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Null Client");
            stage.setScene(scene);
            stage.setOnCloseRequest(e -> {
                disconnectAll();
            });
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Scene getScene() {
        return scene;
    }

}
