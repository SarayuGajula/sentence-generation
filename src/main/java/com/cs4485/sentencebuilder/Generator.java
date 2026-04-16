package com.cs4485.sentencebuilder;

/**
 * Provides utility methods for generator sentences.
 * Two methods:
 *      Following word is the most common
 *      Get top 5 words, weight them with probabilities and randomly pick one
 * @author Joe Su
 * 04/02/2026 - Initial creation, needs update for capitalization and probabilities - Joe
 * 04/10/2026 - Added capitalization and selection based on probability - Joe
 * 4/16/2026 - Updates to add initializing with DAO's for testing - Joe
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.cs4485.sentencebuilder.model.entity.Word;
import com.cs4485.sentencebuilder.model.entity.Bigram;
import com.cs4485.sentencebuilder.model.dao.WordDAO;
import com.cs4485.sentencebuilder.model.dao.BigramDAO;

public class Generator {

    static final String punctRegex = "[.!?;]+";

    private final WordDAO wordDAO;
    private final BigramDAO bigramDAO;

    /**
     * Production Constructor:
     * Defaults to the real database
     */
    public Generator() {
        this.wordDAO = new WordDAO();
        this.bigramDAO = new BigramDAO();
    }

    /**
     * Testing Constructor:
     * Accepts a custom way to reach database
     */
    public Generator(WordDAO wordDAO, BigramDAO bigramDAO) {
        this.wordDAO = wordDAO;
        this.bigramDAO = bigramDAO;
    }

    public String commonGenerator(String first){
        StringBuilder sb = new StringBuilder();
        sb.append(first);
        String word = first.toLowerCase().replaceAll(punctRegex + "$", ""); // word to display next
        int count = 0; // for debugging

        while(!word.matches(punctRegex) && count < 50){

            List<Bigram> bigrams = bigramDAO.getTopKMostCommonBigramsStartingWithWord(1, word.toLowerCase());
            if(bigrams == null || bigrams.isEmpty()){ break; }
            Bigram commonBigram = bigrams.get(0);
            String next = checkCapitalization(commonBigram.getSecondWord());

            if(next.equalsIgnoreCase(word)){ break; } // prevents self-loop
            if(next == null || next.isEmpty()){ break; }
            if(next.matches(punctRegex)){
                sb.append(next);
                break;
            }

            sb.append(" ").append(next);

            word = next.toLowerCase().replaceAll(punctRegex + "$", "");
            count++;
        }

        return sb.toString();
    }

    public String topFiveWordsGenerator(String first){
        StringBuilder sb = new StringBuilder(first);
        String word = first.toLowerCase().replaceAll(punctRegex + "$", ""); // word to display next
        int count = 0; // for debugging
        Random r = new Random();

        while(!word.matches(punctRegex) && count < 50){
            if(!word.equals(first)){ sb.append(" ").append(word); }

            List<Bigram> bigrams = bigramDAO.getTopKMostCommonBigramsStartingWithWord(5, word.toLowerCase());
            if(bigrams == null || bigrams.isEmpty()){ break; }

            int index = -1;
            List<Integer> countList = new ArrayList<>();
            int totalCount = 0;
            for(Bigram bigram : bigrams){
                countList.add(bigram.getCount());
                totalCount += bigram.getCount();
            }

            int prob = r.nextInt(totalCount);
            int thresh = 0;

            for(int i = 0; i < countList.size(); i++){
                int p = countList.get(i);
                thresh+=p;
                if(prob < thresh){
                    index = i;
                    break;
                }
            }

            if(thresh >= totalCount || index == -1){
                throw new Error("Probability selection failed!");
            }

            String next = checkCapitalization(bigrams.get(index).getSecondWord());
            if(next.equalsIgnoreCase(word)){ break; } // prevents self-loop
            if(next == null || next.isEmpty()){ break; }
            if(next.matches(punctRegex)){
                sb.append(next);
                break;
            }

            sb.append(" ").append(next);

            word = next.toLowerCase().replaceAll(punctRegex + "$", "");
            count++;
        }
        sb.append(word);

        return sb.toString();
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
