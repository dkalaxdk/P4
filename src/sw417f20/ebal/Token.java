package sw417f20.ebal;

public class Token {
    public enum Type {

        NOTATOKEN,

        //Operators
        OP_PLUS, OP_MINUS, OP_TIMES, OP_DIVIDE, OP_NOT, OP_QUESTION, OP_MODULO, ASSIGN,
        OP_PLUS_EQUALS, OP_MINUS_EQUALS, OP_TIMES_EQUALS, OP_DIVIDE_EQUALS,

        // Logic Operators
        LOP_LESSTHAN, LOP_GREATERTHAN, LOP_NOTEQUAL,
        LOP_GREATEROREQUAL, LOP_LESSOREQUAL, LOP_EQUALS,

        // Literals
        LIT_Int, LIT_Float, LIT_Bool,

        // Types
        EVENT, PIN, FLOAT, INT, BOOL,

        // Semi-types
        DIGITAL, ANALOG, PWM, INPUT, OUTPUT,
        FLIP, CONSTANT, RANGE,

        // Keywords
        BEGIN, END, MASTER, SLAVE,
        IF, ELSE,

        // Key blocks
        LISTENER, EVENTHANDLER, INITIATE,

        // Calls
        BROADCAST, WRITE, FILTERNOISE, GETVALUE, CREATEEVENT, CREATEPIN,

        //Identifiers
        IDENTIFIER,

        ERROR,

        // Misc
        COMMA, DOT, BACKSLASH, COLON, SEMI, SINGLEQUOTE, DOUBLEQUOTE,
        LPAREN, RPAREN, RBRACKET, LBRACKET, RSQBRACKET, LSQBRACKET,
        COMMENT, MULTILINE_COMMENT,

        EOF

    }
    public Type type;
    public String content;
    public int lineNumber;
    public int offSet;

    public Token(Type inputType,String inputContent) {
        type = inputType;
        content = inputContent;
    }

    @Override
    public String toString() {

        return type.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() != Token.class) {
            return false;
        }
        if(((Token) obj).type != this.type) {
            return false;
        }
        return ((Token) obj).content.equals(this.content);
    }

}
