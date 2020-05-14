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
        public Node node;

        public TestNode(Node node) {
            this.node = node;
        }

        public void addStrategy(SemanticsCheckerStrategy strategy) {
            strategy.SymbolTable = new HashSymbolTable();
            node.SemanticsCheckerStrategy = strategy;
        }

        public void addSymbol(Node symbol) {
            node.SemanticsCheckerStrategy.SymbolTable.EnterSymbol(symbol.Value, symbol.DataType);
        }

        public void addSymbolWithRefNode(Node symbol, Node referenceNode) {
            node.SemanticsCheckerStrategy.SymbolTable.EnterSymbol(symbol.Value, symbol.DataType, referenceNode);
        }

        // Simplified addChild method
        public void addChild(Node child) {
            // Adds a fake strategy to all children
            child.SemanticsCheckerStrategy = new FakeStrategy();

            if (node.FirstChild == null) {
                node.FirstChild = child;
            } else {
                Node currentChild = node.FirstChild;
                // Find last child in linked list
                while (currentChild.Next != null) {
                    currentChild = currentChild.Next;
                }
                currentChild.Next = child;
            }
        }
    }

    private String defaultErrorMessage(ErrorType errorType) {
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

    private TestNode setupAssignmentNodeWithSymbols(Symbol.SymbolType expressionType, Symbol.SymbolType identifierType) {
        TestNode testNode = setupAssignmentNodeWithoutSymbols(expressionType, identifierType);

        // Add identifier symbol to symbol table of strategy
        testNode.addSymbol(testNode.node.FirstChild);

        return testNode;
    }

    private TestNode setupAssignmentNodeWithoutSymbols(Symbol.SymbolType expressionType, Symbol.SymbolType identifierType) {
        Node AssignmentNode = Node.makeNode(Node.NodeType.Assignment);

        SemanticsAssignmentStrategy assignmentStrategy = new SemanticsAssignmentStrategy();

        TestNode testNode = setupNode(AssignmentNode, assignmentStrategy);

        Node IdentifierNode = createNode(Node.NodeType.Identifier, identifierType, "TestVar");
        Node ExpressionNode = createNode(Node.NodeType.Expression, expressionType);
        testNode.addChild(IdentifierNode);
        testNode.addChild(ExpressionNode);

        return testNode;
    }

    private TestNode setupBoolDeclNodeWithoutSymbols(Symbol.SymbolType expressionType, Symbol.SymbolType identifierType) {
        Node DeclarationNode = Node.makeNode(Node.NodeType.BoolDeclaration);

        SemanticsBoolDeclarationStrategy boolDeclarationStrategy = new SemanticsBoolDeclarationStrategy();
        TestNode testNode = setupNode(DeclarationNode, boolDeclarationStrategy);

        // One liner construction
        //TestNode testNode = setupNode(Node.MakeNode(Node.NodeType.BoolDeclaration), new SemanticsBoolDeclarationStrategy());

        Node IdentifierNode = createNode(Node.NodeType.Identifier, identifierType, "TestVar");
        Node ExpressionNode = createNode(Node.NodeType.Expression, expressionType);
        testNode.addChild(IdentifierNode);
        testNode.addChild(ExpressionNode);

        return testNode;
    }

    private TestNode setupBoolLiteralNode(Node prefixNode) {
        Node BoolNode = Node.makeNode(Node.NodeType.BoolDeclaration);
        SemanticsBoolLiteralStrategy boolLiteralStrategy = new SemanticsBoolLiteralStrategy();

        TestNode testNode = setupNode(BoolNode, boolLiteralStrategy);

        testNode.addChild(prefixNode);
        return testNode;
    }

    private TestNode setupEventDeclNode() {
        Node DeclarationNode = Node.makeNode(Node.NodeType.EventDeclaration);
        SemanticsEventDeclarationStrategy eventDeclarationStrategy = new SemanticsEventDeclarationStrategy();
        eventDeclarationStrategy.LocalEvents = new ArrayList<Symbol>();

        return setupNode(DeclarationNode, eventDeclarationStrategy);
    }

    //TODO: Determine if obsolete
    private TestNode setupNode(Node rootNode, SemanticsCheckerStrategy strategy) {
        TestNode testNode = new TestNode(rootNode);
        testNode.addStrategy(strategy);

        return testNode;
    }

    private Node createNode(Node.NodeType nodeType, Symbol.SymbolType dataType, String value) {
        Node node = createNode(nodeType, dataType);
        node.Value = value;
        return node;
    }

    private Node createNode(Node.NodeType nodeType, Symbol.SymbolType dataType) {
        Node node = Node.makeNode(nodeType);
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
        TestNode AssignmentNode = setupAssignmentNodeWithSymbols(Symbol.SymbolType.INT, Symbol.SymbolType.INT);

        AssignmentNode.node.checkSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void AssignmentStrategy_TypeFloat_Returns_NoErrorsThrown() throws SemanticsException {
        TestNode AssignmentNode = setupAssignmentNodeWithSymbols(Symbol.SymbolType.FLOAT, Symbol.SymbolType.FLOAT);

        AssignmentNode.node.checkSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void AssignmentStrategy_TypeBool_Returns_NoErrorsThrown() throws SemanticsException {
        TestNode AssignmentNode = setupAssignmentNodeWithSymbols(Symbol.SymbolType.BOOL, Symbol.SymbolType.BOOL);

        AssignmentNode.node.checkSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void AssignmentStrategy_TypeInt_Returns_WrongTypeErrorThrown() {
        //The string expected as return from the error
        String errorString = "Incompatible types";

        TestNode AssignmentNode = setupAssignmentNodeWithSymbols(Symbol.SymbolType.INT, Symbol.SymbolType.BOOL);

        Exception exception = Assertions.assertThrows(SemanticsException.class, AssignmentNode.node::checkSemantics);

        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }

    @Test
    void AssignmentStrategy_TypeInt_Returns_Error_NotDeclared() {
        //The string expected as return from the error
        String errorString = "not been declared";

        TestNode AssignmentNode = setupAssignmentNodeWithoutSymbols(Symbol.SymbolType.INT, Symbol.SymbolType.INT);

        //In this test, the symbol has not been added to the symbol table, and therefor it should fail.
        Exception exception = Assertions.assertThrows(SemanticsException.class, AssignmentNode.node::checkSemantics);

        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }
    //endregion

    //region BoolDeclarationStrategy tests
    @Test
    void BoolDeclarationStrategy_Correct_Returns_NoErrors() throws SemanticsException {
        TestNode DeclarationNode = setupBoolDeclNodeWithoutSymbols(Symbol.SymbolType.BOOL, Symbol.SymbolType.BOOL);

        DeclarationNode.node.checkSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void BoolDeclarationStrategy_Correct_Returns_AlreadyDefined() {
        //The string expected as return from the error
        String errorString = "has already been declared";

        TestNode DeclarationNode = setupBoolDeclNodeWithoutSymbols(Symbol.SymbolType.BOOL, Symbol.SymbolType.BOOL);

        // Add identifier (which is the first child) to symbolTable
        DeclarationNode.addSymbol(DeclarationNode.node.FirstChild);

        Exception exception = Assertions.assertThrows(SemanticsException.class, DeclarationNode.node::checkSemantics);

        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }

    @Test
    void BoolDeclarationStrategy_Correct_Returns_WrongTypeDeclared() {
        //The string expected as return from the error
        String errorString = "wrong type";

        TestNode DeclarationNode = setupBoolDeclNodeWithoutSymbols(Symbol.SymbolType.INT, Symbol.SymbolType.BOOL);

        Exception exception = Assertions.assertThrows(SemanticsException.class, DeclarationNode.node::checkSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }
    //endregion

    //region BoolLiteralStrategy tests
    @Test
    void BoolLiteralStrategy_No_Prefix_Returns_NoErrors() throws SemanticsException {
        // Empty prefix node
        Node PrefixNode = createNode(Node.NodeType.Empty, Symbol.SymbolType.INT);

        TestNode BoolNode = setupBoolLiteralNode(PrefixNode);

        BoolNode.node.checkSemantics();
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void BoolLiteralStrategy_Correct_Prefix_Returns_NoErrors() throws SemanticsException {
        // PrefixNot
        Node PrefixNode = createNode(Node.NodeType.PrefixNot, Symbol.SymbolType.INT);

        TestNode BoolNode = setupBoolLiteralNode(PrefixNode);

        BoolNode.node.checkSemantics();
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void BoolLiteralStrategy_Wrong_Prefix_Returns_Errors() {
        //The string expected as return from the error
        String errorString = "Only not prefix";

        // PrefixMinus
        Node PrefixNode = createNode(Node.NodeType.PrefixMinus, Symbol.SymbolType.INT);

        TestNode BoolNode = setupBoolLiteralNode(PrefixNode);

        Exception exception = Assertions.assertThrows(SemanticsException.class, BoolNode.node::checkSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }
    //endregion

    //region EventDeclarationStrategy tests
    @Test
    void EventDeclaration_Correct_Returns_NoErrors() throws SemanticsException {
        TestNode DeclarationNode = setupEventDeclNode();

        Node ExpressionNode = Node.makeNode(Node.NodeType.Call);
        Node IdentifierNode = Node.makeNode(Node.NodeType.Identifier);
        Node CreateEventNode = Node.makeNode(Node.NodeType.CreateEvent);
        Node ExpressionChild = Node.makeNode(Node.NodeType.Expression);

        ExpressionNode.FirstChild = CreateEventNode;
        ExpressionNode.FirstChild.Next = ExpressionChild;
        IdentifierNode.Value = "TestVar";

        DeclarationNode.addChild(IdentifierNode);
        DeclarationNode.addChild(ExpressionNode);

        DeclarationNode.node.checkSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void EventDeclaration_InCorrect_Returns_AlreadyDeclared() {
        Node DeclarationNode = Node.makeNode(Node.NodeType.EventDeclaration);
        Node ExpressionNode = Node.makeNode(Node.NodeType.Call);
        Node IdentifierNode = Node.makeNode(Node.NodeType.Identifier);
        Node CreateEventNode = Node.makeNode(Node.NodeType.CreateEvent);
        Node ExpressionChild = Node.makeNode(Node.NodeType.Expression);

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

        Exception exception = Assertions.assertThrows(SemanticsException.class, DeclarationNode::checkSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }

    @Test
    void EventDeclaration_InCorrect_Returns_IllegalDeclaration() {
        //The string expected as return from the error
        String errorString = "Illegal declaration";

        TestNode DeclarationNode = setupEventDeclNode();

        Node ExpressionNode = Node.makeNode(Node.NodeType.Identifier);
        Node IdentifierNode = Node.makeNode(Node.NodeType.Identifier);
        Node CreateEventNode = Node.makeNode(Node.NodeType.CreateEvent);
        Node ExpressionChild = Node.makeNode(Node.NodeType.Expression);

        ExpressionNode.FirstChild = CreateEventNode;
        ExpressionNode.FirstChild.Next = ExpressionChild;
        IdentifierNode.Value = "TestVar";

        DeclarationNode.addChild(IdentifierNode);
        DeclarationNode.addChild(ExpressionNode);

        Exception exception = Assertions.assertThrows(SemanticsException.class, DeclarationNode.node::checkSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }
    //endregion

    //region EventHandlerBlockStrategy tests
    @Test
    void EventHandlerBlockStrategy_Returns_No_Errors() throws SemanticsException {
        // TODO: Refactor by creating setupEventHandler
        Node EventHandlerNode = Node.makeNode(Node.NodeType.EventHandler);
        SemanticsEventHandlerBlockStrategy eventDeclarationStrategy = new SemanticsEventHandlerBlockStrategy();

        TestNode testNode = setupNode(EventHandlerNode, eventDeclarationStrategy);

        Node BlockNode = Node.makeNode(Node.NodeType.Block);
        testNode.addChild(BlockNode);
        BlockNode.SemanticsCheckerStrategy = new FakeStrategy();

        EventHandlerNode.checkSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);

    }

    @Test
    void EventHandlerBlockStrategy_Returns_WrongDeclarations_PinDecl() {
        Node EventHandlerNode = Node.makeNode(Node.NodeType.EventHandler);
        Node BlockNode = Node.makeNode(Node.NodeType.PinDeclaration);

        //The string expected as return from the error
        String errorString = "No pin or event";


        BlockNode.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsEventHandlerBlockStrategy eventDeclarationStrategy = new SemanticsEventHandlerBlockStrategy();
        eventDeclarationStrategy.SymbolTable = new HashSymbolTable();

        EventHandlerNode.FirstChild = BlockNode;

        EventHandlerNode.SemanticsCheckerStrategy = eventDeclarationStrategy;


        Exception exception = Assertions.assertThrows(SemanticsException.class, EventHandlerNode::checkSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }

    @Test
    void EventHandlerBlockStrategy_Returns_WrongDeclarations_EventDecl() {
        Node EventHandlerNode = Node.makeNode(Node.NodeType.EventHandler);
        Node BlockNode = Node.makeNode(Node.NodeType.EventDeclaration);

        //The string expected as return from the error
        String errorString = "No pin or event";

        BlockNode.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsEventHandlerBlockStrategy eventDeclarationStrategy = new SemanticsEventHandlerBlockStrategy();
        eventDeclarationStrategy.SymbolTable = new HashSymbolTable();

        EventHandlerNode.FirstChild = BlockNode;

        EventHandlerNode.SemanticsCheckerStrategy = eventDeclarationStrategy;

        Exception exception = Assertions.assertThrows(SemanticsException.class, EventHandlerNode::checkSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }
    //endregion

    //region EventHandlerCallStrategy tests
    @Test
    void EventHandlerCallStrategy_CheckWrite_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Call));
        SemanticsEventHandlerCallStrategy strategy = new SemanticsEventHandlerCallStrategy();
        testNode.addStrategy(strategy);

        Node writeNode = Node.makeNode(Node.NodeType.Write);
        Node pinIdentifierNode = Node.makeNode(Node.NodeType.Identifier);
        pinIdentifierNode.DataType = Symbol.SymbolType.PIN;
        testNode.addSymbol(pinIdentifierNode);
        Node intLiteralNode = Node.makeNode(Node.NodeType.IntLiteral);
        intLiteralNode.DataType = Symbol.SymbolType.INT;

        testNode.addChild(writeNode);
        testNode.addChild(pinIdentifierNode);
        testNode.addChild(intLiteralNode);

        //Act
        testNode.node.checkSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void EventHandlerCallStrategy_CheckWrite_Returns_SecondParameterWrongTypeError() {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Call));
        SemanticsEventHandlerCallStrategy strategy = new SemanticsEventHandlerCallStrategy();
        testNode.addStrategy(strategy);

        Node writeNode = Node.makeNode(Node.NodeType.Write);
        Node pinIdentifierNode = Node.makeNode(Node.NodeType.Identifier);
        pinIdentifierNode.DataType = Symbol.SymbolType.PIN;
        testNode.addSymbol(pinIdentifierNode);
        Node intLiteralNode = Node.makeNode(Node.NodeType.IntLiteral);
        intLiteralNode.DataType = Symbol.SymbolType.BOOL;   //Bad type

        testNode.addChild(writeNode);
        testNode.addChild(pinIdentifierNode);
        testNode.addChild(intLiteralNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        Assertions.assertTrue(
                exception.getMessage().contains("Second parameter") &&
                exception.getMessage().contains(defaultErrorMessage(ErrorType.WrongType))
        );
    }

    @Test
    void EventHandlerCallStrategy_CheckWrite_Returns_WrongTypeError() {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Call));
        SemanticsEventHandlerCallStrategy strategy = new SemanticsEventHandlerCallStrategy();
        testNode.addStrategy(strategy);

        Node writeNode = Node.makeNode(Node.NodeType.Write);
        Node pinIdentifierNode = Node.makeNode(Node.NodeType.Identifier);
        pinIdentifierNode.DataType = Symbol.SymbolType.EVENT;   //Bad type
        testNode.addSymbol(pinIdentifierNode);
        Node intLiteralNode = Node.makeNode(Node.NodeType.IntLiteral);
        intLiteralNode.DataType = Symbol.SymbolType.INT;

        testNode.addChild(writeNode);
        testNode.addChild(pinIdentifierNode);
        testNode.addChild(intLiteralNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(defaultErrorMessage(ErrorType.WrongType)));
    }

    @Test
    void EventHandlerCallStrategy_CheckWrite_Returns_NotDeclaredError() {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Call));
        SemanticsEventHandlerCallStrategy strategy = new SemanticsEventHandlerCallStrategy();
        testNode.addStrategy(strategy);

        Node writeNode = Node.makeNode(Node.NodeType.Write);
        Node pinIdentifierNode = Node.makeNode(Node.NodeType.Identifier);
        pinIdentifierNode.DataType = Symbol.SymbolType.PIN;
        //testNode.addSymbol(pinIdentifierNode);
        Node intLiteralNode = Node.makeNode(Node.NodeType.IntLiteral);
        intLiteralNode.DataType = Symbol.SymbolType.INT;

        testNode.addChild(writeNode);
        testNode.addChild(pinIdentifierNode);
        testNode.addChild(intLiteralNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(defaultErrorMessage(ErrorType.NotDeclared)));
    }

    @Test
    void EventHandlerCallStrategy_CheckGetValue_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Call));
        SemanticsEventHandlerCallStrategy strategy = new SemanticsEventHandlerCallStrategy();
        String identifier = "eventName";
        strategy.AvailablePinOrEvent = identifier;
        testNode.addStrategy(strategy);

        Node getValueNode = Node.makeNode(Node.NodeType.GetValue);
        Node identifierNode = Node.makeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.EVENT;
        identifierNode.Value = identifier;
        testNode.addSymbol(identifierNode);

        testNode.addChild(getValueNode);
        testNode.addChild(identifierNode);

        //Act
        testNode.node.checkSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void EventHandlerCallStrategy_CheckGetValue_Returns_WrongTypeError() {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Call));
        SemanticsEventHandlerCallStrategy strategy = new SemanticsEventHandlerCallStrategy();
        String identifier = "eventName";
        strategy.AvailablePinOrEvent = identifier;
        testNode.addStrategy(strategy);

        Node getValueNode = Node.makeNode(Node.NodeType.GetValue);
        Node identifierNode = Node.makeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.VOID;   //Bad type
        identifierNode.Value = identifier;
        testNode.addSymbol(identifierNode);

        testNode.addChild(getValueNode);
        testNode.addChild(identifierNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(defaultErrorMessage(ErrorType.WrongType)));
    }

    @Test
    void EventHandlerCallStrategy_CheckGetValue_Returns_EventUnavailableError() {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Call));
        SemanticsEventHandlerCallStrategy strategy = new SemanticsEventHandlerCallStrategy();
        String identifier = "eventName";
        //strategy.AvailablePinOrEvent = identifier;
        testNode.addStrategy(strategy);

        Node getValueNode = Node.makeNode(Node.NodeType.GetValue);
        Node identifierNode = Node.makeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.EVENT;
        identifierNode.Value = identifier;
        testNode.addSymbol(identifierNode);

        testNode.addChild(getValueNode);
        testNode.addChild(identifierNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains("Event unavailable to EventHandler"));
    }

    @Test
    void EventHandlerCallStrategy_CheckGetValue_Returns_NotDeclaredError() {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Call));
        SemanticsEventHandlerCallStrategy strategy = new SemanticsEventHandlerCallStrategy();
        String identifier = "eventName";
        strategy.AvailablePinOrEvent = identifier;
        testNode.addStrategy(strategy);

        Node getValueNode = Node.makeNode(Node.NodeType.GetValue);
        Node identifierNode = Node.makeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.EVENT;
        identifierNode.Value = identifier;
        //testNode.addSymbol(identifierNode);

        testNode.addChild(getValueNode);
        testNode.addChild(identifierNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(defaultErrorMessage(ErrorType.NotDeclared)));
    }

    @Test
    void EventHandlerCallStrategy_Returns_IllegalFuncError() {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Call));
        SemanticsEventHandlerCallStrategy strategy = new SemanticsEventHandlerCallStrategy();
        testNode.addStrategy(strategy);

        testNode.addChild(Node.makeNode(Node.NodeType.Broadcast));  //Bad function

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains("Illegal function call in EventHandler"));
    }
    //endregion

    //region FloatDeclarationStrategy tests
    @Test
    void FloatDeclarationStrategy_Returns_NoErrors() throws SemanticsException {
        Node FloatDeclaration = Node.makeNode(Node.NodeType.FloatDeclaration);
        Node IdentifierNode = Node.makeNode(Node.NodeType.Identifier);
        Node FloatNode = Node.makeNode(Node.NodeType.FloatLiteral);


        IdentifierNode.SemanticsCheckerStrategy = new FakeStrategy();
        FloatNode.SemanticsCheckerStrategy = new FakeStrategy();


        SemanticsFloatDeclarationStrategy floatDeclarationStrategy = new SemanticsFloatDeclarationStrategy();
        floatDeclarationStrategy.SymbolTable = new HashSymbolTable();


        FloatNode.DataType = Symbol.SymbolType.FLOAT;
        IdentifierNode.Next = FloatNode;
        FloatDeclaration.FirstChild = IdentifierNode;

        FloatDeclaration.SemanticsCheckerStrategy = floatDeclarationStrategy;

        FloatDeclaration.checkSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void FloatDeclarationStrategy_Returns_WrongType() {
        Node FloatDeclaration = Node.makeNode(Node.NodeType.FloatDeclaration);
        Node IdentifierNode = Node.makeNode(Node.NodeType.Identifier);
        Node FloatNode = Node.makeNode(Node.NodeType.FloatLiteral);

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

        Exception exception = Assertions.assertThrows(SemanticsException.class, FloatDeclaration::checkSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }

    @Test
    void FloatDeclarationStrategy_Returns_Already_Declared() {
        Node FloatDeclaration = Node.makeNode(Node.NodeType.FloatDeclaration);
        Node IdentifierNode = Node.makeNode(Node.NodeType.Identifier);
        Node FloatNode = Node.makeNode(Node.NodeType.FloatLiteral);

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

        Exception exception = Assertions.assertThrows(SemanticsException.class, FloatDeclaration::checkSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }
    //endregion

    //region FloatLiteralStrategy tests
    @Test
    void FloatLiteralStrategy_No_Prefix_Returns_No_Errors() throws SemanticsException {
        Node FloatNode = Node.makeNode(Node.NodeType.FloatLiteral);
        Node PrefixNode = Node.makeNode(Node.NodeType.Block);

        PrefixNode.SemanticsCheckerStrategy = new FakeStrategy();


        SemanticsFloatLiteralStrategy floatLiteralStrategy = new SemanticsFloatLiteralStrategy();
        floatLiteralStrategy.SymbolTable = new HashSymbolTable();


        PrefixNode.Type = Node.NodeType.Empty;
        FloatNode.FirstChild = PrefixNode;

        FloatNode.SemanticsCheckerStrategy = floatLiteralStrategy;


        floatLiteralStrategy.SymbolTable.EnterSymbol(FloatNode.FirstChild.Value, FloatNode.FirstChild.DataType);


        FloatNode.checkSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);

    }

    @Test
    void FloatLiteralStrategy_Correct_Prefix_Returns_No_Errors() throws SemanticsException {
        Node FloatNode = Node.makeNode(Node.NodeType.FloatLiteral);
        Node PrefixNode = Node.makeNode(Node.NodeType.Block);

        PrefixNode.SemanticsCheckerStrategy = new FakeStrategy();


        SemanticsFloatLiteralStrategy floatLiteralStrategy = new SemanticsFloatLiteralStrategy();
        floatLiteralStrategy.SymbolTable = new HashSymbolTable();


        PrefixNode.Type = Node.NodeType.PrefixMinus;
        FloatNode.FirstChild = PrefixNode;

        FloatNode.SemanticsCheckerStrategy = floatLiteralStrategy;


        floatLiteralStrategy.SymbolTable.EnterSymbol(FloatNode.FirstChild.Value, FloatNode.FirstChild.DataType);


        FloatNode.checkSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);

    }

    @Test
    void FloatLiteralStrategy_Wrong_Prefix_Returns_Wrong_Prefix() {
        Node FloatNode = Node.makeNode(Node.NodeType.FloatLiteral);
        Node PrefixNode = Node.makeNode(Node.NodeType.Block);

        // The string expected as return from the error
        String errorString = "Only minus prefix";

        PrefixNode.SemanticsCheckerStrategy = new FakeStrategy();


        SemanticsFloatLiteralStrategy floatLiteralStrategy = new SemanticsFloatLiteralStrategy();
        floatLiteralStrategy.SymbolTable = new HashSymbolTable();


        PrefixNode.Type = Node.NodeType.PrefixNot;
        FloatNode.FirstChild = PrefixNode;

        FloatNode.SemanticsCheckerStrategy = floatLiteralStrategy;


        floatLiteralStrategy.SymbolTable.EnterSymbol(FloatNode.FirstChild.Value, FloatNode.FirstChild.DataType);


        Exception exception = Assertions.assertThrows(SemanticsException.class, FloatNode::checkSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));

    }
    //endregion

    //region IdentifierStrategy tests
    @Test
    void IdentifierStrategy_No_Prefix_Returns_No_Errors() throws SemanticsException {
        Node IdentifierNode = Node.makeNode(Node.NodeType.Identifier);
        IdentifierNode.Value = "Test";
        Node Prefix = Node.makeNode(Node.NodeType.Empty);

        Prefix.SemanticsCheckerStrategy = new FakeStrategy();


        SemanticsIdentifierStrategy identifierStrategy = new SemanticsIdentifierStrategy();
        identifierStrategy.SymbolTable = new HashSymbolTable();

        IdentifierNode.FirstChild = Prefix;

        IdentifierNode.SemanticsCheckerStrategy = identifierStrategy;


        identifierStrategy.SymbolTable.EnterSymbol(IdentifierNode.Value, IdentifierNode.DataType,true);


        IdentifierNode.checkSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);

    }

    @Test
    void IdentifierStrategy_PrefixMinus_IntType_Returns_No_Errors() throws SemanticsException {
        Node IdentifierNode = Node.makeNode(Node.NodeType.Identifier);
        IdentifierNode.Value = "Test";
        IdentifierNode.DataType = Symbol.SymbolType.INT;
        Node PrefixNode = Node.makeNode(Node.NodeType.PrefixMinus);

        PrefixNode.SemanticsCheckerStrategy = new FakeStrategy();


        SemanticsIdentifierStrategy identifierStrategy = new SemanticsIdentifierStrategy();
        identifierStrategy.SymbolTable = new HashSymbolTable();

        IdentifierNode.FirstChild = PrefixNode;

        IdentifierNode.SemanticsCheckerStrategy = identifierStrategy;


        identifierStrategy.SymbolTable.EnterSymbol(IdentifierNode.Value, IdentifierNode.DataType,true);


        IdentifierNode.checkSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);

    }

    @Test
    void IdentifierStrategy_PrefixNot_BoolType_Returns_No_Errors() throws SemanticsException {
        Node IdentifierNode = Node.makeNode(Node.NodeType.Identifier);
        IdentifierNode.Value = "Test";
        IdentifierNode.DataType = Symbol.SymbolType.BOOL;
        Node PrefixNode = Node.makeNode(Node.NodeType.PrefixNot);

        PrefixNode.SemanticsCheckerStrategy = new FakeStrategy();


        SemanticsIdentifierStrategy identifierStrategy = new SemanticsIdentifierStrategy();
        identifierStrategy.SymbolTable = new HashSymbolTable();

        IdentifierNode.FirstChild = PrefixNode;

        IdentifierNode.SemanticsCheckerStrategy = identifierStrategy;


        identifierStrategy.SymbolTable.EnterSymbol(IdentifierNode.Value, IdentifierNode.DataType,true);


        IdentifierNode.checkSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);

    }

    @Test
    void IdentifierStrategy_PrefixNot_IntType_Returns_Not_Only_Applicable_For_Boolean() {
        Node IdentifierNode = Node.makeNode(Node.NodeType.Identifier);
        IdentifierNode.Value = "Test";
        IdentifierNode.DataType = Symbol.SymbolType.INT;
        Node PrefixNode = Node.makeNode(Node.NodeType.PrefixNot);

        // The string expected as return from the error
        String errorString = "only applicable to boolean";

        PrefixNode.SemanticsCheckerStrategy = new FakeStrategy();


        SemanticsIdentifierStrategy identifierStrategy = new SemanticsIdentifierStrategy();
        identifierStrategy.SymbolTable = new HashSymbolTable();

        IdentifierNode.FirstChild = PrefixNode;

        IdentifierNode.SemanticsCheckerStrategy = identifierStrategy;


        identifierStrategy.SymbolTable.EnterSymbol(IdentifierNode.Value, IdentifierNode.DataType,true);

        Exception exception = Assertions.assertThrows(SemanticsException.class, IdentifierNode::checkSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));

    }

    @Test
    void IdentifierStrategy_PrefixMinus_BoolType_Returns_Minus_Not_Applicable_For_Boolean() {
        Node IdentifierNode = Node.makeNode(Node.NodeType.Identifier);
        IdentifierNode.Value = "Test";
        IdentifierNode.DataType = Symbol.SymbolType.BOOL;
        Node PrefixNode = Node.makeNode(Node.NodeType.PrefixMinus);

        // The string expected as return from the error
        String errorString = " not applicable to boolean";

        PrefixNode.SemanticsCheckerStrategy = new FakeStrategy();


        SemanticsIdentifierStrategy identifierStrategy = new SemanticsIdentifierStrategy();
        identifierStrategy.SymbolTable = new HashSymbolTable();

        IdentifierNode.FirstChild = PrefixNode;

        IdentifierNode.SemanticsCheckerStrategy = identifierStrategy;


        identifierStrategy.SymbolTable.EnterSymbol(IdentifierNode.Value, IdentifierNode.DataType,true);

        Exception exception = Assertions.assertThrows(SemanticsException.class, IdentifierNode::checkSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));

    }

    @Test
    void IdentifierStrategy_No_Prefix_BoolType_Returns_Not_Declared() {
        Node IdentifierNode = Node.makeNode(Node.NodeType.Identifier);
        IdentifierNode.Value = "Test";
        IdentifierNode.DataType = Symbol.SymbolType.BOOL;
        Node PrefixNode = Node.makeNode(Node.NodeType.Empty);

        // The string expected as return from the error
        String errorString = " not been declared";

        PrefixNode.SemanticsCheckerStrategy = new FakeStrategy();


        SemanticsIdentifierStrategy identifierStrategy = new SemanticsIdentifierStrategy();
        identifierStrategy.SymbolTable = new HashSymbolTable();

        IdentifierNode.FirstChild = PrefixNode;

        IdentifierNode.SemanticsCheckerStrategy = identifierStrategy;


        Exception exception = Assertions.assertThrows(SemanticsException.class, IdentifierNode::checkSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));

    }

    @Test
    void IdentifierStrategy_No_Prefix_BoolType_Returns_Not_Instantiated() {
        Node IdentifierNode = Node.makeNode(Node.NodeType.Identifier);
        IdentifierNode.Value = "Test";
        IdentifierNode.DataType = Symbol.SymbolType.BOOL;
        Node PrefixNode = Node.makeNode(Node.NodeType.Empty);

        // The string expected as return from the error
        String errorString = "must be instantiated";

        PrefixNode.SemanticsCheckerStrategy = new FakeStrategy();


        SemanticsIdentifierStrategy identifierStrategy = new SemanticsIdentifierStrategy();
        identifierStrategy.SymbolTable = new HashSymbolTable();

        IdentifierNode.FirstChild = PrefixNode;

        IdentifierNode.SemanticsCheckerStrategy = identifierStrategy;

        identifierStrategy.SymbolTable.EnterSymbol(IdentifierNode.Value, IdentifierNode.DataType,false);


        Exception exception = Assertions.assertThrows(SemanticsException.class, IdentifierNode::checkSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));

    }
    //endregion

    //region IfStrategy tests
    @Test
    void IfStrategy_If_Statement_With_Block_Returns_No_Errors() throws SemanticsException {
        Node IfNode = Node.makeNode(Node.NodeType.If);
        Node BoolExpression = Node.makeNode(Node.NodeType.Expression);
        BoolExpression.DataType = Symbol.SymbolType.BOOL;
        Node BoolNode = Node.makeNode(Node.NodeType.BoolLiteral);
        Node BlockNode = Node.makeNode(Node.NodeType.Block);

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


        IfNode.checkSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);

    }

    @Test
    void IfStrategy_No_Boolean_Returns_Boolean_Error() {
        Node IfNode = Node.makeNode(Node.NodeType.If);
        Node BoolExpression = Node.makeNode(Node.NodeType.Expression);
        BoolExpression.DataType = Symbol.SymbolType.INT;
        Node BoolNode = Node.makeNode(Node.NodeType.BoolLiteral);
        Node BlockNode = Node.makeNode(Node.NodeType.Block);

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

        Exception exception = Assertions.assertThrows(SemanticsException.class, IfNode::checkSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));

    }

    @Test
    void IfStrategy_Nested_If_Returns_No_Errors() throws SemanticsException {
        Node IfNode = Node.makeNode(Node.NodeType.If);
        Node ExpressionNode = Node.makeNode(Node.NodeType.Expression);
        ExpressionNode.DataType = Symbol.SymbolType.BOOL;
        Node BlockNode = Node.makeNode(Node.NodeType.Block);
        //Node BoolNode = Node.MakeNode(Node.NodeType.BoolLiteral);

        Node NestedIfNode = Node.makeNode(Node.NodeType.If);
        Node NestedExpressionNode = Node.makeNode(Node.NodeType.Expression);
        NestedExpressionNode.DataType = Symbol.SymbolType.BOOL;
        Node NestedBlockNode = Node.makeNode(Node.NodeType.Block);
        Node NestedEmptyNode = Node.makeNode(Node.NodeType.Empty);

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

        IfNode.checkSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }
    //endregion

    //region SemanticsInitiateBlockStrategy tests
    @Test
    void InitiateBlockStrategy_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Block));
        SemanticsInitiateBlockStrategy strategy = new SemanticsInitiateBlockStrategy();
        strategy.UsedPinNumbers = new ArrayList<>();
        testNode.addStrategy(strategy);
        Node pinDeclNode = Node.makeNode(Node.NodeType.PinDeclaration);
        pinDeclNode.Next = Node.makeNode(Node.NodeType.Empty);
        testNode.addChild(pinDeclNode);

        //Act
        testNode.node.checkSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void InitiateBlockStrategy_Returns_NotPinDeclError() {
        //Error being checked for
        String errorMessage = "Only pin declarations allowed in Initiate";

        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Block));
        SemanticsInitiateBlockStrategy strategy = new SemanticsInitiateBlockStrategy();
        strategy.UsedPinNumbers = new ArrayList<>();
        testNode.addStrategy(strategy);
        Node notPinDeclNode = Node.makeNode(Node.NodeType.BoolDeclaration);
        notPinDeclNode.Next = Node.makeNode(Node.NodeType.Empty);
        testNode.addChild(notPinDeclNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(errorMessage));
    }
    //endregion

    //region SemanticsInitiateStrategy tests
    @Test
    void InitiateStrategy_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Initiate));
        testNode.addStrategy(new SemanticsInitiateStrategy());
        Node blockNode = Node.makeNode(Node.NodeType.Block);
        testNode.addChild(blockNode);

        //Act
        testNode.node.checkSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }
    //endregion

    //region SemanticsIntDeclarationStrategy tests
    @Test
    void IntDeclarationStrategy_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.IntDeclaration));
        testNode.addStrategy(new SemanticsIntDeclarationStrategy());
        Node identifierNode = Node.makeNode(Node.NodeType.Identifier);
        testNode.addChild(identifierNode);
        Node expressionNode = Node.makeNode(Node.NodeType.Expression);
        expressionNode.DataType = Symbol.SymbolType.INT;    //Could also be a float
        testNode.addChild(expressionNode);

        //Act
        testNode.node.checkSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void IntDeclarationStrategy_Returns_WrongTypeError() {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.IntDeclaration));
        testNode.addStrategy(new SemanticsIntDeclarationStrategy());
        Node identifierNode = Node.makeNode(Node.NodeType.Identifier);
        testNode.addChild(identifierNode);
        Node badExpressionNode = Node.makeNode(Node.NodeType.Expression);
        badExpressionNode.DataType = Symbol.SymbolType.BOOL;
        testNode.addChild(badExpressionNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(defaultErrorMessage(ErrorType.WrongType)));
    }

    @Test
    void IntDeclarationStrategy_Returns_AlreadyDeclaredError() {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.IntDeclaration));
        testNode.addStrategy(new SemanticsIntDeclarationStrategy());
        Node identifierNode = Node.makeNode(Node.NodeType.Identifier);
        testNode.addChild(identifierNode);
        Node expressionNode = Node.makeNode(Node.NodeType.Expression);
        expressionNode.DataType = Symbol.SymbolType.INT;    //Could also be a float
        testNode.addChild(expressionNode);
        testNode.addSymbol(expressionNode);
        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(defaultErrorMessage(ErrorType.AlreadyDeclared)));
    }
    //endregion

    //region SemanticsIntLiteralStrategy tests
    @Test
    void IntLiteralStrategy_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.IntLiteral));
        testNode.addStrategy(new SemanticsIntLiteralStrategy());
        Node emptyNode = Node.makeNode(Node.NodeType.Empty);
        testNode.addChild(emptyNode);

        //Act
        testNode.node.checkSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void IntLiteralStrategy_Returns_NotMinusPrefixError() {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.IntLiteral));
        testNode.addStrategy(new SemanticsIntLiteralStrategy());
        Node badPrefixNode = Node.makeNode(Node.NodeType.PrefixNot);
        testNode.addChild(badPrefixNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        String errorMessage = "Only minus prefix (-) applicable to int data type.";
        Assertions.assertTrue(exception.getMessage().contains(errorMessage));
    }
    //endregion

    //region SemanticsListenerBlockStrategy tests
    @Test
    void ListenerBlockStrategy_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Block));
        testNode.addStrategy(new SemanticsListenerBlockStrategy());
        testNode.addChild(Node.makeNode(Node.NodeType.IntDeclaration));
        testNode.addChild(Node.makeNode(Node.NodeType.Empty));

        //Act
        testNode.node.checkSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void ListenerBlockStrategy_Returns_IsPinDeclError() {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Block));
        testNode.addStrategy(new SemanticsListenerBlockStrategy());
        testNode.addChild(Node.makeNode(Node.NodeType.PinDeclaration));
        testNode.addChild(Node.makeNode(Node.NodeType.Empty));

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        String errorString = "No pin declarations allowed in Listener";
        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }
    //endregion

    //region SemanticsListenerCallStrategy tests
    @Test
    void ListenerCallStrategy_CheckBroadcast_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Call));
        SemanticsListenerCallStrategy strategy = new SemanticsListenerCallStrategy();
        strategy.BroadcastEvents = new ArrayList<>();
        testNode.addStrategy(strategy);
        testNode.addChild(Node.makeNode(Node.NodeType.Broadcast));
        Node identifierNode = Node.makeNode(Node.NodeType.Identifier);
        identifierNode.Value = "eventName";
        identifierNode.DataType = Symbol.SymbolType.EVENT;
        testNode.addSymbol(identifierNode);
        testNode.addChild(identifierNode);

        //Act
        testNode.node.checkSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void ListenerCallStrategy_CheckBroadcast_Returns_WrongTypeError() {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Call));
        SemanticsListenerCallStrategy strategy = new SemanticsListenerCallStrategy();
        strategy.BroadcastEvents = new ArrayList<>();
        testNode.addStrategy(strategy);
        testNode.addChild(Node.makeNode(Node.NodeType.Broadcast));
        Node identifierNode = Node.makeNode(Node.NodeType.Identifier);
        identifierNode.Value = "eventName";
        identifierNode.DataType = Symbol.SymbolType.PIN;    //Bad type
        testNode.addSymbol(identifierNode);
        testNode.addChild(identifierNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(defaultErrorMessage(ErrorType.WrongType)));
    }

    @Test
    void ListenerCallStrategy_CheckBroadcast_Returns_NotDeclaredError() {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Call));
        SemanticsListenerCallStrategy strategy = new SemanticsListenerCallStrategy();
        strategy.BroadcastEvents = new ArrayList<>();
        testNode.addStrategy(strategy);
        testNode.addChild(Node.makeNode(Node.NodeType.Broadcast));
        Node identifierNode = Node.makeNode(Node.NodeType.Identifier);
        identifierNode.Value = "eventName";
        identifierNode.DataType = Symbol.SymbolType.EVENT;
        //testNode.addSymbol(identifierNode);
        testNode.addChild(identifierNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(defaultErrorMessage(ErrorType.NotDeclared)));
    }

    @Test
    void ListenerCallStrategy_CheckGetValue_Pin_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Call));
        SemanticsListenerCallStrategy strategy = new SemanticsListenerCallStrategy();
        String pinName = "pinName";
        strategy.AvailablePinOrEvent = pinName;
        testNode.addStrategy(strategy);
        testNode.addChild(Node.makeNode(Node.NodeType.GetValue));
        Node identifierNode = Node.makeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.PIN;
        identifierNode.Value = pinName;
        testNode.addSymbol(identifierNode);
        testNode.addChild(identifierNode);

        //Act
        testNode.node.checkSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void ListenerCallStrategy_CheckGetValue_Event_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Call));
        SemanticsListenerCallStrategy strategy = new SemanticsListenerCallStrategy();
        String eventName = "eventName";
        strategy.AvailablePinOrEvent = eventName;
        testNode.addStrategy(strategy);
        testNode.addChild(Node.makeNode(Node.NodeType.GetValue));
        Node identifierNode = Node.makeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.EVENT;
        identifierNode.Value = eventName;
        testNode.addSymbol(identifierNode);
        testNode.addChild(identifierNode);

        //Act
        testNode.node.checkSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void ListenerCallStrategy_CheckGetValue_Returns_WrongTypeError() {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Call));
        SemanticsListenerCallStrategy strategy = new SemanticsListenerCallStrategy();
        String pinName = "pinName";
        strategy.AvailablePinOrEvent = pinName;
        testNode.addStrategy(strategy);
        testNode.addChild(Node.makeNode(Node.NodeType.GetValue));
        Node identifierNode = Node.makeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.BOOL;   //Bad type
        identifierNode.Value = pinName;
        testNode.addSymbol(identifierNode);
        testNode.addChild(identifierNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(defaultErrorMessage(ErrorType.WrongType)));
    }

    @Test
    void ListenerCallStrategy_CheckGetValue_Returns_PinUnavailableError() {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Call));
        SemanticsListenerCallStrategy strategy = new SemanticsListenerCallStrategy();
        String pinName = "pinName";
        //strategy.AvailablePinOrEvent = pinName;
        testNode.addStrategy(strategy);
        testNode.addChild(Node.makeNode(Node.NodeType.GetValue));
        Node identifierNode = Node.makeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.PIN;
        identifierNode.Value = pinName;
        testNode.addSymbol(identifierNode);
        testNode.addChild(identifierNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains("Pin unavailable to Listener"));
    }

    @Test
    void ListenerCallStrategy_CheckGetValue_Returns_NotDeclaredError() {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Call));
        SemanticsListenerCallStrategy strategy = new SemanticsListenerCallStrategy();
        String pinName = "pinName";
        strategy.AvailablePinOrEvent = pinName;
        testNode.addStrategy(strategy);
        testNode.addChild(Node.makeNode(Node.NodeType.GetValue));
        Node identifierNode = Node.makeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.PIN;
        identifierNode.Value = pinName;
        //testNode.addSymbol(identifierNode);
        testNode.addChild(identifierNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        System.out.println(exception.getMessage() + " " + ErrorType.NotDeclared.ordinal());
        Assertions.assertTrue(exception.getMessage().contains(defaultErrorMessage(ErrorType.NotDeclared)));
    }

    @Test
    void ListenerCallStrategy_CheckFilterNoise_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Call));
        SemanticsListenerCallStrategy strategy = new SemanticsListenerCallStrategy();
        String pinName = "pinName";
        strategy.AvailablePinOrEvent = pinName;
        testNode.addStrategy(strategy);
        testNode.addChild(Node.makeNode(Node.NodeType.FilterNoise));
        Node identifierNode = Node.makeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.PIN;
        identifierNode.Value = pinName;
        testNode.addSymbolWithRefNode(identifierNode, Node.makeNode(Node.NodeType.Digital));
        testNode.addChild(identifierNode);
        testNode.addChild(Node.makeNode(Node.NodeType.Debounce));

        //Act
        testNode.node.checkSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void ListenerCallStrategy_CheckFilterNoise_CheckPinAndFilterCombination_Returns_DigitalPinRangeError() {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Call));
        SemanticsListenerCallStrategy strategy = new SemanticsListenerCallStrategy();
        String pinName = "pinName";
        strategy.AvailablePinOrEvent = pinName;
        testNode.addStrategy(strategy);
        testNode.addChild(Node.makeNode(Node.NodeType.FilterNoise));
        Node identifierNode = Node.makeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.PIN;
        identifierNode.Value = pinName;
        testNode.addSymbolWithRefNode(identifierNode, Node.makeNode(Node.NodeType.Digital));
        testNode.addChild(identifierNode);
        testNode.addChild(Node.makeNode(Node.NodeType.Range));

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains("Noise from digital pin cannot be filtered as ranged"));
    }

    @Test
    void ListenerCallStrategy_CheckFilterNoise_CheckPinAndFilterCombination_Returns_AnalogPinNotRangeError() {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Call));
        SemanticsListenerCallStrategy strategy = new SemanticsListenerCallStrategy();
        String pinName = "pinName";
        strategy.AvailablePinOrEvent = pinName;
        testNode.addStrategy(strategy);
        testNode.addChild(Node.makeNode(Node.NodeType.FilterNoise));
        Node identifierNode = Node.makeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.PIN;
        identifierNode.Value = pinName;
        testNode.addSymbolWithRefNode(identifierNode, Node.makeNode(Node.NodeType.Analog));
        testNode.addChild(identifierNode);
        testNode.addChild(Node.makeNode(Node.NodeType.Debounce));

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains("Noise from analog pin can only be filtered as ranged"));
    }

    @Test
    void ListenerCallStrategy_CheckFilterNoise_Returns_WrongTypeError() {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Call));
        SemanticsListenerCallStrategy strategy = new SemanticsListenerCallStrategy();
        String pinName = "pinName";
        strategy.AvailablePinOrEvent = pinName;
        testNode.addStrategy(strategy);
        testNode.addChild(Node.makeNode(Node.NodeType.FilterNoise));
        Node identifierNode = Node.makeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.BOOL;   //Bad type
        identifierNode.Value = pinName;
        testNode.addSymbolWithRefNode(identifierNode, Node.makeNode(Node.NodeType.Digital));
        testNode.addChild(identifierNode);
        testNode.addChild(Node.makeNode(Node.NodeType.Debounce));

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(defaultErrorMessage(ErrorType.WrongType)));
    }

    @Test
    void ListenerCallStrategy_CheckFilterNoise_Returns_NotDeclaredError() {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Call));
        SemanticsListenerCallStrategy strategy = new SemanticsListenerCallStrategy();
        String pinName = "pinName";
        strategy.AvailablePinOrEvent = pinName;
        testNode.addStrategy(strategy);
        testNode.addChild(Node.makeNode(Node.NodeType.FilterNoise));
        Node identifierNode = Node.makeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.PIN;   //Bad type
        identifierNode.Value = pinName;
        //testNode.addSymbolWithRefNode(identifierNode, Node.makeNode(Node.NodeType.Digital));
        testNode.addChild(identifierNode);
        testNode.addChild(Node.makeNode(Node.NodeType.Debounce));

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(defaultErrorMessage(ErrorType.NotDeclared)));
    }

    @Test
    void ListenerCallStrategy_CheckFilterNoise_Returns_PinUnavailableError() {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Call));
        SemanticsListenerCallStrategy strategy = new SemanticsListenerCallStrategy();
        String pinName = "pinName";
        //strategy.AvailablePinOrEvent = pinName;
        testNode.addStrategy(strategy);
        testNode.addChild(Node.makeNode(Node.NodeType.FilterNoise));
        Node identifierNode = Node.makeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.PIN;
        identifierNode.Value = pinName;
        testNode.addSymbolWithRefNode(identifierNode, Node.makeNode(Node.NodeType.Digital));
        testNode.addChild(identifierNode);
        testNode.addChild(Node.makeNode(Node.NodeType.Debounce));

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains("Pin unavailable to Listener"));
    }

    @Test
    void ListenerCallStrategy_CheckCreateEvent_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Call));
        testNode.addStrategy(new SemanticsListenerCallStrategy());
        testNode.addChild(Node.makeNode(Node.NodeType.CreateEvent));
        Node identifierNode = Node.makeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.INT;
        testNode.addChild(identifierNode);

        //Act
        testNode.node.checkSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void ListenerCallStrategy_CheckCreateEvent_Returns_WrongTypeError() {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Call));
        testNode.addStrategy(new SemanticsListenerCallStrategy());
        testNode.addChild(Node.makeNode(Node.NodeType.CreateEvent));
        Node identifierNode = Node.makeNode(Node.NodeType.Identifier);
        identifierNode.DataType = Symbol.SymbolType.VOID;   //Bad type
        testNode.addChild(identifierNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(defaultErrorMessage(ErrorType.WrongType)));
    }

    @Test
    void ListenerCallStrategy_Returns_IllegalFuncCallError() {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Call));
        testNode.addStrategy(new SemanticsListenerCallStrategy());
        testNode.addChild(Node.makeNode(Node.NodeType.Write));

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains("Illegal function call in Listener"));
    }
    //endregion

    //region SemanticsListenerStrategy tests
    @Test
    void ListenerStrategy_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Listener));
        testNode.addStrategy(new SemanticsListenerStrategy());
        Node identifierNode = Node.makeNode((Node.NodeType.Identifier));
        identifierNode.Value = "pinName";
        identifierNode.DataType = Symbol.SymbolType.PIN;
        testNode.addChild(identifierNode);
        testNode.addSymbol(identifierNode);
        testNode.addChild(Node.makeNode(Node.NodeType.Block));

        //Act
        testNode.node.checkSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void ListenerStrategy_Returns_WrongTypeError() {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Listener));
        testNode.addStrategy(new SemanticsListenerStrategy());
        Node identifierNode = Node.makeNode((Node.NodeType.Identifier));
        identifierNode.Value = "pinName";
        identifierNode.DataType = Symbol.SymbolType.BOOL;   //Bad type
        testNode.addChild(identifierNode);
        testNode.addSymbol(identifierNode);
        testNode.addChild(Node.makeNode(Node.NodeType.Block));

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(defaultErrorMessage(ErrorType.WrongType)));
    }

    @Test
    void ListenerStrategy_Returns_NotDeclaredError() {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Listener));
        testNode.addStrategy(new SemanticsListenerStrategy());
        Node identifierNode = Node.makeNode((Node.NodeType.Identifier));
        identifierNode.Value = "pinName";
        identifierNode.DataType = Symbol.SymbolType.PIN;
        testNode.addChild(identifierNode);
        //testNode.addSymbol(identifierNode);
        testNode.addChild(Node.makeNode(Node.NodeType.Block));

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(defaultErrorMessage(ErrorType.NotDeclared)));
    }
    //endregion

    //region SemanticsMasterInitiateCallStrategy tests
    @Test
    void MasterInitiateCallStrategy_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Call));
        SemanticsMasterInitiateCallStrategy strategy = new SemanticsMasterInitiateCallStrategy();
        strategy.UsedPinNumbers = new ArrayList<>();
        testNode.addStrategy(strategy);
        Node createPinNode = Node.makeNode(Node.NodeType.CreatePin);
        Node pinTypeNode = Node.makeNode(Node.NodeType.Digital);
        Node ioTypeNode = Node.makeNode(Node.NodeType.Input);
        Node pinNumberNode = Node.makeNode(Node.NodeType.IntLiteral);
        pinNumberNode.Value = "0";
        ioTypeNode.Next = pinNumberNode;
        pinTypeNode.Next = ioTypeNode;
        createPinNode.Next = pinTypeNode;
        testNode.addChild(createPinNode);

        //Act
        testNode.node.checkSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);

    }

    @Test
    void MasterInitiateCallStrategy_Returns_PinAlreadyUsedError() {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Call));
        SemanticsMasterInitiateCallStrategy strategy = new SemanticsMasterInitiateCallStrategy();
        strategy.UsedPinNumbers = new ArrayList<>();
        String pinNumber = "0";
        strategy.UsedPinNumbers.add(pinNumber);
        testNode.addStrategy(strategy);
        Node createPinNode = Node.makeNode(Node.NodeType.CreatePin);
        Node pinTypeNode = Node.makeNode(Node.NodeType.Digital);
        Node ioTypeNode = Node.makeNode(Node.NodeType.Input);
        Node pinNumberNode = Node.makeNode(Node.NodeType.IntLiteral);
        pinNumberNode.Value = pinNumber;
        ioTypeNode.Next = pinNumberNode;
        pinTypeNode.Next = ioTypeNode;
        createPinNode.Next = pinTypeNode;
        testNode.addChild(createPinNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains("Pin number already used"));
    }

    @Test
    void MasterInitiateCallStrategy_Returns_MasterPinPWMError() {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Call));
        SemanticsMasterInitiateCallStrategy strategy = new SemanticsMasterInitiateCallStrategy();
        strategy.UsedPinNumbers = new ArrayList<>();
        testNode.addStrategy(strategy);
        Node createPinNode = Node.makeNode(Node.NodeType.CreatePin);
        Node pinTypeNode = Node.makeNode(Node.NodeType.PWM);    //PWM type is bad here
        Node ioTypeNode = Node.makeNode(Node.NodeType.Input);
        Node pinNumberNode = Node.makeNode(Node.NodeType.IntLiteral);
        pinNumberNode.Value = "0";
        ioTypeNode.Next = pinNumberNode;
        pinTypeNode.Next = ioTypeNode;
        createPinNode.Next = pinTypeNode;
        testNode.addChild(createPinNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains("Pin cannot be PWM in master"));
    }

    @Test
    void MasterInitiateCallStrategy_Returns_MasterPinNotInputError() {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Call));
        SemanticsMasterInitiateCallStrategy strategy = new SemanticsMasterInitiateCallStrategy();
        strategy.UsedPinNumbers = new ArrayList<>();
        testNode.addStrategy(strategy);
        Node createPinNode = Node.makeNode(Node.NodeType.CreatePin);
        Node pinTypeNode = Node.makeNode(Node.NodeType.Digital);
        Node ioTypeNode = Node.makeNode(Node.NodeType.Output);  //Output type is bad here
        Node pinNumberNode = Node.makeNode(Node.NodeType.IntLiteral);
        pinNumberNode.Value = "0";
        ioTypeNode.Next = pinNumberNode;
        pinTypeNode.Next = ioTypeNode;
        createPinNode.Next = pinTypeNode;
        testNode.addChild(createPinNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains("Pin must be input in master"));
    }
    //endregion

    //region SemanticsMasterStrategy tests
    @Test
    void MasterStrategy_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Master));
        SemanticsMasterStrategy strategy = new SemanticsMasterStrategy();
        strategy.BroadcastEvents = new ArrayList<>();
        testNode.addStrategy(strategy);
        testNode.addChild(Node.makeNode(Node.NodeType.PinDeclaration));
        testNode.addChild(Node.makeNode(Node.NodeType.Empty));  //todo Why..?
        testNode.addChild(Node.makeNode(Node.NodeType.Initiate));
        testNode.addChild(Node.makeNode(Node.NodeType.Empty));

        //Act
        testNode.node.checkSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }
    //endregion

    //region SemanticsPinDeclarationStrategy tests
    @Test
    void PinDeclarationStrategy_Returns_NoErrors() throws SemanticsException {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.PinDeclaration));
        SemanticsPinDeclarationStrategy strategy = new SemanticsPinDeclarationStrategy();
        testNode.addStrategy(strategy);
        Node identifierNode = Node.makeNode(Node.NodeType.Identifier);
        Node callNode = Node.makeNode(Node.NodeType.Call);
        callNode.FirstChild = Node.makeNode(Node.NodeType.CreatePin);

        testNode.addChild(identifierNode);
        testNode.addChild(callNode);

        //Act
        testNode.node.checkSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void PinDeclarationStrategy_Returns_IllegalDeclError() {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.PinDeclaration));
        SemanticsPinDeclarationStrategy strategy = new SemanticsPinDeclarationStrategy();
        testNode.addStrategy(strategy);
        Node identifierNode = Node.makeNode(Node.NodeType.Identifier);
        Node callNode = Node.makeNode(Node.NodeType.Call);
        callNode.FirstChild = Node.makeNode(Node.NodeType.CreateEvent);

        testNode.addChild(identifierNode);
        testNode.addChild(callNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains("Illegal declaration of pin."));
    }

    @Test
    void PinDeclarationStrategy_Returns_AlreadyDeclaredError() {
        //Arrange
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.PinDeclaration));
        SemanticsPinDeclarationStrategy strategy = new SemanticsPinDeclarationStrategy();
        testNode.addStrategy(strategy);
        Node identifierNode = Node.makeNode(Node.NodeType.Identifier);
        testNode.addSymbol(identifierNode);
        Node callNode = Node.makeNode(Node.NodeType.Call);
        callNode.FirstChild = Node.makeNode(Node.NodeType.CreatePin);

        testNode.addChild(identifierNode);
        testNode.addChild(callNode);

        //Act
        Exception exception = Assertions.assertThrows(SemanticsException.class, testNode.node::checkSemantics);

        //Assert
        Assertions.assertTrue(exception.getMessage().contains(defaultErrorMessage(ErrorType.AlreadyDeclared)));
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
        TestNode testNode = new TestNode(Node.makeNode(Node.NodeType.Prog));
        SemanticsProgStrategy strategy = new SemanticsProgStrategy();
        testNode.addStrategy(strategy);
        testNode.addChild(Node.makeNode(Node.NodeType.Master));
        testNode.addChild(Node.makeNode(Node.NodeType.Slave));
        testNode.addChild(Node.makeNode(Node.NodeType.Empty));

        //Act
        testNode.node.checkSemantics();

        //Assert
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }
    //endregion

    //region SemanticsSlaveInitiateCallStrategy tests

    private TestNode setupSlaveInitiateCall(SemanticsSlaveInitiateCallStrategy strategy){
        Node CallNode = Node.makeNode(Node.NodeType.Call);

        TestNode testNode = setupNode(CallNode, strategy);

        Node emptyChild1 = Node.makeNode(Node.NodeType.Empty);
        testNode.addChild(emptyChild1);
        Node emptyChild2 = Node.makeNode(Node.NodeType.Empty);
        testNode.addChild(emptyChild2);

        return testNode;
    }

    @Test
    void SlaveInitiateCallStrategy_Returns_NoErrors() {
        SemanticsSlaveInitiateCallStrategy strategy = new SemanticsSlaveInitiateCallStrategy();
        // Add pin values to list of used pins
        strategy.UsedPinNumbers = new ArrayList<>();

        TestNode CallNode = setupSlaveInitiateCall(strategy);

        // Add the IO parameter
        Node IOParam = Node.makeNode(Node.NodeType.Output);
        CallNode.addChild(IOParam);
        // Add the pin parameter
        Node pinParam = Node.makeNode(Node.NodeType.Identifier);
        pinParam.Value = "testPin";
        CallNode.addChild(pinParam);

        Assertions.assertDoesNotThrow(CallNode.node::checkSemantics);
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

        TestNode CallNode = setupSlaveInitiateCall(strategy);

        // Add the IO parameter
        Node IOParam = Node.makeNode(Node.NodeType.Output);
        CallNode.addChild(IOParam);
        // Add the pin parameter
        Node pinParam = Node.makeNode(Node.NodeType.Identifier);
        pinParam.Value = pinValue;
        CallNode.addChild(pinParam);


        Exception exception = Assertions.assertThrows(
                SemanticsException.class,
                CallNode.node::checkSemantics
        );

        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }

    @Test
    void SlaveInitiateCallStrategy_Returns_SlavePinNotOutputError() {
        // The string expected as return from the error
        String errorString = "Pin must be output in slave";
        SemanticsSlaveInitiateCallStrategy strategy = new SemanticsSlaveInitiateCallStrategy();
        TestNode CallNode = setupSlaveInitiateCall(strategy);

        // Add the IO parameter
        Node IOParam = Node.makeNode(Node.NodeType.Input);
        CallNode.addChild(IOParam);
        // Add the pin parameter
        Node pinParam = Node.makeNode(Node.NodeType.Identifier);
        pinParam.Value = "TestPin";
        CallNode.addChild(pinParam);

        Exception exception = Assertions.assertThrows(
                SemanticsException.class,
                CallNode.node::checkSemantics
        );

        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }
    //endregion

    //region SemanticsSlaveStrategy tests
    @Test
    void SlaveStrategy_Returns_NoErrors() {
        Node SlaveNode = Node.makeNode(Node.NodeType.Slave);
        SemanticsSlaveStrategy strategy = new SemanticsSlaveStrategy();

        TestNode testNode = setupNode(SlaveNode, strategy);

        // Create and add first child node for the slave
        Node firstChildIdentifier = createNode(Node.NodeType.Identifier, Symbol.SymbolType.SLAVE, "TestVar");
        testNode.addChild(firstChildIdentifier);

        // Add empty children, to simulate global decls, initiate, and eventHandlers
        Node emptyChild1 = Node.makeNode(Node.NodeType.Empty);
        testNode.addChild(emptyChild1);
        Node emptyChild2 = Node.makeNode(Node.NodeType.Empty);
        testNode.addChild(emptyChild2);
        Node emptyChild3 = Node.makeNode(Node.NodeType.Empty);
        testNode.addChild(emptyChild3);

        Assertions.assertDoesNotThrow(testNode.node::checkSemantics);
    }

    @Test
    void SlaveStrategy_Returns_AlreadyDeclaredError() {
        // The string expected as return from the error
        String errorString = "already been declared";
        Node SlaveNode = Node.makeNode(Node.NodeType.Slave);
        SemanticsSlaveStrategy strategy = new SemanticsSlaveStrategy();

        TestNode testNode = setupNode(SlaveNode, strategy);

        // Create first child node for the slave
        Node firstChildIdentifier = createNode(Node.NodeType.Identifier, Symbol.SymbolType.SLAVE, "TestVar");
        // Enter the node into symbol table
        testNode.node.SemanticsCheckerStrategy.SymbolTable.EnterSymbol(firstChildIdentifier.Value, firstChildIdentifier.DataType);

        testNode.addChild(firstChildIdentifier);

        Exception exception = Assertions.assertThrows(
                    SemanticsException.class,
                    testNode.node::checkSemantics
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


