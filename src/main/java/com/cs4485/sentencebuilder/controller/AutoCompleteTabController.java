package com.cs4485.sentencebuilder.controller;

import com.cs4485.sentencebuilder.model.dao.WordDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.beans.value.ChangeListener;
import javafx.beans.binding.Bindings;
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
 * 5/1/2026 - Fixing issues with spacing and bugs, adding more features to the tab - Jeffrey Gilbert
 */
public class AutoCompleteTabController {
    @FXML
    private TextArea autocompleteInput;
    @FXML
    private ListView<String> suggestionsList;
    @FXML
    private Button useSuggestionButton;
    @FXML
    private Button clearAutocompleteButton;
    private WordDAO wordDAO;
    private Autocomplete ac;

    @FXML
    private void initialize() {
        ac  = new Autocomplete();

        // Add listener to call function whenever text updates
        autocompleteInput.textProperty().addListener(
                (observable, oldValue, newValue) -> {onTextUpdate(newValue);});

        // Stop user from pressing use suggestion if suggestion box is empty
        useSuggestionButton.disableProperty().bind(Bindings.isEmpty(autocompleteInput.textProperty()));

        // Stop user from pressing clear button if both text boxes are empty
        clearAutocompleteButton.disableProperty().bind(autocompleteInput.textProperty().isEmpty()
                .and(Bindings.isEmpty(suggestionsList.getItems()))
        );
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
        if (selected == null) {
            return;
        }

        String text = autocompleteInput.getText();

        // If empty or last character is whitespace, no leading space needed
        boolean endsWithWhitespace =
                Character.isWhitespace(text.charAt(text.length() - 1));

        boolean isPunctuation =
                selected.length() == 1 &&
                        !Character.isLetterOrDigit(text.charAt(0));

        // Remove trailing space if punctuation
        if (isPunctuation && endsWithWhitespace) {
            autocompleteInput.deleteText(text.length() - 1, text.length());
        }

        // Add leading space if needed
        if (!isPunctuation && !endsWithWhitespace) {
            autocompleteInput.appendText(" ");
        }

        autocompleteInput.appendText(selected);

        generateSuggestions(selected);
    }

    @FXML
    protected void onClearAutocomplete() {
        autocompleteInput.clear();
        suggestionsList.getItems().clear();
    }
}
