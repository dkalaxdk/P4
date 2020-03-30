package sw417f20.ebal.ContextAnalysis;

import sw417f20.ebal.SyntaxAnalysis.AST;
import sw417f20.ebal.Visitors.SemanticVisitor;

import java.util.ArrayList;
import java.util.HashMap;

public class HashSymbolTable extends SymbolTable{

    private HashSymbolTable GlobalScope = this;
    private HashSymbolTable Parent;
    private ArrayList<HashSymbolTable> Children = new ArrayList<>();
    private HashMap<String, Symbol> hashtable = new HashMap<>();

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
    public boolean EnterSymbol(String name, Symbol.SymbolType type) {
        // Checks that name is not null and that symbol is not already in hashtable
        if (!DeclaredLocally(name)) {
            hashtable.put(name, new Symbol(name, type));
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean EnterSymbol(String name, Symbol.SymbolType type, PinSymbol.IOType ioType) {
        // Checks that name is not null and that symbol is not already in hashtable
        if (!DeclaredLocally(name)) {
            hashtable.put(name, new PinSymbol(name, type, ioType));
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public Symbol RetrieveSymbol(String name) {
        HashSymbolTable symTab = this;
        if (DeclaredLocally(name)) {
            return hashtable.get(name);
        }
        else if (symTab.Parent != null){
                symTab = symTab.Parent;
                return symTab.RetrieveSymbol(name);
        }
        return null;
    }

    @Override
    public boolean DeclaredLocally(String name) {
        return hashtable.containsKey(name);
    }

    @Override
    public HashSymbolTable GetGlobalScope (){
        return GlobalScope;
    }
}
