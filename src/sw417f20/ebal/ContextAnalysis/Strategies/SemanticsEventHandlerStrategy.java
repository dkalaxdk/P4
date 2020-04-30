package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SemanticsEventHandlerStrategy extends SemanticsCheckerStrategy{

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        Symbol symbol = SymbolTable.RetrieveSymbol(node.FirstChild.Value);
        if (symbol != null){
            if (symbol.DataType == Symbol.SymbolType.EVENT){
                node.FirstChild.Next.CheckSemantics();
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
