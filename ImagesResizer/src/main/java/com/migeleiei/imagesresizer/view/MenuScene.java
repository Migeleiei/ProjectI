package com.migeleiei.imagesresizer.view;

import com.migeleiei.imagesresizer.Launcher;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;
import java.net.URISyntaxException;
import java.util.Objects;

public class MenuScene {
    public Scene menuScene() throws URISyntaxException {
        AnchorPane anchorPane = anchorPane();
        Scene menu = new Scene(anchorPane,600,400);
        return menu;
    }
    public AnchorPane anchorPane() throws URISyntaxException {
        Text text = new Text("Select Tool");
        text.setFont(new Font("Chalkboard SE Light",28));
        text.setLayoutX(228.0);
        text.setLayoutY(87);
        AnchorPane anchorPane = new AnchorPane();

        Image resizeImg = new Image(Objects.requireNonNull(Launcher.class.getResource("/images/Resize.png")).toURI().toString());
        ImageView resize = new ImageView(resizeImg);
        AnchorPane.setLeftAnchor(resize,60.0);
        resize.setFitWidth(150);
        resize.setFitHeight(150);
        resize.setLayoutX(56);
        resize.setLayoutY(150);
        resize.setPreserveRatio(true);

        Image watermarkImg = new Image(Objects.requireNonNull(Launcher.class.getResource("/images/Watermark.png")).toURI().toString());
        ImageView watermark = new ImageView(watermarkImg);
        AnchorPane.setRightAnchor(watermark,60.0);
        watermark.setFitWidth(150);
        watermark.setFitHeight(150);
        watermark.setLayoutX(334);
        watermark.setLayoutY(150);
        watermark.setPreserveRatio(true);

        resize.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            System.out.println("dddd");
        });
        anchorPane.getChildren().addAll(text,resize,watermark);


        return anchorPane;
    }

}
