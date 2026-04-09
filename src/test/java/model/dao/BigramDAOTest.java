package model.dao;

import com.cs4485.sentencebuilder.model.dao.BigramDAO;
import com.cs4485.sentencebuilder.model.entity.Bigram;
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
 * Tests the BigramDAO
 * @author Daniel Dimitrov
 * 04/09/2026 - Initial creation
 */
public class BigramDAOTest {

    private BigramDAO bigramDao;

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
        bigramDao = new BigramDAO(() -> {
            try {
                return DriverManager.getConnection(H2_URL, H2_USER, H2_PASSWORD);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to connect to test database", e);
            }
        });

        // Wipe the table clean before every test to guarantee perfect isolation
        try (Connection conn = DriverManager.getConnection(H2_URL, H2_USER, H2_PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM bigrams");
        }
    }

    /**
     * Tests adding a bigram to the DB
     */
    @Test
    public void testInsert_AddsBigramSuccessfully() {
        Bigram newBigram = new Bigram("hello", "world", 5);

        boolean result = bigramDao.insert(newBigram);

        assertTrue(result, "Insert should return true on success");

        // Verify it actually went into the database
        List<Bigram> allBigrams = bigramDao.getAll();
        assertEquals(1, allBigrams.size(), "There should be exactly 1 bigram in the database");
        assertEquals("hello", allBigrams.getFirst().getFirstWord());
        assertEquals("world", allBigrams.getFirst().getSecondWord());
        assertEquals(5, allBigrams.getFirst().getCount());
    }

    /**
     * Tests getting all bigrams from DB
     */
    @Test
    public void testGetAll_ReturnsAllInsertedBigrams() {
        bigramDao.insert(new Bigram("data", "science", 10));
        bigramDao.insert(new Bigram("machine", "learning", 20));

        List<Bigram> results = bigramDao.getAll();

        assertEquals(2, results.size(), "Should retrieve exactly 2 bigrams");
    }

    /**
     * Tests getting the top K most common bigrams with a specific starting word
     */
    @Test
    public void testGetTopKMostCommonBigrams_FiltersAndLimitsCorrectly() {
        bigramDao.insert(new Bigram("apple", "pie", 50));
        bigramDao.insert(new Bigram("apple", "tree", 100)); // Highest count
        bigramDao.insert(new Bigram("apple", "juice", 10));
        bigramDao.insert(new Bigram("banana", "split", 500)); // Different first word

        List<Bigram> results = bigramDao.getTopKMostCommonBigramsStartingWithWord(2, "apple");

        assertEquals(2, results.size(), "Should only return 2 results based on 'k'");

        // Ensure "banana" wasn't included
        for (Bigram b : results) {
            assertEquals("apple", b.getFirstWord(), "First word must be 'apple'");
        }
    }
}