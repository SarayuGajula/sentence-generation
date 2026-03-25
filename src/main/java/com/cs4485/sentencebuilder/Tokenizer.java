package com.cs4485.sentencebuilder;

/**
 * Provides utility methods for importing and tokenizing text files.
 * Reads text from a file and splits it into sentences and words.
 * @author Sarayu Gajula
 * 03/14/2026 - Initial creation
 */

import java.io.IOException;         // Needed for handling file reading errors
import java.nio.file.Files;         // Lets us read files easily
import java.nio.file.Paths;         // Lets us turn a file path string into a Path object
import java.util.ArrayList;         // ArrayList is a resizable list
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
     * Keeps punctuation and capitalization exactly as they appear in the text.
     * Example: "Hello world. How are you." → [["Hello", "world."], ["How", "are", "you."]]
     */
    public static List<List<String>> tokenize(String text) {

        // Create an empty list that will hold all our sentences
        List<List<String>> allSentences = new ArrayList<>();

        // Split the text into sentences after each period, exclamation mark, or question mark
        // The punctuation is KEPT at the end of the word (not thrown away)
        String[] sentences = text.split("(?<=[.!?])\\s+");

        // Loop through each sentence
        for (String sentence : sentences) {

            // Remove any extra spaces from the start and end of the sentence
            sentence = sentence.trim();

            // Skip the sentence if it's empty
            if (sentence.isEmpty()) continue;

            // Split the sentence into individual words wherever there is a space
            String[] words = sentence.split("\\s+");

            // Create a list to hold the words for this sentence
            List<String> wordList = new ArrayList<>();

            // Loop through each word and add it as-is (keeping punctuation and capitalization)
            for (String word : words) {
                if (!word.isEmpty()) {
                    wordList.add(word);
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
