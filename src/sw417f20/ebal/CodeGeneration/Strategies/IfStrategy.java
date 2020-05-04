package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class IfStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {
//        String content = "";
//        String expression = node.FirstChild.GenerateCode(arduinoSystem);
//        String block = node.FirstChild.Next.GenerateCode(arduinoSystem);
//        Node thirdChild = node.FirstChild.Next.Next;
//
//        content += "if (" + expression + ") ";
//        content += block;
//
//        //Checks if the the third child is empty
//        //if false then it generates code for either a else or else if statement.
//        if (thirdChild == null){
//            return addIndentation(arduinoSystem.Indentation) + content;
//        }
//        else{
//            content += addIndentation(arduinoSystem.Indentation) + "else " + thirdChild.GenerateCode(arduinoSystem);
//        }
//
//        return content;


        String expression = node.FirstChild.GenerateCode(arduinoSystem);
        String block = node.FirstChild.Next.GenerateCode(arduinoSystem);
        String afterIf = node.FirstChild.Next.Next.GenerateCode(arduinoSystem);

        return "";
    }
}
