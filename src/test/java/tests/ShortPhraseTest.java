package tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShortPhraseTest {
    @Test
    public void ShortPhraseTest(){
        String shortText = "short";
        String longText = "longTextMoreThan15characters";
        assertTrue(longText.length() > 15, "Text: "+ longText + " - too short (less than 15 characters");
        assertTrue(shortText.length() > 15, "Text: "+ shortText + " - too short (less than 15 characters");
    }
}
