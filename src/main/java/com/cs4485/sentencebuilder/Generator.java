package com.cs4485.sentencebuilder;

/**
 * Provides utility methods for generator sentences.
 * Two methods:
 *      Following word is the most common
 *      Get top 5 words, weight them with probabilities and randomly pick one
 * @author Joe Su
 * 04/02/2026 - Initial creation, needs update for capitalization and probabilities - Joe
 */

import java.util.List;

import com.cs4485.sentencebuilder.model.entity.Bigram;
import com.cs4485.sentencebuilder.model.dao.BigramDAO;

public class Generator {

    static BigramDAO bigramDAO = new BigramDAO();
    static final String punctRegex = "[.!?;]+";

    public static String commonGenerator(String first){
        StringBuilder sb = new StringBuilder(first);
        String word = first.toLowerCase().replaceAll(punctRegex + "$", ""); // word to display next
        int count = 0; // for debugging
        while(!word.matches(punctRegex) && count < 50){
            if(!word.equals(first)){ sb.append(" ").append(word); }

            Bigram commonBigram = bigramDAO.getTopKMostCommonBigramsStartingWithWord(1, word).get(0);
            word = commonBigram.getSecondWord();
            count++;
        }
        sb.append(word);

        return sb.toString();
    }

    public static String topFiveWordsGenerator(String first){
        StringBuilder sb = new StringBuilder(first);
        String word = first.toLowerCase().replaceAll(punctRegex + "$", ""); // word to display next
        int count = 0; // for debugging
        while(!word.matches(punctRegex) && count < 50){
            if(!word.equals(first)){ sb.append(" ").append(word); }

            List<Bigram> commonBigram = bigramDAO.getTopKMostCommonBigramsStartingWithWord(5, word);
            // probability stuff, update word
            count++;
        }
        sb.append(word);

        return sb.toString();
    }
}
