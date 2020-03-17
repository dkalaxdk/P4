package sw417f20.ebal;

import sw417f20.ebal.Nodes.LeafNodes.*;
import sw417f20.ebal.Nodes.Node;
import sw417f20.ebal.Nodes.NonLeafNodes.*;

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


        Error, Null

    }

    public static Node MakeNode(Token token) {

        switch (token.type) {
            case IDENTIFIER:
                return new IdentifierNode(NodeType.Identifier, token.content);

            case LIT_Int: case LIT_Bool: case LIT_Float:
                return new LiteralNode(NodeType.Literal, token.content);

            case INPUT: case OUTPUT:
                return new IOTypeNode(NodeType.IOType, token.content);

            case DIGITAL: case ANALOG: case PWM:
                return new PinTypeNode(NodeType.PinType, token.content);

            case FLOAT: case INT: case BOOL: case EVENT: case PIN:
                return new TypeNode(NodeType.Type, token.content);

            case OP_PLUS: case OP_MINUS: case OP_TIMES: case OP_DIVIDE:
            case LOP_EQUALS: case LOP_NOTEQUAL: case LOP_LESSTHAN: case LOP_GREATERTHAN: case LOP_LESSOREQUAL: case LOP_GREATEROREQUAL:
                return new OperatorNode(NodeType.Operator, token.content);

            // TODO: Her burde der også være en for minus somehow. Måske noget der skal fixes på en senere stadie?
            case OP_NOT:
                return new PrefixNode(NodeType.Prefix, token.content);

            case CREATEEVENT: case GETVALUE: case BROADCAST: case WRITE: case FILTERNOISE:
                return new FuncNode(NodeType.Func, token.content);

            case FLIP: case CONSTANT: case RANGE:
                return new FilterTypeNode(NodeType.FilterType, token.content);

            default:
                return new Node(NodeType.Error);
        }
    }

    public static Node MakeNode(NodeType nodeType) {
        switch (nodeType) {
            case Prog:
                return new ProgNode(nodeType);
            case Master:
                return new MasterNode(nodeType);
            case Slave:
                return new SlaveNode(nodeType);
            case Initiate:
                return new InitiateNode(nodeType);
            case Listener:
                return new ListenerNode(nodeType);
            case EventHandler:
                return new EventHandlerNode(nodeType);
            case Block:
                return new BlockNode(nodeType);
            case PinDeclaration:
                return new PinDeclarationNode(nodeType);
            case Declaration:
                return new DeclarationNode(nodeType);
            case Assignment:
                return new AssignmentNode(nodeType);
            case If:
                return new IfNode(nodeType);
            case Call:
                return new CallNode(nodeType);
            case Function:
                return new FunctionNode(nodeType);
            case Expression:
                return new ExpressionNode(nodeType);

            case Null:
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
