package sw417f20.ebal.ContextAnalysis;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class Symbol {

    public String Name;
    public SymbolType DataType;
    public Node ReferenceNode = null;
    public SymbolType ValueType = null;
    public enum SymbolType{
        EVENT, PIN, FLOAT, INT, BOOL, VOID, SLAVE
    }

    // Standard symbol constructor
    public Symbol(String name, SymbolType type){
        Name = name;
        DataType = type;
    }

    // Constructor for Pin symbols that need a reference to their PinDeclaration node
    public Symbol(String name, SymbolType type, Node reference){
        Name = name;
        DataType = type;
        ReferenceNode = reference;
    }

    // Constructor for Event symbols that need the type of the value they were created with
    public Symbol(String name, SymbolType type, SymbolType valueType){
        Name = name;
        DataType = type;
        ValueType =valueType;
    }

    @Override
    public String toString(){

        return Name + ", " + DataType + ", " + (ReferenceNode == null ? "false" : "true") + ", " + ValueType;
    }
}
