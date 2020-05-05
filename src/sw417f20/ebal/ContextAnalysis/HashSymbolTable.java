package sw417f20.ebal.ContextAnalysis;

import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;
import java.util.HashMap;

public class HashSymbolTable implements ISymbolTable {

    public HashSymbolTable GlobalScope;
    public HashSymbolTable CurrentScope;
    public HashSymbolTable Parent;
    public ArrayList<HashSymbolTable> Children;
    public HashMap<String, Symbol> Hashtable;

    public HashSymbolTable(){
        GlobalScope = this;
        CurrentScope = this;
        Children = new ArrayList<>();
        Hashtable = new HashMap<>();
    }

    @Override
    public void OpenScope() {
        HashSymbolTable NewChild = new HashSymbolTable();
        NewChild.Parent = GlobalScope.CurrentScope;
        NewChild.GlobalScope = GlobalScope;

        GlobalScope.CurrentScope.Children.add(NewChild);
        GlobalScope.CurrentScope = NewChild;
    }

    @Override
    public void CloseScope() {
         GlobalScope.CurrentScope = GlobalScope.CurrentScope.Parent;
    }

    @Override
    public void EnterSymbol(String name, Symbol.SymbolType type) {
        // Checks that symbol is not already in hashtable
        if (!DeclaredLocally(name)) {
            GlobalScope.CurrentScope.Hashtable.put(name, new Symbol(name, type));
        }
    }

    @Override
    public void EnterSymbol(String name, Symbol.SymbolType type, boolean hasBeenInstantiated) {
        // Checks that symbol is not already in hashtable
        if (!DeclaredLocally(name)) {
            GlobalScope.CurrentScope.Hashtable.put(name, new Symbol(name, type, hasBeenInstantiated));
        }
    }

    //TODO: Er den her nødvendig ift codegen?
    @Override
    public void EnterSymbol(String name, Symbol.SymbolType type, Node reference) {
        // Checks that symbol is not already in hashtable
        if (!DeclaredLocally(name)) {
            GlobalScope.CurrentScope.Hashtable.put(name, new Symbol(name, type, reference));
        }
    }

    @Override
    public void EnterSymbol(String name, Symbol.SymbolType type, Symbol.SymbolType valueType) {
        // Checks that symbol is not already in hashtable
        if (!DeclaredLocally(name)) {
            GlobalScope.CurrentScope.Hashtable.put(name, new Symbol(name, type, valueType));
        }
    }

    @Override
    public Symbol RetrieveSymbol(String name) {
        if (DeclaredLocally(name)) {
            return GlobalScope.CurrentScope.Hashtable.get(name);
        }
        else{
            HashSymbolTable temp = GlobalScope.CurrentScope.Parent;
            while(temp != null){
                if (temp.Hashtable.containsKey((name))){
                    return temp.Hashtable.get(name);
                }
                temp = temp.Parent;
            }
        }
        return null;
    }

    @Override
    public boolean DeclaredLocally(String name) {
        return GlobalScope.CurrentScope.Hashtable.containsKey(name);
    }

    @Override
    public void EnterGlobalSymbol(Symbol symbol){
        // Checks that symbol is not already in hashtable
        if (!DeclaredLocally(symbol.Name)) {
            GlobalScope.Hashtable.put(symbol.Name, symbol);
        }
    }
}
