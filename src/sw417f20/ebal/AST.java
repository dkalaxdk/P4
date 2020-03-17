package sw417f20.ebal;

import java.lang.invoke.SwitchPoint;

public class AST {

    Node Root;

    public AST() {

    }

//    public Node MakeNode(Token token) {
//        Node result = new Node();
//
//        switch (token.type) {
//            case BEGIN:
//                result.Type = Token.Type.BEGIN;
//                break;
//            case END:
//                result.Type = Token.Type.END;
//                break;
//            case MASTER:
//                result.Type = Token.Type.MASTER;
//                break;
//            case INITIATE:
//                result.Type = Token.Type.INITIATE;
//                break;
//            case LISTENER:
//                result.Type = Token.Type.LISTENER;
//                break;
//            case SLAVE:
//                result.Type = Token.Type.SLAVE;
//                break;
//        }
//
//        return result;
//    }
//
//    public enum Type {
//        Listeners, EventHandlers, Slaves, Start
//    }

//    public Node MakeNode(Type type) {
//        Node result = new Node();
//        switch (type) {
//            case Start:
//                break;
//        }
//
//        return result;
//    }

    public static Node MakeNode() {
        return new Node();
    }

    public static Node MakeNode(String name) {
        return new Node(name);
    }

    public static Node MakeNode(Token token) {

        switch (token.type) {
            case IDENTIFIER:
                return new Node("id", token);
            case FLOAT: case INT: case EVENT: case BOOL: case PIN:
                return new Node("type", token);
            default:
                return new Node("Unidentified token");
        }
    }

//    public static Node MakeNode(String name, Token token) {
//        return new Node(name, token);
//    }

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
