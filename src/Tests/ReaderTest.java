package Tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sw417f20.ebal.Reader.Reader;

import java.io.BufferedReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

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
    void findWord() {
    }
}