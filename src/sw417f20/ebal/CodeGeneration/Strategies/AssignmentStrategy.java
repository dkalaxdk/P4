package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.CodeGeneration;
import sw417f20.ebal.CodeGeneration.Utility.Slave;
import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;

public class AssignmentStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";
        //identifier will be the left part of the assignment.
        String identifier = node.FirstChild.Value;
        //expression will be the right side of the assignment.
        String expression = node.FirstChild.Next.GenerateCode();
        //If the right side of the assignment is the CreateEvent function, then this will be of the type CreateEvent node.
        Node createEvent = node.FirstChild.FirstChild;

        //In case that the right side of the assignment contains CreateEvent function, Then it will be true.
        if(createEvent.Type == Node.NodeType.CreateEvent){
            //The value that is assigned to the event
            String eventValue = createEvent.Next.Value;

            //The event is represented as a char array.
            content += "char " + identifier + "[4]";
            content += " = \"" + eventValue + "\";\n";
        }
        else{
            content += identifier + " = " + expression + ";\n";
        }

        return content;
    }
}
