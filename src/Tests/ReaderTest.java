package Tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sw417f20.ebal.Reader.Reader;

import java.io.BufferedReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReaderTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void readChar_ReadsChar_ReturnsCorrectChar() throws IOException {
        char expected = 'a';
        // Mock the inputStream
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        Mockito.when(mockReader.read()).thenReturn((int)expected);
        Reader reader = new Reader(mockReader);

        char actual = reader.readChar();

        assertEquals(expected, actual);
    }

    @Test
    void readChar_ReadsChar_ReturnsCorrectNextChar() throws IOException {
        char expected = 'b';
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        // Mock two consecutive calls to the method with different return values.
        Mockito.when(mockReader.read()).thenReturn((int)'a', (int)'b');
        Reader reader = new Reader(mockReader);

        reader.readChar();
        char actual = reader.nextChar;

        assertEquals(expected, actual);
    }

    @Test
    void readChar_ReadsChar_IncrementsCurrentOffset() throws IOException {
        int expected = 1;
        // Mock the inputStream
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        Mockito.when(mockReader.read()).thenReturn((int)'a');
        Reader reader = new Reader(mockReader);

        reader.readChar();
        int actual = reader.currentOffset;

        assertEquals(expected, actual);
    }

    @Test
    void readChar_ReadsNewLine_IncrementsCurrentLine() throws IOException {
        int expected = 2;
        // Mock the inputStream
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        Mockito.when(mockReader.read()).thenReturn((int)'\n');
        Reader reader = new Reader(mockReader);

        reader.readChar();
        int actual = reader.currentLine;

        assertEquals(expected, actual);
    }

    @Test
    void readChar_ReadsNewLine_ResetsCurrentOffset() throws IOException {
        int expected = 0;
        // Mock the inputStream
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        Mockito.when(mockReader.read()).thenReturn((int)'\n');
        Reader reader = new Reader(mockReader);
        reader.currentOffset = 5;

        reader.readChar();
        int actual = reader.currentOffset;

        assertEquals(expected, actual);
    }

    @Test
    void findWord_ReadsWord_ReturnsCorrectWord() throws IOException {
        String expected = "word";
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        // Mock to return the characters of a word followed by whitespace
        Mockito.when(mockReader.read()).thenReturn((int)'w', (int)'o', (int)'r', (int)'d', (int)'\t');
        Reader reader = new Reader(mockReader);

        reader.readChar();
        String actual = reader.findWord();

        assertEquals(expected, actual);
    }

    @Test
    void findWord_ReadsWord_IgnoresTrailingWhitespace() throws IOException {
        String expected = "word";
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        // Mock to return the characters of a word followed by whitespace
        Mockito.when(mockReader.read()).thenReturn((int)'w', (int)'o', (int)'r', (int)'d', (int)'\t', (int)'\n');
        Reader reader = new Reader(mockReader);

        reader.readChar();
        String actual = reader.findWord();

        assertEquals(expected, actual);
    }

    @Test
    void findWord_ReadsWord_ReturnsWordBeforeWhitespace() throws IOException {
        String expected = "word";
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        // Mock to return the characters of a word followed by whitespace
        Mockito.when(mockReader.read()).thenReturn((int)'w', (int)'o', (int)'r', (int)'d',
                                                   (int)'\t',
                                                   (int)'n', (int)'e', (int)'w');
        Reader reader = new Reader(mockReader);

        reader.readChar();
        String actual = reader.findWord();

        assertEquals(expected, actual);
    }

    @Test
    void findWord_ReadsWord_ReturnsWordBeforeParen() throws IOException {
        String expected = "word";
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        // Mock to return the characters of a word followed by whitespace
        Mockito.when(mockReader.read()).thenReturn((int)'w', (int)'o', (int)'r', (int)'d', (int)'(', (int)'\t');
        Reader reader = new Reader(mockReader);

        reader.readChar();
        String actual = reader.findWord();

        assertEquals(expected, actual);
    }
}