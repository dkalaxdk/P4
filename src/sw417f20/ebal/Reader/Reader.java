package sw417f20.ebal.Reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Reader {
    public Reader(BufferedReader reader) {
        inputStream = reader;
    }

    private FileReader fileReader;
    private BufferedReader inputStream;


    String whitespace = "\t\n\r ";

    public char currentChar = 0;
    public char nextChar = 0;
    public int currentLine = 1;
    public int currentOffset = 0;
    boolean firstRead = true;


    // Reads and returns the next char in the input
    // Keeps track of line and offset
    public char readChar() throws IOException {
        if (!firstRead) {
            // This cast might cause problems
            currentChar = nextChar;
            int res = inputStream.read();

            char c = (char) res;
            currentOffset++;
            if (c == '\n') {
                currentLine++;
            }
            nextChar = c;
        } else {
            int res = inputStream.read();

            char c = (char) res;
            currentOffset++;
            if (c == '\n') {
                currentLine++;
            }
            currentChar = c;
            firstRead = false;
            readChar();
        }
        currentChar = nextChar;
        nextChar = c;
        // Should return c if it is the first character read
        return currentChar == 0 ? c : currentChar;
    }

    public String findWord() throws IOException {
        StringBuilder output = new StringBuilder();
        char currentChar = this.currentChar;
        while (whitespace.indexOf(currentChar) == -1) {
            if (output.indexOf("\uFFFF") != -1) {
                break;
            }
            output.append(currentChar);
            currentChar = readChar();
        }
        return output.toString();
    }

}
