package se233.TermProjectI.view;

import se233.TermProjectI.controller.ResizeController;
import se233.TermProjectI.model.ImageModel;
import se233.TermProjectI.model.ChooseType;
import se233.TermProjectI.model.Constants;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ResizePropertyPane extends VBox {
    ObservableList<ImageModel> listImageModel;
    private ResizeController resizeController;

    public ResizePropertyPane(ObservableList<ImageModel> listImageModel) {
        this.listImageModel = listImageModel;
        resizeController = new ResizeController(this.listImageModel, ChooseType.RESIZE);

        //add save button
        Button saveButton = new Button("Save");
        resizeController.onClickSaveButton(saveButton);
        setAlignment(Pos.CENTER);
        getChildren().add(resizeProperty());
        getChildren().add(saveButton);

        //add back button
        Button backButton = new Button("back");
        backButton.setAlignment(Pos.TOP_LEFT);

    }


    private GridPane resizeProperty() {
        GridPane gridPane = new GridPane();

        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);


        Text width = new Text("Width");
        Text height = new Text("Height");
        Text percent = new Text("Percent");
        Text keepRatio = new Text("Keep Ratio");

        TextField widthField = new TextField();
        TextField heightField = new TextField();


        // Row 1
        gridPane.add(width, 0, 0);
        gridPane.add(widthField, 1, 0);
        gridPane.add(height, 2, 0);
        gridPane.add(heightField, 3, 0);

        // Row 2
        CheckBox cKeepRatio = new CheckBox();
        cKeepRatio.setSelected(Constants.IS_KEEP_RATIO);

        TextField percentField = new TextField();

        gridPane.add(keepRatio, 0, 1);
        gridPane.add(cKeepRatio, 1, 1);
        gridPane.add(percent, 2, 1);
        gridPane.add(percentField, 3, 1);


        // Listener
        resizeController.setKeepRatio(cKeepRatio);
        resizeController.addWidthListener(widthField);
        resizeController.addHeightListener(heightField);
        resizeController.addPercentListener(percentField);

        return gridPane;

    }
}
