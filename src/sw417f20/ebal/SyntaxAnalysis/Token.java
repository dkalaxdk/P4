package sw417f20.ebal.SyntaxAnalysis;

public class Token {
    public enum Type {

        NOTATOKEN,

        //Operators
        OP_PLUS, OP_MINUS, OP_TIMES, OP_DIVIDE, OP_NOT, OP_MODULO, ASSIGN,
        OP_PLUS_EQUALS, OP_MINUS_EQUALS, OP_TIMES_EQUALS, OP_DIVIDE_EQUALS,

        // Logic Operators
        LOP_LESSTHAN, LOP_GREATERTHAN, LOP_NOTEQUAL,
        LOP_GREATEROREQUAL, LOP_LESSOREQUAL, LOP_EQUALS,

        LOP_AND, LOP_OR,

        // Literals
        LIT_Int, LIT_Float, LIT_Bool,

        // Types
        EVENT, PIN, FLOAT, INT, BOOL,

        // Semi-types
        DIGITAL, ANALOG, PWM, INPUT, OUTPUT,
        DEBOUNCE, CONSTANT, RANGE,

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
        COMMA, SEMI, COLON,
        LPAREN, RPAREN, RBRACKET, LBRACKET,

        EOF

    }
    public Type type;   //Refactoring will cause many many many problems, don't do it
    public String Content;
    public int LineNumber;
    public int OffSet;

    public Token(Type inputType,String inputContent) {
        type = inputType;
        Content = inputContent;
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
        return ((Token) obj).Content.equals(this.Content);
    }

}
