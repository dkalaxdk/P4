package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.ISymbolTable;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;


public abstract class SemanticsCheckerStrategy {

    public ISymbolTable SymbolTable;
    public enum ErrorType{ NotDeclared, AlreadyDeclared, WrongType, Default }
    public String AvailablePinOrEvent;

    /**
     * Abstract method that all strategies must implement.
     * Checks semantics for the given node.
     *
     * @param node Node that should be semantically checked
     */
    public abstract void CheckSemantics(Node node) throws SemanticsException;

    /**
     * Displays error message and terminates semantic analysis
     *
     * @param node Node that contains the line number of the error
     * @param name The name of the identifier or the description of the element that is the source of the error
     * @param errorType The type of error that has been found
     * @throws SemanticsException Throws an exception with a message about the error
     */
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

    /**
     * Displays error message and terminates semantic analysis
     *
     * @param node Node that contains the line number of the error
     * @param message The message describing the error that should be displayed
     * @throws SemanticsException Throws an exception with a message about the error
     */
    public void MakeError(Node node, String message) throws SemanticsException{
        throw new SemanticsException("Line " + node.LineNumber + ": " + message);
    }
}
