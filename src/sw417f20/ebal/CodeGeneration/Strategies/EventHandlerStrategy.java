package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class EventHandlerStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";
        int eventID = eventDictionary.GetEventID(node.FirstChild.Value);
        //TODO skal laves om så det passer med hvordan vi håndtere events.
        content += "if (input ==" + eventID + ")\n";

        //generate code for block.
        content += node.FirstChild.Next.GenerateCode();

        return content;
    }
}
