package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SemanticsEventHandlerBlockStrategy extends SemanticsCheckerStrategy {

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        SymbolTable.OpenScope();
        // Check semantics for the statements in the block
        Node child = node.FirstChild;
        while (child != null && !child.isEmpty()) {

            // Check for illegal declarations
            if (child.Type != Node.NodeType.PinDeclaration && child.Type != Node.NodeType.EventDeclaration){
                child.checkSemantics();
            }
            else {
                MakeError(child, "No pin or event declarations allowed in EventHandler");
            }
            child = child.Next;
        }
        SymbolTable.CloseScope();
    }
}
