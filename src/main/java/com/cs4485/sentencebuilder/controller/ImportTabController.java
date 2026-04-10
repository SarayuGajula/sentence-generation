package com.cs4485.sentencebuilder.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Controller for the import tab
 * @author Connor Harris
 * @author Daniel Dimitrov
 * 4/2/2026 - Dummy version implemented that doesn't actually do anything yet - Connor Harris
 * 4/10/2026 - Extracted all fields and functions for import tab from Connor's MainController - Daniel Dimitrov
 */
public class ImportTabController {
    @FXML private TextField filePathField;
    @FXML private Label importStatus;
    @FXML private TableView<?> importedFilesTable;
    @FXML private TableColumn<?, ?> fileNameCol;
    @FXML private TableColumn<?, ?> wordCountCol;
    @FXML private TableColumn<?, ?> importDateCol;

    @FXML
    protected void onBrowse() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Text File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        File file = fileChooser.showOpenDialog(filePathField.getScene().getWindow());
        if (file != null) {
            filePathField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    protected void onImport() {
        String path = filePathField.getText();
        if (path == null || path.isBlank()) {
            importStatus.setText("Please select a file first.");
            return;
        }
        // TODO: wire up Tokenizer + WordAnalyzer + DAO calls here
        importStatus.setText("Import not yet implemented.");
    }
}
