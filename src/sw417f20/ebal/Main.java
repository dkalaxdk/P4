package sw417f20.ebal;

import sw417f20.ebal.CodeGeneration.OutputFileGenerator;
import sw417f20.ebal.SyntaxAnalysis.*;
import sw417f20.ebal.SyntaxAnalysis.Reader;

import java.io.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {
//       ScannerStuff();
        //ParserStuff();
        CodeGenStuff();
    }

    public static void ParserStuff() throws FileNotFoundException {
        try {
            String filePath = new File("").getAbsolutePath();
            String fileInput = filePath + "/TestFiles/SmallParserTestProgram.txt";

            FileReader fileReader = new FileReader(fileInput);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            Reader reader = new Reader(bufferedReader);
            Scanner scanner = new Scanner(reader);

            Parser parser = new Parser(scanner);

            long start = System.currentTimeMillis();
            parser.Parse();
            System.out.println("Runtime: " + (System.currentTimeMillis()-start) + " ms");

            filePath = new File("").getAbsolutePath();
            fileInput = filePath + "/TestFiles/ParserTestProgram.txt";

            fileReader = new FileReader(fileInput);
            bufferedReader = new BufferedReader(fileReader);
            reader = new Reader(bufferedReader);
            scanner = new Scanner(reader);

            parser = new Parser(scanner);

            start = System.currentTimeMillis();
            parser.Parse();
            System.out.println("Runtime: " + (System.currentTimeMillis()-start) + " ms");
        }
        catch (RecursiveDescent.SyntaxException e) {
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

    public static void CodeGenStuff() {
        ArrayList<String> fileContents = new ArrayList<String>();
        fileContents.add("This is the Master file");
        fileContents.add("This is the first Slave file");
        fileContents.add("This is the second Slave file");

        try {
            OutputFileGenerator outGen = new OutputFileGenerator(fileContents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
