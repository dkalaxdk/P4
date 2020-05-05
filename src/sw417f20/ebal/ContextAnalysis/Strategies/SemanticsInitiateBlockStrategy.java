package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;

public class SemanticsInitiateBlockStrategy extends SemanticsCheckerStrategy{

    public ArrayList<Integer> UsedPinNumbers;

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        Node child = node.FirstChild;
        while (child.Type != Node.NodeType.Empty) {
            if (child.Type == Node.NodeType.PinDeclaration) {
                child.CheckSemantics();
            }
            else {
                MakeError(child, "Only pin declarations allowed in Initiate");
            }
            child = child.Next;
        }
        UsedPinNumbers.clear();
    }
}
