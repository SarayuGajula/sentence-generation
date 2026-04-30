package com.cs4485.sentencebuilder;

/**
 * Provides utility methods for autocomplete.
 * Gets the top three words
 * @author Jeffrey Gilbert
 * 04/15/2026 Initial File
 * 04/23/2026 Fill in methods
 */

import java.util.ArrayList;
import java.util.List;

import com.cs4485.sentencebuilder.model.entity.Word;
import com.cs4485.sentencebuilder.model.entity.Bigram;
import com.cs4485.sentencebuilder.model.dao.WordDAO;
import com.cs4485.sentencebuilder.model.dao.BigramDAO;

public class Autocomplete {

    static final String punctRegex = "[.!?;]+";

    private final WordDAO wordDAO;
    private final BigramDAO bigramDAO;

    /**
     * Production Constructor:
     * Defaults to the real database
     */
    public Autocomplete() {
        this.wordDAO = new WordDAO();
        this.bigramDAO = new BigramDAO();
    }

    /**
     * Testing Constructor:
     * Accepts a custom way to reach database
     */
    public Autocomplete(WordDAO wordDAO, BigramDAO bigramDAO) {
        this.wordDAO = wordDAO;
        this.bigramDAO = bigramDAO;
    }

    public List<String> suggestThreeWords(String start){
        String word = start.toLowerCase().replaceAll(punctRegex + "$", ""); // word to display next
        int count = 0; // for debugging

        List<Bigram> bigrams = bigramDAO.getTopKMostCommonBigramsStartingWithWord(3, word.toLowerCase()); // get 3 most common bigrams
        List<String> wordOptions = new ArrayList<>();
        for (Bigram bigram : bigrams) {
            wordOptions.add(checkCapitalization(bigram.getSecondWord()));
        }

        return wordOptions;
    }

    private String checkCapitalization(String str){
        Word word = wordDAO.get(str);
        String strCopy = str;
        int wordCount = word.getTotalCount();
        if(word.getUppercaseCount() == wordCount){ // all previous instances are all uppercase
            strCopy = str.toUpperCase();
        }
        else if(word.getTitleCount() == wordCount){ // all previous instances are all titlecase
            strCopy = Character.toUpperCase(str.charAt(0)) + str.substring(1);
        }
        return strCopy;
    }

}