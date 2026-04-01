package com.cs4485.sentencebuilder.model.dao;

import com.cs4485.sentencebuilder.model.entity.Word;
import com.cs4485.sentencebuilder.model.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for the Word entity.
 * Handles all CRUD operations for the 'words' database table.
 * @author Daniel Dimitrov
 * 02/14/2026 - Initial creation
 * 03/31/2026 - Updated for newly added uppercase and title counts
 */
public class WordDAO {

    /**
     * Inserts a new Word into the database.
     *
     * @param word The Word entity containing the data to insert.
     * @return true if the insertion was successful, false otherwise.
     */
    public boolean insert(Word word) {
        String sql = "INSERT INTO words (word, total_count, start_count, end_count, uppercase_count, title_count) VALUES (?, ?, ?, ?, ?, ?)";

        // try-catch automatically closes the PreparedStatement and handles any errors
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            // Get parameters from Word entity
            preparedStatement.setString(1, word.getWord());
            preparedStatement.setInt(2, word.getTotalCount());
            preparedStatement.setInt(3, word.getStartCount());
            preparedStatement.setInt(4, word.getEndCount());
            preparedStatement.setInt(5, word.getUppercaseCount());
            preparedStatement.setInt(6, word.getTitleCount());

            // Insert word
            int rowsAffected = preparedStatement.executeUpdate();
            // Word successfully inserted if there are rows affected
            if (rowsAffected > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error inserting word");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves all words from the database.
     *
     * @return A List of Word entities representing all records in the table.
     */
    public List<Word> getAll() {
        List<Word> wordList = new ArrayList<>();

        String sql = "SELECT word, total_count, start_count, end_count, uppercase_count, title_count FROM words";

        // try-catch automatically closes the PreparedStatement and handles any errors
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            // Loop through the ResultSet and map each row to a Word entity
            while (rs.next()) {
                // Store parameters in temporary variables
                String word = rs.getString("word");
                int totalCount = rs.getInt("total_count");
                int startCount = rs.getInt("start_count");
                int endCount = rs.getInt("end_count");
                int uppercaseCount = rs.getInt("uppercase_count");
                int titleCount = rs.getInt("title_count");

                Word w = new Word(word, totalCount, startCount, endCount, uppercaseCount, titleCount);

                wordList.add(w);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving all words from the database");
            e.printStackTrace();
        }

        return wordList;
    }
}