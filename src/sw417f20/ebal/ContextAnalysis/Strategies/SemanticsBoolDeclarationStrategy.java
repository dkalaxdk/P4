package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SemanticsBoolDeclarationStrategy extends SemanticsCheckerStrategy{

    boolean HasBeenInstantiated = false;

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        // Check if identifier has already been declared
        if (!SymbolTable.DeclaredLocally(node.FirstChild.Value)){
            Node expression = node.FirstChild.Next;
            if (!expression.IsEmpty()) {
                expression.CheckSemantics();
                HasBeenInstantiated = true;
                if (expression.DataType != Symbol.SymbolType.BOOL){
                    MakeError(node, "Expression", ErrorType.WrongType);
                }
            }
            // Put the variable in the symbol table
            SymbolTable.EnterSymbol(node.FirstChild.Value, Symbol.SymbolType.BOOL, HasBeenInstantiated);
            HasBeenInstantiated = false;
        }
        else {
            MakeError(node, node.FirstChild.Value, ErrorType.AlreadyDeclared);
        }
    }
}





















