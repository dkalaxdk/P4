package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SemanticsEventHandlerCallStrategy extends SemanticsCheckerStrategy{

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        // Determine what function is called, then check semantics
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
            // Check semantics for pin and integer parameter
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
            // Check that parameter is an available pin or event
            if (parameter.DataType != Symbol.SymbolType.EVENT || parameter.Name.equals(AvailablePinOrEvent)) {
                if (parameter.DataType == Symbol.SymbolType.PIN) {
                    node.DataType = Symbol.SymbolType.INT;
                }
                else if (parameter.DataType == Symbol.SymbolType.EVENT) {
                    node.DataType = parameter.ValueType;
                }
                else {
                    MakeError(node, node.FirstChild.Next.Value, ErrorType.WrongType);
                }
            }
            else {
                MakeError(node, "Event unavailable to EventHandler");
            }
        }
        else {
            MakeError(node, node.FirstChild.Next.Value, ErrorType.NotDeclared);
        }
    }
}
