package sw417f20.ebal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Scanner {
    public Scanner(String inputFilePath) {
        fileInput = inputFilePath;
    }
    private String fileInput;
    Token currentToken;
    Token nextToken;


    String whitespace = "\\t\\n\\s";
    FileInputStream fileStream;
    {
        try {
            fileStream = new FileInputStream(fileInput);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
        switch (input) {
            case '+':
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
