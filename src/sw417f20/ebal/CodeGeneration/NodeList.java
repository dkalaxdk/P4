package sw417f20.ebal.CodeGeneration;

import sw417f20.ebal.SyntaxAnalysis.Node;
import sw417f20.ebal.Visitors.Visitor;

import java.util.ArrayList;

/**
 * Concrete visitor that maintains a list of Nodes.
 */
public class NodeList extends Visitor {
    private ArrayList<Node> List = new ArrayList<>();

    @Override
    public void Visit(Node node) {
        List.add(node);
    }

    public ArrayList<Node> GetList() {
        return List;
    }
}
