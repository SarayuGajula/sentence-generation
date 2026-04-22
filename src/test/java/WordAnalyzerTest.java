import com.cs4485.sentencebuilder.WordAnalyzer;
import com.cs4485.sentencebuilder.model.entity.Bigram;
import com.cs4485.sentencebuilder.model.entity.Word;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the WordAnalyzer
 * @author Joe Su
 * 4/13/2026 - Initial creation
 * 4/22/2026 - Updates to test punctuation handling
 */

public class WordAnalyzerTest {
    List<String> testList = List.of("\"Sentence", "one.", "Sentence", "-TWO!");

    @Test
    public void testGetWordsTotalCounts(){
        Map<String, Word> wordMap = WordAnalyzer.getWords(testList);
        assertEquals(wordMap.size(), 5);
        assertEquals(wordMap.get("sentence").getTotalCount(), 2);
        assertEquals(wordMap.get("one").getTotalCount(), 1);
        assertEquals(wordMap.get("two").getTotalCount(), 1);
        assertEquals(wordMap.get("!").getTotalCount(), 1);
        assertEquals(wordMap.get(".").getTotalCount(), 1);
    }

    @Test
    public void testGetWordsStartCounts(){
        Map<String, Word> wordMap = WordAnalyzer.getWords(testList);
        assertEquals(wordMap.get("sentence").getStartCount(), 2);
        assertEquals(wordMap.get("one").getStartCount(), 0);
        assertEquals(wordMap.get("two").getStartCount(), 0);
        assertEquals(wordMap.get("!").getStartCount(), 0);
        assertEquals(wordMap.get(".").getStartCount(), 0);
    }

    @Test
    public void testGetWordsEndCounts(){
        Map<String, Word> wordMap = WordAnalyzer.getWords(testList);
        assertEquals(wordMap.get("sentence").getEndCount(), 0);
        assertEquals(wordMap.get("one").getEndCount(), 1);
        assertEquals(wordMap.get("two").getEndCount(), 1);
        assertEquals(wordMap.get("!").getEndCount(), 0);
        assertEquals(wordMap.get(".").getEndCount(), 0);
    }

    @Test
    public void testGetWordsTitleCounts(){
        Map<String, Word> wordMap = WordAnalyzer.getWords(testList);
        assertEquals(wordMap.get("sentence").getTitleCount(), 2);
        assertEquals(wordMap.get("one").getTitleCount(), 0);
        assertEquals(wordMap.get("two").getTitleCount(), 0);
        assertEquals(wordMap.get("!").getTitleCount(), 0);
        assertEquals(wordMap.get(".").getTitleCount(), 0);
    }

    @Test
    public void testGetWordsUppercaseCount(){
        Map<String, Word> wordMap = WordAnalyzer.getWords(testList);
        assertEquals(wordMap.get("sentence").getUppercaseCount(), 0);
        assertEquals(wordMap.get("one").getUppercaseCount(), 0);
        assertEquals(wordMap.get("two").getUppercaseCount(), 1);
        assertEquals(wordMap.get("!").getUppercaseCount(), 0);
        assertEquals(wordMap.get(".").getUppercaseCount(), 0);
    }

    @Test
    public void testGetBigramsCounts(){
        Map<String, Bigram> bigramMap = WordAnalyzer.getBigrams(testList);
        assertEquals(bigramMap.get("sentence one").getCount(), 1);
        assertEquals(bigramMap.get("one .").getCount(), 1);
        assertEquals(bigramMap.get("sentence two").getCount(), 1);
        assertEquals(bigramMap.get("two !").getCount(), 1);
    }

    @Test
    public void testGetBigramsFirstWord(){
        Map<String, Bigram> bigramMap = WordAnalyzer.getBigrams(testList);
        assertEquals(bigramMap.get("sentence one").getFirstWord(), "sentence");
        assertEquals(bigramMap.get("one .").getFirstWord(), "one");
        assertEquals(bigramMap.get("sentence two").getFirstWord(), "sentence");
        assertEquals(bigramMap.get("two !").getFirstWord(), "two");
    }

    @Test
    public void testGetBigramsSecondWord(){
        Map<String, Bigram> bigramMap = WordAnalyzer.getBigrams(testList);
        assertEquals(bigramMap.get("sentence one").getSecondWord(), "one");
        assertEquals(bigramMap.get("one .").getSecondWord(), ".");
        assertEquals(bigramMap.get("sentence two").getSecondWord(), "two");
        assertEquals(bigramMap.get("two !").getSecondWord(), "!");
    }
}