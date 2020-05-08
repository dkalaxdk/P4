package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SemanticsExpressionStrategy extends SemanticsCheckerStrategy{

    Node FirstOperand;
    Node Operator;
    Node SecondOperand;


    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        FirstOperand = node.FirstChild;
        Operator = node.FirstChild.Next;
        SecondOperand = node.FirstChild.Next.Next;

        FirstOperand.CheckSemantics();
        SecondOperand.CheckSemantics();

        if (FirstOperand.DataType != Symbol.SymbolType.PIN &&
            FirstOperand.DataType != Symbol.SymbolType.EVENT &&
            SecondOperand.DataType != Symbol.SymbolType.PIN &&
            SecondOperand.DataType != Symbol.SymbolType.EVENT){

            CheckOperator(node);
        }
        else {
            MakeError(node, "Pin and Event data types not allowed in expression");
        }
    }

    private void CheckOperator(Node node) throws  SemanticsException{
        switch (Operator.Type){
            case Modulo:
                if (FirstOperand.DataType == Symbol.SymbolType.INT && SecondOperand.DataType == Symbol.SymbolType.INT){
                    node.DataType = Symbol.SymbolType.INT;
                }
                else {
                    MakeError(node, "Type error: must be int");
                }
                break;
            case Plus:
            case Minus:
            case Times:
            case Divide:
                if (FirstOperand.DataType != Symbol.SymbolType.BOOL){
                    if (FirstOperand.DataType == Symbol.SymbolType.FLOAT || SecondOperand.DataType == Symbol.SymbolType.FLOAT){
                        node.DataType = Symbol.SymbolType.FLOAT;
                    }
                    else {
                        node.DataType = Symbol.SymbolType.INT;
                    }
                }
                else {
                    MakeError(node, "Type error: cannot be boolean");
                }
                break;

            case LessThan:
            case GreaterThan:
            case GreaterOrEqual:
            case LessOrEqual:

                if (FirstOperand.DataType != Symbol.SymbolType.BOOL && SecondOperand.DataType != Symbol.SymbolType.BOOL){
                    node.DataType = Symbol.SymbolType.BOOL;
                }
                else {
                    MakeError(node, "Type error: cannot be boolean");
                }
                break;

            case And:
            case Or:
                if (FirstOperand.DataType == Symbol.SymbolType.BOOL && SecondOperand.DataType == Symbol.SymbolType.BOOL){
                    node.DataType = Symbol.SymbolType.BOOL;
                }
                else {
                    MakeError(node, "Type error: must be boolean");
                }
                break;
            case Equals:
            case NotEqual:
                if ((FirstOperand.DataType != Symbol.SymbolType.BOOL
                    && SecondOperand.DataType != Symbol.SymbolType.BOOL) ||
                    FirstOperand.DataType == SecondOperand.DataType) {

                    node.DataType = Symbol.SymbolType.BOOL;
                }
                else {
                    MakeError(node, "Incompatible data types");
                }
                break;
        }
    }
}
