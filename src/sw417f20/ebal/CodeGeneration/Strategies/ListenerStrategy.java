package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class ListenerStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {

        String pinName = node.FirstChild.Value;

        String listenerName = pinName + "Listener" + arduinoSystem.Master.ListenerCount++;

        String block = node.FirstChild.Next.GenerateCode(arduinoSystem);

        arduinoSystem.Master.Listeners
                .append("void ")
                .append(listenerName)
                .append("() ")
                .append(block)
                .append("\n");

        arduinoSystem.Master.Loop
                .append("\t")
                .append(listenerName)
                .append("();\n");

        return "";
    }
}
