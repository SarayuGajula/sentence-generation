package model.entity;

import com.cs4485.sentencebuilder.model.entity.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests the Word entity
 * @author Daniel Dimitrov
 * 03/31/2026 - Initial creation
 */
public class WordTest {

    private Word word;

    /**
     * Sets up the Word object
     */
    @BeforeEach
    public void setUp() {
        this.word = new Word("test-word");
    }

    /**
     * Tests valid total count setting
     */
    @Test
    public void testSetTotalCount_WithValidTotalCount_SetsSuccessfully() {
        word.setTotalCount(1337);

        assertEquals(1337, word.getTotalCount(), "The total count should be set to 1337");
    }

    /**
     * Tests negative total count setting
     * Should throw exception
     */
    @Test
    public void testSetTotalCount_WithNegativeTotalCount_ThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> word.setTotalCount(-3),
                "Should throw IllegalArgumentException for negative count"
        );

        assertEquals("Word total count cannot be negative", exception.getMessage());
    }

    /**
     * Tests valid start count setting
     */
    @Test
    public void testSetStartCount_WithValidStartCount_SetsSuccessfully() {
        word.setStartCount(1337);

        assertEquals(1337, word.getStartCount(), "The start count should be set to 1337");
    }

    /**
     * Tests negative start count setting
     * Should throw exception
     */
    @Test
    public void testSetStartCount_WithNegativeStartCount_ThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> word.setStartCount(-3),
                "Should throw IllegalArgumentException for negative count"
        );

        assertEquals("Word start count cannot be negative", exception.getMessage());
    }

    /**
     * Tests valid end count setting
     */
    @Test
    public void testSetEndCount_WithValidEndCount_SetsSuccessfully() {
        word.setEndCount(1337);

        assertEquals(1337, word.getEndCount(), "The end count should be set to 1337");
    }

    /**
     * Tests negative end count setting
     * Should throw exception
     */
    @Test
    public void testSetEndCount_WithNegativeEndCount_ThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> word.setEndCount(-3),
                "Should throw IllegalArgumentException for negative count"
        );

        assertEquals("Word end count cannot be negative", exception.getMessage());
    }

    /**
     * Tests valid uppercase count setting
     */
    @Test
    public void testSetUppercaseCount_WithValidUppercaseCount_SetsSuccessfully() {
        word.setUppercaseCount(1337);

        assertEquals(1337, word.getUppercaseCount(), "The uppercase count should be set to 1337");
    }

    /**
     * Tests negative uppercase count setting
     * Should throw exception
     */
    @Test
    public void testSetUppercaseCount_WithNegativeUppercaseCount_ThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> word.setUppercaseCount(-3),
                "Should throw IllegalArgumentException for negative count"
        );

        assertEquals("Word uppercase count cannot be negative", exception.getMessage());
    }

    /**
     * Tests valid title count setting
     */
    @Test
    public void testSetTitleCount_WithValidTitleCount_SetsSuccessfully() {
        word.setTitleCount(1337);

        assertEquals(1337, word.getTitleCount(), "The title count should be set to 1337");
    }

    /**
     * Tests negative title count setting
     * Should throw exception
     */
    @Test
    public void testSetTitleCount_WithNegativeTitleCount_ThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> word.setTitleCount(-3),
                "Should throw IllegalArgumentException for negative count"
        );

        assertEquals("Word title count cannot be negative", exception.getMessage());
    }
}
