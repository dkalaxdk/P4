package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SemanticsIntDeclarationStrategy extends  SemanticsCheckerStrategy{

    boolean hasBeenInstantiated = false;

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        // Check that identifier has not already been declared
        if (!SymbolTable.DeclaredLocally(node.FirstChild.Value)){
            Node expression = node.FirstChild.Next;
            if (!expression.isEmpty()) {
                // Check semantics for the expression
                expression.checkSemantics();
                hasBeenInstantiated = true;
                // Expression must evaluate to either int or float
                if (expression.DataType != Symbol.SymbolType.INT && expression.DataType != Symbol.SymbolType.FLOAT){
                    MakeError(node, "Expression", ErrorType.WrongType);
                }
            }
            // Enter the variable into the symbol table
            SymbolTable.EnterSymbol(node.FirstChild.Value, Symbol.SymbolType.INT, hasBeenInstantiated);
            hasBeenInstantiated = false;
        }
        else {
            MakeError(node, node.FirstChild.Value, ErrorType.AlreadyDeclared);
        }
    }
}
