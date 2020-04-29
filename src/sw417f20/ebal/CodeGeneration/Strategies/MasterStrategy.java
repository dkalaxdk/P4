package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.NodeList;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class MasterStrategy extends CodeGenerationStrategy {
    private final int debounce = 300;
    @Override
    public String GenerateCode(Node node) {
        String content = "";
        content += "const long debounce = " + debounce + ";";
        //TODO Husk at sætte h filer ind
        content += "void setup(){\n";
        content += node.FirstChild.GenerateCode();
        content += "Wire.begin();\n";
        content += "}\n";

        content += "void loop() {\n";
        //pinValue will be overwritten each time a new listener is called
        content += "int pinValue;\n";
        // Generates code for all the EventCreators
        content += GenerateCodeForLinkedList(node.FirstChild.Next);
        content += "}\n";

        return content;
    }
}
