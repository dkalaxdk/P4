package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class BroadcastStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {

        String eventName = node.Next.Value;

        return AddIndent(arduinoSystem.Indent) + eventName + ".broadcast();\n";
    }
}
