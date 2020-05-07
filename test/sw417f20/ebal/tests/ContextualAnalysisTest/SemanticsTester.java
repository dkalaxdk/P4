package sw417f20.ebal.tests.ContextualAnalysisTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sw417f20.ebal.ContextAnalysis.HashSymbolTable;
import sw417f20.ebal.ContextAnalysis.Strategies.*;
import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;
import sw417f20.ebal.Visitors.SemanticsStrategiesVisitor;

import java.util.ArrayList;

public class SemanticsTester {

    SemanticsStrategiesVisitor strategiesVisitor;

    @BeforeEach
    void setup() {
        strategiesVisitor = new SemanticsStrategiesVisitor();
    }

    @Test
    void AssignmentStrategy_TypeInt_Returns_NoErrorsThrown() throws SemanticsException {
        Node AssignmentNode = Node.MakeNode(Node.NodeType.Assignment);
        Node ExpressionNode = Node.MakeNode(Node.NodeType.Expression);
        Node nodeIdentifier = Node.MakeNode(Node.NodeType.Identifier);

        ExpressionNode.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsAssignmentStrategy assignmentStrategy = new SemanticsAssignmentStrategy();
        assignmentStrategy.SymbolTable = new HashSymbolTable();

        ExpressionNode.DataType = Symbol.SymbolType.INT;
        nodeIdentifier.DataType = Symbol.SymbolType.INT;


        nodeIdentifier.Value = "TestVar";
        AssignmentNode.FirstChild = nodeIdentifier;
        AssignmentNode.FirstChild.Next = ExpressionNode;


        assignmentStrategy.SymbolTable.EnterSymbol(nodeIdentifier.Value,nodeIdentifier.DataType);
        AssignmentNode.SemanticsCheckerStrategy = assignmentStrategy;
        AssignmentNode.CheckSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }
    @Test
    void AssignmentStrategy_TypeFloat_Returns_NoErrorsThrown() throws SemanticsException {
        Node AssignmentNode = Node.MakeNode(Node.NodeType.Assignment);
        Node ExpressionNode = Node.MakeNode(Node.NodeType.Expression);
        Node nodeIdentifier = Node.MakeNode(Node.NodeType.Identifier);

        ExpressionNode.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsAssignmentStrategy assignmentStrategy = new SemanticsAssignmentStrategy();
        assignmentStrategy.SymbolTable = new HashSymbolTable();

        ExpressionNode.DataType = Symbol.SymbolType.FLOAT;
        nodeIdentifier.DataType = Symbol.SymbolType.FLOAT;


        nodeIdentifier.Value = "TestVar";
        AssignmentNode.FirstChild = nodeIdentifier;
        AssignmentNode.FirstChild.Next = ExpressionNode;


        assignmentStrategy.SymbolTable.EnterSymbol(nodeIdentifier.Value,nodeIdentifier.DataType);
        AssignmentNode.SemanticsCheckerStrategy = assignmentStrategy;
        AssignmentNode.CheckSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void AssignmentStrategy_TypeBool_Returns_NoErrorsThrown() throws SemanticsException {
        Node AssignmentNode = Node.MakeNode(Node.NodeType.Assignment);
        Node ExpressionNode = Node.MakeNode(Node.NodeType.Expression);
        Node nodeIdentifier = Node.MakeNode(Node.NodeType.Identifier);

        ExpressionNode.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsAssignmentStrategy assignmentStrategy = new SemanticsAssignmentStrategy();
        assignmentStrategy.SymbolTable = new HashSymbolTable();

        ExpressionNode.DataType = Symbol.SymbolType.BOOL;
        nodeIdentifier.DataType = Symbol.SymbolType.BOOL;


        nodeIdentifier.Value = "TestVar";
        AssignmentNode.FirstChild = nodeIdentifier;
        AssignmentNode.FirstChild.Next = ExpressionNode;


        assignmentStrategy.SymbolTable.EnterSymbol(nodeIdentifier.Value,nodeIdentifier.DataType);
        AssignmentNode.SemanticsCheckerStrategy = assignmentStrategy;
        AssignmentNode.CheckSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }
    @Test
    void AssignmentStrategy_TypeInt_Returns_WrongTypeErrorThrown() {
        Node AssignmentNode = Node.MakeNode(Node.NodeType.Assignment);
        Node ExpressionNode = Node.MakeNode(Node.NodeType.Expression);
        Node nodeIdentifier = Node.MakeNode(Node.NodeType.Identifier);

        //The string expected as return from the error
        String errorString = "wrong type";

        ExpressionNode.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsAssignmentStrategy assignmentStrategy = new SemanticsAssignmentStrategy();
        assignmentStrategy.SymbolTable = new HashSymbolTable();

        ExpressionNode.DataType = Symbol.SymbolType.INT;
        nodeIdentifier.DataType = Symbol.SymbolType.BOOL;


        nodeIdentifier.Value = "TestVar";
        AssignmentNode.FirstChild = nodeIdentifier;
        AssignmentNode.FirstChild.Next = ExpressionNode;


        assignmentStrategy.SymbolTable.EnterSymbol(nodeIdentifier.Value,nodeIdentifier.DataType);
        AssignmentNode.SemanticsCheckerStrategy = assignmentStrategy;



        Exception exception = Assertions.assertThrows(SemanticsException.class, AssignmentNode::CheckSemantics);

        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }

    @Test
    void AssignmentStrategy_TypeInt_Returns_Error_NotDeclared() {
        Node AssignmentNode = Node.MakeNode(Node.NodeType.Assignment);
        Node ExpressionNode = Node.MakeNode(Node.NodeType.Expression);
        Node nodeIdentifier = Node.MakeNode(Node.NodeType.Identifier);
        //The string expected as return from the error
        String errorString = "not been declared";

        ExpressionNode.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsAssignmentStrategy assignmentStrategy = new SemanticsAssignmentStrategy();
        assignmentStrategy.SymbolTable = new HashSymbolTable();

        ExpressionNode.DataType = Symbol.SymbolType.INT;
        nodeIdentifier.DataType = Symbol.SymbolType.INT;


        nodeIdentifier.Value = "TestVar";
        AssignmentNode.FirstChild = nodeIdentifier;
        AssignmentNode.FirstChild.Next = ExpressionNode;

        //In this test, the symbol has not been added to the symbol table, and therefor it should fail.
        AssignmentNode.SemanticsCheckerStrategy = assignmentStrategy;


        Exception exception = Assertions.assertThrows(SemanticsException.class, AssignmentNode::CheckSemantics);

        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }

    @Test
    void BoolDeclarationStrategy_Correct_Returns_NoErrors() throws SemanticsException {
        Node DeclarationNode = Node.MakeNode(Node.NodeType.BoolDeclaration);
        Node ExpressionNode = Node.MakeNode(Node.NodeType.Expression);
        Node IdentifierNode = Node.MakeNode(Node.NodeType.Identifier);

        ExpressionNode.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsBoolDeclarationStrategy boolDeclarationStrategy = new SemanticsBoolDeclarationStrategy();
        boolDeclarationStrategy.SymbolTable = new HashSymbolTable();

        ExpressionNode.DataType = Symbol.SymbolType.BOOL;
        IdentifierNode.DataType = Symbol.SymbolType.BOOL;


        IdentifierNode.Value = "TestVar";
        DeclarationNode.FirstChild = IdentifierNode;
        DeclarationNode.FirstChild.Next = ExpressionNode;

        DeclarationNode.SemanticsCheckerStrategy = boolDeclarationStrategy;
        DeclarationNode.CheckSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void BoolDeclarationStrategy_Correct_Returns_AllReadyDefined() {
        Node DeclarationNode = Node.MakeNode(Node.NodeType.BoolDeclaration);
        Node ExpressionNode = Node.MakeNode(Node.NodeType.Expression);
        Node IdentifierNode = Node.MakeNode(Node.NodeType.Identifier);
        //The string expected as return from the error
        String errorString = "has already been declared";

        ExpressionNode.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsBoolDeclarationStrategy boolDeclarationStrategy = new SemanticsBoolDeclarationStrategy();
        boolDeclarationStrategy.SymbolTable = new HashSymbolTable();

        ExpressionNode.DataType = Symbol.SymbolType.BOOL;
        IdentifierNode.DataType = Symbol.SymbolType.BOOL;


        IdentifierNode.Value = "TestVar";
        DeclarationNode.FirstChild = IdentifierNode;
        DeclarationNode.FirstChild.Next = ExpressionNode;

        DeclarationNode.SemanticsCheckerStrategy = boolDeclarationStrategy;
        boolDeclarationStrategy.SymbolTable.EnterSymbol(IdentifierNode.Value,IdentifierNode.DataType);

        Exception exception = Assertions.assertThrows(SemanticsException.class, DeclarationNode::CheckSemantics);

        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }

    @Test
    void BoolDeclarationStrategy_Correct_Returns_WrongTypeDeclared() {
        Node DeclarationNode = Node.MakeNode(Node.NodeType.BoolDeclaration);
        Node ExpressionNode = Node.MakeNode(Node.NodeType.Expression);
        Node IdentifierNode = Node.MakeNode(Node.NodeType.Identifier);
        //The string expected as return from the error
        String errorString = "wrong type";

        ExpressionNode.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsBoolDeclarationStrategy boolDeclarationStrategy = new SemanticsBoolDeclarationStrategy();
        boolDeclarationStrategy.SymbolTable = new HashSymbolTable();

        ExpressionNode.DataType = Symbol.SymbolType.INT;
        IdentifierNode.DataType = Symbol.SymbolType.BOOL;


        IdentifierNode.Value = "TestVar";
        DeclarationNode.FirstChild = IdentifierNode;
        DeclarationNode.FirstChild.Next = ExpressionNode;

        DeclarationNode.SemanticsCheckerStrategy = boolDeclarationStrategy;



        Exception exception = Assertions.assertThrows(SemanticsException.class, DeclarationNode::CheckSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }

    @Test
    void BoolLiteralStrategy_No_Prefix_Returns_NoErrors() throws SemanticsException {
        Node BoolNode = Node.MakeNode(Node.NodeType.BoolDeclaration);
        Node PrefixNode = Node.MakeNode(Node.NodeType.Empty);


        PrefixNode.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsBoolLiteralStrategy boolLiteralStrategy = new SemanticsBoolLiteralStrategy();
        boolLiteralStrategy.SymbolTable = new HashSymbolTable();

        PrefixNode.DataType = Symbol.SymbolType.INT;

        BoolNode.FirstChild = PrefixNode;

        BoolNode.SemanticsCheckerStrategy = boolLiteralStrategy;

        BoolNode.CheckSemantics();
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void BoolLiteralStrategy_Correct_Prefix_Returns_NoErrors() throws SemanticsException {
        Node BoolNode = Node.MakeNode(Node.NodeType.BoolDeclaration);
        Node PrefixNode = Node.MakeNode(Node.NodeType.PrefixNot);


        PrefixNode.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsBoolLiteralStrategy boolLiteralStrategy = new SemanticsBoolLiteralStrategy();
        boolLiteralStrategy.SymbolTable = new HashSymbolTable();

        PrefixNode.DataType = Symbol.SymbolType.INT;

        BoolNode.FirstChild = PrefixNode;

        BoolNode.SemanticsCheckerStrategy = boolLiteralStrategy;

        BoolNode.CheckSemantics();
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void BoolLiteralStrategy_Wrong_Prefix_Returns_Errors() {
        Node BoolNode = Node.MakeNode(Node.NodeType.BoolDeclaration);
        Node PrefixNode = Node.MakeNode(Node.NodeType.PrefixMinus);

        //The string expected as return from the error
        String errorString = "Only not prefix";


        PrefixNode.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsBoolLiteralStrategy boolLiteralStrategy = new SemanticsBoolLiteralStrategy();
        boolLiteralStrategy.SymbolTable = new HashSymbolTable();

        PrefixNode.DataType = Symbol.SymbolType.INT;

        BoolNode.FirstChild = PrefixNode;

        BoolNode.SemanticsCheckerStrategy = boolLiteralStrategy;

        Exception exception = Assertions.assertThrows(SemanticsException.class, BoolNode::CheckSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }

    @Test
    void EventDeclaration_Correct_Returns_NoErrors() throws SemanticsException {
        Node DeclarationNode = Node.MakeNode(Node.NodeType.EventDeclaration);
        Node ExpressionNode = Node.MakeNode(Node.NodeType.Call);
        Node IdentifierNode = Node.MakeNode(Node.NodeType.Identifier);
        Node CreateEventNode = Node.MakeNode(Node.NodeType.CreateEvent);
        Node ExpressionChild = Node.MakeNode(Node.NodeType.Expression);

        ExpressionNode.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsEventDeclarationStrategy eventDeclarationStrategy = new SemanticsEventDeclarationStrategy();
        eventDeclarationStrategy.SymbolTable = new HashSymbolTable();
        eventDeclarationStrategy.LocalEvents = new ArrayList<Symbol>();

        ExpressionNode.FirstChild = CreateEventNode;
        ExpressionNode.FirstChild.Next = ExpressionChild;
        IdentifierNode.Value = "TestVar";
        DeclarationNode.FirstChild = IdentifierNode;
        DeclarationNode.FirstChild.Next = ExpressionNode;

        DeclarationNode.SemanticsCheckerStrategy = eventDeclarationStrategy;
        DeclarationNode.CheckSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void EventDeclaration_InCorrect_Returns_AlreadyDeclared() {
        Node DeclarationNode = Node.MakeNode(Node.NodeType.EventDeclaration);
        Node ExpressionNode = Node.MakeNode(Node.NodeType.Call);
        Node IdentifierNode = Node.MakeNode(Node.NodeType.Identifier);
        Node CreateEventNode = Node.MakeNode(Node.NodeType.CreateEvent);
        Node ExpressionChild = Node.MakeNode(Node.NodeType.Expression);

        //The string expected as return from the error
        String errorString = "already been declared";

        ExpressionNode.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsEventDeclarationStrategy eventDeclarationStrategy = new SemanticsEventDeclarationStrategy();
        eventDeclarationStrategy.SymbolTable = new HashSymbolTable();
        eventDeclarationStrategy.LocalEvents = new ArrayList<Symbol>();

        IdentifierNode.Value = "TestVar";

        ExpressionNode.FirstChild = CreateEventNode;
        ExpressionNode.FirstChild.Next = ExpressionChild;

        DeclarationNode.FirstChild = IdentifierNode;
        DeclarationNode.FirstChild.Next = ExpressionNode;

        eventDeclarationStrategy.SymbolTable.EnterSymbol(IdentifierNode.Value,Symbol.SymbolType.EVENT,ExpressionChild.DataType);
        eventDeclarationStrategy.LocalEvents.add(eventDeclarationStrategy.SymbolTable.RetrieveSymbol(IdentifierNode.Value));

        DeclarationNode.SemanticsCheckerStrategy = eventDeclarationStrategy;

        Exception exception = Assertions.assertThrows(SemanticsException.class, DeclarationNode::CheckSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }

    @Test
    void EventDeclaration_InCorrect_Returns_IllegalDeclaration() {
        Node DeclarationNode = Node.MakeNode(Node.NodeType.EventDeclaration);
        Node ExpressionNode = Node.MakeNode(Node.NodeType.Identifier);
        Node IdentifierNode = Node.MakeNode(Node.NodeType.Identifier);
        Node CreateEventNode = Node.MakeNode(Node.NodeType.CreateEvent);
        Node ExpressionChild = Node.MakeNode(Node.NodeType.Expression);

        //The string expected as return from the error
        String errorString = "Illegal declaration";

        ExpressionNode.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsEventDeclarationStrategy eventDeclarationStrategy = new SemanticsEventDeclarationStrategy();
        eventDeclarationStrategy.SymbolTable = new HashSymbolTable();
        eventDeclarationStrategy.LocalEvents = new ArrayList<Symbol>();

        ExpressionNode.FirstChild = CreateEventNode;
        ExpressionNode.FirstChild.Next = ExpressionChild;
        IdentifierNode.Value = "TestVar";
        DeclarationNode.FirstChild = IdentifierNode;
        DeclarationNode.FirstChild.Next = ExpressionNode;

        DeclarationNode.SemanticsCheckerStrategy = eventDeclarationStrategy;


        Exception exception = Assertions.assertThrows(SemanticsException.class, DeclarationNode::CheckSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }

    @Test
    void EventHandlerBlockStrategy_Returns_No_Errors() throws SemanticsException {
        Node EventHandlerNode = Node.MakeNode(Node.NodeType.EventHandler);
        Node BlockNode = Node.MakeNode(Node.NodeType.Block);


        BlockNode.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsEventHandlerBlockStrategy eventDeclarationStrategy = new SemanticsEventHandlerBlockStrategy();
        eventDeclarationStrategy.SymbolTable = new HashSymbolTable();

        EventHandlerNode.FirstChild = BlockNode;

        EventHandlerNode.SemanticsCheckerStrategy = eventDeclarationStrategy;


        EventHandlerNode.CheckSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);

    }

    @Test
    void EventHandlerBlockStrategy_Returns_WrongDeclarations_PinDecl() {
        Node EventHandlerNode = Node.MakeNode(Node.NodeType.EventHandler);
        Node BlockNode = Node.MakeNode(Node.NodeType.PinDeclaration);

        //The string expected as return from the error
        String errorString = "No pin or event";


        BlockNode.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsEventHandlerBlockStrategy eventDeclarationStrategy = new SemanticsEventHandlerBlockStrategy();
        eventDeclarationStrategy.SymbolTable = new HashSymbolTable();

        EventHandlerNode.FirstChild = BlockNode;

        EventHandlerNode.SemanticsCheckerStrategy = eventDeclarationStrategy;


        Exception exception = Assertions.assertThrows(SemanticsException.class, EventHandlerNode::CheckSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }

    @Test
    void EventHandlerBlockStrategy_Returns_WrongDeclarations_EventDecl() {
        Node EventHandlerNode = Node.MakeNode(Node.NodeType.EventHandler);
        Node BlockNode = Node.MakeNode(Node.NodeType.EventDeclaration);

        //The string expected as return from the error
        String errorString = "No pin or event";

        BlockNode.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsEventHandlerBlockStrategy eventDeclarationStrategy = new SemanticsEventHandlerBlockStrategy();
        eventDeclarationStrategy.SymbolTable = new HashSymbolTable();

        EventHandlerNode.FirstChild = BlockNode;

        EventHandlerNode.SemanticsCheckerStrategy = eventDeclarationStrategy;

        Exception exception = Assertions.assertThrows(SemanticsException.class, EventHandlerNode::CheckSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }

    @Test
    void EventHandlerCallStrategy() {
        Node EventHandlerNode = Node.MakeNode(Node.NodeType.EventHandler);
        Node WriteNode = Node.MakeNode(Node.NodeType.Write);
        Node PinParameters = Node.MakeNode(Node.NodeType.Expression);

        //The string expected as return from the error
        String errorString = "No pin or event";

        WriteNode.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsEventHandlerBlockStrategy eventDeclarationStrategy = new SemanticsEventHandlerBlockStrategy();
        eventDeclarationStrategy.SymbolTable = new HashSymbolTable();

        EventHandlerNode.FirstChild = WriteNode;

        EventHandlerNode.SemanticsCheckerStrategy = eventDeclarationStrategy;

        Exception exception = Assertions.assertThrows(SemanticsException.class, EventHandlerNode::CheckSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }
}

class FakeStrategy extends SemanticsCheckerStrategy{
    @Override
    public void CheckSemantics(Node node) {
        // Do nothing
        // This function is made to fake any strategy not needed for specific tests, to limit the scope of the tests.
    }
}


