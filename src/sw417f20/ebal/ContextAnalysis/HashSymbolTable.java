package sw417f20.ebal.ContextAnalysis;

import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;
import java.util.HashMap;

public class HashSymbolTable extends SymbolTable{

    public HashSymbolTable GlobalScope = this;
    public HashSymbolTable Parent;
    public ArrayList<HashSymbolTable> Children = new ArrayList<>();
    public HashMap<String, Symbol> Hashtable = new HashMap<>();

    @Override
    public HashSymbolTable OpenScope() {
        HashSymbolTable NewChild = new HashSymbolTable();
        Children.add(NewChild);
        NewChild.Parent = this;
        NewChild.GlobalScope = this.GlobalScope;
        return NewChild;
    }

    @Override
    public HashSymbolTable CloseScope() {
        return Parent;
    }

    @Override
    public void EnterSymbol(String name, Symbol.SymbolType type) {
        // Checks that symbol is not already in hashtable
        if (!DeclaredLocally(name)) {
            Hashtable.put(name, new Symbol(name, type));
        }
    }

    @Override
    public void EnterSymbol(String name, Symbol.SymbolType type, Node reference) {
        // Checks that symbol is not already in hashtable
        if (!DeclaredLocally(name)) {
            Hashtable.put(name, new Symbol(name, type, reference));
        }
    }

    @Override
    public void EnterSymbol(String name, Symbol.SymbolType type, Symbol.SymbolType valueType) {
        // Checks  that symbol is not already in hashtable
        if (!DeclaredLocally(name)) {
            Hashtable.put(name, new Symbol(name, type, valueType));
        }
    }

    @Override
    public Symbol RetrieveSymbol(String name) {
        HashSymbolTable symTab = this;
        if (DeclaredLocally(name)) {
            return Hashtable.get(name);
        }
        else if (symTab.Parent != null){
                symTab = symTab.Parent;
                return symTab.RetrieveSymbol(name);
        }
        return null;
    }

    @Override
    public boolean DeclaredLocally(String name) {
        return Hashtable.containsKey(name);
    }

    @Override
    public HashSymbolTable GetGlobalScope (){
        return GlobalScope;
    }

    @Override
    public void EnterSymbolAtRoot(String name, Symbol.SymbolType type, Symbol.SymbolType valueType){
        GlobalScope.EnterSymbol(name, type, valueType);
    }


    @Override
    public String toString(){

        return "";
    }
}
