package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SemanticsEventHandlerBlockStrategy extends SemanticsCheckerStrategy {

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        SymbolTable.OpenScope();
        Node child = node.FirstChild;
        while (child != null && !child.IsEmpty()) {

            if (child.Type != Node.NodeType.PinDeclaration && child.Type != Node.NodeType.EventDeclaration){
                child.CheckSemantics();
            }
            else {
                MakeError(child, "No pin or event declarations allowed in EventHandler");
            }
            child = child.Next;
        }
        SymbolTable.CloseScope();
    }
}
