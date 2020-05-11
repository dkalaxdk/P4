package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;

public class SemanticsMasterStrategy extends SemanticsCheckerStrategy{

    public ArrayList<Symbol> BroadcastEvents;

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        SymbolTable.OpenScope();

        // Check semantics for variables global to the master
        Node child = node.FirstChild;
        while (!child.IsEmpty()){
            child.CheckSemantics();
            child = child.Next;
        }

        // Check semantics for Initiate
        child = child.Next;
        child.CheckSemantics();
        child = child.Next;

        // Check semantics for listeners
        while (!child.IsEmpty()){
            child.CheckSemantics();
            child = child.Next;
        }

        MakeLocalEventsGlobal();
        SymbolTable.CloseScope();
    }

    private void MakeLocalEventsGlobal() {
        for(Symbol symbol : BroadcastEvents){
            SymbolTable.EnterGlobalSymbol(symbol);
        }
//        BroadcastEvents.clear();
    }
}
