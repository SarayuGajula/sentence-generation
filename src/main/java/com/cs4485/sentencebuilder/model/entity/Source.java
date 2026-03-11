package com.cs4485.sentencebuilder.model.entity;

/**
 * Represents a Source entity in the application and maps directly to the 'sources' table in the database.
 * @author Daniel Dimitrov
 */
public class Source {

    private int id;
    private String filename;
    private int wordCount;

    /**
     * Constructor for creating a new Source object BEFORE it goes into the database.
     * (The database will auto-generate the ID).
     */
    public Source(String filename) {
        this.filename = filename;
        // Before the source is analyzed the word count is set to 0
        wordCount = 0;
    }

    /**
     * Constructor for reconstructing a User retrieved FROM the database.
     * Includes the database-generated ID.
     */
    public Source(int id, String filename, int wordCount) {
        this.id = id;
        this.filename = filename;
        this.wordCount = wordCount;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public int getWordCount() { return wordCount; }
    public void setWordCount(int wordCount) { this.wordCount = wordCount; }
}
