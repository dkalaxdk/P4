package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;

public class SemanticsListenerCallStrategy extends SemanticsCheckerStrategy{

    public ArrayList<Symbol> BroadcastEvents;

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        // Determine what function is called
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
        // Retrieve symbol for parameter from symbol table
        Symbol parameter = SymbolTable.RetrieveSymbol(node.FirstChild.Next.Value);
        if (parameter != null) {
            // Parameter must be an event
            if (parameter.DataType == Symbol.SymbolType.EVENT) {
                node.DataType = Symbol.SymbolType.VOID;
                if (!InBroadcastEvents(parameter.Name)){
                    // Add event to list of BroadcastEvents
                    BroadcastEvents.add(parameter);
                }
            } else {
                MakeError(node, node.FirstChild.Next.DataType.toString(), ErrorType.WrongType);
            }
        }
        else {
            MakeError(node, node.FirstChild.Next.Value, ErrorType.NotDeclared);
        }
    }

    // Determines if a symbol with the given name is in the BroadcastEvents list
    private boolean InBroadcastEvents(String name) {
        for (Symbol symbol : BroadcastEvents){
            if (symbol.Name.equals(name)){
                return true;
            }
        }
        return false;
    }

    private void CheckGetValue(Node node) throws SemanticsException {
        // Retrieve parameter form symbol table
        Symbol parameter = SymbolTable.RetrieveSymbol(node.FirstChild.Next.Value);
        if (parameter != null) {
            // Check that pin or event is available
            if (parameter.DataType != Symbol.SymbolType.PIN || parameter.Name.equals(AvailablePinOrEvent)) {
                if (parameter.DataType == Symbol.SymbolType.PIN) {
                    node.DataType = Symbol.SymbolType.INT;
                }
                // If parameter is event, return type is the type of the event
                else if (parameter.DataType == Symbol.SymbolType.EVENT){
                    node.DataType = parameter.ValueType;
                }
                else {
                    MakeError(node, node.FirstChild.Next.Value, ErrorType.WrongType);
                }
            }
            else {
                MakeError(node, "Pin unavailable to Listener");
            }
        }
        else {
            MakeError(node, node.FirstChild.Next.Value, ErrorType.NotDeclared);
        }
    }

    private void CheckFilterNoise(Node node) throws SemanticsException {
        // Check that pin is available
        if (node.FirstChild.Next.Value.equals(AvailablePinOrEvent)) {
            // Retrieve symbol for pin
            Symbol pinParameter = SymbolTable.RetrieveSymbol(node.FirstChild.Next.Value);
            if (pinParameter != null) {
                if (pinParameter.DataType == Symbol.SymbolType.PIN) {
                    // Check that filterType matches the type of the pin
                    CheckPinAndFilterCombination(pinParameter, node);
                    node.DataType = Symbol.SymbolType.INT;
                } else {
                    MakeError(node, node.FirstChild.Next.Value, ErrorType.WrongType);
                }
            } else {
                MakeError(node, node.FirstChild.Next.Value, ErrorType.NotDeclared);
            }
        }
        else {
            MakeError(node, "Pin unavailable to Listener");
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
        // Check semantics for parameter
        parameter.CheckSemantics();
        // Check that parameter has a legal type
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
