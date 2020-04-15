package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class WriteStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";
        //TODO find ud af hvad pin nummer den har
        String pinNumber = node.Next.GenerateCode();
        //TODO find ud af om pin er digital eller analog
        String pinType = "digital";
        String output = node.Next.GenerateCode();

        content += pinType + "Write(" + pinNumber + "," + output +");\n";

        return content;
    }
}
