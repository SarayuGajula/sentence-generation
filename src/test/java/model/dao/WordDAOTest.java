package model.dao;

import com.cs4485.sentencebuilder.model.dao.WordDAO;
import com.cs4485.sentencebuilder.model.entity.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the WordDAO
 * @author Daniel Dimitrov
 * 04/09/2026 - Initial creation
 * 04/10/2026 - Added tests for single word retrieval
 * 04/17/2026 - Added update test for insertOrUpdate
 */
public class WordDAOTest {

    private WordDAO wordDao;

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
        wordDao = new WordDAO(() -> {
            try {
                return DriverManager.getConnection(H2_URL, H2_USER, H2_PASSWORD);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to connect to test database", e);
            }
        });

        // Wipe the table clean before every test to guarantee perfect isolation
        try (Connection conn = DriverManager.getConnection(H2_URL, H2_USER, H2_PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM words");
        }
    }

    /**
     * Tests adding a word to the DB
     */
    @Test
    public void testInsertOrUpdate_AddsWordSuccessfully() {
        // Create a word with specific counts to verify they all map correctly
        Word newWord = new Word("example", 100, 20, 15, 5, 10);

        boolean result = wordDao.insertOrUpdate(newWord);

        assertTrue(result, "InsertOrUpdate should return true on success");

        // Verify it actually went into the database with all fields matching
        List<Word> allWords = wordDao.getAll();
        assertEquals(1, allWords.size(), "There should be exactly 1 word in the database");
        
        Word savedWord = allWords.getFirst();
        assertEquals("example", savedWord.getWord());
        assertEquals(100, savedWord.getTotalCount());
        assertEquals(20, savedWord.getStartCount());
        assertEquals(15, savedWord.getEndCount());
        assertEquals(5, savedWord.getUppercaseCount());
        assertEquals(10, savedWord.getTitleCount());
    }

    /**
     * Tests getting all words from DB
     */
    @Test
    public void testGetAll_ReturnsAllInsertedWords() {
        wordDao.insertOrUpdate(new Word("the", 500, 100, 0, 10, 90));
        wordDao.insertOrUpdate(new Word("quick", 50, 5, 5, 0, 5));
        wordDao.insertOrUpdate(new Word("brown", 40, 2, 8, 0, 2));

        List<Word> results = wordDao.getAll();

        assertEquals(3, results.size(), "Should retrieve exactly 3 words");
        
        boolean containsQuick = results.stream()
                .anyMatch(w -> w.getWord().equals("quick"));
        assertTrue(containsQuick, "The list should contain the word 'quick'");
    }

    /**
     * Tests getting a single existing word from DB
     */
    @Test
    public void testGet_ReturnsInsertedWord() {
        wordDao.insertOrUpdate(new Word("the", 500, 100, 0, 10, 90));
        Word expectedResult = new Word("quick", 50, 5, 5, 0, 5);
        wordDao.insertOrUpdate(expectedResult);

        Word result = wordDao.get("quick");

        assertEquals(result, expectedResult, "Retrieved word should match inserted word exactly");
    }

    /**
     * Tests getting a single non-existent word from DB
     */
    @Test
    public void testGet_ReturnsNullIfNotFound() {
        wordDao.insertOrUpdate(new Word("the", 500, 100, 0, 10, 90));
        wordDao.insertOrUpdate(new Word("quick", 50, 5, 5, 0, 5));

        Word result = wordDao.get("brown");

        assertNull(result, "No word should be retrieved");
    }

    /**
     * Tests that inserting an existing word updates its counts
     */
    @Test
    public void testInsertOrUpdate_UpdatesExistingWordSuccessfully() {
        Word initialWord = new Word("example", 100, 20, 15, 5, 10);
        wordDao.insertOrUpdate(initialWord);

        Word updateWord = new Word("example", 50, 10, 5, 2, 5);
        boolean result = wordDao.insertOrUpdate(updateWord);

        assertTrue(result, "InsertOrUpdate should return true on successful update");

        List<Word> allWords = wordDao.getAll();
        assertEquals(1, allWords.size(), "There should still be exactly 1 word in the database");

        Word savedWord = allWords.getFirst();
        assertEquals("example", savedWord.getWord());
        assertEquals(150, savedWord.getTotalCount(), "Total count should be 100 + 50");
        assertEquals(30, savedWord.getStartCount(), "Start count should be 20 + 10");
        assertEquals(20, savedWord.getEndCount(), "End count should be 15 + 5");
        assertEquals(7, savedWord.getUppercaseCount(), "Uppercase count should be 5 + 2");
        assertEquals(15, savedWord.getTitleCount(), "Title count should be 10 + 5");
    }
}