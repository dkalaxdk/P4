package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class FilterTypeStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {

        switch (node.Type) {
            case Range:
                return "range";
            case Debounce:
                return "debounce";
            case Constant:
                return "constant";
            default:
                return "ERROR";
        }
    }
}
