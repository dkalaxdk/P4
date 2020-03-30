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

    @Test
    void Start_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Start();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Start_OnlyBeginEnd_ThrowSyntaxException() {
        // Arrange
        String program = "BEGIN END";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Start();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Start_NoMaster_ThrowSyntaxException() {
        // Arrange
        String program = "BEGIN SLAVE Initiate { } END SLAVE";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Start();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Start_NoSlaves_ThrowSyntaxException() {
        // Arrange
        String program = "BEGIN MASTER Initiate { } END MASTER";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Start();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Start_MinimumProgram_ReturnProgNode() {
        // Arrange
        String program = "BEGIN MASTER Initiate { } END MASTER " +
                         "BEGIN SLAVE Initiate { } END SLAVE";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Start();
        }
        catch (SyntaxException e) {
            fail();
            return;
        }

        // Assert
        assertSame(node.Type, AST.NodeType.Prog);
    }

    @Test
    void Start_MinimumProgram_ReturnedNodeFirstChildIsMaster() {
        // Arrange
        String program = "BEGIN MASTER Initiate { } END MASTER " +
                "BEGIN SLAVE Initiate { } END SLAVE";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Start();
        }
        catch (SyntaxException e) {
            fail();
            return;
        }

        // Assert
        assertSame(node.FirstChild.Type, AST.NodeType.Master);
    }

    @Test
    void Start_MinimalProgram_TwoSlaves_ReturnedNodeThirdChildIsSlave() {
        // Arrange
        String program = "BEGIN MASTER Initiate { } END MASTER " +
                "BEGIN SLAVE Initiate { } END SLAVE " +
                "BEGIN SLAVE Initiate { } END SLAVE ";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Start();
        }
        catch (SyntaxException e) {
            fail();
            return;
        }

        // Assert
        assertSame(node.FirstChild.Next.Next.Type, AST.NodeType.Slave);
    }



    @Test
    void Master_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Master();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Master_OnlyBeginEnd_ThrowSyntaxException() {
        // Arrange
        String program = "BEGIN END";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Master();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Master_NoBegin_ThrowSyntaxException() {
        // Arrange
        String program = "MASTER Initiate { } END MASTER";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Master();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Master_NoEnd_ThrowSyntaxException() {
        // Arrange
        String program = "BEGIN MASTER Initiate { } MASTER";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Master();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Master_NoFirstMaster_ThrowSyntaxException() {
        // Arrange
        String program = "BEGIN Initiate { } END MASTER";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Master();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Master_NoLastMaster_ThrowSyntaxException() {
        // Arrange
        String program = "BEGIN MASTER Initiate { } END";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Master();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Master_NoInitiate_ThrowSyntaxException() {
        // Arrange
        String program = "BEGIN MASTER END MASTER";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Master();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Master_MinimumProgram_ReturnMasterNode() {
        // Arrange
        String program = "BEGIN MASTER Initiate { } END MASTER";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Master();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.Master);
    }

    @Test
    void Master_MinimumProgram_ReturnedNodeFirstChildIsInitiate() {
        // Arrange
        String program = "BEGIN MASTER Initiate { } Listener(id) { } END MASTER";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Master();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Type, AST.NodeType.Initiate);
    }

    @Test
    void Master_MinimalProgram_ReturnedNodeSecondChildIsListener() {
        // Arrange
        String program = "BEGIN MASTER Initiate { } Listener(id) { } END MASTER";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Master();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Next.Type, AST.NodeType.Listener);
    }



    @Test
    void Slaves_NoProgram_ReturnEmptyNode() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Slaves();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.Empty);
    }

    @Test
    void Slaves_OneSlave_ReturnSlaveNode() {
        // Arrange
        String program = "BEGIN SLAVE Initiate { } END SLAVE";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Slaves();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.Slave);
    }

    @Test
    void Slaves_TwoSlaves_ReturnSlaveNode() {
        // Arrange
        String program = "BEGIN SLAVE Initiate { } END SLAVE " + "BEGIN SLAVE Initiate { } END SLAVE";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Slaves();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.Slave);
    }

    @Test
    void Slaves_TwoSlaves_ReturnedNodeNextSiblingIsSlave() {
        // Arrange
        String program = "BEGIN SLAVE Initiate { } END SLAVE " + "BEGIN SLAVE Initiate { } END SLAVE";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Slaves();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Next.Type, AST.NodeType.Slave);
    }



    @Test
    void Slave_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Slave();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Slave_OnlyBeginEnd_ThrowSyntaxException() {
        // Arrange
        String program = "BEGIN END";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Slave();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Slave_NoBegin_ThrowSyntaxException() {
        // Arrange
        String program = "SLAVE Initiate { } END SLAVE";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Slave();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Slave_NoEnd_ThrowSyntaxException() {
        // Arrange
        String program = "BEGIN SLAVE Initiate { } SLAVE";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Slave();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Slave_NoFirstSlave_ThrowSyntaxException() {
        // Arrange
        String program = "BEGIN Initiate { } END SLAVE";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Slave();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Slave_NoLastSlave_ThrowSyntaxException() {
        // Arrange
        String program = "BEGIN SLAVE Initiate { } END";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Slave();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Slave_NoInitiate_ThrowSyntaxException() {
        // Arrange
        String program = "BEGIN SLAVE END SLAVE";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Slave();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Slave_MinimumProgram_ReturnSlaveNode() {
        // Arrange
        String program = "BEGIN SLAVE Initiate { } END SLAVE";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Slaves();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.Slave);
    }

    @Test
    void Slave_MinimalProgram_ReturnedNodeFirstChildIsInitiate() {
        // Arrange
        String program = "BEGIN SLAVE Initiate { } EventHandler(id) { } END SLAVE";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Slaves();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Type, AST.NodeType.Initiate);
    }

    @Test
    void Slave_MinimalProgram_ReturnedNodeSecondChildIsEventHandler() {
        // Arrange
        String program = "BEGIN SLAVE Initiate { } EventHandler(id) { } END SLAVE";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Slaves();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Next.Type, AST.NodeType.EventHandler);
    }



    @Test
    void Initiate_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Initiate();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Initiate_NoInitiate_ThrowSyntaxException() {
        // Arrange
        String program = "{ }";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Initiate();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Initiate_NoLeftBracket_ThrowSyntaxException() {
        // Arrange
        String program = "Initiate }";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Initiate();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Initiate_NoRightBracket_ThrowSyntaxException() {
        // Arrange
        String program = "Initiate {";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Initiate();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Initiate_NoBlock_ThrowSyntaxException() {
        // Arrange
        String program = "Initiate";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Initiate();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Initiate_Declaration_ThrowSyntaxException() {
        // Arrange
        String program = "Initiate { int a = 5; }";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Initiate();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Initiate_MinimumProgram_ReturnInitiateNode() {
        // Arrange
        String program = "Initiate { }";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Initiate();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.Initiate);
    }

    @Test
    void Initiate_MinimalProgram_ReturnedNodeFirstChildIsPinDeclaration() {
        // Arrange
        String program = "Initiate { " +
                "pin a = createPin(digital, input, 1); " +
                "pin b = createPin(digital, input, 2); " +
                "}";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Initiate();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Type, AST.NodeType.PinDeclaration);
    }

    @Test
    void Initiate_MinimalProgram_ReturnedNodeSecondChildIsPinDeclaration() {
        // Arrange
        String program = "Initiate { " +
                "pin a = createPin(digital, input, 1); " +
                "pin b = createPin(digital, input, 2); " +
                "}";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Initiate();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Next.Type, AST.NodeType.PinDeclaration);
    }



    @Test
    void PinDcls_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.PinDcls();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void PinDcls_MinimalProgram_ReturnEmptyNode() {
        // Arrange
        String program = "}";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.PinDcls();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.Empty);
    }

    @Test
    void PinDcls_OnePinDcl_NoSemi_ThrowSyntaxException() {
        // Arrange
        String program = "pin a = createPin(digital, input, 1) } ";
        Parser parser = createParser(program);

        // Act
        try {
            parser.PinDcls();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void PinDcls_OnePinDcl_ReturnPinDclNode() {
        // Arrange
        String program = "pin a = createPin(digital, input, 1); } ";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.PinDcls();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.PinDeclaration);
    }

    @Test
    void PinDcls_TwoPinDcls_ReturnPinDclNode() {
        // Arrange
        String program = "pin a = createPin(digital, input, 1); " +
                         "pin b = createPin(digital, input, 2); " +
                         "}";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.PinDcls();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.PinDeclaration);
    }

    @Test
    void PinDcls_TwoPinDcls_ReturnedNodeNextSiblingIsPinDcl() {
        // Arrange
        String program = "pin a = createPin(digital, input, 1); " +
                         "pin b = createPin(digital, input, 2); " +
                         "}";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.PinDcls();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Next.Type, AST.NodeType.PinDeclaration);
    }



    @Test
    void PinDcl_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.PinDcl();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void PinDcl_NoPin_ThrowSyntaxException() {
        // Arrange
        String program = "id = createPin(digital, input, 1)";
        Parser parser = createParser(program);

        // Act
        try {
            parser.PinDcl();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void PinDcl_NoIdentifier_ThrowSyntaxException() {
        // Arrange
        String program = "pin = createPin(digital, input, 1)";
        Parser parser = createParser(program);

        // Act
        try {
            parser.PinDcl();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void PinDcl_NoAssign_ThrowSyntaxException() {
        // Arrange
        String program = "pin id createPin(digital, input, 1)";
        Parser parser = createParser(program);

        // Act
        try {
            parser.PinDcl();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void PinDcl_NoCreatePinCall_ThrowSyntaxException() {
        // Arrange
        String program = "pin id = (digital, input, 1)";
        Parser parser = createParser(program);

        // Act
        try {
            parser.PinDcl();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void PinDcl_NoLParen_ThrowSyntaxException() {
        // Arrange
        String program = "pin id = createPin digital, input, 1)";
        Parser parser = createParser(program);

        // Act
        try {
            parser.PinDcl();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void PinDcl_NoPinType_ThrowSyntaxException() {
        // Arrange
        String program = "pin id = createPin(, input, 1)";
        Parser parser = createParser(program);

        // Act
        try {
            parser.PinDcl();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void PinDcl_NoFirstComma_ThrowSyntaxException() {
        // Arrange
        String program = "pin id = createPin(digital input, 1)";
        Parser parser = createParser(program);

        // Act
        try {
            parser.PinDcl();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void PinDcl_NoIOType_ThrowSyntaxException() {
        // Arrange
        String program = "pin id = createPin(digital, , 1)";
        Parser parser = createParser(program);

        // Act
        try {
            parser.PinDcl();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void PinDcl_NoSecondComma_ThrowSyntaxException() {
        // Arrange
        String program = "pin id = createPin(digital, input 1)";
        Parser parser = createParser(program);

        // Act
        try {
            parser.PinDcl();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void PinDcl_NoIntLiteral_ThrowSyntaxException() {
        // Arrange
        String program = "pin id = createPin(digital, input, )";
        Parser parser = createParser(program);

        // Act
        try {
            parser.PinDcl();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void PinDcl_NoRParen_ThrowSyntaxException() {
        // Arrange
        String program = "pin id = createPin(digital, input, 1";
        Parser parser = createParser(program);

        // Act
        try {
            parser.PinDcl();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void PinDcl_OnlyDeclaration_ThrowSyntaxException() {
        // Arrange
        String program = "pin id";
        Parser parser = createParser(program);

        // Act
        try {
            parser.PinDcl();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void PinDcl_MinimumProgram_ReturnPinDclNode() {
        // Arrange
        String program = "pin id = createPin(digital, input, 1)";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.PinDcl();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.PinDeclaration);
    }

    @Test
    void PinDcl_MinimumProgram_ReturnedNodeFirstChildIsIdentifier() {
        // Arrange
        String program = "pin id = createPin(digital, input, 1)";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.PinDcl();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Type, AST.NodeType.Identifier);
    }

    @Test
    void PinDcl_MinimumProgram_ReturnedNodeFirstChildHasValueOfIdentifier() {
        // Arrange
        String program = "pin id = createPin(digital, input, 1)";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.PinDcl();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertTrue(node.FirstChild.Value.equals("id"));
    }

    @Test
    void PinDcl_MinimumProgram_ReturnedNodeSecondChildIsPinType() {
        // Arrange
        String program = "pin id = createPin(digital, input, 1)";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.PinDcl();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Next.Type, AST.NodeType.PinType);
    }

    @Test
    void PinDcl_MinimumProgram_ReturnedNodeThirdChildIsIOType() {
        // Arrange
        String program = "pin id = createPin(digital, input, 1)";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.PinDcl();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Next.Next.Type, AST.NodeType.IOType);
    }

    // TODO: Test for andre typer?
    @Test
    void PinDcl_MinimumProgram_ReturnedNodeFourthChildIsLiteral() {
        // Arrange
        String program = "pin id = createPin(digital, input, 1)";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.PinDcl();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Next.Next.Next.Type, AST.NodeType.IntLiteral);
    }



    @Test
    void PinType_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.PinType();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void PinType_TypeIsInt_ThrowSyntaxException() {
        // Arrange
        String program = "int";
        Parser parser = createParser(program);

        // Act
        try {
            parser.PinType();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void PinType_TypeIsDigital_ReturnPinTypeNode() {
        // Arrange
        String program = "digital";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.PinType();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.PinType);
    }

    @Test
    void PinType_TypeIsAnalog_ReturnPinTypeNode() {
        // Arrange
        String program = "analog";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.PinType();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.PinType);
    }

    @Test
    void PinType_TypeIsPWM_ReturnPinTypeNode() {
        // Arrange
        String program = "pwm";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.PinType();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.PinType);
    }



    @Test
    void IOType_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.IOType();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void IOType_TypeIsFloat_ThrowSyntaxException() {
        // Arrange
        String program = "float";
        Parser parser = createParser(program);

        // Act
        try {
            parser.IOType();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void IOType_TypeIsInput_ReturnIOTypeNode() {
        // Arrange
        String program = "input";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.IOType();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.IOType);
    }

    @Test
    void IOType_TypeIsOutput_ReturnIOTypeNode() {
        // Arrange
        String program = "output";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.IOType();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.IOType);
    }



    @Test
    void Listeners_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Listeners();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Listeners_MinimumProgram_ReturnEmptyNode() {
        // Arrange
        String program = "END";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Listeners();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.Empty);
    }

    @Test
    void Listeners_OneListener_ReturnListenerNode() {
        // Arrange
        String program = "Listener (id) { } END";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Listeners();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.Listener);
    }

    @Test
    void Listeners_TwoListeners_ReturnListenerNode() {
        // Arrange
        String program = "Listener (id) { } " +
                         "Listener (id) { } " +
                         "END";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Listeners();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.Listener);
    }

    @Test
    void Listeners_TwoListeners_ReturnedNodeNextSiblingIsListenerNode() {
        // Arrange
        String program = "Listener (id) { } " +
                         "Listener (id) { } " +
                         "END";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Listeners();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Next.Type, AST.NodeType.Listener);
    }



    @Test
    void Listener_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Listener();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Listener_NoListener_ThrowSyntaxException() {
        // Arrange
        String program = "(id) { }";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Listener();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Listener_NoLParen_ThrowSyntaxException() {
        // Arrange
        String program = "Listener id) { }";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Listener();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Listener_NoIdentifier_ThrowSyntaxException() {
        // Arrange
        String program = "Listener () { }";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Listener();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Listener_NoRParen_ThrowSyntaxException() {
        // Arrange
        String program = "Listener (id { }";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Listener();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Listener_NoLBracket_ThrowSyntaxException() {
        // Arrange
        String program = "Listener (id) }";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Listener();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Listener_NoRBracket_ThrowSyntaxException() {
        // Arrange
        String program = "Listener (id) {";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Listener();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Listener_MinimumProgram_ReturnListenerNode() {
        // Arrange
        String program = "Listener (id) { }";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Listener();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.Listener);
    }

    @Test
    void Listener_MinimumProgram_ReturnedNodeFirstChildIsIdentifier() {
        // Arrange
        String program = "Listener (id) { }";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Listener();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Type, AST.NodeType.Identifier);
    }

    @Test
    void Listener_MinimumProgram_ReturnedNodeFirstChildIdentifierHasName() {
        // Arrange
        String program = "Listener (id) { }";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Listener();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertTrue(node.FirstChild.Value.equals("id"));
    }

    @Test
    void Listener_MinimalProgram_ReturnedNodeHasSecondChild() {
        // Arrange
        String program = "Listener (id) { int a = 4; }";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Listener();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertNotNull(node.FirstChild.Next);
    }

    @Test
    void Listener_MinimalProgram_ContainsCode_ReturnedNodeSecondChildIsBlock() {
        // Arrange
        String program = "Listener (id) { int a = 4; }";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Listener();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Next.Type, AST.NodeType.Block);
    }



    @Test
    void EventHandlers_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.EventHandlers();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void EventHandlers_MinimumProgram_ReturnEmptyNode() {
        // Arrange
        String program = "END";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.EventHandlers();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.Empty);
    }

    @Test
    void EventHandlers_OneEventHandler_ReturnEventHandlerNode() {
        // Arrange
        String program = "EventHandler (id) { }" +
                         "END";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.EventHandlers();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.EventHandler);
    }

    @Test
    void EventHandlers_TwoEventHandlers_ReturnEventHandlerNode() {
        // Arrange
        String program = "EventHandler (id) { }" +
                         "EventHandler (id) { }" +
                         "END";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.EventHandlers();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.EventHandler);
    }

    @Test
    void EventHandlers_TwoEventHandlers_ReturnedNodeNextSiblingIsEventHandler() {
        // Arrange
        String program = "EventHandler (id) { }" +
                         "EventHandler (id) { }" +
                         "END";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.EventHandlers();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.EventHandler);
    }



    @Test
    void EventHandler_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.EventHandler();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void EventHandler_NoEventHandler_ThrowSyntaxException() {
        // Arrange
        String program = "(id) { }";
        Parser parser = createParser(program);

        // Act
        try {
            parser.EventHandler();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void EventHandler_NoLParen_ThrowSyntaxException() {
        // Arrange
        String program = "EventHandler id) { }";
        Parser parser = createParser(program);

        // Act
        try {
            parser.EventHandler();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void EventHandler_NoIdentifier_ThrowSyntaxException() {
        // Arrange
        String program = "EventHandler () { }";
        Parser parser = createParser(program);

        // Act
        try {
            parser.EventHandler();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void EventHandler_NoRParen_ThrowSyntaxException() {
        // Arrange
        String program = "EventHandler (id { }";
        Parser parser = createParser(program);

        // Act
        try {
            parser.EventHandler();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void EventHandler_NoLBracket_ThrowSyntaxException() {
        // Arrange
        String program = "EventHandler (id) }";
        Parser parser = createParser(program);

        // Act
        try {
            parser.EventHandler();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void EventHandler_NoRBracket_ThrowSyntaxException() {
        // Arrange
        String program = "EventHandler (id) {";
        Parser parser = createParser(program);

        // Act
        try {
            parser.EventHandler();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void EventHandler_MinimumProgram_ThrowSyntaxException() {
        // Arrange
        String program = "EventHandler (id) { };";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.EventHandler();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.EventHandler);
    }

}