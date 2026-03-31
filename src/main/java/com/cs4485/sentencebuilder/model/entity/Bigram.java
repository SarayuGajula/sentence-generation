package com.cs4485.sentencebuilder.model.entity;

/**
 * Represents a Bigram entity in the application and maps directly to the 'bigrams' table in the database.
 * @author Daniel Dimitrov
 * 03/14/2026 - Initial creation
 * 03/31/2026 - Added negative check and exception for count setting
 */
public class Bigram {

    private String firstWord;
    private String secondWord;
    private int count;

    /**
     * Constructor for creating a new Bigram object BEFORE it goes into the database
     */
    public Bigram(String firstWord, String secondWord) {
        this.firstWord = firstWord;
        this.secondWord = secondWord;
        // Before the source is analyzed the bigram count is set to 0
        this.count = 0;
    }

    /**
     * Constructor for reconstructing a Bigram retrieved FROM the database.
     */
    public Bigram(String firstWord, String secondWord, int count) {
        this.firstWord = firstWord;
        this.secondWord = secondWord;
        this.count = count;
    }

    public String getFirstWord() { return firstWord; }
    public void setFirstWord(String firstWord) { this.firstWord = firstWord; }

    public String getSecondWord() { return secondWord; }
    public void setSecondWord(String secondWord) { this.secondWord = secondWord; }

    public int getCount() { return count; }
    public void setCount(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Bigram count cannot be negative");
        }
        this.count = count;
    }
}
