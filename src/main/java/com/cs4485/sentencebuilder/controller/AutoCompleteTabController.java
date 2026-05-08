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
import com.cs4485.sentencebuilder.WordAnalyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.cs4485.sentencebuilder.model.entity.Word;

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

// Written by Jeffrey Gilbert
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
    private void initialize() { // Jeffrey Gilbert
        ac  = new Autocomplete();
        wordDAO = new WordDAO(); // just for insertion because it's a pain

        // Add listener to call function whenever text updates
        autocompleteInput.textProperty().addListener(
                (observable, oldValue, newValue) -> {onTextUpdate(newValue);});

        // Stop user from pressing use suggestion if suggestion box is empty
        useSuggestionButton.disableProperty().bind(Bindings.isEmpty(suggestionsList.getItems()));

        // Stop user from pressing clear button if both text boxes are empty
        clearAutocompleteButton.disableProperty().bind(autocompleteInput.textProperty().isEmpty()
                .and(Bindings.isEmpty(suggestionsList.getItems()))
        );
    }

    @FXML
    protected void onTextUpdate(String text) { // Jeffrey Gilbert
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

        if  (!lastWord.isEmpty()) {
            // Insert or update was the easiest way I could think of to make sure new words are added
            // Have to do some janky stuff to get a proper Word object because WordAnalyzer is bad
            List<String> lastWordList =  new ArrayList<>();
            lastWordList.add(lastWord);
            Map<String, Word> analyzerOutput = WordAnalyzer.getWords(lastWordList);
            Word lastWordObject = analyzerOutput.values().iterator().next();
            lastWordObject.setStartCount(0); // Little bit jank but there's no good way to get startCount without taking in the
            //whole text box at a time, which is not feasible
            wordDAO.insertOrUpdate(lastWordObject); // Only value in the Map is the word we want

            // Generate suggestions
            generateSuggestions(lastWord);
        }
    }

    @FXML
    private String getLastWord(String text) { // Jeffrey Gilbert
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
    private Boolean isCutoff(String text) { // Jeffrey Gilbert
        // Check if last character is a space or punctuation
        char c = text.charAt(text.length() - 1);
        return Character.isWhitespace(c) || c == '.' || c == ',' || c == '!' || c == '?';
    }

    @FXML
    protected void generateSuggestions(String lastWord) { // Jeffrey Gilbert
        // Get top 3 suggestions
        List<String> suggestions = ac.suggestThreeWords(lastWord);

        // Cast to ObservableList because javaFX is weird
        ObservableList<String> observableSuggestions =
                FXCollections.observableArrayList(suggestions);

        // Set items
        suggestionsList.setItems(observableSuggestions);
    }

    @FXML
    protected void onUseSuggestion() { // Jeffrey Gilbert
        // Get item
        String selected = suggestionsList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        // Check last character for spacing
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

        // Append to text box
        autocompleteInput.appendText(selected);
        // Generate new suggestions
        generateSuggestions(selected);
    }

    @FXML
    protected void onClearAutocomplete() { // Jeffrey Gilbert
        // Self explanatory
        autocompleteInput.clear();
        suggestionsList.getItems().clear();
    }
}
