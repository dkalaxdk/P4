package sw417f20.ebal.Reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Reader {
    public String fileInput = "";


    public Reader(String inputFilePath) {
        this.fileInput = inputFilePath;
    }

    String whitespace = "\\t\\n\\s";
    private char currentChar = 0;
    public char nextChar = 0;
    private int currentLine = 1;
    private int currentOffset = 0;
    private FileReader fileReader;

    {
        try {
            assert fileInput != null;
            fileReader = new FileReader(fileInput);
        } catch (FileNotFoundException e) {
            System.out.println("FilePath: "+fileInput);
            e.printStackTrace();
        }
    }

    private BufferedReader inputStream = new BufferedReader(fileReader);


    // Reads and returns the next char in the input
    // Keeps track of line and offset
    public char readChar() throws IOException {
        // This cast might cause problems
        int res = inputStream.read();
        if (res == -1) {
            //TODO: Handle end of file
            return (char) -1;
        }
        char c = (char) res;
        currentOffset++;
        if (c == '\n') {
            currentLine++;
        }
        return c;
    }

    public String findWord() throws IOException {
        StringBuilder output = new StringBuilder();
        char currentChar = readChar();
        while (whitespace.indexOf(currentChar) != -1) {
            output.append(currentChar);
            currentChar = readChar();
        }
        return output.toString();
    }

}
