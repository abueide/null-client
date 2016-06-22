package abueide.nullclient.ui.jfx.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by gratin on 6/22/16.
 */
public class StoreView implements Initializable{

    @FXML
    WebView webView;

    WebEngine webEngine;
    String s;

    public StoreView(String s){
        this.s = s;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        webEngine = webView.getEngine();
        webEngine.load(s);

        //Very hackish, only solution I could find
        Scene scene = MainWindow.getScene();
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                webView.setPrefWidth(newSceneWidth.doubleValue());
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                webView.setPrefHeight(newSceneHeight.doubleValue());
            }
        });
    }
}
