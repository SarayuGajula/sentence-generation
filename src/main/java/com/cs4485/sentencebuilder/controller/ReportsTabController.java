package com.cs4485.sentencebuilder.controller;

import com.cs4485.sentencebuilder.model.dao.GeneratedSentenceDAO;
import com.cs4485.sentencebuilder.model.entity.GeneratedSentence;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.SimpleDateFormat;

/**
 * Controller for the reports tab
 * @author Connor Harris
 * @author Daniel Dimitrov
 * 4/2/2026  - Dummy version implemented that doesn't actually do anything yet - Connor Harris
 * 4/10/2026 - Extracted all fields and functions for reports tab from Connor's MainController - Daniel Dimitrov
 * 4/22/2026 - Wired up GeneratedSentenceDAO to display sentence history - Connor Harris
 */
public class ReportsTabController {

    // Table and columns injected from reports-tab-view.fxml
    @FXML private TableView<GeneratedSentence> sentencesTable;
    @FXML private TableColumn<GeneratedSentence, String> sentenceCol;
    @FXML private TableColumn<GeneratedSentence, String> sentenceAlgoCol;
    @FXML private TableColumn<GeneratedSentence, String> sentenceDateCol;

    // DAO for reading previously generated sentences from the database
    private final GeneratedSentenceDAO generatedSentenceDAO = new GeneratedSentenceDAO();

    // Format timestamps as readable date + time strings (e.g. "2026-05-07 14:32:00")
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Runs automatically when the Reports tab loads.
     * Wires each column to the corresponding field on GeneratedSentence,
     * then loads existing history from the database.
     */
    @FXML
    public void initialize() {
        // Bind the Sentence and Algorithm columns directly via property name
        sentenceCol.setCellValueFactory(new PropertyValueFactory<>("sentence"));
        sentenceAlgoCol.setCellValueFactory(new PropertyValueFactory<>("algorithm"));

        // Format the timestamp manually since PropertyValueFactory can't format Date objects
        sentenceDateCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getGeneratedAt() != null
                    ? dateFormat.format(data.getValue().getGeneratedAt())
                    : ""
            )
        );

        refreshTable();
    }

    /**
     * Called when the Refresh button is clicked.
     */
    @FXML
    protected void onRefresh() {
        refreshTable();
    }

    /**
     * Fetches all generated sentences from the database and repopulates the table.
     */
    private void refreshTable() {
        sentencesTable.setItems(FXCollections.observableArrayList(generatedSentenceDAO.getAll()));
    }
}
