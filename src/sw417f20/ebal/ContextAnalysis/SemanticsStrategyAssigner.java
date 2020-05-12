package sw417f20.ebal.ContextAnalysis;

import sw417f20.ebal.ContextAnalysis.Strategies.SemanticsCheckerStrategy;
import sw417f20.ebal.ContextAnalysis.Strategies.SemanticsStrategyFactory;
import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;

/**
 * The class responsible for assigning semantic checking strategies to the nodes of an AST.
 */
public class SemanticsStrategyAssigner {

    public ISymbolTable SymbolTable;
    public ArrayList<String> UsedPinNumbers;
    public ArrayList<Symbol> LocalEvents;
    public ArrayList<Symbol> BroadcastEvents;

    private SemanticsStrategyFactory strategies;
    private boolean inSlave;
    private boolean inInitiate;
    private String AvailablePinOrEvent;
    private boolean IsGlobalDeclaration;

    public SemanticsStrategyAssigner() {
        SymbolTable = new HashSymbolTable();
        UsedPinNumbers = new ArrayList<>();
        LocalEvents = new ArrayList<>();
        BroadcastEvents = new ArrayList<>();
        strategies = new SemanticsStrategyFactory(SymbolTable, UsedPinNumbers, LocalEvents, BroadcastEvents);
        inSlave = false;
        inInitiate = false;
        IsGlobalDeclaration = false;
    }

    // Helper method to assign strategies
    private void AssignStrategy (Node node, SemanticsCheckerStrategy strategy){
        node.SemanticsCheckerStrategy = strategy;
    }

    // Starts the process of assigning semantics strategies to the nodes of the AST with node (parameter) as its root.
    public ISymbolTable Run(Node node) {
        AssignStrategyProg(node);
        return SymbolTable;
    }

    // Assigns a strategy to the Prog node.
    // Takes the root node of the AST, which has a master and a number of slaves as children.
    private void AssignStrategyProg(Node node){
        AssignStrategy(node, strategies.GetProgStrategy());

        Node child = node.FirstChild;
        AssignStrategyMaster(child);

        // Sets flag to help check for differences between master and slave.
        inSlave = true;
        child = child.Next;
        // Iterates through linked list of siblings
        while (!child.isEmpty()){
            AssignStrategySlave(child);
            child = child.Next;
        }
    }

    // Assigns a strategy to the Master node.
    // Takes the Master node, which has an Initiate node and a number of Listener nodes as children.
    private void AssignStrategyMaster(Node node){
        AssignStrategy(node, strategies.GetMasterStrategy());

        Node child = node.FirstChild;
        IsGlobalDeclaration = true;
        // Assign strategies to declarations in global scope of the master
        while (!child.isEmpty()){
            AssignStrategyDeclaration(child);
            child = child.Next;
        }
        IsGlobalDeclaration = false;

        // Assign strategy to Initiate in master
        child = child.Next;
        AssignStrategyInitiate(child);
        child = child.Next;

        // Assign strategies to Listeners
        while (!child.isEmpty()){
            AssignStrategyListener(child);
            child = child.Next;
        }
    }

    // Assigns a strategy to the Slave node.
    // Takes a Slave node, which has an Initiate node and a number of EventHandler nodes as children.
    private void AssignStrategySlave(Node node){
        AssignStrategy(node, strategies.GetSlaveStrategy());

        Node child = node.FirstChild.Next;
        IsGlobalDeclaration = true;
        // Assign strategies to declarations in global scope of the slave
        while (!child.isEmpty()){
            AssignStrategyDeclaration(child);
            child = child.Next;
        }
        IsGlobalDeclaration = false;

        // Assign strategy to initiate in slave
        child = child.Next;
        AssignStrategyInitiate(child);
        child = child.Next;

        // Assign strategies to EventHandlers
        while (!child.isEmpty()) {
            AssignStrategyEventHandler(child);
            child = child.Next;
        }
    }

    // Assigns a strategy to the Initiate node.
    // Takes an Initiate node, which has a Block node as its child.
    private void AssignStrategyInitiate(Node node){
        AssignStrategy(node, strategies. getInitiateStrategy());

        inInitiate = true;
        AssignStrategyBlock(node.FirstChild);
        inInitiate = false;
    }

    // Assigns a strategy to the Listener node.
    // Takes a Listener node, which has a Block node as its child.
    private void AssignStrategyListener(Node node){
        AssignStrategy(node, strategies.getListenerStrategy());

        // Stores the Pin that the Listener is listening to,
        // in order to check that it does not try to access any other pins.
        AvailablePinOrEvent = node.FirstChild.Value;

        AssignStrategyBlock(node.FirstChild.Next);

    }

