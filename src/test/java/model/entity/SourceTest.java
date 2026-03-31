package model.entity;

import com.cs4485.sentencebuilder.model.entity.Source;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests the Source entity
 * @author Daniel Dimitrov
 * 03/31/2026 - Initial creation
 */
public class SourceTest {

    private Source source;

    /**
     * Sets up the Source object.
     */
    @BeforeEach
    public void setUp() {
        this.source = new Source("test-source.txt");
    }

    /**
     * Tests valid word count setting
     */
    @Test
    public void testSetWordCount_WithValidWordCount_SetsSuccessfully() {
        source.setWordCount(1337);

        assertEquals(1337, source.getWordCount(), "The word count should be set to 1337");
    }

    /**
     * Tests negative word count setting
     * Should throw exception
     */
    @Test
    public void testSetWordCount_WithNegativeWordCount_ThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> source.setWordCount(-3),
                "Should throw IllegalArgumentException for negative word count"
        );

        assertEquals("Source word count cannot be negative", exception.getMessage());
    }
}
