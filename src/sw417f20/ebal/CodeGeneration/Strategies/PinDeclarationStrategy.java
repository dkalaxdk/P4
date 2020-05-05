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

        board.PinDeclarations.add("ebalPin " + pinName + ";\n");

        Node call = node.FirstChild.Next;
        Node pinType = call.FirstChild.Next;
        Node ioType = pinType.Next;
        Node pinNumber = ioType.Next;

        board.PinInstantiations.add(pinName + ".createPin("
                + pinType.GenerateCode(arduinoSystem) + ", "
                + ioType.GenerateCode(arduinoSystem) + ", "
                + pinNumber.GenerateCode(arduinoSystem) + ");\n");

        board.Loop.add(pinName + ".readPin();\n");

        return "";
    }
}
