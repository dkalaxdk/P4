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
        //Mockito.when(mockReader.get("nextchar")).thenReturn((int)expected);
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

        Token actual = new Token(Token.Type.NOTATOKEN, "NotAKeyWord");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesMASTERKeyword_ReturnsTokenWithTypeMASTER() {
        Token.Type expected = Token.Type.MASTER;

        Token actual = new Token(Token.Type.NOTATOKEN, "MASTER");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesSLAVEKeyword_ReturnsTokenWithTypeSLAVE() {
        Token.Type expected = Token.Type.SLAVE;

        Token actual = new Token(Token.Type.NOTATOKEN, "SLAVE");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesENDKeyword_ReturnsTokenWithTypeEND() {
        Token.Type expected = Token.Type.END;

        Token actual = new Token(Token.Type.NOTATOKEN, "END");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesBEGINKeyword_ReturnsTokenWithTypeBEGIN() {
        Token.Type expected = Token.Type.BEGIN;

        Token actual = new Token(Token.Type.NOTATOKEN, "BEGIN");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesDigitalKeyword_ReturnsTokenWithTypeDigital() {
        Token.Type expected = Token.Type.DIGITAL;

        Token actual = new Token(Token.Type.NOTATOKEN, "digital");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesInputKeyword_ReturnsTokenWithTypeInput() {
        Token.Type expected = Token.Type.INPUT;

        Token actual = new Token(Token.Type.NOTATOKEN, "input");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesListenerKeyword_ReturnsTokenWithTypeListener() {
        Token.Type expected = Token.Type.LISTENER;

        Token actual = new Token(Token.Type.NOTATOKEN, "Listener");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesOutputKeyword_ReturnsTokenWithTypeOutput() {
        Token.Type expected = Token.Type.OUTPUT;

        Token actual = new Token(Token.Type.NOTATOKEN, "output");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesWriteKeyword_ReturnsTokenWithTypeWrite() {
        Token.Type expected = Token.Type.WRITE;

        Token actual = new Token(Token.Type.NOTATOKEN, "write");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesEventHandlerKeyword_ReturnsTokenWithTypeEventHandler() {
        Token.Type expected = Token.Type.EVENTHANDLER;

        Token actual = new Token(Token.Type.NOTATOKEN, "EventHandler");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesInitiateKeyword_ReturnsTokenWithTypeInitiate() {
        Token.Type expected = Token.Type.INITIATE;

        Token actual = new Token(Token.Type.NOTATOKEN, "Initiate");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesIfKeyword_ReturnsTokenWithTypeIf() {
        Token.Type expected = Token.Type.IF;

        Token actual = new Token(Token.Type.NOTATOKEN, "if");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesElseKeyword_ReturnsTokenWithTypeElse() {
        Token.Type expected = Token.Type.ELSE;

        Token actual = new Token(Token.Type.NOTATOKEN, "else");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesAnalogKeyword_ReturnsTokenWithTypeAnalog() {
        Token.Type expected = Token.Type.ANALOG;

        Token actual = new Token(Token.Type.NOTATOKEN, "analog");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesPwmKeyword_ReturnsTokenWithTypePwm() {
        Token.Type expected = Token.Type.PWM;

        Token actual = new Token(Token.Type.NOTATOKEN, "pwm");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesFlipKeyword_ReturnsTokenWithTypeFlip() {
        Token.Type expected = Token.Type.FLIP;

        Token actual = new Token(Token.Type.NOTATOKEN, "flip");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesConstantKeyword_ReturnsTokenWithTypeConstant() {
        Token.Type expected = Token.Type.CONSTANT;

        Token actual = new Token(Token.Type.NOTATOKEN, "constant");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesRangeKeyword_ReturnsTokenWithTypeRange() {
        Token.Type expected = Token.Type.RANGE;

        Token actual = new Token(Token.Type.NOTATOKEN, "range");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesBroadcastKeyword_ReturnsTokenWithTypeBroadCast() {
        Token.Type expected = Token.Type.BROADCAST;

        Token actual = new Token(Token.Type.NOTATOKEN, "broadcast");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesFilterNoiseKeyword_ReturnsTokenWithTypeFilterNoise() {
        Token.Type expected = Token.Type.FILTERNOISE;

        Token actual = new Token(Token.Type.NOTATOKEN, "filterNoise");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesGetValueKeyword_ReturnsTokenWithTypeGetValue() {
        Token.Type expected = Token.Type.GETVALUE;

        Token actual = new Token(Token.Type.NOTATOKEN, "getValue");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesCreateEventKeyword_ReturnsTokenWithTypeCreateEvent() {
        Token.Type expected = Token.Type.CREATEEVENT;

        Token actual = new Token(Token.Type.NOTATOKEN, "createEvent");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesCreatePinKeyword_ReturnsTokenWithTypeCreatePin() {
        Token.Type expected = Token.Type.CREATEPIN;

        Token actual = new Token(Token.Type.NOTATOKEN, "createPin");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesPinKeyword_ReturnsTokenWithTypePin() {
        Token.Type expected = Token.Type.PIN;

        Token actual = new Token(Token.Type.NOTATOKEN, "pin");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesFloatKeyword_ReturnsTokenWithTypeFloat() {
        Token.Type expected = Token.Type.FLOAT;

        Token actual = new Token(Token.Type.NOTATOKEN, "float");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesIntKeyword_ReturnsTokenWithTypeInt() {
        Token.Type expected = Token.Type.INT;

        Token actual = new Token(Token.Type.NOTATOKEN, "int");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesBoolKeyword_ReturnsTokenWithTypeBool() {
        Token.Type expected = Token.Type.BOOL;

        Token actual = new Token(Token.Type.NOTATOKEN, "bool");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesEventKeyword_ReturnsTokenWithTypeEvent() {
        Token.Type expected = Token.Type.EVENT;

        Token actual = new Token(Token.Type.NOTATOKEN, "event");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesTRUEKeyword_ReturnsTokenWithTypeLIT_Bool() {
        Token.Type expected = Token.Type.LIT_Bool;

        Token actual = new Token(Token.Type.NOTATOKEN, "TRUE");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesTrueKeyword_ReturnsTokenWithTypeLIT_Bool() {
        Token.Type expected = Token.Type.LIT_Bool;

        Token actual = new Token(Token.Type.NOTATOKEN, "true");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesFALSEKeyword_ReturnsTokenWithTypeLIT_Bool() {
        Token.Type expected = Token.Type.LIT_Bool;

        Token actual = new Token(Token.Type.NOTATOKEN, "FALSE");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesFalseKeyword_ReturnsTokenWithTypeLIT_Bool() {
        Token.Type expected = Token.Type.LIT_Bool;

        Token actual = new Token(Token.Type.NOTATOKEN, "false");
        actual = tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    /**
     * Tests for the isSingleCharacter method
     */
    @Test
    void isSingleCharacter_TokenIsNotSingleCharacter_ReturnsTokenWithTypeNOTATOKEN() throws IOException {
        Token.Type expected = Token.Type.NOTATOKEN;

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