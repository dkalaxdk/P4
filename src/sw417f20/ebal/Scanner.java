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
    private char nextChar = readChar();
    private int currentLine = 1;
    private int currentOffset = 0;

    // Reads and returns the next char in the input
    // Keeps track of line and offset
    private char readChar()  {
        // This cast might cause problems
        int res = 0;

        try {
            res = inputStream.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(res == -1) {
            //TODO: Handle end of file
            return (char)-1;
        }
        char c = (char)res;
        currentOffset++;
        if(c == '\n') {
            currentLine++;
        }
        currentChar = nextChar;
        nextChar = c;
        return currentChar;
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

    private Token getToken() throws IOException {
        String buffer;
        Token token = new Token(Token.Type.NOTATOKEN, "");
        while (token.type == Token.Type.NOTATOKEN) {
            IsSingleCharacter(token);
            findKeyword(token);
        }

    }

    public char findKeyword(Token token) throws IOException {
        char output = ' ';
        char input = readChar();
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
        return Character.isWhitespace(currentChar);
        //return whitespace.indexOf(nextToken) != -1;
    }

    public char IsDigit() {
        return ' ';
    }

    public void IsChar() {

    }


    public Token IsSingleCharacter(Token token) throws IOException {
        // Needs to be able to peek at next character
        // Consider loading the whole goddamn file into one loooong array.
        // If you think you got the RAM for it. (i'm tired, don't judge me)
        char input = readChar();
        switch (input) {
            case '+':
                if(nextChar == '=') {
                    token.type = Token.Type.OP_PLUS_EQUALS;
                }
                else {
                    token.type = Token.Type.OP_PLUS;
                }
                return token;
            case '-':
                if(nextChar == '=') {
                    token.type = Token.Type.OP_MINUS_EQUALS;
                }
                else {
                    token.type = Token.Type.OP_MINUS;
                }
                return token;
            case '*':
                if(nextChar == '=') {
                    token.type = Token.Type.OP_TIMES_EQUALS;
                }
                else {
                    token.type = Token.Type.OP_TIMES;
                }
                return token;
            case '/':   // Has some special cases when followed by other symbols
                if(nextChar == '=') {
                    token.type = Token.Type.OP_DIVIDE_EQUALS;
                }
                else if(nextChar == '*') {
                    //TODO: Handle start of comment
                }
                else {
                    token.type = Token.Type.OP_DIVIDE;
                }
                return token;

            case '=':
                token.type = Token.Type.OP_EQUALS;
                return token;

            case '?':
                token.type = Token.Type.OP_QUESTION;
                return token;

            case '!':
                token.type = Token.Type.OP_NOT;
                return token;

            case '(':
                token.type = Token.Type.LPAREN;
                return token;

            case ')':
                token.type = Token.Type.RPAREN;
                return token;

            case '[':
                token.type = Token.Type.LSQBRACKET;
                return token;

            case ']':
                token.type = Token.Type.RSQBRACKET;
                return token;

            case '{':
                token.type = Token.Type.LBRACKET;
                return token;

            case '}':
                token.type = Token.Type.RBRACKET;
                return token;

            case ',':
                token.type = Token.Type.COMMA;
                return token;

            case '.':
                token.type = Token.Type.DOT;
                return token;

            case ';':
                token.type = Token.Type.SEMI;
                return token;

            case ':':
                token.type = Token.Type.COLON;
                return token;

            case '\\':
                token.type = Token.Type.BACKSLASH;
                return token;

            case '"':
                token.type = Token.Type.DOUBLEQUOTE;
                return token;

            case '\'':
                token.type = Token.Type.SINGLEQUOTE;
                return token;


            default:
                return token;
        }

    }

    public void IsIdentifier() {

    }

    private String findWord() throws IOException {
        String output = "";

        while (whitespace.indexOf(readChar())!= -1) {

        }
        return output;
    }
}
