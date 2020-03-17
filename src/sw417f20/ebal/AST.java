package sw417f20.ebal;

public class AST {

    Node Root;

    public AST() {

    }

    public enum NodeType {
        Prog, Master, Slave, Initiate, Listener, EventHandler, Block,

        PinDeclaration,

        Declaration, Assignment, If,

        Call, Function,

        Expression,

        Identifier, Type, Literal, Operator, Prefix, Func, Returns,
        PinType, IOType, FilterType,


        Error, Empty

    }

    public static Node MakeNode(Token token) {

        switch (token.type) {
            case IDENTIFIER:
                return new Node(NodeType.Identifier, token.content);

            case LIT_Int: case LIT_Bool: case LIT_Float:
                return new Node(NodeType.Literal, token.content);

            case INPUT: case OUTPUT:
                return new Node(NodeType.IOType, token.content);

            case DIGITAL: case ANALOG: case PWM:
                return new Node(NodeType.PinType, token.content);

            case FLOAT: case INT: case BOOL: case EVENT: case PIN:
                return new Node(NodeType.Type, token.content);

            case OP_PLUS: case OP_MINUS: case OP_TIMES: case OP_DIVIDE:
            case LOP_EQUALS: case LOP_NOTEQUAL: case LOP_LESSTHAN: case LOP_GREATERTHAN: case LOP_LESSOREQUAL: case LOP_GREATEROREQUAL:
                return new Node(NodeType.Operator, token.content);

            // TODO: Her burde der også være en for minus somehow. Måske noget der skal fixes på en senere stadie?
            case OP_NOT:
                return new Node(NodeType.Prefix, token.content);

            case CREATEEVENT: case GETVALUE: case BROADCAST: case WRITE: case FILTERNOISE:
                return new Node(NodeType.Func, token.content);

            case FLIP: case CONSTANT: case RANGE:
                return new Node(NodeType.FilterType, token.content);

            default:
                return new Node(NodeType.Error);
        }
    }

    public static Node MakeNode(NodeType nodeType) {
        switch (nodeType) {
            case Prog:
            case Master:
            case Slave:
            case Initiate:
            case Listener:
            case EventHandler:
            case Block:
            case PinDeclaration:
            case Declaration:
            case Assignment:
            case If:
            case Call:
            case Function:
            case Returns:
            case Expression:
            case Empty:
                return new Node(nodeType);
            default:
                return new Node(NodeType.Error);
        }


    }


//    public Node MakeFamiliy(Node parent, Node child1, Node child2) {
//        parent.AdoptChildren(child1.MakeSiblings(child2));
//
//        return parent;
//    }
//
//    public Node MakeFamiliy(Node parent, Node child1, Node child2, Node child3) {
//        parent.AdoptChildren(child1.MakeSiblings(child2.MakeSiblings(child3)));
//
//        return parent;
//    }

//    public Node MakeFamily(Node parent, Node[] children, int length) {
//
//        if (length >= 2) {
//            parent.AdoptChildren(children[length - 2].MakeSiblings(children[length - 1]));
//        }
//
//        return parent;
//    }
}
