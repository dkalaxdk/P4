package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class ListenerStrategy extends CodeGenerationStrategy {
    private String functionName; // TODO: Should be gotten from a shared state
    @Override
    public String GenerateCode(Node node) {
        String content = "";
        String pinName = node.FirstChild.Value;
        content += "reading" + functionName + " = ??? (" + functionName + ");\n"; //TODO find ud af hvordan man finde typen af pin, brug symbol table

        // Generate code for block
        content += node.FirstChild.Next.GenerateCode();
        return content;
    }
}
