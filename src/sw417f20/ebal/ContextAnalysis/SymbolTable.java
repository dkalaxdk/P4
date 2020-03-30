package sw417f20.ebal.ContextAnalysis;

public abstract class SymbolTable {
    public abstract HashSymbolTable OpenScope();
    public abstract HashSymbolTable CloseScope();
    public abstract boolean EnterSymbol(String name, Symbol.SymbolType type);
    public abstract boolean EnterSymbol(String name, Symbol.SymbolType type, PinSymbol.IOType ioType);
    public abstract Symbol RetrieveSymbol(String name);
    public abstract boolean DeclaredLocally(String name);
    public abstract HashSymbolTable GetGlobalScope ();
    }
