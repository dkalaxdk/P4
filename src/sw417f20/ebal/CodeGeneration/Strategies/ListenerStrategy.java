package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class ListenerStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {

        // Get the name of the pin that is listened to
        String pinName = node.FirstChild.Value;

        // Create the unique Listener name for the Arduino
        String listenerName = pinName + "Listener" + arduinoSystem.Master.ListenerCount++;

        // Generate code for the Listener's block
        String block = node.FirstChild.Next.GenerateCode();

        // Add the Listener to the system's master with the correct signature
        arduinoSystem.Master.Listeners
                .append("void ")
                .append(listenerName)
                .append("() ")
                .append(block)
                .append("\n");

        // Call the Listener in the master's loop
        arduinoSystem.Master.Loop
                .append("\t")
                .append(listenerName)
                .append("();\n");

        return "";
    }
}
