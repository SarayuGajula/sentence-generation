package com.cs4485.sentencebuilder;
/**
 * Provides utility methods for analyzing word frequency and bigrams.
 * Maps to the 'words' and 'bigrams' tables in the database.
 * @author Sarayu Gajula
 * 03/14/2026 - Initial creation
 */
import java.util.HashMap; // HashMap lets us store key-value pairs (word -> count)
import java.util.List;    // List is the data type for the input (a list of words)
import java.util.Map;     // Map is the data type we return (word -> count)

public class WordAnalyzer {

    /**
     * Takes a list of words and counts how many times each word appears.
     * Example: ["the", "cat", "the"] → {"the"=2, "cat"=1}
     */
    public static Map<String, Integer> getWordCounts(List<String> words) {
        
        // Create an empty map to store each word and its count
        Map<String, Integer> wordCounts = new HashMap<>();
        
        // Loop through every word in the list
        for (String word : words) {
            
            // If the word is already in the map, add 1 to its count
            // If it's not in the map yet, start it at 0 then add 1
            wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
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
