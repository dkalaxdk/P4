package sw417f20.ebal.Nodes.LeafNodes;

import sw417f20.ebal.AST;
import sw417f20.ebal.Nodes.Node;

public class OperatorNode extends Node {
    public String Value;

    public OperatorNode(AST.NodeType nodeType, String value) {
        super(nodeType);
        this.Value = value;
//        switch (value) {
//            case "+":
//                this.Value = "Add";
//                break;
//            case "-":
//                this.Value = "Subtract";
//                break;
//            case "*":
//                this.Value = "Multiply";
//                break;
//            case "/":
//                this.Value = "Divide";
//                break;
//            case "%":
//                this.Value = "Modulo";
//                break;
//            case "==":
//                this.Value = "Is equal";
//                break;
//        }
    }
}
