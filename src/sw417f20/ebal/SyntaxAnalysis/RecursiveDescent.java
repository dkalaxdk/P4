package sw417f20.ebal.SyntaxAnalysis;

import sw417f20.ebal.Exceptions.SyntaxException;
import sw417f20.ebal.Printers.ASTPrinter;

import java.io.IOException;

public abstract class RecursiveDescent {
    private Scanner scanner;
    private String currentFile;

    public RecursiveDescent(Scanner scanner, String file) {
        this.scanner = scanner;
        currentFile = file;
    }


    public Node Parse(boolean debug) throws SyntaxException {
        if (scanner == null) {
            return null;
        }

        // Parse the input file
        System.out.println("Parsing: " + currentFile);
        Node root = Start();
        Expect(Token.Type.EOF);

        if (debug) {
            PrintTree(root);
        }

        System.out.println("======== Parse successful ========");

        return root;
    }


    protected abstract Node Start() throws SyntaxException;

    protected Token Peek() {
        return scanner.Peek();
    }

    protected Token Expect(Token.Type t) throws SyntaxException {
        Token token = Peek();

        if (token.type != t) {
            MakeError("Expected [" + t + "]");
        }

        try {
            scanner.Advance();
        }
        catch (IOException e) {
            System.err.println(e);
        }

        return token;
    }

    protected void MakeError(String message) throws SyntaxException {
        throw new SyntaxException(message +
                            " on line: " + scanner.nextToken.LineNumber +
                            " : " + scanner.nextToken.OffSet +
                            ". Got [" + scanner.nextToken.type + "] with content \"" + scanner.nextToken.Content + "\" ");
    }

    protected int GetLineNumber() {
        return scanner.reader.currentLine;
    }

    private void PrintTree(Node tree) {
        ASTPrinter ASTPrinter = new ASTPrinter();
        ASTPrinter.Visit(tree);
    }
}
