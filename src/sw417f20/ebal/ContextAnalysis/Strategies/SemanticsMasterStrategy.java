package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SemanticsMasterStrategy extends SemanticsCheckerStrategy{

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        SymbolTable.OpenScope();
        Node child = node.FirstChild;
        child.CheckSemantics();
        child = child.Next;
        while (child.Type != Node.NodeType.Empty){
            child.CheckSemantics();
            child = child.Next;
        }
        MakeLocalEventsGlobal();
        SymbolTable.CloseScope();
    }

    private void MakeLocalEventsGlobal() {
        for(Symbol symbol : LocalEvents){
            SymbolTable.EnterGlobalSymbol(symbol);
        }
        LocalEvents.clear();
    }
}
