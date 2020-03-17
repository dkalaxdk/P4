package sw417f20.ebal;

import org.junit.jupiter.params.shadow.com.univocity.parsers.conversions.RegexConversion;

import java.awt.*;
import java.io.IOException;

public abstract class RecursiveDescent {
    private Scanner PScanner;
    private AST Tree;
    private PrintVisitor PrintVisitor;
    public RecursiveDescent() {}

    public AST Parse(String filePath) {
        PScanner = new Scanner(filePath);
        Tree = new AST();

        System.out.println();
        System.out.println("Parsing " + filePath);

//        String[] splt = filePath.split("P4");
//
//        System.out.println(splt[splt.length - 1]);

        Tree.Root = Start();
        Expect(Token.Type.EOF);

        PrintVisitor = new PrintVisitor();

        PrintVisitor.Visit(Tree.Root);

        System.out.println("======== Parse successful ========");

        return Tree;
    }

    public abstract Node Start();

    protected Token Peek() {
        return PScanner.Peek();
    }

    protected Token Expect(Token.Type t) {
        return Expect(t, "Expected [" + t + "]");
    }

    protected Token Expect(Token.Type t, String message) {
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

    protected void MakeError(String message) {
        System.err.println(message + " on line: " + PScanner.nextToken.lineNumber + " : " + PScanner.nextToken.offSet + ", got [" + PScanner.nextToken.type + "] with content < " + PScanner.nextToken.content + " > " +
                           "(Current is [" + PScanner.currentToken.type + "] with content < " + PScanner.currentToken.content + " >)");
        System.exit(0);
    }
}
