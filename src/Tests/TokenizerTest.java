package Tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sw417f20.ebal.Reader.Reader;
import sw417f20.ebal.Token;
import sw417f20.ebal.Tokenizer;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TokenizerTest {
    private Reader mockReader;
    private Tokenizer tokenizer;
    @BeforeEach
    void setUp() {
        // Mock the reader class needed as parameter for tokenizer
        mockReader = Mockito.mock(Reader.class);
        tokenizer = new Tokenizer(mockReader);
        //Mockito.when(mockReader.read()).thenReturn((int)expected);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getToken() {
    }

    /**
     * Test for the findNumberTokenType method
     */
    @Test
    void findNumberTokenType_TokenIsNotNumber_ReturnsTokenWithTypeNOTATOKEN() {
        Token.Type expected = Token.Type.NOTATOKEN;

        Token actual = new Token(Token.Type.NOTATOKEN, "NotANumber");
        actual = tokenizer.findNumberTokenType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findNumberTokenType_TokenContentIsEmpty_ReturnsTokenWithTypeNOTATOKEN() {
        Token.Type expected = Token.Type.NOTATOKEN;

        Token actual = new Token(Token.Type.NOTATOKEN, "");
        actual = tokenizer.findNumberTokenType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findNumberTokenType_TokenContentIsAFloat_ReturnsTokenWithTypeLIT_Float() {
        Token.Type expected = Token.Type.LIT_Float;

        Token actual = new Token(Token.Type.NOTATOKEN, "1.2");
        actual = tokenizer.findNumberTokenType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findNumberTokenType_TokenContentIsAnInt_ReturnsTokenWithTypeLIT_Int() {
        Token.Type expected = Token.Type.LIT_Int;

        Token actual = new Token(Token.Type.NOTATOKEN, "123");
        actual = tokenizer.findNumberTokenType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findNumberTokenType_TokenContentIsZero_ReturnsTokenWithTypeLIT_Int() {
        Token.Type expected = Token.Type.LIT_Int;

        Token actual = new Token(Token.Type.NOTATOKEN, "0");
        actual = tokenizer.findNumberTokenType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findNumberTokenType_TokenContentIsNumberFollowedByLetters_ReturnsTokenWithTypeERROR() {
        Token.Type expected = Token.Type.ERROR;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "1A");
        actual = tokenizer.findNumberTokenType(actual);

        assertEquals(expected, actual.type);
    }

    /**
     * Tests for the FindKeyword method
     */
    @Test
    void findKeyword_TokenIsNotKeyword_ReturnsTokenWithTypeNOTATOKEN() {
        Token.Type expected = Token.Type.NOTATOKEN;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "NotAKeyWord");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesMASTERKeyword_ReturnsTokenWithTypeMASTER() {
        Token.Type expected = Token.Type.MASTER;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "MASTER");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesSLAVEKeyword_ReturnsTokenWithTypeSLAVE() {
        Token.Type expected = Token.Type.SLAVE;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "SLAVE");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }


    /**
     * Tests for the isSingleCharacter method
     */
    @Test
    void isSingleCharacter_TokenIsNotSingleCharacter_ReturnsTokenWithTypeNOTATOKEN() throws IOException {
        Token.Type expected = Token.Type.NOTATOKEN;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "NotASingleCharacter");
        actual = tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }

}