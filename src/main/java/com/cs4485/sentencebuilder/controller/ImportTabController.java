package com.cs4485.sentencebuilder.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;        // needed for file reading errors
import java.util.List;             // needed for list of tokens
import java.util.Map;              // needed for word and bigram maps
import com.cs4485.sentencebuilder.Tokenizer;
import com.cs4485.sentencebuilder.WordAnalyzer;
import com.cs4485.sentencebuilder.model.entity.Bigram;
import com.cs4485.sentencebuilder.model.entity.Word;

/**
 * Controller for the import tab
 * @author Connor Harris
 * @author Daniel Dimitrov
 * @author Sarayu Gajula
 * 4/2/2026 - Dummy version implemented that doesn't actually do anything yet - Connor Harris
 * 4/10/2026 - Extracted all fields and functions for import tab from Connor's MainController - Daniel Dimitrov
 * 4/11/2026 - Wired up Tokenizer and WordAnalyzer to onImport - Sarayu Gajula
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
        try {
            // Step 1: Read the file using Tokenizer - Sarayu Gajula
            String text = Tokenizer.readFile(path);

            // Step 2: Tokenize the text into a list of words - Sarayu Gajula
            List<String> tokens = Tokenizer.tokenize(text);

            // Step 3: Get word counts and bigrams from WordAnalyzer - Sarayu Gajula
            Map<String, Word> words = WordAnalyzer.getWords(tokens);
            Map<String, Bigram> bigrams = WordAnalyzer.getBigrams(tokens);

            importStatus.setText("Successfully imported " + words.size() + " unique words.");
        } catch (IOException e) {
            importStatus.setText("Error reading file: " + e.getMessage());
        }
    }
}
