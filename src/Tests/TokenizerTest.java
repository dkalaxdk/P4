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
        Token expected = new Token(Token.Type.NOTATOKEN, "NotAKeyWord");
        // Mock the Reader
        Reader mockReader = Mockito.mock(Reader.class);
        //Mockito.when(mockReader.read()).thenReturn((int)expected);
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "NotAKeyWord");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual);
    }

    @Test
    void isSingleCharacter_TokenIsNotSingleCharacter_ReturnsTokenWithTypeNOTATOKEN() throws IOException {
        Token expected = new Token(Token.Type.NOTATOKEN, "NotASingleCharacter");
        // Mock the Reader
        Reader mockReader = Mockito.mock(Reader.class);
        //Mockito.when(mockReader.read()).thenReturn((int)expected);
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "NotASingleCharacter");
        actual = tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual);
    }

    @Test
    void findNumberTokenType_TokenIsNotNumber_ReturnsTokenWithTypeNOTATOKEN() throws IOException {
        Token expected = new Token(Token.Type.NOTATOKEN, "NotANumber");
        // Mock the Reader
        Reader mockReader = Mockito.mock(Reader.class);
        Mockito.when(mockReader.findNumber()).thenReturn("");
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "NotANumber");
        actual = tokenizer.findNumberTokenType(actual);

        assertEquals(expected, actual);
    }
}