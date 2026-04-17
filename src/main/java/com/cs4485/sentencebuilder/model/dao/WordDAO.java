package com.cs4485.sentencebuilder.model.dao;

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
 * Data Access Object for the Word entity.
 * Handles all CRUD operations for the 'words' database table.
 * @author Daniel Dimitrov
 * 02/14/2026 - Initial creation
 * 03/31/2026 - Updated for newly added uppercase and title counts
 * 04/09/2026 - Added connectionProvider and constructors for testing
 * 04/10/2026 - Added get function to retrieve single word
 * 04/17/2026 - Changed insert function to update preexisting words by summing old counts and new counts
 */
public class WordDAO {

    // A function that supplies connections, can either supply the real DBConnection for prod, or a custom connection for testing
    private final Supplier<Connection> connectionProvider;

    /**
     * Production Constructor:
     * Defaults to the real DBConnection.
     */
    public WordDAO() {
        this.connectionProvider = DBConnection::getConnection;
    }

    /**
     * Testing Constructor:
     * Accepts a custom way to generate connections (like the H2 database used in JUnit tests).
     */
    public WordDAO(Supplier<Connection> connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    /**
     * Inserts a new Word or updates the counts if the word already exists.
     *
     * @param word The Word entity containing the data.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean insertOrUpdate(Word word) {
        String sql = "INSERT INTO words (word, total_count, start_count, end_count, uppercase_count, title_count) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "total_count = total_count + VALUES(total_count), " +
                "start_count = start_count + VALUES(start_count), " +
                "end_count = end_count + VALUES(end_count), " +
                "uppercase_count = uppercase_count + VALUES(uppercase_count), " +
                "title_count = title_count + VALUES(title_count)";

        try (Connection conn = connectionProvider.get();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, word.getWord());
            preparedStatement.setInt(2, word.getTotalCount());
            preparedStatement.setInt(3, word.getStartCount());
            preparedStatement.setInt(4, word.getEndCount());
            preparedStatement.setInt(5, word.getUppercaseCount());
            preparedStatement.setInt(6, word.getTitleCount());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error performing upsert for word: " + word.getWord());
            e.printStackTrace();
            return false;
        }
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
        try (Connection conn = connectionProvider.get();
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

    /**
     * Retrieves the word matching the parameter from the database.
     * @param word The word to retrieve
     * @return Word if found, null otherwise
     */
    public Word get(String word) {
        String sql = "SELECT total_count, start_count, end_count, uppercase_count, title_count FROM words where word = ?";
        Word retrievedWord = null;

        // try-catch automatically closes the PreparedStatement and handles any errors
        try (Connection conn = connectionProvider.get();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, word);

            // try-catch automatically closes the ResultSet and handles any errors
            try (ResultSet rs = preparedStatement.executeQuery()) {

                // Check if there's a result
                if (rs.next()) {
                    // Store parameters in temporary variables
                    int totalCount = rs.getInt("total_count");
                    int startCount = rs.getInt("start_count");
                    int endCount = rs.getInt("end_count");
                    int uppercaseCount = rs.getInt("uppercase_count");
                    int titleCount = rs.getInt("title_count");

                    retrievedWord = new Word(word, totalCount, startCount, endCount, uppercaseCount, titleCount);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving word from the database");
            e.printStackTrace();
        }

        return retrievedWord;
    }
}