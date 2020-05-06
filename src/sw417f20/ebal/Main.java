package sw417f20.ebal;

import sw417f20.ebal.CodeGeneration.Strategies.StrategyFactory;
import sw417f20.ebal.ContextAnalysis.ISymbolTable;
import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.Exceptions.SyntaxException;
import sw417f20.ebal.CodeGeneration.OutputFileGenerator;
import sw417f20.ebal.SyntaxAnalysis.*;
import sw417f20.ebal.SyntaxAnalysis.Reader;
import sw417f20.ebal.Visitors.CodeGenerationStrategiesVisitor;
import sw417f20.ebal.Visitors.HashSymbolTablePrinter;
import sw417f20.ebal.Visitors.SemanticsStrategiesVisitor;

import java.io.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws IOException {
//        ScannerStuff();
//        ParserStuff();
//        SemanticsStuff();
//        CodeGenStuff();

        String[] a = {"/TestFiles/CompilerTest.txt"};
        RealMain(a);
    }

    public static void RealMain(String[] args) throws FileNotFoundException {
        boolean debug = false;
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
            AST = parser.Parse(debug);

            // Semantics
            SemanticsStrategiesVisitor visitor = new SemanticsStrategiesVisitor();
            ISymbolTable symbolTable = visitor.Run(AST);

            AST.CheckSemantics();

            if (debug) {
                HashSymbolTablePrinter printer = new HashSymbolTablePrinter();
                printer.PrintTable(symbolTable);
            }

            // Codegeneration
            StrategyFactory codeGenFactory = new StrategyFactory();
            CodeGenerationStrategiesVisitor codeGenVisitor = new CodeGenerationStrategiesVisitor(codeGenFactory);

            codeGenVisitor.Visit(AST);

            ArduinoSystem system = new ArduinoSystem(AST);
            system.Generate();
            HashMap<String, String> files = system.Print();

            String[] n = args[0].split("/");

            String sourceFile;

            if (n[n.length - 1].contains(".")) {
                String[] m = n[n.length - 1].split("\\.");
                sourceFile = m[m.length - 2];
            }
            else {
                sourceFile = n[n.length - 1];
            }

            OutputFileGenerator generator = new OutputFileGenerator(files, sourceFile);

            System.out.println("\nRuntime: " + (System.currentTimeMillis()-start) + " ms \n");
        }

        catch (SyntaxException | SemanticsException e) {
            System.err.println(e.getMessage());
        }
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
            root = parser.Parse(false);
            System.out.println("Runtime: " + (System.currentTimeMillis() - start) + " ms \n");
        }
        catch (SyntaxException e) {

            System.err.println(e.getMessage());
        }
        SemanticsStrategiesVisitor visitor = new SemanticsStrategiesVisitor();
        symbolTable = visitor.Run(root);
        try {
            if (root != null) {
                root.CheckSemantics();
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

//    public static void CodeGenStuff() {
//        ArrayList<String> fileContents = new ArrayList<String>();
//        fileContents.add("This is the Master file");
//        fileContents.add("This is the first Slave file");
//        fileContents.add("This is the second Slave file");
//
//        try {
//            OutputFileGenerator outGen = new OutputFileGenerator(fileContents);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
