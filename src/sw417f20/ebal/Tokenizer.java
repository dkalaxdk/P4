package sw417f20.ebal;

import sw417f20.ebal.Reader.Reader;

import java.io.IOException;

public class Tokenizer {
    private Reader reader;

    public Tokenizer(Reader reader) {
        this.reader = reader;
    }

    // Could maybe move back to scanner
    public Token getToken() throws IOException {
        Token token = new Token(Token.Type.NOTATOKEN, "");
        token.lineNumber = reader.currentLine;
        token.offSet = reader.currentOffset;

        token.content += reader.readChar();

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
            default:
                token.type = Token.Type.NOTATOKEN;
        }
        return token;
    }

    public Token IsSingleCharacter(Token token) throws IOException {
        if(token.content.length() > 1) {
            return token;
        }
        switch (token.content.charAt(0)) {
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

    public Token findNumberTokenType(Token token) throws IOException {
        token.content = reader.findNumber();
        if (!token.content.contains(".") && token.content.length() > 0) {
            token.type = Token.Type.LIT_Int;
        } else if(token.content.length() > 0){
            token.type = Token.Type.LIT_Float;
        } if (token.content.matches("[0-9A-Za-z.]+")) {
            token.type = Token.Type.ERROR;
        }

        return token;

    }
}