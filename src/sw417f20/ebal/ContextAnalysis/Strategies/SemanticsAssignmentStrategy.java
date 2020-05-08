package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SemanticsAssignmentStrategy extends SemanticsCheckerStrategy {

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        Symbol identifier = SymbolTable.RetrieveSymbol(node.FirstChild.Value);
        Node expression = node.FirstChild.Next;
        if(identifier != null) {
            expression.CheckSemantics();
            if ((identifier.DataType == Symbol.SymbolType.BOOL && expression.DataType != Symbol.SymbolType.BOOL) ||
                (expression.DataType == Symbol.SymbolType.BOOL && identifier.DataType != Symbol.SymbolType.BOOL)) {

                MakeError(node, "Incompatible types");
            }
            else if (expression.DataType == Symbol.SymbolType.PIN){
                MakeError(node, "expression cannot be of type Pin");
            }
            else if (expression.DataType == Symbol.SymbolType.EVENT){
                MakeError(node, "expression cannot be of type Event");
            }
            identifier.HasBeenInstantiated = true;
        }
        else {
            MakeError(node, node.FirstChild.Value, ErrorType.NotDeclared);
        }
    }
}
