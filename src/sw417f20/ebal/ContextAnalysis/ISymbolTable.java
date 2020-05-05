package sw417f20.ebal.ContextAnalysis;

import sw417f20.ebal.SyntaxAnalysis.Node;

public interface ISymbolTable {

    void OpenScope();
    void CloseScope();
    void EnterSymbol(String name, Symbol.SymbolType type);
    void EnterSymbol(String name, Symbol.SymbolType type, boolean hasBeenInstantiated);
    void EnterSymbol(String name, Symbol.SymbolType type, Node reference);
    void EnterSymbol(String name, Symbol.SymbolType type, Symbol.SymbolType valueType);
    Symbol RetrieveSymbol(String name);
    boolean DeclaredLocally(String name);
    void EnterGlobalSymbol(Symbol symbol);
}
