package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.Dictionaries;
import sw417f20.ebal.SyntaxAnalysis.Node;

public abstract class CodeGenerationStrategy {
    public Dictionaries Lists;
    /**
     * Abstract method that all strategies must implement.
     * Generated code for the given node.
     *
     * @param node Node that should be generated code for
     * @return String containing the generated code
     */
    public abstract String GenerateCode(Node node);

    /**
     * Generates code for all elements in a given linked list of nodes.
     * Useful as sibling nodes are arranged in singly linked lists
     *
     * @param headNode Head of the linked list of nodes
     * @return String containing the resulting code
     */
    public String GenerateCodeForLinkedList(Node headNode) {
        String content = "";
        if(headNode.IsEmpty()) {
            return content;
        }

        Node node = headNode;

        //TODO: Tree traversal should maybe be handled by a separate class
        while(!node.IsEmpty()) {
            content += node.GenerateCode();
            node = node.Next;
        }
        return content;
    }
}
