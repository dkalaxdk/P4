package sw417f20.ebal.SyntaxAnalysis;

import sw417f20.ebal.Visitors.PrintVisitor;

import java.io.IOException;

public abstract class RecursiveDescent {
    private Scanner PScanner;
    private AST Tree;

    public RecursiveDescent(Scanner scanner) {
        PScanner = scanner;
    }

    public AST Parse() throws SyntaxException{
        if (PScanner == null) {
            return null;
        }

        Tree = new AST();

        Tree.Root = Start();

        Expect(Token.Type.EOF);

        PrintVisitor printVisitor = new PrintVisitor();

        printVisitor.Visit(Tree.Root);

        System.out.println("======== Parse successful ========");

        return Tree;
    }

    public abstract Node Start() throws SyntaxException;

    protected Token Peek() {
        return PScanner.Peek();
    }

    protected Token Expect(Token.Type t) throws SyntaxException {
        return Expect(t, "Expected [" + t + "]");
    }

    protected Token Expect(Token.Type t, String message) throws SyntaxException {
        Token token = Peek();

        if (token.type != t) {
            MakeError(message);
        }
        else {
            try {
                PScanner.Advance();
            }
            catch (IOException e) {
                System.err.println(e);
            }
        }

        return token;
    }

    protected void MakeError(String message) throws SyntaxException {
        throw new SyntaxException(message +
                            " on line: " + PScanner.nextToken.lineNumber +
                            " : " + PScanner.nextToken.offSet + ", got [" + PScanner.nextToken.type + "] with content < " + PScanner.nextToken.content + " > " +
                            "(Current is [" + PScanner.currentToken.type + "] with content < " + PScanner.currentToken.content + " >)");
    }

    public static class SyntaxException extends Exception {

        public SyntaxException(String message) {
            super(message);
        }
    }
}
