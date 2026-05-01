package com.cs4485.sentencebuilder.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.cs4485.sentencebuilder.Tokenizer;
import com.cs4485.sentencebuilder.WordAnalyzer;
import com.cs4485.sentencebuilder.model.dao.BigramDAO;
import com.cs4485.sentencebuilder.model.dao.SourceDAO;
import com.cs4485.sentencebuilder.model.dao.WordDAO;
import com.cs4485.sentencebuilder.model.entity.Bigram;
import com.cs4485.sentencebuilder.model.entity.Source;
import com.cs4485.sentencebuilder.model.entity.Word;

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
    @FXML private TableColumn<Source, String> importDateCol;

    /**
     * Runs automatically when the Import tab loads.
     */
    @FXML
    public void initialize() {
        // Connect the File Name column to Source.getFilename()
        fileNameCol.setCellValueFactory(new PropertyValueFactory<>("filename"));

        // Connect the Word Count column to Source.getWordCount()
        wordCountCol.setCellValueFactory(new PropertyValueFactory<>("wordCount"));

        // Source currently has no importedAt/date field, so this prevents the column from being blank/erroring.
        importDateCol.setCellValueFactory(cellData -> new SimpleStringProperty("N/A"));

        // Load already imported sources from the database into the table.
        refreshTable();
    }

    /**
     * Fetches all sources from the database and displays them in the table.
     */
    private void refreshTable() {
        SourceDAO sourceDAO = new SourceDAO();
        List<Source> sources = sourceDAO.getAll();
        ObservableList<Source> data = FXCollections.observableArrayList(sources);
        importedFilesTable.setItems(data);
    }

    /**
     * Opens a file chooser so the user can select a .txt file.
     */
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

    /**
     * Imports the selected file, tokenizes it, analyzes it, saves results to the database,
     * saves the source record, and refreshes the table.
     */
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
                // Step 1: Read the selected file using Tokenizer
                updateMessage("Reading file...");
                String text = Tokenizer.readFile(path);

                // Step 2: Tokenize the file text into words
                updateMessage("Tokenizing file...");
                List<String> tokens = Tokenizer.tokenize(text);

                // Step 3: Analyze words and bigrams
                updateMessage("Analyzing words and bigrams...");
                Map<String, Word> words = WordAnalyzer.getWords(tokens);
                Map<String, Bigram> bigrams = WordAnalyzer.getBigrams(tokens);

                // Step 4: Save all words to the database using batch insert/update
                updateMessage("Saving " + words.size() + " unique words...");
                WordDAO wordDAO = new WordDAO();
                boolean wordsSaved = wordDAO.insertOrUpdateBatch(words.values());

                if (!wordsSaved) {
                    throw new IOException("Words could not be saved to the database.");
                }

                // Step 5: Save all bigrams to the database using batch insert/update
                updateMessage("Saving " + bigrams.size() + " bigrams...");
                BigramDAO bigramDAO = new BigramDAO();
                boolean bigramsSaved = bigramDAO.insertOrUpdateBatch(bigrams.values());

                if (!bigramsSaved) {
                    throw new IOException("Bigrams could not be saved to the database.");
                }

                // Step 6: Save the imported source file record to the database
                File importedFile = new File(path);
                Source source = new Source(importedFile.getName());
                source.setWordCount(tokens.size());

                SourceDAO sourceDAO = new SourceDAO();
                boolean sourceSaved = sourceDAO.insert(source);

                if (!sourceSaved) {
                    throw new IOException("Source file could not be saved to the database.");
                }

                updateMessage("Successfully imported " + tokens.size() + " words from " + importedFile.getName() + ".");
                return null;
            }
        };

        // Show progress messages while the import runs.
        importStatus.textProperty().bind(importTask.messageProperty());

        importTask.setOnSucceeded(e -> {
            importStatus.textProperty().unbind();
            refreshTable();
            importStatus.setText("Import completed successfully.");
        });

        importTask.setOnFailed(e -> {
            importStatus.textProperty().unbind();

            if (importTask.getException() != null && importTask.getException().getMessage() != null) {
                importStatus.setText("Error: " + importTask.getException().getMessage());
            } else {
                importStatus.setText("Error importing file.");
            }
        });

        Thread thread = new Thread(importTask);
        thread.setDaemon(true);
        thread.start();
    }
}
