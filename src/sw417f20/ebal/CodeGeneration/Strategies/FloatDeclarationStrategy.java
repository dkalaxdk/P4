package sw417f20.ebal.CodeGeneration.Strategies;


import sw417f20.ebal.SyntaxAnalysis.Node;

public class FloatDeclarationStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "float ";

        // Add the identifier
        content += node.FirstChild.Value;

        Node nextChild = node.FirstChild.Next;
        //Initialization if there is an expression as the next node.
        if(!nextChild.IsEmpty()) {
            // Assign a value if there is an expression defined.
            content += " = ";
            content += nextChild.GenerateCode();
        }

        content += ";\n";
        return content;
    }
}