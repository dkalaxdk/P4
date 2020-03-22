package sw417f20.ebal;

import sw417f20.ebal.Reader.Reader;
import java.io.IOException;

public class Scanner {
    public Reader reader;
    private Tokenizer tokenizer;
    public Token currentToken;
    public Token nextToken;

    public Scanner(Reader reader, Tokenizer tokenizer) {
        currentToken = new Token(Token.Type.NOTATOKEN, "");
        nextToken = new Token(Token.Type.NOTATOKEN, "");

        this.reader = reader;
        this.tokenizer = tokenizer;
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

    // Could maybe move back to scanner
    public Token getToken() throws IOException {
        Token token = new Token(Token.Type.NOTATOKEN, "");
        token.lineNumber = reader.currentLine;
        token.offSet = reader.currentOffset;

        //char character = reader.readChar();
        char character = reader.currentChar;
        token.content += character;

        // if the first character of a token is a number, it must be a number literal
        if(isNumber(character)) {
            token.content = reader.findNumber();
            tokenizer.findNumberTokenType(token);
        }
        // if it is a letter or '_' it is a keyword or an identifier
        else if(isLetter(character) || character == '_') {
            token.content = reader.findWord();
            tokenizer.findKeyword(token);
            if(token.type == Token.Type.NOTATOKEN) {
                token.type = Token.Type.IDENTIFIER;
            }
        }
        // if it is none of the above it must be a "single" character token
        else {
            /*
            // add characters until a character is read that cannot be part of
            // a "single" character token
            //TODO: Determine what to do if it reads more characters than are used in the token
            // Supports tokens of arbitrary length
            while(canBePartOfSingleCharacterToken(reader.nextChar)) {
                token.content += reader.readChar();
            }
             */

            // This supports tokens with a max length of 2
            if(canBePartOfSingleCharacterToken(reader.nextChar)) {
                token.content += reader.readChar();
            }

            token.content += reader.readChar();
            tokenizer.IsSingleCharacter(token);

            // Handle special cases
            if(token.type == Token.Type.MULTILINE_COMMENT) {
                reader.readToEndOfComment();
                return getToken();
            }
            else if(token.type == Token.Type.COMMENT) {
                while (reader.currentChar != '\n') {
                    reader.readChar();
                }
                return getToken();
            }
        }

        /*
        tokenizer.IsSingleCharacter(token);
        if (token.type == Token.Type.NOTATOKEN) {
            token.content = reader.findNumber();
            tokenizer.findNumberTokenType(token);
        }
        if (token.type == Token.Type.NOTATOKEN) {
            token.content = reader.findWord();
            tokenizer.findKeyword(token);
        }
         */

        // if none of the cases matched it must be an error
        if (token.type == Token.Type.NOTATOKEN) {
            token.type = Token.Type.ERROR;
        }

        return token;
    }

    private boolean isNumber(char c) {
        String charAsString = "" + c;
        return charAsString.matches("[0-9]");
    }

    private boolean isLetter(char c) {
        String charAsString = "" + c;
        return charAsString.matches("[a-zA-Z]");
    }

    private boolean isWhitespace(char c) {
        return Character.isWhitespace(c);
    }

    private boolean canBePartOfSingleCharacterToken(char c) {
        return (!isLetter(c) && !isNumber(c) && !isWhitespace(c) && c != '_');
    }
}
