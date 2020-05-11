package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SemanticsIdentifierStrategy extends SemanticsCheckerStrategy{

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        Symbol identifier = SymbolTable.RetrieveSymbol(node.Value);

        if (identifier != null){
            if (identifier.HasBeenInstantiated){
                node.DataType = identifier.DataType;

                if (!node.FirstChild.IsEmpty()){
                    if (node.FirstChild.Type == Node.NodeType.PrefixMinus && node.DataType == Symbol.SymbolType.BOOL){
                        MakeError(node, "Minus prefix (-) not applicable to boolean data type.");
                    }
                    else if (node.FirstChild.Type == Node.NodeType.PrefixNot && node.DataType != Symbol.SymbolType.BOOL){
                        MakeError(node, "Not prefix (!) only applicable to boolean data type.");
                    }
                }

            }
            else {
                MakeError(node, identifier.Name + " must be instantiated before use.");
            }
        }
        else {
            MakeError(node, node.Value, ErrorType.NotDeclared);
        }
    }
}