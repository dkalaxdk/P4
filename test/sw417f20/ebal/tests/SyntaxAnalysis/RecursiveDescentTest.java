package sw417f20.ebal.tests.SyntaxAnalysis;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sw417f20.ebal.Exceptions.SyntaxException;
import sw417f20.ebal.SyntaxAnalysis.*;

import static org.junit.jupiter.api.Assertions.*;

class RecursiveDescentTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void Parse_InputScannerIsNull_ReturnNull() {
        // Arrange
        Parser parser = new Parser(null, "");

        try {
            // Act
            Node ast = parser.Parse(false);

            // Assert
            assertNull(ast);
        }
        catch (SyntaxException e) {

        }
    }

//    @Test
//    void Peek_ReturnsNextToken() {
//        Scanner mockScanner = Mockito.mock(Scanner.class);
//
//        Token token1 = new Token(Token.Type.BEGIN, "BEGIN");
//        Token token2 = new Token(Token.Type.END, "END");
//
//        Mockito.when(mockScanner.Peek()).thenReturn(token1, token2);
//
//        Parser parser = new Parser();
//
//        parser.Parse(mockScanner);
//
//    }
//
//    @Test
//    void Peek_DoesNotAdvanceScanner() {
//
//    }

}