package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class AssignmentStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {

        String identifier = node.FirstChild.Value;
        String expression = node.FirstChild.Next.generateCode();

        return addIndent(arduinoSystem.Indent) + identifier + " = " + expression + ";\n";
    }
}
