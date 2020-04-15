package sw417f20.ebal.ContextAnalysis;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class Symbol {

    public String Name;
    public SymbolType Type;
    public Node ReferenceNode = null;
    public enum SymbolType{
        EVENT, PIN, FLOAT, INT, BOOL, FILTERTYPE, IO, PINTYPE, VOID
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
}
