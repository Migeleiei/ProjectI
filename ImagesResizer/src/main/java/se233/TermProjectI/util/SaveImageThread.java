package se233.TermProjectI.util;

import se233.TermProjectI.model.ChooseType;
import se233.TermProjectI.model.ImageModel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SaveImageThread extends Thread {

    private final ImageModel img;
    private final String path;
    private final String fileName;
    private final ChooseType chooseType;

    public SaveImageThread(ImageModel img, String path, String fileName, ChooseType chooseType) {
        this.img = img;
        this.path = path;
        this.fileName = fileName;
        this.chooseType = chooseType;
    }

    @Override
    public void run() {
        super.run();
        File abPath = new File(path, fileName);
        System.out.println("Show abs path : " + abPath.getAbsolutePath());

        switch (this.chooseType) {
            case WATERMARK:
                addTextWatermark(this.img, abPath);
                break;
            case RESIZE:
                resizeImage(this.img, abPath);
                break;
        }
    }

    private void resizeImage(ImageModel img, File destImageFile) {
        double percent = (double) img.percentPropertyProperty().get() / 100;
        int originalWidth = img.bufferedImageProperty().get().getWidth();
        int originalHeight = img.bufferedImageProperty().get().getHeight();

        int width;
        int height;

        if (percent == 1) {
            if (img.keepRatioPropertyProperty().get()) {
                if (img.widthImagePropertyProperty().get() == 0) {
                    double scale = (double) originalWidth / originalHeight;
                    height = (int) (img.heightImagePropertyProperty().get() * percent);
                    width = (int) (height * scale);
                } else {
                    double scale = (double) originalHeight / originalWidth;
                    width = (int) (img.widthImagePropertyProperty().get() * percent);
                    height = (int) (width * scale);
                }
            } else {
                width = img.widthImagePropertyProperty().get();
                height = img.heightImagePropertyProperty().get();
            }
        } else {
            width = (int) (originalWidth * percent);
            height = (int) (originalHeight * percent);
        }

        try {
            BufferedImage sourceImage = img.bufferedImageProperty().get();
            BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = resizedImage.createGraphics();
            graphics2D.drawImage(sourceImage, 0, 0, width, height, null);
            graphics2D.dispose();
            ImageIO.write(resizedImage, "png", destImageFile);
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public void addTextWatermark(ImageModel img, File destImageFile) {
        try {
            BufferedImage sourceImage = img.bufferedImageProperty().get();
            Graphics2D g2d = (Graphics2D) sourceImage.getGraphics();
            AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) img.textOpacityProperty().doubleValue());
            g2d.setComposite(alphaChannel);
            g2d.setColor(Color.decode(img.textWaterMarkColorProperty().getValue()));
            g2d.setFont(new Font(img.textWaterMarkFontProperty().getValue(), Font.BOLD, img.getTextWaterMarkSize().intValue()));
            FontMetrics fontMetrics = g2d.getFontMetrics();
            Rectangle2D rect = fontMetrics.getStringBounds(img.textWaterMarkProperty().getValue(), g2d);
            int centerX = (sourceImage.getWidth() - (int) rect.getWidth()) / 2;
            int centerY = sourceImage.getHeight() / 2;
            g2d.rotate(Math.toRadians(img.textRotateProperty().doubleValue()), centerX + rect.getWidth() / 2, centerY);
            g2d.drawString(img.textWaterMarkProperty().getValue(), centerX, centerY);
            ImageIO.write(sourceImage, "png", destImageFile);
            g2d.dispose();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}
