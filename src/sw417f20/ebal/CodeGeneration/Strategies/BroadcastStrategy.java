package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.Slave;
import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;

public class BroadcastStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";
        String event = node.FirstChild.Next.Value;
        ArrayList<Slave> slaves = EventList.GetEventAssociatedSlaves(node.FirstChild.Value);

        for (Slave slave : slaves) {
            content += "Wire.beginTransmission(" + slave.getID() + ");\n";
            content += "Wire.write(" + event + ");\n";
            content += "Wire.endTransmission();\n";
        }

        return content;
    }
}
