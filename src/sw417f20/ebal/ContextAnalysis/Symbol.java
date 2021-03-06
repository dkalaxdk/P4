package sw417f20.ebal.ContextAnalysis;

import sw417f20.ebal.SyntaxAnalysis.Node;


public class Symbol {

    public String Name;
    public SymbolType DataType;

    // Used by pin symbols to save a reference to the node where a pin was declared
    public Node ReferenceNode = null;

    // Used by event symbols to save the data type of the value the event was created with
    public SymbolType ValueType = null;

    // used by symbols representing int, float and bool variables
    // to mark whether or not the variable has been instantiated
    public boolean HasBeenInstantiated = false;

    // The types a symbol can be. Represents data types, except void and slave.
    public enum SymbolType{
        EVENT, PIN, FLOAT, INT, BOOL, VOID, SLAVE
    }

    // Standard symbol constructor
    public Symbol(String name, SymbolType type){
        Name = name;
        DataType = type;
    }

    // Standard symbol constructor, with an option for marking a symbol as instantiated
    public Symbol(String name, SymbolType type, boolean hasBeenInstantiated){
        Name = name;
        DataType = type;
        HasBeenInstantiated = hasBeenInstantiated;
    }

    // Constructor for Pin symbols that need a reference to their PinDeclaration node
    public Symbol(String name, SymbolType type, Node reference){
        Name = name;
        DataType = type;
        ReferenceNode = reference;
        HasBeenInstantiated = true;
    }

    // Constructor for Event symbols that need the type of the value they were created with
    public Symbol(String name, SymbolType type, SymbolType valueType){
        Name = name;
        DataType = type;
        ValueType = valueType;
        HasBeenInstantiated = true;
    }

    @Override
    public String toString(){
        return Name + ", " + DataType + ", " + (ReferenceNode == null ? "false" : "true") + ", " + ValueType + ", " + HasBeenInstantiated;
    }
}
