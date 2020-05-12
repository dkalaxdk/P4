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
        eventDeclarationStrategy.LocalEvents = new ArrayList<Symbol>();

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
    void EventHandlerCallStrategy_Returns_NoErrors() {
        Node EventHandlerNode = Node.makeNode(Node.NodeType.EventHandler);
        Node WriteNode = Node.makeNode(Node.NodeType.Write);
        Node PinParameters = Node.makeNode(Node.NodeType.Expression);

        //The string expected as return from the error
        String errorString = "No pin or event";

        WriteNode.SemanticsCheckerStrategy = new FakeStrategy();

        SemanticsEventHandlerBlockStrategy eventDeclarationStrategy = new SemanticsEventHandlerBlockStrategy();
        eventDeclarationStrategy.SymbolTable = new HashSymbolTable();

        EventHandlerNode.FirstChild = WriteNode;

        EventHandlerNode.SemanticsCheckerStrategy = eventDeclarationStrategy;

        Exception exception = Assertions.assertThrows(SemanticsException.class, EventHandlerNode::checkSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));

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
    void InitiateBlockStrategy_Returns_NotPinDeclError() throws SemanticsException {
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
    void IntDeclarationStrategy_Returns_WrongTypeError() throws SemanticsException {
        String errorMessage = ": wrong type"; //todo lav fields til at repræsentere standard error messages

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
        Assertions.assertTrue(exception.getMessage().contains(errorMessage));
    }

    @Test
    void IntDeclarationStrategy_Returns_AlreadyDeclaredError() throws SemanticsException {
        assert(false);
    }
    //endregion

    //region SemanticsIntLiteralStrategy tests
    @Test
    void IntLiteralStrategy_Returns_NoErrors() throws SemanticsException {
        assert(false);
    }

    @Test
    void IntLiteralStrategy_Returns_NotMinusPrefixError() throws SemanticsException {
        assert(false);
    }
    //endregion

    //region SemanticsListenerBlockStrategy tests
    @Test
    void ListenerBlockStrategy_Returns_NoErrors() throws SemanticsException {
        assert(false);
    }

    @Test
    void ListenerBlockStrategy_Returns_IsPinDeclError() throws SemanticsException {
        assert(false);
    }
    //endregion

    //region SemanticsListenerCallStrategy tests
    @Test
    void ListenerCallStrategy_CheckBroadcast_Returns_NoErrors() throws SemanticsException {
        assert(false);
    }

    @Test
    void ListenerCallStrategy_CheckBroadcast_Returns_WrongTypeError() throws SemanticsException {
        assert(false);
    }

    @Test
    void ListenerCallStrategy_CheckBroadcast_Returns_NotDeclaredError() throws SemanticsException {
        assert(false);
    }

    @Test
    void ListenerCallStrategy_CheckGetValue_Returns_NoErrors() throws SemanticsException {
        assert(false);
    }

    @Test
    void ListenerCallStrategy_CheckGetValue_Returns_WrongTypeError() throws SemanticsException {
        assert(false);
    }

    @Test
    void ListenerCallStrategy_CheckGetValue_Returns_PinUnavailableError() throws SemanticsException {
        assert(false);
    }

    @Test
    void ListenerCallStrategy_CheckGetValue_Returns_NotDeclaredError() throws SemanticsException {
        assert(false);
    }

    @Test
    void ListenerCallStrategy_CheckFilterNoise_Returns_NoErrors() throws SemanticsException {
        assert(false);
    }

    @Test
    void ListenerCallStrategy_CheckFilterNoise_Returns_WrongTypeError() throws SemanticsException {
        assert(false);
    }

    @Test
    void ListenerCallStrategy_CheckFilterNoise_Returns_NotDeclaredError() throws SemanticsException {
        assert(false);
    }

    @Test
    void ListenerCallStrategy_CheckFilterNoise_Returns_PinUnavailableError() throws SemanticsException {
        assert(false);
    }

    @Test
    void ListenerCallStrategy_CheckCreateEvent_Returns_NoErrors() throws SemanticsException {
        assert(false);
    }

    @Test
    void ListenerCallStrategy_CheckCreateEvent_Returns_WrongTypeError() throws SemanticsException {
        assert(false);
    }

    @Test
    void ListenerCallStrategy_Returns_IllegalFuncCallError() throws SemanticsException {
        assert(false);
    }
    //endregion

    //region SemanticsListenerStrategy tests
    @Test
    void ListenerStrategy_Returns_NoErrors() throws SemanticsException {
        assert(false);
    }

    @Test
    void ListenerStrategy_Returns_WrongTypeError() throws SemanticsException {
        assert(false);
    }

    @Test
    void ListenerStrategy_Returns_NotDeclaredError() throws SemanticsException {
        assert(false);
    }
    //endregion

    //region SemanticsMasterInitiateCallStrategy tests
    @Test
    void MasterInitiateCallStrategy_Returns_NoErrors() throws SemanticsException {
        assert(false);
    }

    @Test
    void MasterInitiateCallStrategy_Returns_PinAlreadyUsedError() throws SemanticsException {
        assert(false);
    }

    @Test
    void MasterInitiateCallStrategy_Returns_MasterPinPWMError() throws SemanticsException {
        assert(false);
    }

    @Test
    void MasterInitiateCallStrategy_Returns_MasterPinNotInputError() throws SemanticsException {
        assert(false);
    }
    //endregion

    //region SemanticsMasterStrategy tests
    @Test
    void MasterStrategy_Returns_NoErrors() throws SemanticsException {
        assert(false);
    }
    //endregion

    //region SemanticsPinDeclarationStrategy tests
    @Test
    void PinDeclarationStrategy_Returns_NoErrors() throws SemanticsException {
        assert(false);
    }

    @Test
    void PinDeclarationStrategy_Returns_IllegalDeclError() throws SemanticsException {
        assert(false);
    }

    @Test
    void PinDeclarationStrategy_Returns_AlreadyDeclaredError() throws SemanticsException {
        assert(false);
    }

    @Test
    void PinDeclarationStrategy_Returns_GlobalPinError() throws SemanticsException {
        assert(false);
    }
    //endregion

    //region SemanticsProgStrategy tests
    @Test
    void ProgStrategy_Returns_NoErrors() throws SemanticsException {
        assert(false);
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
    void SlaveInitiateCallStrategy_Returns_NoErrors() throws SemanticsException {
        SemanticsSlaveInitiateCallStrategy strategy = new SemanticsSlaveInitiateCallStrategy();
        // Add pin values to list of used pins
        ArrayList<String> usedPins = new ArrayList<String>();
        strategy.UsedPinNumbers = usedPins;

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
    void SlaveInitiateCallStrategy_Returns_PinAlreadyUsedError() throws SemanticsException {
        // The string expected as return from the error
        String errorString = "Pin number already used";
        String pinValue = "TestPin";
        SemanticsSlaveInitiateCallStrategy strategy = new SemanticsSlaveInitiateCallStrategy();
        // Add pin values to list of used pins
        ArrayList<String> usedPins = new ArrayList<String>();
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
    void SlaveInitiateCallStrategy_Returns_SlavePinNotOutputError() throws SemanticsException {
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
    void SlaveStrategy_Returns_NoErrors() throws SemanticsException {
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
    void SlaveStrategy_Returns_AlreadyDeclaredError() throws SemanticsException {
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


