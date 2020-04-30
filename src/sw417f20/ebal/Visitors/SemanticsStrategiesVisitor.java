package sw417f20.ebal.Visitors;

import sw417f20.ebal.ContextAnalysis.HashSymbolTable;
import sw417f20.ebal.ContextAnalysis.Strategies.SemanticsCheckerStrategy;
import sw417f20.ebal.ContextAnalysis.Strategies.SemanticsStrategyFactory;
import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;

public class SemanticsStrategiesVisitor {

    public HashSymbolTable SymbolTable;
    public ArrayList<Integer> UsedPinNumbers;
    public ArrayList<Symbol> LocalEvents;

    private SemanticsStrategyFactory strategies;
    private boolean inSlave;
    private boolean inInitiate;

    public SemanticsStrategiesVisitor(){
        SymbolTable = new HashSymbolTable();
        UsedPinNumbers = new ArrayList<>();
        LocalEvents = new ArrayList<>();
        strategies = new SemanticsStrategyFactory(SymbolTable, UsedPinNumbers, LocalEvents);
        inSlave = false;
        inInitiate = false;
    }

    private void AssignStrategy (Node node, SemanticsCheckerStrategy strategy){
        node.SemanticsCheckerStrategy = strategy;
    }

    // Starts the process of assigning semantics strategies to the nodes of the AST with node (parameter) as its root
    public HashSymbolTable Run(Node node) {
        AssignStrategyProg(node);
        return SymbolTable;
    }

    // Assigns a strategy to the Prog node
    private void AssignStrategyProg(Node node){
        AssignStrategy(node,strategies.GetProgStrategy());

        Node child = node.FirstChild;
        AssignStrategyMaster(child);

        // Sets flag to help check that the slave cannot call broadcast()
        inSlave = true;
        child = child.Next;
        while (child.Type != Node.NodeType.Empty){
            AssignStrategySlave(child);
            child = child.Next;
        }
    }

    // Assigns a strategy to the Master node
    private void AssignStrategyMaster(Node node){
        AssignStrategy(node, strategies.GetMasterStrategy());

        Node child = node.FirstChild;
        AssignStrategyInitiate(child);
        child = child.Next;
        while (child.Type != Node.NodeType.Empty){
            AssignStrategyListener(child);
            child = child.Next;
        }
    }

    // Assigns a strategy to the Slave node
    private void AssignStrategySlave(Node node){
        AssignStrategy(node, strategies.GetSlaveStrategy());

        Node child = node.FirstChild.Next;
        AssignStrategyInitiate(child);
        child = child.Next;
        while (child.Type != Node.NodeType.Empty) {
            AssignStrategyEventHandler(child);
            child = child.Next;
        }
    }

    // Assigns a strategy to the Initiate node
    private void AssignStrategyInitiate(Node node){
        AssignStrategy(node, strategies. getInitiateStrategy());

        inInitiate = true;
        AssignStrategyBlock(node.FirstChild);
        inInitiate = false;
    }

    // Assigns a strategy to the Listener node
    private void AssignStrategyListener(Node node){
        AssignStrategy(node, strategies.getListenerStrategy());

        AssignStrategyBlock(node.FirstChild.Next);

    }

    // Assigns a strategy to the EventHandler node
    private void AssignStrategyEventHandler(Node node){
        AssignStrategy(node, strategies.getEventHandlerStrategy());

        AssignStrategyBlock(node.FirstChild.Next);
    }

    // Assigns a strategy to the block node, depending on the context of the node
    private void AssignStrategyBlock(Node node){

        if (inInitiate){
            AssignStrategy(node, strategies.getInitiateBlockStrategy());
        }
        else if (inSlave){
            AssignStrategy(node, strategies.getEventHandlerBlockStrategy());
        }
        else {
            AssignStrategy(node, strategies.getListenerBlockStrategy());
        }

        Node child = node.FirstChild;
        while (child.Type != Node.NodeType.Empty){
            switch (child.Type){
                case BoolDeclaration:
                case IntDeclaration:
                case FloatDeclaration:
                case PinDeclaration:
                case EventDeclaration:
                    AssignStrategyDeclaration(child);
                    break;
                case Assignment:
                    AssignStrategyAssignment(child);
                    break;
                case If:
                    AssignStrategyIf(child);
                    break;
                case Call:
                    AssignStrategyCall(child);
                    break;
            }
            child = child.Next;
        }
    }

