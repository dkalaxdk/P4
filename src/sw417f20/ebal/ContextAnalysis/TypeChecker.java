package sw417f20.ebal.ContextAnalysis;

import net.bytebuddy.description.type.TypeDescription;
import sw417f20.ebal.ContextAnalysis.HashSymbolTable;
//import sw417f20.ebal.ContextAnalysis.PinSymbol;
import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.ContextAnalysis.SymbolTable;
import sw417f20.ebal.SyntaxAnalysis.AST;
import sw417f20.ebal.SyntaxAnalysis.Node;


public class TypeChecker {

    SymbolTable CurrentScope = new HashSymbolTable();

    private enum ErrorType{ NotDeclared, AlreadyDeclared, WrongType, WrongParameter, Default}

    private boolean InSlave = false;
    private boolean InInitiate = false;

    // Starts the process of scope and type checking the AST and decorating its nodes with datatypes
    public void Run(Node node){
        CheckProg(node);
    }

    // Checks the Prog node
    private void CheckProg(Node node){
        Node child = node.FirstChild;
        CheckMaster(child);

        // Sets flag to help check that the slave cannot call broadcast()
        InSlave = true;
        child = child.Next;
        while (child != null){
            CheckSlave(child);
            child = child.Next;
        }
    }

    // Checks the Master node
    private void CheckMaster(Node node){
        Node child = node.FirstChild;
        CheckInitiate(child);
        child = child.Next;
        while (child != null){
            CheckListener(child);
            child = child.Next;
        }
    }

    // Checks the Slave node
    private void CheckSlave(Node node){
        Node child = node.FirstChild;
        CheckInitiate(child);
        child = child.Next;
        while (child != null){
            CheckEventHandler(child);
            child = child.Next;
        }
    }

    // Checks the Initiate node
    private void CheckInitiate(Node node){
        InInitiate = true;
        CheckBlock(node.FirstChild);
        InInitiate = false;
    }


    // Scope and type checks the Listener node
    private void CheckListener(Node node) {
        Symbol symbol = CurrentScope.RetrieveSymbol(node.FirstChild.Value);
        if (symbol != null){
            if (symbol.Type == Symbol.SymbolType.PIN){
                CheckBlock(node.FirstChild.Next);
            }
            else {
                MakeError(symbol.Name, ErrorType.WrongType);
            }
        }
        else {
            MakeError(node.FirstChild.Value, ErrorType.NotDeclared);
        }
    }

    // Scope and type checks the EventHandler node
    private void CheckEventHandler(Node node) {
        Symbol symbol = CurrentScope.RetrieveSymbol(node.FirstChild.Value);
        if (symbol != null){
            if (symbol.Type == Symbol.SymbolType.EVENT){
                CheckBlock(node.FirstChild.Next);
            }
            else {
                MakeError(symbol.Name, ErrorType.WrongType);
            }
        }
        else {
            MakeError(node.FirstChild.Value, ErrorType.NotDeclared);
        }
    }

    // Checks the Block node
    private void CheckBlock(Node node){
        if (InInitiate){
            CheckInitiateBlock(node);
        }
        else if (InSlave){
            CheckEventHandlerBlock(node);
        }
        else {
            CheckListenerBlock(node);
        }
    }

    private void CheckInitiateBlock(Node node){
        Node child = node.FirstChild;
        while (child.Type != AST.NodeType.Empty) {
            switch (child.Type) {
                case Assignment:
                    CheckAssignment(child);
                    break;
                case PinDeclaration:
                    CheckPinDeclaration(child);
                    break;
                default:
                    MakeError("Only pin declarations and assignments allowed in Initiate");
            }
        }
    }

