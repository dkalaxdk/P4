package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.HashSymbolTable;
import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;

public abstract class SemanticsCheckerStrategy {

    HashSymbolTable SymbolTable;
    public ArrayList<Integer> UsedPinNumbers;
    public ArrayList<Symbol> LocalEvents;
    public enum ErrorType{ NotDeclared, AlreadyDeclared, WrongType, Default}


    /**
     * Abstract method that all strategies must implement.
     * Checks semantics for the given node.
     *
     * @param node Node that should be semantically checked for
     */
    public abstract void CheckSemantics(Node node) throws SemanticsException;

    /**
     * Checks semantics for all elements in a given linked list of nodes.
     * Useful as sibling nodes are arranged in singly linked lists
     *
     * @param headNode Head of the linked list of nodes
     */
    public void CheckSemanticsForLinkedList(Node headNode) {
        String content = "";
        if(headNode.IsEmpty()) {

        }

        Node node = headNode;

        //TODO: Tree traversal should maybe be handled by a separate class
        while(!node.IsEmpty()) {
            //content += node.CheckSemantics();
            node = node.Next;
        }
    }

    // Displays error message and terminates semantic analysis
    public void MakeError(Node node, String name, ErrorType errorType) throws SemanticsException {
        String message = "";

        switch (errorType){
            case AlreadyDeclared:
                message = "Line " + node.LineNumber + ": " + name + " has already been declared.";
                break;
            case NotDeclared:
                message = "Line " + node.LineNumber + ": " + name + " has not been declared.";
                break;
            case WrongType:
                message = "Line " + node.LineNumber + ": " + name + ": wrong type";
                break;
            case Default:
                message = "Line " + node.LineNumber + ": " + name + ": error";
        }

        throw new SemanticsException(message);
    }

    public void MakeError(Node node, String message) throws SemanticsException{
        throw new SemanticsException("Line " + node.LineNumber + ": " + message);
    }
}
