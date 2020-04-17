package sw417f20.ebal.CodeGeneration;

import sw417f20.ebal.SyntaxAnalysis.Node;
import sw417f20.ebal.Visitors.Visitor;

import java.util.ArrayList;

public class NodeList extends Visitor {
    public ArrayList<Node> nodeList = new ArrayList<>();

    @Override
    public void Visit(Node node) {
        nodeList.add(node);
    }
}