    // Assigns a strategy to the node, depending on the type of declaration
    private void AssignStrategyDeclaration(Node node) {
        switch (node.Type){
            case BoolDeclaration:
                AssignStrategy(node, strategies.getBoolDeclarationStrategy());
                break;
            case IntDeclaration:
                AssignStrategy(node, strategies.getIntDeclarationStrategy());
                break;
            case FloatDeclaration:
                AssignStrategy(node, strategies.getFloatDeclarationStrategy());
                break;
            case PinDeclaration:
                AssignStrategy(node, strategies.getPinDeclarationStrategy());
                break;
            case EventDeclaration:
                AssignStrategy(node, strategies.getEventDeclarationStrategy());
                break;
        }

        if (!node.FirstChild.Next.IsEmpty()){
            AssignStrategyExpression(node.FirstChild.Next);
        }
    }

    // Assigns a strategy to the Assignment node
    private void AssignStrategyAssignment(Node node){
        AssignStrategy(node, strategies.getAssignmentStrategy());

        AssignStrategy(node.FirstChild, strategies.getIdentifierStrategy());
        AssignStrategyExpression(node.FirstChild.Next);
    }

    // Assigns a strategy to the If node
    private void AssignStrategyIf(Node node){
        AssignStrategy(node, strategies.getIfStrategy());

        AssignStrategyExpression(node.FirstChild);
        AssignStrategyBlock(node.FirstChild.Next);

        Node elseStmt = node.FirstChild.Next.Next;
        if (elseStmt.Type != Node.NodeType.Empty){
            if(elseStmt.Type == Node.NodeType.Block){
                AssignStrategyBlock(elseStmt);
            }
            else {
                AssignStrategyIf(elseStmt);
            }
        }
    }

    // Type checks the Call node depending on the function called
    private void AssignStrategyCall(Node node){

        if (inInitiate){
            if (inSlave){
                AssignStrategy(node, strategies.getSlaveInitiateCallStrategy());
            }
            else {
                AssignStrategy(node, strategies.getMasterInitiateCallStrategy());
            }
        }
        else if (inSlave){
            AssignStrategy(node, strategies.getEventHandlerCallStrategy());
            if (node.FirstChild.Type == Node.NodeType.Write){
                AssignStrategyExpression(node.FirstChild.Next.Next);
            }
        }
        else {
            AssignStrategy(node, strategies.getListenerCallStrategy());
            if (node.FirstChild.Type == Node.NodeType.CreateEvent){
                AssignStrategyExpression(node.FirstChild.Next);
            }
        }
    }

    // Checks which node type the node is and then checks the node as that type
    private void AssignStrategyExpression(Node node) {
        switch (node.Type){
            case Expression:
                AssignStrategyExpressionNode(node);
                break;
            case IntLiteral:
                AssignStrategy(node, strategies.getIntLiteralStrategy());
                break;
            case FloatLiteral:
                AssignStrategy(node, strategies.getFloatLiteralStrategy());
                break;
            case BoolLiteral:
                AssignStrategy(node, strategies.getBoolLiteralStrategy());
                break;
            case Identifier:
                AssignStrategy(node, strategies.getIdentifierStrategy());
                break;
            case Call:
                AssignStrategyCall(node);
                break;
        }
    }

    // Checks the expression node
    private void AssignStrategyExpressionNode(Node node) {
        AssignStrategy(node, strategies.getExpressionStrategy());

        AssignStrategyExpression(node.FirstChild);
        AssignStrategyExpression(node.FirstChild.Next.Next);
    }

}
