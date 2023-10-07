package com.migeleiei.imagesresizer.controller;

import com.migeleiei.imagesresizer.model.ImageModel;
import com.migeleiei.imagesresizer.model.ChooseType;
import com.migeleiei.imagesresizer.model.Constants;
import com.migeleiei.imagesresizer.util.SaveImageThread;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ResizeController {
    private final ObservableList<ImageModel> listImageModel;
    private final ChooseType chooseType;


    public ResizeController(ObservableList<ImageModel> listImageModel, ChooseType chooseType) {
        this.listImageModel = listImageModel;

        this.chooseType = chooseType;
    }

    //set the ratio
    public void setKeepRatio(CheckBox keepRatio) {
        keepRatio.selectedProperty().addListener((ob, o, n) -> {
            listImageModel.forEach(i -> {
                i.setKeepRatioProperty(n);
                i.setModelProperty(n);
            });
        });
    }

    
    public void addWidthListener(final TextField textField) {
        textField.textProperty().addListener((ob, o, n) -> {

            if (n.isBlank() || n.isEmpty()) {
                listImageModel.forEach(i -> {
                    i.setWidthImageProperty(0);
                    i.setModelProperty(0);
                });
            }

            if (isNumber(n)) {
                int width = Integer.parseInt(n);

                listImageModel.forEach(i -> {
                    i.setWidthImageProperty(width);
                    i.setModelProperty(width);
                });

            } else {
                textField.setText("");
            }
        });
    }


    public void addHeightListener(final TextField textField) {
        textField.textProperty().addListener((ob, o, n) -> {

            if (n.isBlank() || n.isEmpty()) {
                listImageModel.forEach(i -> {
                    i.setHeightImageProperty(0);
                    i.setModelProperty(0);
                });
            }

            if (isNumber(n)) {
                int height = Integer.parseInt(n);

                listImageModel.forEach(i -> {
                    i.setHeightImageProperty(height);
                    i.setModelProperty(height);

                });


            } else {
                textField.setText("");
            }
        });
    }

    public void addPercentListener(final TextField textField) {


        textField.textProperty().addListener((ob, o, n) -> {

            if (isNumber(n)) {
                int percent = Integer.parseInt(n);

                if (percent > Constants.PERCENTAGE_MAX_FOR_RESIZE_IMAGE) {
                    percent = Constants.PERCENTAGE_MAX_FOR_RESIZE_IMAGE;
                    textField.setText("" + Constants.PERCENTAGE_MAX_FOR_RESIZE_IMAGE);
                }
                ;
                if (percent == 0) {
                    listImageModel.forEach(i -> {
                        i.setPercentProperty(100);
                        i.setModelProperty(100);
                    });
                } else {

                    int finalPercent = percent;
                    listImageModel.forEach(i -> {
                        i.setPercentProperty(finalPercent);
                        i.setModelProperty(finalPercent);
                    });
                }

            } else {
                textField.setText("");
            }
        });
    }


    private Boolean isNumber(String str) {
        if (str == null || str.trim().isEmpty()) {
            // Handle empty or null input here if needed
            return false;
        }

        try {
            int intValue = Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            // This block will be executed only when the input is not a valid integer
            return false;
        }
    }

    private void showErrorTypeDialog(String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(content.isEmpty() ? Constants.CONTENT_TYPE_ERROR : content);
        alert.setTitle(Constants.TITLE_TYPE_ERROR);
        alert.show();
    }

    //set on back action
    public void onClickBackButton(Button button1) {
        button1.setOnAction(e -> {
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(Constants.TITLE_SAVE_DIALOG);
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png"));

            File fileDialog = fileChooser.showSaveDialog(stage);

            String pathTo = fileDialog.getPath();

            // Check if there are any images to save
            if (!listImageModel.isEmpty()) {
                listImageModel.forEach(i -> {
                    String fileNameDialog = fileDialog.getName();
                    String fileExtension = fileNameDialog.substring(fileNameDialog.lastIndexOf(".") + 1, fileNameDialog.length());
                    String fileName = i.getImageName();

                    SaveImageThread saveImageThread = new SaveImageThread(i, pathTo, fileName, this.chooseType);
                    saveImageThread.start();
                });

                System.out.println("Save images are success");
                showSaveImageSuccess();
            } else {
                // Handle the case where there are no images to save (e.g., show an error message).
            }

        });

        // Add the back button to your layout or UI as needed.
    }


    //set on save action

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

                    SaveImageThread saveImageThread = new SaveImageThread(i, pathTo, fileName, this.chooseType);
                    saveImageThread.start();

                } else {
                    String fileNameDialog = fileDialog.getName();
                    String fileExtension = fileNameDialog.substring(fileNameDialog.lastIndexOf(".") + 1, fileDialog.getName().length());
                    //have > 1 save same original
//                    File fileOrigi = new File(i.getPathImage());
                    String fileName = i.getImageName();

                    SaveImageThread saveImageThread = new SaveImageThread(i, pathTo, fileName, this.chooseType);
                    saveImageThread.start();


                }

            });
            System.out.println("Save images are success");
            showSaveImageSuccess();
        });
    }

    private void showSaveImageSuccess() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Save Image from resize");
        alert.setHeaderText(null);
        alert.setContentText("Save Images resize is successful.");

        alert.showAndWait();
    }

}
