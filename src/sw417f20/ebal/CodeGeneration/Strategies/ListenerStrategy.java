package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class ListenerStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {

        String pinName = node.FirstChild.Value;

        String listenerName = pinName + "Listener" + arduinoSystem.Master.ListenerCount++;

        String block = node.FirstChild.Next.GenerateCode(arduinoSystem);

        arduinoSystem.Master.Listeners.add("void " + listenerName + "() " + block);

        arduinoSystem.Master.Loop.add(listenerName + "();\n");

        return "";
    }
}
