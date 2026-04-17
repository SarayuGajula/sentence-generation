package com.cs4485.sentencebuilder.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import com.cs4485.sentencebuilder.Generator;

/**
 * Controller for the generate tab
 * @author Connor Harris
 * @author Daniel Dimitrov
 * @author Joe Su
 * 4/2/2026 - Dummy version implemented that doesn't actually do anything yet - Connor Harris
 * 4/10/2026 - Extracted all fields and functions for generate tab from Connor's MainController - Daniel Dimitrov
 * 4/17/2026 - Added sentence generation logic - Joe Su
 */
public class GenerateTabController {
    @FXML private TextField startWordField;
    @FXML private ComboBox<String> algorithmCombo;
    @FXML private TextArea generatedOutput;

    private Generator generator;

    @FXML
    protected void onGenerate() {
        String startWord = startWordField.getText();
        if (startWord == null || startWord.isBlank()) {
            generatedOutput.setText("Please enter a starting word.");
            return;
        }
        // TODO: call sentence generation logic here
        String selected = algorithmCombo.getSelectionModel().getSelectedItem();
        String sentence;
        if("Most Frequent".equals(selected)) {
            sentence = generator.commonGenerator(startWord);
        }
        else if("Random Weighted".equals(selected)){
            sentence = generator.topFiveWordsGenerator(startWord);
        }
        else{
            sentence = "Please pick a selection model.";
        }
        generatedOutput.setText(sentence);
    }

    @FXML
    public void initialize() {
        algorithmCombo.getItems().addAll("Most Frequent", "Random Weighted");
        algorithmCombo.setValue("Most Frequent");

        generator = new Generator();

//        wordSortCombo.getItems().addAll(
//                "Alphabetical",
//                "Frequency (High to Low)",
//                "Frequency (Low to High)",
//                "Sentence Start Count",
//                "Sentence End Count"
//        );
//        wordSortCombo.setValue("Alphabetical");
    }
}
