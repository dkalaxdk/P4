package sw417f20.ebal.ContextAnalysis;

public abstract class SymbolTable {
    public abstract HashSymbolTable OpenScope();
    public abstract HashSymbolTable CloseScope();
    public abstract void EnterSymbol(String name, String type);
    public abstract Symbol RetrieveSymbol(String name);
    public abstract boolean DeclaredLocally(String name);
    }
