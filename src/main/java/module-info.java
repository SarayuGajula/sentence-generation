module com.cs4485.sentencebuilder {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;


    opens com.cs4485.sentencebuilder to javafx.fxml;
    exports com.cs4485.sentencebuilder;
    exports com.cs4485.sentencebuilder.controller;
    exports com.cs4485.sentencebuilder.model.entity; // for testing
    opens com.cs4485.sentencebuilder.controller to javafx.fxml;
}