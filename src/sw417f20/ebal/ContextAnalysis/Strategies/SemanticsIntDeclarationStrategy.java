package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SemanticsIntDeclarationStrategy extends  SemanticsCheckerStrategy{

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        if (!SymbolTable.DeclaredLocally(node.FirstChild.Value)){
            Node expression = node.FirstChild.Next;
            if (expression.Type != Node.NodeType.Empty) {
                expression.CheckSemantics();
                if (expression.DataType != Symbol.SymbolType.INT){
                    MakeError(node, node.FirstChild.Value, ErrorType.WrongType);
                }
            }
            SymbolTable.EnterSymbol(node.FirstChild.Value, Symbol.SymbolType.INT);
        }
        else {
            MakeError(node, node.FirstChild.Value, ErrorType.AlreadyDeclared);
        }
    }
}
