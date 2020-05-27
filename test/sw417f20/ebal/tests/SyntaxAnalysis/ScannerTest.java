package sw417f20.ebal.tests.SyntaxAnalysis;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sw417f20.ebal.SyntaxAnalysis.Reader;
import sw417f20.ebal.SyntaxAnalysis.Scanner;
import sw417f20.ebal.SyntaxAnalysis.Token;

import java.io.BufferedReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ScannerTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void Peek_ReadsToken_Finds_MASTER_Token() throws IOException {
        Token expected = new Token(Token.Type.MASTER, "MASTER");
        // The buffered reader needed to be mocked as well, to ensure the reader did not get a nullStream.
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);

        Mockito.when(mockReader.read()).thenReturn((int) 'M', (int) 'A', (int) 'S', (int) 'T', (int) 'E', (int) 'R', (int) ' ');
        Reader reader = new Reader(mockReader);
        Scanner scanner = new Scanner(reader);
        Token actual = scanner.Peek();

        assertEquals(expected, actual);
    }

    @Test
    void Advance_findsNewToken_Returns_tokensNotEqual() throws IOException {
        // The buffered reader needed to be mocked as well, to ensure the reader did not get a nullStream.
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);

        Mockito.when(mockReader.read()).thenReturn((int) 'M', (int) 'A', (int) 'S', (int) 'T', (int) 'E', (int) 'R', (int) ' ', (int) 'i'
                , (int) 'n', (int) 't', (int) ' ');
        Reader reader = new Reader(mockReader);
        Scanner scanner = new Scanner(reader);
        Token first = scanner.Peek();
        scanner.Advance();
        Token last = scanner.Peek();

        assertNotEquals(first, last);
    }
}
