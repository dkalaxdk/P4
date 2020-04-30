package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.Exceptions.SemanticsException;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SemanticsExpressionStrategy extends SemanticsCheckerStrategy{

    @Override
    public void CheckSemantics(Node node) throws SemanticsException {
        node.FirstChild.CheckSemantics();
        node.FirstChild.Next.Next.CheckSemantics();
        if (node.FirstChild.DataType == node.FirstChild.Next.Next.DataType){
            CheckOperator(node);
        }
        else {
            MakeError(node, "Operators in expression must be of same data types");
        }
    }

    private void CheckOperator(Node node) throws  SemanticsException{
        switch (node.FirstChild.Next.Type){
            case Modulo:
                if (node.FirstChild.DataType == Symbol.SymbolType.INT){
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
                if (node.FirstChild.DataType != Symbol.SymbolType.BOOL){
                    node.DataType = node.FirstChild.DataType;
                }
                else {
                    MakeError(node, "Type error: cannot be boolean");
                }
                break;

            case LessThan:
            case GreaterThan:
            case GreaterOrEqual:
            case LessOrEqual:

                if (node.FirstChild.DataType != Symbol.SymbolType.BOOL){
                    node.DataType = Symbol.SymbolType.BOOL;
                }
                else {
                    MakeError(node, "Type error: cannot be boolean");
                }
                break;

            case And:
            case Or:
                if (node.FirstChild.DataType == Symbol.SymbolType.BOOL){
                    node.DataType = Symbol.SymbolType.BOOL;
                }
                else {
                    MakeError(node, "Type error: must be boolean");
                }
                break;
            case Equals:
            case NotEqual:
                node.DataType = Symbol.SymbolType.BOOL;
                break;
        }
    }
}
