package com.cs4485.sentencebuilder.model.dao;

import com.cs4485.sentencebuilder.model.entity.GeneratedSentence;
import com.cs4485.sentencebuilder.model.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

/**
 * Data Access Object for the Generated Sentence entity.
 * Handles all CRUD operations for the 'generated_sentences' database table.
 * @author Daniel Dimitrov
 * 04/21/2026 - Initial creation
 */
public class GeneratedSentenceDAO {

    // A function that supplies connections, can either supply the real DBConnection for prod, or a custom connection for testing
    private final Supplier<Connection> connectionProvider;

    /**
     * Production Constructor:
     * Defaults to the real DBConnection.
     */
    public GeneratedSentenceDAO() {
        this.connectionProvider = DBConnection::getConnection;
    }

    /**
     * Testing Constructor:
     * Accepts a custom way to generate connections (like the H2 database used in JUnit tests).
     */
    public GeneratedSentenceDAO(Supplier<Connection> connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    /**
     * Inserts a new Generated Sentence into the database.
     *
     * @param generatedSentence The Generated Sentence entity containing the data to insert.
     * @return true if the insertion was successful, false otherwise.
     */
    public boolean insert(GeneratedSentence generatedSentence) {
        String sql = "INSERT INTO generated_sentences (sentence, algorithm) VALUES (?, ?)";

        // try-catch automatically closes the PreparedStatement and handles any errors
        try (Connection conn = connectionProvider.get();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            // Get parameters from Generated Sentence entity
            preparedStatement.setString(1, generatedSentence.getSentence());
            preparedStatement.setString(2, generatedSentence.getAlgorithm());

            // Insert generated sentence
            int rowsAffected = preparedStatement.executeUpdate();
            // Generated Sentence successfully inserted if there are rows affected
            if (rowsAffected > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error inserting generated sentence");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves all generated sentences from the database.
     *
     * @return A List of Generated Sentence entities representing all records in the table.
     */
    public List<GeneratedSentence> getAll() {
        List<GeneratedSentence> generatedSentencesList = new ArrayList<>();

        String sql = "SELECT id, sentence, algorithm, generated_at FROM generated_sentences";

        // try-catch automatically closes the PreparedStatement and handles any errors
        try (Connection conn = connectionProvider.get();
             PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            // Loop through the ResultSet and map each row to a Generated Sentence entity
            while (rs.next()) {
                // Store parameters in temporary variables
                int id = rs.getInt("id");
                String sentence = rs.getString("sentence");
                String algorithm = rs.getString("algorithm");
                Date generatedAt = rs.getTimestamp("generated_at");

                GeneratedSentence generatedSentence = new GeneratedSentence(id, sentence, algorithm, generatedAt);

                generatedSentencesList.add(generatedSentence);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving all generated sentences from the database");
            e.printStackTrace();
        }

        return generatedSentencesList;
    }
}