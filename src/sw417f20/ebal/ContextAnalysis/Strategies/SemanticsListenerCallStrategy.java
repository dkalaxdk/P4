package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SemanticsListenerCallStrategy extends SemanticsCheckerStrategy{

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        switch (node.FirstChild.Type){
            case Broadcast:
                CheckBroadcast(node);
                break;
            case GetValue:
                CheckGetValue(node);
                break;
            case FilterNoise:
                CheckFilterNoise(node);
                break;
            case CreateEvent:
                CheckCreateEvent(node);
                break;
            default:
                MakeError(node, "Illegal function call in Listener");
                break;
        }
    }

    private void CheckBroadcast(Node node) throws SemanticsException {
        Symbol parameter = SymbolTable.RetrieveSymbol(node.FirstChild.Next.Value);
        if (parameter != null) {
            if (parameter.DataType == Symbol.SymbolType.EVENT) {
                node.DataType = Symbol.SymbolType.VOID;
            } else {
                MakeError(node, node.FirstChild.Next.DataType.toString(), ErrorType.WrongType);
            }
        }
        else {
            MakeError(node, node.FirstChild.Next.Value, ErrorType.NotDeclared);
        }
    }

    private void CheckGetValue(Node node) throws SemanticsException {
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

    private void CheckFilterNoise(Node node) throws SemanticsException {
        Symbol pinParameter = SymbolTable.RetrieveSymbol(node.FirstChild.Next.Value);
        if (pinParameter != null){
            if (pinParameter.DataType == Symbol.SymbolType.PIN){
                CheckPinAndFilterCombination(pinParameter, node);
                node.DataType = Symbol.SymbolType.INT;
            }
            else {
                MakeError(node, node.FirstChild.Next.Value, ErrorType.WrongType);
            }
        }
        else {
            MakeError(node, node.FirstChild.Next.Value, ErrorType.NotDeclared);
        }
    }

    // Checks that the combination of pin type and filter type for the FilterNoise() function is legal
    // Allowed combinations:
    //   Digital: Flip, Constant
    //   Analog: Range
    private void CheckPinAndFilterCombination(Symbol symbol, Node node) throws SemanticsException{
        Node.NodeType pinType = symbol.ReferenceNode.Type;
        Node.NodeType filterType = node.FirstChild.Next.Next.Type;

        if (pinType == Node.NodeType.Digital && filterType == Node.NodeType.Range){
            MakeError(node, "Noise from digital pin cannot be filtered as ranged");
        }
        else if (pinType == Node.NodeType.Analog && filterType != Node.NodeType.Range){
            MakeError(node, "Noise from analog pin can only be filtered as ranged");
        }
    }

    private void CheckCreateEvent(Node node) throws SemanticsException {
        Node parameter = node.FirstChild.Next;
        parameter.CheckSemantics();
        if (parameter.DataType == Symbol.SymbolType.INT ||
                parameter.DataType == Symbol.SymbolType.FLOAT ||
                parameter.DataType == Symbol.SymbolType.BOOL) {
            node.DataType = Symbol.SymbolType.EVENT;
        }
        else {
            MakeError(node, parameter.Value, ErrorType.WrongType);
        }
    }

}
