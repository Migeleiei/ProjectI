package se233.TermProjectI.util;

import javafx.scene.text.Text;
import se233.TermProjectI.model.ImageModel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class UtilImage {
    private static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPreMultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPreMultiplied, null);
    }

    public static BufferedImage convertToBufferedImage(ImageModel img) {

        BufferedImage sourceImage;


        if (img.singleTextPropertyProperty().getValue()) {

            sourceImage = deepCopy(img.bufferedImageProperty().get());
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
            g2d.dispose();

        } else {
            sourceImage = deepCopy(img.bufferedImageProperty().get());
            double width = sourceImage.getWidth();
            double height = sourceImage.getHeight();
            int constantStepX = (int) width / 4;
            int constantStepY = (int) height / 4;
            int yStart = (int) height / 2;
            boolean isOddRaw = true;
            boolean isOddCol = true;
            boolean isIndent = false;


            for (int r = 0; r < 3; r++) {
                int xStart = (int) width / 2;

                if (isIndent) {
                    xStart += constantStepX / 2;
                }

                for (int c = 0; c < 3; c++) {

                    ///
                    Graphics2D g2d = (Graphics2D) sourceImage.getGraphics();
                    // initializes necessary graphic properties
                    AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) img.textOpacityProperty().doubleValue());
                    g2d.setComposite(alphaChannel);

                    g2d.setColor(Color.decode(img.textWaterMarkColorProperty().getValue()));
                    g2d.setFont(new Font(img.textWaterMarkFontProperty().getValue(), Font.BOLD, img.getTextWaterMarkSize().intValue()));
                    FontMetrics fontMetrics = g2d.getFontMetrics();
                    Rectangle2D rect = fontMetrics.getStringBounds(img.textWaterMarkProperty().getValue(), g2d);

                    // calculates the coordinate where the String is painted
                    int centerX = xStart - (int) rect.getWidth() / 2;
                    int centerY = yStart ;
                    g2d.rotate(Math.toRadians(img.textRotateProperty().doubleValue()), centerX + rect.getWidth() / 2, centerY);
                    g2d.drawString(img.textWaterMarkProperty().getValue(), centerX, centerY);
                    g2d.dispose();
                    ////


                    xStart += isOddCol ? constantStepX * (c + 1) : -constantStepX * (c + 1);
                    isOddCol = !isOddCol;
                }

                isIndent = !isIndent;
                yStart += isOddRaw ? constantStepY * (r + 1) : -constantStepY * (r + 1);
                isOddRaw = !isOddRaw;
            }


        }


        System.out.println("Success");
        return sourceImage;

    }

    //bufferImageSwing
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
