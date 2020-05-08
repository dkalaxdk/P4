package sw417f20.ebal.CodeGeneration;

import sw417f20.ebal.CodeGeneration.Strategies.CodeGenerationStrategy;
import sw417f20.ebal.CodeGeneration.Strategies.StrategyFactory;
import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;
import sw417f20.ebal.Printers.Visitor;

/**
 * Assigns the appropriate Code Generation Strategy to nodes
 */
public class CodeGenerationStrategyAssigner extends Visitor {
    private final StrategyFactory strategies;
    private int arduinoID = -1;
    private ArduinoSystem arduinoSystem;

    public CodeGenerationStrategyAssigner(StrategyFactory strategyFactory, ArduinoSystem system) {
        strategies = strategyFactory;
        this.arduinoSystem = system;
    }

    @Override
    public void Visit(Node node) {


        switch(node.Type) {
            case Prog:
                AssignStrategy(node, strategies.GetProgStrategy());
                break;
            case Master:
                AssignStrategy(node, strategies.GetMasterStrategy());
                break;
            case Slave:
                AssignStrategy(node, strategies.GetSlaveStrategy());
                arduinoID++;
                break;
            case Block:
                AssignStrategy(node, strategies.GetBlockStrategy());
                break;
            case PinDeclaration:
                AssignStrategy(node, strategies.GetPinDeclarationStrategy());
                break;
            case BoolDeclaration:
                AssignStrategy(node, strategies.GetBoolDeclarationStrategy());
                break;
            case IntDeclaration:
                AssignStrategy(node, strategies.GetIntDeclarationStrategy());
                break;
            case FloatDeclaration:
                AssignStrategy(node, strategies.GetFloatDeclarationStrategy());
                break;
            case EventDeclaration:
                AssignStrategy(node, strategies.GetEventDeclarationStrategy());
                break;
            case Assignment:
                AssignStrategy(node, strategies.GetAssignmentStrategy());
                break;
            case If:
                AssignStrategy(node, strategies.GetIfStrategy());
                break;
            case Call:
                AssignStrategy(node, strategies.GetCallStrategy());
                break;
            case Broadcast:
                AssignStrategy(node, strategies.GetBroadcastStrategy());
                break;
            case Write:
                AssignStrategy(node, strategies.GetWriteStrategy());
                break;
            case GetValue:
                AssignStrategy(node, strategies.GetGetValueStrategy());
                break;
            case FilterNoise:
                AssignStrategy(node, strategies.GetFilterNoiseStrategy());
                break;
            case Digital:
            case Analog:
            case PWM:
                AssignStrategy(node, strategies.GetPinTypeStrategy());
                break;
            case Input:
            case Output:
                AssignStrategy(node, strategies.GetIOTypeStrategy());
                break;
            case Constant:
            case Debounce:
            case Range:
                AssignStrategy(node, strategies.GetFilterTypeStrategy());
                break;
            case Expression:
                AssignStrategy(node, strategies.GetExpressionStrategy());
                break;
            case Identifier:
                AssignStrategy(node, strategies.GetIdentifierStrategy());
                break;
            case PrefixMinus:
            case PrefixNot:
                AssignStrategy(node, strategies.GetPrefixStrategy());
                break;
            case IntLiteral:
            case BoolLiteral:
            case FloatLiteral:
                AssignStrategy(node, strategies.GetLiteralStrategy());
                break;
            case LessThan:
            case GreaterThan:
            case NotEqual:
            case Equals:
            case GreaterOrEqual:
            case LessOrEqual:
            case And:
            case Or:
            case Plus:
            case Minus:
            case Times:
            case Divide:
            case Modulo:
                AssignStrategy(node, strategies.GetOperatorStrategy());
                break;
            case EventHandler:
                AssignStrategy(node, strategies.GetEventHandlerStrategy());
                break;
            case Listener:
                AssignStrategy(node, strategies.GetListenerStrategy());
                break;
            case Initiate:
                AssignStrategy(node, strategies.GetInitiateStrategy());
                break;
            case CreateEvent:
            case CreatePin:
            case Empty:
                AssignStrategy(node, strategies.GetEmptyStrategy());
                break;
            default:
                System.err.println("Node type not found");
        }

        node.ArduinoID = arduinoID;
        node.CodeGenerationStrategy.setArduinoSystem(this.arduinoSystem);

        VisitChildren(node);
    }

    private void AssignStrategy(Node node, CodeGenerationStrategy strategy) {
        node.CodeGenerationStrategy = strategy;
    }



    // TODO: Implement or remove this
    public void Run(Node root) {
        AssignStrategyProg(root);
    }

    private void AssignStrategyProg(Node node) {
        AssignStrategy(node, strategies.GetProgStrategy());

        Node child = node.FirstChild;

        AssignStrategyMaster(child);

        child = child.Next;

        while (child != null && !child.IsEmpty()) {
            AssignStrategySlave(child);
            child = child.Next;
        }
    }

