package com.migeleiei.imagesresizer.view;

import com.migeleiei.imagesresizer.model.ImageModel;
import com.migeleiei.imagesresizer.util.UtilImage;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

//import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.Callable;

public class ImageViewThread implements Callable<StackPane> {

    private final VBox newLeftPane;
    private final GridPane mainGridImg;

    private final ImageModel imgModel;
    private final IntegerProperty intColumn;


    public ImageViewThread(VBox newLeftPane,
                           GridPane mainGridImg,
                           ImageModel imageModel,
                           IntegerProperty intColumn
    ) {


        this.newLeftPane = newLeftPane;
        this.mainGridImg = mainGridImg;
        this.imgModel = imageModel;
        this.intColumn = intColumn;

    }


    private StackPane imageViewBufferedImage(ImageModel imgModel, IntegerProperty intColumn) {
        StackPane imageStack = new StackPane();


        BufferedImage bufferedImage = imgModel.bufferedImageProperty().get();

        Image image = UtilImage.convertToFxImage(bufferedImage);

        ImageView imageView = new ImageView(image);


        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty().bind(newLeftPane.widthProperty().divide(intColumn).multiply(0.95));
        imageView.fitHeightProperty().bind(newLeftPane.heightProperty());


        Text textWaterMark = new Text();
//        imageStack.getChildren().add(imageView);
//        imageStack.getChildren().add(textWaterMark);

//        addImageClickListener(imageView, imgModel);


        imgModel.getModelProperty().addListener((ob, o, n) -> {

            setTextProperties(textWaterMark, imageView, imgModel);
        });

        mainGridImg.widthProperty().addListener((ob, o, n) -> {

            setTextProperties(textWaterMark, imageView, imgModel);
        });
        mainGridImg.heightProperty().addListener((ob, o, n) -> {

            setTextProperties(textWaterMark, imageView, imgModel);
        });


        //clip ImageView

        Rectangle rectangle = new Rectangle();
        double scale = (double) imgModel.bufferedImageProperty().get().getWidth() / imgModel.bufferedImageProperty().get().getHeight();

        rectangle.widthProperty().bind(newLeftPane.widthProperty().divide(intColumn).multiply(0.95));
        rectangle.heightProperty().bind(newLeftPane.widthProperty().divide(intColumn).multiply(0.95).divide(scale));


        rectangle.setArcWidth(20.0);
        rectangle.setArcHeight(20.0);

        rectangle.setFill(new ImagePattern(image));
        rectangle.setEffect(new DropShadow(20, Color.BLACK));

        addClickListener(rectangle, imgModel);

        imageStack.getChildren().add(rectangle);
        imageStack.getChildren().add(textWaterMark);

        return imageStack;
    }


    private void setTextProperties(Text textWatermark, ImageView imageView, ImageModel imageModel) {

        double imageScale = imageView.getLayoutBounds().getWidth() / imageView.getImage().getWidth();
        int textPx = (int) (imageModel.getTextWaterMarkSize().doubleValue() * imageScale);
        textWatermark.setFill(Paint.valueOf(imageModel.textWaterMarkColorProperty().getValue()));
        textWatermark.setRotate(imageModel.textRotateProperty().doubleValue());
        textWatermark.setFont(Font.font(imageModel.textWaterMarkFontProperty().getValue(), FontWeight.BLACK, FontPosture.REGULAR, textPx));


        double textWidth = textWatermark.getLayoutBounds().getWidth();
        //show text
        textWatermark.setText(imageModel.textWaterMarkProperty().getValue());


    }


    private void addImageClickListener(ImageView imageView, ImageModel imageModel) {
        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                File file = new File(imageModel.getPathImage());
                String fileName = file.getName();


//                SnapshotParameters sp = new SnapshotParameters();
//                WritableImage image = imgStack.snapshot(sp, null);

                BufferedImage image = UtilImage.convertToBufferedImage(imageModel);
                Image img = UtilImage.convertToFxImage(image);


                Pane root = new Pane();
                Stage stage = new Stage();
                stage.setTitle(fileName);

                ImageView imageV = new ImageView(img);
                imageV.setPreserveRatio(true);
                imageV.fitWidthProperty().bind(root.widthProperty());

                root.getChildren().add(imageV);



                Scene scene = new Scene(root, image.getWidth(), image.getHeight());
                stage.setScene(scene);
                stage.show();


            }
        });
    }


    private void addClickListener(Rectangle rectangle, ImageModel imageModel) {

        DoubleProperty widthProperty = new SimpleDoubleProperty();

        rectangle.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                File file = new File(imageModel.getPathImage());
                String fileName = file.getName();


                BufferedImage image = UtilImage.convertToBufferedImage(imageModel);
                Image img = UtilImage.convertToFxImage(image);


                Pane root = new Pane();
                root.setBackground(Background.fill(Paint.valueOf("#35363A")));

                Stage stage = new Stage();
                stage.setTitle(fileName);

//                ImageView imageV = new ImageView(img);
//                imageV.setPreserveRatio(true);
//                imageV.fitWidthProperty().bind(root.widthProperty());

//                root.getChildren().add(imageV);


                /////
                Rectangle rectangle = new Rectangle();
                double scale = (double) imgModel.bufferedImageProperty().get().getWidth() / imgModel.bufferedImageProperty().get().getHeight();

                rectangle.widthProperty().bind(root.widthProperty().multiply(0.9));
                rectangle.heightProperty().bind(root.widthProperty().divide(scale).multiply(0.9));
                rectangle.xProperty().bind(root.widthProperty().multiply(0.1).divide(2));
                rectangle.yProperty().bind(root.heightProperty().multiply(0.1).divide(2));



                rectangle.setArcWidth(50);
                rectangle.setArcHeight(50);


                rectangle.setFill(new ImagePattern(img));
                rectangle.setEffect(new DropShadow(20, Color.BLACK));


                root.getChildren().add(rectangle);

                //////


                Scene scene = new Scene(root, image.getWidth(), image.getHeight());

                stage.setScene(scene);
                stage.show();


            }
        });
    }


    @Override
    public StackPane call() throws Exception {

        return imageViewBufferedImage(this.imgModel, this.intColumn);
    }
}
