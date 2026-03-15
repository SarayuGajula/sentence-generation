package com.cs4485.sentencebuilder;

/**
 * Provides utility methods for importing and tokenizing text files.
 * Reads text from a file and splits it into sentences and words.
 * @author Jeffrey Gilbert
 * 03/14/2026 - Initial creation
 */

import java.io.IOException;         // Needed for handling file reading errors
import java.nio.file.Files;         // Lets us read files easily
import java.nio.file.Paths;         // Lets us turn a file path string into a Path object
import java.util.ArrayList;         // ArrayList is a resizable list
import java.util.Arrays;            // Arrays lets us convert arrays to lists
import java.util.List;              // List is the data type we return

public class Tokenizer {

    /**
     * Reads a text file and returns the entire contents as a single String.
     * Example: readFile("book.txt") → "Hello world. How are you."
     */
    public static String readFile(String filePath) throws IOException {
        
        // Read all the bytes from the file and convert to a String
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    /**
     * Takes a block of text, splits it into sentences,
     * then splits each sentence into individual words.
     * Example: "Hello world. How are you." → [["Hello", "world"], ["How", "are", "you"]]
     */
    public static List<List<String>> tokenize(String text) {

        // Create an empty list that will hold all our sentences
        List<List<String>> allSentences = new ArrayList<>();

        // Split the text into sentences wherever there is a period, 
        // exclamation mark, or question mark
        String[] sentences = text.split("[.!?]+");

        // Loop through each sentence
        for (String sentence : sentences) {

            // Remove any extra spaces from the start and end of the sentence
            sentence = sentence.trim();

            // Skip the sentence if it's empty
            if (sentence.isEmpty()) continue;

            // Split the sentence into individual words wherever there is a space
            // Also remove any punctuation from each word and make everything lowercase
            String[] words = sentence.split("\\s+");

            // Create a list to hold the cleaned words for this sentence
            List<String> wordList = new ArrayList<>();

            // Loop through each word and clean it up
            for (String word : words) {

                // Remove anything that isn't a letter or number
                String cleaned = word.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();

                // Only add the word if it's not empty after cleaning
                if (!cleaned.isEmpty()) {
                    wordList.add(cleaned);
                }
            }

            // Add this sentence's word list to our main list
            if (!wordList.isEmpty()) {
                allSentences.add(wordList);
            }
        }

        // Return the full list of sentences, each as a list of words
        return allSentences;
    }
}
