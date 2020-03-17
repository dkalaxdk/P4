package sw417f20.ebal;

import sw417f20.ebal.Reader.Reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
        } catch (IOException e) {
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

        IsSingleCharacter(token);
        if (token.type == Token.Type.NOTATOKEN) {
            findNumberTokenType(token);
        }
        if (token.type == Token.Type.NOTATOKEN) {
            token.content = reader.findWord();
            findKeyword(token);
        }

        if (token.type == Token.Type.NOTATOKEN && token.content.length() >= 1) {
            token.type = Token.Type.IDENTIFIER;
        }
        return token;
    }

    public void findKeyword(Token token) {
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

    }

    public void IsSingleCharacter(Token token) throws IOException {
        char input = reader.readChar();
        token.content += input;
        switch (input) {
            case '+':
                if (reader.nextChar == '=') {
                    token.type = Token.Type.OP_PLUS_EQUALS;
                } else token.type = Token.Type.OP_PLUS;
                break;
            case '-':
                if (reader.nextChar == '=') {
                    token.content += reader.readChar();
                    token.type = Token.Type.OP_MINUS_EQUALS;
                } else token.type = Token.Type.OP_MINUS;
                break;
            case '*':
                if (reader.nextChar == '=') {
                    token.content += reader.readChar();
                    token.type = Token.Type.OP_TIMES_EQUALS;
                } else token.type = Token.Type.OP_TIMES;
                break;
            case '/':   // Has some special cases when followed by other symbols
                if (reader.nextChar == '=') {
                    token.type = Token.Type.OP_DIVIDE_EQUALS;
                    token.content += reader.readChar();
                } else if (reader.nextChar == '*') {
                    reader.readToEndOfComment();
                } else if (reader.nextChar == '/') {
                    while (reader.currentChar != '\n') {
                        reader.readChar();
                    }
                    break;
                } else token.type = Token.Type.OP_DIVIDE;
                break;

            case '=':
                if (reader.nextChar == '=') {
                    token.type = Token.Type.LOP_EQUALS;
                    token.content += reader.readChar();
                } else token.type = Token.Type.ASSIGN;
                break;

            case '%':
                token.type = Token.Type.OP_MODULO;
                break;

            case '?':
                token.type = Token.Type.OP_QUESTION;
                break;

            case '!':
                if (reader.nextChar == '=') {
                    token.type = Token.Type.LOP_NOTEQUAL;
                } else token.type = Token.Type.OP_NOT;
                break;

            case '(':
                token.type = Token.Type.LPAREN;
                break;

            case ')':
                token.type = Token.Type.RPAREN;
                break;

            case '[':
                token.type = Token.Type.LSQBRACKET;
                break;

            case ']':
                token.type = Token.Type.RSQBRACKET;
                break;

            case '{':
                token.type = Token.Type.LBRACKET;
                break;

            case '}':
                token.type = Token.Type.RBRACKET;
                break;

            case ',':
                token.type = Token.Type.COMMA;
                break;

            case '.':
                if (String.valueOf(reader.nextChar).matches("[0-9A-Za-z]")) {
                    reader.readChar();
                    token.content += reader.findNumber();
                    token.type = Token.Type.ERROR;
                } else token.type = Token.Type.DOT;
                break;
            case ';':
                token.type = Token.Type.SEMI;
                break;
            case ':':
                token.type = Token.Type.COLON;
                break;
            case '\\':
                token.type = Token.Type.BACKSLASH;
                break;
            case '"':
                token.type = Token.Type.DOUBLEQUOTE;
                break;
            case '\'':
                token.type = Token.Type.SINGLEQUOTE;
                break;
            case '\uFFFF':
                token.type = Token.Type.EOF;
                break;
            case '>':
                if (reader.nextChar == '=') {
                    token.type = Token.Type.LOP_GREATEROREQUAL;
                    token.content += reader.readChar();
                } else token.type = Token.Type.LOP_GREATERTHAN;
                break;
            case '<':
                if (reader.nextChar == '=') {
                    token.type = Token.Type.LOP_LESSOREQUAL;
                    token.content += reader.readChar();
                } else token.type = Token.Type.LOP_LESSTHAN;
                break;
            default:
                token.type = Token.Type.NOTATOKEN;
                break;
        }

    }

    public void findNumberTokenType(Token token) throws IOException {
        token.content = reader.findNumber();
        if (token.content.matches("[0-9]+")) {
            token.type = Token.Type.LIT_Int;
        } else if (token.content.matches("[0-9]+\\.[0-9]*")) {
            token.type = Token.Type.LIT_Float;
        } else if (token.content.matches("[0-9A-Za-z.]+")) {
            token.type = Token.Type.ERROR;
        }

    }


}
