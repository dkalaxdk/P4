package sw417f20.ebal;

public class Token {
    public enum Type {
        NOTATOKEN,
        //Operators
        OP_PLUS, OP_MINUS, OP_EQUALS, OP_TIMES, OP_DIVIDE, OP_NOT,OP_QUESTION,
        OP_PLUS_EQUALS, OP_MINUS_EQUALS, OP_TIMES_EQUALS, OP_DIVIDE_EQUALS,
        ASSIGN,

        // Literals
        LIT_Int, LIT_Float, LIT_char,

        EVENT, PIN, FLOAT, INT, BOOL,

        COMMA, DOT, BACKSLASH, COLON, SEMI, SINGLEQUOTE, DOUBLEQUOTE,

        LPAREN, RPAREN, RBRACKET, LBRACKET, RSQBRACKET, LSQBRACKET,

        //Keywords
        KEYWORD,
        BEGIN, END, MASTER, SLAVE,
        DIGITAL, ANALOG, PWM, INPUT, OUTPUT,
        BROADCAST, WRITE, FILTERNOISE, GETVALUE,
        LISTENER, EVENTHANDLER, INITIATE,
        IF,

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
