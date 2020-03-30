package sw417f20.ebal.Visitors;

import sw417f20.ebal.ContextAnalysis.HashSymbolTable;
import sw417f20.ebal.ContextAnalysis.PinSymbol;
import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.ContextAnalysis.SymbolTable;
import sw417f20.ebal.SyntaxAnalysis.Node;

import javax.naming.Name;
import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;

public class SemanticVisitor extends Visitor {

    SymbolTable CurrentScope = new HashSymbolTable();

    private enum ErrorType{ NotDeclared, AlreadyDeclared, WrongType}

    @Override
    public void Visit(Node node) {
        switch(node.Type) {
            case Prog:
            case Master:
            case Slave:
            case Initiate:
            case Block:
                VisitChildren(node);
                break;
            case Listener:
                CheckListener(node);
                break;
            case EventHandler:
                CheckEventHandler(node);
                break;
            case PinDeclaration:
                CheckPinDeclaration(node);
                break;
            case Declaration:
                CheckDeclaration(node);
                break;
            case Assignment:
                CheckAssignment(node);
                break;
            case If:
                CheckIf(node);
                break;
            case Call:
                CheckCall(node);
                break;
            case Function:
                CheckFunction(node);
                break;
            case Expression:
                CheckExpression(node);
                break;
            case Error:
            case Empty:
                break;
            default:
                System.err.println("Node type not found");
        }
    }

    private void CheckListener(Node node) {

        Symbol symbol = CurrentScope.RetrieveSymbol(node.FirstChild.Value);
        if (symbol != null){
            if (symbol.Type == Symbol.SymbolType.PIN){
                Visit(node.FirstChild.Next);
            }
            else {
                MakeError(symbol.Name, ErrorType.WrongType);
            }
        }
        else {
            MakeError(node.FirstChild.Value, ErrorType.NotDeclared);
        }
    }

    private void CheckEventHandler(Node node) {
        Symbol symbol = CurrentScope.RetrieveSymbol(node.FirstChild.Value);
        if (symbol != null){
            if (symbol.Type == Symbol.SymbolType.EVENT){
                Visit(node.FirstChild.Next);
            }
            else {
                MakeError(symbol.Name, ErrorType.WrongType);
            }
        }
        else {
            MakeError(node.FirstChild.Value, ErrorType.NotDeclared);
        }
    }

    private void CheckPinDeclaration(Node node) {
        if (!CurrentScope.DeclaredLocally(node.FirstChild.Value)){
            CurrentScope.EnterSymbol(node.Value, Symbol.SymbolType.PIN, GetIOType(node.FirstChild.Next.Next));
        }
        else {
            MakeError(node.FirstChild.Value, ErrorType.NotDeclared);
        }
    }

    private PinSymbol.IOType GetIOType(Node node) {
        if (node.Value.equals("input")){
            return PinSymbol.IOType.Input;
        }
        else{
            return PinSymbol.IOType.Output;
        }
    }


    private void CheckDeclaration(Node node) {
        if (!CurrentScope.DeclaredLocally(node.FirstChild.Value)){
            Node expression = node.FirstChild.Next.Next;
            if (expression != null){
                Visit(expression);
            }
            //TODO: check at identifier og expression har samme type
            CurrentScope.EnterSymbol(node.Value, GetType(node));
        }
        else {
            MakeError(node.FirstChild.Value, ErrorType.NotDeclared);
        }
    }

    private Symbol.SymbolType GetType(Node node) {
        switch (node.FirstChild.Value){
            case "int":
                return Symbol.SymbolType.INT;
            case "float":
                return Symbol.SymbolType.FLOAT;
            case "bool":
                return Symbol.SymbolType.BOOL;
            case "event":
                return Symbol.SymbolType.EVENT;
            default:
                return null;
        }
    }

    private void CheckAssignment(Node node) {
        ScopeCheckAssignment(node);
        VisitChildren(node.FirstChild.Next);
        TypeCheckAssignment(node);
    }

    private void ScopeCheckAssignment(Node node) {
        if(CurrentScope.RetrieveSymbol(node.FirstChild.Value) == null){
            MakeError(node.FirstChild.Value, ErrorType.NotDeclared);
        }
    }

    //TODO: husk at afkommentere når ast er dekoreret
    private void TypeCheckAssignment(Node node) {
//        if(CurrentScope.RetrieveSymbol(node.FirstChild.Value).Type != node.FirstChild.Next.DataType){
//            MakeError(node.FirstChild.Value, ErrorType.WrongType);
//        }
    }

    private void CheckIf(Node node) {
        Visit(node.FirstChild);
//        if(node.FirstChild.DataType == Symbol.SymbolType.BOOL){
//            Visit(node.FirstChild.Next);
//            if (node.FirstChild.Next.Next != null){
//                Visit(node.FirstChild.Next.Next);
//            }
//        }
    }

    private void CheckCall(Node node) {

    }

    private void CheckFunction(Node node) {
    }

    private void CheckExpression(Node node) {
    }

    private void MakeError(String name, ErrorType errorType){
        String message = "";

        switch (errorType){
            case AlreadyDeclared:
                message = name + ": has already been declared.";
                break;
            case NotDeclared:
                message = name + ": has not been declared.";
                break;
            case WrongType:
                message = name + ": wrong type";
                break;
        }

        System.err.println(message);
        System.exit(0);
    }
}
