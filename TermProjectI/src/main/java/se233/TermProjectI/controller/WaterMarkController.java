package se233.TermProjectI.controller;

import se233.TermProjectI.model.ImageModel;
import se233.TermProjectI.model.ChooseType;
import se233.TermProjectI.model.Constants;
import se233.TermProjectI.util.SaveImageThread;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class WaterMarkController {

    private ObservableList<ImageModel> listImageModel;
    private ChooseType chooseType;

    public WaterMarkController(ObservableList<ImageModel> listImageModel, ChooseType chooseType) {
        this.listImageModel = listImageModel;
        this.chooseType = chooseType;
    }

    public void onClickSaveButton(Button button) {

        button.setOnAction(e -> {
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(Constants.TITLE_SAVE_DIALOG);
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

            File fileDialog = fileChooser.showSaveDialog(stage);
            String pathTo = fileDialog.getPath();
            File file = new File(pathTo);
            String pathParent = file.getParent();
            String fileName = fileDialog.getName();

            for (int ix = 0; ix < listImageModel.size(); ix++) {
                ImageModel i = listImageModel.get(ix);
                //have 1 image save name input
                if (listImageModel.size() == 1) {
                    SaveImageThread saveImageThread = new SaveImageThread(i, pathParent, fileName, this.chooseType);
                    saveImageThread.start();
                } else {
                    String newFileName = "(" + ix + ")" + fileName  ;

                    SaveImageThread saveImageThread = new SaveImageThread(i, pathParent, newFileName, this.chooseType);
                    saveImageThread.start();
                }
            }
            System.out.println("Save images are success");
            showSaveImageSuccess();
        });
    }

    public void setSingleText(CheckBox keepRatio) {
        keepRatio.selectedProperty().addListener((ob, o, n) -> {
            listImageModel.forEach(i -> {
                i.setSingleTextProperty(n);
                i.setModelProperty(n);
            });
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
                //active code
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


    private void showSaveImageSuccess() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Save Image with Watermark");
        alert.setHeaderText(null);
        alert.setContentText("Save Images with Watermark is successful.");

        alert.showAndWait();
    }


}
