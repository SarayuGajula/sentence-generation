import com.cs4485.sentencebuilder.Autocomplete;
import com.cs4485.sentencebuilder.Tokenizer;
import com.cs4485.sentencebuilder.WordAnalyzer;
import com.cs4485.sentencebuilder.model.dao.BigramDAO;
import com.cs4485.sentencebuilder.model.dao.WordDAO;
import com.cs4485.sentencebuilder.model.entity.Bigram;
import com.cs4485.sentencebuilder.model.entity.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Tests autocomplete
 * @author Jeffrey Gilbert
 * 04/23/2026 - Initial creation
 */

public class AutocompleteTest {

    private WordDAO wordDao;
    private BigramDAO bigramDao;

    // DB_CLOSE_DELAY=-1 keeps the DB alive during the tests.
    // INIT=RUNSCRIPT executes the schema.sql file that creates all the tables!
    private final String H2_URL = "jdbc:h2:mem:testdb;MODE=MySQL;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'classpath:schema.sql'";
    private final String H2_USER = "sa";
    private final String H2_PASSWORD = "";

    private Autocomplete ac;
    List<String> inputWords = List.of("This", "A", "John", "Because", "It's", "Jesus", "Yes");
    // test input is Crime and Punishment by Fyodor Dostoevsky from Project Gutenberg

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

        List<String> testFile = new ArrayList<>();

        // insert data
        try {
            testFile = Tokenizer.tokenize(Tokenizer.readFile("src/test/resources/CrimeAndPunishment.txt"));
        } catch (IOException e){
            e.printStackTrace();
        }

        Map<String, Word> wordMap = WordAnalyzer.getWords(testFile);
        for(Word word : wordMap.values()){
            wordDao.insertOrUpdate(word);
        }

        Map<String, Bigram> bigramMap = WordAnalyzer.getBigrams(testFile);
        for(Bigram bigram : bigramMap.values()){
            bigramDao.insertOrUpdate(bigram);
        }

        ac = new Autocomplete(wordDao, bigramDao);
    }

    @Test
    public void testSuggestThreeWords(){
        for(String input: inputWords){
            List<String> output = ac.suggestThreeWords(input);
            System.out.println("Input word: " + input);
            int i = 1;
            for (String outputWord: output){
                System.out.println("Suggestion " + i + ": " + outputWord);
            }
        }
    }
}
