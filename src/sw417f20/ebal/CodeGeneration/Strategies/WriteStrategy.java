package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class WriteStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {

        String pinName = node.Next.Value;
        String value = node.Next.Next.GenerateCode();

        return AddIndent(arduinoSystem.Indent) + pinName + ".write(" + value + ");\n";
    }
}
