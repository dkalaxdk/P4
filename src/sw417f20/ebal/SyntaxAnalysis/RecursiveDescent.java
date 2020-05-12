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


    public Node parse(boolean debug) throws SyntaxException {
        if (scanner == null) {
            return null;
        }

        // Parse the input file
        System.out.println("Parsing: " + currentFile);
        Node root = Start();
        expect(Token.Type.EOF);

        if (debug) {
            printTree(root);
        }

        System.out.println("======== Parse successful ========");

        return root;
    }


    protected abstract Node Start() throws SyntaxException;

    protected Token peek() {
        return scanner.peek();
    }

    protected Token expect(Token.Type t) throws SyntaxException {
        Token token = peek();

        if (token.type != t) {
            makeError("Expected [" + t + "]");
        }

        try {
            scanner.advance();
        }
        catch (IOException e) {
            System.err.println(e);
        }

        return token;
    }

    protected void makeError(String message) throws SyntaxException {
        throw new SyntaxException(message +
                            " on line: " + scanner.nextToken.lineNumber +
                            " : " + scanner.nextToken.offSet +
                            ". Got [" + scanner.nextToken.type + "] with content \"" + scanner.nextToken.content + "\" ");
    }

    protected int getLineNumber() {
        return scanner.reader.currentLine;
    }

    private void printTree(Node tree) {
        ASTPrinter ASTPrinter = new ASTPrinter();
        ASTPrinter.Visit(tree);
    }
}
