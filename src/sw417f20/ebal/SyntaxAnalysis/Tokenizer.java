package sw417f20.ebal.SyntaxAnalysis;

import java.io.IOException;

public class Tokenizer {
    private Reader reader;

    public Tokenizer(Reader reader) {
        this.reader = reader;
    }

    public Token GetToken() throws IOException {
        Token token = new Token(Token.Type.NOTATOKEN, "");
        token.LineNumber = reader.currentLine;
        token.OffSet = reader.currentOffset;

        token.Content += reader.ReadChar();

        AssignSpecialCharacterType(token);
        if (token.type == Token.Type.NOTATOKEN) {
            token.Content = reader.FindNumber();
            AssignNumberTokenType(token);
        }
        if (token.type == Token.Type.NOTATOKEN) {
            token.Content = reader.FindWord();
            AssignKeywordType(token);
        }

        if (token.type == Token.Type.NOTATOKEN && token.Content.length() >= 1) {
            token.type = Token.Type.IDENTIFIER;
        }
        return token;
    }

    public void AssignKeywordType(Token token) {
        switch (token.Content) {
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
                token.Content = token.Content.equals("HIGH") ? "1" : "0";
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

    public void AssignSpecialCharacterType(Token token) throws IOException {
        if(token.Content.length() > 1) {
            return;
        }
        switch (token.Content.charAt(0)) {  //todo Fjern ikke brugte cases
            case '+':
                if (reader.nextChar == '=') {
                    token.type = Token.Type.OP_PLUS_EQUALS;
                } else token.type = Token.Type.OP_PLUS;
                break;
            case '-':
                if (reader.nextChar == '=') {
                    token.Content += reader.ReadChar();
                    token.type = Token.Type.OP_MINUS_EQUALS;
                } else token.type = Token.Type.OP_MINUS;
                break;
            case '*':
                if (reader.nextChar == '=') {
                    token.Content += reader.ReadChar();
                    token.type = Token.Type.OP_TIMES_EQUALS;
                } else token.type = Token.Type.OP_TIMES;
                break;
            case '/':   // Has some special cases when followed by other symbols
                if (reader.nextChar == '=') {
                    token.type = Token.Type.OP_DIVIDE_EQUALS;
                    token.Content += reader.ReadChar();
                } else if (reader.nextChar == '*') {
                    reader.ReadToEndOfComment();
                } else if (reader.nextChar == '/') {
                    reader.ReadToEndOfLine();
                } else token.type = Token.Type.OP_DIVIDE;
                break;

            case '=':
                if (reader.nextChar == '=') {
                    token.type = Token.Type.LOP_EQUALS;
                    token.Content += reader.ReadChar();
                } else token.type = Token.Type.ASSIGN;
                break;

            case '%':
                token.type = Token.Type.OP_MODULO;
                break;

            case '!':
                if (reader.nextChar == '=') {
                    token.type = Token.Type.LOP_NOTEQUAL;
                    token.Content += reader.ReadChar();
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
                    token.Content += reader.ReadChar();
                } else token.type = Token.Type.LOP_GREATERTHAN;
                break;
            case '<':
                if (reader.nextChar == '=') {
                    token.type = Token.Type.LOP_LESSOREQUAL;
                    token.Content += reader.ReadChar();
                } else token.type = Token.Type.LOP_LESSTHAN;
                break;

            case '&':
                if (reader.nextChar == '&') {
                    token.type = Token.Type.LOP_AND;
                    token.Content += reader.ReadChar();
                }
                break;

            case '|':
                if (reader.nextChar == '|') {
                    token.type = Token.Type.LOP_OR;
                    token.Content += reader.ReadChar();
                }
                break;

            default:
                token.type = Token.Type.NOTATOKEN;
                break;
        }
    }

    public void AssignNumberTokenType(Token token) {
        if (token.Content.matches("[0-9]+")) {
            token.type = Token.Type.LIT_Int;
        } else if (token.Content.matches("[0-9]+\\.[0-9]*")) {
            token.type = Token.Type.LIT_Float;
        } else if (token.Content.matches("[0-9A-Za-z.]+")) {
            token.type = Token.Type.ERROR;
        }
    }

}
