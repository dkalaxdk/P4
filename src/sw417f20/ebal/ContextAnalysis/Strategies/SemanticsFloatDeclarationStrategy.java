package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SemanticsFloatDeclarationStrategy extends SemanticsCheckerStrategy{

    boolean hasBeenInstantiated = false;

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        // Check if float is already declared
        if (!SymbolTable.DeclaredLocally(node.FirstChild.Value)){
            Node expression = node.FirstChild.Next;
            // Check semantics for the expression
            if (!expression.IsEmpty()) {
                expression.CheckSemantics();
                hasBeenInstantiated = true;
                // Check that expression has correct type
                if (expression.DataType != Symbol.SymbolType.FLOAT && expression.DataType != Symbol.SymbolType.INT){
                    MakeError(node, "Expression", ErrorType.WrongType);
                }
            }
            // Enter variable into symbol table
            SymbolTable.EnterSymbol(node.FirstChild.Value, Symbol.SymbolType.FLOAT, hasBeenInstantiated);
            hasBeenInstantiated = false;
        }
        else {
            MakeError(node, node.FirstChild.Value, ErrorType.AlreadyDeclared);
        }
    }
}
