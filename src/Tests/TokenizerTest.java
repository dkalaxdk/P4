package Tests;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sw417f20.ebal.Reader.Reader;
import sw417f20.ebal.Token;
import sw417f20.ebal.Tokenizer;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TokenizerTest {

    @Test
    void getToken() {
    }

    @Test
    void findKeyword_TokenIsNotKeyword_ReturnsTokenWithTypeNOTATOKEN() {
        Token.Type expected = Token.Type.NOTATOKEN;
        // Mock the Reader
        Reader mockReader = Mockito.mock(Reader.class);
        //Mockito.when(mockReader.read()).thenReturn((int)expected);
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "NotAKeyWord");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void isSingleCharacter_TokenIsNotSingleCharacter_ReturnsTokenWithTypeNOTATOKEN() throws IOException {
        Token.Type expected = Token.Type.NOTATOKEN;
        Reader mockReader = Mockito.mock(Reader.class);
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "NotASingleCharacter");
        actual = tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findNumberTokenType_TokenIsNotNumber_ReturnsTokenWithTypeNOTATOKEN() {
        Token.Type expected = Token.Type.NOTATOKEN;
        Reader mockReader = Mockito.mock(Reader.class);
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "NotANumber");
        actual = tokenizer.findNumberTokenType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findNumberTokenType_TokenContentIsEmpty_ReturnsTokenWithTypeNOTATOKEN() {
        Token.Type expected = Token.Type.NOTATOKEN;
        Reader mockReader = Mockito.mock(Reader.class);
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "");
        actual = tokenizer.findNumberTokenType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findNumberTokenType_TokenContentIsAFloat_ReturnsTokenWithTypeLIT_Float() {
        Token.Type expected = Token.Type.LIT_Float;
        Reader mockReader = Mockito.mock(Reader.class);
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "1.2");
        actual = tokenizer.findNumberTokenType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findNumberTokenType_TokenContentIsAnInt_ReturnsTokenWithTypeLIT_Int() {
        Token.Type expected = Token.Type.LIT_Int;
        Reader mockReader = Mockito.mock(Reader.class);
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "123");
        actual = tokenizer.findNumberTokenType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findNumberTokenType_TokenContentIsZero_ReturnsTokenWithTypeLIT_Int() {
        Token.Type expected = Token.Type.LIT_Int;
        Reader mockReader = Mockito.mock(Reader.class);
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "0");
        actual = tokenizer.findNumberTokenType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findNumberTokenType_TokenContentIsNumberFollowedByLetters_ReturnsTokenWithTypeERROR() {
        Token.Type expected = Token.Type.ERROR;
        Reader mockReader = Mockito.mock(Reader.class);
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "1A");
        actual = tokenizer.findNumberTokenType(actual);

        assertEquals(expected, actual.type);
    }
}