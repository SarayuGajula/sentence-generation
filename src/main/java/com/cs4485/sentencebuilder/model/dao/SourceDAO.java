package com.cs4485.sentencebuilder.model.dao;

import com.cs4485.sentencebuilder.model.entity.Source;
import com.cs4485.sentencebuilder.model.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for the Source entity.
 * Handles all CRUD operations for the 'sources' database table.
 * @author Daniel Dimitrov
 * 02/11/2026 - Initial creation
 * 02/14/2026 - Minor fixes
 */
public class SourceDAO {

    /**
     * Inserts a new Source into the database.
     *
     * @param source The Source entity containing the data to insert.
     * @return true if the insertion was successful, false otherwise.
     */
    public boolean insert(Source source) {
        String sql = "INSERT INTO sources (filename, word_count) VALUES (?, ?)";

        // try-catch automatically closes the PreparedStatement and handles any errors
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            // Get parameters from Source entity
            preparedStatement.setString(1, source.getFilename());
            preparedStatement.setInt(2, source.getWordCount());

            // Insert source
            int rowsAffected = preparedStatement.executeUpdate();
            // Source successfully inserted if there are rows affected
            if (rowsAffected > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error inserting source");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves all sources from the database.
     *
     * @return A List of Source entities representing all records in the table.
     */
    public List<Source> getAll() {
        List<Source> sourcesList = new ArrayList<>();

        String sql = "SELECT id, filename, word_count FROM sources";

        // try-catch automatically closes the PreparedStatement and handles any errors
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            // Loop through the ResultSet and map each row to a Source entity
            while (rs.next()) {
                // Store parameters in temporary variables
                int id = rs.getInt("id");
                String filename = rs.getString("filename");
                int wordCount = rs.getInt("word_count");

                Source source = new Source(id, filename, wordCount);

                sourcesList.add(source);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving all sources from the database");
            e.printStackTrace();
        }

        return sourcesList;
    }
}