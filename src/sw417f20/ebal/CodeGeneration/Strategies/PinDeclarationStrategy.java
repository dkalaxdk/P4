package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoBoard;
import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class PinDeclarationStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {

        ArduinoBoard board;

        if (node.ArduinoID == -1) {
            board = arduinoSystem.Master;
        }
        else {
            board = arduinoSystem.SlaveList.get(node.ArduinoID);
        }

        String pinName = node.FirstChild.Value;

        board.PinDeclarations
                .append("ebalPin ")
                .append(pinName)
                .append(";\n");

        Node call = node.FirstChild.Next;
        Node pinType = call.FirstChild.Next;
        Node ioType = pinType.Next;
        Node pinNumber = ioType.Next;

        board.PinInstantiations
                .append("\t")
                .append(pinName).append(".createPin(")
                .append(pinType.GenerateCode(arduinoSystem)).append(", ")
                .append(ioType.GenerateCode(arduinoSystem)).append(", ")
                .append(pinNumber.GenerateCode(arduinoSystem))
                .append(");\n");

        board.Loop
                .append("\t")
                .append(pinName)
                .append(".readPin();\n");

        return "";
    }
}
