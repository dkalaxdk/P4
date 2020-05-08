package sw417f20.ebal.tests.ContextualAnalysisTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import sw417f20.ebal.ContextAnalysis.HashSymbolTable;
import sw417f20.ebal.ContextAnalysis.SemanticsStrategyAssigner;
import sw417f20.ebal.ContextAnalysis.Strategies.*;
import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;

public class SemanticsTester {

    SemanticsStrategyAssigner strategiesVisitor;

    @BeforeEach
    void setup() {
        strategiesVisitor = new SemanticsStrategyAssigner();
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


        assignmentStrategy.SymbolTable.EnterSymbol(nodeIdentifier.Value, nodeIdentifier.DataType);
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


        assignmentStrategy.SymbolTable.EnterSymbol(nodeIdentifier.Value, nodeIdentifier.DataType);
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


        assignmentStrategy.SymbolTable.EnterSymbol(nodeIdentifier.Value, nodeIdentifier.DataType);
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


        assignmentStrategy.SymbolTable.EnterSymbol(nodeIdentifier.Value, nodeIdentifier.DataType);
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
        boolDeclarationStrategy.SymbolTable.EnterSymbol(IdentifierNode.Value, IdentifierNode.DataType);

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

        eventDeclarationStrategy.SymbolTable.EnterSymbol(IdentifierNode.Value, Symbol.SymbolType.EVENT, ExpressionChild.DataType);
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
    void EventHandlerCallStrategy_WriteMethod_Returns_No_Errors() throws SemanticsException {
        Node CallNode = Node.MakeNode(Node.NodeType.Call);
        Node WriteNode = Node.MakeNode(Node.NodeType.Write);
        Node PinParameters = Node.MakeNode(Node.NodeType.Identifier);
        Node IntParameters = Node.MakeNode(Node.NodeType.Identifier);

        WriteNode.SemanticsCheckerStrategy = new FakeStrategy();
        PinParameters.SemanticsCheckerStrategy = new FakeStrategy();
        IntParameters.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsEventHandlerCallStrategy CallHandlerStrategy = new SemanticsEventHandlerCallStrategy();
        CallHandlerStrategy.SymbolTable = new HashSymbolTable();

        PinParameters.Value = "Test";

        IntParameters.DataType = Symbol.SymbolType.INT;
        PinParameters.DataType = Symbol.SymbolType.PIN;

        PinParameters.Next = IntParameters;
        WriteNode.Next = PinParameters;
        CallNode.FirstChild = WriteNode;

        CallHandlerStrategy.SymbolTable.EnterSymbol(CallNode.FirstChild.Next.Value, CallNode.FirstChild.Next.DataType);
        CallNode.SemanticsCheckerStrategy = CallHandlerStrategy;


        CallNode.CheckSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void EventHandlerCallStrategy_WriteMethod_Returns_SecondParameter_Error() {
        Node CallNode = Node.MakeNode(Node.NodeType.Call);
        Node WriteNode = Node.MakeNode(Node.NodeType.Write);
        Node PinParameters = Node.MakeNode(Node.NodeType.Identifier);
        Node IntParameters = Node.MakeNode(Node.NodeType.Identifier);

        // The string expected as return from the error
        String errorString = "wrong type";

        WriteNode.SemanticsCheckerStrategy = new FakeStrategy();
        PinParameters.SemanticsCheckerStrategy = new FakeStrategy();
        IntParameters.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsEventHandlerCallStrategy CallHandlerStrategy = new SemanticsEventHandlerCallStrategy();
        CallHandlerStrategy.SymbolTable = new HashSymbolTable();

        PinParameters.Value = "Test";

        IntParameters.DataType = Symbol.SymbolType.BOOL;
        PinParameters.DataType = Symbol.SymbolType.PIN;

        PinParameters.Next = IntParameters;
        WriteNode.Next = PinParameters;
        CallNode.FirstChild = WriteNode;

        CallHandlerStrategy.SymbolTable.EnterSymbol(CallNode.FirstChild.Next.Value, CallNode.FirstChild.Next.DataType);
        CallNode.SemanticsCheckerStrategy = CallHandlerStrategy;

        Exception exception = Assertions.assertThrows(SemanticsException.class, CallNode::CheckSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }

    @Test
    void EventHandlerCallStrategy_WriteMethod_Returns_FirstParameter_Error() {
        Node CallNode = Node.MakeNode(Node.NodeType.Call);
        Node WriteNode = Node.MakeNode(Node.NodeType.Write);
        Node PinParameters = Node.MakeNode(Node.NodeType.Identifier);
        Node IntParameters = Node.MakeNode(Node.NodeType.Identifier);

        // The string expected as return from the error
        String errorString = "wrong type";

        WriteNode.SemanticsCheckerStrategy = new FakeStrategy();
        PinParameters.SemanticsCheckerStrategy = new FakeStrategy();
        IntParameters.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsEventHandlerCallStrategy CallHandlerStrategy = new SemanticsEventHandlerCallStrategy();
        CallHandlerStrategy.SymbolTable = new HashSymbolTable();

        PinParameters.Value = "Test";

        IntParameters.DataType = Symbol.SymbolType.INT;
        PinParameters.DataType = Symbol.SymbolType.BOOL;

        PinParameters.Next = IntParameters;
        WriteNode.Next = PinParameters;
        CallNode.FirstChild = WriteNode;

        CallHandlerStrategy.SymbolTable.EnterSymbol(CallNode.FirstChild.Next.Value, CallNode.FirstChild.Next.DataType);
        CallNode.SemanticsCheckerStrategy = CallHandlerStrategy;

        Exception exception = Assertions.assertThrows(SemanticsException.class, CallNode::CheckSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }

    @Test
    void EventHandlerCallStrategy_WriteMethod_Returns_Not_Declared() {
        Node CallNode = Node.MakeNode(Node.NodeType.Call);
        Node WriteNode = Node.MakeNode(Node.NodeType.Write);
        Node PinParameters = Node.MakeNode(Node.NodeType.Identifier);
        Node IntParameters = Node.MakeNode(Node.NodeType.Identifier);

        // The string expected as return from the error
        String errorString = "not been declared";

        WriteNode.SemanticsCheckerStrategy = new FakeStrategy();
        PinParameters.SemanticsCheckerStrategy = new FakeStrategy();
        IntParameters.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsEventHandlerCallStrategy CallHandlerStrategy = new SemanticsEventHandlerCallStrategy();
        CallHandlerStrategy.SymbolTable = new HashSymbolTable();

        PinParameters.Value = "Test";

        IntParameters.DataType = Symbol.SymbolType.INT;
        PinParameters.DataType = Symbol.SymbolType.PIN;

        PinParameters.Next = IntParameters;
        WriteNode.Next = PinParameters;
        CallNode.FirstChild = WriteNode;

        CallNode.SemanticsCheckerStrategy = CallHandlerStrategy;

        Exception exception = Assertions.assertThrows(SemanticsException.class, CallNode::CheckSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }

    @Test
    void EventHandlerCallStrategy_GetValue_PinType_Returns_No_Errors() throws SemanticsException {
        Node CallNode = Node.MakeNode(Node.NodeType.Call);
        Node WriteNode = Node.MakeNode(Node.NodeType.GetValue);
        Node PinParameters = Node.MakeNode(Node.NodeType.Identifier);

        WriteNode.SemanticsCheckerStrategy = new FakeStrategy();
        PinParameters.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsEventHandlerCallStrategy CallHandlerStrategy = new SemanticsEventHandlerCallStrategy();
        CallHandlerStrategy.SymbolTable = new HashSymbolTable();

        PinParameters.Value = "Test";

        PinParameters.DataType = Symbol.SymbolType.PIN;

        WriteNode.Next = PinParameters;
        CallNode.FirstChild = WriteNode;

        CallHandlerStrategy.SymbolTable.EnterSymbol(CallNode.FirstChild.Next.Value, CallNode.FirstChild.Next.DataType);
        CallNode.SemanticsCheckerStrategy = CallHandlerStrategy;

        CallNode.CheckSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);

    }

    @Test
    void EventHandlerCallStrategy_GetValue_EventType_Returns_No_Errors() throws SemanticsException {
        Node CallNode = Node.MakeNode(Node.NodeType.Call);
        Node WriteNode = Node.MakeNode(Node.NodeType.GetValue);
        Node PinParameters = Node.MakeNode(Node.NodeType.Identifier);

        WriteNode.SemanticsCheckerStrategy = new FakeStrategy();
        PinParameters.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsEventHandlerCallStrategy CallHandlerStrategy = new SemanticsEventHandlerCallStrategy();
        CallHandlerStrategy.SymbolTable = new HashSymbolTable();

        PinParameters.Value = "Test";
        CallHandlerStrategy.AvailablePinOrEvent = PinParameters.Value;
        PinParameters.DataType = Symbol.SymbolType.EVENT;


        WriteNode.Next = PinParameters;
        CallNode.FirstChild = WriteNode;

        CallHandlerStrategy.SymbolTable.EnterSymbol(CallNode.FirstChild.Next.Value, CallNode.FirstChild.Next.DataType);
        CallNode.SemanticsCheckerStrategy = CallHandlerStrategy;

        CallNode.CheckSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);

    }

    @Test
    void EventHandlerCallStrategy_GetValue_Integer_Returns_WrongType() {
        Node CallNode = Node.MakeNode(Node.NodeType.Call);
        Node WriteNode = Node.MakeNode(Node.NodeType.GetValue);
        Node PinParameters = Node.MakeNode(Node.NodeType.Identifier);

        // The string expected as return from the error
        String errorString = "wrong type";

        WriteNode.SemanticsCheckerStrategy = new FakeStrategy();
        PinParameters.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsEventHandlerCallStrategy CallHandlerStrategy = new SemanticsEventHandlerCallStrategy();
        CallHandlerStrategy.SymbolTable = new HashSymbolTable();

        PinParameters.Value = "Test";

        PinParameters.DataType = Symbol.SymbolType.INT;

        WriteNode.Next = PinParameters;
        CallNode.FirstChild = WriteNode;

        CallHandlerStrategy.SymbolTable.EnterSymbol(CallNode.FirstChild.Next.Value, CallNode.FirstChild.Next.DataType);
        CallNode.SemanticsCheckerStrategy = CallHandlerStrategy;


        Exception exception = Assertions.assertThrows(SemanticsException.class, CallNode::CheckSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));

    }

    @Test
    void EventHandlerCallStrategy_GetValue_PinType_Returns_NotDeclared() {
        Node CallNode = Node.MakeNode(Node.NodeType.Call);
        Node WriteNode = Node.MakeNode(Node.NodeType.GetValue);
        Node PinParameters = Node.MakeNode(Node.NodeType.Identifier);

        // The string expected as return from the error
        String errorString = "has not been declared.";

        WriteNode.SemanticsCheckerStrategy = new FakeStrategy();
        PinParameters.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsEventHandlerCallStrategy CallHandlerStrategy = new SemanticsEventHandlerCallStrategy();
        CallHandlerStrategy.SymbolTable = new HashSymbolTable();

        PinParameters.Value = "Test";

        PinParameters.DataType = Symbol.SymbolType.PIN;

        WriteNode.Next = PinParameters;
        CallNode.FirstChild = WriteNode;

        CallNode.SemanticsCheckerStrategy = CallHandlerStrategy;


        Exception exception = Assertions.assertThrows(SemanticsException.class, CallNode::CheckSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));

    }

    @Test
    void EventHandlerCallStrategy_GetValue_EventType_Returns_Event_Not_Available() {
        Node CallNode = Node.MakeNode(Node.NodeType.Call);
        Node WriteNode = Node.MakeNode(Node.NodeType.GetValue);
        Node PinParameters = Node.MakeNode(Node.NodeType.Identifier);

        // The string expected as return from the error
        String errorString = "Event unavailable";

        WriteNode.SemanticsCheckerStrategy = new FakeStrategy();
        PinParameters.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsEventHandlerCallStrategy CallHandlerStrategy = new SemanticsEventHandlerCallStrategy();
        CallHandlerStrategy.SymbolTable = new HashSymbolTable();

        PinParameters.Value = "Test";
        PinParameters.DataType = Symbol.SymbolType.EVENT;


        WriteNode.Next = PinParameters;
        CallNode.FirstChild = WriteNode;

        CallHandlerStrategy.SymbolTable.EnterSymbol(CallNode.FirstChild.Next.Value, CallNode.FirstChild.Next.DataType);
        CallNode.SemanticsCheckerStrategy = CallHandlerStrategy;

        Exception exception = Assertions.assertThrows(SemanticsException.class, CallNode::CheckSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));

    }

