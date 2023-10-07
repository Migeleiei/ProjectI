package com.migeleiei.imagesresizer.controller;

import com.migeleiei.imagesresizer.model.ImageModel;
import com.migeleiei.imagesresizer.model.ChooseType;
import com.migeleiei.imagesresizer.model.Constants;
import com.migeleiei.imagesresizer.util.SaveImageThread;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class WaterMarkController {

    private ObservableList<ImageModel> listImageModel;
    private ChooseType chooseType;

    public WaterMarkController(ObservableList<ImageModel> listImageModel,ChooseType chooseType) {
        this.listImageModel = listImageModel;
        this.chooseType = chooseType;
    }

    public void onClickSaveButton(Button button) {

        button.setOnAction(e -> {
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(Constants.TITLE_SAVE_DIALOG);
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png"));

            File fileDialog = fileChooser.showSaveDialog(stage);


            String pathTo = fileDialog.getPath();

            //Check has image
            listImageModel.forEach(i -> {
                //have 1 image save name input
                if (listImageModel.size() == 1) {

                    String fileName = fileDialog.getName();

                    SaveImageThread saveImageThread = new SaveImageThread(i, pathTo, fileName,this.chooseType);
                    saveImageThread.start();

                } else {
                   // String fileNameDialog = fileDialog.getName();
                 //   String fileExtension = fileNameDialog.substring(fileNameDialog.lastIndexOf(".") + 1, fileDialog.getName().length());//
                    //have > 1 save same original
//                    File fileOrigi = new File(i.getPathImage());
                    String fileName = i.getImageName();

                    SaveImageThread saveImageThread = new SaveImageThread(i, pathTo, fileName,this.chooseType);
                    saveImageThread.start();


                }


            });

            System.out.println("Save images are success");
            showSaveImageSuccess();


        });
    }

    public void addTextListener(final TextField textField) {
        textField.textProperty().addListener((ob, o, n) -> {
            listImageModel.forEach(i -> {
                i.setTextWaterMarkProperty(n);
                i.setModelProperty(n);
            });
        });
    }


    public void addSliderFontSizeListener(Slider slider) {
        slider.valueProperty().addListener((observable, oldValue, n) -> {
            listImageModel.forEach(i -> {
                i.setTextWaterMarkSize(n.intValue());
                i.setModelProperty(n);

            });
        });
    }

    public void addTextOpacityListener(Spinner<Double> spinner) {
        spinner.valueProperty().addListener((ob, o, n) -> {
            listImageModel.forEach(i -> {
                i.setTextOpacityProperty(n);
                i.setModelProperty(n);
            });
        });
    }

    public void addSliderRotateListener(Slider slider) {
        slider.valueProperty().addListener((observable, oldValue, n) -> {
            listImageModel.forEach(i -> {
                i.setRotate(n.doubleValue());
                i.setModelProperty(n);

            });
        });
    }



    public void addSelectFontListener(final ComboBox<String> combo) {
        combo.setOnAction(e -> {
            listImageModel.forEach(i -> {
                i.setTextWaterMarkFont(combo.getValue());
                i.setModelProperty(combo.getValue());
            });

        });
    }

    public void addSelectColorListener(final ColorPicker colorPicker) {
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

    private static String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }


    private void showSaveImageSuccess(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Save Image with Watermark");
        alert.setHeaderText(null);
        alert.setContentText("Save Images with Watermark is successful.");

        alert.showAndWait();
    }


}
