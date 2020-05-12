package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SemanticsBoolLiteralStrategy extends SemanticsCheckerStrategy{

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        node.DataType = Symbol.SymbolType.BOOL;
        // Check if the literal has a prefix
        if (!node.FirstChild.isEmpty()){
            // Prefix must be prefixNot
            if (node.FirstChild.Type != Node.NodeType.PrefixNot){
                MakeError(node, "Only not prefix (!) applicable to bool data type.");
            }
        }
    }
}
