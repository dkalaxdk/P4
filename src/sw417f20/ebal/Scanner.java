package sw417f20.ebal;

import sw417f20.ebal.reader.Reader;

import java.io.*;

public class Scanner {
    String filePath = "";
    public Scanner(String fileInput) {
        filePath = fileInput;
    }
    Token currentToken;
    Token nextToken;

    private Reader reader = new Reader(filePath);

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


    //Only for public use
    public Token Peek() {
        return nextToken;
    }

    //Only for public use
    public char Advance() throws IOException {
        // Check whether the switch may find another case, ie isDigit, isText or the likes.
        // Depending on the cases, it should be redirected to something in the likes of "Find keyword" or "Find literal"
        nextToken = currentToken;
        currentToken = getToken();
        // Set currentChar to nextChar
        return ' ';
    }

    private Token getToken() throws IOException {
        Token token = new Token(Token.Type.NOTATOKEN, "");
        while (token.type == Token.Type.NOTATOKEN) {
            IsSingleCharacter(token);
            token.content = reader.findWord();
            findKeyword(token);
        }
        return token;
    }

    public char findKeyword(Token token) {
        char output = ' ';
        switch (token.content) {
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

    public char IsDigit() {
        return ' ';
    }

    public void IsChar() {

    }


    public Token IsSingleCharacter(Token token) throws IOException {
        // Needs to be able to peek at next character
        // Consider loading the whole goddamn file into one loooong array.
        // If you think you got the RAM for it. (i'm tired, don't judge me)
        char input = reader.readChar();
        switch (input) {
            case '+':
                if(reader.nextChar == '=') {
                    token.type = Token.Type.OP_PLUS_EQUALS;
                }
                else {
                    token.type = Token.Type.OP_PLUS;
                }
                return token;
            case '-':
                if(reader.nextChar == '=') {
                    token.type = Token.Type.OP_MINUS_EQUALS;
                }
                else {
                    token.type = Token.Type.OP_MINUS;
                }
                return token;
            case '*':
                if(reader.nextChar == '=') {
                    token.type = Token.Type.OP_TIMES_EQUALS;
                }
                else {
                    token.type = Token.Type.OP_TIMES;
                }
                return token;
            case '/':   // Has some special cases when followed by other symbols
                if(reader.nextChar == '=') {
                    token.type = Token.Type.OP_DIVIDE_EQUALS;
                }
                else if(reader.nextChar == '*') {
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


}
