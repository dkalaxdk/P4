package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public interface CodeGenerationStrategy {
    String GenerateCode(Node node);
}
