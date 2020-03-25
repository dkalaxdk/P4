package Tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import sw417f20.ebal.SyntaxAnalysis.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;
import static sw417f20.ebal.SyntaxAnalysis.RecursiveDescent.*;

class ParserTest {


    Parser createParser(String program) {
        StringReader stringReader = new StringReader(program);
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        Reader reader = new Reader(bufferedReader);
        Scanner scanner = new Scanner(reader);

        return new Parser(scanner);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void Start_MasterIsNull_ReturnErrorNode() {
        // Arrange
        String program = "BEGIN SLAVE Initiate { } END SLAVE";
        Parser parser = createParser(program);

        // Act
        try {
            Node node = parser.Master();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }
}