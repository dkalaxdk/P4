package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SemanticsListenerBlockStrategy extends SemanticsCheckerStrategy{

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        SymbolTable.OpenScope();
        Node child = node.FirstChild;
        while (child.Type != Node.NodeType.Empty){
            if (child.Type != Node.NodeType.PinDeclaration){
                child.CheckSemantics();
            }
            else {
                MakeError(child, "No pin declarations allowed in Listener");
            }
            child = child.Next;
        }
        SymbolTable.CloseScope();
    }
}
