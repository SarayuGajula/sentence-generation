module com.cs4485.sentencebuilder {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.cs4485.sentencebuilder to javafx.fxml;
    exports com.cs4485.sentencebuilder;
}