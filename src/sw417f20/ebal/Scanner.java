package sw417f20.ebal;

import java.io.*;

public class Scanner {
    public Scanner(String inputFilePath) {
        fileInput = inputFilePath;
    }
    private String fileInput;
    Token currentToken;
    Token nextToken;


    String whitespace = "\\t\\n\\s";
    /*
    FileInputStream fileStream;
    {
        try {
            fileStream = new FileInputStream(fileInput);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    */
    private FileReader fileReader;

    {
        try {
            fileReader = new FileReader(fileInput);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private BufferedReader inputStream = new BufferedReader(fileReader);
    private char currentChar = 0;
    private char nextChar = 0;
    private int currentLine = 1;
    private int currentOffset = 0;

    // Reads and returns the next char in the input
    // Keeps track of line and offset
    private char readChar() throws IOException {
        // This cast might cause problems
        int res = inputStream.read();
        if(res == -1) {
            //TODO: Handle end of file
            return (char)-1;
        }
        char c = (char)res;
        currentOffset++;
        if(c == '\n') {
            currentLine++;
        }
        return c;
    }

    //Only for public use
    public Token Peek() {
        return nextToken;
    }
    //Only for public use
    public char Advance() {
        // Check whether the switch may find another case, ie isDigit, isText or the likes.
        // Depending on the cases, it should be redirected to something in the likes of "Find keyword" or "Find literal"
        nextToken = currentToken;
        currentToken = getToken();
        // Set currentChar to nextChar
        return ' ';
    }

    private Token getToken() {
        String buffer;
        char currentChar;
        while (IsSingleCharacter((char)fileStream.read())){

        }

    }

    public char findKeyword() {
        char output = ' ';
        Token token = new Token(Token.Type.EOF, "");
        switch (input) {
            case "begin":
                token.type = Token.Type.KEYWORD;
                break;
            case "master":
                token.type = Token.Type.KEYWORD;
                break;
            case "slave":
                break;


            default:
                output = ' ';
        }
        return output;
    }

    public boolean isWhiteSpace() {
        return whitespace.indexOf(nextToken) != -1;
    }

    public char IsDigit() {
        return ' ';
    }

    public void IsChar() {

    }


    public Token IsSingleCharacter(char input) {
        // Needs to be able to peek at next character
        // Consider loading the whole goddamn file into one loooong array.
        // If you think you got the RAM for it. (i'm tired, don't judge me)
        switch (input) {
            case '+':
                // if next char is '=' its another token
                break;
            case '-':
                break;
            case '*':
                break;
            case '/':   // Has some special cases when followed by other symbols
                break;
            case '=':
                break;
            case '?':
                break;
            case '!':
                break;
            case '(':
                break;
            case ')':
                break;
            case '[':
                break;
            case ']':
                break;
            case '{':
                break;
            case '}':
                break;
            case ',':
                break;
            case '.':
                break;
            case ';':
                break;
            case ':':
                break;
            case '"':
                break;
            case '\'':
                break;

            default:
                return false;
                break;
        }


        return true;
    }

    public void IsIdentifier() {

    }

    public Token IdentifyToken() {

    }
}
