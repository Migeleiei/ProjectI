package com.migeleiei.imagesresizer;
/**
 * Author MigelEiEi or Seksan Jomchanasuk and Chonthicha Maitham
 */

import com.migeleiei.imagesresizer.model.ChooseType;
import com.migeleiei.imagesresizer.view.DropScene;
import com.migeleiei.imagesresizer.view.MenuScene;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URISyntaxException;

public class Launcher extends Application {


    ObjectProperty<ChooseType> chooseTypeObjectProperty = new SimpleObjectProperty<>();

    @Override
    public void start(Stage stage) throws URISyntaxException {


        MenuScene menuScene = new MenuScene(chooseTypeObjectProperty);
        Scene meScene = menuScene.menuScene();




        stage.setTitle("Floptopica Project");
        stage.setScene(meScene);
        stage.show();
        // switch scene
        chooseTypeObjectProperty.addListener((ob, o, n) -> {

            switch (n) {
                case RESIZE -> {
                    stage.setTitle("Drop Your Images");
                    DropScene dropScene = new DropScene(n);
                    Scene dScene = null;
                    try {
                        dScene = dropScene.dropScene();
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                    stage.setScene(dScene);
                    stage.show();
                }
                case WATERMARK -> {
                    stage.setTitle("Drop your Image");
                    DropScene dropScene = new DropScene(n);
                    Scene dScene = null;
                    try {
                        dScene = dropScene.dropScene();
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                    stage.setScene(dScene);
                    stage.show();
                }

            }


        });


    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("Stop");
    }
}