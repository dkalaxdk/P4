package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SemanticsSlaveStrategy extends SemanticsCheckerStrategy {

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        if (!SymbolTable.DeclaredLocally(node.FirstChild.Value)) {
            SymbolTable.EnterSymbol(node.FirstChild.Value, Symbol.SymbolType.SLAVE);

            SymbolTable.OpenScope();
            Node child = node.FirstChild.Next;
            child.CheckSemantics();
            child = child.Next;
            while (!child.IsEmpty()) {
                child.CheckSemantics();
                child = child.Next;
            }
            SymbolTable.CloseScope();
        }
        else {
            MakeError(node, node.FirstChild.Value, ErrorType.AlreadyDeclared);
        }
    }
}
