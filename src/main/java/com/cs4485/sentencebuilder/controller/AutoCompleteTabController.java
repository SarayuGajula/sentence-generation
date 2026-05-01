package com.cs4485.sentencebuilder.controller;

import com.cs4485.sentencebuilder.model.dao.WordDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.beans.value.ChangeListener;

import com.cs4485.sentencebuilder.Autocomplete;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the auto-complete tab
 * @author Connor Harris
 * @author Daniel Dimitrov
 * @author Jeffrey Gilbert
 * 4/2/2026 - Dummy version implemented that doesn't actually do anything yet - Connor Harris
 * 4/10/2026 - Extracted all fields and functions for auto-complete tab from Connor's MainController - Daniel Dimitrov
 * 4/23/2026 - Start integration work - Jeffrey Gilbert
 * 4/30/2026 - Finished bulk of integration work, still need to work out adding to db - Jeffrey Gilbert
 */
public class AutoCompleteTabController {
    @FXML
    private TextArea autocompleteInput;
    @FXML
    private ListView<String> suggestionsList;
    private WordDAO wordDAO;
    private Autocomplete ac;

    @FXML
    private void initialize() {
        ac  = new Autocomplete();

        // Add listener
        autocompleteInput.textProperty().addListener(
                (observable, oldValue, newValue) -> {onTextUpdate(newValue);});

    }

    @FXML
    protected void onTextUpdate(String text) {
        if (text == null || text.isEmpty()) { // Check if text box is empty
            suggestionsList.getItems().clear();
            return;
        }

        if(!isCutoff(text)) { // If not currently ending with a space/character, clear the suggestions list
            suggestionsList.getItems().clear();
            return;
        }

        // Get last word and generate suggestions list
        String lastWord = getLastWord(text);

        // TODO: If last word is not in database, add into database

        if  (!lastWord.isEmpty()) {
            generateSuggestions(lastWord);
        }
    }

    @FXML
    private String getLastWord(String text) {
        String trimmed = text.trim();
        int lastSpace = Math.max(
                trimmed.lastIndexOf(' '),
                Math.max(trimmed.lastIndexOf('.'), trimmed.lastIndexOf(','))
        );

        return lastSpace == -1
                ? trimmed
                : trimmed.substring(lastSpace + 1);
    }

    @FXML
    private Boolean isCutoff(String text) {
        char c = text.charAt(text.length() - 1);

        return Character.isWhitespace(c) || c == '.' || c == ',' || c == '!' || c == '?';
    }

    @FXML
    protected void generateSuggestions(String lastWord) {
        List<String> suggestions = ac.suggestThreeWords(lastWord);

        ObservableList<String> observableSuggestions =
                FXCollections.observableArrayList(suggestions);

        suggestionsList.setItems(observableSuggestions);
    }

    @FXML
    protected void onUseSuggestion() {
        String selected = suggestionsList.getSelectionModel().getSelectedItem();
        if (selected != null) { // If character is whitespace, don't add a leading space
            if (Character.isWhitespace(autocompleteInput.getText().charAt(autocompleteInput.getLength()))) {
                autocompleteInput.appendText(selected + " ");
            } else {
                autocompleteInput.appendText(" " + selected + " ");
            }
            suggestionsList.getItems().clear();
        }
    }

    @FXML
    protected void onClearAutocomplete() {
        autocompleteInput.clear();
        suggestionsList.getItems().clear();
    }
}
