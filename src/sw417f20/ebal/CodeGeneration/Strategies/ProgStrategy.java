package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class ProgStrategy extends CodeGenerationStrategy {

    @Override
    public String GenerateCode(Node node) {

        //Generates code for the the master and all the slaves
        GenerateCodeForLinkedList(node.FirstChild);

        System.out.println("======= CodeGen successful =======");

        return "";
    }
}
