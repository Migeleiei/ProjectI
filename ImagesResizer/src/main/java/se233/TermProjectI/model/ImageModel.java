package se233.TermProjectI.model;

import javafx.beans.property.*;

import java.awt.image.BufferedImage;

//INfo img
public class ImageModel {

    // new for BufferedImage
    //property declare การเปลี่ยนแปลง
    private final ObjectProperty<BufferedImage> bufferedImageProperty = new SimpleObjectProperty<>();
    private String pathImage;
    private String imageName;
    private final StringProperty textWaterMarkColor = new SimpleStringProperty();
    private final StringProperty textWaterMarkFont = new SimpleStringProperty();

    private final IntegerProperty textWaterMarkSize = new SimpleIntegerProperty();
    private final StringProperty textWaterMark = new SimpleStringProperty();

    //ไว้ดูการเปลี่ยนแปลง
    private final ObjectProperty<Object> isChange = new SimpleObjectProperty<>();

    private final DoubleProperty textOpacityProperty = new SimpleDoubleProperty();

    private final DoubleProperty textRotate = new SimpleDoubleProperty();

// for resize properties

    private final BooleanProperty keepRatioProperty = new SimpleBooleanProperty();

    private final IntegerProperty widthImageProperty = new SimpleIntegerProperty();
    private final IntegerProperty heightImageProperty = new SimpleIntegerProperty();


    private final IntegerProperty percentProperty = new SimpleIntegerProperty();

    public ImageModel(String path, String color, int fontSize, String fontFamily, double opacity,
                      BufferedImage bufferedImage, String imageName, boolean keepRatio,
                      int height,
                      int width) {
        this.textWaterMark.set("");
        this.pathImage = path;
        this.textWaterMarkColor.set(color);
        this.textWaterMarkSize.set(fontSize);
        this.setTextWaterMarkFont(fontFamily);
        this.textOpacityProperty.set(opacity);
        this.textRotateProperty().set(0.0);
        this.bufferedImageProperty.set(bufferedImage);
        this.imageName = imageName;
        this.keepRatioProperty.set(keepRatio);
        this.heightImageProperty.set(height);
        this.widthImageProperty.set(width);
        this.setPercentProperty(100);


    }

    public String getImageName() {
        return this.imageName;
    }


    public ObjectProperty<BufferedImage> bufferedImageProperty() {
        return this.bufferedImageProperty;
    }

    public void setBufferedImageProperty(BufferedImage bufferedImage) {
        this.bufferedImageProperty.set(bufferedImage);
    }

    public void setTextOpacityProperty(double opacity) {
        this.textOpacityProperty.set(opacity);
    }

    public DoubleProperty textOpacityProperty() {
        return textOpacityProperty;
    }


    public void setModelProperty(Object isChange) {
        this.isChange.set(isChange);
    }

    public ObjectProperty<Object> getModelProperty() {
        return this.isChange;
    }

    public void setRotate(Double rotate) {
        this.textRotate.set(rotate);
    }

    public DoubleProperty textRotateProperty() {
        return this.textRotate;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public StringProperty textWaterMarkColorProperty() {
        return textWaterMarkColor;
    }

    public void setTextWaterMarkColor(String textWaterMarkColor) {

        this.textWaterMarkColor.set(textWaterMarkColor);

    }

    public StringProperty textWaterMarkFontProperty() {
        return textWaterMarkFont;
    }

    public void setTextWaterMarkFont(String textWaterMarkFont) {
        this.textWaterMarkFont.set(textWaterMarkFont);

    }

    public IntegerProperty getTextWaterMarkSize() {
        return textWaterMarkSize;

    }


    public void setTextWaterMarkSize(int textWaterMarkSize) {
        this.textWaterMarkSize.set(textWaterMarkSize);
    }

    public StringProperty textWaterMarkProperty() {
        return this.textWaterMark;
    }

    public void setTextWaterMarkProperty(String text) {
        this.textWaterMark.set(text);
    }


    public BooleanProperty keepRatioPropertyProperty() {
        return keepRatioProperty;
    }

    public void setKeepRatioProperty(boolean keepRatioProperty) {
        this.keepRatioProperty.set(keepRatioProperty);
    }


    public IntegerProperty percentPropertyProperty() {
        return percentProperty;
    }

    public void setPercentProperty(int percentProperty) {
        this.percentProperty.set(percentProperty);
    }


    public IntegerProperty widthImagePropertyProperty() {
        return widthImageProperty;
    }

    public void setWidthImageProperty(int widthImageProperty) {
        this.widthImageProperty.set(widthImageProperty);
    }


    public IntegerProperty heightImagePropertyProperty() {
        return heightImageProperty;
    }

    public void setHeightImageProperty(int heightImageProperty) {
        this.heightImageProperty.set(heightImageProperty);
    }


}
