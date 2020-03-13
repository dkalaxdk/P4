package sw417f20.ebal;


import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        String filePath = new File("").getAbsolutePath();
        String fileInput = filePath + "/TestFiles/TestProgram.txt";
        Scanner scanner = new Scanner(fileInput);

        while (scanner.currentToken.type != Token.Type.EOF) {
            scanner.Advance();
            if (scanner.currentToken.type != Token.Type.NOTATOKEN)
            System.out.println("Token found: " + scanner.currentToken.type + " on line: " + scanner.currentToken.lineNumber + " : " + scanner.currentToken.offSet);
        }


    }
}
