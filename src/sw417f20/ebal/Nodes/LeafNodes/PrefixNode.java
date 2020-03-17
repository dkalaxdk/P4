package sw417f20.ebal.Nodes.LeafNodes;

import sw417f20.ebal.AST;
import sw417f20.ebal.Nodes.Node;

public class PrefixNode extends Node {
    public String Value;

    public PrefixNode(AST.NodeType nodeType, String value) {
        super(nodeType);
        this.Value = value;
    }
}
