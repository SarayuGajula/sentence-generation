package com.cs4485.sentencebuilder;
/**
 * Provides utility methods for analyzing word frequency and bigrams.
 * Maps to the 'words' and 'bigrams' tables in the database.
 * @author Sarayu Gajula, Jeffrey Gilbert, Joe Su
 * 03/14/2026 - Initial creation - Sarayu
 * 03/31/2026 - Changed getWords to use Word object - Jeff + Joe
 * 04/02/2026 - Fixed capitalization counts, added punctuation processing, updated getBigrams to use Bigram object - Joe
 */
import java.util.HashMap; // HashMap lets us store key-value pairs (token -> Word)
import java.util.List;    // List is the data type for the input (a list of words)
import java.util.Map;     // Map is the data type we return (token -> Word)

import com.cs4485.sentencebuilder.model.entity.Word;
import com.cs4485.sentencebuilder.model.entity.Bigram;

public class WordAnalyzer {

    static final String punctRegex = "[.!?;]+";
    static final String endPunctRegex = ".*" + punctRegex + "$"; // checks if the word ends with punctuation
    static final String notPunct = "[^.!?;]+"; // finds everything that's not punctuation

    /**
     * Takes a list of words and converts them to Word objects with updated counts
     */
    public static Map<String, Word> getWords(List<String> words) {
        
        // Create an empty map to store each token and its Word
        Map<String,Word> wordCounts = new HashMap<String,Word>();

        // Loop through every word in the list
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            // first word is a starting word
            String rawWord = word.replaceAll(punctRegex + "$", ""); // get rid of ending punctuation, used for capitalization counts
            String token = rawWord.toLowerCase(); // get rid of capitalization
            int startCount = 0;
            int endCount = 0;
            int uppercaseCount = 0;
            int titleCount = 0;

            // get counts
            if(i <= 0){ startCount = 1; }
            else if(words.get(i-1).matches(endPunctRegex)) { startCount = 1; }
            if(word.matches(endPunctRegex)){ endCount = 1; }
            if(token.toUpperCase().equals(rawWord)) {uppercaseCount = 1; }
            if((Character.toUpperCase(token.charAt(0)) + token.substring(1)).equals(rawWord)) { titleCount = 1; }
            // what would capitalization do if the char doesn't have a capitalized version?

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

            String punct = word.replaceAll(notPunct, "");
            if(!punct.equals("")){ // punctuation detected

                // treat punctuation as a unique kind of word that only stores totalCount
                if(!wordCounts.containsKey(punct)){
                    Word newPunct = new Word(punct, 1, 0, 0, 0, 0);
                    wordCounts.put(punct, newPunct);
                }

                else{
                    Word oldPunct = wordCounts.get(punct);
                    oldPunct.setTotalCount(oldPunct.getTotalCount() + 1);
                }
            }

        }
        
        // Return the finished map of words and their counts
        return wordCounts;
    }

    /**
     * Takes a list of words and finds all bigrams (pairs of consecutive words).
     * Example: ["the", "cat", "sat"] → {"the cat"=1, "cat sat"=1}
     */
    public static Map<String, Bigram> getBigrams(List<String> words) {
        
        // Create an empty map to store each word pair and its count
        Map<String, Bigram> bigrams = new HashMap<>();
        // keys will be stored as "firstWord secondWord"

        for (int i = 0; i < words.size(); i++) {

            String firstWord = words.get(i);
            String firstToken = firstWord.toLowerCase().replaceAll(punctRegex, "");
            if(firstWord.matches(endPunctRegex)){
                String punct = firstWord.replaceAll(notPunct, "");
                String token = firstToken + " " + punct;

                if(!bigrams.containsKey(token)){
                    Bigram newBigram = new Bigram(firstToken, punct, 1);
                    bigrams.put(token, newBigram);
                }

                else{
                    Bigram oldBigram = bigrams.get(token);
                    oldBigram.setCount(oldBigram.getCount() + 1);
                }
            }

            if(i >= words.size() - 1){ continue; }

            String secondToken = words.get(i+1).toLowerCase().replaceAll(punctRegex, "");
            String token = firstToken + " " + secondToken;

            if(!bigrams.containsKey(token)){
                Bigram newBigram = new Bigram(firstToken, secondToken, 1);
                bigrams.put(token, newBigram);
            }

            else{
                Bigram oldBigram = bigrams.get(token);
                oldBigram.setCount(oldBigram.getCount() + 1);
            }

        }
        
        // Return the finished map of word pairs and their Bigrams
        return bigrams;
    }
}
