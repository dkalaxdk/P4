package Tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sw417f20.ebal.SyntaxAnalysis.Reader;
import sw417f20.ebal.SyntaxAnalysis.Token;
import sw417f20.ebal.SyntaxAnalysis.Tokenizer;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenizerTest {
    private Reader mockReader;
    private Tokenizer tokenizer;
    @BeforeEach
    void setUp() {
        // Mock the reader class needed as parameter for tokenizer
        //mockReader = Mockito.mock(Reader.class);
        tokenizer = new Tokenizer();
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
        tokenizer.findNumberTokenType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findNumberTokenType_TokenContentIsEmpty_ReturnsTokenWithTypeNOTATOKEN() {
        Token.Type expected = Token.Type.NOTATOKEN;

        Token actual = new Token(Token.Type.NOTATOKEN, "");
        tokenizer.findNumberTokenType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findNumberTokenType_TokenContentIsAFloat_ReturnsTokenWithTypeLIT_Float() {
        Token.Type expected = Token.Type.LIT_Float;

        Token actual = new Token(Token.Type.NOTATOKEN, "1.2");
        tokenizer.findNumberTokenType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findNumberTokenType_TokenContentIsAnInt_ReturnsTokenWithTypeLIT_Int() {
        Token.Type expected = Token.Type.LIT_Int;

        Token actual = new Token(Token.Type.NOTATOKEN, "123");
        tokenizer.findNumberTokenType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findNumberTokenType_TokenContentIsZero_ReturnsTokenWithTypeLIT_Int() {
        Token.Type expected = Token.Type.LIT_Int;

        Token actual = new Token(Token.Type.NOTATOKEN, "0");
        tokenizer.findNumberTokenType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findNumberTokenType_TokenContentIsNumberFollowedByLetters_ReturnsTokenWithTypeERROR() {
        Token.Type expected = Token.Type.ERROR;

        Token actual = new Token(Token.Type.NOTATOKEN, "1A");
        tokenizer.findNumberTokenType(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void findNumberTokenType_TokenContentIsDotFollowedByNumbers_ReturnsTokenWithTypeERROR() {
        Token.Type expected = Token.Type.ERROR;

        Token actual = new Token(Token.Type.NOTATOKEN, ".3");
        tokenizer.findNumberTokenType(actual);

        assertEquals(expected, actual.type);
    }

    /**
     * Tests for the FindKeyword method
     */
    @Test
    void findKeyword_TokenIsNotKeyword_ReturnsTokenWithTypeNOTATOKEN() {
        Token.Type expected = Token.Type.NOTATOKEN;

        Token actual = new Token(Token.Type.NOTATOKEN, "NotAKeyWord");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesMASTERKeyword_ReturnsTokenWithTypeMASTER() {
        Token.Type expected = Token.Type.MASTER;

        Token actual = new Token(Token.Type.NOTATOKEN, "MASTER");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesSLAVEKeyword_ReturnsTokenWithTypeSLAVE() {
        Token.Type expected = Token.Type.SLAVE;

        Token actual = new Token(Token.Type.NOTATOKEN, "SLAVE");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesENDKeyword_ReturnsTokenWithTypeEND() {
        Token.Type expected = Token.Type.END;

        Token actual = new Token(Token.Type.NOTATOKEN, "END");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesBEGINKeyword_ReturnsTokenWithTypeBEGIN() {
        Token.Type expected = Token.Type.BEGIN;

        Token actual = new Token(Token.Type.NOTATOKEN, "BEGIN");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesDigitalKeyword_ReturnsTokenWithTypeDigital() {
        Token.Type expected = Token.Type.DIGITAL;

        Token actual = new Token(Token.Type.NOTATOKEN, "digital");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesInputKeyword_ReturnsTokenWithTypeInput() {
        Token.Type expected = Token.Type.INPUT;

        Token actual = new Token(Token.Type.NOTATOKEN, "input");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesListenerKeyword_ReturnsTokenWithTypeListener() {
        Token.Type expected = Token.Type.LISTENER;

        Token actual = new Token(Token.Type.NOTATOKEN, "Listener");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesOutputKeyword_ReturnsTokenWithTypeOutput() {
        Token.Type expected = Token.Type.OUTPUT;

        Token actual = new Token(Token.Type.NOTATOKEN, "output");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesWriteKeyword_ReturnsTokenWithTypeWrite() {
        Token.Type expected = Token.Type.WRITE;

        Token actual = new Token(Token.Type.NOTATOKEN, "write");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesEventHandlerKeyword_ReturnsTokenWithTypeEventHandler() {
        Token.Type expected = Token.Type.EVENTHANDLER;

        Token actual = new Token(Token.Type.NOTATOKEN, "EventHandler");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesInitiateKeyword_ReturnsTokenWithTypeInitiate() {
        Token.Type expected = Token.Type.INITIATE;

        Token actual = new Token(Token.Type.NOTATOKEN, "Initiate");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesIfKeyword_ReturnsTokenWithTypeIf() {
        Token.Type expected = Token.Type.IF;

        Token actual = new Token(Token.Type.NOTATOKEN, "if");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesElseKeyword_ReturnsTokenWithTypeElse() {
        Token.Type expected = Token.Type.ELSE;

        Token actual = new Token(Token.Type.NOTATOKEN, "else");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesAnalogKeyword_ReturnsTokenWithTypeAnalog() {
        Token.Type expected = Token.Type.ANALOG;

        Token actual = new Token(Token.Type.NOTATOKEN, "analog");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesPwmKeyword_ReturnsTokenWithTypePwm() {
        Token.Type expected = Token.Type.PWM;

        Token actual = new Token(Token.Type.NOTATOKEN, "pwm");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesFlipKeyword_ReturnsTokenWithTypeFlip() {
        Token.Type expected = Token.Type.FLIP;

        Token actual = new Token(Token.Type.NOTATOKEN, "flip");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesConstantKeyword_ReturnsTokenWithTypeConstant() {
        Token.Type expected = Token.Type.CONSTANT;

        Token actual = new Token(Token.Type.NOTATOKEN, "constant");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesRangeKeyword_ReturnsTokenWithTypeRange() {
        Token.Type expected = Token.Type.RANGE;

        Token actual = new Token(Token.Type.NOTATOKEN, "range");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesBroadcastKeyword_ReturnsTokenWithTypeBroadCast() {
        Token.Type expected = Token.Type.BROADCAST;

        Token actual = new Token(Token.Type.NOTATOKEN, "broadcast");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesFilterNoiseKeyword_ReturnsTokenWithTypeFilterNoise() {
        Token.Type expected = Token.Type.FILTERNOISE;

        Token actual = new Token(Token.Type.NOTATOKEN, "filterNoise");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesGetValueKeyword_ReturnsTokenWithTypeGetValue() {
        Token.Type expected = Token.Type.GETVALUE;

        Token actual = new Token(Token.Type.NOTATOKEN, "getValue");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesCreateEventKeyword_ReturnsTokenWithTypeCreateEvent() {
        Token.Type expected = Token.Type.CREATEEVENT;

        Token actual = new Token(Token.Type.NOTATOKEN, "createEvent");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesCreatePinKeyword_ReturnsTokenWithTypeCreatePin() {
        Token.Type expected = Token.Type.CREATEPIN;

        Token actual = new Token(Token.Type.NOTATOKEN, "createPin");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesPinKeyword_ReturnsTokenWithTypePin() {
        Token.Type expected = Token.Type.PIN;

        Token actual = new Token(Token.Type.NOTATOKEN, "pin");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesFloatKeyword_ReturnsTokenWithTypeFloat() {
        Token.Type expected = Token.Type.FLOAT;

        Token actual = new Token(Token.Type.NOTATOKEN, "float");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesIntKeyword_ReturnsTokenWithTypeInt() {
        Token.Type expected = Token.Type.INT;

        Token actual = new Token(Token.Type.NOTATOKEN, "int");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesBoolKeyword_ReturnsTokenWithTypeBool() {
        Token.Type expected = Token.Type.BOOL;

        Token actual = new Token(Token.Type.NOTATOKEN, "bool");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesEventKeyword_ReturnsTokenWithTypeEvent() {
        Token.Type expected = Token.Type.EVENT;

        Token actual = new Token(Token.Type.NOTATOKEN, "event");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesTRUEKeyword_ReturnsTokenWithTypeLIT_Bool() {
        Token.Type expected = Token.Type.LIT_Bool;

        Token actual = new Token(Token.Type.NOTATOKEN, "TRUE");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesTrueKeyword_ReturnsTokenWithTypeLIT_Bool() {
        Token.Type expected = Token.Type.LIT_Bool;

        Token actual = new Token(Token.Type.NOTATOKEN, "true");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesFALSEKeyword_ReturnsTokenWithTypeLIT_Bool() {
        Token.Type expected = Token.Type.LIT_Bool;

        Token actual = new Token(Token.Type.NOTATOKEN, "FALSE");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void findKeyword_TokenContentMatchesFalseKeyword_ReturnsTokenWithTypeLIT_Bool() {
        Token.Type expected = Token.Type.LIT_Bool;

        Token actual = new Token(Token.Type.NOTATOKEN, "false");
        tokenizer.findKeyword(actual);

        assertEquals(expected, actual.type);
    }

    /**
     * Tests for the isSingleCharacter method
     */
    @Test
    void isSingleCharacter_TokenIsNotSingleCharacter_ReturnsTokenWithTypeNOTATOKEN() throws IOException {
        Token.Type expected = Token.Type.NOTATOKEN;

        Token actual = new Token(Token.Type.NOTATOKEN, "NotASingleCharacter");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void isSingleCharacter_TokenContentMatchesPlus_ReturnsTokenWithTypeOP_PLUS() throws IOException {
        Token.Type expected = Token.Type.OP_PLUS;

        Token actual = new Token(Token.Type.NOTATOKEN, "+");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void isSingleCharacter_TokenContentMatchesPlusEquals_ReturnsTokenWithTypeOP_PLUS_EQUALS() throws IOException {
        Token.Type expected = Token.Type.OP_PLUS_EQUALS;

        Token actual = new Token(Token.Type.NOTATOKEN, "+=");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void isSingleCharacter_TokenContentMatchesMinus_ReturnsTokenWithTypeOP_MINUS() throws IOException {
        Token.Type expected = Token.Type.OP_MINUS;

        Token actual = new Token(Token.Type.NOTATOKEN, "-");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void isSingleCharacter_TokenContentMatchesMinusEquals_ReturnsTokenWithTypeOP_MINUS_EQUALS() throws IOException {
        Token.Type expected = Token.Type.OP_MINUS_EQUALS;

        Token actual = new Token(Token.Type.NOTATOKEN, "-=");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesTimes_ReturnsTokenWithTypeOP_TIMES() throws IOException {
        Token.Type expected = Token.Type.OP_TIMES;

        Token actual = new Token(Token.Type.NOTATOKEN, "*");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesTimesEquals_ReturnsTokenWithTypeOP_TIMES_EQUALS() throws IOException {
        Token.Type expected = Token.Type.OP_TIMES_EQUALS;

        Token actual = new Token(Token.Type.NOTATOKEN, "*=");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesDivide_ReturnsTokenWithTypeOP_DIVIDE() throws IOException {
        Token.Type expected = Token.Type.OP_DIVIDE;

        Token actual = new Token(Token.Type.NOTATOKEN, "/");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesDivideEquals_ReturnsTokenWithTypeOP_DIVIDE_EQUALS() throws IOException {
        Token.Type expected = Token.Type.OP_DIVIDE_EQUALS;

        Token actual = new Token(Token.Type.NOTATOKEN, "/=");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    /* This test should be moved to scanner
    @Test
    void isSingleCharacter_TokenContentMatchesDivideTimes_CallsReadToEndOfComment() throws IOException {
        mockReader.nextChar = '*';
        Token actual = new Token(Token.Type.NOTATOKEN, "/");
        tokenizer.IsSingleCharacter(actual);

        Mockito.verify(mockReader).readToEndOfComment();
    }
     */
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

        Token actual = new Token(Token.Type.NOTATOKEN, "=");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesEqualsEquals_ReturnsTokenWithTypeLOP_EQUALS() throws IOException {
        Token.Type expected = Token.Type.LOP_EQUALS;

        Token actual = new Token(Token.Type.NOTATOKEN, "==");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesModulo_ReturnsTokenWithTypeOP_MODULO() throws IOException {
        Token.Type expected = Token.Type.OP_MODULO;

        Token actual = new Token(Token.Type.NOTATOKEN, "%");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesQuestionMark_ReturnsTokenWithTypeOP_QUESTION() throws IOException {
        Token.Type expected = Token.Type.OP_QUESTION;

        Token actual = new Token(Token.Type.NOTATOKEN, "?");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesExclamationMark_ReturnsTokenWithTypeLOP_NOT() throws IOException {
        Token.Type expected = Token.Type.OP_NOT;

        Token actual = new Token(Token.Type.NOTATOKEN, "!");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesExclamationMarkEquals_ReturnsTokenWithTypeLOP_NOTEQUAL() throws IOException {
        Token.Type expected = Token.Type.LOP_NOTEQUAL;

        Token actual = new Token(Token.Type.NOTATOKEN, "!=");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesLeftParen_ReturnsTokenWithTypeLPAREN() throws IOException {
        Token.Type expected = Token.Type.LPAREN;

        Token actual = new Token(Token.Type.NOTATOKEN, "(");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesRightParen_ReturnsTokenWithTypeRPAREN() throws IOException {
        Token.Type expected = Token.Type.RPAREN;

        Token actual = new Token(Token.Type.NOTATOKEN, ")");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesLeftSquareBracket_ReturnsTokenWithTypeLSQBRACKET() throws IOException {
        Token.Type expected = Token.Type.LSQBRACKET;

        Token actual = new Token(Token.Type.NOTATOKEN, "[");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesRightSquareBracket_ReturnsTokenWithTypeRSQBRACKET() throws IOException {
        Token.Type expected = Token.Type.RSQBRACKET;

        Token actual = new Token(Token.Type.NOTATOKEN, "]");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesLeftBracket_ReturnsTokenWithTypeLBRACKET() throws IOException {
        Token.Type expected = Token.Type.LBRACKET;

        Token actual = new Token(Token.Type.NOTATOKEN, "{");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesRightBracket_ReturnsTokenWithTypeRBRACKET() throws IOException {
        Token.Type expected = Token.Type.RBRACKET;

        Token actual = new Token(Token.Type.NOTATOKEN, "}");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesComma_ReturnsTokenWithTypeCOMMA() throws IOException {
        Token.Type expected = Token.Type.COMMA;

        Token actual = new Token(Token.Type.NOTATOKEN, ",");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesSemicolon_ReturnsTokenWithTypeSEMI() throws IOException {
        Token.Type expected = Token.Type.SEMI;

        Token actual = new Token(Token.Type.NOTATOKEN, ";");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesColon_ReturnsTokenWithTypeCOLON() throws IOException {
        Token.Type expected = Token.Type.COLON;

        Token actual = new Token(Token.Type.NOTATOKEN, ":");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesBackslash_ReturnsTokenWithTypeBACKSLASH() throws IOException {
        Token.Type expected = Token.Type.BACKSLASH;

        Token actual = new Token(Token.Type.NOTATOKEN, "\\");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesDoubleQuote_ReturnsTokenWithTypeDOUBLEQUOTE() throws IOException {
        Token.Type expected = Token.Type.DOUBLEQUOTE;

        Token actual = new Token(Token.Type.NOTATOKEN, "\"");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesSingleQuote_ReturnsTokenWithTypeSINGLEQUOTE() throws IOException {
        Token.Type expected = Token.Type.SINGLEQUOTE;

        Token actual = new Token(Token.Type.NOTATOKEN, "\'");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesEndOfFile_ReturnsTokenWithTypeEOF() throws IOException {
        Token.Type expected = Token.Type.EOF;

        Token actual = new Token(Token.Type.NOTATOKEN, "\uFFFF");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesGreaterThan_ReturnsTokenWithTypeGREATERTHAN() throws IOException {
        Token.Type expected = Token.Type.LOP_GREATERTHAN;

        Token actual = new Token(Token.Type.NOTATOKEN, ">");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesLessThan_ReturnsTokenWithTypeLESSTHAN() throws IOException {
        Token.Type expected = Token.Type.LOP_LESSTHAN;

        Token actual = new Token(Token.Type.NOTATOKEN, "<");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesGreaterOrEqual_ReturnsTokenWithTypeLOP_GREATEROREQUAL() throws IOException {
        Token.Type expected = Token.Type.LOP_GREATEROREQUAL;

        Token actual = new Token(Token.Type.NOTATOKEN, ">=");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesLessOrEqual_ReturnsTokenWithTypeLOP_LessOREQUAL() throws IOException {
        Token.Type expected = Token.Type.LOP_LESSOREQUAL;

        Token actual = new Token(Token.Type.NOTATOKEN, "<=");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void isSingleCharacter_TokenContentMatchesNumber_ReturnsTokenWithTypeNOTATOKEN() throws IOException {
        Token.Type expected = Token.Type.NOTATOKEN;

        Token actual = new Token(Token.Type.NOTATOKEN, "3");
        tokenizer.IsSingleCharacter(actual);

        assertEquals(expected, actual.type);
    }



}