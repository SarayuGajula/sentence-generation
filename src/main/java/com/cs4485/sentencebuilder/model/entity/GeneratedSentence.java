package com.cs4485.sentencebuilder.model.entity;

import java.util.Date;

/**
 * Represents a Generated Sentence entity in the application and maps directly to the 'generated_sentences' table in the database.
 * @author Daniel Dimitrov
 * 04/21/2026 - Initial creation
 */
public class GeneratedSentence {

    private int id;
    private String sentence;
    private String algorithm;
    private Date generatedAt;

    /**
     * Constructor for creating a new Generated Sentence object BEFORE it goes into the database.
     * (The database will auto-generate the ID).
     */
    public GeneratedSentence(String sentence, String algorithm) {
        this.sentence = sentence;
        this.algorithm = algorithm;
        this.generatedAt = new Date();
    }

    /**
     * Constructor for reconstructing a Generated Sentence retrieved FROM the database.
     * Includes the database-generated ID.
     */
    public GeneratedSentence(int id, String sentence, String algorithm, Date generatedAt) {
        this.id = id;
        this.sentence = sentence;
        this.algorithm = algorithm;
        this.generatedAt = generatedAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSentence() { return sentence; }
    public void setSentence(String sentence) { this.sentence = sentence; }

    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

    public Date getGeneratedAt() { return generatedAt; }
}
