package com.cs4485.sentencebuilder.model.entity;

/**
 * Represents a Source entity in the application and maps directly to the 'sources' table in the database.
 * @author Daniel Dimitrov
 * 02/11/2026 - Initial creation
 * 02/14/2026 - Minor fixes
 * 03/31/2026 - Added negative check and exception for word count setting
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
     * Constructor for reconstructing a Source retrieved FROM the database.
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
    public void setWordCount(int wordCount) {
        if (wordCount < 0) {
            throw new IllegalArgumentException("Source word count cannot be negative");
        }
        this.wordCount = wordCount;
    }
}
