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

        assertSame(node.FirstChild.Next.Next.Next.Type, AST.NodeType.Literal);
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
}