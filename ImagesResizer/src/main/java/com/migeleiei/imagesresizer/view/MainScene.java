package com.migeleiei.imagesresizer.view;


import com.migeleiei.imagesresizer.model.ImageModel;
import com.migeleiei.imagesresizer.model.ChooseType;
import com.migeleiei.imagesresizer.model.Constants;
import javafx.beans.property.*;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;



import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.*;


public class MainScene {

    private final ChooseType chooseType;
    private final ObservableList<ImageModel> listImageModel;

    public MainScene(ChooseType chooseType, ObservableList<ImageModel> listImageModel) {
        this.chooseType = chooseType;
        this.listImageModel = listImageModel;
    }


    IntegerProperty columnProperty = new SimpleIntegerProperty();


    public Scene mainScene() throws URISyntaxException {
        HBox mainPain = new HBox();

        VBox leftPane = new VBox();
        VBox rightPane = new VBox();

        WaterMarkPropertyPane waterMarkPropertyPane = new WaterMarkPropertyPane(listImageModel);
        ResizePropertyPane resizePropertyPane = new ResizePropertyPane(listImageModel);


        rightPane.setBackground(Background.fill(Color.valueOf("#919aea")));

        leftPane.prefWidthProperty().bind(mainPain.widthProperty().divide(4 / 2));
        leftPane.prefHeightProperty().bind(mainPain.heightProperty());
        //rightSide_of_functions
        rightPane.prefWidthProperty().bind(mainPain.widthProperty().divide(4 / 2));
        rightPane.prefHeightProperty().bind(mainPain.heightProperty());

        switch (chooseType) {
            case WATERMARK -> {
                rightPane.getChildren().add(waterMarkPropertyPane);
            }
            case RESIZE -> {
                rightPane.getChildren().add(resizePropertyPane);
            }
        }


        leftPane.setAlignment(Pos.TOP_LEFT);
        rightPane.setAlignment(Pos.CENTER);

        mainPain.getChildren().add(imageList(leftPane));
        mainPain.getChildren().add(rightPane);

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



}