    private void CheckListenerBlock(Node node){
        Node child = node.FirstChild;
        while (child.Type != AST.NodeType.Empty){
            switch (child.Type){
                case Call: // ikke broadcast, createEvent eller createPin i slave, ikke createpin i listener
                    CheckCall(child);
                    break;
                case Assignment: //initiate, ikke asing andet end pin  i initiate, ikke assign pin og event i slave, ikke assigne pin i listener
                    CheckAssignment(child);
                    break;
                case If:
                    CheckIf(child);
                    break;
                case BoolDeclaration:
                    CheckBoolDeclaration(child);
                    break;
                case IntDeclaration:
                    CheckIntDeclaration(child);
                    break;
                case FloatDeclaration:
                    CheckFloatDeclaration(child);
                    break;
                case EventDeclaration: // ikke slave
                    CheckEventDeclaration(child);
                    break;
                default:
                    MakeError("No pin declarations allowed in Listener");
            }
            child = child.Next;
        }
    }

    private void CheckEventHandlerBlock(Node node){
        Node child = node.FirstChild;
        while (child.Type != AST.NodeType.Empty) {
            switch (child.Type) {
                case Call:
                    CheckCall(child);
                    break;
                case Assignment: //initiate, ikke asing andet end pin  i initiate, ikke assign pin og event i slave, ikke assigne pin i listener
                    CheckAssignment(child);
                    break;
                case If:
                    CheckIf(child);
                    break;
                case BoolDeclaration:
                    CheckBoolDeclaration(child);
                    break;
                case IntDeclaration:
                    CheckIntDeclaration(child);
                    break;
                case FloatDeclaration:
                    CheckFloatDeclaration(child);
                    break;
                default:
                    MakeError("No pin or event declarations allowed in EventHandler");
            }
        }
    }

    // Scope checks the PinDeclaration node and checks that a pin cannot be both input and PWM
    private void CheckPinDeclaration(Node node) {
        if (!CurrentScope.DeclaredLocally(node.FirstChild.Value)){
            PinSymbol.PinType pinType = GetPinType(node.FirstChild.Next);
            PinSymbol.IOType ioType = GetIOType(node.FirstChild.Next.Next);
            if (ioType != PinSymbol.IOType.Input || pinType != PinSymbol.PinType.PWM) {
                CurrentScope.EnterSymbol(node.Value, Symbol.SymbolType.PIN, pinType, ioType,Integer.parseInt(node.FirstChild.Next.Next.Next.Value));
            }
            else{
                MakeError(node.Value, ErrorType.WrongParameter);
            }
        }
        else {
            MakeError(node.FirstChild.Value, ErrorType.NotDeclared);
        }
    }

    // Returns the IO type of a PinDeclaration node
    private PinSymbol.IOType GetIOType(Node node) {
        return node.Type == AST.NodeType.Input ? PinSymbol.IOType.Input : PinSymbol.IOType.Output;
    }

    // Returns the pin type of a PinDeclaration node
    private PinSymbol.PinType GetPinType(Node node) {
        switch (node.Type){
            case Digital:
                return PinSymbol.PinType.Digital;
            case Analog:
                return  PinSymbol.PinType.Analog;
            case PWM:
                return PinSymbol.PinType.PWM;
        }
        return null;
    }

    // Scope and type checks the FloatDeclaration node
    private void CheckFloatDeclaration(Node node) {
        if (!CurrentScope.DeclaredLocally(node.FirstChild.Value)){
            Node expression = node.FirstChild.Next;
            if (expression != null) {
                CheckExpression(expression);
                if (expression.DataType != Symbol.SymbolType.FLOAT){
                    MakeError(node.FirstChild.Value, ErrorType.WrongType);
                }
            }
            CurrentScope.EnterSymbol(node.Value, Symbol.SymbolType.FLOAT);
        }
        else {
            MakeError(node.FirstChild.Value, ErrorType.NotDeclared);
        }
    }

    // Scope and type checks the IntDeclaration node
    private void CheckIntDeclaration(Node node) {
        if (!CurrentScope.DeclaredLocally(node.FirstChild.Value)){
            Node expression = node.FirstChild.Next;
            if (expression != null) {
                CheckExpression(expression);
                if (expression.DataType != Symbol.SymbolType.INT){
                    MakeError(node.FirstChild.Value, ErrorType.WrongType);
                }
            }
            CurrentScope.EnterSymbol(node.Value, Symbol.SymbolType.INT);
        }
        else {
            MakeError(node.FirstChild.Value, ErrorType.NotDeclared);
        }
    }

