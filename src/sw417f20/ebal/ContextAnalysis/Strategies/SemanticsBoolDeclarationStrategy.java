package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SemanticsBoolDeclarationStrategy extends SemanticsCheckerStrategy{

    boolean hasBeenIdentified = false;

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        if (!SymbolTable.DeclaredLocally(node.FirstChild.Value)){
            Node expression = node.FirstChild.Next;
            if (expression.Type != Node.NodeType.Empty) {
                expression.CheckSemantics();
                hasBeenIdentified = true;
                if (expression.DataType != Symbol.SymbolType.BOOL){
                    MakeError(node, node.FirstChild.Value, ErrorType.WrongType);
                }
            }
            SymbolTable.EnterSymbol(node.FirstChild.Value, Symbol.SymbolType.BOOL, hasBeenIdentified);
            hasBeenIdentified = false;
        }
        else {
            MakeError(node, node.FirstChild.Value, ErrorType.AlreadyDeclared);
        }
    }
}
