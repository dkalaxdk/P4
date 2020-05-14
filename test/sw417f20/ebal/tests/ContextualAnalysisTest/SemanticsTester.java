package sw417f20.ebal.tests.ContextualAnalysisTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sw417f20.ebal.ContextAnalysis.HashSymbolTable;
import sw417f20.ebal.ContextAnalysis.SemanticsStrategyAssigner;
import sw417f20.ebal.ContextAnalysis.Strategies.*;
import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;

import static sw417f20.ebal.ContextAnalysis.Strategies.SemanticsCheckerStrategy.ErrorType;

public class SemanticsTester {
    SemanticsStrategyAssigner strategyAssigner;

    private class TestNode {
        public Node Node;

        public TestNode(Node node) {
            this.Node = node;
        }

        public void AddStrategy(SemanticsCheckerStrategy strategy) {
            strategy.SymbolTable = new HashSymbolTable();
            Node.SemanticsCheckerStrategy = strategy;
        }

        public void AddSymbol(Node symbol) {
            Node.SemanticsCheckerStrategy.SymbolTable.EnterSymbol(symbol.Value, symbol.DataType);
        }

        public void AddSymbolWithRefNode(Node symbol, Node referenceNode) {
            Node.SemanticsCheckerStrategy.SymbolTable.EnterSymbol(symbol.Value, symbol.DataType, referenceNode);
        }

        // Simplified addChild method
        public void AddChild(Node child) {
            // Adds a fake strategy to all children
            child.SemanticsCheckerStrategy = new FakeStrategy();

            if (Node.FirstChild == null) {
                Node.FirstChild = child;
            } else {
                Node currentChild = Node.FirstChild;
                // Find last child in linked list
                while (currentChild.Next != null) {
                    currentChild = currentChild.Next;
                }
                currentChild.Next = child;
            }
        }
    }

    private String DefaultErrorMessage(ErrorType errorType) {
        switch (errorType) {
            case AlreadyDeclared:
                return " has already been declared.";
            case NotDeclared:
                return " has not been declared.";
            case WrongType:
                return ": wrong type";
            case Default:
                return ": error";
        }

        return "Error does not exist";
    }

    private TestNode SetupAssignmentNodeWithSymbols(Symbol.SymbolType expressionType, Symbol.SymbolType identifierType) {
        TestNode testNode = SetupAssignmentNodeWithoutSymbols(expressionType, identifierType);

        // Add identifier symbol to symbol table of strategy
        testNode.AddSymbol(testNode.Node.FirstChild);

        return testNode;
    }

    private TestNode SetupAssignmentNodeWithoutSymbols(Symbol.SymbolType expressionType, Symbol.SymbolType identifierType) {
        Node AssignmentNode = Node.MakeNode(Node.NodeType.Assignment);

        SemanticsAssignmentStrategy assignmentStrategy = new SemanticsAssignmentStrategy();

        TestNode testNode = SetupNode(AssignmentNode, assignmentStrategy);

        Node IdentifierNode = CreateNode(Node.NodeType.Identifier, identifierType, "TestVar");
        Node ExpressionNode = CreateNode(Node.NodeType.Expression, expressionType);
        testNode.AddChild(IdentifierNode);
        testNode.AddChild(ExpressionNode);

        return testNode;
    }

    private TestNode SetupBoolDeclNodeWithoutSymbols(Symbol.SymbolType expressionType, Symbol.SymbolType identifierType) {
        Node DeclarationNode = Node.MakeNode(Node.NodeType.BoolDeclaration);

        SemanticsBoolDeclarationStrategy boolDeclarationStrategy = new SemanticsBoolDeclarationStrategy();
        TestNode testNode = SetupNode(DeclarationNode, boolDeclarationStrategy);

        // One liner construction
        //TestNode testNode = setupNode(Node.MakeNode(Node.NodeType.BoolDeclaration), new SemanticsBoolDeclarationStrategy());

        Node IdentifierNode = CreateNode(Node.NodeType.Identifier, identifierType, "TestVar");
        Node ExpressionNode = CreateNode(Node.NodeType.Expression, expressionType);
        testNode.AddChild(IdentifierNode);
        testNode.AddChild(ExpressionNode);

        return testNode;
    }

    private TestNode SetupBoolLiteralNode(Node prefixNode) {
        Node BoolNode = Node.MakeNode(Node.NodeType.BoolDeclaration);
        SemanticsBoolLiteralStrategy boolLiteralStrategy = new SemanticsBoolLiteralStrategy();

        TestNode testNode = SetupNode(BoolNode, boolLiteralStrategy);

        testNode.AddChild(prefixNode);
        return testNode;
    }

    private TestNode SetupEventDeclNode() {
        Node DeclarationNode = Node.MakeNode(Node.NodeType.EventDeclaration);
        SemanticsEventDeclarationStrategy eventDeclarationStrategy = new SemanticsEventDeclarationStrategy();
        eventDeclarationStrategy.LocalEvents = new ArrayList<Symbol>();

        return SetupNode(DeclarationNode, eventDeclarationStrategy);
    }

    //TODO: Determine if obsolete
    private TestNode SetupNode(Node rootNode, SemanticsCheckerStrategy strategy) {
        TestNode testNode = new TestNode(rootNode);
        testNode.AddStrategy(strategy);

        return testNode;
    }

    private Node CreateNode(Node.NodeType nodeType, Symbol.SymbolType dataType, String value) {
        Node node = CreateNode(nodeType, dataType);
        node.Value = value;
        return node;
    }

    private Node CreateNode(Node.NodeType nodeType, Symbol.SymbolType dataType) {
        Node node = Node.MakeNode(nodeType);
        node.DataType = dataType;
        return node;
    }

    @BeforeEach
    void setup() {
        strategyAssigner = new SemanticsStrategyAssigner();
    }

