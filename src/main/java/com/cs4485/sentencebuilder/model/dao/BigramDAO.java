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

/**
 * Data Access Object for the Bigram entity.
 * Handles all CRUD operations for the 'bigrams' database table.
 * @author Daniel Dimitrov
 * 02/14/2026 - Initial creation
 * 04/01/2026 - Added getTopKMostCommonBigramsStartingWithWord
 */
public class BigramDAO {

    /**
     * Inserts a new Bigram into the database.
     *
     * @param bigram The Bigram entity containing the data to insert.
     * @return true if the insertion was successful, false otherwise.
     */
    public boolean insert(Bigram bigram) {
        String sql = "INSERT INTO bigrams (first_word, second_word, count) VALUES (?, ?, ?)";

        // try-catch automatically closes the PreparedStatement and handles any errors
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            // Get parameters from Bigram entity
            preparedStatement.setString(1, bigram.getFirstWord());
            preparedStatement.setString(2, bigram.getSecondWord());
            preparedStatement.setInt(3, bigram.getCount());

            // Insert bigram
            int rowsAffected = preparedStatement.executeUpdate();
            // bigram successfully inserted if there are rows affected
            if (rowsAffected > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error inserting bigram");
            e.printStackTrace();
        }
        return false;
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
        try (Connection conn = DBConnection.getConnection();
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

    public List<Bigram> getTopKMostCommonBigramsStartingWithWord(int k, String word) {
        List<Bigram> bigramList = new ArrayList<>();

        String sql = "SELECT TOP ? first_word, second_word, count FROM Bigrams WHERE first_word = ?;";

        // try-catch automatically closes the PreparedStatement and handles any errors
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, k);
            preparedStatement.setString(2, word);

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