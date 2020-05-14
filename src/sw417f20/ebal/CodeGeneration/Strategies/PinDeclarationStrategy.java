package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoBoard;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class PinDeclarationStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {

        ArduinoBoard board;

        // Find the board for which this pin is declared
        // Master is -1, slaves are 0, 1, ...
        if (node.ArduinoID == -1) {
            board = arduinoSystem.Master;
        }
        else {
            board = arduinoSystem.SlaveList.get(node.ArduinoID);
        }

        // Get the name of the pin that is being declared
        String pinName = node.FirstChild.Value;

        // Add it to the board's pin declarations
        board.PinDeclarations
                .append("ebalPin ")
                .append(pinName)
                .append(";\n");

        // Sort through the call node to get the createPin's parameters
        Node call = node.FirstChild.Next;
        Node pinType = call.FirstChild.Next;
        Node ioType = pinType.Next;
        Node pinNumber = ioType.Next;

        // Add the pin to the board's pin instantiations,
        // and generate code for createPin's parameters
        board.PinInstantiations
                .append("\t")
                .append(pinName).append(".createPin(")
                .append(pinType.generateCode()).append(", ")
                .append(ioType.generateCode()).append(", ")
                .append(pinNumber.generateCode())
                .append(");\n");

        // Add the pin to the loop so it's value is constantly updated
        board.Loop
                .append("\t")
                .append(pinName)
                .append(".read();\n");

        return "";
    }
}
