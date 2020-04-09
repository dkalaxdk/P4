package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.NodeList;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class MasterStrategy implements CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";
        //NodeList nodeList = new NodeList();
        int name = 0;
        //TODO Husk at sætte h filer ind
        content += "void setup(){\n";
        //content += EmitInitiate(node.FirstChild);
        content += "Wire.begin();";
        content += "}\n";

        content += "void loop() {\n";
        //TODO: Tree traversal should be handles by a separate class
        /*
        nodeList.VisitSiblings(node.FirstChild);
        for(Node listener : nodeList.nodeList){
            name++;
            content += EmitListener(listener, name);
        }
         */
        content += "}\n";

        return content;
    }
}
