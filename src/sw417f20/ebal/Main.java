package sw417f20.ebal;

import sw417f20.ebal.SyntaxAnalysis.*;
import sw417f20.ebal.SyntaxAnalysis.Reader;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        ScannerStuff();
        //ParserStuff();
    }

    public static void ParserStuff() throws FileNotFoundException {
        Parser parser = new Parser();

        Scanner scanner = createScanner();

        long start = System.currentTimeMillis();
        parser.Parse(scanner);
        System.out.println("Runtime: " + (System.currentTimeMillis()-start) + " ms");

        start = System.currentTimeMillis();
        parser.Parse(scanner);
        System.out.println("Runtime: " + (System.currentTimeMillis()-start) + " ms");
    }

    public static void ScannerStuff() throws IOException {
        long start = System.currentTimeMillis();

        System.out.println();
        Scanner scanner = createScanner();
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

    private static Scanner createScanner() throws FileNotFoundException {
        String filePath = new File("").getAbsolutePath();
        String fileInput = filePath + "/TestFiles/TestProgram.txt";

        FileReader fileReader = new FileReader(fileInput);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        Reader reader = new Reader(bufferedReader);
        return new Scanner(reader, new Tokenizer());
    }

    public static String GetFullPath(String file) {
        String filePath = new File("").getAbsolutePath();
        return filePath + file;
    }
}
