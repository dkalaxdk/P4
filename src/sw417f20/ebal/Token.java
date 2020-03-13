package sw417f20.ebal;

public class Token {
    public enum Type {
        NOTATOKEN,
        //Operators
        OP_PLUS, OP_MINUS, OP_TIMES, OP_DIVIDE, OP_NOT, OP_QUESTION, OP_MODULO,
        OP_PLUS_EQUALS, OP_MINUS_EQUALS, OP_TIMES_EQUALS, OP_DIVIDE_EQUALS,

        ASSIGN,
        
        // Logic Operators
        LOP_LESSTHAN, LOP_GREATERTHAN, LOP_NOTEQUAL, LOP_GREATEROREQUAL, LOP_LESSOREQUAL, LOP_EQUALS,

        // Literals
        LIT_Int, LIT_Float, LIT_Bool,

        EVENT, PIN, FLOAT, INT, BOOL,

        COMMA, DOT, BACKSLASH, COLON, SEMI, SINGLEQUOTE, DOUBLEQUOTE,

        LPAREN, RPAREN, RBRACKET, LBRACKET, RSQBRACKET, LSQBRACKET,

        //Keywords
        BEGIN, END, MASTER, SLAVE,
        DIGITAL, ANALOG, PWM, INPUT, OUTPUT, FLIP, CONSTANT, RANGE,
        BROADCAST, WRITE, FILTERNOISE, GETVALUE,
        LISTENER, EVENTHANDLER, INITIATE,
        IF, ELSE,

        //Identifiers
        IDENTIFIER,

        ERROR,


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
}
