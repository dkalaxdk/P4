package sw417f20.ebal.tests.SyntaxAnalysis;

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
        mockReader = Mockito.mock(Reader.class);
        tokenizer = new Tokenizer(mockReader);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void GetToken() {
    }

    /**
     * Test for the findNumberTokenType method
     */
    @Test
    void FindNumberTokenType_TokenIsNotNumber_ReturnsTokenWithTypeNOTATOKEN() {
        Token.Type expected = Token.Type.ERROR;

        Token actual = new Token(Token.Type.NOTATOKEN, "1NotANumber");
        tokenizer.AssignNumberTokenType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindNumberTokenType_TokenContentIsEmpty_ReturnsTokenWithTypeNOTATOKEN() {
        Token.Type expected = Token.Type.NOTATOKEN;

        Token actual = new Token(Token.Type.NOTATOKEN, "");
        tokenizer.AssignNumberTokenType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindNumberTokenType_TokenContentIsAFloat_ReturnsTokenWithTypeLIT_Float() {
        Token.Type expected = Token.Type.LIT_Float;

        Token actual = new Token(Token.Type.NOTATOKEN, "1.2");
        tokenizer.AssignNumberTokenType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindNumberTokenType_TokenContentIsAnInt_ReturnsTokenWithTypeLIT_Int() {
        Token.Type expected = Token.Type.LIT_Int;

        Token actual = new Token(Token.Type.NOTATOKEN, "123");
        tokenizer.AssignNumberTokenType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindNumberTokenType_TokenContentIsZero_ReturnsTokenWithTypeLIT_Int() {
        Token.Type expected = Token.Type.LIT_Int;

        Token actual = new Token(Token.Type.NOTATOKEN, "0");
        tokenizer.AssignNumberTokenType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindNumberTokenType_TokenContentIsNumberFollowedByLetters_ReturnsTokenWithTypeERROR() {
        Token.Type expected = Token.Type.ERROR;

        Token actual = new Token(Token.Type.NOTATOKEN, "1A");
        tokenizer.AssignNumberTokenType(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void FindNumberTokenType_TokenContentIsDotFollowedByNumbers_ReturnsTokenWithTypeERROR() {
        Token.Type expected = Token.Type.ERROR;

        Token actual = new Token(Token.Type.NOTATOKEN, ".3");
        tokenizer.AssignNumberTokenType(actual);

        assertEquals(expected, actual.type);
    }

    /**
     * Tests for the FindKeyword method
     */
    @Test
    void FindKeyword_TokenIsNotKeyword_ReturnsTokenWithTypeNOTATOKEN() {
        Token.Type expected = Token.Type.NOTATOKEN;

        Token actual = new Token(Token.Type.NOTATOKEN, "NotAKeyWord");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesMASTERKeyword_ReturnsTokenWithTypeMASTER() {
        Token.Type expected = Token.Type.MASTER;

        Token actual = new Token(Token.Type.NOTATOKEN, "MASTER");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesSLAVEKeyword_ReturnsTokenWithTypeSLAVE() {
        Token.Type expected = Token.Type.SLAVE;

        Token actual = new Token(Token.Type.NOTATOKEN, "SLAVE");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesENDKeyword_ReturnsTokenWithTypeEND() {
        Token.Type expected = Token.Type.END;

        Token actual = new Token(Token.Type.NOTATOKEN, "END");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesBEGINKeyword_ReturnsTokenWithTypeBEGIN() {
        Token.Type expected = Token.Type.BEGIN;

        Token actual = new Token(Token.Type.NOTATOKEN, "BEGIN");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesDigitalKeyword_ReturnsTokenWithTypeDigital() {
        Token.Type expected = Token.Type.DIGITAL;

        Token actual = new Token(Token.Type.NOTATOKEN, "digital");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesInputKeyword_ReturnsTokenWithTypeInput() {
        Token.Type expected = Token.Type.INPUT;

        Token actual = new Token(Token.Type.NOTATOKEN, "input");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesListenerKeyword_ReturnsTokenWithTypeListener() {
        Token.Type expected = Token.Type.LISTENER;

        Token actual = new Token(Token.Type.NOTATOKEN, "Listener");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesOutputKeyword_ReturnsTokenWithTypeOutput() {
        Token.Type expected = Token.Type.OUTPUT;

        Token actual = new Token(Token.Type.NOTATOKEN, "output");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesWriteKeyword_ReturnsTokenWithTypeWrite() {
        Token.Type expected = Token.Type.WRITE;

        Token actual = new Token(Token.Type.NOTATOKEN, "write");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesEventHandlerKeyword_ReturnsTokenWithTypeEventHandler() {
        Token.Type expected = Token.Type.EVENTHANDLER;

        Token actual = new Token(Token.Type.NOTATOKEN, "EventHandler");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesInitiateKeyword_ReturnsTokenWithTypeInitiate() {
        Token.Type expected = Token.Type.INITIATE;

        Token actual = new Token(Token.Type.NOTATOKEN, "Initiate");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesIfKeyword_ReturnsTokenWithTypeIf() {
        Token.Type expected = Token.Type.IF;

        Token actual = new Token(Token.Type.NOTATOKEN, "if");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesElseKeyword_ReturnsTokenWithTypeElse() {
        Token.Type expected = Token.Type.ELSE;

        Token actual = new Token(Token.Type.NOTATOKEN, "else");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesAnalogKeyword_ReturnsTokenWithTypeAnalog() {
        Token.Type expected = Token.Type.ANALOG;

        Token actual = new Token(Token.Type.NOTATOKEN, "analog");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesPwmKeyword_ReturnsTokenWithTypePwm() {
        Token.Type expected = Token.Type.PWM;

        Token actual = new Token(Token.Type.NOTATOKEN, "pwm");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesFlipKeyword_ReturnsTokenWithTypeFlip() {
        Token.Type expected = Token.Type.DEBOUNCE;

        Token actual = new Token(Token.Type.NOTATOKEN, "debounce");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesConstantKeyword_ReturnsTokenWithTypeConstant() {
        Token.Type expected = Token.Type.CONSTANT;

        Token actual = new Token(Token.Type.NOTATOKEN, "constant");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesRangeKeyword_ReturnsTokenWithTypeRange() {
        Token.Type expected = Token.Type.RANGE;

        Token actual = new Token(Token.Type.NOTATOKEN, "range");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesBroadcastKeyword_ReturnsTokenWithTypeBroadCast() {
        Token.Type expected = Token.Type.BROADCAST;

        Token actual = new Token(Token.Type.NOTATOKEN, "broadcast");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesFilterNoiseKeyword_ReturnsTokenWithTypeFilterNoise() {
        Token.Type expected = Token.Type.FILTERNOISE;

        Token actual = new Token(Token.Type.NOTATOKEN, "filterNoise");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesGetValueKeyword_ReturnsTokenWithTypeGetValue() {
        Token.Type expected = Token.Type.GETVALUE;

        Token actual = new Token(Token.Type.NOTATOKEN, "getValue");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesCreateEventKeyword_ReturnsTokenWithTypeCreateEvent() {
        Token.Type expected = Token.Type.CREATEEVENT;

        Token actual = new Token(Token.Type.NOTATOKEN, "createEvent");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesCreatePinKeyword_ReturnsTokenWithTypeCreatePin() {
        Token.Type expected = Token.Type.CREATEPIN;

        Token actual = new Token(Token.Type.NOTATOKEN, "createPin");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesPinKeyword_ReturnsTokenWithTypePin() {
        Token.Type expected = Token.Type.PIN;

        Token actual = new Token(Token.Type.NOTATOKEN, "pin");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesFloatKeyword_ReturnsTokenWithTypeFloat() {
        Token.Type expected = Token.Type.FLOAT;

        Token actual = new Token(Token.Type.NOTATOKEN, "float");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesIntKeyword_ReturnsTokenWithTypeInt() {
        Token.Type expected = Token.Type.INT;

        Token actual = new Token(Token.Type.NOTATOKEN, "int");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesBoolKeyword_ReturnsTokenWithTypeBool() {
        Token.Type expected = Token.Type.BOOL;

        Token actual = new Token(Token.Type.NOTATOKEN, "bool");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesEventKeyword_ReturnsTokenWithTypeEvent() {
        Token.Type expected = Token.Type.EVENT;

        Token actual = new Token(Token.Type.NOTATOKEN, "event");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesTRUEKeyword_ReturnsTokenWithTypeLIT_Bool() {
        Token.Type expected = Token.Type.LIT_Bool;

        Token actual = new Token(Token.Type.NOTATOKEN, "TRUE");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesTrueKeyword_ReturnsTokenWithTypeLIT_Bool() {
        Token.Type expected = Token.Type.LIT_Bool;

        Token actual = new Token(Token.Type.NOTATOKEN, "true");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesFALSEKeyword_ReturnsTokenWithTypeLIT_Bool() {
        Token.Type expected = Token.Type.LIT_Bool;

        Token actual = new Token(Token.Type.NOTATOKEN, "FALSE");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void FindKeyword_TokenContentMatchesFalseKeyword_ReturnsTokenWithTypeLIT_Bool() {
        Token.Type expected = Token.Type.LIT_Bool;

        Token actual = new Token(Token.Type.NOTATOKEN, "false");
        tokenizer.AssignKeywordType(actual);

        assertEquals(expected, actual.type);
    }

    /**
     * Tests for the isSingleCharacter method
     */
    @Test
    void IsSingleCharacter_TokenIsNotSingleCharacter_ReturnsTokenWithTypeNOTATOKEN() throws IOException {
        Token.Type expected = Token.Type.NOTATOKEN;

        Token actual = new Token(Token.Type.NOTATOKEN, "NotASingleCharacter");
        tokenizer.AssignSpecialCharacterType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void IsSingleCharacter_TokenContentMatchesPlus_ReturnsTokenWithTypeOP_PLUS() throws IOException {
        Token.Type expected = Token.Type.OP_PLUS;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "+");
        tokenizer.AssignSpecialCharacterType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void IsSingleCharacter_TokenContentMatchesPlusEquals_ReturnsTokenWithTypeOP_PLUS_EQUALS() throws IOException {
        Token.Type expected = Token.Type.OP_PLUS_EQUALS;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        mockReader.nextChar = '=';
        Token actual = new Token(Token.Type.NOTATOKEN, "+");
        tokenizer.AssignSpecialCharacterType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void IsSingleCharacter_TokenContentMatchesMinus_ReturnsTokenWithTypeOP_MINUS() throws IOException {
        Token.Type expected = Token.Type.OP_MINUS;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "-");
        tokenizer.AssignSpecialCharacterType(actual);

        assertEquals(expected, actual.type);
    }

    @Test
    void IsSingleCharacter_TokenContentMatchesMinusEquals_ReturnsTokenWithTypeOP_MINUS_EQUALS() throws IOException {
        Token.Type expected = Token.Type.OP_MINUS_EQUALS;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        mockReader.nextChar = '=';
        Token actual = new Token(Token.Type.NOTATOKEN, "-");
        tokenizer.AssignSpecialCharacterType(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void IsSingleCharacter_TokenContentMatchesTimes_ReturnsTokenWithTypeOP_TIMES() throws IOException {
        Token.Type expected = Token.Type.OP_TIMES;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "*");
        tokenizer.AssignSpecialCharacterType(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void IsSingleCharacter_TokenContentMatchesTimesEquals_ReturnsTokenWithTypeOP_TIMES_EQUALS() throws IOException {
        Token.Type expected = Token.Type.OP_TIMES_EQUALS;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        mockReader.nextChar = '=';
        Token actual = new Token(Token.Type.NOTATOKEN, "*");
        tokenizer.AssignSpecialCharacterType(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void IsSingleCharacter_TokenContentMatchesDivide_ReturnsTokenWithTypeOP_DIVIDE() throws IOException {
        Token.Type expected = Token.Type.OP_DIVIDE;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "/");
        tokenizer.AssignSpecialCharacterType(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void IsSingleCharacter_TokenContentMatchesDivideEquals_ReturnsTokenWithTypeOP_DIVIDE_EQUALS() throws IOException {
        Token.Type expected = Token.Type.OP_DIVIDE_EQUALS;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        mockReader.nextChar = '=';
        Token actual = new Token(Token.Type.NOTATOKEN, "/");
        tokenizer.AssignSpecialCharacterType(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void IsSingleCharacter_TokenContentMatchesDivideTimes_CallsReadToEndOfComment() throws IOException {
        mockReader.nextChar = '*';
        Token actual = new Token(Token.Type.NOTATOKEN, "/");
        tokenizer.AssignSpecialCharacterType(actual);

        Mockito.verify(mockReader).ReadToEndOfComment();
    }
    @Test
    void IsSingleCharacter_TokenContentMatchesDivideDivide_CallsReadChar() throws IOException {
        mockReader.nextChar = '/';
        Token actual = new Token(Token.Type.NOTATOKEN, "/");
        tokenizer.AssignSpecialCharacterType(actual);


        Mockito.verify(mockReader).ReadToEndOfLine();
    }
    @Test
    void IsSingleCharacter_TokenContentMatchesEquals_ReturnsTokenWithTypeASSIGN() throws IOException {
        Token.Type expected = Token.Type.ASSIGN;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "=");
        tokenizer.AssignSpecialCharacterType(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void IsSingleCharacter_TokenContentMatchesEqualsEquals_ReturnsTokenWithTypeLOP_EQUALS() throws IOException {
        Token.Type expected = Token.Type.LOP_EQUALS;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        mockReader.nextChar = '=';
        Token actual = new Token(Token.Type.NOTATOKEN, "=");
        tokenizer.AssignSpecialCharacterType(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void IsSingleCharacter_TokenContentMatchesModulo_ReturnsTokenWithTypeOP_MODULO() throws IOException {
        Token.Type expected = Token.Type.OP_MODULO;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "%");
        tokenizer.AssignSpecialCharacterType(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void IsSingleCharacter_TokenContentMatchesExclamationMark_ReturnsTokenWithTypeLOP_NOT() throws IOException {
        Token.Type expected = Token.Type.OP_NOT;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "!");
        tokenizer.AssignSpecialCharacterType(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void IsSingleCharacter_TokenContentMatchesExclamationMarkEquals_ReturnsTokenWithTypeLOP_NOTEQUAL() throws IOException {
        Token.Type expected = Token.Type.LOP_NOTEQUAL;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        mockReader.nextChar = '=';
        Token actual = new Token(Token.Type.NOTATOKEN, "!");
        tokenizer.AssignSpecialCharacterType(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void IsSingleCharacter_TokenContentMatchesLeftParen_ReturnsTokenWithTypeLPAREN() throws IOException {
        Token.Type expected = Token.Type.LPAREN;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "(");
        tokenizer.AssignSpecialCharacterType(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void IsSingleCharacter_TokenContentMatchesRightParen_ReturnsTokenWithTypeRPAREN() throws IOException {
        Token.Type expected = Token.Type.RPAREN;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, ")");
        tokenizer.AssignSpecialCharacterType(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void IsSingleCharacter_TokenContentMatchesLeftBracket_ReturnsTokenWithTypeLBRACKET() throws IOException {
        Token.Type expected = Token.Type.LBRACKET;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "{");
        tokenizer.AssignSpecialCharacterType(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void IsSingleCharacter_TokenContentMatchesRightBracket_ReturnsTokenWithTypeRBRACKET() throws IOException {
        Token.Type expected = Token.Type.RBRACKET;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "}");
        tokenizer.AssignSpecialCharacterType(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void IsSingleCharacter_TokenContentMatchesComma_ReturnsTokenWithTypeCOMMA() throws IOException {
        Token.Type expected = Token.Type.COMMA;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, ",");
        tokenizer.AssignSpecialCharacterType(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void IsSingleCharacter_TokenContentMatchesSemicolon_ReturnsTokenWithTypeSEMI() throws IOException {
        Token.Type expected = Token.Type.SEMI;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, ";");
        tokenizer.AssignSpecialCharacterType(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void IsSingleCharacter_TokenContentMatchesEndOfFile_ReturnsTokenWithTypeEOF() throws IOException {
        Token.Type expected = Token.Type.EOF;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "\uFFFF");
        tokenizer.AssignSpecialCharacterType(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void IsSingleCharacter_TokenContentMatchesGreaterThan_ReturnsTokenWithTypeGREATERTHAN() throws IOException {
        Token.Type expected = Token.Type.LOP_GREATERTHAN;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, ">");
        tokenizer.AssignSpecialCharacterType(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void IsSingleCharacter_TokenContentMatchesLessThan_ReturnsTokenWithTypeLESSTHAN() throws IOException {
        Token.Type expected = Token.Type.LOP_LESSTHAN;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "<");
        tokenizer.AssignSpecialCharacterType(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void IsSingleCharacter_TokenContentMatchesGreaterOrEqual_ReturnsTokenWithTypeLOP_GREATEROREQUAL() throws IOException {
        Token.Type expected = Token.Type.LOP_GREATEROREQUAL;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        mockReader.nextChar = '=';
        Token actual = new Token(Token.Type.NOTATOKEN, ">");
        tokenizer.AssignSpecialCharacterType(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void IsSingleCharacter_TokenContentMatchesLessOrEqual_ReturnsTokenWithTypeLOP_LessOREQUAL() throws IOException {
        Token.Type expected = Token.Type.LOP_LESSOREQUAL;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        mockReader.nextChar = '=';
        Token actual = new Token(Token.Type.NOTATOKEN, "<");
        tokenizer.AssignSpecialCharacterType(actual);

        assertEquals(expected, actual.type);
    }
    @Test
    void IsSingleCharacter_TokenContentMatchesNumber_ReturnsTokenWithTypeNOTATOKEN() throws IOException {
        Token.Type expected = Token.Type.NOTATOKEN;
        Tokenizer tokenizer = new Tokenizer(mockReader);

        Token actual = new Token(Token.Type.NOTATOKEN, "3");
        tokenizer.AssignSpecialCharacterType(actual);

        assertEquals(expected, actual.type);
    }
}