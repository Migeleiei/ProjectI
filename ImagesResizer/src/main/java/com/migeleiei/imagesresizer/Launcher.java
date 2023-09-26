package com.migeleiei.imagesresizer;
/**
 *
 * Author MigelEiEi or Seksan Jomchanasuk and .....
 *
 *
 */

import com.migeleiei.imagesresizer.view.DropScene;
import com.migeleiei.imagesresizer.view.MainScene;
import com.migeleiei.imagesresizer.view.MenuScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URISyntaxException;

public class Launcher extends Application {


    @Override
    public void start(Stage stage) throws  URISyntaxException {


        MainScene mainScene = new MainScene();
        DropScene dropScene = new DropScene();
        MenuScene menuScene = new MenuScene();
        Scene scene = mainScene.mainScene();

        stage.setTitle("Project");
        stage.setScene(mainScene.mainScene());
        stage.show();
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