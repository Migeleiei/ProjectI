module com.migeleiei.TermProjectI {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens se233.TermProjectI to javafx.fxml;
    exports se233.TermProjectI;
}