    //region AssignmentStrategy tests
    @Test
    void AssignmentStrategy_TypeInt_Returns_NoErrorsThrown() throws SemanticsException {
        TestNode AssignmentNode = SetupAssignmentNodeWithSymbols(Symbol.SymbolType.INT, Symbol.SymbolType.INT);

        AssignmentNode.Node.CheckSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void AssignmentStrategy_TypeFloat_Returns_NoErrorsThrown() throws SemanticsException {
        TestNode AssignmentNode = SetupAssignmentNodeWithSymbols(Symbol.SymbolType.FLOAT, Symbol.SymbolType.FLOAT);

        AssignmentNode.Node.CheckSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void AssignmentStrategy_TypeBool_Returns_NoErrorsThrown() throws SemanticsException {
        TestNode AssignmentNode = SetupAssignmentNodeWithSymbols(Symbol.SymbolType.BOOL, Symbol.SymbolType.BOOL);

        AssignmentNode.Node.CheckSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void AssignmentStrategy_TypeInt_Returns_WrongTypeErrorThrown() {
        //The string expected as return from the error
        String errorString = "Incompatible types";

        TestNode AssignmentNode = SetupAssignmentNodeWithSymbols(Symbol.SymbolType.INT, Symbol.SymbolType.BOOL);

        Exception exception = Assertions.assertThrows(SemanticsException.class, AssignmentNode.Node::CheckSemantics);

        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }

    @Test
    void AssignmentStrategy_TypeInt_Returns_Error_NotDeclared() {
        //The string expected as return from the error
        String errorString = "not been declared";

        TestNode AssignmentNode = SetupAssignmentNodeWithoutSymbols(Symbol.SymbolType.INT, Symbol.SymbolType.INT);

        //In this test, the symbol has not been added to the symbol table, and therefor it should fail.
        Exception exception = Assertions.assertThrows(SemanticsException.class, AssignmentNode.Node::CheckSemantics);

        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }
    //endregion

    //region BoolDeclarationStrategy tests
    @Test
    void BoolDeclarationStrategy_Correct_Returns_NoErrors() throws SemanticsException {
        TestNode DeclarationNode = SetupBoolDeclNodeWithoutSymbols(Symbol.SymbolType.BOOL, Symbol.SymbolType.BOOL);

        DeclarationNode.Node.CheckSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void BoolDeclarationStrategy_Correct_Returns_AlreadyDefined() {
        //The string expected as return from the error
        String errorString = "has already been declared";

        TestNode DeclarationNode = SetupBoolDeclNodeWithoutSymbols(Symbol.SymbolType.BOOL, Symbol.SymbolType.BOOL);

        // Add identifier (which is the first child) to symbolTable
        DeclarationNode.AddSymbol(DeclarationNode.Node.FirstChild);

        Exception exception = Assertions.assertThrows(SemanticsException.class, DeclarationNode.Node::CheckSemantics);

        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }

    @Test
    void BoolDeclarationStrategy_Correct_Returns_WrongTypeDeclared() {
        //The string expected as return from the error
        String errorString = "wrong type";

        TestNode DeclarationNode = SetupBoolDeclNodeWithoutSymbols(Symbol.SymbolType.INT, Symbol.SymbolType.BOOL);

        Exception exception = Assertions.assertThrows(SemanticsException.class, DeclarationNode.Node::CheckSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }
    //endregion

    //region BoolLiteralStrategy tests
    @Test
    void BoolLiteralStrategy_No_Prefix_Returns_NoErrors() throws SemanticsException {
        // Empty prefix node
        Node PrefixNode = CreateNode(Node.NodeType.Empty, Symbol.SymbolType.INT);

        TestNode BoolNode = SetupBoolLiteralNode(PrefixNode);

        BoolNode.Node.CheckSemantics();
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void BoolLiteralStrategy_Correct_Prefix_Returns_NoErrors() throws SemanticsException {
        // PrefixNot
        Node PrefixNode = CreateNode(Node.NodeType.PrefixNot, Symbol.SymbolType.INT);

        TestNode BoolNode = SetupBoolLiteralNode(PrefixNode);

        BoolNode.Node.CheckSemantics();
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void BoolLiteralStrategy_Wrong_Prefix_Returns_Errors() {
        //The string expected as return from the error
        String errorString = "Only not prefix";

        // PrefixMinus
        Node PrefixNode = CreateNode(Node.NodeType.PrefixMinus, Symbol.SymbolType.INT);

        TestNode BoolNode = SetupBoolLiteralNode(PrefixNode);

        Exception exception = Assertions.assertThrows(SemanticsException.class, BoolNode.Node::CheckSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }
    //endregion

    //region EventDeclarationStrategy tests
    @Test
    void EventDeclaration_Correct_Returns_NoErrors() throws SemanticsException {
        TestNode DeclarationNode = SetupEventDeclNode();

        Node ExpressionNode = Node.MakeNode(Node.NodeType.Call);
        Node IdentifierNode = Node.MakeNode(Node.NodeType.Identifier);
        Node CreateEventNode = Node.MakeNode(Node.NodeType.CreateEvent);
        Node ExpressionChild = Node.MakeNode(Node.NodeType.Expression);

        ExpressionNode.FirstChild = CreateEventNode;
        ExpressionNode.FirstChild.Next = ExpressionChild;
        IdentifierNode.Value = "TestVar";

        DeclarationNode.AddChild(IdentifierNode);
        DeclarationNode.AddChild(ExpressionNode);

        DeclarationNode.Node.CheckSemantics();

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
        eventDeclarationStrategy.LocalEvents = new ArrayList<>();

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
        //The string expected as return from the error
        String errorString = "Illegal declaration";

        TestNode DeclarationNode = SetupEventDeclNode();

        Node ExpressionNode = Node.MakeNode(Node.NodeType.Identifier);
        Node IdentifierNode = Node.MakeNode(Node.NodeType.Identifier);
        Node CreateEventNode = Node.MakeNode(Node.NodeType.CreateEvent);
        Node ExpressionChild = Node.MakeNode(Node.NodeType.Expression);

        ExpressionNode.FirstChild = CreateEventNode;
        ExpressionNode.FirstChild.Next = ExpressionChild;
        IdentifierNode.Value = "TestVar";

        DeclarationNode.AddChild(IdentifierNode);
        DeclarationNode.AddChild(ExpressionNode);

        Exception exception = Assertions.assertThrows(SemanticsException.class, DeclarationNode.Node::CheckSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }
    //endregion

    //region EventHandlerBlockStrategy tests
    @Test
    void EventHandlerBlockStrategy_Returns_No_Errors() throws SemanticsException {
        // TODO: Refactor by creating setupEventHandler
        Node EventHandlerNode = Node.MakeNode(Node.NodeType.EventHandler);
        SemanticsEventHandlerBlockStrategy eventDeclarationStrategy = new SemanticsEventHandlerBlockStrategy();

        TestNode testNode = SetupNode(EventHandlerNode, eventDeclarationStrategy);

        Node BlockNode = Node.MakeNode(Node.NodeType.Block);
        testNode.AddChild(BlockNode);
        BlockNode.SemanticsCheckerStrategy = new FakeStrategy();

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
    //endregion

    //region EventHandlerCallStrategy tests
    @Test
    void EventHandlerCallStrategy_CheckWrite_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Call));
        SemanticsEventHandlerCallStrategy strategy = new SemanticsEventHandlerCallStrategy();
        testNode.AddStrategy(strategy);

        Node writeNode = Node.MakeNode(Node.NodeType.Write);
        Node pinIdentifierNode = Node.MakeNode(Node.NodeType.Identifier);
        pinIdentifierNode.DataType = Symbol.SymbolType.PIN;
        testNode.AddSymbol(pinIdentifierNode);
        Node intLiteralNode = Node.MakeNode(Node.NodeType.IntLiteral);
        intLiteralNode.DataType = Symbol.SymbolType.INT;

        testNode.AddChild(writeNode);
        testNode.AddChild(pinIdentifierNode);
        testNode.AddChild(intLiteralNode);

        //Act
        testNode.Node.CheckSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void EventHandlerCallStrategy_CheckWrite_Returns_SecondParameterWrongTypeError() {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Call));
        SemanticsEventHandlerCallStrategy strategy = new SemanticsEventHandlerCallStrategy();
        testNode.AddStrategy(strategy);

        Node writeNode = Node.MakeNode(Node.NodeType.Write);
        Node pinIdentifierNode = Node.MakeNode(Node.NodeType.Identifier);
        pinIdentifierNode.DataType = Symbol.SymbolType.PIN;
        testNode.AddSymbol(pinIdentifierNode);
        Node intLiteralNode = Node.MakeNode(Node.NodeType.IntLiteral);
        intLiteralNode.DataType = Symbol.SymbolType.BOOL;   //Bad type

        testNode.AddChild(writeNode);
        testNode.AddChild(pinIdentifierNode);
        testNode.AddChild(intLiteralNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        Assertions.assertTrue(
                exception.getMessage().contains("Second parameter") &&
                exception.getMessage().contains(DefaultErrorMessage(ErrorType.WrongType))
        );
    }

    @Test
    void EventHandlerCallStrategy_CheckWrite_Returns_WrongTypeError() {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Call));
        SemanticsEventHandlerCallStrategy strategy = new SemanticsEventHandlerCallStrategy();
        testNode.AddStrategy(strategy);

        Node writeNode = Node.MakeNode(Node.NodeType.Write);
        Node pinIdentifierNode = Node.MakeNode(Node.NodeType.Identifier);
        pinIdentifierNode.DataType = Symbol.SymbolType.EVENT;   //Bad type
        testNode.AddSymbol(pinIdentifierNode);
        Node intLiteralNode = Node.MakeNode(Node.NodeType.IntLiteral);
        intLiteralNode.DataType = Symbol.SymbolType.INT;

        testNode.AddChild(writeNode);
        testNode.AddChild(pinIdentifierNode);
        testNode.AddChild(intLiteralNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(DefaultErrorMessage(ErrorType.WrongType)));
    }

    @Test
    void EventHandlerCallStrategy_CheckWrite_Returns_NotDeclaredError() {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Call));
        SemanticsEventHandlerCallStrategy strategy = new SemanticsEventHandlerCallStrategy();
        testNode.AddStrategy(strategy);

        Node writeNode = Node.MakeNode(Node.NodeType.Write);
        Node pinIdentifierNode = Node.MakeNode(Node.NodeType.Identifier);
        pinIdentifierNode.DataType = Symbol.SymbolType.PIN;
        //testNode.addSymbol(pinIdentifierNode);
        Node intLiteralNode = Node.MakeNode(Node.NodeType.IntLiteral);
        intLiteralNode.DataType = Symbol.SymbolType.INT;

        testNode.AddChild(writeNode);
        testNode.AddChild(pinIdentifierNode);
        testNode.AddChild(intLiteralNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(DefaultErrorMessage(ErrorType.NotDeclared)));
    }

    @Test
    void EventHandlerCallStrategy_CheckGetValue_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Call));
        SemanticsEventHandlerCallStrategy strategy = new SemanticsEventHandlerCallStrategy();
        String identifier = "eventName";
        strategy.AvailablePinOrEvent = identifier;
        testNode.AddStrategy(strategy);

        Node getValueNode = Node.MakeNode(Node.NodeType.GetValue);
        Node identifierNode = Node.MakeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.EVENT;
        identifierNode.Value = identifier;
        testNode.AddSymbol(identifierNode);

        testNode.AddChild(getValueNode);
        testNode.AddChild(identifierNode);

        //Act
        testNode.Node.CheckSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void EventHandlerCallStrategy_CheckGetValue_Returns_WrongTypeError() {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Call));
        SemanticsEventHandlerCallStrategy strategy = new SemanticsEventHandlerCallStrategy();
        String identifier = "eventName";
        strategy.AvailablePinOrEvent = identifier;
        testNode.AddStrategy(strategy);

