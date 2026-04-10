package com.cs4485.sentencebuilder.model.entity;

import java.util.Objects;

/**
 * Represents a Word entity in the application and maps directly to the 'words' table in the database.
 * @author Daniel Dimitrov
 * 03/14/2026 - Initial creation
 * 03/31/2026 - Added uppercase and title counts, as well as negative checks and exceptions for all count settings
 * 04/10/2026 - Added equals function
 */
public class Word {

    private String word;
    private int totalCount;
    private int startCount; // Count at the beginning of a sentence
    private int endCount; // Count at the end of a sentence
    private int uppercaseCount; // Fully UPPERCASED count
    private int titleCount; // Title capitalization count (just the first letter is uppercased)

    /**
     * Constructor for creating a new Word object BEFORE it goes into the database
     */
    public Word(String word) {
        this.word = word;
        // Before the source is analyzed the word counts are set to 0
        totalCount = 0;
        startCount = 0;
        endCount = 0;
        uppercaseCount = 0;
        titleCount = 0;
    }

    /**
     * Constructor for reconstructing a Word retrieved FROM the database.
     */
    public Word(String word, int totalCount, int startCount, int endCount, int uppercaseCount, int titleCount) {
        this.word = word;
        this.totalCount = totalCount;
        this.startCount = startCount;
        this.endCount = endCount;
        this.uppercaseCount = uppercaseCount;
        this.titleCount = titleCount;
    }

    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }

    public int getTotalCount() { return totalCount; }
    public void setTotalCount(int totalCount) {
        if (totalCount < 0) {
            throw new IllegalArgumentException("Word total count cannot be negative");
        }
        this.totalCount = totalCount;
    }

    public int getStartCount() { return startCount; }
    public void setStartCount(int startCount) {
        if (startCount < 0) {
            throw new IllegalArgumentException("Word start count cannot be negative");
        }
        this.startCount = startCount;
    }

    public int getEndCount() { return endCount; }
    public void setEndCount(int endCount) {
        if (endCount < 0) {
            throw new IllegalArgumentException("Word end count cannot be negative");
        }
        this.endCount = endCount;
    }

    public int getUppercaseCount() { return uppercaseCount; }
    public void setUppercaseCount(int uppercaseCount) {
        if (uppercaseCount < 0) {
            throw new IllegalArgumentException("Word uppercase count cannot be negative");
        }
        this.uppercaseCount = uppercaseCount;
    }

    public int getTitleCount() { return titleCount; }
    public void setTitleCount(int titleCount) {
        if (titleCount < 0) {
            throw new IllegalArgumentException("Word title count cannot be negative");
        }
        this.titleCount = titleCount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Word word1 = (Word) o;
        return totalCount == word1.totalCount && startCount == word1.startCount && endCount == word1.endCount && uppercaseCount == word1.uppercaseCount && titleCount == word1.titleCount && Objects.equals(word, word1.word);
    }
}
