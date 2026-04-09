package model.dao;

import com.cs4485.sentencebuilder.model.dao.SourceDAO;
import com.cs4485.sentencebuilder.model.entity.Source;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the SourceDAO
 * @author Daniel Dimitrov
 * 04/09/2026 - Initial creation
 */
public class SourceDAOTest {

    private SourceDAO sourceDao;

    // DB_CLOSE_DELAY=-1 keeps the DB alive during the tests.
    // INIT=RUNSCRIPT executes the schema.sql file that creates all the tables!
    private final String H2_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'classpath:schema.sql'";
    private final String H2_USER = "sa";
    private final String H2_PASSWORD = "";

    /**
     * Sets up the H2 in-memory DB before each test
     * @throws SQLException if the test database cannot be connected to
     */
    @BeforeEach
    public void setup() throws SQLException {
        // Initialize the DAO with a Supplier that generates H2 connections
        sourceDao = new SourceDAO(() -> {
            try {
                return DriverManager.getConnection(H2_URL, H2_USER, H2_PASSWORD);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to connect to test database", e);
            }
        });

        // Wipe the table clean before every test to guarantee perfect isolation
        try (Connection conn = DriverManager.getConnection(H2_URL, H2_USER, H2_PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM sources");
        }
    }

    /**
     * Tests adding a source to the DB
     */
    @Test
    public void testInsert_AddsSourceSuccessfully() {
        Source newSource = new Source(0, "hamlet.txt", 30557);

        boolean isInserted = sourceDao.insert(newSource);

        assertTrue(isInserted, "Insert should return true on success");

        // Verify it actually went into the database
        List<Source> allSources = sourceDao.getAll();
        assertEquals(1, allSources.size(), "There should be exactly 1 source in the database");

        Source savedSource = allSources.getFirst();
        assertEquals("hamlet.txt", savedSource.getFilename());
        assertEquals(30557, savedSource.getWordCount());
        assertTrue(savedSource.getId() > 0, "The database should have auto-generated an ID");
    }

    /**
     * Tests getting all sources from DB
     */
    @Test
    public void testGetAll_ReturnsAllInsertedSources() {
        sourceDao.insert(new Source(0, "frankenstein.txt", 74900));
        sourceDao.insert(new Source(0, "dracula.txt", 160000));
        sourceDao.insert(new Source(0, "moby_dick.txt", 206000));

        List<Source> results = sourceDao.getAll();

        assertEquals(3, results.size(), "Should retrieve exactly 3 sources");

        boolean containsDracula = results.stream()
                .anyMatch(s -> s.getFilename().equals("dracula.txt"));
        assertTrue(containsDracula, "The list should contain dracula.txt");
    }
}