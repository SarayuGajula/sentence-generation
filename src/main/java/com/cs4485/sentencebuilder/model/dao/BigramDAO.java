package com.cs4485.sentencebuilder.model.dao;

import com.cs4485.sentencebuilder.model.entity.Bigram;
import com.cs4485.sentencebuilder.model.entity.Word;
import com.cs4485.sentencebuilder.model.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Data Access Object for the Bigram entity.
 * Handles all CRUD operations for the 'bigrams' database table.
 * @author Daniel Dimitrov
 * 02/14/2026 - Initial creation
 * 04/01/2026 - Added getTopKMostCommonBigramsStartingWithWord
 * 04/09/2026 - Added connectionProvider and constructors for testing
 * 04/09/2026 - Added comment for getTopKMostCommonBigramsStartingWithWord and fixed SQL statement
 * 04/17/2026 - Small fix in query for getTopKMostCommonBigramsStartingWithWord
 * 04/17/2026 - Changed insert function to update preexisting bigrams by summing old counts and new counts
 */
public class BigramDAO {

    // A function that supplies connections, can either supply the real DBConnection for prod, or a custom connection for testing
    private final Supplier<Connection> connectionProvider;

    /**
     * Production Constructor:
     * Defaults to the real DBConnection.
     */
    public BigramDAO() {
        this.connectionProvider = DBConnection::getConnection;
    }

    /**
     * Testing Constructor:
     * Accepts a custom way to generate connections (like the H2 database used in JUnit tests).
     */
    public BigramDAO(Supplier<Connection> connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    /**
     * Inserts a new Bigram or updates the count if it already exists.
     *
     * @param bigram The Bigram entity containing the data.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean insertOrUpdate(Bigram bigram) {
        String sql = "INSERT INTO bigrams (first_word, second_word, count) " +
                "VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "count = count + VALUES(count)";

        try (Connection conn = connectionProvider.get();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, bigram.getFirstWord());
            preparedStatement.setString(2, bigram.getSecondWord());
            preparedStatement.setInt(3, bigram.getCount());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error performing upsert for bigram: " +
                    bigram.getFirstWord() + " " + bigram.getSecondWord());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all bigrams from the database.
     *
     * @return A List of Bigram entities representing all records in the table.
     */
    public List<Bigram> getAll() {
        List<Bigram> bigramList = new ArrayList<>();

        String sql = "SELECT first_word, second_word, count FROM bigrams";

        // try-catch automatically closes the PreparedStatement and handles any errors
        try (Connection conn = connectionProvider.get();
             PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            // Loop through the ResultSet and map each row to a Bigram entity
            while (rs.next()) {
                // Store parameters in temporary variables
                String firstWord = rs.getString("first_word");
                String secondWord = rs.getString("second_word");
                int count = rs.getInt("count");

                Bigram bigram = new Bigram(firstWord, secondWord, count);

                bigramList.add(bigram);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving all bigrams from the database");
            e.printStackTrace();
        }

        return bigramList;
    }

    /**
     * Retrieves a list of the top K most frequently occurring bigrams that begin with a specific word.
     *
     * @param k    The maximum number of bigram records to return (e.g., 5 for the top 5 most common).
     * @param word The starting word (first word) to filter the bigrams by.
     * @return A List of the top K Bigram entities starting with the word parameter
     */
    public List<Bigram> getTopKMostCommonBigramsStartingWithWord(int k, String word) {
        List<Bigram> bigramList = new ArrayList<>();

        String sql = "SELECT first_word, second_word, count FROM Bigrams WHERE first_word = ? ORDER BY count DESC LIMIT ?;";

        // try-catch automatically closes the PreparedStatement and handles any errors
        try (Connection conn = connectionProvider.get();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, word);
            preparedStatement.setInt(2, k);

            // try-catch automatically closes the ResultSet and handles any errors
            try (ResultSet rs = preparedStatement.executeQuery()) {
                // Loop through the ResultSet and map each row to a Bigram entity
                while (rs.next()) {
                    // Store parameters in temporary variables
                    String secondWord = rs.getString("second_word");
                    int count = rs.getInt("count");

                    Bigram bigram = new Bigram(word, secondWord, count);

                    bigramList.add(bigram);
                }

            } catch (SQLException e) {
                System.err.println("Error retrieving top K most common bigrams from the database");
                e.printStackTrace();
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving all bigrams from the database");
            e.printStackTrace();
        }

        return bigramList;
    }
}