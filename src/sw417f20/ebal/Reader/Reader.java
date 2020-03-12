package sw417f20.ebal.Reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Reader {
    private String fileInput = "";

    public Reader(BufferedReader reader) {
        inputStream = reader;
        //fileInput = inputFilePath;
    }

    private FileReader fileReader;
    private BufferedReader inputStream;

    public Reader(String inputFilePath) {
        System.out.println("Testing");
        this.fileInput = inputFilePath;
        {
            try {
                fileReader = new FileReader(fileInput);
            } catch (FileNotFoundException e) {
                System.out.println("FilePath: "+fileInput);
                e.printStackTrace();
            }
        }
        assert fileReader != null;
        inputStream = new BufferedReader(fileReader);
    }

    String whitespace = "\\t\\n\\s";

    public char currentChar = 0;
    public char nextChar = 0;
    public int currentLine = 1;
    public int currentOffset = 0;


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
