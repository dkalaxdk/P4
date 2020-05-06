package sw417f20.ebal.ContextAnalysis;

public interface ISymbolTable {

    void OpenScope();
    void CloseScope();
    void EnterSymbol(String name, Symbol.SymbolType type);
    void EnterSymbol(String name, Symbol.SymbolType type, boolean hasBeenInstantiated);
    void EnterSymbol(String name, Symbol.SymbolType type, Symbol.SymbolType valueType);
    Symbol RetrieveSymbol(String name);
    boolean DeclaredLocally(String name);
    void EnterGlobalSymbol(Symbol symbol);
}
