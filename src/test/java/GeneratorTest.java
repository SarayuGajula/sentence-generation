import com.cs4485.sentencebuilder.Generator;
import com.cs4485.sentencebuilder.model.dao.BigramDAO;
import com.cs4485.sentencebuilder.model.dao.WordDAO;
import com.cs4485.sentencebuilder.model.entity.Bigram;
import com.cs4485.sentencebuilder.model.entity.Word;
import com.cs4485.sentencebuilder.Tokenizer;
import com.cs4485.sentencebuilder.WordAnalyzer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests generation algorithms
 * @author Joe Su
 * 04/16/2026 - Initial creation
 */

public class GeneratorTest {

    private WordDAO wordDao;
    private BigramDAO bigramDao;

    // DB_CLOSE_DELAY=-1 keeps the DB alive during the tests.
    // INIT=RUNSCRIPT executes the schema.sql file that creates all the tables!
    private final String H2_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'classpath:schema.sql'";
    private final String H2_USER = "sa";
    private final String H2_PASSWORD = "";

    private Generator gen;
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
            wordDao.insert(word);
        }

        Map<String, Bigram> bigramMap = WordAnalyzer.getBigrams(testFile);
        for(Bigram bigram : bigramMap.values()){
            bigramDao.insert(bigram);
        }

        gen = new Generator(wordDao, bigramDao);
    }

    @Test
    public void testCommonGenerator(){
        double avgLen = 0.0;
        for(String input: inputWords){
            String output = gen.commonGenerator(input);
            int len = output.split("\\s+").length;
            avgLen += len;
            System.out.println("Input word: " + input);
            System.out.println(output);
            System.out.println("Sentence length: " + len);
        }
        avgLen/=inputWords.size();
        System.out.println("Average sentence length: " + avgLen);
    }

    @Test
    public void testTopFiveGenerator(){
        double avgLen = 0.0;
        for(String input: inputWords){
            String output = gen.topFiveWordsGenerator(input);
            int len = output.split("\\s+").length;
            avgLen += len;
            System.out.println("Input word: " + input);
            System.out.println(output);
            System.out.println("Sentence length: " + len);
        }
        avgLen/=inputWords.size();
        System.out.println("Average sentence length: " + avgLen);
    }
}
