import com.cs4485.sentencebuilder.Tokenizer;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the Tokenizer utility class
 * @author Sarayu Gajula
 * 04/03/2026 - Initial creation
 */
public class TokenizerTest {

    /**
     * Tests that a simple sentence splits into the correct number of tokens
     */
    @Test
    public void testTokenize_SimpleSentence_ReturnsCorrectTokenCount() {
        List<String> tokens = Tokenizer.tokenize("The cat sat on the mat");
        assertEquals(6, tokens.size(), "Should split into 6 tokens");
    }

    /**
     * Tests that punctuation stays attached to the preceding word
     */
    @Test
    public void testTokenize_PunctuationAttached_StaysWithWord() {
        List<String> tokens = Tokenizer.tokenize("Hello! How are you?");
        assertEquals("Hello!", tokens.get(0), "Punctuation should stay attached to word");
        assertEquals("you?", tokens.get(3), "Punctuation should stay attached to word");
    }

    /**
     * Tests tokenizing a multi-word sentence and checks each token value
     */
    @Test
    public void testTokenize_MultiWordSentence_ReturnsCorrectTokens() {
        List<String> tokens = Tokenizer.tokenize("The quick brown fox");
        assertEquals(4, tokens.size(), "Should have 4 tokens");
        assertEquals("The", tokens.get(0));
        assertEquals("quick", tokens.get(1));
        assertEquals("brown", tokens.get(2));
        assertEquals("fox", tokens.get(3));
    }

    /**
     * Tests that extra whitespace between words is handled correctly
     */
    @Test
    public void testTokenize_ExtraWhitespace_IgnoresExtraSpaces() {
        List<String> tokens = Tokenizer.tokenize("Hello   world");
        assertEquals(2, tokens.size(), "Extra spaces should be ignored");
        assertEquals("Hello", tokens.get(0));
        assertEquals("world", tokens.get(1));
    }

    /**
     * Tests that an empty string returns an empty list
     */
    @Test
    public void testTokenize_EmptyString_ReturnsEmptyList() {
        List<String> tokens = Tokenizer.tokenize("");
        assertTrue(tokens.isEmpty(), "Empty string should return empty list");
    }

    /**
     * Tests a sentence with a comma stays attached
     */
    @Test
    public void testTokenize_CommaAttached_StaysWithWord() {
        List<String> tokens = Tokenizer.tokenize("Yes, I agree");
        assertEquals("Yes,", tokens.get(0), "Comma should stay attached to preceding word");
        assertEquals(3, tokens.size());
    }
}
