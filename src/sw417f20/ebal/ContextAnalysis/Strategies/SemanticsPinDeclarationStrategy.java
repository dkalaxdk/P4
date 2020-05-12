package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SemanticsPinDeclarationStrategy extends SemanticsCheckerStrategy{

    // Whether or not the declaration is in the global scope
    // Assigned by the strategy factory
    boolean inGlobalScope;

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        // Pins cannot be in global scope
        if (!inGlobalScope) {
            // Check if identifier is already declared
            if (!SymbolTable.DeclaredLocally(node.FirstChild.Value)) {
                Node expression = node.FirstChild.Next;
                // The expression must be a call to CreatePin
                if (expression.Type == Node.NodeType.Call && expression.FirstChild.Type == Node.NodeType.CreatePin) {
                    // Check the semantics for the expression
                    expression.checkSemantics();
                    // Enter the pin in the symbol table
                    SymbolTable.EnterSymbol(node.FirstChild.Value, Symbol.SymbolType.PIN, node.FirstChild.Next);
                }
                else {
                    MakeError(node, "Illegal declaration of pin.");
                }
            }
            else {
                MakeError(node, node.FirstChild.Value, ErrorType.AlreadyDeclared);
            }
        }
        else {
            MakeError(node, "Pins cannot be declared globally");
        }
    }


}
