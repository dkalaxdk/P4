package sw417f20.ebal;

import javax.sound.midi.Soundbank;
import java.io.*;
import java.time.Instant;
import java.util.Currency;
import sw417f20.ebal.Reader.Reader;

public class Main {

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        System.out.println();
        String filePath = new File("").getAbsolutePath();
        String fileInput = filePath + "/TestFiles/TestProgram.txt";

        FileReader fileReader = new FileReader(fileInput);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        Reader reader = new Reader(bufferedReader);
        Scanner scanner = new Scanner(reader);
        int tokenCount = 0;

        while (scanner.currentToken.type != Token.Type.EOF) {
            scanner.Advance();
            if (scanner.currentToken.type != Token.Type.NOTATOKEN) {
                System.out.println("Token found: " + scanner.currentToken.type + " on line: " + scanner.currentToken.lineNumber + " : " + scanner.currentToken.offSet + " with content: " + scanner.currentToken.content);
                tokenCount++;
            } else if (scanner.currentToken.content.length() > 0){
                System.out.println("Unable to find token matching: " + scanner.currentToken.type + " on line: " + scanner.currentToken.lineNumber + " : " + scanner.currentToken.offSet + " with content: " + scanner.currentToken.content);
            }
        }
        System.out.println("Tokens found in file: " + tokenCount);
        System.out.println("Runtime:");
        System.out.println(System.currentTimeMillis()-start);

    }
}
