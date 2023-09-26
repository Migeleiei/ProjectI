package com.migeleiei.imagesresizer.view;

import com.migeleiei.imagesresizer.Launcher;
import com.migeleiei.imagesresizer.model.ImageModel;
import com.migeleiei.imagesresizer.util.Constants;
import com.migeleiei.imagesresizer.util.UtilImage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class DropScene {
    ObservableList<ImageModel> listImageModel = FXCollections.observableArrayList();

    public Scene dropScene() throws URISyntaxException {
        Scene scene = new Scene(dropPane(),400,400);
        return scene;
    }


    public HBox dropPane() throws URISyntaxException {
        List<String> pathListImage = new ArrayList<>();


        HBox hBox = new HBox();
        VBox vBox = new VBox();

        vBox.setFillWidth(true);
        hBox.setFillHeight(true);
        vBox.setAlignment(Pos.CENTER);
        hBox.setAlignment(Pos.CENTER);

        String imagePath = Objects.requireNonNull(Launcher.class.getResource("/images/dropfile.png")).toURI().toString();
        ImageView image = new ImageView(imagePath);

        Button openBtn = new Button("Choose Files");
        vBox.getChildren().add(image);
//        vBox.getChildren().add(openBtn);
        vBox.setSpacing(10);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Multiple Files");
        Stage stage = new Stage();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
        openBtn.setOnAction(e -> {
            List<File> listFile = fileChooser.showOpenMultipleDialog(stage);
            if (listFile != null) {
                listFile.forEach(f -> {
                    ImageModel imageModel = null;
                    try {
                        imageModel = new ImageModel(
                                f.getPath(),
                                Constants.DEFAULT_TEXT_COLOR,
                                Constants.DEFAULT_TEXT_SIZE,
                                Constants.DEFAULT_TEXT_FONT,
                                Constants.DEFAULT_TEXT_OPACITY,
                                ImageIO.read(new File(f.getPath())),
                                f.getName()
                        );
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    listImageModel.add(imageModel);
//                    pathListImage.add(f.getPath());
                });
                //            paneController.setPaneDropProperty(true);
//                paneController.setImageModelList(FXCollections.observableList(listImageModel));
            }


        });


        hBox.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            boolean isError = false;

            boolean success = false;
            String filePath = null;
            if (db.hasFiles()) {
                success = true;

                for (File file : db.getFiles()) {
                    String fileEx = getFileExtension(file.getAbsolutePath());
                    if (fileEx.equals("zip")) {
                        boolean isZip = readZipFile(file);

                        isError = !isZip;
                    } else {

                        filePath = file.getAbsolutePath();
                        String fileType = getFileExtension(filePath);

                        if (Constants.LIST_FILE_IMAGE.contains(fileType)) {
                            ImageModel imageModel = null;
                            try {
                                imageModel = new ImageModel(
                                        filePath,
                                        Constants.DEFAULT_TEXT_COLOR,
                                        Constants.DEFAULT_TEXT_SIZE,
                                        Constants.DEFAULT_TEXT_FONT,
                                        Constants.DEFAULT_TEXT_OPACITY,
                                        ImageIO.read(new File(filePath)),
                                        file.getName()
                                );
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            listImageModel.add(imageModel);
//                            pathListImage.add(filePath);
                            isError = false;
                        } else {
                            isError = true;
                            showErrorTypeDialog();
                        }


                    }
                }

                e.setDropCompleted(success);
                if (!isError) {
//                    paneController.setPaneDropProperty(true);
//                    paneController.setImageModelList(FXCollections.observableList(listImageModel));
                }

                e.consume();
            }
        });


        hBox.setOnDragOver(e -> {
            Dragboard db = e.getDragboard();
            if (db.hasFiles()) {
                e.acceptTransferModes(TransferMode.COPY);
            } else {
                e.consume();
            }
        });

        hBox.getChildren().add(vBox);
        return hBox;
    }
    private String getFileExtension(String fullFileName) {
        String fileFullName = new File(fullFileName).getName();
        int lastDot = fileFullName.lastIndexOf('.');
        return (lastDot == -1) ? "" : fileFullName.substring(lastDot + 1);
    }
    private boolean readZipFile(File file) {
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(file.getAbsolutePath());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        try {
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            BufferedInputStream bis = new BufferedInputStream(fis);
            ZipInputStream zis = new ZipInputStream(bis);

            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                InputStream inputStream = zipFile.getInputStream(zipEntry);
                String extension = getFileExtension(zipEntry.getName());

                if (Constants.LIST_FILE_IMAGE.contains(extension)) {
                    BufferedImage bf = ImageIO.read(inputStream);

                    Stage stage1 = new Stage();

                    Pane pane = new Pane();
                    Scene scene = new Scene(pane);
                    Image img = UtilImage.convertToFxImage(bf);
                    ImageView imageView = new ImageView(img);
                    pane.getChildren().add(imageView);
                    stage1.setScene(scene);
                    stage1.show();

                } else {
                    showErrorTypeDialog();

                    return false;
                }
            }


        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return true;

    }
    private void showErrorTypeDialog() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(Constants.CONTENT_TYPE_ERROR);
        alert.setTitle(Constants.TITLE_TYPE_ERROR);
        alert.show();
    }

}
