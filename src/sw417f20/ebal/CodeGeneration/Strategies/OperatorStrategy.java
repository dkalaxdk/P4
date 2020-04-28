package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class OperatorStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {

        switch (node.Type){
            case LessThan:
                return " < ";
            case GreaterThan:
                return " > ";
            case NotEqual:
                return " != ";
            case Equals:
                return " == ";
            case GreaterOrEqual:
                return " >= ";
            case LessOrEqual:
                return " <= ";
            case And:
                return " && ";
            case Or:
                return " || ";
            case Plus:
                return " + ";
            case Minus:
                return " - ";
            case Times:
                return " * ";
            case Divide:
                //TODO find ud af hvordan man smider en fejl hvis man dividere med 0.
                return " / ";
            case Modulo:
                return " % ";
            default:
                return "ERROR";
        }
    }
}
