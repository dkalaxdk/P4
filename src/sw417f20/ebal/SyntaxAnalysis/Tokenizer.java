package sw417f20.ebal.SyntaxAnalysis;

import java.io.IOException;

public class Tokenizer {
    private Reader reader;

    public Tokenizer(Reader reader) {
        this.reader = reader;
    }

    public Token getToken() throws IOException {
        Token token = new Token(Token.Type.NOTATOKEN, "");
        token.lineNumber = reader.currentLine;
        token.offSet = reader.currentOffset;

        token.content += reader.readChar();

        IsSingleCharacter(token);
        if (token.type == Token.Type.NOTATOKEN) {
            token.content = reader.findNumber();
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
                break;
            case "pwm":
                token.type = Token.Type.PWM;
                break;
            case "debounce":
                token.type = Token.Type.DEBOUNCE;
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
            case "createEvent":
                token.type = Token.Type.CREATEEVENT;
                break;
            case "createPin":
                token.type = Token.Type.CREATEPIN;
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
            case "HIGH":
            case "LOW":
                token.type = Token.Type.LIT_Int;
                token.content = token.content.equals("HIGH") ? "1" : "0";
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
        if(token.content.length() > 1) {
            return;
        }
        switch (token.content.charAt(0)) {
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
                    reader.readToEndOfLine();
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

            case '!':
                if (reader.nextChar == '=') {
                    token.type = Token.Type.LOP_NOTEQUAL;
                    token.content += reader.readChar();
                } else token.type = Token.Type.OP_NOT;
                break;

            case '(':
                token.type = Token.Type.LPAREN;
                break;

            case ')':
                token.type = Token.Type.RPAREN;
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
            case ';':
                token.type = Token.Type.SEMI;
                break;
            case ':':
                token.type = Token.Type.COLON;
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

            case '&':
                if (reader.nextChar == '&') {
                    token.type = Token.Type.LOP_AND;
                    token.content += reader.readChar();
                }
                break;

            case '|':
                if (reader.nextChar == '|') {
                    token.type = Token.Type.LOP_OR;
                    token.content += reader.readChar();
                }
                break;

            default:
                token.type = Token.Type.NOTATOKEN;
                break;
        }
    }

    public void findNumberTokenType(Token token) {
        if (token.content.matches("[0-9]+")) {
            token.type = Token.Type.LIT_Int;
        } else if (token.content.matches("[0-9]+\\.[0-9]*")) {
            token.type = Token.Type.LIT_Float;
        } else if (token.content.matches("[0-9A-Za-z.]+")) {
            token.type = Token.Type.ERROR;
        }
    }

}
