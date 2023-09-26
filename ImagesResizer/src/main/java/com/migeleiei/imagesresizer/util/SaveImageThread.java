package com.migeleiei.imagesresizer.util;

import com.migeleiei.imagesresizer.model.ImageModel;

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

    public SaveImageThread(ImageModel img,
                           String path,
                           String fileName) {
        this.img = img;
        this.path = path;
        this.fileName = fileName;

    }

    @Override
    public void run() {
        super.run();

        addTextWatermark(this.img, new File(path + fileName));
    }


    /**
     * Author MigelEiEi
     *
     * For save image by from source convert to Bufferimage and save to
     * another such as png jeg jpeg
     *
     * @param img
     * @param destImageFile
     */
    public static void addTextWatermark(ImageModel img, File destImageFile) {


        try {
            BufferedImage sourceImage = img.bufferedImageProperty().get();
            Graphics2D g2d = (Graphics2D) sourceImage.getGraphics();

            // initializes necessary graphic properties
            AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) img.textOpacityProperty().doubleValue());
            g2d.setComposite(alphaChannel);
            g2d.setColor(Color.decode(img.textWaterMarkColorProperty().getValue()));

            g2d.setFont(new Font(img.textWaterMarkFontProperty().getValue(), Font.BOLD, img.getTextWaterMarkSize().intValue()));
            FontMetrics fontMetrics = g2d.getFontMetrics();
            Rectangle2D rect = fontMetrics.getStringBounds(img.textWaterMarkProperty().getValue(), g2d);

            // calculates the coordinate where the String is painted
            int centerX = (sourceImage.getWidth() - (int) rect.getWidth()) / 2;
            int centerY = sourceImage.getHeight() / 2;




            g2d.rotate(Math.toRadians(img.textRotateProperty().doubleValue()),centerX+rect.getWidth()/2,centerY);
            g2d.drawString(img.textWaterMarkProperty().getValue(), centerX, centerY);

            ImageIO.write(sourceImage, "png", destImageFile);
            g2d.dispose();

            System.out.println("MigelEiEi Save Image Success");

        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}
