package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SemanticsEventHandlerStrategy extends SemanticsCheckerStrategy{

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        // Retrieve event from symbol table
        Symbol symbol = SymbolTable.RetrieveSymbol(node.FirstChild.Value);
        if (symbol != null){
            if (symbol.DataType == Symbol.SymbolType.EVENT){
                // Check semantics for the block of the EventHandler
                node.FirstChild.Next.checkSemantics();
            }
            else {
                MakeError(node, symbol.Name, ErrorType.WrongType);
            }
        }
        else {
            MakeError(node, node.FirstChild.Value, ErrorType.NotDeclared);
        }
    }
}
