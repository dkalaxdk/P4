package sw417f20.ebal.ContextAnalysis;

import sw417f20.ebal.SyntaxAnalysis.Node;

public abstract class SymbolTable {
    public abstract HashSymbolTable OpenScope();
    public abstract HashSymbolTable CloseScope();
    public abstract boolean EnterSymbol(String name, Symbol.SymbolType type);
    public abstract boolean EnterSymbol(String name, Symbol.SymbolType type, Node reference);
//    public abstract boolean EnterSymbol(String name, Symbol.SymbolType type, PinSymbol.PinType pinType, PinSymbol.IOType ioType, int pinNumber);
    public abstract Symbol RetrieveSymbol(String name);
    public abstract boolean DeclaredLocally(String name);
    public abstract HashSymbolTable GetGlobalScope ();
    }
