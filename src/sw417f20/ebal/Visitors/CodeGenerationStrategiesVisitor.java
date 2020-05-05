package sw417f20.ebal.Visitors;

import sw417f20.ebal.CodeGeneration.Strategies.CodeGenerationStrategy;
import sw417f20.ebal.CodeGeneration.Strategies.StrategyFactory;
import sw417f20.ebal.SyntaxAnalysis.Node;

/**
 * Assigns the appropriate Code Generation Strategy to nodes
 */
public class CodeGenerationStrategiesVisitor extends Visitor {
    private StrategyFactory strategies;
    private int arduinoID = -1;

    public CodeGenerationStrategiesVisitor(StrategyFactory strategyFactory) {
        strategies = strategyFactory;
    }

    @Override
    public void Visit(Node node) {

        node.ArduinoID = arduinoID;

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
            case CreateEvent:
            case CreatePin:
            case EventHandler:
                AssignStrategy(node, strategies.GetEventHandlerStrategy());
                break;
            case Listener:
                AssignStrategy(node, strategies.GetListenerStrategy());
                break;
            case Initiate:
                AssignStrategy(node, strategies.GetInitiateStrategy());
                break;
            case Empty:
                AssignStrategy(node, strategies.GetEmptyStrategy());
                break;
            default:
                System.err.println("Node type not found");
        }

        VisitChildren(node);
    }

    private void AssignStrategy(Node node, CodeGenerationStrategy strategy) {
        node.CodeGenerationStrategy = strategy;
    }
}
