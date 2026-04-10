package com.cs4485.sentencebuilder.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Controller for the words tab
 * @author Connor Harris
 * @author Daniel Dimitrov
 * 4/2/2026 - Dummy version implemented that doesn't actually do anything yet - Connor Harris
 * 4/10/2026 - Extracted all fields and functions for words tab from Connor's MainController - Daniel Dimitrov
 */
public class WordsTabController {
    @FXML private ComboBox<String> wordSortCombo;
    @FXML private TableView<?> wordsTable;
    @FXML private TableColumn<?, ?> wordCol;
    @FXML private TableColumn<?, ?> totalCountCol;
    @FXML private TableColumn<?, ?> startCountCol;
    @FXML private TableColumn<?, ?> endCountCol;

    @FXML
    public void initialize() {
        wordSortCombo.getItems().addAll(
            "Alphabetical",
            "Frequency (High to Low)",
            "Frequency (Low to High)",
            "Sentence Start Count",
            "Sentence End Count"
        );
        wordSortCombo.setValue("Alphabetical");
    }

    @FXML
    protected void onSortWords() {
        // TODO: re-sort wordsTable based on wordSortCombo.getValue()
    }

    @FXML
    protected void onRefreshWords() {
        // TODO: reload word list from database
    }
}
