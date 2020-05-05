package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;


public class SemanticsProgStrategy extends SemanticsCheckerStrategy{

        @Override
        public void CheckSemantics(Node node) throws SemanticsException {
            Node child = node.FirstChild;
            child.CheckSemantics();

            child = child.Next;
            while (child.Type != Node.NodeType.Empty){
                child. CheckSemantics();
                child = child.Next;
            }
        }
}
