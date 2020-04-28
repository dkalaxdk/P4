package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

/**
 * This should never be called in the case where an expression node only has a single child
 */
public class ExpressionStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {

        // Generate code for child nodes.
        // Child nodes should be: Operand, Operator and Expression
        return "(" + GenerateCodeForLinkedList(node.FirstChild) + ")";
    }
}
