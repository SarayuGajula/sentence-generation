package com.cs4485.sentencebuilder.model.entity;

/**
 * Represents a Word entity in the application and maps directly to the 'words' table in the database.
 * @author Daniel Dimitrov
 * 03/14/2026 - Initial creation
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
    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }

    public int getStartCount() { return startCount; }
    public void setStartCount(int startCount) { this.startCount = startCount; }

    public int getEndCount() { return endCount; }
    public void setEndCount(int endCount) { this.endCount = endCount; }

    public int getUppercaseCount() { return uppercaseCount; }
    public void setUppercaseCount(int uppercaseCount) { this.uppercaseCount = uppercaseCount; }

    public int getTitleCount() { return titleCount; }
    public void setTitleCount(int titleCount) { this.titleCount = titleCount; }
}
