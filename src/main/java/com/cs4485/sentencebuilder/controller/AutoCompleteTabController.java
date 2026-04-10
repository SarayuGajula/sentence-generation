package com.cs4485.sentencebuilder.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

/**
 * Controller for the auto-complete tab
 * @author Connor Harris
 * @author Daniel Dimitrov
 * 4/2/2026 - Dummy version implemented that doesn't actually do anything yet - Connor Harris
 * 4/10/2026 - Extracted all fields and functions for auto-complete tab from Connor's MainController - Daniel Dimitrov
 */
public class AutoCompleteTabController {
    @FXML
    private TextArea autocompleteInput;
    @FXML private ListView<String> suggestionsList;

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
}
