package com.cs4485.sentencebuilder.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.cs4485.sentencebuilder.Tokenizer;
import com.cs4485.sentencebuilder.WordAnalyzer;
import com.cs4485.sentencebuilder.model.entity.Bigram;
import com.cs4485.sentencebuilder.model.entity.Word;
import com.cs4485.sentencebuilder.model.entity.Source;
import com.cs4485.sentencebuilder.model.dao.WordDAO;
import com.cs4485.sentencebuilder.model.dao.BigramDAO;
import com.cs4485.sentencebuilder.model.dao.SourceDAO;

/**
 * Controller for the import tab
 * @author Connor Harris
 * @author Daniel Dimitrov
 * @author Sarayu Gajula
 * 4/2/2026 - Dummy version implemented that doesn't actually do anything yet - Connor Harris
 * 4/10/2026 - Extracted all fields and functions for import tab from Connor's MainController - Daniel Dimitrov
 * 4/21/2026 - Wired up Tokenizer, WordAnalyzer, and DAOs to save to database - Sarayu Gajula
 * 4/21/2026 - Made importing a background task so that it doesn't freeze your screen when importing large files - Connor Harris
 * 4/22/2026 - Changed to batch update - Connor Harris
 * 5/1/2026 - Added source saving and table display - Sarayu Gajula
 */
public class ImportTabController {
    @FXML private TextField filePathField;
    @FXML private Label importStatus;
    @FXML private TableView<Source> importedFilesTable;
    @FXML private TableColumn<Source, String> fileNameCol;
    @FXML private TableColumn<Source, Integer> wordCountCol;
    @FXML private TableColumn<Source, Integer> importDateCol;

    // Runs automatically when the tab loads - Sarayu Gajula
    @FXML
    public void initialize() {
        // Hook up table columns to Source fields - Sarayu Gajula
        fileNameCol.setCellValueFactory(new PropertyValueFactory<>("filename"));
        wordCountCol.setCellValueFactory(new PropertyValueFactory<>("wordCount"));

        // Load existing sources from database into table - Sarayu Gajula
        refreshTable();
    }

    // Fetches all sources from database and displays them in the table - Sarayu Gajula
    private void refreshTable() {
        SourceDAO sourceDAO = new SourceDAO();
        List<Source> sources = sourceDAO.getAll();
        ObservableList<Source> data = FXCollections.observableArrayList(sources);
        importedFilesTable.setItems(data);
    }

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

        Task<Void> importTask = new Task<>() {
            @Override
            protected Void call() throws IOException {
                // Step 1: Read the file using Tokenizer - Sarayu Gajula
                updateMessage("Reading file…");
                String text = Tokenizer.readFile(path);

                // Step 2: Tokenize the text into a list of words - Sarayu Gajula
                List<String> tokens = Tokenizer.tokenize(text);

                // Step 3: Get word counts and bigrams from WordAnalyzer - Sarayu Gajula
                Map<String, Word> words = WordAnalyzer.getWords(tokens);
                Map<String, Bigram> bigrams = WordAnalyzer.getBigrams(tokens);

                // Step 4: Save all words to the database in one batch - Sarayu Gajula
                updateMessage("Saving " + words.size() + " words…");
                WordDAO wordDAO = new WordDAO();
                wordDAO.insertOrUpdateBatch(words.values());

                // Step 5: Save all bigrams to the database in one batch - Sarayu Gajula
                updateMessage("Saving " + bigrams.size() + " bigrams…");
                BigramDAO bigramDAO = new BigramDAO();
                bigramDAO.insertOrUpdateBatch(bigrams.values());

                // Step 6: Save the source file record to the database - Sarayu Gajula
                File file = new File(path);
                Source source = new Source(file.getName());
                source.setWordCount(tokens.size());
                SourceDAO sourceDAO = new SourceDAO();
                sourceDAO.insert(source);

                updateMessage("Successfully imported " + words.size() + " unique words.");
                return null;
            }
        };

        importStatus.textProperty().bind(importTask.messageProperty());

        importTask.setOnSucceeded(e -> {
            importStatus.textProperty().unbind();
            refreshTable();
        });

        importTask.setOnFailed(e -> {
            importStatus.textProperty().unbind();
            importStatus.setText("Error: " + importTask.getException().getMessage());
        });

        Thread thread = new Thread(importTask);
        thread.setDaemon(true);
        thread.start();
    }
}
