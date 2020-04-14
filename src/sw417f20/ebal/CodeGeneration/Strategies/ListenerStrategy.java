package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class ListenerStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";
        String pinName = node.FirstChild.Value;
        //pinValue is a magical variable name that will be overwritten each time a new listener is made.
        content += "pinValue" + " = ???Read(" + pinName + ");\n"; //TODO find ud af hvordan man finde typen af pin, brug symbol table

        // Generate code for block
        content += node.FirstChild.Next.GenerateCode();
        return content;
    }
}
