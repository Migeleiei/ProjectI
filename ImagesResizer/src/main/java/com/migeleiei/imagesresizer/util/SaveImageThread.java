package com.migeleiei.imagesresizer.util;

import com.migeleiei.imagesresizer.model.ChooseType;
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
    private final ChooseType chooseType;

    public SaveImageThread(ImageModel img,
                           String path,
                           String fileName,
                           ChooseType chooseType) {
        this.img = img;
        this.path = path;
        this.fileName = fileName;
        this.chooseType = chooseType;

    }

    @Override
    public void run() {
        super.run();


        switch (this.chooseType) {
            case WATERMARK -> {
                addTextWatermark(this.img, new File(path + fileName));
            }
            case RESIZE -> {

                resizeImage(this.img, new File(path + fileName));

            }

        }


    }

    private void resizeImage(ImageModel img, File destImageFile) {

        int percent = img.percentPropertyProperty().get() / 100;

        int width = img.widthImagePropertyProperty().get() * percent;
        int height = img.heightImagePropertyProperty().get() * percent;

        if (width == 0) {
            width = img.bufferedImageProperty().get().getWidth();
        } else if (height == 0) {
            height = img.bufferedImageProperty().get().getHeight();
        }

        if (img.keepRatioPropertyProperty().get()) {

            if (img.heightImagePropertyProperty().get() == 0 && img.widthImagePropertyProperty().get() == 0) {
                width = img.bufferedImageProperty().get().getWidth() * percent;
                height = img.bufferedImageProperty().get().getHeight() * percent;
            } else if (img.heightImagePropertyProperty().get() == 0) {
                double scale = (double) img.bufferedImageProperty().get().getHeight() / img.bufferedImageProperty().get().getWidth();


                width = img.widthImagePropertyProperty().get() * percent;
                height = (int) (width * scale);

            } else if (img.widthImagePropertyProperty().get() == 0) {
                double scale = (double) img.bufferedImageProperty().get().getWidth() / img.bufferedImageProperty().get().getHeight();


                height = img.heightImagePropertyProperty().get() * percent;
                width = (int) (height * scale);
            } else {

                double scale = (double) img.bufferedImageProperty().get().getHeight() / img.bufferedImageProperty().get().getWidth();


                width = img.widthImagePropertyProperty().get() * percent;

                height = (int) (width * scale);


            }
        }


        try {
            BufferedImage sourceImage = img.bufferedImageProperty().get();
            //resize
            BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = resizedImage.createGraphics();
            graphics2D.drawImage(sourceImage, 0, 0, width, height, null);
            graphics2D.dispose();
            // resize

            ImageIO.write(resizedImage, "png", destImageFile);



        } catch (IOException ex) {
            System.err.println(ex);
        }
    }


    /**
     * Author MigelEiEi
     * <p>
     * For save image by from source convert to Bufferimage and save to
     * another such as png jeg jpeg
     *
     * @param img
     * @param destImageFile
     */
    public void addTextWatermark(ImageModel img, File destImageFile) {


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


            g2d.rotate(Math.toRadians(img.textRotateProperty().doubleValue()), centerX + rect.getWidth() / 2, centerY);
            g2d.drawString(img.textWaterMarkProperty().getValue(), centerX, centerY);

            ImageIO.write(sourceImage, "png", destImageFile);

            g2d.dispose();



        } catch (IOException ex) {
            System.err.println(ex);
        }
    }


}
