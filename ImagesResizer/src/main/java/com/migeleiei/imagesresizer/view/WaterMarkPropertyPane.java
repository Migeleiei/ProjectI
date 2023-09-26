package com.migeleiei.imagesresizer.view;

import com.migeleiei.imagesresizer.model.ImageModel;
import com.migeleiei.imagesresizer.util.Constants;
import com.migeleiei.imagesresizer.util.SaveImageThread;
import com.migeleiei.imagesresizer.util.UtilImage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class WaterMarkPropertyPane extends VBox {

    //Font in OS
    ObservableList<String> fontsList;

    ObservableList<ImageModel> listImageModel;

    public WaterMarkPropertyPane(ObservableList<ImageModel> listImageModel) {

        this.listImageModel = listImageModel;


        // Font in OS
        fontsList = FXCollections.observableArrayList(Font.getFontNames());

        //set VBox
        setSpacing(10);
        setAlignment(Pos.CENTER);

        Button saveButton = new Button("Save");
        onClickSaveButton(saveButton);

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
        TextField fontSizeTextField = new TextField();


        // ROW 1

//        Spinner<Integer> spinner = new Spinner<Integer>();
//        SpinnerValueFactory<Integer> spinnerValueFactory = //
//                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 120, Constants.DEFAULT_TEXT_SIZE);
//        spinner.setValueFactory(spinnerValueFactory);

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

        addTextListener(textField);
        addSliderFontSizeListener(sliderFontSize);
//        addTextFontSizeListener(spinner);
//        addTextSizeListener(fontSizeTextField);
        addSelectFontListener(combo);
        addSelectColorListener(colorPicker);
        addTextOpacityListener(spinnerOpacity);
        addSliderRotateListener(slider);

        return gridPane;
    }

    //listen event

    private void onClickSaveButton(Button button) {

        button.setOnAction(e -> {
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(Constants.TITLE_SAVE_DIALOG);
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg","*.png"));

            File fileDialog = fileChooser.showSaveDialog(stage);


            String pathTo = fileDialog.getPath();

            //Check has image
            listImageModel.forEach(i -> {
                //have 1 image save name input
                if (listImageModel.size() == 1) {

                    String fileName = fileDialog.getName();

                    SaveImageThread saveImageThread = new SaveImageThread(i, pathTo, fileName);
                    saveImageThread.start();

                } else {
                    String fileNameDialog = fileDialog.getName();
                    String fileExtension = fileNameDialog.substring(fileNameDialog.lastIndexOf(".") + 1, fileDialog.getName().length());
                    //have > 1 save same original
//                    File fileOrigi = new File(i.getPathImage());
                    String fileName = i.getImageName();

                    SaveImageThread saveImageThread = new SaveImageThread(i, pathTo, fileName);
                    saveImageThread.start();


                }


            });


        });
    }

    private void addTextListener(final TextField textField) {
        textField.textProperty().addListener((ob, o, n) -> {
            listImageModel.forEach(i -> {
                i.setTextWaterMarkProperty(n);
                i.setModelProperty(n);
            });
        });
    }

//    private void addTextFontSizeListener(Spinner<Integer> spinner) {
//        spinner.valueProperty().addListener((ob, o, n) -> {
//            listImageModel.forEach(i -> {
//                i.setTextWaterMarkSize(n);
//                i.setModelProperty(n);
//            });
//
//        });
//    }


    private void addSliderFontSizeListener(Slider slider) {
        slider.valueProperty().addListener((observable, oldValue, n) -> {
            listImageModel.forEach(i -> {
                i.setTextWaterMarkSize(n.intValue());
                i.setModelProperty(n);

            });
        });
    }
    private void addTextOpacityListener(Spinner<Double> spinner) {
        spinner.valueProperty().addListener((ob, o, n) -> {
            listImageModel.forEach(i -> {
                i.setTextOpacityProperty(n);
                i.setModelProperty(n);
            });
        });
    }

    private void addSliderRotateListener(Slider slider) {
        slider.valueProperty().addListener((observable, oldValue, n) -> {
            listImageModel.forEach(i -> {
                i.setRotate(n.doubleValue());
                i.setModelProperty(n);

            });
        });
    }

    private void addTextSizeListener(final TextField textField) {
        textField.textProperty().addListener((ob, o, n) -> {
            listImageModel.forEach(i -> {
                if (!n.isBlank() || !n.isEmpty())
                    if (Integer.parseInt(n) < 1) {
                        i.setTextWaterMarkSize(2);
                        i.setModelProperty(2);
                    } else {
                        i.setTextWaterMarkSize(Integer.parseInt(n));
                        i.setModelProperty(Integer.parseInt(n));
                    }

            });
        });
    }

    private void addSelectFontListener(final ComboBox<String> combo) {
        combo.setOnAction(e -> {
            listImageModel.forEach(i -> {
                i.setTextWaterMarkFont(combo.getValue());
                i.setModelProperty(combo.getValue());
            });

        });
    }

    private void addSelectColorListener(final ColorPicker colorPicker) {
        colorPicker.setOnAction(e -> {
            Color c = colorPicker.getValue();

            String hex = toRGBCode(c);
            System.out.println("Hex color :" + hex);
            System.out.println("Size : " + listImageModel.size());


            listImageModel.forEach(i -> {
                i.setTextWaterMarkColor(hex);
                i.setModelProperty(hex);

            });

        });
    }


    public static String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }


}
