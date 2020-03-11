package sw417f20.ebal;

public class Main {

    public static void main(String[] args) {
	Scanner scanner = new Scanner("FilePath");

	scanner.Peek();
	scanner.Advance();

	scanner.findKeyword();
	scanner.IsIdentifier();
	scanner.IsSingleCharacter();


    }
}
