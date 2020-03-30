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

    private enum ErrorType{ NotDeclared, AlreadyDeclared, WrongType, Default}

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
            case Identifier:
                CheckIdentifier(node);
                break;
            case Literal:
                CheckLiteral(node);
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
                if(node.FirstChild.DataType != expression.DataType){
                    MakeError(node.FirstChild.Value, ErrorType.WrongType);
                }
            }
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

    private void TypeCheckAssignment(Node node) {
        if(CurrentScope.RetrieveSymbol(node.FirstChild.Value).Type != node.FirstChild.Next.DataType){
            MakeError(node.FirstChild.Value, ErrorType.WrongType);
        }
    }

    private void CheckIf(Node node) {
        Visit(node.FirstChild);
        if(node.FirstChild.DataType == Symbol.SymbolType.BOOL){
            Visit(node.FirstChild.Next);
            if (node.FirstChild.Next.Next != null){
                Visit(node.FirstChild.Next.Next);
            }
        }
    }

    private void CheckCall(Node node) {
        VisitChildren(node);
        switch (node.FirstChild.Value){
            case "broadcast":
                if (node.FirstChild.Next.DataType == Symbol.SymbolType.EVENT){
                    break;
                }
                MakeError(node.FirstChild.Value, ErrorType.WrongType);
                break;

            case "filterNoise":
                if (node.FirstChild.Next.DataType == Symbol.SymbolType.PIN &&
                    node.FirstChild.Next.Next.DataType == Symbol.SymbolType.FILTERTYPE){

                    break;
                }
                MakeError(node.FirstChild.Value, ErrorType.WrongType);
                break;

            case "getValue":
                if (node.FirstChild.Next.DataType == Symbol.SymbolType.PIN ||
                    node.FirstChild.Next.DataType == Symbol.SymbolType.EVENT){

                    break;
                }
                MakeError(node.FirstChild.Value, ErrorType.WrongType);
                break;

            case "write":
                if (node.FirstChild.Next.DataType == Symbol.SymbolType.PIN &&
                    node.FirstChild.Next.Next.DataType == Symbol.SymbolType.INT){

                    break;
                }
                MakeError(node.FirstChild.Value, ErrorType.WrongType);
                break;

            case "createEvent":
                if (node.FirstChild.Next.DataType == Symbol.SymbolType.INT){
                    break;
                }
                MakeError(node.FirstChild.Value, ErrorType.WrongType);
                break;

            case "createPin":
                if (node.FirstChild.Next.DataType == Symbol.SymbolType.PINTYPE &&
                    node.FirstChild.Next.Next.DataType == Symbol.SymbolType.IO &&
                    node.FirstChild.Next.Next.Next.DataType == Symbol.SymbolType.INT){

                    break;
                }
                MakeError(node.FirstChild.Value, ErrorType.WrongType);
                break;

            default:
                MakeError(node.FirstChild.Value, ErrorType.Default);
        }
        node.DataType = node.FirstChild.DataType;
    }

    private void CheckFunction(Node node) {
        switch (node.Value){
            case "write":
            case "broadcast":
                node.DataType = Symbol.SymbolType.VOID;
                break;
            case "filterNoise":
            case "getValue":
                node.DataType = Symbol.SymbolType.INT;
                break;
            case "createEvent":
                node.DataType = Symbol.SymbolType.EVENT;
                break;
            case "createPin":
                node.DataType = Symbol.SymbolType.PIN;
                break;
        }
    }

    private void CheckExpression(Node node) {
//TODO: lav den her
    }

    private void CheckIdentifier(Node node) {
        Symbol sym = CurrentScope.RetrieveSymbol(node.Value);
        if (sym != null){
            node.DataType = sym.Type;
        }
        else{
            MakeError(node.Value, ErrorType.NotDeclared);
        }
    }

    private void CheckLiteral(Node node) {

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
            case Default:
                message = name + ": error";
        }

        System.err.println(message);
        System.exit(0);
    }
}
