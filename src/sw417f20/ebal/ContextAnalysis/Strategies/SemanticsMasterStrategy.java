package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;

public class SemanticsMasterStrategy extends SemanticsCheckerStrategy{

    // List of events that are broadcast to slaves
    // Is assigned when strategy is created
    public ArrayList<Symbol> BroadcastEvents;

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        SymbolTable.OpenScope();

        // Check semantics for variables global to the master
        Node child = node.FirstChild;
        while (!child.isEmpty()){
            child.checkSemantics();
            child = child.Next;
        }

        // Check semantics for Initiate
        child = child.Next;
        child.checkSemantics();
        child = child.Next;

        // Check semantics for listeners
        while (!child.isEmpty()){
            child.checkSemantics();
            child = child.Next;
        }

        // Make events that are broadcast available to slaves
        // by putting them in the global scope
        MakeLocalEventsGlobal();
        SymbolTable.CloseScope();
    }

    // Put local events into the global scope
    private void MakeLocalEventsGlobal() {
        for(Symbol symbol : BroadcastEvents){
            SymbolTable.EnterGlobalSymbol(symbol);
        }
//        BroadcastEvents.clear();
    }
}
