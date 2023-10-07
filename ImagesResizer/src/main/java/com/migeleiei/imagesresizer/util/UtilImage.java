package com.migeleiei.imagesresizer.util;

import com.migeleiei.imagesresizer.model.ImageModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class UtilImage{


    public static BufferedImage convertToBufferedImage(ImageModel img,int i) {

        File file = new File(img.getPathImage());
        BufferedImage sourceImage;
        try {
            sourceImage = ImageIO.read(file);
            Graphics2D g2d = (Graphics2D) sourceImage.getGraphics();

            // initializes necessary graphic properties
            AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,  (float) img.textOpacityProperty().doubleValue());
            g2d.setComposite(alphaChannel);

            g2d.setColor(Color.decode(img.textWaterMarkColorProperty().getValue()));
            g2d.setFont(new Font(img.textWaterMarkFontProperty().getValue(), Font.BOLD, img.getTextWaterMarkSize().intValue()));
            FontMetrics fontMetrics = g2d.getFontMetrics();
            Rectangle2D rect = fontMetrics.getStringBounds(img.textWaterMarkProperty().getValue(), g2d);

            // calculates the coordinate where the String is painted
            int centerX = (sourceImage.getWidth() - (int) rect.getWidth())/2;
            int centerY = sourceImage.getHeight() / 2;


            g2d.rotate(Math.toRadians(img.textRotateProperty().doubleValue()),centerX+rect.getWidth()/2,centerY);
            g2d.drawString(img.textWaterMarkProperty().getValue(),centerX , centerY);



            g2d.dispose();

            System.out.println("Success");
            return sourceImage;

        } catch (IOException ex) {
            System.err.println(ex);
            return null;
        }
    }


    private static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
    public static BufferedImage convertToBufferedImage(ImageModel img) {



        BufferedImage sourceImage =  deepCopy(img.bufferedImageProperty().get());

        Graphics2D g2d = (Graphics2D) sourceImage.getGraphics();


        // initializes necessary graphic properties
        AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,  (float) img.textOpacityProperty().doubleValue());
        g2d.setComposite(alphaChannel);

        g2d.setColor(Color.decode(img.textWaterMarkColorProperty().getValue()));
        g2d.setFont(new Font(img.textWaterMarkFontProperty().getValue(), Font.BOLD, img.getTextWaterMarkSize().intValue()));
        FontMetrics fontMetrics = g2d.getFontMetrics();
        Rectangle2D rect = fontMetrics.getStringBounds(img.textWaterMarkProperty().getValue(), g2d);

        // calculates the coordinate where the String is painted
        int centerX = (sourceImage.getWidth() - (int) rect.getWidth())/2;
        int centerY = sourceImage.getHeight() / 2;


        g2d.rotate(Math.toRadians(img.textRotateProperty().doubleValue()),centerX+rect.getWidth()/2,centerY);
        g2d.drawString(img.textWaterMarkProperty().getValue(),centerX , centerY);


        g2d.dispose();

        System.out.println("Success");
        return sourceImage;

    }


    public static Image convertToFxImage(BufferedImage image) {
        WritableImage wr = null;
        if (image != null) {
            wr = new WritableImage(image.getWidth(), image.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    pw.setArgb(x, y, image.getRGB(x, y));
                }
            }
        }

        return new ImageView(wr).getImage();
    }


}
