package com.cs4485.sentencebuilder;

/**
 * Provides utility methods for importing and tokenizing text files.
 * Reads text from a file and splits it into words.
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
     * Takes a block of text and splits it into individual words.
     * Punctuation stays attached to the word before it.
     * Example: "Hello! How are you?" → ["Hello!", "How", "are", "you?"]
     */
    public static List<String> tokenize(String text) {

        // Create an empty list to hold all the words
        List<String> words = new ArrayList<>();

        // Split the text into words wherever there is a space
        String[] splitWords = text.split("\\s+");

        // Loop through each word and add it to the list as-is
        for (String word : splitWords) {

            // Only add the word if it's not empty
            if (!word.isEmpty()) {
                words.add(word);
            }
        }

        // Return the full list of words
        return words;
    }
}
