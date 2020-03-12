package sw417f20.ebal;


import java.io.File;

public class Main {

    public static void main(String[] args) {
        String filePath = new File("").getAbsolutePath();
        String fileInput = filePath + "/TestFiles/TestProgram.txt";
        Scanner scanner = new Scanner(fileInput);

        scanner.Peek();


    }
}
