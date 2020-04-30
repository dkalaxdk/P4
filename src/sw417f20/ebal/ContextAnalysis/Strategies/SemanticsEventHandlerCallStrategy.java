package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SemanticsEventHandlerCallStrategy extends SemanticsCheckerStrategy{

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        if (node.FirstChild.Type == Node.NodeType.Write) {
            CheckWrite(node);
        }
        else if (node.FirstChild.Type == Node.NodeType.GetValue){
            CheckGetValue(node);
        }
        else {
            MakeError(node, "Illegal function call in EventHandler");
        }
    }

    private void CheckWrite(Node node) throws SemanticsException{
        Symbol pinParameter = SymbolTable.RetrieveSymbol(node.FirstChild.Next.Value);
        Node intParameter = node.FirstChild.Next.Next;

        if (pinParameter != null) {
            if (pinParameter.DataType == Symbol.SymbolType.PIN) {
                intParameter.CheckSemantics();
                if (intParameter.DataType == Symbol.SymbolType.INT) {
                    node.DataType = Symbol.SymbolType.VOID;
                }
                else {
                    MakeError(node, "Second parameter", ErrorType.WrongType);
                }
            }
            else {
                MakeError(node, node.FirstChild.Next.Value, ErrorType.WrongType);
            }
        }
        else {
            MakeError(node, node.FirstChild.Next.Value, ErrorType.NotDeclared);
        }
    }

    private void CheckGetValue(Node node) throws SemanticsException{
        Symbol parameter = SymbolTable.RetrieveSymbol(node.FirstChild.Next.Value);
        if (parameter != null) {
            if (parameter.DataType == Symbol.SymbolType.PIN) {
                node.DataType = Symbol.SymbolType.INT;
            }
            else if (parameter.DataType == Symbol.SymbolType.EVENT){
                node.DataType = parameter.ValueType;
            }
            else {
                MakeError(node, node.FirstChild.Next.Value, ErrorType.WrongType);
            }
        }
        else {
            MakeError(node, node.FirstChild.Next.Value, ErrorType.NotDeclared);
        }
    }
}
