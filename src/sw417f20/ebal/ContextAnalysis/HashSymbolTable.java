package sw417f20.ebal.ContextAnalysis;

import sw417f20.ebal.SyntaxAnalysis.AST;
import sw417f20.ebal.Visitors.SemanticVisitor;

import java.util.ArrayList;
import java.util.HashMap;

public class HashSymbolTable extends SymbolTable{

    private HashSymbolTable Parent;
    private ArrayList<HashSymbolTable> Children = new ArrayList<>();
    private HashMap<String, Symbol> hashtable = new HashMap<>();
    private SemanticVisitor Visitor = new SemanticVisitor();

    @Override
    public HashSymbolTable OpenScope() {
        HashSymbolTable NewChild = new HashSymbolTable();
        Children.add(NewChild);
        NewChild.Parent = this;
        return NewChild;
    }

    @Override
    public HashSymbolTable CloseScope() {
        return Parent;
    }

    @Override
    public void EnterSymbol(String name, String type) {
        // Checks that name is not null and that symbol is not already in hashtable
        if (!DeclaredLocally(name)) {
            hashtable.put(name, new Symbol(name, type));
        }
        else {
            // TODO:hvordan håndterer vi semantiske fejl?
            System.err.println(name + " already declared.");
            System.exit(0);
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
}
