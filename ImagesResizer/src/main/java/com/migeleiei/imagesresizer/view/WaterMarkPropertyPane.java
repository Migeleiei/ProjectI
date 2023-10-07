package com.migeleiei.imagesresizer.view;

import com.migeleiei.imagesresizer.controller.WaterMarkController;
import com.migeleiei.imagesresizer.model.ImageModel;
import com.migeleiei.imagesresizer.model.ChooseType;
import com.migeleiei.imagesresizer.model.Constants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class WaterMarkPropertyPane extends VBox {

    //Font in OS
    ObservableList<String> fontsList;

    ObservableList<ImageModel> listImageModel;
    WaterMarkController watermarkController;

    public WaterMarkPropertyPane(ObservableList<ImageModel> listImageModel) {

        this.listImageModel = listImageModel;
        watermarkController = new WaterMarkController(listImageModel, ChooseType.WATERMARK);


        // Font in OS
        fontsList = FXCollections.observableArrayList(Font.getFontNames());

        //set VBox
        setSpacing(10);
        setAlignment(Pos.CENTER);

        Button saveButton = new Button("Save");
        watermarkController.onClickSaveButton(saveButton);

        getChildren().add(setTextProperty());

        getChildren().add(saveButton);
    }

    private GridPane setTextProperty() {
        GridPane gridPane = new GridPane();

        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);



        Text text = new Text("Text");
        Text fontSize = new Text("Font Size");
        Text font = new Text("Font");
        Text color = new Text("Color");
        Text rotate = new Text("Rotate");
        Text opacity = new Text("Opacity");

        TextField textField = new TextField();


        // ROW 1


        Slider sliderFontSize = new Slider();
        sliderFontSize.setMax(120);
        sliderFontSize.setMin(10);
        sliderFontSize.setValue(Constants.DEFAULT_TEXT_SIZE);


        gridPane.add(text, 0, 0);
        gridPane.add(textField, 1, 0);
        gridPane.add(fontSize, 2, 0);
        gridPane.add(sliderFontSize, 3, 0);


        ComboBox<String> combo = new ComboBox<>();


        for (String f : fontsList) {
            combo.getItems().add(f);
        }

        ColorPicker colorPicker = new ColorPicker();

        //ROW 2
        gridPane.add(font, 0, 1);
        gridPane.add(combo, 1, 1);
        gridPane.add(color, 2, 1);
        gridPane.add(colorPicker, 3, 1);

        // ROW 3

        Slider slider = new Slider();
        slider.setMax(180);
        slider.setMin(-180);
        slider.setValue(0);

        Spinner<Double> spinnerOpacity = new Spinner<Double>();
        SpinnerValueFactory<Double> opacityValueFactory = //
                new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1.0, Constants.DEFAULT_TEXT_OPACITY, 0.1);
        spinnerOpacity.setValueFactory(opacityValueFactory);


        gridPane.add(rotate, 0, 2);
        gridPane.add(slider, 1, 2);
        gridPane.add(opacity, 2, 2);
        gridPane.add(spinnerOpacity, 3, 2);


        /// Listener

        watermarkController.addTextListener(textField);
        watermarkController.addSliderFontSizeListener(sliderFontSize);
        watermarkController.addSelectFontListener(combo);
        watermarkController.addSelectColorListener(colorPicker);
        watermarkController.addTextOpacityListener(spinnerOpacity);
        watermarkController.addSliderRotateListener(slider);

        return gridPane;
    }


}
