package sw417f20.ebal;

import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Calendar;
import java.util.Currency;

public class Main {

    public static void main(String[] args) throws IOException {
        ScannerStuff();
//        ParserStuff();
    }

    public static void ParserStuff() {
        String filePath = new File("").getAbsolutePath();
        String fileInput = filePath + "/TestFiles/TestProgram.txt";

        Parser parser = new Parser();
        parser.Parse(fileInput);
    }

    public static void ScannerStuff() {
        long start = System.currentTimeMillis();
        System.out.println();
        String filePath = new File("").getAbsolutePath();
        String fileInput = filePath + "/TestFiles/TestProgram.txt";
        Scanner scanner = new Scanner(fileInput);
        int tokenCount = 0;

        while (scanner.currentToken.type != Token.Type.EOF) {

            if (scanner.nextToken.type != Token.Type.NOTATOKEN) {
                System.out.println("Token found: " + scanner.nextToken.type + " on line: " + scanner.nextToken.lineNumber + " : " + scanner.nextToken.offSet + " with content: " + scanner.nextToken.content);
                tokenCount++;
            } else if (scanner.currentToken.content.length() > 0){
                System.out.println("Unable to find token matching: " + scanner.currentToken.type + " on line: " + scanner.currentToken.lineNumber + " : " + scanner.currentToken.offSet + " with content: " + scanner.currentToken.content);
            }

            try {
                scanner.Advance();
            }
            catch (IOException e) {
                System.err.println(e);
            }
        }
        System.out.println("Tokens found in file: " + tokenCount);
        System.out.println("Runtime:");
        System.out.println(System.currentTimeMillis()-start);
    }
}
