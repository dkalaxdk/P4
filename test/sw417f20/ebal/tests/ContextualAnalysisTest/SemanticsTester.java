package sw417f20.ebal.tests.ContextualAnalysisTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sw417f20.ebal.ContextAnalysis.HashSymbolTable;
import sw417f20.ebal.ContextAnalysis.Strategies.*;
import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;
import sw417f20.ebal.Visitors.SemanticsStrategiesVisitor;

import java.util.ArrayList;

public class SemanticsTester {
    SemanticsStrategiesVisitor strategiesVisitor;

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

            if(node.FirstChild == null) {
                node.FirstChild = child;
            }
            else {
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
        Node AssignmentNode = Node.MakeNode(Node.NodeType.Assignment);

        SemanticsAssignmentStrategy assignmentStrategy = new SemanticsAssignmentStrategy();

        TestNode testNode = setupNode(AssignmentNode, assignmentStrategy);

        Node IdentifierNode = createNode(Node.NodeType.Identifier, identifierType, "TestVar");
        Node ExpressionNode = createNode(Node.NodeType.Expression, expressionType);
        testNode.addChild(IdentifierNode);
        testNode.addChild(ExpressionNode);

        return testNode;
    }

    private TestNode setupBoolDeclNodeWithoutSymbols(Symbol.SymbolType expressionType, Symbol.SymbolType identifierType) {
        Node DeclarationNode = Node.MakeNode(Node.NodeType.BoolDeclaration);

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
        Node BoolNode = Node.MakeNode(Node.NodeType.BoolDeclaration);
        SemanticsBoolLiteralStrategy boolLiteralStrategy = new SemanticsBoolLiteralStrategy();

        TestNode testNode = setupNode(BoolNode, boolLiteralStrategy);

        testNode.addChild(prefixNode);
        return testNode;
    }

    private TestNode setupEventDeclNode() {
        Node DeclarationNode = Node.MakeNode(Node.NodeType.EventDeclaration);
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
        Node node = Node.MakeNode(nodeType);
        node.DataType = dataType;
        return node;
    }

    @BeforeEach
    void setup() {
        strategiesVisitor = new SemanticsStrategiesVisitor();
    }