    private void AssignStrategyMaster(Node node) {
        AssignStrategy(node, strategies.GetMasterStrategy());

        Node child = node.FirstChild;

        AssignStrategyInitiate(child);

        child = child.Next;

        while (child != null && !child.IsEmpty()) {
            AssignStrategyListener(child);
            child = child.Next;
        }
    }

    private void AssignStrategyListener(Node node) {
        AssignStrategy(node, strategies.GetListenerStrategy());

        AssignStrategyIdentifier(node.FirstChild);

        AssignStrategyBlock(node.FirstChild.Next);
    }

    private void AssignStrategySlave(Node node) {
        AssignStrategy(node, strategies.GetSlaveStrategy());

        Node child = node.FirstChild.Next;

        AssignStrategyInitiate(child);

        child = child.Next;

        while (child != null && !child.IsEmpty()) {
            AssignStrategyEventHandler(child);
            child = child.Next;
        }
    }

    private void AssignStrategyEventHandler(Node node) {
        AssignStrategy(node, strategies.GetEventHandlerStrategy());

        AssignStrategyIdentifier(node.FirstChild);

        AssignStrategyBlock(node.FirstChild.Next);
    }

    private void AssignStrategyInitiate(Node node) {
        AssignStrategy(node, strategies.GetInitiateStrategy());

        AssignStrategyBlock(node.FirstChild);
    }

    private void AssignStrategyBlock(Node node) {
        if (node == null) return;

        AssignStrategy(node, strategies.GetBlockStrategy());

        Node child = node.FirstChild;

        while (child != null && !child.IsEmpty()) {
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

    private void AssignStrategyDeclaration(Node node) {

        switch (node.Type){
            case BoolDeclaration:
                AssignStrategy(node, strategies.GetBoolDeclarationStrategy());
                break;
            case IntDeclaration:
                AssignStrategy(node, strategies.GetIntDeclarationStrategy());
                break;
            case FloatDeclaration:
                AssignStrategy(node, strategies.GetFloatDeclarationStrategy());
                break;
            case PinDeclaration:
                AssignStrategy(node, strategies.GetPinDeclarationStrategy());
                break;
            case EventDeclaration:
                AssignStrategy(node, strategies.GetEventDeclarationStrategy());
                break;
        }

        AssignStrategy(node.FirstChild, strategies.GetIdentifierStrategy());

        if (node.FirstChild.Next != null && !node.FirstChild.Next.IsEmpty()) {
            AssignStrategyExpression(node.FirstChild.Next);
        }
    }

    private void AssignStrategyAssignment(Node node) {
        AssignStrategy(node, strategies.GetAssignmentStrategy());

        AssignStrategy(node.FirstChild, strategies.GetIdentifierStrategy());

        AssignStrategyExpression(node.FirstChild.Next);
    }

    private void AssignStrategyIf(Node node) {
        AssignStrategy(node, strategies.GetIfStrategy());

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

    private void AssignStrategyCall(Node node) {
        AssignStrategy(node, strategies.GetCallStrategy());

        switch (node.FirstChild.Type) {
            case Broadcast:
                AssignStrategy(node, strategies.GetBroadcastStrategy());
                break;
            case CreateEvent:
            case CreatePin:
                AssignStrategy(node, strategies.GetEmptyStrategy());
                break;
            case Write:
                AssignStrategy(node, strategies.GetWriteStrategy());
                break;
            case FilterNoise:
                AssignStrategy(node, strategies.GetFilterNoiseStrategy());
                break;
        }
    }

    private void AssignStrategyExpression(Node node) {
        switch (node.Type){
            case Expression:
                AssignStrategyExpressionNode(node);
                break;
            case IntLiteral:
            case FloatLiteral:
            case BoolLiteral:
                AssignStrategy(node, strategies.GetLiteralStrategy());
                break;
            case Identifier:
                AssignStrategy(node, strategies.GetIdentifierStrategy());
                break;
            case Call:
                AssignStrategyCall(node);
                break;
        }
    }

    private void AssignStrategyExpressionNode(Node node) {
        AssignStrategy(node, strategies.GetExpressionStrategy());

        AssignStrategyExpression(node.FirstChild);
        AssignStrategyExpression(node.FirstChild.Next.Next);
    }

    private void AssignStrategyEmpty(Node node) {

    }

    private void AssignStrategyFilterType(Node node) {

    }

    private void AssignStrategyIdentifier(Node node) {

    }

    private void AssignStrategyLiteral(Node node) {

    }

    private void AssignStrategyIOType(Node node) {

    }

    private void AssignStrategyOperator(Node node) {

    }

    private void AssignStrategyPrefix(Node node) {

    }



}
