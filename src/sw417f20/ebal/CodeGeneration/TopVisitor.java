package sw417f20.ebal.CodeGeneration;

import sw417f20.ebal.SyntaxAnalysis.Node;
import sw417f20.ebal.Visitors.Visitor;

public class TopVisitor extends Visitor {

    @Override
    public void Visit(Node node) {
        switch (node.Type) {
            case Prog:
                Visit(node.Next);
                break;
            case Master:
                emitMaster();
                break;
            case Slave:
                emitSlave();
                break;
            case Initiate:
                EmitInitiate();
                break;
            case Listener:
                EmitListener();
                break;
            case EventHandler:
                EmitEventHandler();
                break;
            case Block:
                EmitBlock();
                break;
            case PinDeclaration:
                EmitPinDeclaration();
                break;
            case Declaration:
                EmitDeclaration();
                break;
            case Assignment:
                EmitAssignment();
                break;
            case If:
                EmitIf();
                break;
            case Call:
                emitCall();
                break;
            case Func:
                emitFunc();
                break;
            case Expression:
                emitExpression();
                break;
            case Identifier:
                emitIdentifier();
                break;
            case Type:
                emitType();
                break;
            case Literal:
                emitLiteral();
                break;
            case Operator:
                emitOperator();
                break;
            case Prefix:
                emitPrefix();
                break;
            case Returns:
                emitReturns();
                break;
            case PinType:
                emitPinType();
                break;
            case IOType:
                emitIOType();
                break;
            case FilterType:
                emitFilterType();
                break;
        }
    }
    private void emitMaster() {

    }

    private void emitSlave() {
        Node node;
        Visit(node.Next);
    }

    private void EmitInitiate() {

    }

    private void EmitListener() {

    }

    private void EmitEventHandler() {

    }

    private void EmitBlock() {

    }

    private void EmitPinDeclaration() {

    }

    private void EmitDeclaration() {

    }

    private void EmitAssignment() {

    }

    private void EmitIf() {

    }

    private void emitCall() {

    }

    private void emitFunction() {

    }

    private void emitExpression() {

    }

    private void emitIdentifier() {

    }

    private void emitType() {

    }

    private void emitOperator() {

    }

    private void emitPrefix() {

    }

    private void emitFunc() {

    }

    private void emitReturns() {

    }

    private void emitPinType() {

    }

    private void emitIOType() {

    }

    private void emitLiteral() {

    }

    private void emitFilterType() {

    }
}
