package sw417f20.ebal.tests.ContextualAnalysisTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sw417f20.ebal.ContextAnalysis.HashSymbolTable;
import sw417f20.ebal.ContextAnalysis.Strategies.SemanticsAssignmentStrategy;
import sw417f20.ebal.ContextAnalysis.Strategies.SemanticsCheckerStrategy;
import sw417f20.ebal.ContextAnalysis.Strategies.SemanticsExpressionStrategy;
import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;
import sw417f20.ebal.Visitors.SemanticsStrategiesVisitor;

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
    void AssignmentStrategy_TypeInt_Returns_WrongTypeErrorThrown() throws SemanticsException {
        Node AssignmentNode = Node.MakeNode(Node.NodeType.Assignment);
        Node ExpressionNode = Node.MakeNode(Node.NodeType.Expression);
        Node nodeIdentifier = Node.MakeNode(Node.NodeType.Identifier);

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


        Assertions.assertThrows(SemanticsException.class, AssignmentNode::CheckSemantics);
    }

    @Test
    void AssignmentStrategy_TypeInt_Returns_Error_NotDeclared() throws SemanticsException {
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

        //In this test, the symbol has not been added to the symbol table, and therefor it cant run.
        AssignmentNode.SemanticsCheckerStrategy = assignmentStrategy;


        Assertions.assertThrows(SemanticsException.class, AssignmentNode::CheckSemantics);
    }

    @Test
    void BoolDeclarationStrategy_Correct() throws SemanticsException {
        Node AssignmentNode = Node.MakeNode(Node.NodeType.BoolDeclaration);
        Node ExpressionNode = Node.MakeNode(Node.NodeType.Expression);
        Node nodeIdentifier = Node.MakeNode(Node.NodeType.Identifier);

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


        Assertions.assertThrows(SemanticsException.class, AssignmentNode::CheckSemantics);
    }

}

class FakeStrategy extends SemanticsCheckerStrategy{
    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        // DO nothing
    }
}


