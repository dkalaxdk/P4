package sw417f20.ebal.SyntaxAnalysis;

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
        char c = (char) inputStream.read();
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
        return currentChar;
    }

    public String findWord() throws IOException {
        StringBuilder output = new StringBuilder();
        char currentChar = this.currentChar;
        while (whitespace.indexOf(currentChar) == -1) {
            if (output.length() >= 1 && String.valueOf(currentChar).matches("[A-Za-z_0-9]")) {
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

    public String findNumber() throws IOException {
        StringBuilder output = new StringBuilder();
        while (whitespace.indexOf(currentChar) == -1) {
            // Adds the number, or a dot, if the output string does not contain a dot and the length of the string is longer than 1
            // It needs to be longer than one, as we cant start the float with a dot.
            if (output.length() >= 1) {
                if (String.valueOf(currentChar).matches("[0-9.]") && output.indexOf(".") == -1) {
                    output.append(currentChar);
                } else if (String.valueOf(currentChar).matches("[0-9]") && output.indexOf(".") != -1) {
                    output.append(currentChar);
                }
                // This line includes any characters that might be included, so we can catch faulty identifiers, IE: 1Identifier.
                else if (output.toString().matches("[0-9A-Za-z.]+")) {
                    output.append(currentChar);
                }
            // If the output string is not longer than one, it must be 0-9.
            } else if (String.valueOf(currentChar).matches("[0-9.]")) {
                output.append(currentChar);
            } else break;
            // If the next char is not a white space, continue reading.
            if (String.valueOf(nextChar).matches("[0-9A-Za-z.]")) {
                currentChar = readChar();
            } else break;
        }
        return output.toString();
    }

    public void readToEndOfComment() throws IOException {
//        System.out.println("Comment found");
        while (!(currentChar == '*' && nextChar == '/')) {
            readChar();
        }
        readChar();
    }
    public void readToEndOfLine() throws IOException {
//        System.out.println("Comment found");
        while (!(currentChar == '\n')) {
            readChar();
        }
        readChar();
    }
}
