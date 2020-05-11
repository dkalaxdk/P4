package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;

public class SemanticsSlaveInitiateCallStrategy extends SemanticsCheckerStrategy{

    // List of pin numbers already in use
    // Assigned by strategy factory
    public ArrayList<String> UsedPinNumbers;

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        // The pin is the third parameter, which is the fourth child node
        String pinNumber = node.FirstChild.Next.Next.Next.Value;

        // Check that IO type is output
        if (node.FirstChild.Next.Next.Type == Node.NodeType.Output) {
            // Check that pin is not already in use
            if (!UsedPinNumbers.contains(pinNumber)) {
                node.DataType = Symbol.SymbolType.PIN;
                UsedPinNumbers.add(pinNumber);
            }
            else{
                MakeError(node, "Pin number already used");
            }
        }
        else {
            MakeError(node, "Pin must be output in slave");
        }
    }
}
