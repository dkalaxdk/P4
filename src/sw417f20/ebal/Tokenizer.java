package sw417f20.ebal;

import sw417f20.ebal.Reader.Reader;

import java.io.IOException;

public class Tokenizer {

    public Tokenizer() {
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
        // Max length of a "single" character token
        if(token.content.length() > 2) {
            return;
        }
        switch (token.content) {
            case "+":
                token.type = Token.Type.OP_PLUS;
                break;
            case "+=":
                token.type = Token.Type.OP_PLUS_EQUALS;
                break;
            case "-":
                token.type = Token.Type.OP_MINUS;
                break;
            case "-=":
                token.type = Token.Type.OP_MINUS_EQUALS;
                break;
            case "*":
                token.type = Token.Type.OP_TIMES;
                break;
            case "*=":
                token.type = Token.Type.OP_TIMES_EQUALS;
                break;
            case "/":   // Has some special cases when followed by other symbols
                token.type = Token.Type.OP_DIVIDE;
                break;
            case "/=":
                token.type = Token.Type.OP_DIVIDE_EQUALS;
                break;
            case "/*":
                token.type = Token.Type.MULTILINE_COMMENT;
                break;
            case "//":
                token.type = Token.Type.COMMENT;
                break;
            case "=":
                token.type = Token.Type.ASSIGN;
                break;
            case "==":
                token.type = Token.Type.LOP_EQUALS;
                break;
            case "%":
                token.type = Token.Type.OP_MODULO;
                break;
            //TODO slet mig '?' måske
            case "?":
                token.type = Token.Type.OP_QUESTION;
                break;
            case "!":
                token.type = Token.Type.OP_NOT;
                break;
            case "!=":
                token.type = Token.Type.LOP_NOTEQUAL;
                break;
            case "(":
                token.type = Token.Type.LPAREN;
                break;
            case ")":
                token.type = Token.Type.RPAREN;
                break;
            //TODO har vi [] stadig væk?
            case "[":
                token.type = Token.Type.LSQBRACKET;
                break;
            case "]":
                token.type = Token.Type.RSQBRACKET;
                break;
            case "{":
                token.type = Token.Type.LBRACKET;
                break;
            case "}":
                token.type = Token.Type.RBRACKET;
                break;
            case ",":
                token.type = Token.Type.COMMA;
                break;
            case ";":
                token.type = Token.Type.SEMI;
                break;
            //TODO slet mig ':' måske
            case ":":
                token.type = Token.Type.COLON;
                break;
            //TODO hvad gør '\' i vores sprog
            case "\\":
                token.type = Token.Type.BACKSLASH;
                break;
            case "\"":
                token.type = Token.Type.DOUBLEQUOTE;
                break;
            case "'":
                token.type = Token.Type.SINGLEQUOTE;
                break;
            case "\uFFFF":
                token.type = Token.Type.EOF;
                break;
            case ">":
                token.type = Token.Type.LOP_GREATERTHAN;
                break;
            case ">=":
                token.type = Token.Type.LOP_GREATEROREQUAL;
                break;
            case "<":
                token.type = Token.Type.LOP_LESSTHAN;
                break;
            case "<=":
                token.type = Token.Type.LOP_LESSOREQUAL;
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
