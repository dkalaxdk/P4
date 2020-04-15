package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.CodeGeneration;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class AssignmentStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";
        String identifier = node.FirstChild.Value;
        String expression = node.FirstChild.Next.GenerateCode();

        content += identifier + " = " + expression + "\n";

        return content;
    }
}
