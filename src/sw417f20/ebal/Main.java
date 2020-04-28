package sw417f20.ebal;

import sw417f20.ebal.ContextAnalysis.HashSymbolTable;
import sw417f20.ebal.ContextAnalysis.StaticSemanticsChecker;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.Exceptions.SyntaxException;
import sw417f20.ebal.SyntaxAnalysis.*;
import sw417f20.ebal.SyntaxAnalysis.Reader;
import sw417f20.ebal.Visitors.HashSymbolTablePrinter;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
//       ScannerStuff();
//        ParserStuff();
        SemanticsStuff();
    }

    public static void ParserStuff() throws FileNotFoundException {
        boolean debug = true;

        try {
            String filePath = new File("").getAbsolutePath();
            String fileInput = filePath + "/TestFiles/SmallParserTestProgram.txt";

            FileReader fileReader = new FileReader(fileInput);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            Reader reader = new Reader(bufferedReader);
            Scanner scanner = new Scanner(reader);

            Parser parser = new Parser(scanner, fileInput);

            long start = System.currentTimeMillis();
            parser.Parse(debug);
            System.out.println("Runtime: " + (System.currentTimeMillis()-start) + " ms \n");

            filePath = new File("").getAbsolutePath();
            fileInput = filePath + "/TestFiles/ParserTestProgram.txt";


            fileReader = new FileReader(fileInput);
            bufferedReader = new BufferedReader(fileReader);
            reader = new Reader(bufferedReader);
            scanner = new Scanner(reader);

            parser = new Parser(scanner, fileInput);

            start = System.currentTimeMillis();
            parser.Parse(debug);
            System.out.println("Runtime: " + (System.currentTimeMillis()-start) + " ms \n");


            filePath = new File("").getAbsolutePath();
            fileInput = filePath + "/TestFiles/TestAllTheThings.txt";

            fileReader = new FileReader(fileInput);
            bufferedReader = new BufferedReader(fileReader);
            reader = new Reader(bufferedReader);
            scanner = new Scanner(reader);

            parser = new Parser(scanner, fileInput);

            start = System.currentTimeMillis();
            parser.Parse(debug);
            System.out.println("Runtime: " + (System.currentTimeMillis()-start) + " ms \n");


//            filePath = new File("").getAbsolutePath();
//            fileInput = filePath + "/TestFiles/DeclarationsTest.txt";
//
//            fileReader = new FileReader(fileInput);
//            bufferedReader = new BufferedReader(fileReader);
//            reader = new Reader(bufferedReader);
//            scanner = new Scanner(reader);
//
//            parser = new Parser(scanner);
//
//            start = System.currentTimeMillis();
//            parser.Parse();
//            System.out.println("Runtime: " + (System.currentTimeMillis()-start) + " ms \n");
        }
        catch (SyntaxException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void ScannerStuff() throws IOException {
        long start = System.currentTimeMillis();

        System.out.println();
        String filePath = new File("").getAbsolutePath();
        String fileInput = filePath + "/TestFiles/ScannerTestProgram.txt";

        FileReader fileReader = new FileReader(fileInput);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        Reader reader = new Reader(bufferedReader);
        Scanner scanner = new Scanner(reader);
        int tokenCount = 0;

        while (scanner.currentToken.type != Token.Type.EOF) {
            scanner.Advance();

            if (scanner.currentToken.type != Token.Type.NOTATOKEN && scanner.currentToken.type != Token.Type.ERROR) {
                System.out.println("Token found: " + scanner.currentToken.type + " on line: " + scanner.currentToken.lineNumber + " : " + scanner.currentToken.offSet + " with content: " + scanner.currentToken.content);
                tokenCount++;
            } else if (scanner.currentToken.type == Token.Type.ERROR){
                System.out.println("Unable to find token matching: " + scanner.currentToken.type + " on line: " + scanner.currentToken.lineNumber + " : " + scanner.currentToken.offSet + " with content: " + scanner.currentToken.content);
            }


        }
        System.out.println();
        System.out.println("Tokens found in file: " + tokenCount);
        System.out.println("Runtime: " + (System.currentTimeMillis()-start) + " ms");
    }

    public static String GetFullPath(String file) {
        String filePath = new File("").getAbsolutePath();
        return filePath + file;
    }

    public static void SemanticsStuff() throws FileNotFoundException{
        Node root;
        try {
            String filePath = new File("").getAbsolutePath();
            String fileInput = filePath + "/TestFiles/SemanticsErrorTest.txt";

            FileReader fileReader = new FileReader(fileInput);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            Reader reader = new Reader(bufferedReader);
            Scanner scanner = new Scanner(reader);

            Parser parser = new Parser(scanner, fileInput);

            long start = System.currentTimeMillis();
            root = parser.Parse(false);
            System.out.println("Runtime: " + (System.currentTimeMillis() - start) + " ms \n");

            StaticSemanticsChecker checker = new StaticSemanticsChecker();
            HashSymbolTable table = (HashSymbolTable)checker.Run(root);
            HashSymbolTablePrinter printer = new HashSymbolTablePrinter();
            printer.PrintTable(table);
        }
        catch (SyntaxException | SemanticsException e) {
            System.err.println(e.getMessage());
        }
    }
}
