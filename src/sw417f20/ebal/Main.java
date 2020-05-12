package sw417f20.ebal;

import sw417f20.ebal.CodeGeneration.Strategies.StrategyFactory;
import sw417f20.ebal.ContextAnalysis.ISymbolTable;
import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.Exceptions.SyntaxException;
import sw417f20.ebal.CodeGeneration.OutputFileGenerator;
import sw417f20.ebal.SyntaxAnalysis.*;
import sw417f20.ebal.SyntaxAnalysis.Reader;
import sw417f20.ebal.CodeGeneration.CodeGenerationStrategyAssigner;
import sw417f20.ebal.Printers.HashSymbolTablePrinter;
import sw417f20.ebal.ContextAnalysis.SemanticsStrategyAssigner;

import java.io.*;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
//        ScannerStuff();
//        ParserStuff();
//        SemanticsStuff();
//        CodeGenStuff();

        String[] a = {"/TestFiles/CompilerTest.txt"};
        RealMain(a);
    }

    public static void RealMain(String[] args) {
        boolean debug = true;
        Node AST = null;

        try {
            long start = System.currentTimeMillis();

            // Parsing
            String filePath = new File("").getAbsolutePath();
            String fileInput = filePath + args[0];

            FileReader fileReader = new FileReader(fileInput);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            Reader reader = new Reader(bufferedReader);
            Scanner scanner = new Scanner(reader);

            Parser parser = new Parser(scanner, fileInput);
            AST = parser.parse(debug);

            // Semantics
            SemanticsStrategyAssigner visitor = new SemanticsStrategyAssigner();
            ISymbolTable symbolTable = visitor.Run(AST);

            AST.checkSemantics();

            if (debug) {
                HashSymbolTablePrinter printer = new HashSymbolTablePrinter();
                printer.PrintTable(symbolTable);
            }

            // Codegeneration
            ArduinoSystem system = new ArduinoSystem(AST);
            StrategyFactory codeGenFactory = new StrategyFactory();
            CodeGenerationStrategyAssigner codeGenAssigner = new CodeGenerationStrategyAssigner(codeGenFactory, system);

            codeGenAssigner.Visit(AST);
//            codeGenAssigner.Run(AST);

            system.Generate();
            HashMap<String, String> files = system.Print();

            String sourceFile = getFileName(args[0]);

            OutputFileGenerator generator = new OutputFileGenerator(files, sourceFile);

            System.out.println("\nRuntime: " + (System.currentTimeMillis()-start) + " ms \n");
        }

        catch (SyntaxException | SemanticsException | IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static String getFileName(String string) {
        String[] n = string.split("/");

        String sourceFile;

        if (n[n.length - 1].contains(".")) {
            String[] m = n[n.length - 1].split("\\.");
            sourceFile = m[m.length - 2];
        }
        else {
            sourceFile = n[n.length - 1];
        }

        return sourceFile;
    }

    public static void ParserStuff() throws FileNotFoundException {
        boolean debug = true;

        try {
            String filePath = new File("").getAbsolutePath();
            String fileInput = filePath + "/TestFiles/ParserTestProgram.txt";

            FileReader fileReader = new FileReader(fileInput);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            Reader reader = new Reader(bufferedReader);
            Scanner scanner = new Scanner(reader);

            Parser parser = new Parser(scanner, fileInput);

            long start = System.currentTimeMillis();
            parser.parse(debug);
            System.out.println("Runtime: " + (System.currentTimeMillis()-start) + " ms \n");

            filePath = new File("").getAbsolutePath();
            fileInput = filePath + "/TestFiles/ParserTestProgram.txt";


            fileReader = new FileReader(fileInput);
            bufferedReader = new BufferedReader(fileReader);
            reader = new Reader(bufferedReader);
            scanner = new Scanner(reader);

            parser = new Parser(scanner, fileInput);

            start = System.currentTimeMillis();
            parser.parse(debug);
            System.out.println("Runtime: " + (System.currentTimeMillis()-start) + " ms \n");


            filePath = new File("").getAbsolutePath();
            fileInput = filePath + "/TestFiles/TestAllTheThings.txt";

            fileReader = new FileReader(fileInput);
            bufferedReader = new BufferedReader(fileReader);
            reader = new Reader(bufferedReader);
            scanner = new Scanner(reader);

            parser = new Parser(scanner, fileInput);

            start = System.currentTimeMillis();
            parser.parse(debug);
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
            scanner.advance();

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
        Node root = null;
        ISymbolTable symbolTable = null;
        try {
            String filePath = new File("").getAbsolutePath();
            String fileInput = filePath + "/TestFiles/SemanticsErrorTest.txt";

            FileReader fileReader = new FileReader(fileInput);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            Reader reader = new Reader(bufferedReader);
            Scanner scanner = new Scanner(reader);

            Parser parser = new Parser(scanner, fileInput);

            long start = System.currentTimeMillis();
            root = parser.parse(false);
            System.out.println("Runtime: " + (System.currentTimeMillis() - start) + " ms \n");
        }
        catch (SyntaxException e) {

            System.err.println(e.getMessage());
        }
        SemanticsStrategyAssigner visitor = new SemanticsStrategyAssigner();
        symbolTable = visitor.Run(root);
        try {
            if (root != null) {
                root.checkSemantics();
            }
//            HashSymbolTablePrinter printer = new HashSymbolTablePrinter();
//            printer.PrintTable(symbolTable);

//            StaticSemanticsChecker checker = new StaticSemanticsChecker();
//            HashSymbolTable table = (HashSymbolTable)checker.Run(root);
//            HashSymbolTablePrinter printer = new HashSymbolTablePrinter();
//            printer.PrintTable(table);
        }
        catch (SemanticsException e) {

            System.err.println(e.getMessage());
        }
        HashSymbolTablePrinter printer = new HashSymbolTablePrinter();
        printer.PrintTable(symbolTable);
    }
}
