package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SemanticsSlaveStrategy extends SemanticsCheckerStrategy {

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        // Add slave to symbol table and open new scope
        if (!SymbolTable.DeclaredLocally(node.FirstChild.Value)) {
            SymbolTable.EnterSymbol(node.FirstChild.Value, Symbol.SymbolType.SLAVE);

            SymbolTable.OpenScope();

            // Check semantics for variables global to the slave
            Node child = node.FirstChild.Next;
            while (!child.isEmpty()){
                child.checkSemantics();
                child = child.Next;
            }

            // Check semantics for Initiate
            child = child.Next;
            child.checkSemantics();
            child = child.Next;

            // Check semantics for EventHandlers
            while (!child.isEmpty()) {
                child.checkSemantics();
                child = child.Next;
            }

            SymbolTable.CloseScope();
        }
        else {
            MakeError(node, node.FirstChild.Value, ErrorType.AlreadyDeclared);
        }
    }
}
