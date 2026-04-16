package com.cs4485.sentencebuilder.controller;

import com.cs4485.sentencebuilder.model.dao.WordDAO;
import com.cs4485.sentencebuilder.model.entity.Word;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

/**
 * Controller for the words tab
 * @author Connor Harris
 * @author Daniel Dimitrov
 * 4/2/2026 - Dummy version implemented that doesn't actually do anything yet - Connor Harris
 * 4/10/2026 - Extracted all fields and functions for words tab from Connor's MainController - Daniel Dimitrov
 * 4/16/2026 - Bound table columns to Word entity, removed sorting, implemented fetching from DB and loading data into table
 */
public class WordsTabController {
    @FXML private TableView<Word> wordsTable;
    @FXML private TableColumn<Word, String> wordCol;
    @FXML private TableColumn<Word, Integer> totalCountCol;
    @FXML private TableColumn<Word, Integer> startCountCol;
    @FXML private TableColumn<Word, Integer> endCountCol;
    @FXML private TableColumn<Word, Integer> uppercaseCountCol;
    @FXML private TableColumn<Word, Integer> titleCountCol;

    // Instantiate DAO to access the database
    private final WordDAO wordDAO = new WordDAO();

    /**
     * Tab initialization
     */
    @FXML
    public void initialize() {
        // Bind TableColumns to Word entity properties
        wordCol.setCellValueFactory(new PropertyValueFactory<>("word"));
        totalCountCol.setCellValueFactory(new PropertyValueFactory<>("totalCount"));
        startCountCol.setCellValueFactory(new PropertyValueFactory<>("startCount"));
        endCountCol.setCellValueFactory(new PropertyValueFactory<>("endCount"));
        uppercaseCountCol.setCellValueFactory(new PropertyValueFactory<>("uppercaseCount"));
        titleCountCol.setCellValueFactory(new PropertyValueFactory<>("titleCount"));

        // Load the initial data when the tab is initialized
        onRefreshWords();
    }

    /**
     * Triggered in initialization and on refresh button press
     * Fetches all words and adds them to table
     */
    @FXML
    protected void onRefreshWords() {
        // Fetch fresh list from database
        List<Word> words = wordDAO.getAll();

        // Convert standard List to JavaFX ObservableList
        ObservableList<Word> observableWords = FXCollections.observableArrayList(words);

        // Inject data into the table
        wordsTable.setItems(observableWords);
    }
}