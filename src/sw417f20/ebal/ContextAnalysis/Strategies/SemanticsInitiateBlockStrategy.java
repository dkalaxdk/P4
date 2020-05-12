package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;

public class SemanticsInitiateBlockStrategy extends SemanticsCheckerStrategy{

    // List containing the pin numbers already used
    // This is assigned by the strategy factory
    public ArrayList<String> UsedPinNumbers;

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        // Check semantics for pin declarations
        Node child = node.FirstChild;
        while (!child.isEmpty()) {
            if (child.Type == Node.NodeType.PinDeclaration) {
                child.checkSemantics();
            }
            else {
                MakeError(child, "Only pin declarations allowed in Initiate");
            }
            child = child.Next;
        }
        UsedPinNumbers.clear();
    }
}
