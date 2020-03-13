package sw417f20.ebal.Reader;

import java.io.BufferedReader;
import java.io.IOException;

public class Reader {
    public Reader(BufferedReader reader) {
        inputStream = reader;
    }

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
        char c;
        int res = inputStream.read();

        c = (char) res;

        currentOffset++;
        if (c == '\n') {
            currentLine++;
            currentOffset = 0;
        }

        if (firstRead) {
            currentChar = c;
            nextChar = (char) inputStream.read();
            firstRead = false;
        } else {
            currentChar = nextChar;
            nextChar = c;
        }
        //currentChar = nextChar;
        //nextChar = c;
        // Should return c if it is the first character read
        return currentChar;
    }

    public String findWord() throws IOException {
        StringBuilder output = new StringBuilder();
        char currentChar = this.currentChar;
        while (whitespace.indexOf(currentChar) == -1) {
            // Fixing potential infinite loop \uFFFF is EOF.
            if (output.indexOf("\uFFFF") != -1) break;

            if ((output.length() >= 1) && String.valueOf(currentChar).matches("[A-Za-z_0-9]")) {
                output.append(currentChar);
            } else if (String.valueOf(currentChar).matches("[A-Za-z_]")) {
                output.append(currentChar);
            } else break;


            if (String.valueOf(nextChar).matches(("[A-Za-z_0-9]"))) {
                currentChar = readChar();
            } else break;
        }

        return output.toString();
    }

}
