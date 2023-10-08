package se233.TermProjectI.view;

import se233.TermProjectI.model.ImageModel;
import se233.TermProjectI.util.UtilImage;
import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
        // cal to find the column
        StackPane imageStack = new StackPane();

        //buffer -> img (done waiting to save)
        BufferedImage bufferedImage = imgModel.bufferedImageProperty().get();

        Image image = UtilImage.convertToFxImage(bufferedImage);

        ImageView imageView = new ImageView(image);


        imageView.setPreserveRatio(true);


        imageView.fitWidthProperty().bind(newLeftPane.widthProperty().divide(intColumn).multiply(0.95));
        imageView.fitHeightProperty().bind(newLeftPane.heightProperty());


        Text textWaterMark = new Text();

        Pane p = new Pane();
        //set it to new value as real time
        imgModel.getModelProperty().addListener((ob, o, n) -> {

            setTextProperties(textWaterMark, imageView, imgModel);
            setMultiText(imageView, p);
        });

        mainGridImg.widthProperty().addListener((ob, o, n) -> {

            setTextProperties(textWaterMark, imageView, imgModel);
            setMultiText(imageView, p);
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



        imageStack.getChildren().add(rectangle);
        imageStack.getChildren().add(p);

        addClickListener(p, imgModel);
//        imageStack.getChildren().add(textWaterMark);

        return imageStack;
    }

    private Pane setMultiText(ImageView imageView, Pane p) {


        p.setLayoutX(imageView.getLayoutBounds().getWidth());
        p.setLayoutY(imageView.getLayoutBounds().getHeight());

        double width = imageView.getLayoutBounds().getWidth();

        double height = imageView.getLayoutBounds().getHeight();


        double imageScale = imageView.getLayoutBounds().getWidth() / imageView.getImage().getWidth();

        int textPx = (int) (imgModel.getTextWaterMarkSize().doubleValue() * imageScale);

        if (imgModel.singleTextPropertyProperty().get()) {
            p.getChildren().clear();
            Text text = setSingleText(textPx, width / 2, height / 2);
            p.getChildren().add(text);
        } else {

            int constantStepX = (int) width / 4;
            int constantStepY = (int) height / 4;
            int yStart = (int) height / 2;
            boolean isOddRaw = true;
            boolean isOddCol = true;
            boolean isIndent = false;

            p.getChildren().clear();
            for (int r = 0; r < 3; r++) {
                int xStart = (int) width / 2;

                if (isIndent) {
                    xStart += constantStepX / 2;
                }

                for (int c = 0; c < 3; c++) {

                    Text text = setSingleText(textPx, xStart, yStart);
                    p.getChildren().add(text);
                    xStart += isOddCol ? constantStepX * (c + 1) : -constantStepX * (c + 1);
                    isOddCol = !isOddCol;
                }

                isIndent = !isIndent;
                yStart += isOddRaw ? constantStepY * (r + 1) : -constantStepY * (r + 1);
                isOddRaw = !isOddRaw;
            }

        }
        //show text


//        p.getChildren().clear();
//        p.getChildren().add(textWatermark);

        return p;

    }

    private Text setSingleText(int textPx, double x, double y) {
        Text textWatermark = new Text();

        textWatermark.setText(imgModel.textWaterMarkProperty().getValue());


        textWatermark.setFill(Paint.valueOf(imgModel.textWaterMarkColorProperty().getValue()));
        textWatermark.setOpacity(imgModel.textOpacityProperty().doubleValue());
        textWatermark.setRotate(imgModel.textRotateProperty().doubleValue());
        textWatermark.setFont(Font.font(imgModel.textWaterMarkFontProperty().getValue(), FontWeight.BLACK, FontPosture.REGULAR, textPx));

        double textWidth = textWatermark.getLayoutBounds().getWidth();
        double textHeight = textWatermark.getLayoutBounds().getHeight();

        textWatermark.setX(x - textWidth / 2);
        textWatermark.setY(y);

        return textWatermark;
    }


    private void setTextProperties(Text textWatermark, ImageView imageView, ImageModel imageModel) {

        double imageScale = imageView.getLayoutBounds().getWidth() / imageView.getImage().getWidth();
        int textPx = (int) (imageModel.getTextWaterMarkSize().doubleValue() * imageScale);
        textWatermark.setFill(Paint.valueOf(imageModel.textWaterMarkColorProperty().getValue()));
        textWatermark.setOpacity(imageModel.textOpacityProperty().doubleValue());
        textWatermark.setRotate(imageModel.textRotateProperty().doubleValue());
        textWatermark.setFont(Font.font(imageModel.textWaterMarkFontProperty().getValue(), FontWeight.BLACK, FontPosture.REGULAR, textPx));


        //show text
        textWatermark.setText(imageModel.textWaterMarkProperty().getValue());


    }


    private void addClickListener(Pane pane, ImageModel imageModel) {
        pane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
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


                //
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
                Scene scene = new Scene(root, image.getWidth(), image.getHeight());

                stage.setScene(scene);
                stage.show();


            }
        });
    }

    //return to stackPane
    @Override
    public StackPane call() throws Exception {

        return imageViewBufferedImage(this.imgModel, this.intColumn);
    }
}
