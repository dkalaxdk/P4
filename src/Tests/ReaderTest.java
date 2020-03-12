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
    void readChar() throws IOException {
        char expected = 'a';
        // Mock the inputStream
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        Mockito.when(mockReader.read()).thenReturn((int)expected);

        Reader reader = new Reader(mockReader);

        char actual = reader.readChar();

        assertEquals(expected, actual);
    }

    @Test
    void findWord() {
    }
}