    // Assigns a strategy to the EventHandler node.
    // Takes an EventHandler node, which has a Block node as its child.
    private void AssignStrategyEventHandler(Node node){
        AssignStrategy(node, strategies.getEventHandlerStrategy());
        AvailablePinOrEvent = node.FirstChild.Value;

        AssignStrategyBlock(node.FirstChild.Next);
    }

    // Assigns a strategy to the block node, depending on the context of the node.
    // Takes a Block node, which has a number of declaration, Assignment, If and Call nodes as children.
    private void AssignStrategyBlock(Node node){

        //Assigns a strategy to node, if it is in an Initiate, an EventHandler or a Listener.
        if (inInitiate){
            AssignStrategy(node, strategies.getInitiateBlockStrategy());
        }
        else if (inSlave){
            AssignStrategy(node, strategies.getEventHandlerBlockStrategy());
        }
        else {
            AssignStrategy(node, strategies.getListenerBlockStrategy());
        }

        // Calls AssignStrategy methods on node's children depending on their node type.
        Node child = node.FirstChild;
        while (!child.isEmpty()){
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

    // Assigns a strategy to the node, depending on the type of declaration.
    // Takes a type of declaration node, which has an identifier and possibly an expression as its children.
    private void AssignStrategyDeclaration(Node node) {
        //Assigns a strategy to node depending on the type of the declaration.
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
                AssignStrategy(node, strategies.getPinDeclarationStrategy(IsGlobalDeclaration));
                break;
            case EventDeclaration:
                AssignStrategy(node, strategies.getEventDeclarationStrategy(IsGlobalDeclaration));
                break;
        }

        // Assigns a strategy to the expression child of the declaration if it has one.
        if (!node.FirstChild.Next.isEmpty()){
            AssignStrategyExpression(node.FirstChild.Next);
        }
    }

    // Assigns a strategy to the Assignment node.
    //Takes an Assignment node, which has an Identifier and an expression node as children.
    private void AssignStrategyAssignment(Node node){
        AssignStrategy(node, strategies.getAssignmentStrategy());

        AssignStrategy(node.FirstChild, strategies.getIdentifierStrategy());
        AssignStrategyExpression(node.FirstChild.Next);
    }

    // Assigns a strategy to the If node.
    // Takes an If node, which has an expression, a Block and possibly a second
    // Block or If node (representing the else statement) as its children.
    private void AssignStrategyIf(Node node){
        AssignStrategy(node, strategies.getIfStrategy());

        AssignStrategyExpression(node.FirstChild);
        AssignStrategyBlock(node.FirstChild.Next);

        Node elseStmt = node.FirstChild.Next.Next;
        if (!elseStmt.isEmpty()){
            if(elseStmt.Type == Node.NodeType.Block){
                AssignStrategyBlock(elseStmt);
            }
            else {
                AssignStrategyIf(elseStmt);
            }
        }
    }

    // Assigns a strategy to the Call node, depending on its context in the AST.
    // Takes a Call node, which has a function node (with the called function)
    // and a number of expression nodes (representing function parameters).
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
            node.SemanticsCheckerStrategy.AvailablePinOrEvent = AvailablePinOrEvent;
            // Assign strategy to expression parameter, if the call is to the write function
            if (node.FirstChild.Type == Node.NodeType.Write){
                AssignStrategyExpression(node.FirstChild.Next.Next);
            }
        }
        else {
            AssignStrategy(node, strategies.getListenerCallStrategy());
            node.SemanticsCheckerStrategy.AvailablePinOrEvent = AvailablePinOrEvent;
            // Assign strategy to expression parameter, if the call is to the createEvent function
            if (node.FirstChild.Type == Node.NodeType.CreateEvent){
                AssignStrategyExpression(node.FirstChild.Next);
            }
        }
    }

    // Checks which node type the node is and then assigns the appropriate strategy to the node
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

    // Assigns a strategy to the expression node
    // An expression has an operand, an operator and another expression as children.
    private void AssignStrategyExpressionNode(Node node) {
        AssignStrategy(node, strategies.getExpressionStrategy());

        // Assign strategy to operand, which can be an expression
        AssignStrategyExpression(node.FirstChild);
        // Assign strategy to expression
        AssignStrategyExpression(node.FirstChild.Next.Next);
    }

}
