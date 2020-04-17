package sw417f20.ebal.tests;

import org.junit.jupiter.api.Test;
import sw417f20.ebal.Exceptions.SyntaxException;
import sw417f20.ebal.SyntaxAnalysis.*;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {


    Parser createParser(String program) {
        StringReader stringReader = new StringReader(program);
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        Reader reader = new Reader(bufferedReader);
        Scanner scanner = new Scanner(reader);

        return new Parser(scanner, program);
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
        assertSame(node.Type, Node.NodeType.Prog);
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
        assertSame(node.FirstChild.Type, Node.NodeType.Master);
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
        assertSame(node.FirstChild.Next.Next.Type, Node.NodeType.Slave);
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

        assertSame(node.Type, Node.NodeType.Master);
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

        assertSame(node.FirstChild.Type, Node.NodeType.Initiate);
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

        assertSame(node.FirstChild.Next.Type, Node.NodeType.Listener);
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

        assertSame(node.Type, Node.NodeType.Empty);
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

        assertSame(node.Type, Node.NodeType.Slave);
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

        assertSame(node.Type, Node.NodeType.Slave);
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

        assertSame(node.Next.Type, Node.NodeType.Slave);
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

        assertSame(node.Type, Node.NodeType.Slave);
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

        assertSame(node.FirstChild.Type, Node.NodeType.Initiate);
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

        assertSame(node.FirstChild.Next.Type, Node.NodeType.EventHandler);
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

        assertSame(node.Type, Node.NodeType.Initiate);
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

        assertSame(node.FirstChild.Type, Node.NodeType.PinDeclaration);
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

        assertSame(node.FirstChild.Next.Type, Node.NodeType.PinDeclaration);
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
    void PinType_TypeIsDigital_ReturnDigitalNode() {
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

        assertSame(node.Type, Node.NodeType.Digital);
    }

    @Test
    void PinType_TypeIsAnalog_ReturnAnalogNode() {
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

        assertSame(node.Type, Node.NodeType.Analog);
    }

    @Test
    void PinType_TypeIsPWM_ReturnPWMNode() {
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

        assertSame(node.Type, Node.NodeType.PWM);
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
    void IOType_TypeIsInput_ReturnInputNode() {
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

        assertSame(node.Type, Node.NodeType.Input);
    }

    @Test
    void IOType_TypeIsOutput_ReturnOutputNode() {
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

        assertSame(node.Type, Node.NodeType.Output);
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

        assertSame(node.Type, Node.NodeType.Empty);
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

        assertSame(node.Type, Node.NodeType.Listener);
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

        assertSame(node.Type, Node.NodeType.Listener);
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

        assertSame(node.Next.Type, Node.NodeType.Listener);
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

        assertSame(node.Type, Node.NodeType.Listener);
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

        assertSame(node.FirstChild.Type, Node.NodeType.Identifier);
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

        assertSame(node.FirstChild.Next.Type, Node.NodeType.Block);
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

        assertSame(node.Type, Node.NodeType.Empty);
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

        assertSame(node.Type, Node.NodeType.EventHandler);
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

        assertSame(node.Type, Node.NodeType.EventHandler);
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

        assertSame(node.Type, Node.NodeType.EventHandler);
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

        assertSame(node.Type, Node.NodeType.EventHandler);
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

        assertSame(node.FirstChild.Type, Node.NodeType.Identifier);
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

        assertSame(node.FirstChild.Next.Type, Node.NodeType.Block);
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

        assertSame(node.Type, Node.NodeType.Block);
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

        assertSame(node.FirstChild.Type, Node.NodeType.Empty);
    }

    @Test
    void Block_MinimalProgram_ContainsIntDeclaration_ReturnedNodeFirstChildIsIntDeclarationNode() {
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

        assertSame(node.FirstChild.Type, Node.NodeType.IntDeclaration);
    }

    @Test
    void Block_MinimalProgram_ContainsTwoIntDeclarations_ReturnedNodeSecondChildIsIntDeclarationNode() {
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

        assertSame(node.FirstChild.Next.Type, Node.NodeType.IntDeclaration);
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

        assertSame(node.Type, Node.NodeType.Empty);
    }

    @Test
    void Stmts_MinimalProgram_OneIntDeclaration_ReturnIntDeclarationNode() {
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

        assertSame(node.Type, Node.NodeType.IntDeclaration);
    }

    @Test
    void Stmts_MinimalProgram_TwoIntDeclarations_ReturnIntDeclarationNode() {
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

        assertSame(node.Type, Node.NodeType.IntDeclaration);
    }

    @Test
    void Stmts_MinimalProgram_TwoIntDeclarations_ReturnedNodeNextSiblingIsIntDeclarationNode() {
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

        assertSame(node.Next.Type, Node.NodeType.IntDeclaration);
    }



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
    void Stmt_Call_BroadcastReturnCallNode() {
        // Arrange
        String program = "broadcast ( b )";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Call();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.Call);
    }

    @Test
    void Stmt_Assignment_ReturnAssignmentNode() {
        // Arrange
        String program = "a = 3;";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Stmt();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.Assignment);
    }

    @Test
    void Stmt_Dcl_DeclarationIsInt_ReturnIntDeclaration() {
        // Arrange
        String program = "int a;";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Stmt();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.IntDeclaration);
    }

    @Test
    void Stmt_IfStmt_ReturnIfNode() {
        // Arrange
        String program = "if ( b ) { } " +
                "}";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Stmt();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.If);
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

        assertSame(node.Type, Node.NodeType.Assignment);
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

        assertSame(node.FirstChild.Type, Node.NodeType.Identifier);
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

        assertSame(node.FirstChild.Next.Type, Node.NodeType.IntLiteral);
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

        assertSame(node.FirstChild.Next.Type, Node.NodeType.Expression);
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
    void Dcl_MinimumProgram_TypeIsInt_ReturnIntDeclarationNode() {
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

        assertSame(node.Type, Node.NodeType.IntDeclaration);
    }

    @Test
    void Dcl_MinimumProgram_TypeIsFloat_ReturnFloatDeclarationNode() {
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

        assertSame(node.Type, Node.NodeType.FloatDeclaration);
    }

    @Test
    void Dcl_MinimumProgram_TypeIsBool_ReturnBoolDeclarationNode() {
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

        assertSame(node.Type, Node.NodeType.BoolDeclaration);
    }

    @Test
    void Dcl_MinimumProgram_TypeIsEvent_ReturnEventDeclarationNode() {
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

        assertSame(node.Type, Node.NodeType.EventDeclaration);
    }

    @Test
    void Dcl_MinimumProgram_ReturnedNodeFirstChildIsIdentifier() {
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

        assertSame(node.FirstChild.Type, Node.NodeType.Identifier);
    }

    @Test
    void Dcl_MinimalProgramWithAssigment_ExpressionIsIntLiteral_ReturnedNodeSecondChildIsIntLiteral() {
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

        assertSame(node.FirstChild.Next.Type, Node.NodeType.IntLiteral);
    }

    @Test
    void Dcl_MinimalProgramWithAssigment_ExpressionIsComposite_ReturnedNodeSecondChildIsExpression() {
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

        assertSame(node.FirstChild.Next.Type, Node.NodeType.Expression);
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

        assertSame(node.Type, Node.NodeType.Empty);
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

        assertSame(node.Type, Node.NodeType.IntLiteral);
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

        assertSame(node.Type, Node.NodeType.Expression);
    }



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
    void Expr_Value_Identifier_ReturnIdentifierNode() {
        // Arrange
        String program = "a;";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Expr();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.Identifier);
    }

    @Test
    void Expr_Value_Literal_Int_ReturnLiteralNode() {
        // Arrange
        String program = "2;";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Expr();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.IntLiteral);
    }

    @Test
    void Expr_Parenthesised_Identifier_ReturnIdentifierNode() {
        // Arrange
        String program = "(a);";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Expr();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.Identifier);
    }

    @Test
    void Expr_Parenthesised_Literal_Int_ReturnLiteralNode() {
        // Arrange
        String program = "(1);";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Expr();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.IntLiteral);
    }

    @Test
    void Expr_Parenthesised_Composite_ReturnExpressionNode() {
        // Arrange
        String program = "(1 + 1);";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Expr();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.Expression);
    }

    @Test
    void Expr_Composite_ReturnExpressionNode() {
        // Arrange
        String program = "1 + 1;";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Expr();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.Expression);
    }

    @Test
    void Expr_Composite_IdentifierPlusIdentifier_ReturnedNodeFirstChildIsIdentifier() {
        // Arrange
        String program = "a + b;";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Expr();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Type, Node.NodeType.Identifier);
    }

    @Test
    void Expr_Composite_IdentifierPlusIdentifier_ReturnedNodeSecondChildIsPlusOperator() {
        // Arrange
        String program = "a + b;";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Expr();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Next.Type, Node.NodeType.Plus);
    }

    @Test
    void Expr_Composite_IdentifierPlusIdentifier_ReturnedNodeThirdChildIsIdentifier() {
        // Arrange
        String program = "a + b;";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Expr();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Next.Next.Type, Node.NodeType.Identifier);
    }

    @Test
    void Expr_Composite_IdentifierPlusIdentifierPlusIdentifier_ReturnedNodeThirdChildIsExpression() {
        // Arrange
        String program = "a + b + c;";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Expr();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Next.Next.Type, Node.NodeType.Expression);
    }

    @Test
    void Expr_Prefix_Minus_Identifier_ReturnedNodeFirstChildIsPrefixMinus() {
        // Arrange
        String program = "-a;";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Expr();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Type, Node.NodeType.PrefixMinus);
    }

    @Test
    void Expr_Prefix_Minus_Literal_ReturnedNodeFirstChildIsPrefixMinus() {
        // Arrange
        String program = "-1;";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Expr();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Type, Node.NodeType.PrefixMinus);
    }

    @Test
    void Expr_Prefix_Minus_InsideParenthesis_ReturnedNodeFirstChildIsPrefixMinus() {
        // Arrange
        String program = "(-1);";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Expr();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Type, Node.NodeType.PrefixMinus);
    }

    @Test
    void Expr_Prefix_Minus_OutsideParenthesis_ThrowSyntaxException() {
        // Arrange
        String program = "-(1);";
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
    void Expr_Prefix_Not_Identifier_ReturnedNodeFirstChildIsPrefixNot() {
        // Arrange
        String program = "!a;";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Expr();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Type, Node.NodeType.PrefixNot);
    }

    @Test
    void Expr_Prefix_Not_Literal_ThrowSyntaxException() {
        // Arrange
        String program = "!1;";
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
    void Expr_ReturnsCall_ReturnCallNode() {
        // Arrange
        String program = "getValue(a);";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Expr();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.Call);
    }

    @Test
    void Expr_VoidCall_ThrowSyntaxException() {
        // Arrange
        String program = "broadcast(a);";
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

        assertSame(node.Type, Node.NodeType.IntLiteral);
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

        assertSame(node.Type, Node.NodeType.FloatLiteral);
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

        assertSame(node.Type, Node.NodeType.BoolLiteral);
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

        assertSame(node.Type, Node.NodeType.Identifier);
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

        assertSame(node.Type, Node.NodeType.Empty);
    }

    @Test
    void AfterExpr_MinimumProgram_OperatorIsPlus_ReturnPlusNode() {
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

        assertSame(node.Type, Node.NodeType.Plus);
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

        assertSame(node.Next.Type, Node.NodeType.IntLiteral);
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

        assertSame(node.Next.Type, Node.NodeType.Expression);
    }



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
    void Call_VoidCall_CallIsBroadcast_ReturnCallNode() {
        // Arrange
        String program = "broadcast ( b )";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Call();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.Call);
    }

    @Test
    void Call_VoidCall_CallIsBroadcast_ReturnedNodeFirstChildIsBroadcast() {
        // Arrange
        String program = "broadcast ( b )";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Call();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Type, Node.NodeType.Broadcast);
    }

    @Test
    void Call_VoidCall_CallIsWrite_ReturnCallNode() {
        // Arrange
        String program = "write(a, 1)";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Call();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.Call);
    }

    @Test
    void Call_VoidCall_CallIsWrite_ReturnedNodeFirstChildIsWrite() {
        // Arrange
        String program = "write(a, 1)";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Call();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Type, Node.NodeType.Write);
    }

    @Test
    void Call_ReturnsCall_CallIsFilterNoise_ReturnCallNode() {
        // Arrange
        String program = "filterNoise(a, flip)";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Call();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.Call);
    }

    @Test
    void Call_ReturnsCall_CallIsFilterNoise_ReturnedNodeFirstChildIsFilterNoise() {
        // Arrange
        String program = "filterNoise(a, flip)";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Call();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Type, Node.NodeType.FilterNoise);
    }

    @Test
    void Call_ReturnsCall_CallIsGetValue_ReturnCallNode() {
        // Arrange
        String program = "getValue(a)";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Call();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.Call);
    }

    @Test
    void Call_ReturnsCall_CallIsGetValue_ReturnedNodeFirstChildIsGetValue() {
        // Arrange
        String program = "getValue(a)";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Call();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Type, Node.NodeType.GetValue);
    }

    @Test
    void Call_ReturnsCall_CallIsCreateEvent_ReturnCallNode() {
        // Arrange
        String program = "createEvent(a)";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Call();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.Call);
    }

    @Test
    void Call_ReturnsCall_CallIsCreateEvent_ReturnedNodeFirstChildIsCreateEvent() {
        // Arrange
        String program = "createEvent(a)";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Call();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Type, Node.NodeType.CreateEvent);
    }



    @Test
    void VoidCall_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.ProcedureCall();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }


    @Test
    void VoidCall_Broadcast_NoBroadcast_ThrowSyntaxException() {
        // Arrange
        String program = "( b )";
        Parser parser = createParser(program);

        // Act
        try {
            parser.ProcedureCall();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void VoidCall_Broadcast_NoLParen_ThrowSyntaxException() {
        // Arrange
        String program = "broadcast  b )";
        Parser parser = createParser(program);

        // Act
        try {
            parser.ProcedureCall();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void VoidCall_Broadcast_NoIdentifier_ThrowSyntaxException() {
        // Arrange
        String program = "broadcast (  )";
        Parser parser = createParser(program);

        // Act
        try {
            parser.ProcedureCall();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void VoidCall_Broadcast_NoRParen_ThrowSyntaxException() {
        // Arrange
        String program = "broadcast ( b ";
        Parser parser = createParser(program);

        // Act
        try {
            parser.ProcedureCall();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void VoidCall_Broadcast_MinimumProgram_ReturnCallNode() {
        // Arrange
        String program = "broadcast ( b )";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.ProcedureCall();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.Call);
    }

    @Test
    void VoidCall_Broadcast_MinimumProgram_ReturnedNodeFirstChildIsBroadcast() {
        // Arrange
        String program = "broadcast ( b )";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.ProcedureCall();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Type, Node.NodeType.Broadcast);
    }

    @Test
    void VoidCall_Broadcast_MinimumProgram_ReturnedNodeSecondChildIsIdentifier() {
        // Arrange
        String program = "broadcast ( b )";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.ProcedureCall();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Next.Type, Node.NodeType.Identifier);
    }


    @Test
    void VoidCall_Write_NoWrite_ThrowSyntaxException() {
        // Arrange
        String program = " (a, 1)";
        Parser parser = createParser(program);

        // Act
        try {
            parser.ProcedureCall();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void VoidCall_Write_NoLParen_ThrowSyntaxException() {
        // Arrange
        String program = "write a, 1)";
        Parser parser = createParser(program);

        // Act
        try {
            parser.ProcedureCall();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void VoidCall_Write_NoIdentifier_ThrowSyntaxException() {
        // Arrange
        String program = "write (, 1)";
        Parser parser = createParser(program);

        // Act
        try {
            parser.ProcedureCall();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void VoidCall_Write_NoComma_ThrowSyntaxException() {
        // Arrange
        String program = "write (a 1)";
        Parser parser = createParser(program);

        // Act
        try {
            parser.ProcedureCall();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void VoidCall_Write_NoExpression_ThrowSyntaxException() {
        // Arrange
        String program = "write (a, )";
        Parser parser = createParser(program);

        // Act
        try {
            parser.ProcedureCall();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void VoidCall_Write_NoRParen_ThrowSyntaxException() {
        // Arrange
        String program = "write (a, 1";
        Parser parser = createParser(program);

        // Act
        try {
            parser.ProcedureCall();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void VoidCall_Write_MinimumProgram_ReturnCallNode() {
        // Arrange
        String program = "write (a, 1)";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.ProcedureCall();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.Call);
    }

    @Test
    void VoidCall_Write_MinimumProgram_ReturnedNodeFirstChildIsWrite() {
        // Arrange
        String program = "write (a, 1)";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.ProcedureCall();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Type, Node.NodeType.Write);
    }

    @Test
    void VoidCall_Write_MinimumProgram_ReturnedNodeSecondChildIsIdentifier() {
        // Arrange
        String program = "write (a, 1)";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.ProcedureCall();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Next.Type, Node.NodeType.Identifier);
    }

    @Test
    void VoidCall_Write_MinimumProgram_ExpressionIsIntLiteral_ReturnedNodeThirdChildIsIntLiteral() {
        // Arrange
        String program = "write (a, 1)";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.ProcedureCall();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Next.Next.Type, Node.NodeType.IntLiteral);
    }

    @Test
    void VoidCall_Write_MinimumProgram_ExpressionIsComposite_ReturnedNodeThirdChildIsExpression() {
        // Arrange
        String program = "write (a, 1 + 1)";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.ProcedureCall();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Next.Next.Type, Node.NodeType.Expression);
    }



    @Test
    void ReturnsCall_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.FunctionCall();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }


    @Test
    void ReturnsCall_FilterNoise_NoFilterNoise_ThrowSyntaxException() {
        // Arrange
        String program = "(a, flip)";
        Parser parser = createParser(program);

        // Act
        try {
            parser.FunctionCall();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void ReturnsCall_FilterNoise_NoLParen_ThrowSyntaxException() {
        // Arrange
        String program = "filterNoise a, flip)";
        Parser parser = createParser(program);

        // Act
        try {
            parser.FunctionCall();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void ReturnsCall_FilterNoise_NoIdentifier_ThrowSyntaxException() {
        // Arrange
        String program = "filterNoise( , flip)";
        Parser parser = createParser(program);

        // Act
        try {
            parser.FunctionCall();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void ReturnsCall_FilterNoise_NoComma_ThrowSyntaxException() {
        // Arrange
        String program = "filterNoise(a flip)";
        Parser parser = createParser(program);

        // Act
        try {
            parser.FunctionCall();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void ReturnsCall_FilterNoise_NoFilterType_ThrowSyntaxException() {
        // Arrange
        String program = "filterNoise(a, )";
        Parser parser = createParser(program);

        // Act
        try {
            parser.FunctionCall();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void ReturnsCall_FilterNoise_NoRParen_ThrowSyntaxException() {
        // Arrange
        String program = "filterNoise(a, flip";
        Parser parser = createParser(program);

        // Act
        try {
            parser.FunctionCall();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void ReturnsCall_FilterNoise_MinimumProgram_ReturnCallNode() {
        // Arrange
        String program = "filterNoise(a, flip)";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.FunctionCall();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.Call);
    }

    @Test
    void ReturnsCall_FilterNoise_MinimumProgram_ReturnedNodeFirstChildIsFilterNoise() {
        // Arrange
        String program = "filterNoise(a, flip)";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.FunctionCall();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Type, Node.NodeType.FilterNoise);
    }

    @Test
    void ReturnsCall_FilterNoise_MinimumProgram_ReturnedNodeFirstSecondChildIsIdentifier() {
        // Arrange
        String program = "filterNoise(a, flip)";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.FunctionCall();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Next.Type, Node.NodeType.Identifier);
    }

    @Test
    void ReturnsCall_FilterNoise_MinimumProgram_FilterTypeIsFlip_ReturnedNodeFirstThirdChildIsFlip() {
        // Arrange
        String program = "filterNoise(a, flip)";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.FunctionCall();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Next.Next.Type, Node.NodeType.Flip);
    }


    @Test
    void ReturnsCall_GetValue_NoGetValue_ThrowSyntaxException() {
        // Arrange
        String program = "(a)";
        Parser parser = createParser(program);

        // Act
        try {
            parser.FunctionCall();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void ReturnsCall_GetValue_NoLParen_ThrowSyntaxException() {
        // Arrange
        String program = "getValue a)";
        Parser parser = createParser(program);

        // Act
        try {
            parser.FunctionCall();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void ReturnsCall_GetValue_NoValue_ThrowSyntaxException() {
        // Arrange
        String program = "getValue( )";
        Parser parser = createParser(program);

        // Act
        try {
            parser.FunctionCall();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void ReturnsCall_GetValue_NoRParen_ThrowSyntaxException() {
        // Arrange
        String program = "getValue(a";
        Parser parser = createParser(program);

        // Act
        try {
            parser.FunctionCall();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void ReturnsCall_GetValue_MinimumProgram_ReturnCallNode() {
        // Arrange
        String program = "getValue(a)";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.FunctionCall();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.Call);
    }

    @Test
    void ReturnsCall_GetValue_MinimumProgram_ReturnedNodeFirstChildIsGetValue() {
        // Arrange
        String program = "getValue(a)";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.FunctionCall();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Type, Node.NodeType.GetValue);
    }

    @Test
    void ReturnsCall_GetValue_MinimumProgram_ReturnedNodeSecondChildIsIdentifier() {
        // Arrange
        String program = "getValue(a)";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.FunctionCall();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Next.Type, Node.NodeType.Identifier);
    }


    @Test
    void ReturnsCall_CreateEvent_NoCreateEvent_ThrowSyntaxException() {
        // Arrange
        String program = "(a)";
        Parser parser = createParser(program);

        // Act
        try {
            parser.FunctionCall();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void ReturnsCall_CreateEvent_NoLParen_ThrowSyntaxException() {
        // Arrange
        String program = "createEvent a)";
        Parser parser = createParser(program);

        // Act
        try {
            parser.FunctionCall();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void ReturnsCall_CreateEvent_NoValue_ThrowSyntaxException() {
        // Arrange
        String program = "createEvent( )";
        Parser parser = createParser(program);

        // Act
        try {
            parser.FunctionCall();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void ReturnsCall_CreateEvent_NoRParen_ThrowSyntaxException() {
        // Arrange
        String program = "createEvent(a";
        Parser parser = createParser(program);

        // Act
        try {
            parser.FunctionCall();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void ReturnsCall_CreateEvent_MinimumProgram_ReturnCallNode() {
        // Arrange
        String program = "createEvent(a)";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.FunctionCall();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.Call);
    }

    @Test
    void ReturnsCall_CreateEvent_MinimumProgram_ReturnedNodeFirstChildIsCreateEvent() {
        // Arrange
        String program = "createEvent(a)";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.FunctionCall();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Type, Node.NodeType.CreateEvent);
    }

    @Test
    void ReturnsCall_CreateEvent_MinimumProgram_ReturnedNodeSecondChildIsIdentifier() {
        // Arrange
        String program = "createEvent(a)";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.FunctionCall();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Next.Type, Node.NodeType.Identifier);
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
    void IfStmt_NoIf_ThrowSyntaxException() {
        // Arrange
        String program = "( b ) { } " +
                "}";
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
    void IfStmt_NoLParen_ThrowSyntaxException() {
        // Arrange
        String program = "if  b ) { } " +
                "}";
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
    void IfStmt_NoExpression_ThrowSyntaxException() {
        // Arrange
        String program = "if ( ) { } " +
                "}";
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
    void IfStmt_NoRParen_ThrowSyntaxException() {
        // Arrange
        String program = "if ( b  { } " +
                "}";
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
    void IfStmt_NoLBracket_ThrowSyntaxException() {
        // Arrange
        String program = "if ( b ) } " +
                "}";
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
    void IfStmt_NoRBracket_ThrowSyntaxException() {
        // Arrange
        String program = "if ( b ) {  " +
                "}";
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
    void IfStmt_MinimumProgram_ReturnIfNode() {
        // Arrange
        String program = "if ( b ) { } " +
                "}";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.IfStmt();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.If);
    }

    @Test
    void IfStmt_MinimumProgram_ExpressionIsIdentifier_ReturnedNodeFirstChildIsIdentifier() {
        // Arrange
        String program = "if ( b ) { } " +
                "}";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.IfStmt();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Type, Node.NodeType.Identifier);
    }

    @Test
    void IfStmt_MinimumProgram_ExpressionIsComposite_ReturnedNodeFirstChildIsExpression() {
        // Arrange
        String program = "if ( a == b ) { } " +
                "}";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.IfStmt();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Type, Node.NodeType.Expression);
    }

    @Test
    void IfStmt_MinimumProgram_ReturnedNodeSecondChildIsBlock() {
        // Arrange
        String program = "if ( b ) { } " +
                "}";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.IfStmt();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Next.Type, Node.NodeType.Block);
    }

    @Test
    void IfStmt_MinimumProgram_ReturnedNodeThirdChildIsEmpty() {
        // Arrange
        String program = "if ( b ) { } " +
                "}";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.IfStmt();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Next.Next.Type, Node.NodeType.Empty);
    }

    @Test
    void IfStmt_MinimalProgram_WithElse_ReturnedNodeThirdChildIsBlock() {
        // Arrange
        String program = "if ( b ) { } else { }" +
                "}";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.IfStmt();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Next.Next.Type, Node.NodeType.Block);
    }

    @Test
    void IfStmt_MinimalProgram_WithElseIf_ReturnedNodeThirdChildIsIf() {
        // Arrange
        String program = "if ( b ) { } else if ( c ) { }" +
                "}";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.IfStmt();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.FirstChild.Next.Next.Type, Node.NodeType.If);
    }



    @Test
    void IfEnd_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.IfEnd();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void IfEnd_NoElse_ReturnEmptyNode() {
        // Arrange
        String program = "}";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.IfEnd();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.Empty);
    }

    @Test
    void IfEnd_Else_ReturnBlockNode() {
        // Arrange
        String program = "else { } }";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.IfEnd();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.Block);
    }

    @Test
    void IfEnd_ElseIf_ReturnIfNode() {
        // Arrange
        String program = "else if ( b ) { } }";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.IfEnd();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.If);
    }



    @Test
    void AfterElse_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.AfterElse();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void AfterElse_Block_ReturnBlockNode() {
        // Arrange
        String program = "{ }";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.AfterElse();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.Block);
    }

    @Test
    void AfterElse_ElseIf_ReturnIfNode() {
        // Arrange
        String program = "if ( b ) { }" +
                "}";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.AfterElse();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.If);
    }



    @Test
    void FilterType_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.FilterType();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void FilterType_Flip_ReturnFlipNode() {
        // Arrange
        String program = "flip";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.FilterType();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.Flip);
    }

    @Test
    void FilterType_Constant_ReturnConstantNode() {
        // Arrange
        String program = "constant";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.FilterType();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.Constant);
    }

    @Test
    void FilterType_Range_ReturnRangeNode() {
        // Arrange
        String program = "range";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.FilterType();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.Range);
    }



    @Test
    void Operator_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.Operator();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void Operator_Plus_ReturnPlusNode() {
        // Arrange
        String program = "+";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Operator();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.Plus);
    }

    @Test
    void Operator_Minus_ReturnMinusNode() {
        // Arrange
        String program = "-";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Operator();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.Minus);
    }

    @Test
    void Operator_Times_ReturnTimesNode() {
        // Arrange
        String program = "*";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Operator();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.Times);
    }

    @Test
    void Operator_Divide_ReturnDivideNode() {
        // Arrange
        String program = "/";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Operator();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.Divide);
    }

    @Test
    void Operator_Modulo_ReturnModuloNode() {
        // Arrange
        String program = "%";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.Operator();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.Modulo);
    }



    @Test
    void LogicOperator_NoProgram_ThrowSyntaxException() {
        // Arrange
        String program = "";
        Parser parser = createParser(program);

        // Act
        try {
            parser.LogicOperator();
        }
        // Assert
        catch (SyntaxException e) {
            assertTrue(true);
            return;
        }

        fail();
    }

    @Test
    void LogicOperator_LessThan_ReturnLessThanNode() {
        // Arrange
        String program = "<";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.LogicOperator();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.LessThan);
    }

    @Test
    void LogicOperator_GreaterThan_ReturnGreaterThanNode() {
        // Arrange
        String program = ">";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.LogicOperator();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.GreaterThan);
    }

    @Test
    void LogicOperator_NotEqual_ReturnNotEqualNode() {
        // Arrange
        String program = "!=";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.LogicOperator();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.NotEqual);
    }

    @Test
    void LogicOperator_Equals_ReturnEqualsNode() {
        // Arrange
        String program = "==";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.LogicOperator();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.Equals);
    }

    @Test
    void LogicOperator_GreaterOrEqual_ReturnGreaterOrEqualNode() {
        // Arrange
        String program = ">=";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.LogicOperator();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.GreaterOrEqual);
    }

    @Test
    void LogicOperator_LessOrEqual_ReturnLessOrEqualNode() {
        // Arrange
        String program = "<=";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.LogicOperator();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.LessOrEqual);
    }

    @Test
    void LogicOperator_And_ReturnAndNode() {
        // Arrange
        String program = "&&";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.LogicOperator();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.And);
    }

    @Test
    void LogicOperator_Or_ReturnOrNode() {
        // Arrange
        String program = "||";
        Parser parser = createParser(program);
        Node node;

        // Act
        try {
            node = parser.LogicOperator();
        }
        // Assert
        catch (SyntaxException e) {
            fail();
            return;
        }

        assertSame(node.Type, Node.NodeType.Or);
    }
}