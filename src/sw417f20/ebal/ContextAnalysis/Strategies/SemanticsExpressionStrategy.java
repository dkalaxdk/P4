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

        // Check semantics for operands
        FirstOperand.CheckSemantics();
        SecondOperand.CheckSemantics();

        if (FirstOperand.DataType != Symbol.SymbolType.PIN &&
            FirstOperand.DataType != Symbol.SymbolType.EVENT &&
            SecondOperand.DataType != Symbol.SymbolType.PIN &&
            SecondOperand.DataType != Symbol.SymbolType.EVENT){

            // Check semantics for the operator
            CheckOperator(node);
        }
        else {
            MakeError(node, "Pin and Event data types not allowed in expression");
        }
    }

    private void CheckOperator(Node node) throws  SemanticsException{
        // Determine type of the operator and check the semantics
        switch (Operator.Type){
            case Modulo:
                // both operands must be integers
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
                    // If either operand is float, the result is a float
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
                // Neither operand can be a bool
                if (FirstOperand.DataType != Symbol.SymbolType.BOOL && SecondOperand.DataType != Symbol.SymbolType.BOOL){
                    node.DataType = Symbol.SymbolType.BOOL;
                }
                else {
                    MakeError(node, "Type error: cannot be boolean");
                }
                break;

            case And:
            case Or:
                // Both operands must be bools
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
