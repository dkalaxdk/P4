package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SemanticsFloatLiteralStrategy extends SemanticsCheckerStrategy{

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        node.DataType = Symbol.SymbolType.FLOAT;
        // If literal has a prefix, check that it is prefixMinus
        if (!node.FirstChild.IsEmpty()){
            if (node.FirstChild.Type != Node.NodeType.PrefixMinus){
                MakeError(node, "Only minus prefix (-) applicable to float data type.");
            }
        }
    }
}
