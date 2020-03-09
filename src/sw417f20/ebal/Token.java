package sw417f20.ebal;

public class Token {
    public enum Type {
        //Operators
        OP_add,OP_subtract,OP_multiply,OP_divide,
        // Literals

        LIT_Int,LIT_Float,LIT_char,


        //Keywords
        someKeyword,

        //Identifiers
        Identifier,




        EOF

    }
    public Type type;
    public String content;

    public Token(Type inputType,String inputContent) {
        type = inputType;
        content = inputContent;
    }
}
