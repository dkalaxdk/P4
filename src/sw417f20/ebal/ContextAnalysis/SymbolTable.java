package sw417f20.ebal.ContextAnalysis;

import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class SymbolTable {

    public abstract SymbolTable OpenScope();
    public abstract SymbolTable CloseScope();
    public abstract void EnterSymbol(String name, Symbol.SymbolType type);
    public abstract void EnterSymbol(String name, Symbol.SymbolType type, Node reference);
    public abstract void EnterSymbol(String name, Symbol.SymbolType type, Symbol.SymbolType valueType);
    //    public abstract boolean EnterSymbol(String name, Symbol.SymbolType type, PinSymbol.PinType pinType, PinSymbol.IOType ioType, int pinNumber);
    public abstract Symbol RetrieveSymbol(String name);
    public abstract boolean DeclaredLocally(String name);
    public abstract SymbolTable GetGlobalScope ();
    public abstract void EnterSymbolAtRoot(String name, Symbol.SymbolType type, Symbol.SymbolType valueType);
    }
