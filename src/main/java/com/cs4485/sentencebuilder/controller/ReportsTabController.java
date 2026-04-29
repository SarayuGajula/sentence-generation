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

    @FXML private TableView<GeneratedSentence> sentencesTable;
    @FXML private TableColumn<GeneratedSentence, String> sentenceCol;
    @FXML private TableColumn<GeneratedSentence, String> sentenceAlgoCol;
    @FXML private TableColumn<GeneratedSentence, String> sentenceDateCol;

    private final GeneratedSentenceDAO generatedSentenceDAO = new GeneratedSentenceDAO();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @FXML
    public void initialize() {
        sentenceCol.setCellValueFactory(new PropertyValueFactory<>("sentence"));
        sentenceAlgoCol.setCellValueFactory(new PropertyValueFactory<>("algorithm"));
        sentenceDateCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getGeneratedAt() != null
                    ? dateFormat.format(data.getValue().getGeneratedAt())
                    : ""
            )
        );

        refreshTable();
    }

    @FXML
    protected void onRefresh() {
        refreshTable();
    }

    private void refreshTable() {
        sentencesTable.setItems(FXCollections.observableArrayList(generatedSentenceDAO.getAll()));
    }
}
