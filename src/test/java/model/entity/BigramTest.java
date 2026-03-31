package model.entity;

import com.cs4485.sentencebuilder.model.entity.Bigram;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests the Bigram entity
 * @author Daniel Dimitrov
 * 03/31/2026 - Initial creation
 */
public class BigramTest {

    private Bigram bigram;

    /**
     * Sets up the Bigram object.
     */
    @BeforeEach
    public void setUp() {
        this.bigram = new Bigram("first-test-word", "second-test-word");
    }

    /**
     * Tests valid count setting
     */
    @Test
    public void testSetCount_WithValidCount_SetsSuccessfully() {
        bigram.setCount(1337);

        assertEquals(1337, bigram.getCount(), "The count should be set to 1337");
    }

    /**
     * Tests negative count setting
     * Should throw exception
     */
    @Test
    public void testSetCount_WithNegativeCount_ThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bigram.setCount(-3),
                "Should throw IllegalArgumentException for negative count"
        );

        assertEquals("Bigram count cannot be negative", exception.getMessage());
    }
}
