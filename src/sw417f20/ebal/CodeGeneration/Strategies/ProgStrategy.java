package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.Dictionaries;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class ProgStrategy extends CodeGenerationStrategy {

    @Override
    public String GenerateCode(Node node) {
        //Generates lists with event and slave information
        Lists = new Dictionaries(node);

        String content = "";

        //Generates code for the the master and all the slaves
        content += GenerateCodeForLinkedList(node.FirstChild);
        return content;
    }
}
