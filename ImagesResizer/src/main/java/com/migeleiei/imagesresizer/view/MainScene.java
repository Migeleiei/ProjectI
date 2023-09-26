package com.migeleiei.imagesresizer.view;

import com.migeleiei.imagesresizer.Launcher;
import com.migeleiei.imagesresizer.model.ImageModel;
import com.migeleiei.imagesresizer.model.ImageViewWithPosition;
import com.migeleiei.imagesresizer.util.Constants;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class MainScene {

    public MainScene() {

    }

    ListProperty<ImageModel> paneController = new SimpleListProperty<>();
    ObservableList<ImageModel> listImageModel = FXCollections.observableArrayList();
    IntegerProperty columnProperty = new SimpleIntegerProperty();


    public Scene mainScene() throws URISyntaxException {
        HBox mainPain = new HBox();

        VBox leftPane = new VBox();
        VBox rightPane = new VBox();

        WaterMarkPropertyPane waterMarkPropertyPane = new WaterMarkPropertyPane(listImageModel);

        leftPane.setBackground(Background.fill(Color.valueOf("#e27171")));
        rightPane.setBackground(Background.fill(Color.valueOf("#919aea")));

        leftPane.prefWidthProperty().bind(mainPain.widthProperty().divide(4 / 2));
        leftPane.prefHeightProperty().bind(mainPain.heightProperty());

        rightPane.prefWidthProperty().bind(mainPain.widthProperty().divide(4 / 2));
        rightPane.prefHeightProperty().bind(mainPain.heightProperty());


        leftPane.getChildren().add(dropPane());
        rightPane.getChildren().add(waterMarkPropertyPane);


        leftPane.setAlignment(Pos.CENTER);
        rightPane.setAlignment(Pos.CENTER);

        mainPain.getChildren().add(leftPane);
        mainPain.getChildren().add(rightPane);

        paneController.addListener((ob, o, n) -> {
            VBox newLeftPane = new VBox();

            newLeftPane.setAlignment(Pos.TOP_LEFT);

            mainPain.getChildren().remove(leftPane);
            mainPain.getChildren().remove(rightPane);

            newLeftPane.prefWidthProperty().bind(mainPain.widthProperty().divide(4 / 2));
            newLeftPane.prefHeightProperty().bind(mainPain.heightProperty());

            mainPain.getChildren().add(imageList(newLeftPane));
            mainPain.getChildren().add(rightPane);
        });

        Scene scene = new Scene(mainPain, 800, 600);

        return scene;
    }


    private VBox imageList(VBox vBox) {

        Pane gridPane = createImageGrid(vBox, listImageModel);
        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        vBox.getChildren().add(scrollPane);

        return vBox;
    }


    private HBox dropPane() throws URISyntaxException {


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
                    paneController.set(FXCollections.observableList(listImageModel));

                });
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

                            isError = false;
                        } else {
                            isError = true;
                            showErrorTypeDialog("");
                        }

                    }
                }

                e.setDropCompleted(success);
                if (!isError) {
                    paneController.set(FXCollections.observableList(listImageModel));
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


    private boolean readZipFile(File file) {
        ZipFile zipFile = null;
        ZipInputStream zis;
        BufferedInputStream bis;
        FileInputStream fis;
        try {
            zipFile = new ZipFile(file.getAbsolutePath());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        try {
            fis = new FileInputStream(file.getAbsolutePath());
            bis = new BufferedInputStream(fis);
            zis = new ZipInputStream(bis);

            ZipEntry zipEntry;

            while ((zipEntry = zis.getNextEntry()) != null) {

                InputStream inputStream = zipFile.getInputStream(zipEntry);
                String extension = getFileExtension(zipEntry.getName());

                String hiddenFile = zipEntry.getName();
                char symbol = hiddenFile.charAt(0);

                if (!Constants.LIST_HIDDEN_FILE.contains(symbol)) {
                    if (Constants.LIST_FILE_IMAGE.contains(extension)) {


                        BufferedImage bf = ImageIO.read(inputStream);

                        ImageModel imageModel = new ImageModel(
                                zipEntry.getName(),
                                Constants.DEFAULT_TEXT_COLOR,
                                Constants.DEFAULT_TEXT_SIZE,
                                Constants.DEFAULT_TEXT_FONT,
                                Constants.DEFAULT_TEXT_OPACITY,
                                bf,
                                zipEntry.getName()
                        );

                        listImageModel.add(imageModel);


                    }
                    if (listImageModel.isEmpty()) {
                        showErrorTypeDialog(Constants.CONTENT_ZIP_DONT_HAVE_IMAGE);
                        return false;
                    }
                    //without check error because filter only image
//                    else {
//                        showErrorTypeDialog();
//                        return false;
//                    }
                }

            }

            zipFile.close();
            fis.close();
            bis.close();
            zis.close();


        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }


        return true;
    }


    private GridPane createImageGrid(VBox leftPane, ObservableList<ImageModel> listImageModel) {

        GridPane mainGridImg = new GridPane();
        mainGridImg.setAlignment(Pos.TOP_LEFT);
        mainGridImg.setHgap(5);
        mainGridImg.setVgap(5);

        leftPane.widthProperty().addListener((ob, o, n) -> {
            double width = leftPane.widthProperty().doubleValue();
            double column = width / Constants.DEFAULT_IMAGE_MIN_WIDTH;
            column = column < 1 ? 1 : Math.floor(column);
            int intColumn = (int) column;

//            int intRow = (int) Math.ceil((double) listImageModel.size() / intColumn);

            if (intColumn != columnProperty.intValue()) {
                columnProperty.set(intColumn);
            }
        });


        int indexRow = 0;
        int indexColumn = 0;

        List<StackPane> stackPanes = new ArrayList<>();


        for (ImageModel imageModel : listImageModel) {

            // MultiThread
            Callable<StackPane> task = new ImageViewThread(leftPane, mainGridImg, imageModel, columnProperty);
            ExecutorService service = Executors.newFixedThreadPool(listImageModel.size());
            Future<StackPane> result = service.submit(task);
            StackPane imageStack = null;


            try {
                imageStack = result.get();

            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }


            stackPanes.add(imageStack);
            mainGridImg.add(imageStack, indexColumn, indexRow);
            indexColumn++;
            if (indexColumn >= columnProperty.getValue()) {
                indexColumn = 0;
                indexRow++;
            }
            //stop thread
            service.shutdown();
        }

        // Update GridPand when Pane is screen size update
        columnProperty.addListener((ob, o, n) -> {
            mainGridImg.getChildren().clear();
            mainGridImg.getRowConstraints().clear();
            mainGridImg.getColumnConstraints().clear();
            int inxColumn = 0;
            int inxRow = 0;

            for (StackPane image : stackPanes) {
                mainGridImg.add(image, inxColumn, inxRow);
                inxColumn++;
                if (inxColumn >= columnProperty.getValue()) {
                    inxColumn = 0;
                    inxRow++;
                }
            }


        });

        return mainGridImg;
    }


    private String getFileExtension(String fullFileName) {
        String fileFullName = new File(fullFileName).getName();
        int lastDot = fileFullName.lastIndexOf('.');
        return (lastDot == -1) ? "" : fileFullName.substring(lastDot + 1);
    }


    private void showErrorTypeDialog(String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(content.isEmpty() ? Constants.CONTENT_TYPE_ERROR : content);
        alert.setTitle(Constants.TITLE_TYPE_ERROR);
        alert.show();
    }

}
