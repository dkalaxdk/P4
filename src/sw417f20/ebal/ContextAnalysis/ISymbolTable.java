package sw417f20.ebal.ContextAnalysis;

public interface ISymbolTable {
    SymbolTable OpenScope();
    SymbolTable CloseScope();
    void EnterSymbol(String name, String type);
    Symbol RetrieveSymbol(String name);
    boolean DeclaredLocally(String name);
}
