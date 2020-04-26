package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class EventHandlerStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";
        String eventID = Lists.GetEventID(node.FirstChild.Value);

        content += "if (input ==" + eventID + ")\n";

        //generate code for block.
        content += node.FirstChild.Next.GenerateCode();

        return content;
    }
}
