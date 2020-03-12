package sw417f20.ebal;

import java.io.*;

import sw417f20.ebal.Reader.Reader;

public class Scanner {
    public Reader reader;
    Token currentToken;
    Token nextToken;

    private BufferedReader bufferedReader;

    public Scanner(String fileInput) {
        currentToken = new Token(Token.Type.NOTATOKEN, "");
        nextToken = new Token(Token.Type.NOTATOKEN, "");

        {
            try {
                FileReader fileReader = new FileReader(fileInput);
                bufferedReader = new BufferedReader(fileReader);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        this.reader = new Reader(bufferedReader);
    }


    //Only for public use
    public Token Peek() {
        return nextToken;
    }

    //Only for public use
    public void Advance() throws IOException {
        // Check whether the switch may find another case, ie isDigit, isText or the likes.
        // Depending on the cases, it should be redirected to something in the likes of "Find keyword" or "Find literal"
        nextToken = currentToken;
        currentToken = getToken();
    }

    private Token getToken() throws IOException {
        Token token = new Token(Token.Type.NOTATOKEN, "");
        while (token.type == Token.Type.NOTATOKEN) {
            token = IsSingleCharacter(token);
            if (token.type == Token.Type.NOTATOKEN) {
                token.content = reader.findWord();
                token = findKeyword(token);
            }
        }
        return token;
    }

    public Token findKeyword(Token token) {
        switch (token.content) {
            case "MASTER":
            case "SLAVE":
            case "END":
            case "begin":
            case "digital":
            case "input":
            case "EventCreator":
            case "output":
            case "write":
            case "EventHandler":
            case "Initiate":
            case "if":
                token.type = Token.Type.KEYWORD;
                break;
            default:
                token.type = Token.Type.NOTATOKEN;
        }
        return token;
    }

    public Token IsSingleCharacter(Token token) throws IOException {
        // Needs to be able to peek at next character
        // Consider loading the whole goddamn file into one loooong array.
        // If you think you got the RAM for it. (i'm tired, don't judge me)
        char input = reader.readChar();
        token.content += input;
        switch (input) {
            case '+':
                if (reader.nextChar == '=') {
                    token.type = Token.Type.OP_PLUS_EQUALS;
                } else {
                    token.type = Token.Type.OP_PLUS;
                }
                return token;
            case '-':
                if (reader.nextChar == '=') {
                    token.content += reader.nextChar;
                    token.type = Token.Type.OP_MINUS_EQUALS;
                } else {
                    token.type = Token.Type.OP_MINUS;
                }
                return token;
            case '*':
                if (reader.nextChar == '=') {
                    token.type = Token.Type.OP_TIMES_EQUALS;
                } else {
                    token.type = Token.Type.OP_TIMES;
                }
                return token;
            case '/':   // Has some special cases when followed by other symbols
                if (reader.nextChar == '=') {
                    token.type = Token.Type.OP_DIVIDE_EQUALS;
                } else if (reader.nextChar == '*') {
                    //TODO: Handle start of comment
                } else {
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
            case '\uFFFF':
                token.type = Token.Type.EOF;
                return token;

            default:
                return token;
        }

    }

    public void IsIdentifier() {

    }


}
