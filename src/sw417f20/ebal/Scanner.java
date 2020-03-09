package sw417f20.ebal;

public class Scanner {

    char currentChar;
    char nextChar;
    String whitespace = "\\t\\n\\s";

    public boolean isWhiteSpace() {
        return whitespace.indexOf(nextChar) != -1;
    }


    public char Peek() {
        return ' ';
    }

    public void pop() {
        // Do code related to popping or finding potential popping errors
    }

    public Token Advance() {

        if (isWhiteSpace()) {
            pop();
            // Check if it is a white space, if white space, advance
        }

        // Check whether the switch may find another case, ie isDigit, isText or the likes.
        // Depending on the cases, it should be redirected to something in the likes of "Find keyword" or "Find literal"

        // Set currentChar to nextChar
        return new Token(Token.Type.EOF,"");
    }


    public char findKeyword(String input) {
        char output = ' ';
        Token token = new Token(Token.Type.EOF,"");
        switch (input) {
            case "begin":
                token.type = Token.Type.someKeyword;
                break;
            case "master":
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

    public void IsBoolean() {

    }

    public void IsOperator() {

    }

    public void IsIdentifier() {

    }
}
