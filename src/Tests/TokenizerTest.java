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

    @Test
    void isSingleCharacter_TokenContentMatchesPlus_ReturnsTokenWithTypeOP_PLUS() throws IOException {
        Token.Type expected = Token.Type.OP_PLUS;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "+");
        actual = tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void isSingleCharacter_TokenContentMatchesPlusEquals_ReturnsTokenWithTypeOP_PLUS_EQUALS() throws IOException {
        Token.Type expected = Token.Type.OP_PLUS_EQUALS;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        mockReader.nextChar = '=';
        Token actual = new Token(Token.Type.NOTATOKEN, "+");
        actual = tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void isSingleCharacter_TokenContentMatchesMinus_ReturnsTokenWithTypeOP_MINUS() throws IOException {
        Token.Type expected = Token.Type.OP_MINUS;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "-");
        actual = tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void isSingleCharacter_TokenContentMatchesMinusEquals_ReturnsTokenWithTypeOP_MINUS_EQUALS() throws IOException {
        Token.Type expected = Token.Type.OP_MINUS_EQUALS;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        mockReader.nextChar = '=';
        Token actual = new Token(Token.Type.NOTATOKEN, "-");
        actual = tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesTimes_ReturnsTokenWithTypeOP_TIMES() throws IOException {
        Token.Type expected = Token.Type.OP_TIMES;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "*");
        actual = tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesTimesEquals_ReturnsTokenWithTypeOP_TIMES_EQUALS() throws IOException {
        Token.Type expected = Token.Type.OP_TIMES_EQUALS;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        mockReader.nextChar = '=';
        Token actual = new Token(Token.Type.NOTATOKEN, "*");
        actual = tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesDivide_ReturnsTokenWithTypeOP_DIVIDE() throws IOException {
        Token.Type expected = Token.Type.OP_DIVIDE;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "/");
        actual = tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesDivideEquals_ReturnsTokenWithTypeOP_DIVIDE_EQUALS() throws IOException {
        Token.Type expected = Token.Type.OP_DIVIDE_EQUALS;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        mockReader.nextChar = '=';
        Token actual = new Token(Token.Type.NOTATOKEN, "/");
        actual = tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesDivideTimes_CallsReadToEndOfComment() throws IOException {
        mockReader.nextChar = '*';
        Token actual = new Token(Token.Type.NOTATOKEN, "/");
        actual = tokenizer.IsSingleCharacter(actual);

        Mockito.verify(mockReader).readToEndOfComment();
    }
    /*@Test  //TODO find ud af hvordan man laver en test til en "uendelig" while lykke
    void isSingleCharacter_TokenContentMatchesDivideDivide_CallsReadChar() throws IOException {
        Mockito.when(mockReader.readChar()).thenReturn('\n');


        mockReader.nextChar = '/';
        Token actual = new Token(Token.Type.NOTATOKEN, "/");
        mockReader.currentChar = '\n';
        actual = tokenizer.IsSingleCharacter(actual);


        Mockito.verify(mockReader).readChar();
    }*/
    @Test
    void isSingleCharacter_TokenContentMatchesEquals_ReturnsTokenWithTypeASSIGN() throws IOException {
        Token.Type expected = Token.Type.ASSIGN;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "=");
        actual = tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesEqualsEquals_ReturnsTokenWithTypeLOP_EQUALS() throws IOException {
        Token.Type expected = Token.Type.LOP_EQUALS;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        mockReader.nextChar = '=';
        Token actual = new Token(Token.Type.NOTATOKEN, "=");
        actual = tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesModulo_ReturnsTokenWithTypeOP_MODULO() throws IOException {
        Token.Type expected = Token.Type.OP_MODULO;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "%");
        actual = tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesExclamationMark_ReturnsTokenWithTypeLOP_NOT() throws IOException {
        Token.Type expected = Token.Type.OP_NOT;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "!");
        actual = tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesExclamationMarkEquals_ReturnsTokenWithTypeLOP_NOTEQUAL() throws IOException {
        Token.Type expected = Token.Type.LOP_NOTEQUAL;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        mockReader.nextChar = '=';
        Token actual = new Token(Token.Type.NOTATOKEN, "!");
        actual = tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }


}