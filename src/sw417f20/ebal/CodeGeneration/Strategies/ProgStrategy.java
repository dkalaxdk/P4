package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.EventDictionary;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class ProgStrategy extends CodeGenerationStrategy {

    @Override
    public String GenerateCode(Node node) {
        EventList = new EventDictionary(node);

        String content = "";

        content += GenerateCodeForLinkedList(node.FirstChild);
        return content;
    }
}
