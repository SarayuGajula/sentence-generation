package com.cs4485.sentencebuilder.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Controller for the reports tab
 * @author Connor Harris
 * @author Daniel Dimitrov
 * 4/2/2026 - Dummy version implemented that doesn't actually do anything yet - Connor Harris
 * 4/10/2026 - Extracted all fields and functions for reports tab from Connor's MainController - Daniel Dimitrov
 */
public class ReportsTabController {
    @FXML private TableView<?> sentencesTable;
    @FXML private TableColumn<?, ?> sentenceCol;
    @FXML private TableColumn<?, ?> sentenceAlgoCol;
    @FXML private TableColumn<?, ?> sentenceDateCol;
}
