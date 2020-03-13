package sw417f20.ebal;

import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.IOException;
import java.time.Instant;

public class Main {

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        System.out.println();
        String filePath = new File("").getAbsolutePath();
        String fileInput = filePath + "/TestFiles/TestProgram.txt";
        Scanner scanner = new Scanner(fileInput);
        int tokenCount = 0;

        while (scanner.currentToken.type != Token.Type.EOF) {
            scanner.Advance();
            if (scanner.currentToken.type != Token.Type.NOTATOKEN) {
                System.out.println("Token found: " + scanner.currentToken.type + " on line: " + scanner.currentToken.lineNumber + " : " + scanner.currentToken.offSet + " with content: " + scanner.currentToken.content);
                tokenCount++;
            }
        }
        System.out.println("Tokens found in file: " + tokenCount);
        System.out.println("Runtime:");
        System.out.println(System.currentTimeMillis()-start);

    }
}
