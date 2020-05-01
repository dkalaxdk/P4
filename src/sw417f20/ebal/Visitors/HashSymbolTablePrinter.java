package sw417f20.ebal.Visitors;

import sw417f20.ebal.ContextAnalysis.HashSymbolTable;
import sw417f20.ebal.ContextAnalysis.Symbol;

public class HashSymbolTablePrinter {

    int indent = -1;

    public void PrintTable(HashSymbolTable table){
        PrintSymbols(table);
        System.out.println("");

        indent++;
        for (HashSymbolTable child : table.Children){
            PrintTable(child);
        }
        indent--;
    }

    private void PrintSymbols (HashSymbolTable table){
        for (String name : table.Hashtable.keySet()){
            Symbol symbol = table.RetrieveSymbol(name);
            for (int i = 0; i <= indent; i++) {
                System.out.print("\t");
            }
            System.out.println(symbol.toString());
        }
    }
}