    // Scope and type checks the BoolDeclaration node
    private void CheckBoolDeclaration(Node node) {
        if (!CurrentScope.DeclaredLocally(node.FirstChild.Value)){
            Node expression = node.FirstChild.Next;
            if (expression != null) {
                CheckExpression(expression);
                if (expression.DataType != Symbol.SymbolType.BOOL){
                    MakeError(node.FirstChild.Value, ErrorType.WrongType);
                }
            }
            CurrentScope.EnterSymbol(node.Value, Symbol.SymbolType.BOOL);
        }
        else {
            MakeError(node.FirstChild.Value, ErrorType.NotDeclared);
        }
    }

    //TODO: vend tilbage hertil når vi har hørt fra Bent

    // Scope and type checks the EventDeclaration node
    private void CheckEventDeclaration(Node node) {
        if (!CurrentScope.DeclaredLocally(node.FirstChild.Value)){
            Node expression = node.FirstChild.Next;
            if (expression != null) {
                CheckExpression(expression);
                if (expression.DataType != Symbol.SymbolType.EVENT){
                    MakeError(node.FirstChild.Value, ErrorType.WrongType);
                }
            }
            CurrentScope.EnterSymbol(node.Value, Symbol.SymbolType.EVENT);
        }
        else {
            MakeError(node.FirstChild.Value, ErrorType.NotDeclared);
        }
    }

    //TODO: Lav evt type convertering

    // Scope and type checks the Assignment node
    private void CheckAssignment(Node node) {
        Symbol identifier = CurrentScope.RetrieveSymbol(node.FirstChild.Value);
        if(identifier != null) {
            CheckExpression(node.FirstChild.Next);
            if (identifier.Type != node.FirstChild.Next.DataType) {
                MakeError(node.FirstChild.Value, ErrorType.WrongType);
            }
        }
        else {
            MakeError(node.FirstChild.Value, ErrorType.NotDeclared);
        }
    }

    // Type checks the If node
    private void CheckIf(Node node) {
        CheckExpression(node.FirstChild);
        if(node.FirstChild.DataType == Symbol.SymbolType.BOOL){
            CheckBlock(node.FirstChild.Next);
            Node elseStmt = node.FirstChild.Next.Next;
            if (elseStmt != null){
                if(elseStmt.Type == AST.NodeType.Block){
                    CheckBlock(elseStmt);
                }
                else {
                    CheckIf(elseStmt);
                }
            }
        }
        else{
            MakeError("If statement", ErrorType.WrongType);
        }
    }

    // TODO: fix efter svar fra Bent
    // Type checks the Call node depending on the function called
    private void CheckCall(Node node) {
        CheckParameters(node);
        switch (node.FirstChild.Type){
            case Broadcast:
                CheckBroadcastCall(node);
                break;

            case FilterNoise:
                CheckFilterNoiseCall(node);
                break;

            case GetValue:
                CheckGetValueCall(node);
                break;

            case Write:
                CheckWriteCall(node);
                break;

            case CreateEvent:
                CheckCreateEventCall(node);
                break;
        }
        node.DataType = node.FirstChild.DataType;
    }

    //TODO: kan ikke checke for null/empty for at finde enden af parameterlisten
    // Takes a call node and check all its parameters
    private void CheckParameters(Node node){
        Node child = node.FirstChild.Next;
        while (child != null){
            switch (child.Type){
                case Constant:
                    CheckConstant(child);
                    break;
                case Flip:
                    CheckFlip(child);
                    break;
                case Range:
                    CheckRange(child);
                    break;
                default:
                    CheckExpression(child);
            }
            child = child.Next;
        }
    }

