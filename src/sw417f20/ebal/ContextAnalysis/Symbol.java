package sw417f20.ebal.ContextAnalysis;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class Symbol {

    public String Name;
    public SymbolType Type; //TODO: ænder til DataType
    public Node ReferenceNode = null;
    public SymbolType ValueType = null;
    public enum SymbolType{
        EVENT, PIN, FLOAT, INT, BOOL, VOID, SLAVE
    }

    public Symbol(){}

    public Symbol(String name, SymbolType type){
        Name = name;
        Type = type;
    }

    public Symbol(String name, SymbolType type, Node reference){
        Name = name;
        Type = type;
        ReferenceNode = reference;
    }

    public Symbol(String name, SymbolType type, SymbolType valueType){
        Name = name;
        Type = type;
        ValueType =valueType;
    }
}
