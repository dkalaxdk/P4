package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;


public class SemanticsProgStrategy extends SemanticsCheckerStrategy{

        @Override
        public void CheckSemantics(Node node) throws SemanticsException {
            Node child = node.FirstChild;
            // Check semantics for master
            child.checkSemantics();

            // Check semantics for slaves
            child = child.Next;
            while (!child.isEmpty()){
                child.checkSemantics();
                child = child.Next;
            }

            System.out.println("======= Semantics accepted =======");
        }
}
