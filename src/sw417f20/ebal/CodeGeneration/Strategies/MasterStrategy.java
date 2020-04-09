package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.NodeList;
import sw417f20.ebal.SyntaxAnalysis.AST;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class MasterStrategy implements CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";
        //NodeList nodeList = new NodeList();
        int name = 0;
        //TODO Husk at sætte h filer ind
        content += "void setup(){\n";
        content += node.FirstChild.GenerateCode();
        content += "Wire.begin();";
        content += "}\n";

        content += "void loop() {\n";
        /*
        nodeList.VisitSiblings(node.FirstChild);
        for(Node listener : nodeList.nodeList){
            // The name should instead be controlled in a different class.
            name++;
            content += EmitListener(listener, name);
        }
         */
        Node childNode = node.FirstChild.Next;
        if(childNode.IsEmpty()) {
            return content;
        }

        //TODO: Tree traversal should maybe be handled by a separate class
        while(!childNode.IsEmpty()) {
            content += childNode.GenerateCode();
            childNode = childNode.Next;
        }
        content += "}\n";

        return content;
    }
}
