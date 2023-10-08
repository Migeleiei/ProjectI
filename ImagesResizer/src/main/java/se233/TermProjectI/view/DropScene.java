package se233.TermProjectI.view;

import se233.TermProjectI.Launcher;
import se233.TermProjectI.model.ImageModel;
import se233.TermProjectI.model.ChooseType;
import se233.TermProjectI.model.Constants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class DropScene {
    ObservableList<ImageModel> listImageModel = FXCollections.observableArrayList();
    private final ChooseType chooseType;

    public DropScene(ChooseType chooseType) {
        this.chooseType = chooseType;
    }

    public Scene dropScene() throws URISyntaxException {
        Scene scene = new Scene(dropPane(), 400, 400);
        return scene;
    }

    public HBox dropPane() throws URISyntaxException {
        List<String> pathListImage = new ArrayList<>();
        HBox hBox = new HBox();
        VBox vBox = new VBox();

        vBox.setFillWidth(true);
        hBox.setFillHeight(true);
        vBox.setAlignment(Pos.CENTER);
        hBox.setAlignment(Pos.CENTER);
        hBox.setBackground(Background.fill(Color.valueOf("#CBC3E3")));

        String imagePath = Objects.requireNonNull(Launcher.class.getResource("/images/dropfile.png")).toURI().toString();
        ImageView image = new ImageView(imagePath);

        vBox.getChildren().add(image);
        vBox.setSpacing(10);

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
                                BufferedImage bf = ImageIO.read(new File(filePath));
                                imageModel = new ImageModel(
                                        filePath,
                                        Constants.DEFAULT_TEXT_COLOR,
                                        Constants.DEFAULT_TEXT_SIZE,
                                        Constants.DEFAULT_TEXT_FONT,
                                        Constants.DEFAULT_TEXT_OPACITY,
                                        bf,
                                        file.getName(),
                                        Constants.IS_KEEP_RATIO,
                                        bf.getHeight(),
                                        bf.getWidth()
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
                    MainScene mainScene = new MainScene(this.chooseType, listImageModel);
                    Stage stage1 = new Stage();
                    Scene scene = null;
                    try {
                        scene = mainScene.mainScene();
                    } catch (URISyntaxException ex) {
                        throw new RuntimeException(ex);
                    }

                    if (this.chooseType == ChooseType.RESIZE) {
                        stage1.setTitle(Constants.TITLE_RESIZE_SCENE);
                    } else if (this.chooseType == ChooseType.WATERMARK) {
                        stage1.setTitle(Constants.TITLE_WATERMARK_SCENE);
                    }

                    stage1.setScene(scene);
                    stage1.show();
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

    private String getFileExtension(String fullFileName) {
        String fileFullName = new File(fullFileName).getName();
        int lastDot = fileFullName.lastIndexOf('.');
        return (lastDot == -1) ? "" : fileFullName.substring(lastDot + 1);
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
                                zipEntry.getName(),
                                Constants.IS_KEEP_RATIO,
                                bf.getHeight(),
                                bf.getWidth()
                        );

                        listImageModel.add(imageModel);
                    }

                    if (listImageModel.isEmpty()) {
                        showErrorTypeDialog(Constants.CONTENT_ZIP_DONT_HAVE_IMAGE);
                        return false;
                    }
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

    private void showErrorTypeDialog(String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(content.isEmpty() ? Constants.CONTENT_TYPE_ERROR : content);
        alert.setTitle(Constants.TITLE_TYPE_ERROR);
        alert.show();
    }
}
