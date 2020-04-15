package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class BroadcastStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";
        String event = node.Next.Value;
        //TODO brug hvad end kristian laver til at finde ud af hvilken slaver der skal sendes til
        //Der skal laves et loop til at sende til alle slavere

        content += "Wire.beginTransmission(" + ");";
        content += "Wire.write(pinValue);";
        content += "Wire.endTransmission();";

        return content;
    }
}
