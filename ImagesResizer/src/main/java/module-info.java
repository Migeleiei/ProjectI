module com.migeleiei.imagesresizer {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.migeleiei.imagesresizer to javafx.fxml;
    exports com.migeleiei.imagesresizer;
}