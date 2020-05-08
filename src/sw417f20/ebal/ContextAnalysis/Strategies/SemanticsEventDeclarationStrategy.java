package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;

public class SemanticsEventDeclarationStrategy extends SemanticsCheckerStrategy{

    public ArrayList<Symbol> LocalEvents;
    boolean inGlobalScope;

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        if (!inGlobalScope) {
            if (!InLocalEvents(node.FirstChild.Value)) {
                Node expression = node.FirstChild.Next;
                if (expression.Type == Node.NodeType.Call && expression.FirstChild.Type == Node.NodeType.CreateEvent) {
                    expression.CheckSemantics();
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

    private boolean InLocalEvents(String name) {
        for (Symbol symbol : LocalEvents){
            if (symbol.Name.equals(name)){
                return true;
            }
        }
        return false;
    }
}