        Node getValueNode = Node.MakeNode(Node.NodeType.GetValue);
        Node identifierNode = Node.MakeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.VOID;   //Bad type
        identifierNode.Value = identifier;
        testNode.AddSymbol(identifierNode);

        testNode.AddChild(getValueNode);
        testNode.AddChild(identifierNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(DefaultErrorMessage(ErrorType.WrongType)));
    }

    @Test
    void EventHandlerCallStrategy_CheckGetValue_Returns_EventUnavailableError() {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Call));
        SemanticsEventHandlerCallStrategy strategy = new SemanticsEventHandlerCallStrategy();
        String identifier = "eventName";
        //strategy.AvailablePinOrEvent = identifier;
        testNode.AddStrategy(strategy);

        Node getValueNode = Node.MakeNode(Node.NodeType.GetValue);
        Node identifierNode = Node.MakeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.EVENT;
        identifierNode.Value = identifier;
        testNode.AddSymbol(identifierNode);

        testNode.AddChild(getValueNode);
        testNode.AddChild(identifierNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains("Event unavailable to EventHandler"));
    }

    @Test
    void EventHandlerCallStrategy_CheckGetValue_Returns_NotDeclaredError() {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Call));
        SemanticsEventHandlerCallStrategy strategy = new SemanticsEventHandlerCallStrategy();
        String identifier = "eventName";
        strategy.AvailablePinOrEvent = identifier;
        testNode.AddStrategy(strategy);

        Node getValueNode = Node.MakeNode(Node.NodeType.GetValue);
        Node identifierNode = Node.MakeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.EVENT;
        identifierNode.Value = identifier;
        //testNode.addSymbol(identifierNode);

        testNode.AddChild(getValueNode);
        testNode.AddChild(identifierNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(DefaultErrorMessage(ErrorType.NotDeclared)));
    }

    @Test
    void EventHandlerCallStrategy_Returns_IllegalFuncError() {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Call));
        SemanticsEventHandlerCallStrategy strategy = new SemanticsEventHandlerCallStrategy();
        testNode.AddStrategy(strategy);

        testNode.AddChild(Node.MakeNode(Node.NodeType.Broadcast));  //Bad function

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains("Illegal function call in EventHandler"));
    }
    //endregion

    //region FloatDeclarationStrategy tests
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
    //endregion

    //region FloatLiteralStrategy tests
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
    //endregion

    //region IdentifierStrategy tests
    @Test
    void IdentifierStrategy_No_Prefix_Returns_No_Errors() throws SemanticsException {
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
    void IdentifierStrategy_PrefixMinus_IntType_Returns_No_Errors() throws SemanticsException {
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
    void IdentifierStrategy_PrefixNot_BoolType_Returns_No_Errors() throws SemanticsException {
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
    void IdentifierStrategy_PrefixNot_IntType_Returns_Not_Only_Applicable_For_Boolean() {
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
    void IdentifierStrategy_PrefixMinus_BoolType_Returns_Minus_Not_Applicable_For_Boolean() {
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
    void IdentifierStrategy_No_Prefix_BoolType_Returns_Not_Declared() {
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
    void IdentifierStrategy_No_Prefix_BoolType_Returns_Not_Instantiated() {
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
    //endregion

    //region IfStrategy tests
    @Test
    void IfStrategy_If_Statement_With_Block_Returns_No_Errors() throws SemanticsException {
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
    void IfStrategy_No_Boolean_Returns_Boolean_Error() {
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
    void IfStrategy_Nested_If_Returns_No_Errors() throws SemanticsException {
        Node IfNode = Node.MakeNode(Node.NodeType.If);
        Node ExpressionNode = Node.MakeNode(Node.NodeType.Expression);
        ExpressionNode.DataType = Symbol.SymbolType.BOOL;
        Node BlockNode = Node.MakeNode(Node.NodeType.Block);
        //Node BoolNode = Node.MakeNode(Node.NodeType.BoolLiteral);

        Node NestedIfNode = Node.MakeNode(Node.NodeType.If);
        Node NestedExpressionNode = Node.MakeNode(Node.NodeType.Expression);
        NestedExpressionNode.DataType = Symbol.SymbolType.BOOL;
        Node NestedBlockNode = Node.MakeNode(Node.NodeType.Block);
        Node NestedEmptyNode = Node.MakeNode(Node.NodeType.Empty);

        ExpressionNode.SemanticsCheckerStrategy = new FakeStrategy();
        BlockNode.SemanticsCheckerStrategy = new FakeStrategy();
        //BoolNode.SemanticsCheckerStrategy = new FakeStrategy();
        NestedIfNode.SemanticsCheckerStrategy = new SemanticsIfStrategy();
        NestedExpressionNode.SemanticsCheckerStrategy = new FakeStrategy();
        NestedBlockNode.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsIfStrategy ifStrategy = new SemanticsIfStrategy();
        ifStrategy.SymbolTable = new HashSymbolTable();

        NestedBlockNode.Next = NestedEmptyNode;
        NestedExpressionNode.Next = NestedBlockNode;
        NestedIfNode.FirstChild = NestedExpressionNode;

        BlockNode.Next = NestedIfNode;
        ExpressionNode.Next = BlockNode;
        //BoolNode.Next = NestedIfNode;
        //ExpressionNode.Next = BoolNode;
        IfNode.FirstChild = ExpressionNode;

        IfNode.SemanticsCheckerStrategy = ifStrategy;

        ifStrategy.SymbolTable.EnterSymbol(IfNode.Value, IfNode.DataType);

        IfNode.CheckSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }
    //endregion

    //region SemanticsInitiateBlockStrategy tests
    @Test
    void InitiateBlockStrategy_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Block));
        SemanticsInitiateBlockStrategy strategy = new SemanticsInitiateBlockStrategy();
        strategy.UsedPinNumbers = new ArrayList<>();
        testNode.AddStrategy(strategy);
        Node pinDeclNode = Node.MakeNode(Node.NodeType.PinDeclaration);
        pinDeclNode.Next = Node.MakeNode(Node.NodeType.Empty);
        testNode.AddChild(pinDeclNode);

        //Act
        testNode.Node.CheckSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void InitiateBlockStrategy_Returns_NotPinDeclError() {
        //Error being checked for
        String errorMessage = "Only pin declarations allowed in Initiate";

        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Block));
        SemanticsInitiateBlockStrategy strategy = new SemanticsInitiateBlockStrategy();
        strategy.UsedPinNumbers = new ArrayList<>();
        testNode.AddStrategy(strategy);
        Node notPinDeclNode = Node.MakeNode(Node.NodeType.BoolDeclaration);
        notPinDeclNode.Next = Node.MakeNode(Node.NodeType.Empty);
        testNode.AddChild(notPinDeclNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(errorMessage));
    }
    //endregion

    //region SemanticsInitiateStrategy tests
    @Test
    void InitiateStrategy_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Initiate));
        testNode.AddStrategy(new SemanticsInitiateStrategy());
        Node blockNode = Node.MakeNode(Node.NodeType.Block);
        testNode.AddChild(blockNode);

        //Act
        testNode.Node.CheckSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }
    //endregion

    //region SemanticsIntDeclarationStrategy tests
    @Test
    void IntDeclarationStrategy_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.IntDeclaration));
        testNode.AddStrategy(new SemanticsIntDeclarationStrategy());
        Node identifierNode = Node.MakeNode(Node.NodeType.Identifier);
        testNode.AddChild(identifierNode);
        Node expressionNode = Node.MakeNode(Node.NodeType.Expression);
        expressionNode.DataType = Symbol.SymbolType.INT;    //Could also be a float
        testNode.AddChild(expressionNode);

        //Act
        testNode.Node.CheckSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void IntDeclarationStrategy_Returns_WrongTypeError() {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.IntDeclaration));
        testNode.AddStrategy(new SemanticsIntDeclarationStrategy());
        Node identifierNode = Node.MakeNode(Node.NodeType.Identifier);
        testNode.AddChild(identifierNode);
        Node badExpressionNode = Node.MakeNode(Node.NodeType.Expression);
        badExpressionNode.DataType = Symbol.SymbolType.BOOL;
        testNode.AddChild(badExpressionNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(DefaultErrorMessage(ErrorType.WrongType)));
    }

    @Test
    void IntDeclarationStrategy_Returns_AlreadyDeclaredError() {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.IntDeclaration));
        testNode.AddStrategy(new SemanticsIntDeclarationStrategy());
        Node identifierNode = Node.MakeNode(Node.NodeType.Identifier);
        testNode.AddChild(identifierNode);
        Node expressionNode = Node.MakeNode(Node.NodeType.Expression);
        expressionNode.DataType = Symbol.SymbolType.INT;    //Could also be a float
        testNode.AddChild(expressionNode);
        testNode.AddSymbol(expressionNode);
        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(DefaultErrorMessage(ErrorType.AlreadyDeclared)));
    }
    //endregion

    //region SemanticsIntLiteralStrategy tests
    @Test
    void IntLiteralStrategy_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.IntLiteral));
        testNode.AddStrategy(new SemanticsIntLiteralStrategy());
        Node emptyNode = Node.MakeNode(Node.NodeType.Empty);
        testNode.AddChild(emptyNode);

        //Act
        testNode.Node.CheckSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void IntLiteralStrategy_Returns_NotMinusPrefixError() {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.IntLiteral));
        testNode.AddStrategy(new SemanticsIntLiteralStrategy());
        Node badPrefixNode = Node.MakeNode(Node.NodeType.PrefixNot);
        testNode.AddChild(badPrefixNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        String errorMessage = "Only minus prefix (-) applicable to int data type.";
        Assertions.assertTrue(exception.getMessage().contains(errorMessage));
    }
    //endregion

    //region SemanticsListenerBlockStrategy tests
    @Test
    void ListenerBlockStrategy_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Block));
        testNode.AddStrategy(new SemanticsListenerBlockStrategy());
        testNode.AddChild(Node.MakeNode(Node.NodeType.IntDeclaration));
        testNode.AddChild(Node.MakeNode(Node.NodeType.Empty));

        //Act
        testNode.Node.CheckSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void ListenerBlockStrategy_Returns_IsPinDeclError() {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Block));
        testNode.AddStrategy(new SemanticsListenerBlockStrategy());
        testNode.AddChild(Node.MakeNode(Node.NodeType.PinDeclaration));
        testNode.AddChild(Node.MakeNode(Node.NodeType.Empty));

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        String errorString = "No pin declarations allowed in Listener";
        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }
    //endregion

    //region SemanticsListenerCallStrategy tests
    @Test
    void ListenerCallStrategy_CheckBroadcast_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Call));
        SemanticsListenerCallStrategy strategy = new SemanticsListenerCallStrategy();
        strategy.BroadcastEvents = new ArrayList<>();
        testNode.AddStrategy(strategy);
        testNode.AddChild(Node.MakeNode(Node.NodeType.Broadcast));
        Node identifierNode = Node.MakeNode(Node.NodeType.Identifier);
        identifierNode.Value = "eventName";
        identifierNode.DataType = Symbol.SymbolType.EVENT;
        testNode.AddSymbol(identifierNode);
        testNode.AddChild(identifierNode);

        //Act
        testNode.Node.CheckSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void ListenerCallStrategy_CheckBroadcast_Returns_WrongTypeError() {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Call));
        SemanticsListenerCallStrategy strategy = new SemanticsListenerCallStrategy();
        strategy.BroadcastEvents = new ArrayList<>();
        testNode.AddStrategy(strategy);
        testNode.AddChild(Node.MakeNode(Node.NodeType.Broadcast));
        Node identifierNode = Node.MakeNode(Node.NodeType.Identifier);
        identifierNode.Value = "eventName";
        identifierNode.DataType = Symbol.SymbolType.PIN;    //Bad type
        testNode.AddSymbol(identifierNode);
        testNode.AddChild(identifierNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(DefaultErrorMessage(ErrorType.WrongType)));
    }

    @Test
    void ListenerCallStrategy_CheckBroadcast_Returns_NotDeclaredError() {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Call));
        SemanticsListenerCallStrategy strategy = new SemanticsListenerCallStrategy();
        strategy.BroadcastEvents = new ArrayList<>();
        testNode.AddStrategy(strategy);
        testNode.AddChild(Node.MakeNode(Node.NodeType.Broadcast));
        Node identifierNode = Node.MakeNode(Node.NodeType.Identifier);
        identifierNode.Value = "eventName";
        identifierNode.DataType = Symbol.SymbolType.EVENT;
        //testNode.addSymbol(identifierNode);
        testNode.AddChild(identifierNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(DefaultErrorMessage(ErrorType.NotDeclared)));
    }

    @Test
    void ListenerCallStrategy_CheckGetValue_Pin_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Call));
        SemanticsListenerCallStrategy strategy = new SemanticsListenerCallStrategy();
        String pinName = "pinName";
        strategy.AvailablePinOrEvent = pinName;
        testNode.AddStrategy(strategy);
        testNode.AddChild(Node.MakeNode(Node.NodeType.GetValue));
        Node identifierNode = Node.MakeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.PIN;
        identifierNode.Value = pinName;
        testNode.AddSymbol(identifierNode);
        testNode.AddChild(identifierNode);

        //Act
        testNode.Node.CheckSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void ListenerCallStrategy_CheckGetValue_Event_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Call));
        SemanticsListenerCallStrategy strategy = new SemanticsListenerCallStrategy();
        String eventName = "eventName";
        strategy.AvailablePinOrEvent = eventName;
        testNode.AddStrategy(strategy);
        testNode.AddChild(Node.MakeNode(Node.NodeType.GetValue));
        Node identifierNode = Node.MakeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.EVENT;
        identifierNode.Value = eventName;
        testNode.AddSymbol(identifierNode);
        testNode.AddChild(identifierNode);

        //Act
        testNode.Node.CheckSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void ListenerCallStrategy_CheckGetValue_Returns_WrongTypeError() {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Call));
        SemanticsListenerCallStrategy strategy = new SemanticsListenerCallStrategy();
        String pinName = "pinName";
        strategy.AvailablePinOrEvent = pinName;
        testNode.AddStrategy(strategy);
        testNode.AddChild(Node.MakeNode(Node.NodeType.GetValue));
        Node identifierNode = Node.MakeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.BOOL;   //Bad type
        identifierNode.Value = pinName;
        testNode.AddSymbol(identifierNode);
        testNode.AddChild(identifierNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(DefaultErrorMessage(ErrorType.WrongType)));
    }

    @Test
    void ListenerCallStrategy_CheckGetValue_Returns_PinUnavailableError() {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Call));
        SemanticsListenerCallStrategy strategy = new SemanticsListenerCallStrategy();
        String pinName = "pinName";
        //strategy.AvailablePinOrEvent = pinName;
        testNode.AddStrategy(strategy);
        testNode.AddChild(Node.MakeNode(Node.NodeType.GetValue));
        Node identifierNode = Node.MakeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.PIN;
        identifierNode.Value = pinName;
        testNode.AddSymbol(identifierNode);
        testNode.AddChild(identifierNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains("Pin unavailable to Listener"));
    }

    @Test
    void ListenerCallStrategy_CheckGetValue_Returns_NotDeclaredError() {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Call));
        SemanticsListenerCallStrategy strategy = new SemanticsListenerCallStrategy();
        String pinName = "pinName";
        strategy.AvailablePinOrEvent = pinName;
        testNode.AddStrategy(strategy);
        testNode.AddChild(Node.MakeNode(Node.NodeType.GetValue));
        Node identifierNode = Node.MakeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.PIN;
        identifierNode.Value = pinName;
        //testNode.addSymbol(identifierNode);
        testNode.AddChild(identifierNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        System.out.println(exception.getMessage() + " " + ErrorType.NotDeclared.ordinal());
        Assertions.assertTrue(exception.getMessage().contains(DefaultErrorMessage(ErrorType.NotDeclared)));
    }

    @Test
    void ListenerCallStrategy_CheckFilterNoise_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Call));
        SemanticsListenerCallStrategy strategy = new SemanticsListenerCallStrategy();
        String pinName = "pinName";
        strategy.AvailablePinOrEvent = pinName;
        testNode.AddStrategy(strategy);
        testNode.AddChild(Node.MakeNode(Node.NodeType.FilterNoise));
        Node identifierNode = Node.MakeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.PIN;
        identifierNode.Value = pinName;
        testNode.AddSymbolWithRefNode(identifierNode, Node.MakeNode(Node.NodeType.Digital));
        testNode.AddChild(identifierNode);
        testNode.AddChild(Node.MakeNode(Node.NodeType.Debounce));

        //Act
        testNode.Node.CheckSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void ListenerCallStrategy_CheckFilterNoise_CheckPinAndFilterCombination_Returns_DigitalPinRangeError() {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Call));
        SemanticsListenerCallStrategy strategy = new SemanticsListenerCallStrategy();
        String pinName = "pinName";
        strategy.AvailablePinOrEvent = pinName;
        testNode.AddStrategy(strategy);
        testNode.AddChild(Node.MakeNode(Node.NodeType.FilterNoise));
        Node identifierNode = Node.MakeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.PIN;
        identifierNode.Value = pinName;
        testNode.AddSymbolWithRefNode(identifierNode, Node.MakeNode(Node.NodeType.Digital));
        testNode.AddChild(identifierNode);
        testNode.AddChild(Node.MakeNode(Node.NodeType.Range));

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains("Noise from digital pin cannot be filtered as ranged"));
    }

    @Test
    void ListenerCallStrategy_CheckFilterNoise_CheckPinAndFilterCombination_Returns_AnalogPinNotRangeError() {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Call));
        SemanticsListenerCallStrategy strategy = new SemanticsListenerCallStrategy();
        String pinName = "pinName";
        strategy.AvailablePinOrEvent = pinName;
        testNode.AddStrategy(strategy);
        testNode.AddChild(Node.MakeNode(Node.NodeType.FilterNoise));
        Node identifierNode = Node.MakeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.PIN;
        identifierNode.Value = pinName;
        testNode.AddSymbolWithRefNode(identifierNode, Node.MakeNode(Node.NodeType.Analog));
        testNode.AddChild(identifierNode);
        testNode.AddChild(Node.MakeNode(Node.NodeType.Debounce));

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains("Noise from analog pin can only be filtered as ranged"));
    }

    @Test
    void ListenerCallStrategy_CheckFilterNoise_Returns_WrongTypeError() {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Call));
        SemanticsListenerCallStrategy strategy = new SemanticsListenerCallStrategy();
        String pinName = "pinName";
        strategy.AvailablePinOrEvent = pinName;
        testNode.AddStrategy(strategy);
        testNode.AddChild(Node.MakeNode(Node.NodeType.FilterNoise));
        Node identifierNode = Node.MakeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.BOOL;   //Bad type
        identifierNode.Value = pinName;
        testNode.AddSymbolWithRefNode(identifierNode, Node.MakeNode(Node.NodeType.Digital));
        testNode.AddChild(identifierNode);
        testNode.AddChild(Node.MakeNode(Node.NodeType.Debounce));

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(DefaultErrorMessage(ErrorType.WrongType)));
    }

    @Test
    void ListenerCallStrategy_CheckFilterNoise_Returns_NotDeclaredError() {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Call));
        SemanticsListenerCallStrategy strategy = new SemanticsListenerCallStrategy();
        String pinName = "pinName";
        strategy.AvailablePinOrEvent = pinName;
        testNode.AddStrategy(strategy);
        testNode.AddChild(Node.MakeNode(Node.NodeType.FilterNoise));
        Node identifierNode = Node.MakeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.PIN;   //Bad type
        identifierNode.Value = pinName;
        //testNode.addSymbolWithRefNode(identifierNode, Node.makeNode(Node.NodeType.Digital));
        testNode.AddChild(identifierNode);
        testNode.AddChild(Node.MakeNode(Node.NodeType.Debounce));

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(DefaultErrorMessage(ErrorType.NotDeclared)));
    }

    @Test
    void ListenerCallStrategy_CheckFilterNoise_Returns_PinUnavailableError() {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Call));
        SemanticsListenerCallStrategy strategy = new SemanticsListenerCallStrategy();
        String pinName = "pinName";
        //strategy.AvailablePinOrEvent = pinName;
        testNode.AddStrategy(strategy);
        testNode.AddChild(Node.MakeNode(Node.NodeType.FilterNoise));
        Node identifierNode = Node.MakeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.PIN;
        identifierNode.Value = pinName;
        testNode.AddSymbolWithRefNode(identifierNode, Node.MakeNode(Node.NodeType.Digital));
        testNode.AddChild(identifierNode);
        testNode.AddChild(Node.MakeNode(Node.NodeType.Debounce));

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains("Pin unavailable to Listener"));
    }

    @Test
    void ListenerCallStrategy_CheckCreateEvent_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Call));
        testNode.AddStrategy(new SemanticsListenerCallStrategy());
        testNode.AddChild(Node.MakeNode(Node.NodeType.CreateEvent));
        Node identifierNode = Node.MakeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.INT;
        testNode.AddChild(identifierNode);

        //Act
        testNode.Node.CheckSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void ListenerCallStrategy_CheckCreateEvent_Returns_WrongTypeError() {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Call));
        testNode.AddStrategy(new SemanticsListenerCallStrategy());
        testNode.AddChild(Node.MakeNode(Node.NodeType.CreateEvent));
        Node identifierNode = Node.MakeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.VOID;   //Bad type
        testNode.AddChild(identifierNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(DefaultErrorMessage(ErrorType.WrongType)));
    }

    @Test
    void ListenerCallStrategy_Returns_IllegalFuncCallError() {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Call));
        testNode.AddStrategy(new SemanticsListenerCallStrategy());
        testNode.AddChild(Node.MakeNode(Node.NodeType.Write));

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains("Illegal function call in Listener"));
    }
    //endregion

    //region SemanticsListenerStrategy tests
    @Test
    void ListenerStrategy_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Listener));
        testNode.AddStrategy(new SemanticsListenerStrategy());
        Node identifierNode = Node.MakeNode((Node.NodeType.Identifier));
        identifierNode.Value = "pinName";
        identifierNode.DataType = Symbol.SymbolType.PIN;
        testNode.AddChild(identifierNode);
        testNode.AddSymbol(identifierNode);
        testNode.AddChild(Node.MakeNode(Node.NodeType.Block));

        //Act
        testNode.Node.CheckSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void ListenerStrategy_Returns_WrongTypeError() {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Listener));
        testNode.AddStrategy(new SemanticsListenerStrategy());
        Node identifierNode = Node.MakeNode((Node.NodeType.Identifier));
        identifierNode.Value = "pinName";
        identifierNode.DataType = Symbol.SymbolType.BOOL;   //Bad type
        testNode.AddChild(identifierNode);
        testNode.AddSymbol(identifierNode);
        testNode.AddChild(Node.MakeNode(Node.NodeType.Block));

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(DefaultErrorMessage(ErrorType.WrongType)));
    }

    @Test
    void ListenerStrategy_Returns_NotDeclaredError() {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Listener));
        testNode.AddStrategy(new SemanticsListenerStrategy());
        Node identifierNode = Node.MakeNode((Node.NodeType.Identifier));
        identifierNode.Value = "pinName";
        identifierNode.DataType = Symbol.SymbolType.PIN;
        testNode.AddChild(identifierNode);
        //testNode.addSymbol(identifierNode);
        testNode.AddChild(Node.MakeNode(Node.NodeType.Block));

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(DefaultErrorMessage(ErrorType.NotDeclared)));
    }
    //endregion

    //region SemanticsMasterInitiateCallStrategy tests
    @Test
    void MasterInitiateCallStrategy_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Call));
        SemanticsMasterInitiateCallStrategy strategy = new SemanticsMasterInitiateCallStrategy();
        strategy.UsedPinNumbers = new ArrayList<>();
        testNode.AddStrategy(strategy);
        Node createPinNode = Node.MakeNode(Node.NodeType.CreatePin);
        Node pinTypeNode = Node.MakeNode(Node.NodeType.Digital);
        Node ioTypeNode = Node.MakeNode(Node.NodeType.Input);
        Node pinNumberNode = Node.MakeNode(Node.NodeType.IntLiteral);
        pinNumberNode.Value = "0";
        ioTypeNode.Next = pinNumberNode;
        pinTypeNode.Next = ioTypeNode;
        createPinNode.Next = pinTypeNode;
        testNode.AddChild(createPinNode);

        //Act
        testNode.Node.CheckSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);

    }

    @Test
    void MasterInitiateCallStrategy_Returns_PinAlreadyUsedError() {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Call));
        SemanticsMasterInitiateCallStrategy strategy = new SemanticsMasterInitiateCallStrategy();
        strategy.UsedPinNumbers = new ArrayList<>();
        String pinNumber = "0";
        strategy.UsedPinNumbers.add(pinNumber);
        testNode.AddStrategy(strategy);
        Node createPinNode = Node.MakeNode(Node.NodeType.CreatePin);
        Node pinTypeNode = Node.MakeNode(Node.NodeType.Digital);
        Node ioTypeNode = Node.MakeNode(Node.NodeType.Input);
        Node pinNumberNode = Node.MakeNode(Node.NodeType.IntLiteral);
        pinNumberNode.Value = pinNumber;
        ioTypeNode.Next = pinNumberNode;
        pinTypeNode.Next = ioTypeNode;
        createPinNode.Next = pinTypeNode;
        testNode.AddChild(createPinNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains("Pin number already used"));
    }

    @Test
    void MasterInitiateCallStrategy_Returns_MasterPinPWMError() {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Call));
        SemanticsMasterInitiateCallStrategy strategy = new SemanticsMasterInitiateCallStrategy();
        strategy.UsedPinNumbers = new ArrayList<>();
        testNode.AddStrategy(strategy);
        Node createPinNode = Node.MakeNode(Node.NodeType.CreatePin);
        Node pinTypeNode = Node.MakeNode(Node.NodeType.PWM);    //PWM type is bad here
        Node ioTypeNode = Node.MakeNode(Node.NodeType.Input);
        Node pinNumberNode = Node.MakeNode(Node.NodeType.IntLiteral);
        pinNumberNode.Value = "0";
        ioTypeNode.Next = pinNumberNode;
        pinTypeNode.Next = ioTypeNode;
        createPinNode.Next = pinTypeNode;
        testNode.AddChild(createPinNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains("Pin cannot be PWM in master"));
    }

    @Test
    void MasterInitiateCallStrategy_Returns_MasterPinNotInputError() {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Call));
        SemanticsMasterInitiateCallStrategy strategy = new SemanticsMasterInitiateCallStrategy();
        strategy.UsedPinNumbers = new ArrayList<>();
        testNode.AddStrategy(strategy);
        Node createPinNode = Node.MakeNode(Node.NodeType.CreatePin);
        Node pinTypeNode = Node.MakeNode(Node.NodeType.Digital);
        Node ioTypeNode = Node.MakeNode(Node.NodeType.Output);  //Output type is bad here
        Node pinNumberNode = Node.MakeNode(Node.NodeType.IntLiteral);
        pinNumberNode.Value = "0";
        ioTypeNode.Next = pinNumberNode;
        pinTypeNode.Next = ioTypeNode;
        createPinNode.Next = pinTypeNode;
        testNode.AddChild(createPinNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains("Pin must be input in master"));
    }
    //endregion

    //region SemanticsMasterStrategy tests
    @Test
    void MasterStrategy_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Master));
        SemanticsMasterStrategy strategy = new SemanticsMasterStrategy();
        strategy.BroadcastEvents = new ArrayList<>();
        testNode.AddStrategy(strategy);
        testNode.AddChild(Node.MakeNode(Node.NodeType.PinDeclaration));
        testNode.AddChild(Node.MakeNode(Node.NodeType.Empty));  //todo Why..?
        testNode.AddChild(Node.MakeNode(Node.NodeType.Initiate));
        testNode.AddChild(Node.MakeNode(Node.NodeType.Empty));

        //Act
        testNode.Node.CheckSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }
    //endregion

    //region SemanticsPinDeclarationStrategy tests
    @Test
    void PinDeclarationStrategy_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.PinDeclaration));
        SemanticsPinDeclarationStrategy strategy = new SemanticsPinDeclarationStrategy();
        testNode.AddStrategy(strategy);
        Node identifierNode = Node.MakeNode(Node.NodeType.Identifier);
        Node callNode = Node.MakeNode(Node.NodeType.Call);
        callNode.FirstChild = Node.MakeNode(Node.NodeType.CreatePin);

        testNode.AddChild(identifierNode);
        testNode.AddChild(callNode);

        //Act
        testNode.Node.CheckSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void PinDeclarationStrategy_Returns_IllegalDeclError() {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.PinDeclaration));
        SemanticsPinDeclarationStrategy strategy = new SemanticsPinDeclarationStrategy();
        testNode.AddStrategy(strategy);
        Node identifierNode = Node.MakeNode(Node.NodeType.Identifier);
        Node callNode = Node.MakeNode(Node.NodeType.Call);
        callNode.FirstChild = Node.MakeNode(Node.NodeType.CreateEvent);

        testNode.AddChild(identifierNode);
        testNode.AddChild(callNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains("Illegal declaration of pin."));
    }

    @Test
    void PinDeclarationStrategy_Returns_AlreadyDeclaredError() {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.PinDeclaration));
        SemanticsPinDeclarationStrategy strategy = new SemanticsPinDeclarationStrategy();
        testNode.AddStrategy(strategy);
        Node identifierNode = Node.MakeNode(Node.NodeType.Identifier);
        testNode.AddSymbol(identifierNode);
        Node callNode = Node.MakeNode(Node.NodeType.Call);
        callNode.FirstChild = Node.MakeNode(Node.NodeType.CreatePin);

        testNode.AddChild(identifierNode);
        testNode.AddChild(callNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.Node::CheckSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(DefaultErrorMessage(ErrorType.AlreadyDeclared)));
    }

    /*  todo Maybe make this test? Not sure how to be honest, since the inGlobalScope bool is outside of its scope
    @Test
    void PinDeclarationStrategy_Returns_GlobalPinError() throws SemanticsException {

    }
    */

    //endregion

    //region SemanticsProgStrategy tests
    @Test
    void ProgStrategy_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.MakeNode(Node.NodeType.Prog));
        SemanticsProgStrategy strategy = new SemanticsProgStrategy();
        testNode.AddStrategy(strategy);
        testNode.AddChild(Node.MakeNode(Node.NodeType.Master));
        testNode.AddChild(Node.MakeNode(Node.NodeType.Slave));
        testNode.AddChild(Node.MakeNode(Node.NodeType.Empty));

        //Act
        testNode.Node.CheckSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }
    //endregion

    //region SemanticsSlaveInitiateCallStrategy tests

    private TestNode SetupSlaveInitiateCall(SemanticsSlaveInitiateCallStrategy strategy){
        Node CallNode = Node.MakeNode(Node.NodeType.Call);

        TestNode testNode = SetupNode(CallNode, strategy);

        Node emptyChild1 = Node.MakeNode(Node.NodeType.Empty);
        testNode.AddChild(emptyChild1);
        Node emptyChild2 = Node.MakeNode(Node.NodeType.Empty);
        testNode.AddChild(emptyChild2);

        return testNode;
    }

    @Test
    void SlaveInitiateCallStrategy_Returns_NoErrors() {
        SemanticsSlaveInitiateCallStrategy strategy = new SemanticsSlaveInitiateCallStrategy();
        // Add pin values to list of used pins
        strategy.UsedPinNumbers = new ArrayList<>();

        TestNode CallNode = SetupSlaveInitiateCall(strategy);

        // Add the IO parameter
        Node IOParam = Node.MakeNode(Node.NodeType.Output);
        CallNode.AddChild(IOParam);
        // Add the pin parameter
        Node pinParam = Node.MakeNode(Node.NodeType.Identifier);
        pinParam.Value = "testPin";
        CallNode.AddChild(pinParam);

        Assertions.assertDoesNotThrow(CallNode.Node::CheckSemantics);
    }

    @Test
    void SlaveInitiateCallStrategy_Returns_PinAlreadyUsedError() {
        // The string expected as return from the error
        String errorString = "Pin number already used";
        String pinValue = "TestPin";
        SemanticsSlaveInitiateCallStrategy strategy = new SemanticsSlaveInitiateCallStrategy();
        // Add pin values to list of used pins
        ArrayList<String> usedPins = new ArrayList<>();
        usedPins.add(pinValue);
        strategy.UsedPinNumbers = usedPins;

        TestNode CallNode = SetupSlaveInitiateCall(strategy);

        // Add the IO parameter
        Node IOParam = Node.MakeNode(Node.NodeType.Output);
        CallNode.AddChild(IOParam);
        // Add the pin parameter
        Node pinParam = Node.MakeNode(Node.NodeType.Identifier);
        pinParam.Value = pinValue;
        CallNode.AddChild(pinParam);


        Exception exception = Assertions.assertThrows(
                SemanticsException.class,
                CallNode.Node::CheckSemantics
        );

        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }

    @Test
    void SlaveInitiateCallStrategy_Returns_SlavePinNotOutputError() {
        // The string expected as return from the error
        String errorString = "Pin must be output in slave";
        SemanticsSlaveInitiateCallStrategy strategy = new SemanticsSlaveInitiateCallStrategy();
        TestNode CallNode = SetupSlaveInitiateCall(strategy);

        // Add the IO parameter
        Node IOParam = Node.MakeNode(Node.NodeType.Input);
        CallNode.AddChild(IOParam);
        // Add the pin parameter
        Node pinParam = Node.MakeNode(Node.NodeType.Identifier);
        pinParam.Value = "TestPin";
        CallNode.AddChild(pinParam);

        Exception exception = Assertions.assertThrows(
                SemanticsException.class,
                CallNode.Node::CheckSemantics
        );

        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }
    //endregion

    //region SemanticsSlaveStrategy tests
    @Test
    void SlaveStrategy_Returns_NoErrors() {
        Node SlaveNode = Node.MakeNode(Node.NodeType.Slave);
        SemanticsSlaveStrategy strategy = new SemanticsSlaveStrategy();

        TestNode testNode = SetupNode(SlaveNode, strategy);

        // Create and add first child node for the slave
        Node firstChildIdentifier = CreateNode(Node.NodeType.Identifier, Symbol.SymbolType.SLAVE, "TestVar");
        testNode.AddChild(firstChildIdentifier);

        // Add empty children, to simulate global decls, initiate, and eventHandlers
        Node emptyChild1 = Node.MakeNode(Node.NodeType.Empty);
        testNode.AddChild(emptyChild1);
        Node emptyChild2 = Node.MakeNode(Node.NodeType.Empty);
        testNode.AddChild(emptyChild2);
        Node emptyChild3 = Node.MakeNode(Node.NodeType.Empty);
        testNode.AddChild(emptyChild3);

        Assertions.assertDoesNotThrow(testNode.Node::CheckSemantics);
    }

    @Test
    void SlaveStrategy_Returns_AlreadyDeclaredError() {
        // The string expected as return from the error
        String errorString = "already been declared";
        Node SlaveNode = Node.MakeNode(Node.NodeType.Slave);
        SemanticsSlaveStrategy strategy = new SemanticsSlaveStrategy();

        TestNode testNode = SetupNode(SlaveNode, strategy);

        // Create first child node for the slave
        Node firstChildIdentifier = CreateNode(Node.NodeType.Identifier, Symbol.SymbolType.SLAVE, "TestVar");
        // Enter the node into symbol table
        testNode.Node.SemanticsCheckerStrategy.SymbolTable.EnterSymbol(firstChildIdentifier.Value, firstChildIdentifier.DataType);

        testNode.AddChild(firstChildIdentifier);

        Exception exception = Assertions.assertThrows(
                    SemanticsException.class,
                    testNode.Node::CheckSemantics
                );

        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }
    //endregion

    //todo SemanticsStrategyFactory test? Jeg vil rigtig gerne sige nej.
}

class FakeStrategy extends SemanticsCheckerStrategy {
    @Override
    public void CheckSemantics(Node node) {
        // Do nothing
        // This function is made to fake any strategy not needed for specific tests, to limit the scope of the tests.
    }
}


