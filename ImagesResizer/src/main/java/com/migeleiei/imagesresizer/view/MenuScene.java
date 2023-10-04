package com.migeleiei.imagesresizer.view;

import com.migeleiei.imagesresizer.Launcher;
import com.migeleiei.imagesresizer.controller.SceneController;
import com.migeleiei.imagesresizer.controller.WaterMarkController;
import com.migeleiei.imagesresizer.util.ChooseType;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URISyntaxException;
import java.util.Objects;

public class MenuScene {
    SceneController sceneController = new SceneController();
    private final ObjectProperty<ChooseType> chooseTypeObjectProperty;


    public MenuScene(ObjectProperty<ChooseType> chooseTypeObjectProperty) {
        this.chooseTypeObjectProperty = chooseTypeObjectProperty;
    }

    public Scene menuScene() throws URISyntaxException {

        return new Scene(menuPane(), 600, 400);
    }


    private Pane menuPane() throws URISyntaxException {


        VBox main = new VBox();
        main.setSpacing(60);
        main.setFillWidth(true);
        main.setAlignment(Pos.CENTER);

        HBox menu = new HBox();
        menu.setSpacing(80);
        menu.setFillHeight(true);
        menu.setAlignment(Pos.CENTER);

        // Menu

        Image resizeImg = new Image(Objects.requireNonNull(Launcher.class.getResource("/images/Resize.png")).toURI().toString());
        ImageView resize = new ImageView(resizeImg);
        AnchorPane.setLeftAnchor(resize, 60.0);
        resize.setFitWidth(150);
        resize.setFitHeight(150);
        resize.setPreserveRatio(true);

        Image watermarkImg = new Image(Objects.requireNonNull(Launcher.class.getResource("/images/Watermark.png")).toURI().toString());
        ImageView watermark = new ImageView(watermarkImg);
        AnchorPane.setRightAnchor(watermark, 60.0);
        watermark.setFitWidth(150);
        watermark.setFitHeight(150);
        watermark.setPreserveRatio(true);

        menu.getChildren().add(resize);
        menu.getChildren().add(watermark);

        ///
//        addListen SceneController
        sceneController.addSceneController(resize, chooseTypeObjectProperty, ChooseType.RESIZE);
        sceneController.addSceneController(watermark, chooseTypeObjectProperty, ChooseType.WATERMARK);

        Text title = new Text("Select Tool");

        // Effect
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.color(0.4, 0.4, 0.4));
        ds.setSpread(0.1);


        title.setFont(new Font("Chalkboard SE Light", 60));
        title.setEffect(ds);


        main.getChildren().add(title);
        main.getChildren().add(menu);

        return main;
    }


}
