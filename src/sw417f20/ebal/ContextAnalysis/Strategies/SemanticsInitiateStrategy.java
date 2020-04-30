package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SemanticsInitiateStrategy extends SemanticsCheckerStrategy{

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        node.FirstChild.CheckSemantics();
    }
}
