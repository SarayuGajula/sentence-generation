package com.cs4485.sentencebuilder;

import java.util.HashMap; // HashMap lets us store key-value pairs (token -> Word)
import java.util.List;    // List is the data type for the input (a list of words)
import java.util.Map;     // Map is the data type we return (token -> Word)
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cs4485.sentencebuilder.model.entity.Word;
import com.cs4485.sentencebuilder.model.entity.Bigram;

/**
 * Provides utility methods for analyzing word frequency and bigrams.
 * Maps to the 'words' and 'bigrams' tables in the database.
 * @author Sarayu Gajula, Jeffrey Gilbert, Joe Su, Daniel Dimitrov
 * 3/14/2026 - Initial creation - Sarayu
 * 3/31/2026 - Changed getWords to use Word object - Jeff + Joe
 * 4/2/2026 - Fixed capitalization counts, added punctuation processing, updated getBigrams to use Bigram object - Joe
 * 4/22/2026 - Added more robust punctuation handling - Joe
 * 4/23/2026 - Fixed punctuation handling - Joe
 * 4/23/2026 - Expanded garbage regex to handle more garbage - Daniel
 */
public class WordAnalyzer {

    static final String punctRegex = ".!?;"; // punctuation we want to keep
    static final String startPunctRegex = "^[" + punctRegex + "]+";
    static final String endPunctRegex = "[" + punctRegex + "]+$";
    static final String endPunctRegexMatch = ".*" + endPunctRegex; // used for matching instead of replacing
    static final String garbage = "[-_,'\"“”‘’()\\]\\[:*#+$0-9%]+"; // punctuation we don't want to store (neither starting nor ending)

    /**
     * Takes a list of words and converts them to Word objects with updated counts
     */
    public static Map<String, Word> getWords(List<String> words) {
        
        // Create an empty map to store each token and its Word
        Map<String,Word> wordCounts = new HashMap<>();

        // Loop through every word in the list
        for (int i = 0; i < words.size(); i++) {
            String word = cleanWord(words.get(i));
            String rawWord = getRawWord(word); // get rid of starting and ending punctuation, used for capitalization counts
            if(rawWord.isEmpty()) { continue; }

            String token = rawWord.toLowerCase(); // get rid of capitalization
            int startCount = 0;
            int endCount = 0;
            int uppercaseCount = 0;
            int titleCount = 0;

            // get counts
            if(i == 0){ startCount = 1; }
            else if(cleanWord(words.get(i-1)).matches(endPunctRegexMatch)) { startCount = 1; }
            if(word.matches(endPunctRegexMatch)) { endCount = 1; }
            if(token.toUpperCase().equals(rawWord)) {uppercaseCount = 1; }
            else if((Character.toUpperCase(token.charAt(0)) + token.substring(1)).equals(rawWord)) { titleCount = 1; }

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

            String punct;
            Matcher m = Pattern.compile(endPunctRegex).matcher(word);
            if(m.find()){ // punctuation detected
                punct = m.group();
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

            String firstWord = cleanWord(words.get(i));
            String firstToken = getRawWord(firstWord).toLowerCase();
            if(firstWord.matches(endPunctRegexMatch)){
                Matcher m = Pattern.compile(endPunctRegex).matcher(firstWord);
                m.find();
                String punct = m.group();

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

            String secondToken = getRawWord(words.get(i+1)).toLowerCase();
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

    private static String getRawWord(String word){
        String rawWord = cleanWord(word);
        rawWord = rawWord.replaceAll(endPunctRegex, "");
        rawWord = rawWord.replaceAll(startPunctRegex, "");
        return rawWord;
    }

    private static String cleanWord(String word){
        return  word.replaceAll(garbage + "$", "").replaceAll("^" + garbage, "");
    }
}
