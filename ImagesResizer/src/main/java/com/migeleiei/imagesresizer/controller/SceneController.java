package com.migeleiei.imagesresizer.controller;

import com.migeleiei.imagesresizer.util.ChooseType;
import javafx.beans.property.ObjectProperty;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class SceneController {

    public void addSceneController(ImageView imageView, ObjectProperty<ChooseType> chooseTypeObjectProperty, ChooseType chooseType) {
        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            System.out.println("dddd");
            chooseTypeObjectProperty.set(chooseType);
        });
    }
}
