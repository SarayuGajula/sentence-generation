package com.cs4485.sentencebuilder;
/**
 * Provides utility methods for analyzing word frequency and bigrams.
 * Maps to the 'words' and 'bigrams' tables in the database.
 * @author Sarayu Gajula, Jeffrey Gilbert, Joe Su
 * 03/14/2026 - Initial creation
 * 03/31/2026 - Edited
 */
import java.util.HashMap; // HashMap lets us store key-value pairs (word -> count)
import java.util.List;    // List is the data type for the input (a list of words)
import java.util.Map;     // Map is the data type we return (word -> count)
import java.util.ArrayList;

import com.cs4485.sentencebuilder.model.entity.Word;

public class WordAnalyzer {

    /**
     * Takes a list of words and counts how many times each word appears.
     * Example: ["the", "cat", "the"] → {"the"=2, "cat"=1}
     */
    public static Map<String, Word> getWords(List<String> words) {
        
        // Create an empty map to store each word and its count
        // Map<String, Integer> wordCounts = new HashMap<>();
        Map<String,Word> wordCounts = new HashMap<String,Word>();



        // Loop through every word in the list
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            // first word is a starting word
            String token = word.toLowerCase().replaceAll("[\\p{Punct}&&[^-']]+", ""); // get rid of capitalization and ending punctuation
            int startCount = 0;
            int endCount = 0;
            int uppercaseCount = 0;
            int titleCount = 0;

            if(i == 0){ startCount = 1; }
            if(i > 0 && words.get(i-1).matches("[\\p{Punct}&&[^-',]]+")) { startCount = 1; }
            if(word.matches("[\\p{Punct}&&[^-',]]+")){ endCount = 1; }
            if(token.toUpperCase().equals(token)) {uppercaseCount = 1; }
            if((Character.toUpperCase(token.charAt(0)) + token.substring(1, token.length())).equals(word.replaceAll("[\\p{Punct}&&[^-']]+", ""))) { titleCount = 1; } // to fix later, this is ugly

            // if word is not in list, initialize
            if(!wordCounts.containsKey(token)){
                // if it's not the first word and the word before has ending punctuation
                Word newWord = new Word(token, 1, startCount, endCount, uppercaseCount, titleCount);
                wordCounts.put(token, newWord);
            }

            // if word is already in list, update counts
            else{
                Word oldWord = wordCounts.get(token);
                oldWord.setTotalCount(oldWord.getTotalCount() + 1);
                oldWord.setStartCount(oldWord.getStartCount() + startCount);
                oldWord.setEndCount(oldWord.getEndCount() + endCount);
                oldWord.setUppercaseCount(oldWord.getUppercaseCount() + uppercaseCount);
                oldWord.setTitleCount(oldWord.getTitleCount() + titleCount);
            }

        }
        
        // Return the finished map of words and their counts
        return wordCounts;
    }

    /**
     * Takes a list of words and finds all bigrams (pairs of consecutive words).
     * Example: ["the", "cat", "sat"] → {"the cat"=1, "cat sat"=1}
     */
    public static Map<String, Integer> getBigrams(List<String> words) {
        
        // Create an empty map to store each word pair and its count
        Map<String, Integer> bigrams = new HashMap<>();
        
        // Loop through the list, stopping one before the end
        // because we always need a current word AND the next word
        for (int i = 0; i < words.size() - 1; i++) {
            
            // Combine the current word and the next word into one string
            // Example: "the" + " " + "cat" = "the cat"
            String bigram = words.get(i) + " " + words.get(i + 1);
            
            // If this pair already exists, add 1 to its count
            // If it's new, start it at 0 then add 1
            bigrams.put(bigram, bigrams.getOrDefault(bigram, 0) + 1);
        }
        
        // Return the finished map of word pairs and their counts
        return bigrams;
    }
}
