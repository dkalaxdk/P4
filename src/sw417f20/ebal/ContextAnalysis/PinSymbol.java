package sw417f20.ebal.ContextAnalysis;

public class PinSymbol extends Symbol {

    public enum IOType{Input, Output}
    public IOType IO;

    public PinSymbol(String name, SymbolType symType, IOType ioType){
        Name = name;
        Type = symType;
        IO = ioType;
    }
}
