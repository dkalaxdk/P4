package sw417f20.ebal.ContextAnalysis;

public class Symbol {

    public String Name;
    public SymbolType Type;
    public enum SymbolType{
        EVENT, PIN, FLOAT, INT, BOOL, FILTERTYPE, IO, PINTYPE, VOID
    }

    public Symbol(){}

    public Symbol(String name, SymbolType type){
        Name = name;
        Type = type;
    }
}
