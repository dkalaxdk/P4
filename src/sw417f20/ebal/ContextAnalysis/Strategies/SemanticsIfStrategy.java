package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SemanticsIfStrategy extends SemanticsCheckerStrategy{

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        // Check semantics for the expression containing the conditions
        node.FirstChild.checkSemantics();
        if(node.FirstChild.DataType == Symbol.SymbolType.BOOL){
            // Check semantics for the block of the if statement
            node.FirstChild.Next.checkSemantics();
            Node elseStmt = node.FirstChild.Next.Next;
            if (!elseStmt.isEmpty()){
                // Check semantics for the else statement
                elseStmt.checkSemantics();
            }
        }
        else{
            MakeError(node, "If statement requires boolean expression");
        }
    }
}
