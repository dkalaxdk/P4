package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class CallStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";
        //Call only visits the first child which is a function.
        //The different functions each visits their own parameters.
        content += node.FirstChild.GenerateCode();

        return content;
    }
}
