package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;

public class SemanticsSlaveInitiateCallStrategy extends SemanticsCheckerStrategy{

    public ArrayList<Integer> UsedPinNumbers;

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        int pinNumber = Integer.parseInt(node.FirstChild.Next.Next.Next.Value);

        if (node.FirstChild.Next.Next.Type == Node.NodeType.Output) {
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
