package sw417f20.ebal.ContextAnalysis;

import java.util.ArrayList;

public class SymbolTable implements ISymbolTable{

    private SymbolTable Parent;
    private ArrayList<SymbolTable> Children = new ArrayList<SymbolTable>();

    public SymbolTable BuildSymbolTable(){
        return null;
    }

    @Override
    public SymbolTable OpenScope() {
        SymbolTable NewChild = new SymbolTable();
        Children.add(NewChild);
        NewChild.Parent = this;
        return NewChild;
    }

    @Override
    public SymbolTable CloseScope() {
        return Parent;
    }

    @Override
    public void EnterSymbol(String name, String type) {

    }

    @Override
    public Symbol RetrieveSymbol(String name) {
        return null;
    }

    @Override
    public boolean DeclaredLocally(String name) {
        return false;
    }

    private int GetHashValue(String name){
        return 1;
    }
}
