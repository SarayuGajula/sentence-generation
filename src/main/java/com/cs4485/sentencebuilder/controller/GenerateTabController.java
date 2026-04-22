package com.cs4485.sentencebuilder.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import com.cs4485.sentencebuilder.Generator;
import com.cs4485.sentencebuilder.model.entity.GeneratedSentence;
import com.cs4485.sentencebuilder.model.dao.GeneratedSentenceDAO;

/**
 * Controller for the generate tab
 * @author Connor Harris
 * @author Daniel Dimitrov
 * @author Joe Su
 * 4/2/2026 - Dummy version implemented that doesn't actually do anything yet - Connor Harris
 * 4/10/2026 - Extracted all fields and functions for generate tab from Connor's MainController - Daniel Dimitrov
 * 4/17/2026 - Added sentence generation logic - Joe Su
 * 4/22/2026 - Added uploading generated sentences to Reports database - Joe Su
 */
public class GenerateTabController {
    @FXML private TextField startWordField;
    @FXML private ComboBox<String> algorithmCombo;
    @FXML private TextArea generatedOutput;
    @FXML private Button generateButton;

    private Generator generator;
    private GeneratedSentenceDAO generatedSentenceDAO;

    @FXML
    protected void onGenerate() {
        String startWord = startWordField.getText().trim();

        if(startWord.isBlank()) {
            generatedOutput.setText("Please enter a starting word.");
            return;
        }

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

        // upload generated sentence to reports database
        GeneratedSentence generatedSentence = new GeneratedSentence(sentence, selected);
        generatedSentenceDAO.insert(generatedSentence);
    }

    @FXML
    public void initialize() {
        algorithmCombo.getItems().addAll("Most Frequent", "Random Weighted");
        algorithmCombo.setValue("Most Frequent");

        generator = new Generator();
        generatedSentenceDAO = new GeneratedSentenceDAO();

        generateButton.disableProperty().bind(startWordField.textProperty().map(text -> text.trim().isEmpty()));
    }
}