    @Test
    void EventHandlerCallStrategy_GetValue_EventType_Returns_Event_Not_Declared() {
        Node CallNode = Node.MakeNode(Node.NodeType.Call);
        Node WriteNode = Node.MakeNode(Node.NodeType.GetValue);
        Node PinParameters = Node.MakeNode(Node.NodeType.Identifier);

        // The string expected as return from the error
        String errorString = "not been declared";

        WriteNode.SemanticsCheckerStrategy = new FakeStrategy();
        PinParameters.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsEventHandlerCallStrategy CallHandlerStrategy = new SemanticsEventHandlerCallStrategy();
        CallHandlerStrategy.SymbolTable = new HashSymbolTable();

        PinParameters.Value = "Test";
        PinParameters.DataType = Symbol.SymbolType.EVENT;


        WriteNode.Next = PinParameters;
        CallNode.FirstChild = WriteNode;

        CallNode.SemanticsCheckerStrategy = CallHandlerStrategy;

        Exception exception = Assertions.assertThrows(SemanticsException.class, CallNode::CheckSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));

    }

    @Test
    void EventHandlerStrategy_Returns_NoErrors() throws SemanticsException {
        Node EventHandlerNode = Node.MakeNode(Node.NodeType.EventHandler);
        Node BlockNode = Node.MakeNode(Node.NodeType.Block);
        Node IdentifierNode = Node.MakeNode(Node.NodeType.Identifier);


        BlockNode.SemanticsCheckerStrategy = new FakeStrategy();
        IdentifierNode.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsEventHandlerStrategy eventHandlerStrategy = new SemanticsEventHandlerStrategy();
        eventHandlerStrategy.SymbolTable = new HashSymbolTable();

        IdentifierNode.Value = "Test";
        BlockNode.DataType = Symbol.SymbolType.EVENT;


        BlockNode.Next = IdentifierNode;
        EventHandlerNode.FirstChild = BlockNode;

        EventHandlerNode.SemanticsCheckerStrategy = eventHandlerStrategy;
        eventHandlerStrategy.SymbolTable.EnterSymbol(EventHandlerNode.FirstChild.Value, EventHandlerNode.FirstChild.DataType);

        EventHandlerNode.CheckSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);

    }

    @Test
    void EventHandlerStrategy_WrongType_Returns_WrongType() {
        Node EventHandlerNode = Node.MakeNode(Node.NodeType.EventHandler);
        Node BlockNode = Node.MakeNode(Node.NodeType.Block);
        Node IdentifierNode = Node.MakeNode(Node.NodeType.Identifier);


        // The string expected as return from the error
        String errorString = "wrong type";

        BlockNode.SemanticsCheckerStrategy = new FakeStrategy();
        IdentifierNode.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsEventHandlerStrategy eventHandlerStrategy = new SemanticsEventHandlerStrategy();
        eventHandlerStrategy.SymbolTable = new HashSymbolTable();

        IdentifierNode.Value = "Test";
        BlockNode.DataType = Symbol.SymbolType.INT;


        BlockNode.Next = IdentifierNode;
        EventHandlerNode.FirstChild = BlockNode;

        EventHandlerNode.SemanticsCheckerStrategy = eventHandlerStrategy;
        eventHandlerStrategy.SymbolTable.EnterSymbol(EventHandlerNode.FirstChild.Value, EventHandlerNode.FirstChild.DataType);

        Exception exception = Assertions.assertThrows(SemanticsException.class, EventHandlerNode::CheckSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));

    }

    @Test
    void EventHandlerStrategy_WrongType_Returns_Not_Declared() {
        Node EventHandlerNode = Node.MakeNode(Node.NodeType.EventHandler);
        Node BlockNode = Node.MakeNode(Node.NodeType.Block);
        Node IdentifierNode = Node.MakeNode(Node.NodeType.Identifier);


        // The string expected as return from the error
        String errorString = "not been declared";

        BlockNode.SemanticsCheckerStrategy = new FakeStrategy();
        IdentifierNode.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsEventHandlerStrategy eventHandlerStrategy = new SemanticsEventHandlerStrategy();
        eventHandlerStrategy.SymbolTable = new HashSymbolTable();

        IdentifierNode.Value = "Test";
        BlockNode.DataType = Symbol.SymbolType.EVENT;


        BlockNode.Next = IdentifierNode;
        EventHandlerNode.FirstChild = BlockNode;

        EventHandlerNode.SemanticsCheckerStrategy = eventHandlerStrategy;

        Exception exception = Assertions.assertThrows(SemanticsException.class, EventHandlerNode::CheckSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));

    }

    @Test
    void FloatDeclarationStrategy_Returns_NoErrors() throws SemanticsException {
        Node FloatDeclaration = Node.MakeNode(Node.NodeType.FloatDeclaration);
        Node IdentifierNode = Node.MakeNode(Node.NodeType.Identifier);
        Node FloatNode = Node.MakeNode(Node.NodeType.FloatLiteral);


        IdentifierNode.SemanticsCheckerStrategy = new FakeStrategy();
        FloatNode.SemanticsCheckerStrategy = new FakeStrategy();


        SemanticsFloatDeclarationStrategy floatDeclarationStrategy = new SemanticsFloatDeclarationStrategy();
        floatDeclarationStrategy.SymbolTable = new HashSymbolTable();


        FloatNode.DataType = Symbol.SymbolType.FLOAT;
        IdentifierNode.Next = FloatNode;
        FloatDeclaration.FirstChild = IdentifierNode;

        FloatDeclaration.SemanticsCheckerStrategy = floatDeclarationStrategy;

        FloatDeclaration.CheckSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void FloatDeclarationStrategy_Returns_WrongType() {
        Node FloatDeclaration = Node.MakeNode(Node.NodeType.FloatDeclaration);
        Node IdentifierNode = Node.MakeNode(Node.NodeType.Identifier);
        Node FloatNode = Node.MakeNode(Node.NodeType.FloatLiteral);

        // The string expected as return from the error
        String errorString = "wrong type";


        IdentifierNode.SemanticsCheckerStrategy = new FakeStrategy();
        FloatNode.SemanticsCheckerStrategy = new FakeStrategy();


        SemanticsFloatDeclarationStrategy floatDeclarationStrategy = new SemanticsFloatDeclarationStrategy();
        floatDeclarationStrategy.SymbolTable = new HashSymbolTable();

        FloatNode.DataType = Symbol.SymbolType.BOOL;
        IdentifierNode.Next = FloatNode;
        FloatDeclaration.FirstChild = IdentifierNode;

        FloatDeclaration.SemanticsCheckerStrategy = floatDeclarationStrategy;

        Exception exception = Assertions.assertThrows(SemanticsException.class, FloatDeclaration::CheckSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }

    @Test
    void FloatDeclarationStrategy_Returns_Already_Declared() {
        Node FloatDeclaration = Node.MakeNode(Node.NodeType.FloatDeclaration);
        Node IdentifierNode = Node.MakeNode(Node.NodeType.Identifier);
        Node FloatNode = Node.MakeNode(Node.NodeType.FloatLiteral);

        // The string expected as return from the error
        String errorString = "already been declared";


        IdentifierNode.SemanticsCheckerStrategy = new FakeStrategy();
        FloatNode.SemanticsCheckerStrategy = new FakeStrategy();


        SemanticsFloatDeclarationStrategy floatDeclarationStrategy = new SemanticsFloatDeclarationStrategy();
        floatDeclarationStrategy.SymbolTable = new HashSymbolTable();

        FloatNode.DataType = Symbol.SymbolType.BOOL;
        IdentifierNode.Next = FloatNode;
        FloatDeclaration.FirstChild = IdentifierNode;

        FloatDeclaration.SemanticsCheckerStrategy = floatDeclarationStrategy;


        floatDeclarationStrategy.SymbolTable.EnterSymbol(FloatDeclaration.FirstChild.Value, FloatDeclaration.FirstChild.DataType);

        Exception exception = Assertions.assertThrows(SemanticsException.class, FloatDeclaration::CheckSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }

    @Test
    void FloatLiteralStrategy_No_Prefix_Returns_No_Errors() throws SemanticsException {
        Node FloatNode = Node.MakeNode(Node.NodeType.FloatLiteral);
        Node PrefixNode = Node.MakeNode(Node.NodeType.Block);

        PrefixNode.SemanticsCheckerStrategy = new FakeStrategy();


        SemanticsFloatLiteralStrategy floatLiteralStrategy = new SemanticsFloatLiteralStrategy();
        floatLiteralStrategy.SymbolTable = new HashSymbolTable();


        PrefixNode.Type = Node.NodeType.Empty;
        FloatNode.FirstChild = PrefixNode;

        FloatNode.SemanticsCheckerStrategy = floatLiteralStrategy;


        floatLiteralStrategy.SymbolTable.EnterSymbol(FloatNode.FirstChild.Value, FloatNode.FirstChild.DataType);


        FloatNode.CheckSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);

    }

    @Test
    void FloatLiteralStrategy_Correct_Prefix_Returns_No_Errors() throws SemanticsException {
        Node FloatNode = Node.MakeNode(Node.NodeType.FloatLiteral);
        Node PrefixNode = Node.MakeNode(Node.NodeType.Block);

        PrefixNode.SemanticsCheckerStrategy = new FakeStrategy();


        SemanticsFloatLiteralStrategy floatLiteralStrategy = new SemanticsFloatLiteralStrategy();
        floatLiteralStrategy.SymbolTable = new HashSymbolTable();


        PrefixNode.Type = Node.NodeType.PrefixMinus;
        FloatNode.FirstChild = PrefixNode;

        FloatNode.SemanticsCheckerStrategy = floatLiteralStrategy;


        floatLiteralStrategy.SymbolTable.EnterSymbol(FloatNode.FirstChild.Value, FloatNode.FirstChild.DataType);


        FloatNode.CheckSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);

    }

    @Test
    void FloatLiteralStrategy_Wrong_Prefix_Returns_Wrong_Prefix() {
        Node FloatNode = Node.MakeNode(Node.NodeType.FloatLiteral);
        Node PrefixNode = Node.MakeNode(Node.NodeType.Block);

        // The string expected as return from the error
        String errorString = "Only minus prefix";

        PrefixNode.SemanticsCheckerStrategy = new FakeStrategy();


        SemanticsFloatLiteralStrategy floatLiteralStrategy = new SemanticsFloatLiteralStrategy();
        floatLiteralStrategy.SymbolTable = new HashSymbolTable();


        PrefixNode.Type = Node.NodeType.PrefixNot;
        FloatNode.FirstChild = PrefixNode;

        FloatNode.SemanticsCheckerStrategy = floatLiteralStrategy;


        floatLiteralStrategy.SymbolTable.EnterSymbol(FloatNode.FirstChild.Value, FloatNode.FirstChild.DataType);


        Exception exception = Assertions.assertThrows(SemanticsException.class, FloatNode::CheckSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));

    }

    @Test
    void SemanticsIdentifierStrategy_No_Prefix_Returns_No_Errors() throws SemanticsException {
        Node IdentifierNode = Node.MakeNode(Node.NodeType.Identifier);
        IdentifierNode.Value = "Test";
        Node Prefix = Node.MakeNode(Node.NodeType.Empty);

        Prefix.SemanticsCheckerStrategy = new FakeStrategy();


        SemanticsIdentifierStrategy identifierStrategy = new SemanticsIdentifierStrategy();
        identifierStrategy.SymbolTable = new HashSymbolTable();

        IdentifierNode.FirstChild = Prefix;

        IdentifierNode.SemanticsCheckerStrategy = identifierStrategy;


        identifierStrategy.SymbolTable.EnterSymbol(IdentifierNode.Value, IdentifierNode.DataType,true);


        IdentifierNode.CheckSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);

    }

    @Test
    void SemanticsIdentifierStrategy_PrefixMinus_IntType_Returns_No_Errors() throws SemanticsException {
        Node IdentifierNode = Node.MakeNode(Node.NodeType.Identifier);
        IdentifierNode.Value = "Test";
        IdentifierNode.DataType = Symbol.SymbolType.INT;
        Node PrefixNode = Node.MakeNode(Node.NodeType.PrefixMinus);

        PrefixNode.SemanticsCheckerStrategy = new FakeStrategy();


        SemanticsIdentifierStrategy identifierStrategy = new SemanticsIdentifierStrategy();
        identifierStrategy.SymbolTable = new HashSymbolTable();

        IdentifierNode.FirstChild = PrefixNode;

        IdentifierNode.SemanticsCheckerStrategy = identifierStrategy;


        identifierStrategy.SymbolTable.EnterSymbol(IdentifierNode.Value, IdentifierNode.DataType,true);


        IdentifierNode.CheckSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);

    }

    @Test
    void SemanticsIdentifierStrategy_PrefixNot_BoolType_Returns_No_Errors() throws SemanticsException {
        Node IdentifierNode = Node.MakeNode(Node.NodeType.Identifier);
        IdentifierNode.Value = "Test";
        IdentifierNode.DataType = Symbol.SymbolType.BOOL;
        Node PrefixNode = Node.MakeNode(Node.NodeType.PrefixNot);

        PrefixNode.SemanticsCheckerStrategy = new FakeStrategy();


        SemanticsIdentifierStrategy identifierStrategy = new SemanticsIdentifierStrategy();
        identifierStrategy.SymbolTable = new HashSymbolTable();

        IdentifierNode.FirstChild = PrefixNode;

        IdentifierNode.SemanticsCheckerStrategy = identifierStrategy;


        identifierStrategy.SymbolTable.EnterSymbol(IdentifierNode.Value, IdentifierNode.DataType,true);


        IdentifierNode.CheckSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);

    }

    @Test
    void SemanticsIdentifierStrategy_PrefixNot_IntType_Returns_Not_Only_Applicable_For_Boolean() {
        Node IdentifierNode = Node.MakeNode(Node.NodeType.Identifier);
        IdentifierNode.Value = "Test";
        IdentifierNode.DataType = Symbol.SymbolType.INT;
        Node PrefixNode = Node.MakeNode(Node.NodeType.PrefixNot);

        // The string expected as return from the error
        String errorString = "only applicable to boolean";

        PrefixNode.SemanticsCheckerStrategy = new FakeStrategy();


        SemanticsIdentifierStrategy identifierStrategy = new SemanticsIdentifierStrategy();
        identifierStrategy.SymbolTable = new HashSymbolTable();

        IdentifierNode.FirstChild = PrefixNode;

        IdentifierNode.SemanticsCheckerStrategy = identifierStrategy;


        identifierStrategy.SymbolTable.EnterSymbol(IdentifierNode.Value, IdentifierNode.DataType,true);

        Exception exception = Assertions.assertThrows(SemanticsException.class, IdentifierNode::CheckSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));

    }

    @Test
    void SemanticsIdentifierStrategy_PrefixMinus_BoolType_Returns_Minus_Not_Applicable_For_Boolean() {
        Node IdentifierNode = Node.MakeNode(Node.NodeType.Identifier);
        IdentifierNode.Value = "Test";
        IdentifierNode.DataType = Symbol.SymbolType.BOOL;
        Node PrefixNode = Node.MakeNode(Node.NodeType.PrefixMinus);

        // The string expected as return from the error
        String errorString = " not applicable to boolean";

        PrefixNode.SemanticsCheckerStrategy = new FakeStrategy();


        SemanticsIdentifierStrategy identifierStrategy = new SemanticsIdentifierStrategy();
        identifierStrategy.SymbolTable = new HashSymbolTable();

        IdentifierNode.FirstChild = PrefixNode;

        IdentifierNode.SemanticsCheckerStrategy = identifierStrategy;


        identifierStrategy.SymbolTable.EnterSymbol(IdentifierNode.Value, IdentifierNode.DataType,true);

        Exception exception = Assertions.assertThrows(SemanticsException.class, IdentifierNode::CheckSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));

    }

    @Test
    void SemanticsIdentifierStrategy_No_Prefix_BoolType_Returns_Not_Declared() {
        Node IdentifierNode = Node.MakeNode(Node.NodeType.Identifier);
        IdentifierNode.Value = "Test";
        IdentifierNode.DataType = Symbol.SymbolType.BOOL;
        Node PrefixNode = Node.MakeNode(Node.NodeType.Empty);

        // The string expected as return from the error
        String errorString = " not been declared";

        PrefixNode.SemanticsCheckerStrategy = new FakeStrategy();


        SemanticsIdentifierStrategy identifierStrategy = new SemanticsIdentifierStrategy();
        identifierStrategy.SymbolTable = new HashSymbolTable();

        IdentifierNode.FirstChild = PrefixNode;

        IdentifierNode.SemanticsCheckerStrategy = identifierStrategy;


        Exception exception = Assertions.assertThrows(SemanticsException.class, IdentifierNode::CheckSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));

    }

    @Test
    void SemanticsIdentifierStrategy_No_Prefix_BoolType_Returns_Not_Instantiated() {
        Node IdentifierNode = Node.MakeNode(Node.NodeType.Identifier);
        IdentifierNode.Value = "Test";
        IdentifierNode.DataType = Symbol.SymbolType.BOOL;
        Node PrefixNode = Node.MakeNode(Node.NodeType.Empty);

        // The string expected as return from the error
        String errorString = "must be instantiated";

        PrefixNode.SemanticsCheckerStrategy = new FakeStrategy();


        SemanticsIdentifierStrategy identifierStrategy = new SemanticsIdentifierStrategy();
        identifierStrategy.SymbolTable = new HashSymbolTable();

        IdentifierNode.FirstChild = PrefixNode;

        IdentifierNode.SemanticsCheckerStrategy = identifierStrategy;

        identifierStrategy.SymbolTable.EnterSymbol(IdentifierNode.Value, IdentifierNode.DataType,false);


        Exception exception = Assertions.assertThrows(SemanticsException.class, IdentifierNode::CheckSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));

    }

    @Test
    void SemanticsIfStrategy_If_Statement_With_Block_Returns_No_Errors() throws SemanticsException {
        Node IfNode = Node.MakeNode(Node.NodeType.If);
        Node BoolExpression = Node.MakeNode(Node.NodeType.Expression);
        BoolExpression.DataType = Symbol.SymbolType.BOOL;
        Node BoolNode = Node.MakeNode(Node.NodeType.BoolLiteral);
        Node BlockNode = Node.MakeNode(Node.NodeType.Block);

        BoolExpression.SemanticsCheckerStrategy = new FakeStrategy();
        BoolNode.SemanticsCheckerStrategy = new FakeStrategy();
        BlockNode.SemanticsCheckerStrategy = new FakeStrategy();


        SemanticsIfStrategy ifStrategy = new SemanticsIfStrategy();
        ifStrategy.SymbolTable = new HashSymbolTable();

        BoolNode.Next = BlockNode;
        BoolExpression.Next = BoolNode;
        IfNode.FirstChild = BoolExpression;

        IfNode.SemanticsCheckerStrategy = ifStrategy;


        ifStrategy.SymbolTable.EnterSymbol(IfNode.Value, IfNode.DataType);


        IfNode.CheckSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);

    }

    @Test
    void SemanticsIfStrategy_No_Boolean_Returns_Boolean_Error() {
        Node IfNode = Node.MakeNode(Node.NodeType.If);
        Node BoolExpression = Node.MakeNode(Node.NodeType.Expression);
        BoolExpression.DataType = Symbol.SymbolType.INT;
        Node BoolNode = Node.MakeNode(Node.NodeType.BoolLiteral);
        Node BlockNode = Node.MakeNode(Node.NodeType.Block);

        // The string expected as return from the error
        String errorString = "If statement";

        BoolExpression.SemanticsCheckerStrategy = new FakeStrategy();
        BoolNode.SemanticsCheckerStrategy = new FakeStrategy();
        BlockNode.SemanticsCheckerStrategy = new FakeStrategy();


        SemanticsIfStrategy ifStrategy = new SemanticsIfStrategy();
        ifStrategy.SymbolTable = new HashSymbolTable();

        BoolNode.Next = BlockNode;
        BoolExpression.Next = BoolNode;
        IfNode.FirstChild = BoolExpression;

        IfNode.SemanticsCheckerStrategy = ifStrategy;


        ifStrategy.SymbolTable.EnterSymbol(IfNode.Value, IfNode.DataType);

        Exception exception = Assertions.assertThrows(SemanticsException.class, IfNode::CheckSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));

    }

    @Test
    void SemanticsIfStrategy_Nested_If_Returns_No_Errors() throws SemanticsException {
        Node IfNode = Node.MakeNode(Node.NodeType.If);
        Node BoolExpression = Node.MakeNode(Node.NodeType.Expression);
        BoolExpression.DataType = Symbol.SymbolType.BOOL;
        Node BoolNode = Node.MakeNode(Node.NodeType.BoolLiteral);

        Node NestedIfNode = Node.MakeNode(Node.NodeType.If);

        Node NestedBoolNode = Node.MakeNode(Node.NodeType.Expression);
        NestedBoolNode.DataType = Symbol.SymbolType.BOOL;
        Node NestedBlockNode = Node.MakeNode(Node.NodeType.Block);


        BoolExpression.SemanticsCheckerStrategy = new FakeStrategy();
        BoolNode.SemanticsCheckerStrategy = new FakeStrategy();
        NestedIfNode.SemanticsCheckerStrategy = new SemanticsIfStrategy();
        NestedBoolNode.SemanticsCheckerStrategy = new FakeStrategy();
        NestedBlockNode.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsIfStrategy ifStrategy = new SemanticsIfStrategy();
        ifStrategy.SymbolTable = new HashSymbolTable();


        NestedBoolNode.Next = NestedBlockNode;

        NestedIfNode.FirstChild = NestedBoolNode;

        BoolNode.Next = NestedIfNode;
        BoolExpression.Next = BoolNode;
        IfNode.FirstChild = BoolExpression;

        IfNode.SemanticsCheckerStrategy = ifStrategy;


        ifStrategy.SymbolTable.EnterSymbol(IfNode.Value, IfNode.DataType);

        IfNode.CheckSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);

    }


}

class FakeStrategy extends SemanticsCheckerStrategy {
    @Override
    public void CheckSemantics(Node node) {
        // Do nothing
        // This function is made to fake any strategy not needed for specific tests, to limit the scope of the tests.
    }
}


