package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.EventDictionary;
import sw417f20.ebal.CodeGeneration.Utility.SlaveDictionary;
import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public abstract class CodeGenerationStrategy {
    public EventDictionary eventDictionary;
    public SlaveDictionary slaveDictionary;
    /**
     * Abstract method that all strategies must implement.
     * Generated code for the given node.
     *
     * @param node Node that should be generated code for
     * @return String containing the generated code
     */
    public abstract String GenerateCode(Node node, ArduinoSystem arduinoSystem);

    /**
     * Generates code for all elements in a given linked list of nodes.
     * Useful as sibling nodes are arranged in singly linked lists
     *
     * @param headNode Head of the linked list of nodes
     * @return String containing the resulting code
     */
    public String GenerateCodeForLinkedList(Node headNode, ArduinoSystem arduinoSystem) {
        String content = "";
        if(headNode.IsEmpty()) {
            return content;
        }

        Node node = headNode;

        //TODO: Tree traversal should maybe be handled by a separate class
        while(node != null && !node.IsEmpty()) {
            content += node.GenerateCode(arduinoSystem);
            node = node.Next;
        }
        return content;
    }
}
