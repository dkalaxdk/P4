package sw417f20.ebal.ContextAnalysis;

import sw417f20.ebal.Visitors.SemanticVisitor;

import java.util.ArrayList;
import java.util.Hashtable;

public class SymbolTable implements ISymbolTable{

    private int tableSize = 101;
    private SymbolTable Parent;
    private ArrayList<SymbolTable> Children = new ArrayList<>();
    private ArrayList<Symbol> hashtable = new ArrayList<>();
    private SemanticVisitor Visitor = new SemanticVisitor();

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
        // Checks that name is not null and that symbol is not already in hashtable
        if (name != null && !DeclaredLocally(name)) {
            // If empty space is found, insert symbol
            int index = FindFreeIndex(name);
            if(index >= 0) {
                hashtable.set(index, new Symbol(name, type));
            }
        }
    }

    // Finds the index of the first free space in the hashtable based on the hash value of the input string
    private int FindFreeIndex(String name){
        int index = GetHashValue(name);
        int count = 0;
        boolean indexIsFree = hashtable.get(index) != null;

        // Looks for empty space in hashtable
        while(!indexIsFree && count < tableSize){
            index = (index + 1) % tableSize;
            indexIsFree = hashtable.get(index) != null;
            count++;
        }
        if (count < tableSize){
            return index;
        }
        else{
            return -1;
        }
    }

    // Returns the hashcode of the input string modulo the size of the hashtable
    private int GetHashValue(String name){
        return name.hashCode()%tableSize;
    }

    @Override
    public Symbol RetrieveSymbol(String name) {
        if (name != null && DeclaredLocally(name)) {
            return hashtable.get(FindIndex(name));
        }
        return null;
    }

    // Finds the index of the Symbol in the hashtable with the same name as the input string
    private int FindIndex(String name){
        int index = GetHashValue(name);
        int count = 0;

        // Looks for index for entry in hashtable matching input string
        while(!name.equals(hashtable.get(index).Name) && count < tableSize){
            index = (index + 1) % tableSize;
            count++;
        }
        if (count < tableSize){
            return index;
        }
        else{
            return -1;
        }
    }

    @Override
    public boolean DeclaredLocally(String name) {
        if (FindIndex(name) >= 0){
            return true;
        }
        else{
            return false;
        }
    }

}