    private void CheckBroadcastCall(Node node){
        if (!InSlave) {
            if (node.FirstChild.Next.DataType == Symbol.SymbolType.EVENT) {
            }
            MakeError(node.FirstChild.Value, ErrorType.WrongType);
        }
        else {
            MakeError("Broadcast function cannot be called by slave");
        }
    }

    // digital : flip, constant
    // analog : range
    private void CheckFilterNoiseCall(Node node){
        if (node.FirstChild.Next.DataType == Symbol.SymbolType.PIN &&
                node.FirstChild.Next.Next.DataType == Symbol.SymbolType.FILTERTYPE){
        }
        MakeError(node.FirstChild.Value, ErrorType.WrongType);
    }

    private void CheckGetValueCall(Node node){
        if (node.FirstChild.Next.DataType == Symbol.SymbolType.PIN ||
                node.FirstChild.Next.DataType == Symbol.SymbolType.EVENT){
        }
        MakeError(node.FirstChild.Value, ErrorType.WrongType);
    }

    private void CheckWriteCall(Node node){
        if (node.FirstChild.Next.DataType == Symbol.SymbolType.PIN &&
                node.FirstChild.Next.Next.DataType == Symbol.SymbolType.INT){
        }
        MakeError(node.FirstChild.Value, ErrorType.WrongType);
    }

    private void CheckCreateEventCall(Node node){
        if (node.FirstChild.Next.DataType == Symbol.SymbolType.INT){
        }
        MakeError(node.FirstChild.Value, ErrorType.WrongType);
    }



    private void CheckExpression(Node node) {
//TODO: tjek at det faktisk er en expression node
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

    private void CheckIntLiteral(Node node) {
        node.DataType = Symbol.SymbolType.INT;
    }

    private void CheckFloatLiteral(Node node) {
        node.DataType = Symbol.SymbolType.FLOAT;
    }

    private void CheckBoolLiteral(Node node) {
        node.DataType = Symbol.SymbolType.BOOL;
    }

    private void CheckDigital(Node node){
    }

    private void CheckAnalog(Node node){
    }

    private void CheckPWM(Node node){
    }

    private void CheckInput(Node node){
    }

    private void CheckOutput(Node node){
    }

    private void CheckConstant(Node node){
    }

    private void CheckFlip(Node node){
    }

    private void CheckRange(Node node){
    }

    private void CheckBroadcast(Node node){
    }

    private void CheckWrite(Node node){
    }

    private void CheckGetValue(Node node){
    }

    private void CheckFilterNoise(Node node){
    }

    private void CheckCreateEvent(Node node){
    }

    private void CheckLessThan(Node node){
    }

    private void CheckGreaterThan(Node node){
    }

    private void CheckNotEqual(Node node){
    }

    private void CheckEquals(Node node){
    }

    private void CheckGreaterOrEqual(Node node){
    }

    private void CheckLessOrEqual(Node node){
    }

    private void CheckAnd(Node node){
    }

    private void CheckOr(Node node){
    }

    private void CheckPlus(Node node){
    }

    private void CheckMinus(Node node){
    }

    private void CheckTimes(Node node){
    }

    private void CheckDivide(Node node){
    }

    private void CheckModulo(Node node){
    }

    private void CheckPrefixNot(Node node){
    }

    private void CheckPrefixMinus(Node node){
    }

    private void CheckError(Node node){
    }

    private void CheckEmpty(Node node){
    }

//    private void CheckFunction(Node node) {
//        switch (node.Value){
//            case "write":
//            case "broadcast":
//                node.DataType = Symbol.SymbolType.VOID;
//                break;
//            case "filterNoise":
//            case "getValue":
//                node.DataType = Symbol.SymbolType.INT;
//                break;
//            case "createEvent":
//                node.DataType = Symbol.SymbolType.EVENT;
//                break;
//        }
//    }



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
            case WrongParameter:
                message = name + ": wrong parameter";
            case Default:
                message = name + ": error";
        }

        System.err.println(message);
        System.exit(0);
    }

    private void MakeError(String message){
        System.err.println(message);
        System.exit(0);
    }
}
