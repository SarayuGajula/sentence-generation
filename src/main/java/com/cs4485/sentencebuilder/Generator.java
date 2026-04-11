package com.cs4485.sentencebuilder;

/**
 * Provides utility methods for generator sentences.
 * Two methods:
 *      Following word is the most common
 *      Get top 5 words, weight them with probabilities and randomly pick one
 * @author Joe Su
 * 04/02/2026 - Initial creation, needs update for capitalization and probabilities - Joe
 * 04/10/2026 - Added capitalization and selection based on probability - Joe
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.cs4485.sentencebuilder.model.entity.Word;
import com.cs4485.sentencebuilder.model.entity.Bigram;
import com.cs4485.sentencebuilder.model.dao.WordDAO;
import com.cs4485.sentencebuilder.model.dao.BigramDAO;

public class Generator {

    static BigramDAO bigramDAO = new BigramDAO();
    static WordDAO wordDAO = new WordDAO();
    static final String punctRegex = "[.!?;]+";

    public static String commonGenerator(String first){
        StringBuilder sb = new StringBuilder(first);
        String word = first.toLowerCase().replaceAll(punctRegex + "$", ""); // word to display next
        int count = 0; // for debugging
        while(!word.matches(punctRegex) && count < 50){
            if(!word.equals(first)){ sb.append(" ").append(word); }

            Bigram commonBigram = bigramDAO.getTopKMostCommonBigramsStartingWithWord(1, word.toLowerCase()).getFirst();
            word = checkCapitalization(commonBigram.getSecondWord());
            count++;

        }
        sb.append(word);

        return sb.toString();
    }

    public static String topFiveWordsGenerator(String first){
        StringBuilder sb = new StringBuilder(first);
        String word = first.toLowerCase().replaceAll(punctRegex + "$", ""); // word to display next
        int count = 0; // for debugging
        Random r = new Random();

        while(!word.matches(punctRegex) && count < 50){
            if(!word.equals(first)){ sb.append(" ").append(word); }

            List<Bigram> commonBigram = bigramDAO.getTopKMostCommonBigramsStartingWithWord(5, word.toLowerCase());
            int index = -1;
            List<Integer> countList = new ArrayList<>();
            int totalCount = 0;
            for(Bigram bigram : commonBigram){
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

            word = checkCapitalization(commonBigram.get(index).getSecondWord());
            count++;
        }
        sb.append(word);

        return sb.toString();
    }

    private static String checkCapitalization(String str){
        Word word = wordDAO.get(str);
        String strCopy = str;
        int wordCount = word.getTotalCount()
        if(word.getUppercaseCount() == wordCount){ // all previous instances are all uppercase
            strCopy = str.toUpperCase();
        }
        else if(word.getTitleCount() == wordCount){ // all previous instances are all titlecase
            strCopy = Character.getName(str.charAt(0)) + str.substring(1);
        }
        return strCopy;
    }

}
