/**
 * This is the controller for all of the UI elements.
 * @author Connor Harris
 * 4/2/2026 - Dummy version implemented that doesn't actually do anything yet
 */

package com.cs4485.sentencebuilder;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import java.io.File;

public class MainController {

    // Import tab
    @FXML private TextField filePathField;
    @FXML private Label importStatus;
    @FXML private TableView<?> importedFilesTable;
    @FXML private TableColumn<?, ?> fileNameCol;
    @FXML private TableColumn<?, ?> wordCountCol;
    @FXML private TableColumn<?, ?> importDateCol;

    // Generate tab
    @FXML private TextField startWordField;
    @FXML private ComboBox<String> algorithmCombo;
    @FXML private TextArea generatedOutput;

    // Auto-complete tab
    @FXML private TextArea autocompleteInput;
    @FXML private ListView<String> suggestionsList;

    // Words tab
    @FXML private ComboBox<String> wordSortCombo;
    @FXML private TableView<?> wordsTable;
    @FXML private TableColumn<?, ?> wordCol;
    @FXML private TableColumn<?, ?> totalCountCol;
    @FXML private TableColumn<?, ?> startCountCol;
    @FXML private TableColumn<?, ?> endCountCol;

    // Reports tab
    @FXML private TableView<?> sentencesTable;
    @FXML private TableColumn<?, ?> sentenceCol;
    @FXML private TableColumn<?, ?> sentenceAlgoCol;
    @FXML private TableColumn<?, ?> sentenceDateCol;

    @FXML
    public void initialize() {
        algorithmCombo.getItems().addAll("Most Frequent", "Random Weighted");
        algorithmCombo.setValue("Most Frequent");

        wordSortCombo.getItems().addAll(
            "Alphabetical",
            "Frequency (High to Low)",
            "Frequency (Low to High)",
            "Sentence Start Count",
            "Sentence End Count"
        );
        wordSortCombo.setValue("Alphabetical");
    }

    // Import tab handlers

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

    // Generate tab handlers

    @FXML
    protected void onGenerate() {
        String startWord = startWordField.getText();
        if (startWord == null || startWord.isBlank()) {
            generatedOutput.setText("Please enter a starting word.");
            return;
        }
        // TODO: call sentence generation logic here
        generatedOutput.setText("Generation not yet implemented.");
    }

    // Auto-complete tab handlers

    @FXML
    protected void onUseSuggestion() {
        String selected = suggestionsList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            autocompleteInput.appendText(selected + " ");
        }
    }

    @FXML
    protected void onClearAutocomplete() {
        autocompleteInput.clear();
        suggestionsList.getItems().clear();
    }

    // Words tab handlers

    @FXML
    protected void onSortWords() {
        // TODO: re-sort wordsTable based on wordSortCombo.getValue()
    }

    @FXML
    protected void onRefreshWords() {
        // TODO: reload word list from database
    }
}
