package sw417f20.ebal.ContextAnalysis;

import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;
import java.util.HashMap;

// The HashSymbolTable class implements the ISymbolTable interface using a hashtable
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

    // Opens a new scope in the symbol table by adding a new symbol table
    // and setting the current scope to the new table
    @Override
    public void OpenScope() {
        HashSymbolTable NewChild = new HashSymbolTable();
        NewChild.Parent = GlobalScope.CurrentScope;
        NewChild.GlobalScope = GlobalScope;

        GlobalScope.CurrentScope.Children.add(NewChild);
        GlobalScope.CurrentScope = NewChild;
    }

    // Closes the current scope by setting the current scope to the closed scope's parent
    @Override
    public void CloseScope() {
         GlobalScope.CurrentScope = GlobalScope.CurrentScope.Parent;
    }

    // Enters a symbol into the current scope of the symbol table
    @Override
    public void EnterSymbol(String name, Symbol.SymbolType type) {
        if (!DeclaredLocally(name)) {
            GlobalScope.CurrentScope.Hashtable.put(name, new Symbol(name, type));
        }
    }

    // Enters a symbol into the current scope of the symbol table.
    // Used for variables and includes whether or not the variable has been instantiated.
    @Override
    public void EnterSymbol(String name, Symbol.SymbolType type, boolean hasBeenInstantiated) {
        if (!DeclaredLocally(name)) {
            GlobalScope.CurrentScope.Hashtable.put(name, new Symbol(name, type, hasBeenInstantiated));
        }
    }

    // Enters a symbol into the current scope of the symbol table.
    // Used for events and includes the datatype of the value the event was created with
    @Override
    public void EnterSymbol(String name, Symbol.SymbolType type, Symbol.SymbolType valueType) {
        if (!DeclaredLocally(name)) {
            GlobalScope.CurrentScope.Hashtable.put(name, new Symbol(name, type, valueType));
        }
    }

    // Enters a symbol into the current scope of the symbol table.
    // Used for pins and includes a reference to the node where to pin was declared,
    // in order to provide access to the information used to create the pin
    @Override
    public void EnterSymbol(String name, Symbol.SymbolType type, Node referenceNode) {
        if (!DeclaredLocally(name)) {
            GlobalScope.CurrentScope.Hashtable.put(name, new Symbol(name, type, referenceNode));
        }
    }

    // Retrieves a symbol with the parameter string as name from the symbol table.
    // Returns the found symbol or null, if none is found
    @Override
    public Symbol RetrieveSymbol(String name) {
        // First checks if the symbol is in the current scope, and returns it if it is
        if (DeclaredLocally(name)) {
            return GlobalScope.CurrentScope.Hashtable.get(name);
        }
        // Otherwise it goes through the line of parent up to the global scope.
        // Returns the closest symbol found or null if no symbol is found
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

    // Checks if a symbol with the parameter string as name is in the current scope.
    @Override
    public boolean DeclaredLocally(String name) {
        return GlobalScope.CurrentScope.Hashtable.containsKey(name);
    }

    // Enters a symbol into the hashtable for the global scope.
    @Override
    public void EnterGlobalSymbol(Symbol symbol){
        GlobalScope.Hashtable.put(symbol.Name, symbol);
    }
}
