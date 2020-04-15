package sw417f20.ebal.SyntaxAnalysis;

public class AST {

    public Node Root;

    public AST() {

    }

    public enum NodeType {
        Prog, Master, Slave, Initiate, Listener, EventHandler, Block,

        // Declarations
        PinDeclaration, FloatDeclaration, IntDeclaration, BoolDeclaration, EventDeclaration,

        Assignment, If,

        Call,

        Expression,

        Identifier,

        // Literals
        IntLiteral, FloatLiteral, BoolLiteral,

        // Pin types
        Digital, Analog, PWM,

        // IO types
        Input, Output,

        // Filter types
        Constant, Flip, Range,

        // Function
        Broadcast, Write, GetValue, FilterNoise, CreateEvent, CreatePin,

        // Operator
        LessThan, GreaterThan, NotEqual, Equals, GreaterOrEqual, LessOrEqual, And, Or,
        Plus, Minus, Times, Divide, Modulo,

        // Prefixes
        PrefixNot, PrefixMinus,

        Error, Empty
    }

    public static Node MakeNode(Token token) {

        switch (token.type) {
            case IDENTIFIER:
                return new Node(NodeType.Identifier, token);

            case LIT_Bool:
                return new Node(NodeType.BoolLiteral, token);

            case LIT_Int:
                return new Node(NodeType.IntLiteral, token);

            case LIT_Float:
                return new Node(NodeType.FloatLiteral, token);

            default:
                return new Node(NodeType.Error);
        }
    }

    public static Node MakeNode(NodeType nodeType) {
        return new Node(nodeType);
    }

    public static Node MakeNode(NodeType nodeType, int lineNumber) {
        return new Node(nodeType, lineNumber);
    }
}
