package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SemanticsIdentifierStrategy extends SemanticsCheckerStrategy{

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        Symbol identifier = SymbolTable.RetrieveSymbol(node.Value);
        if (identifier != null){
            node.DataType = identifier.DataType;
        }
        else {
            MakeError(node, node.Value, ErrorType.NotDeclared);
        }
    }
}
