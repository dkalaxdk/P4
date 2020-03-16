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

        Init();
    }

    public void Init() {
        try {
            nextToken = getToken();
        }
        catch (IOException e) {
            System.err.println(e);
        }
    }

    //Only for public use
    public Token Peek() {
        return nextToken;
    }

    //Only for public use
    public void Advance() throws IOException {
        // Check whether the switch may find another case, ie isDigit, isText or the likes.
        // Depending on the cases, it should be redirected to something in the likes of "Find keyword" or "Find literal"
        Token temp = nextToken;

        do {
            nextToken = getToken();
        }
        while (nextToken.type == Token.Type.NOTATOKEN);

        currentToken = temp;
    }

    private Token getToken() throws IOException {
        Token token = new Token(Token.Type.NOTATOKEN, "");
        token.lineNumber = reader.currentLine;
        token.offSet = reader.currentOffset;

        token = IsSingleCharacter(token);
        if (token.type == Token.Type.NOTATOKEN) {
            token = findNumberTokenType(token);
        }
        if (token.type == Token.Type.NOTATOKEN) {
            token.content = reader.findWord();
            token = findKeyword(token);
        }

        if (token.type == Token.Type.NOTATOKEN && token.content.length() >= 1) {
            token.type = Token.Type.IDENTIFIER;
        }
        return token;
    }

    public Token findKeyword(Token token) {
        switch (token.content) {
            case "MASTER":
                token.type = Token.Type.MASTER;
                break;
            case "SLAVE":
                token.type = Token.Type.SLAVE;
                break;
            case "END":
                token.type = Token.Type.END;
                break;
            case "BEGIN":
                token.type = Token.Type.BEGIN;
                break;
            case "digital":
                token.type = Token.Type.DIGITAL;
                break;
            case "input":
                token.type = Token.Type.INPUT;
                break;
            case "Listener":
                token.type = Token.Type.LISTENER;
                break;
            case "output":
                token.type = Token.Type.OUTPUT;
                break;
            case "write":
                token.type = Token.Type.WRITE;
                break;
            case "EventHandler":
                token.type = Token.Type.EVENTHANDLER;
                break;
            case "Initiate":
                token.type = Token.Type.INITIATE;
                break;
            case "if":
                token.type = Token.Type.IF;
                break;
            case "else":
                token.type = Token.Type.ELSE;
                break;
            case "analog":
                token.type = Token.Type.ANALOG;
            case "pwm":
                token.type = Token.Type.PWM;
                break;
            case "flip":
                token.type = Token.Type.FLIP;
                break;
            case "constant":
                token.type = Token.Type.CONSTANT;
                break;
            case "range":
                token.type = Token.Type.RANGE;
                break;
            case "broadcast":
                token.type = Token.Type.BROADCAST;
                break;
            case "filterNoise":
                token.type = Token.Type.FILTERNOISE;
                break;
            case "getValue":
                token.type = Token.Type.GETVALUE;
                break;
            case "pin":
                token.type = Token.Type.PIN;
                break;
            case "float":
                token.type = Token.Type.FLOAT;
                break;
            case "int":
                token.type = Token.Type.INT;
                break;
            case "bool":
                token.type = Token.Type.BOOL;
                break;
            case "event":
                token.type = Token.Type.EVENT;
                break;
            case "TRUE":
            case "true":
            case "false":
            case "FALSE":
                token.type = Token.Type.LIT_Bool;
                break;
            default:
                token.type = Token.Type.NOTATOKEN;
        }
        return token;
    }

    public Token IsSingleCharacter(Token token) throws IOException {
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
                    token.content += reader.readChar();
                    token.type = Token.Type.OP_MINUS_EQUALS;
                } else {
                    token.type = Token.Type.OP_MINUS;
                }
                return token;
            case '*':
                if (reader.nextChar == '=') {
                    token.content += reader.readChar();
                    token.type = Token.Type.OP_TIMES_EQUALS;
                } else {
                    token.type = Token.Type.OP_TIMES;
                }
                return token;
            case '/':   // Has some special cases when followed by other symbols
                if (reader.nextChar == '=') {
                    token.type = Token.Type.OP_DIVIDE_EQUALS;
                    token.content += reader.readChar();
                } else if (reader.nextChar == '*') {
                    //TODO: Handle start of comment
                } else if (reader.nextChar == '/') {
                    while(reader.currentChar != '\n') {
                        reader.readChar();
                    }
                    return token;
                } else {
                    token.type = Token.Type.OP_DIVIDE;
                }
                return token;

            case '=':
                if (reader.nextChar == '=') {
                    token.type = Token.Type.LOP_EQUALS;
                    token.content += reader.readChar();
                }
                else {
                    token.type = Token.Type.ASSIGN;
                }
                return token;

            case '%':
                token.type = Token.Type.OP_MODULO;
                return token;

            case '?':
                token.type = Token.Type.OP_QUESTION;
                return token;

            case '!':
                if (reader.nextChar == '=') {
                    token.type = Token.Type.LOP_NOTEQUAL;

                } else
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

            case '>':
                if (reader.nextChar == '=') {
                    token.type = Token.Type.LOP_GREATEROREQUAL;
                    token.content += reader.readChar();
                } else {
                    token.type = Token.Type.LOP_GREATERTHAN;
                }
                return token;
            case '<':
                if (reader.nextChar == '=') {
                    token.type = Token.Type.LOP_LESSOREQUAL;
                    token.content += reader.readChar();
                } else {
                    token.type = Token.Type.LOP_LESSTHAN;
                }
                return token;
            default:
                return token;
        }

    }

    public Token findNumberTokenType(Token token) throws IOException {
        token.content = reader.findNumber();
        if (token.content.matches("[0-9]+") && token.content.length() > 0) {
            token.type = Token.Type.LIT_Int;
        } else if(token.content.length() > 0 && token.content.matches("[0-9]+.?[0-9]*")){
            token.type = Token.Type.LIT_Float;
        }else if (token.content.matches("[0-9A-Za-z.]+")) {
            token.type = Token.Type.ERROR;
        }

        return token;

    }


}
