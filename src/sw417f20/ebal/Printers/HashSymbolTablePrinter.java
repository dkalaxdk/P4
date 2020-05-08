package sw417f20.ebal.Printers;

import sw417f20.ebal.ContextAnalysis.HashSymbolTable;
import sw417f20.ebal.ContextAnalysis.ISymbolTable;
import sw417f20.ebal.ContextAnalysis.Symbol;

public class HashSymbolTablePrinter {

    int indent = -1;

    public void PrintTable(ISymbolTable table){
        PrintSymbols((HashSymbolTable) table);
        System.out.println("------");

        indent++;
        for (HashSymbolTable child : ((HashSymbolTable)table).Children){
            PrintTable(child);
        }
        indent--;
    }

    private void PrintSymbols (HashSymbolTable table){
        for (String name : table.Hashtable.keySet()){
            Symbol symbol = table.Hashtable.get(name);

            for (int i = 0; i <= indent; i++) {
                System.out.print("\t");
            }
            if (symbol != null){
                System.out.println(symbol.toString());
            }
        }
    }
}
