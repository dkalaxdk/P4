package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.EventDictionary;
import sw417f20.ebal.CodeGeneration.Utility.SlaveDictionary;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class ProgStrategy extends CodeGenerationStrategy {

    @Override
    public String GenerateCode(Node node) {
        //Generates lists with event and slave information
        eventDictionary = new EventDictionary(node);
        slaveDictionary = new SlaveDictionary(node);

        String content = "";

        //Generates code for the the master and all the slaves
        content += GenerateCodeForLinkedList(node.FirstChild);
        return content;
    }
}
