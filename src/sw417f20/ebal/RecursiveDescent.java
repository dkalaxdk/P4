package sw417f20.ebal;

import java.io.IOException;

public abstract class RecursiveDescent {
    private Scanner PScanner;
    private AST Tree;
    public RecursiveDescent() {}

    public AST Parse(String filePath) {
        PScanner = new Scanner(filePath);
        Tree = new AST();

        Start();
        Expect(Token.Type.EOF);

        return Tree;
    }

    public abstract void Start();

    protected Token Peek() {
        return PScanner.Peek();
    }

    protected void Expect(Token.Type t) {
        Expect(t, "Expected [" + t + "]");
    }

    protected void Expect(Token.Type t, String message) {
        if (Peek().type != t) {
            MakeError(message);
            System.exit(0);
        }
        else {
            try {
                PScanner.Advance();
            }
            catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    protected void MakeError(String message) {
        System.err.println(message + " on line: " + PScanner.nextToken.lineNumber + " : " + PScanner.nextToken.offSet);
    }
}
