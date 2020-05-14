package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;

public class SemanticsEventDeclarationStrategy extends SemanticsCheckerStrategy{

    // Contains events in the local scope
    // is assigned when strategy is created
    public ArrayList<Symbol> LocalEvents;
    boolean InGlobalScope;

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        if (!InGlobalScope) {
            // Check event identifier is not already in local scope
            if (!InLocalEvents(node.FirstChild.Value)) {
                Node expression = node.FirstChild.Next;
                if (expression.Type == Node.NodeType.Call && expression.FirstChild.Type == Node.NodeType.CreateEvent) {
                    expression.CheckSemantics();
                    // Enter the event into the symbol table, and the list of local events
                    SymbolTable.EnterSymbol(node.FirstChild.Value, Symbol.SymbolType.EVENT, expression.FirstChild.Next.DataType);
                    LocalEvents.add(SymbolTable.RetrieveSymbol(node.FirstChild.Value));
                }
                else {
                    MakeError(node, "Illegal declaration of event.");
                }
            }
            else {
                MakeError(node, node.FirstChild.Value, ErrorType.AlreadyDeclared);
            }
        }
        else {
            MakeError(node, "Events cannot be declared globally");
        }
    }

    // Checks if local events contain a symbol with the given name
    private boolean InLocalEvents(String name) {
        for (Symbol symbol : LocalEvents){
            if (symbol.Name.equals(name)){
                return true;
            }
        }
        return false;
    }
}
