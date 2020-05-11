package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;

public class SemanticsMasterInitiateCallStrategy extends SemanticsCheckerStrategy{

    // List of pin numbers in use
    // Assigned by the strategy factory
    public ArrayList<String> UsedPinNumbers;

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        // The number of the pin is the third parameter equal to the fourth node
        String pinNumber = node.FirstChild.Next.Next.Next.Value;
        // Check that pinType is input
        if (node.FirstChild.Next.Next.Type == Node.NodeType.Input) {
            // Check IO Type is not PWM
            if (node.FirstChild.Next.Type != Node.NodeType.PWM) {
                // Check that pin number is not in use
                if (!UsedPinNumbers.contains(pinNumber)) {
                    node.DataType = Symbol.SymbolType.PIN;
                    UsedPinNumbers.add(pinNumber);
                } else {
                    MakeError(node, "Pin number already used");
                }
            } else {
                MakeError(node, "Pin cannot be PWM in master");
            }
        }
        else {
            MakeError(node, "Pin must be input in master");
        }
    }
}
