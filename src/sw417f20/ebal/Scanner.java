package sw417f20.ebal;

import sw417f20.ebal.Reader.Reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Scanner {
    public Reader reader;
    private Tokenizer tokenizer;
    Token currentToken;
    Token nextToken;

    //private BufferedReader bufferedReader;

    /*
    public Scanner(String fileInput) {
        currentToken = new Token(Token.Type.NOTATOKEN, "");
        nextToken = new Token(Token.Type.NOTATOKEN, "");

        {
            try {
                FileReader fileReader = new FileReader(fileInput);
                bufferedReader = new BufferedReader(fileReader);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        this.reader = new Reader(bufferedReader);

        Init();
    }
    */

    public Scanner(Reader reader) {
        currentToken = new Token(Token.Type.NOTATOKEN, "");
        nextToken = new Token(Token.Type.NOTATOKEN, "");

        this.reader = reader;
        tokenizer = new Tokenizer(reader);
        Init();
    }

    public void Init() {
        try {
            nextToken = tokenizer.getToken();
        }
        catch (IOException e) {
            System.err.println(e);
        }
    }

    //Only for public use
    public Token Peek() {
        return nextToken;
    }

    //Only for public use
    public void Advance() throws IOException {
        // Check whether the switch may find another case, ie isDigit, isText or the likes.
        // Depending on the cases, it should be redirected to something in the likes of "Find keyword" or "Find literal"
        Token temp = nextToken;

        do {
            nextToken = tokenizer.getToken();
        }
        while (nextToken.type == Token.Type.NOTATOKEN);

        currentToken = temp;
    }
}
