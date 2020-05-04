package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class CreateEventStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {
//        String content = "";
        //Returns an empty string.
        //The target code for create event is made in EventDeclarationStrategy and AssignmentStrategy.
        //Since you can't access the Identifier that the CreateEvent function gets assigned too.
        return "";
    }
}
