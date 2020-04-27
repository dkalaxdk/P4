package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class OperatorStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";

        switch (node.Type){
            case LessThan:
                content += " < ";
                break;
            case GreaterThan:
                content += " > ";
                break;
            case NotEqual:
                content += " != ";
                break;
            case Equals:
                content += " == ";
                break;
            case GreaterOrEqual:
                content += " >= ";
                break;
            case LessOrEqual:
                content += " <= ";
                break;
            case And:
                content += " && ";
                break;
            case Or:
                content += " || ";
                break;
            case Plus:
                content += " + ";
                break;
            case Minus:
                content += " - ";
                break;
            case Times:
                content += " * ";
                break;
            case Divide:
                //TODO find ud af hvordan man smider en fejl hvis man devidere med 0.
                content += " / ";
                break;
            case Modulo:
                content += " % ";
                break;
        }

        return content;
    }
}
