package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class ProgStrategy extends CodeGenerationStrategy {

    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {
        //Generates lists with event and slave information
//        eventDictionary = new EventDictionary(node);
//        slaveDictionary = new SlaveDictionary(node);

        //Generates code for the the master and all the slaves
        GenerateCodeForLinkedList(node.FirstChild, arduinoSystem);

        return "";
    }
}
