package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SemanticsIfStrategy extends SemanticsCheckerStrategy{

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        node.FirstChild.CheckSemantics();
        if(node.FirstChild.DataType == Symbol.SymbolType.BOOL){
            node.FirstChild.Next.CheckSemantics();
            Node elseStmt = node.FirstChild.Next.Next;
            if (!elseStmt.IsEmpty()){
                elseStmt.CheckSemantics();
            }
        }
        else{
            MakeError(node, "If statement requires boolean expression");
        }
    }
}