    @Test
    void AssignmentStrategy_TypeInt_Returns_NoErrorsThrown() throws SemanticsException {
        TestNode AssignmentNode = setupAssignmentNodeWithSymbols(Symbol.SymbolType.INT, Symbol.SymbolType.INT);

        AssignmentNode.node.CheckSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }
    @Test
    void AssignmentStrategy_TypeFloat_Returns_NoErrorsThrown() throws SemanticsException {
        TestNode AssignmentNode = setupAssignmentNodeWithSymbols(Symbol.SymbolType.FLOAT, Symbol.SymbolType.FLOAT);

        AssignmentNode.node.CheckSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void AssignmentStrategy_TypeBool_Returns_NoErrorsThrown() throws SemanticsException {
        TestNode AssignmentNode = setupAssignmentNodeWithSymbols(Symbol.SymbolType.BOOL, Symbol.SymbolType.BOOL);

        AssignmentNode.node.CheckSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }
    @Test
    void AssignmentStrategy_TypeInt_Returns_WrongTypeErrorThrown() {
        //The string expected as return from the error
        String errorString = "wrong type";

        TestNode AssignmentNode = setupAssignmentNodeWithSymbols(Symbol.SymbolType.INT, Symbol.SymbolType.BOOL);

        Exception exception = Assertions.assertThrows(SemanticsException.class, AssignmentNode.node::CheckSemantics);

        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }

    @Test
    void AssignmentStrategy_TypeInt_Returns_Error_NotDeclared() {
        //The string expected as return from the error
        String errorString = "not been declared";

        TestNode AssignmentNode = setupAssignmentNodeWithoutSymbols(Symbol.SymbolType.INT, Symbol.SymbolType.INT);

        //In this test, the symbol has not been added to the symbol table, and therefor it should fail.
        Exception exception = Assertions.assertThrows(SemanticsException.class, AssignmentNode.node::CheckSemantics);

        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }

    @Test
    void BoolDeclarationStrategy_Correct_Returns_NoErrors() throws SemanticsException {
        TestNode DeclarationNode = setupBoolDeclNodeWithoutSymbols(Symbol.SymbolType.BOOL, Symbol.SymbolType.BOOL);

        DeclarationNode.node.CheckSemantics();

        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void BoolDeclarationStrategy_Correct_Returns_AllReadyDefined() {
        //The string expected as return from the error
        String errorString = "has already been declared";

        TestNode DeclarationNode = setupBoolDeclNodeWithoutSymbols(Symbol.SymbolType.BOOL, Symbol.SymbolType.BOOL);

        // Add identifier (which is the first child) to symbolTable
        DeclarationNode.addSymbol(DeclarationNode.node.FirstChild);

        Exception exception = Assertions.assertThrows(SemanticsException.class, DeclarationNode.node::CheckSemantics);

        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }

    @Test
    void BoolDeclarationStrategy_Correct_Returns_WrongTypeDeclared() {
        //The string expected as return from the error
        String errorString = "wrong type";

        TestNode DeclarationNode = setupBoolDeclNodeWithoutSymbols(Symbol.SymbolType.INT, Symbol.SymbolType.BOOL);

        Exception exception = Assertions.assertThrows(SemanticsException.class, DeclarationNode.node::CheckSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }

    @Test
    void BoolLiteralStrategy_No_Prefix_Returns_NoErrors() throws SemanticsException {
        // Empty prefix node
        Node PrefixNode = createNode(Node.NodeType.Empty, Symbol.SymbolType.INT);

        TestNode BoolNode = setupBoolLiteralNode(PrefixNode);

        BoolNode.node.CheckSemantics();
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void BoolLiteralStrategy_Correct_Prefix_Returns_NoErrors() throws SemanticsException {
        // PrefixNot
        Node PrefixNode = createNode(Node.NodeType.PrefixNot, Symbol.SymbolType.INT);

        TestNode BoolNode = setupBoolLiteralNode(PrefixNode);

        BoolNode.node.CheckSemantics();
        Assertions.assertDoesNotThrow(SemanticsTester::new);
    }

    @Test
    void BoolLiteralStrategy_Wrong_Prefix_Returns_Errors() {
        //The string expected as return from the error
        String errorString = "Only not prefix";

        // PrefixMinus
        Node PrefixNode = createNode(Node.NodeType.PrefixMinus, Symbol.SymbolType.INT);

        TestNode BoolNode = setupBoolLiteralNode(PrefixNode);

        Exception exception = Assertions.assertThrows(SemanticsException.class, BoolNode.node::CheckSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }

    @Test
    void EventDeclaration_Correct_Returns_NoErrors() throws SemanticsException {
        TestNode DeclarationNode = setupEventDeclNode();

        Node ExpressionNode = Node.MakeNode(Node.NodeType.Call);
        Node IdentifierNode = Node.MakeNode(Node.NodeType.Identifier);
        Node CreateEventNode = Node.MakeNode(Node.NodeType.CreateEvent);
        Node ExpressionChild = Node.MakeNode(Node.NodeType.Expression);

        ExpressionNode.FirstChild = CreateEventNode;
        ExpressionNode.FirstChild.Next = ExpressionChild;
        IdentifierNode.Value = "TestVar";

        DeclarationNode.addChild(IdentifierNode);
        DeclarationNode.addChild(ExpressionNode);

        DeclarationNode.node.CheckSemantics();

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
        //The string expected as return from the error
        String errorString = "Illegal declaration";

        TestNode DeclarationNode = setupEventDeclNode();

        Node ExpressionNode = Node.MakeNode(Node.NodeType.Identifier);
        Node IdentifierNode = Node.MakeNode(Node.NodeType.Identifier);
        Node CreateEventNode = Node.MakeNode(Node.NodeType.CreateEvent);
        Node ExpressionChild = Node.MakeNode(Node.NodeType.Expression);

        ExpressionNode.FirstChild = CreateEventNode;
        ExpressionNode.FirstChild.Next = ExpressionChild;
        IdentifierNode.Value = "TestVar";

        DeclarationNode.addChild(IdentifierNode);
        DeclarationNode.addChild(ExpressionNode);

        Exception exception = Assertions.assertThrows(SemanticsException.class, DeclarationNode.node::CheckSemantics);
        Assertions.assertTrue(exception.getMessage().contains(errorString));
    }

    @Test
    void EventHandlerBlockStrategy_Returns_No_Errors() throws SemanticsException {
        // TODO: Refactor by creating setupEventHandler
        Node EventHandlerNode = Node.MakeNode(Node.NodeType.EventHandler);
        SemanticsEventHandlerBlockStrategy eventDeclarationStrategy = new SemanticsEventHandlerBlockStrategy();

        TestNode testNode = setupNode(EventHandlerNode, eventDeclarationStrategy);

        Node BlockNode = Node.MakeNode(Node.NodeType.Block);
        testNode.addChild(BlockNode);
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


