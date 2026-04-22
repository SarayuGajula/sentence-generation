package model.dao;

import com.cs4485.sentencebuilder.model.dao.GeneratedSentenceDAO;
import com.cs4485.sentencebuilder.model.entity.GeneratedSentence;
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
 * Tests the GeneratedSentenceDAO
 * @author Daniel Dimitrov
 * 04/21/2026 - Initial creation
 */
public class GeneratedSentenceDAOTest {

    private GeneratedSentenceDAO generatedSentenceDao;

    // DB_CLOSE_DELAY=-1 keeps the DB alive during the tests.
    // INIT=RUNSCRIPT executes the schema.sql file that creates all the tables!
    private final String H2_URL = "jdbc:h2:mem:testdb;MODE=MySQL;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'classpath:schema.sql'";
    private final String H2_USER = "sa";
    private final String H2_PASSWORD = "";

    /**
     * Sets up the H2 in-memory DB before each test
     * @throws SQLException if the test database cannot be connected to
     */
    @BeforeEach
    public void setup() throws SQLException {
        // Initialize the DAO with a Supplier that generates H2 connections
        generatedSentenceDao = new GeneratedSentenceDAO(() -> {
            try {
                return DriverManager.getConnection(H2_URL, H2_USER, H2_PASSWORD);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to connect to test database", e);
            }
        });

        // Wipe the table clean before every test to guarantee perfect isolation
        try (Connection conn = DriverManager.getConnection(H2_URL, H2_USER, H2_PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM generated_sentences");
        }
    }

    /**
     * Tests adding a generated sentence to the DB
     */
    @Test
    public void testInsert_AddsGeneratedSentenceSuccessfully() {
        GeneratedSentence newGeneratedSentence = new GeneratedSentence("test sentence", "test algorithm");

        boolean isInserted = generatedSentenceDao.insert(newGeneratedSentence);

        assertTrue(isInserted, "Insert should return true on success");

        // Verify it actually went into the database
        List<GeneratedSentence> allGeneratedSentences = generatedSentenceDao.getAll();
        assertEquals(1, allGeneratedSentences.size(), "There should be exactly 1 generated sentence in the database");

        GeneratedSentence savedGeneratedSentence = allGeneratedSentences.getFirst();
        assertEquals("test sentence", savedGeneratedSentence.getSentence());
        assertEquals("test algorithm", savedGeneratedSentence.getAlgorithm());
        assertTrue(savedGeneratedSentence.getId() > 0, "The database should have auto-generated an ID");
    }

    /**
     * Tests getting all generated sentences from DB
     */
    @Test
    public void testGetAll_ReturnsAllInsertedGeneratedSentences() {
        generatedSentenceDao.insert(new GeneratedSentence("first test", "first algo"));
        generatedSentenceDao.insert(new GeneratedSentence("second test", "second algo"));
        generatedSentenceDao.insert(new GeneratedSentence("third test", "first algo"));

        List<GeneratedSentence> results = generatedSentenceDao.getAll();

        assertEquals(3, results.size(), "Should retrieve exactly 3 generated sentences");

        boolean containsDracula = results.stream()
                .anyMatch(s -> s.getSentence().equals("second test"));
        assertTrue(containsDracula, "The list should contain second test");
    }
}