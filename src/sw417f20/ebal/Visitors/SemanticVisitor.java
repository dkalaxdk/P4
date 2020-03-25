package sw417f20.ebal.Visitors;

import sw417f20.ebal.ContextAnalysis.HashSymbolTable;
import sw417f20.ebal.ContextAnalysis.SymbolTable;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SemanticVisitor extends Visitor {

    SymbolTable symTab = new HashSymbolTable();

    @Override
    public void Visit(Node node) {
        switch(node.Type) {
            case Prog:
            case Master:
            case Slave:
            case Initiate:
            case Block:
                VisitChildren(node);
                break;
            case Listener:
                break;
            case EventHandler:
                break;
            case PinDeclaration:
                break;
            case Declaration:
                break;
            case Assignment:
                break;
            case If:
                break;
            case Call:
                break;
            case Function:
                break;
            case Expression:
                break;
            case Identifier:
                break;
            case Type:
                break;
            case Literal:
                break;
            case Operator:
                break;
            case Prefix:
                break;
            case Func:
                break;
            case Returns:
                break;
            case PinType:
                break;
            case IOType:
                break;
            case FilterType:
                break;
            case Error:
                break;
            case Empty:
                break;
            default:
                // code block
        }
    }
}
