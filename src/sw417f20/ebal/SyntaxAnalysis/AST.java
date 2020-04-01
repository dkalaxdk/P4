package sw417f20.ebal.SyntaxAnalysis;

public class AST {

    Node Root;

    public AST() {

    }

    public enum NodeType {
        Prog, Master, Slave, Initiate, Listener, EventHandler, Block,

        // Declarations
        PinDeclaration, FloatDeclaration, IntDeclaration, BoolDeclaration, EventDeclaration,

        Assignment, If,

        Call,

        Expression,

        Identifier, Prefix,

        // Literals
        IntLiteral, FloatLiteral, BoolLiteral,

        // Pin types
        Digital, Analog, PWM,

        // IO types
        Input, Output,

        // Filter types
        Constant, Flip, Range,

        // Function
        Broadcast, Write, GetValue, FilterNoise, CreateEvent,

        // Operator
        LessThan, GreaterThan, NotEqual, Equals, GreaterOrEqual, LessOrEqual, And, Or,
        Plus, Minus, Times, Divide, Modulo,

        Error, Empty
    }

    public static Node MakeNode(Token token) {

        switch (token.type) {
            case IDENTIFIER:
                return new Node(NodeType.Identifier, token.content);

            case LIT_Bool:
                return new Node(NodeType.BoolLiteral, token.content);

            case LIT_Int:
                return new Node(NodeType.IntLiteral, token.content);

            case LIT_Float:
                return new Node(NodeType.FloatLiteral, token.content);

            // TODO: Her burde der også være en for minus somehow. Måske noget der skal fixes på et senere stadie?
            case OP_NOT:
                return new Node(NodeType.Prefix, token.content);

            default:
                return new Node(NodeType.Error);
        }
    }

    public static Node MakeNode(NodeType nodeType) {
        return new Node(nodeType);
    }
}
