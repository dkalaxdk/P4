package Tests.SyntaxAnalysisTests;

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

//        assertSame(node.FirstChild.Next.Type, AST.NodeType.PinType);
        fail();
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

//        assertSame(node.FirstChild.Next.Next.Type, AST.NodeType.IOType);
        fail();
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

//        assertSame(node.Type, AST.NodeType.PinType);
        fail();
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

//        assertSame(node.Type, AST.NodeType.PinType);
        fail();
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

//        assertSame(node.Type, AST.NodeType.PinType);
        fail();
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

//        assertSame(node.Type, AST.NodeType.IOType);
        fail();
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

//        assertSame(node.Type, AST.NodeType.IOType);
        fail();
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

        assertEquals("id", node.FirstChild.Value);
    }

    @Test
    void Listener_MinimumProgram_ReturnedNodeSecondChildIsBlock() {
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
    void EventHandler_MinimumProgram_ReturnEventHandlerNode() {
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

    @Test
    void EventHandler_MinimumProgram_ReturnedNodeFirstChildIsIdentifier() {
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

        assertSame(node.FirstChild.Type, AST.NodeType.Identifier);
    }

    @Test
    void EventHandler_MinimumProgram_ReturnedNodeFirstChildIdentifierHasName() {
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

        assertEquals("id", node.FirstChild.Value);
    }

    @Test
    void EventHandler_MinimumProgram_ReturnedNodeSecondChildIsBlock() {
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

        assertSame(node.FirstChild.Next.Type, AST.NodeType.Block);
    }



    @Test
    void Block_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Block();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Block_NoLBracket_ThrowSyntaxException() {
        // Arrange
        String program = "}";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Block();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Block_NoRBracket_ThrowSyntaxException() {
        // Arrange
        String program = "{";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Block();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Block_MinimumProgram_ReturnBlockNode() {
        // Arrange
        String program = "{ }";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Block();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.Block);
    }

    @Test
    void Block_MinimumProgram_ReturnedNodeFirstChildIsEmptyNode() {
        // Arrange
        String program = "{ }";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Block();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Type, AST.NodeType.Empty);
    }

    @Test
    void Block_MinimalProgram_ContainsDeclaration_ReturnedNodeFirstChildIsDeclarationNode() {
        // Arrange
        String program = "{ int a = 6; }";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Block();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

//        assertSame(node.FirstChild.Type, AST.NodeType.Declaration);
        fail();
    }

    @Test
    void Block_MinimalProgram_ContainsTwoDeclarations_ReturnedNodeSecondChildIsDeclarationNode() {
        // Arrange
        String program = "{ int a = 6; int b = 3; }";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Block();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

//        assertSame(node.FirstChild.Next.Type, AST.NodeType.Declaration);
        fail();
    }



    @Test
    void Stmts_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Stmts();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Stmts_MinimumProgram_ReturnEmptyNode() {
        // Arrange
        String program = "}";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Stmts();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.Empty);
    }

    @Test
    void Stmts_MinimalProgram_OneDeclaration_ReturnDeclarationNode() {
        // Arrange
        String program = "int a = 2;" +
                "}";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Stmts();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

//        assertSame(node.Type, AST.NodeType.Declaration);
        fail();
    }

    @Test
    void Stmts_MinimalProgram_TwoDeclarations_ReturnDeclarationNode() {
        // Arrange
        String program = "int a = 2;" +
                "int b = 3;" +
                "}";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Stmts();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

//        assertSame(node.Type, AST.NodeType.Declaration);
        fail();
    }

    @Test
    void Stmts_MinimalProgram_TwoDeclarations_ReturnedNodeNextSiblingIsDeclarationNode() {
        // Arrange
        String program = "int a = 2;" +
                "int b = 3;" +
                "}";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Stmts();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

//        assertSame(node.Next.Type, AST.NodeType.Declaration);
        fail();
    }



    // TODO: Lav Stmt test
    @Test
    void Stmt_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Stmt();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Stmt_Call() {

    }

    @Test
    void Stmt_Assignment() {

    }

    @Test
    void Stmt_Dcl() {

    }

    @Test
    void Stmt_IfStmt() {

    }



    @Test
    void Assignment_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Assignment();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Assignment_NoIdentifier_ThrowSyntaxException() {
        // Arrange
        String program = "= 3;";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Assignment();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Assignment_NoAssign_ThrowSyntaxException() {
        // Arrange
        String program = "a 3;";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Assignment();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Assignment_NoExpression_ThrowSyntaxException() {
        // Arrange
        String program = "a =;";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Assignment();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Assignment_NoSemi_ThrowSyntaxException() {
        // Arrange
        String program = "a = 3";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Assignment();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Assignment_MinimumProgram_ReturnAssigmentNode() {
        // Arrange
        String program = "a = 3;";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Assignment();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.Assignment);
    }

    @Test
    void Assignment_MinimumProgram_ReturnedNodeFirstChildIsIdentifier() {
        // Arrange
        String program = "a = 3;";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Assignment();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Type, AST.NodeType.Identifier);
    }

    @Test
    void Assignment_MinimumProgram_ExpressionIsIntLiteral_ReturnedNodeSecondChildIsIntLiteral() {
        // Arrange
        String program = "a = 3;";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Assignment();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Next.Type, AST.NodeType.IntLiteral);
    }

    @Test
    void Assignment_MinimumProgram_ExpressionIsComposite_ReturnedNodeSecondChildIsExpression() {
        // Arrange
        String program = "a = 3 + 3;";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Assignment();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Next.Type, AST.NodeType.Expression);
    }



    @Test
    void Dcl_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Dcl();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Dcl_NoType_ThrowSyntaxException() {
        // Arrange
        String program = "a;";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Dcl();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Dcl_NoIdentifier_ThrowSyntaxException() {
        // Arrange
        String program = "int ;";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Dcl();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Dcl_NoSemi_ThrowSyntaxException() {
        // Arrange
        String program = "int a";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Dcl();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Dcl_MinimumProgram_ReturnDeclarationNode() {
        // Arrange
        String program = "int a;";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Dcl();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

//        assertSame(node.Type, AST.NodeType.Declaration);
        fail();
    }

    @Test
    void Dcl_MinimumProgram_ReturnedNodeFirstChildIsType() {
        // Arrange
        String program = "int a;";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Dcl();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

//        assertSame(node.FirstChild.Type, AST.NodeType.Type);
        fail();
    }

    @Test
    void Dcl_MinimumProgram_TypeIsFloat_ReturnedNodeFirstChildIsTypeFloat() {
        // Arrange
        String program = "float a;";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Dcl();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertEquals("float", node.FirstChild.Value);
    }

    @Test
    void Dcl_MinimumProgram_TypeIsInt_ReturnedNodeFirstChildIsTypeInt() {
        // Arrange
        String program = "int a;";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Dcl();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertEquals("int", node.FirstChild.Value);
    }

    @Test
    void Dcl_MinimumProgram_TypeIsBool_ReturnedNodeFirstChildIsTypeBool() {
        // Arrange
        String program = "bool a;";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Dcl();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertEquals("bool", node.FirstChild.Value);
    }

    @Test
    void Dcl_MinimumProgram_TypeIsEvent_ReturnedNodeFirstChildIsTypeEvent() {
        // Arrange
        String program = "event a;";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Dcl();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertEquals("event", node.FirstChild.Value);
    }

    @Test
    void Dcl_MinimumProgram_ReturnedNodeSecondChildIsIdentifier() {
        // Arrange
        String program = "int a;";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Dcl();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Next.Type, AST.NodeType.Identifier);
    }

    @Test
    void Dcl_MinimalProgramWithAssigment_ExpressionIsIntLiteral_ReturnedNodeThirdChildIsIntLiteral() {
        // Arrange
        String program = "int a = 3;";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Dcl();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Next.Next.Type, AST.NodeType.IntLiteral);
    }

    @Test
    void Dcl_MinimalProgramWithAssigment_ExpressionIsComposite_ReturnedNodeThirdChildIsExpression() {
        // Arrange
        String program = "int a = 3 + 3;";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Dcl();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Next.Next.Type, AST.NodeType.Expression);
    }



    @Test
    void DclAssign_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.DclAssign();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void DclAssign_NoAssign_ThrowSyntaxException() {
        // Arrange
        String program = "3;";
        Parser parser = createParser(program);

        // Act
        try {
            parser.DclAssign();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void DclAssign_NoExpression_ThrowSyntaxException() {
        // Arrange
        String program = "= ;";
        Parser parser = createParser(program);

        // Act
        try {
            parser.DclAssign();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void DclAssign_NoSemi_ThrowSyntaxException() {
        // Arrange
        String program = "= 3";
        Parser parser = createParser(program);

        // Act
        try {
            parser.DclAssign();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void DclAssign_MinimumProgram_NoExpression_ReturnEmptyNode() {
        // Arrange
        String program = ";";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.DclAssign();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.Empty);
    }

    @Test
    void DclAssign_MinimumProgram_ExpressionIsIntLiteral_ReturnIntLiteralNode() {
        // Arrange
        String program = "= 2;";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.DclAssign();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.IntLiteral);
    }

    @Test
    void DclAssign_MinimumProgram_ExpressionIsComposite_ReturnExpressionNode() {
        // Arrange
        String program = "= 2 + 2;";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.DclAssign();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.Expression);
    }



    // TODO: Lav Expr test
    @Test
    void Expr_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Expr();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Expr_Value() {

    }

    @Test
    void Expr_LParen() {

    }

    @Test
    void Expr_Minus() {

    }

    @Test
    void Expr_Not() {

    }

    @Test
    void Expr_ReturnsCall() {

    }



    @Test
    void Value_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Value();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Value_ValueIsIntLiteral_ReturnIntLiteralNode() {
        // Arrange
        String program = "2";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Value();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.IntLiteral);
    }

    @Test
    void Value_ValueIsFloatLiteral_ReturnFloatLiteralNode() {
        // Arrange
        String program = "1.2";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Value();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.FloatLiteral);
    }

    @Test
    void Value_ValueIsBoolLiteral_ReturnBoolLiteralNode() {
        // Arrange
        String program = "true";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Value();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.BoolLiteral);
    }

    @Test
    void Value_ValueIsIdentifier_ReturnIdentifierNode() {
        // Arrange
        String program = "test";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Value();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.Identifier);
    }



    @Test
    void AfterExpr_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.AfterExpr();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void AfterExpr_NoOperator_ThrowSyntaxException() {
        // Arrange
        String program = "4;";
        Parser parser = createParser(program);

        // Act
        try {
            parser.AfterExpr();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void AfterExpr_NoExpression_ThrowSyntaxException() {
        // Arrange
        String program = "+ ;";
        Parser parser = createParser(program);

        // Act
        try {
            parser.AfterExpr();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void AfterExpr_NoSemi_ThrowSyntaxException() {
        // Arrange
        String program = "+ 4";
        Parser parser = createParser(program);

        // Act
        try {
            parser.AfterExpr();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void AfterExpr_MinimumProgram_NoExpression_ReturnEmptyNode() {
        // Arrange
        String program = ";";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.AfterExpr();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, AST.NodeType.Empty);
    }

    @Test
    void AfterExpr_MinimumProgram_ReturnOperatorNode() {
        // Arrange
        String program = "+ 4;";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.AfterExpr();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

//        assertSame(node.Type, AST.NodeType.Operator);
        fail();
    }

    @Test
    void AfterExpr_MinimumProgram_ExpressionIsIntLiteral_ReturnedNodeNextIsIntLiteral() {
        // Arrange
        String program = "+ 4;";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.AfterExpr();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Next.Type, AST.NodeType.IntLiteral);
    }

    @Test
    void AfterExpr_MinimumProgram_ExpressionIsComposite_ReturnedNodeNextIsExpression() {
        // Arrange
        String program = "+ 4 + 6;";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.AfterExpr();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Next.Type, AST.NodeType.Expression);
    }


    // TODO: Lav Call test
    @Test
    void Call_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Call();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Call_VoidCall() {

    }

    @Test
    void Call_ReturnsCall() {

    }



    @Test
    void VoidCall_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.VoidCall();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void VoidCall_Broadcast() {

    }

    @Test
    void VoidCall_Write() {

    }



    @Test
    void ReturnsCall_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.ReturnsCall();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void ReturnsCall_FilterNoise() {

    }

    @Test
    void ReturnsCall_GetValue() {

    }

    @Test
    void ReturnsCall_CreateEvent() {

    }



    @Test
    void IfStmt_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.IfStmt();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }



    @Test
    void IfEnd_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.IfStmt();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }
}