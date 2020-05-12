package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class CallStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {

        // Call only visits the first child which is a function.
        // The different functions each visits their own parameters.
        return node.FirstChild.generateCode();
    }
}
