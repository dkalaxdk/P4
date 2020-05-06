package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SemanticsPinDeclarationStrategy extends SemanticsCheckerStrategy{

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        if (!SymbolTable.DeclaredLocally(node.FirstChild.Value)){
            Node expression = node.FirstChild.Next;
            if (expression.Type == Node.NodeType.Call && expression.FirstChild.Type == Node.NodeType.CreatePin){
                expression.CheckSemantics();
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


}
