package com.migeleiei.imagesresizer.view;

import com.migeleiei.imagesresizer.Launcher;
import com.migeleiei.imagesresizer.controller.SceneController;
import com.migeleiei.imagesresizer.model.ChooseType;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URISyntaxException;
import java.util.Objects;

public class MenuScene {
    //click in sceneControl -> choose water or resize
    SceneController sceneController = new SceneController();
    private final ObjectProperty<ChooseType> chooseTypeObjectProperty;


    public MenuScene(ObjectProperty<ChooseType> chooseTypeObjectProperty) {
        this.chooseTypeObjectProperty = chooseTypeObjectProperty;
    }

    public Scene menuScene() throws URISyntaxException {

        return new Scene(menuPane(), 600, 400);
    }

    private Pane menuPane() throws URISyntaxException {
        Font font1 = Font.loadFont(Objects.requireNonNull(Launcher.class.getResource("/fonts/Halloween-Funtime.ttf")).toURI().toString(),20);
        Image backgroundImage = new Image(Objects.requireNonNull(Launcher.class.getResource("/images/1.png")).toURI().toString());

        Font font2 = Font.loadFont(Objects.requireNonNull(Launcher.class.getResource("/fonts/Halloween-Funtime.ttf")).toURI().toString(),50);

        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(600); // Set the width to match your scene size
        backgroundImageView.setFitHeight(400);
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImageObj = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
        Background background = new Background(backgroundImageObj);


        VBox main = new VBox();
        main.setSpacing(60);
        main.setFillWidth(true);
        main.setAlignment(Pos.CENTER);
//        main.setStyle("-fx-background-color: #ffb6c1;");
        main.setBackground(background);

        //8fbc8f green
        //b0e0e6 blue
        //d8bfd8 light purple
        //ffc87c yellow



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

        Text resizeText = new Text("Resize");
        resizeText.setFont(font1);
        VBox resizeBox = new VBox();
        resizeBox.setAlignment(Pos.CENTER);
        resizeBox.getChildren().addAll(resize, resizeText);

        Image watermarkImg = new Image(Objects.requireNonNull(Launcher.class.getResource("/images/Watermark.png")).toURI().toString());
        ImageView watermark = new ImageView(watermarkImg);
        AnchorPane.setRightAnchor(watermark, 60.0);
        watermark.setFitWidth(150);
        watermark.setFitHeight(150);
        watermark.setPreserveRatio(true);

        Text watermarkText = new Text("Watermark");
        watermarkText.setFont(font1);
        VBox watermarkBox = new VBox();
        watermarkBox.setAlignment(Pos.CENTER);
        watermarkBox.getChildren().addAll(watermark, watermarkText);

        menu.getChildren().addAll(resizeBox, watermarkBox);

//

        ///
//        addListen SceneController
        sceneController.addSceneController(resize, chooseTypeObjectProperty, ChooseType.RESIZE);
        sceneController.addSceneController(watermark, chooseTypeObjectProperty, ChooseType.WATERMARK);

        Text title = new Text("Select Tool");

        // Effect
//        DropShadow ds = new DropShadow();
//        ds.setOffsetY(3.0f);
//        ds.setColor(Color.color(0.4, 0.4, 0.4));
//        ds.setSpread(0.1);


        title.setFont(font2);

//        title.setEffect(ds);


        main.getChildren().add(title);
        main.getChildren().add(menu);

        return main;
    }


}
