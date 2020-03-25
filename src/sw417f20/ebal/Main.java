package sw417f20.ebal;

import sw417f20.ebal.ContextAnalysis.HashSymbolTable;
import sw417f20.ebal.SyntaxAnalysis.AST;
import sw417f20.ebal.SyntaxAnalysis.Parser;
import sw417f20.ebal.SyntaxAnalysis.Scanner;
import sw417f20.ebal.SyntaxAnalysis.Token;
import sw417f20.ebal.Visitors.SemanticVisitor;
import sw417f20.ebal.Visitors.Visitor;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        ScannerStuff();
        AST ast = ParserStuff();
        SymbolTableStuff(ast);
    }

    public static AST ParserStuff() {
        Parser parser = new Parser();

        long start = System.currentTimeMillis();
        parser.Parse(GetFullPath("/TestFiles/SmallTestProgram.txt"));
        System.out.println("Runtime: " + (System.currentTimeMillis()-start) + " ms");

        start = System.currentTimeMillis();
        AST ast = parser.Parse(GetFullPath("/TestFiles/TestProgram.txt"));
        System.out.println("Runtime: " + (System.currentTimeMillis()-start) + " ms");

        return ast;
    }

    public static void ScannerStuff() throws IOException {
        long start = System.currentTimeMillis();

        System.out.println();
        Scanner scanner = new Scanner(GetFullPath("/TestFiles/TestProgram.txt"));
        int tokenCount = 0;

        while (scanner.currentToken.type != Token.Type.EOF) {
            scanner.Advance();

            if (scanner.currentToken.type != Token.Type.NOTATOKEN) {
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

    public static void SymbolTableStuff (AST ast){
        Visitor visitor = new SemanticVisitor();
        visitor.Visit(ast.Root);
    }
}
