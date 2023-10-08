package se233.TermProjectI.controller;

import se233.TermProjectI.model.ChooseType;
import javafx.beans.property.ObjectProperty;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class SceneController {

    public void addSceneController(ImageView imageView, ObjectProperty<ChooseType> chooseTypeObjectProperty, ChooseType chooseType) {
        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            chooseTypeObjectProperty.set(chooseType);
        });
    }
}
