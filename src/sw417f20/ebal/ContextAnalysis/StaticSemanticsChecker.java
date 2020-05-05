//package sw417f20.ebal.ContextAnalysis;
//
////import sw417f20.ebal.ContextAnalysis.PinSymbol;
//import sw417f20.ebal.Exceptions.SemanticsException;
//import sw417f20.ebal.SyntaxAnalysis.Node;
//import java.util.ArrayList;
//
///**
// * Class responsible for checking the static semantics of the EBAL code
// */
//public class StaticSemanticsChecker {
//
//    SymbolTable CurrentScope;
//
//    private enum ErrorType{ NotDeclared, AlreadyDeclared, WrongType, Default}
//
//    private boolean InEventHandler;
//    private boolean InInitiate;
//    private ArrayList<Integer> UsedPinNumbers;
//
//    public StaticSemanticsChecker(){
//        CurrentScope = new HashSymbolTable();
//        InEventHandler = false;
//        InInitiate = false;
//        UsedPinNumbers = new ArrayList<>();
//    }
//
////region Done
//    // Starts the process of scope and type checking the AST and decorating its nodes with datatypes
//    public SymbolTable Run(Node node) throws SemanticsException{
//        CheckProg(node);
//        return CurrentScope.GetGlobalScope();
//    }
//
//    // Checks the Prog node
//    private void CheckProg(Node node) throws SemanticsException{
//        Node child = node.FirstChild;
//        CheckMaster(child);
//
//        // Sets flag to help check that the slave cannot call broadcast()
//        InEventHandler = true;
//        child = child.Next;
//        while (child.Type != Node.NodeType.Empty){
//            CheckSlave(child);
//            child = child.Next;
//        }
//    }
//
//    // Checks the Master node
//    private void CheckMaster(Node node) throws SemanticsException{
//        CurrentScope = CurrentScope.OpenScope();
//        Node child = node.FirstChild;
//        CheckInitiate(child);
//        child = child.Next;
//        while (child.Type != Node.NodeType.Empty){
//            CheckListener(child);
//            child = child.Next;
//        }
//        CurrentScope = CurrentScope.CloseScope();
//    }
//
//    // Checks the Slave node
//    private void CheckSlave(Node node) throws SemanticsException{
//        if (!CurrentScope.DeclaredLocally(node.FirstChild.Value)) {
//            CurrentScope.EnterSymbol(node.FirstChild.Value, Symbol.SymbolType.SLAVE);
//
//            CurrentScope = CurrentScope.OpenScope();
//            Node child = node.FirstChild.Next;
//            CheckInitiate(child);
//            child = child.Next;
//            while (child.Type != Node.NodeType.Empty) {
//                CheckEventHandler(child);
//                child = child.Next;
//            }
//            CurrentScope = CurrentScope.CloseScope();
//        }
//        else {
//            MakeError(node, node.FirstChild.Value, ErrorType.AlreadyDeclared);
//        }
//    }
//
//    // Checks the Initiate node
//    private void CheckInitiate(Node node) throws SemanticsException{
//        InInitiate = true;
//        CheckBlock(node.FirstChild);
//        InInitiate = false;
//    }
//
//    // Scope and type checks the Listener node
//    private void CheckListener(Node node) throws SemanticsException{
//        Symbol symbol = CurrentScope.RetrieveSymbol(node.FirstChild.Value);
//        if (symbol != null){
//            if (symbol.DataType == Symbol.SymbolType.PIN){
//                node.FirstChild.DefinitionReference = symbol.ReferenceNode;
//                CheckBlock(node.FirstChild.Next);
//            }
//            else {
//                MakeError(node, symbol.Name, ErrorType.WrongType);
//            }
//        }
//        else {
//            MakeError(node, node.FirstChild.Value, ErrorType.NotDeclared);
//        }
//    }
//
//    // Scope and type checks the EventHandler node
//    private void CheckEventHandler(Node node) throws SemanticsException{
//        Symbol symbol = CurrentScope.RetrieveSymbol(node.FirstChild.Value);
//        if (symbol != null){
//            if (symbol.DataType == Symbol.SymbolType.EVENT){
//                CheckBlock(node.FirstChild.Next);
//            }
//            else {
//                MakeError(node, symbol.Name, ErrorType.WrongType);
//            }
//        }
//        else {
//            MakeError(node, node.FirstChild.Value, ErrorType.NotDeclared);
//        }
//    }
//
//    // Determines what kind of block the node is and then calls the appropriate method for checking it
//    private void CheckBlock(Node node) throws SemanticsException{
//
//        if (InInitiate){
//            CheckInitiateBlock(node);
//        }
//        else if (InEventHandler){
//            CurrentScope = CurrentScope.OpenScope();
//            CheckEventHandlerBlock(node);
//            CurrentScope = CurrentScope.CloseScope();
//        }
//        else {
//            CurrentScope = CurrentScope.OpenScope();
//            CheckListenerBlock(node);
//            CurrentScope = CurrentScope.CloseScope();
//        }
//    }
//
//    // Checks an Initiate block
//    private void CheckInitiateBlock(Node node) throws SemanticsException{
//        Node child = node.FirstChild;
//        while (child.Type != Node.NodeType.Empty) {
//            if (child.Type == Node.NodeType.PinDeclaration) {
//                CheckPinDeclaration(child);
//            }
//            else {
//                MakeError(child, "Only pin declarations allowed in Initiate");
//            }
//            child = child.Next;
//        }
//        UsedPinNumbers.clear();
//    }
//
//    // checks a Listener block
//    private void CheckListenerBlock(Node node) throws SemanticsException{
//        Node child = node.FirstChild;
//        while (child.Type != Node.NodeType.Empty){
//            switch (child.Type){
//                case Call:
//                    CheckCall(child);
//                    break;
//                case Assignment:
//                    CheckAssignment(child);
//                    break;
//                case If:
//                    CheckIf(child);
//                    break;
//                case BoolDeclaration:
//                    CheckBoolDeclaration(child);
//                    break;
//                case IntDeclaration:
//                    CheckIntDeclaration(child);
//                    break;
//                case FloatDeclaration:
//                    CheckFloatDeclaration(child);
//                    break;
//                case EventDeclaration:
//                    CheckEventDeclaration(child);
//                    break;
//                default:
//                    MakeError(child, "No pin declarations allowed in Listener");
//            }
//            child = child.Next;
//        }
//    }
//
//    // Check an EventHandler block
//    private void CheckEventHandlerBlock(Node node) throws SemanticsException{
//        Node child = node.FirstChild;
//        while (child.Type != Node.NodeType.Empty) {
//            switch (child.Type) {
//                case Call:
//                    CheckCall(child);
//                    break;
//                case Assignment:
//                    CheckAssignment(child);
//                    break;
//                case If:
//                    CheckIf(child);
//                    break;
//                case BoolDeclaration:
//                    CheckBoolDeclaration(child);
//                    break;
//                case IntDeclaration:
//                    CheckIntDeclaration(child);
//                    break;
//                case FloatDeclaration:
//                    CheckFloatDeclaration(child);
//                    break;
//                default:
//                    MakeError(child, "No pin or event declarations allowed in EventHandler");
//            }
//            child = child.Next;
//        }
//    }
//
//    // Scope checks the PinDeclaration node
//    private void CheckPinDeclaration(Node node) throws SemanticsException{
//        if (!CurrentScope.DeclaredLocally(node.FirstChild.Value)){
//            Node expression = node.FirstChild.Next;
//            if (expression.Type == Node.NodeType.Call){
//                CheckCall(expression);
//                if (expression.DataType == Symbol.SymbolType.PIN){
//                    CurrentScope.EnterSymbol(node.FirstChild.Value, Symbol.SymbolType.PIN, node);
//                }
//                else {
//                    MakeError(node, "Expression", ErrorType.WrongType);
//                }
//            }
//            else {
//                MakeError(node, "Illegal declaration of pin.");
//            }
//        }
//        else {
//            MakeError(node, node.FirstChild.Value, ErrorType.AlreadyDeclared);
//        }
//    }
//
//    // Scope and type checks the EventDeclaration node
//    private void CheckEventDeclaration(Node node) throws SemanticsException{
//        if (!CurrentScope.DeclaredLocally(node.FirstChild.Value)){
//            Node expression = node.FirstChild.Next;
//            if (expression.Type == Node.NodeType.Call) {
//                CheckCall(expression);
//                if (expression.DataType == Symbol.SymbolType.EVENT){
//                    CurrentScope.EnterGlobalSymbol(node.FirstChild.Value, Symbol.SymbolType.EVENT, expression.FirstChild.Next.DataType);
//                }
//                else {
//                    MakeError(node, "Expression", ErrorType.WrongType);
//                }
//            }
//            else {
//                MakeError(node, "Illegal declaration of event.");
//            }
//        }
//        else {
//            MakeError(node, node.FirstChild.Value, ErrorType.AlreadyDeclared);
//        }
//    }
//
//    // Scope and type checks the FloatDeclaration node
//    private void CheckFloatDeclaration(Node node) throws SemanticsException{
//        if (!CurrentScope.DeclaredLocally(node.FirstChild.Value)){
//            Node expression = node.FirstChild.Next;
//            if (expression.Type != Node.NodeType.Empty) {
//                CheckExpression(expression);
//                if (expression.DataType != Symbol.SymbolType.FLOAT){
//                    MakeError(node, node.FirstChild.Value, ErrorType.WrongType);
//                }
//            }
//            CurrentScope.EnterSymbol(node.FirstChild.Value, Symbol.SymbolType.FLOAT);
//        }
//        else {
//            MakeError(node, node.FirstChild.Value, ErrorType.AlreadyDeclared);
//        }
//    }
//
//    // Scope and type checks the IntDeclaration node
//    private void CheckIntDeclaration(Node node) throws SemanticsException{
//        if (!CurrentScope.DeclaredLocally(node.FirstChild.Value)){
//            Node expression = node.FirstChild.Next;
//            if (expression.Type != Node.NodeType.Empty) {
//                CheckExpression(expression);
//                if (expression.DataType != Symbol.SymbolType.INT){
//                    MakeError(node, node.FirstChild.Value, ErrorType.WrongType);
//                }
//            }
//            CurrentScope.EnterSymbol(node.FirstChild.Value, Symbol.SymbolType.INT);
//        }
//        else {
//            MakeError(node, node.FirstChild.Value, ErrorType.AlreadyDeclared);
//        }
//    }
//
//    // Scope and type checks the BoolDeclaration node
//    private void CheckBoolDeclaration(Node node) throws SemanticsException{
//        if (!CurrentScope.DeclaredLocally(node.FirstChild.Value)){
//            Node expression = node.FirstChild.Next;
//            if (expression.Type != Node.NodeType.Empty) {
//                CheckExpression(expression);
//                if (expression.DataType != Symbol.SymbolType.BOOL){
//                    MakeError(node, node.FirstChild.Value, ErrorType.WrongType);
//                }
//            }
//            CurrentScope.EnterSymbol(node.FirstChild.Value, Symbol.SymbolType.BOOL);
//        }
//        else {
//            MakeError(node, node.FirstChild.Value, ErrorType.AlreadyDeclared);
//        }
//    }
//
//    // Scope and type checks the Assignment node
//    private void CheckAssignment(Node node) throws SemanticsException{
//        Symbol identifier = CurrentScope.RetrieveSymbol(node.FirstChild.Value);
//        Node expression = node.FirstChild.Next;
//        if(identifier != null) {
//            CheckExpression(expression);
//            if (identifier.DataType != expression.DataType) {
//                MakeError(node, node.FirstChild.Value, ErrorType.WrongType);
//            }
//            else if (expression.DataType == Symbol.SymbolType.PIN){
//                MakeError(node, "expression cannot be of type Pin");
//            }
//            else if (expression.DataType == Symbol.SymbolType.EVENT){
//                MakeError(node, "expression cannot be of type Event");
//            }
//        }
//        else {
//            MakeError(node, node.FirstChild.Value, ErrorType.NotDeclared);
//        }
//    }
//
//    // Type checks the If node
//    private void CheckIf(Node node) throws SemanticsException{
//        CheckExpression(node.FirstChild);
//        if(node.FirstChild.DataType == Symbol.SymbolType.BOOL){
//            CheckBlock(node.FirstChild.Next);
//            Node elseStmt = node.FirstChild.Next.Next;
//            if (elseStmt.Type != Node.NodeType.Empty){
//                if(elseStmt.Type == Node.NodeType.Block){
//                    CheckBlock(elseStmt);
//                }
//                else {
//                    CheckIf(elseStmt);
//                }
//            }
//        }
//        else{
//            MakeError(node, "If statement requires boolean expression");
//        }
//    }
//
//    // Type checks the Call node depending on the function called
//    private void CheckCall(Node node) throws SemanticsException{
//        Node.NodeType functionType = node.FirstChild.Type;
//
//        if (InInitiate){
//            if (functionType == Node.NodeType.CreatePin){
//                CheckCreatePinCall(node);
//            }
//            else{
//                MakeError(node, "Illegal declaration of pin");
//            }
//        }
//        else if (InEventHandler){
//            switch (functionType){
//                case Write:
//                    CheckWriteCall(node);
//                    break;
//                case GetValue:
//                    CheckGetValueCall(node);
//                    break;
//                default:
//                    MakeError(node, "Illegal function call");
//            }
//        }
//        else {
//            switch (functionType){
//                case Broadcast:
//                    CheckBroadcastCall(node);
//                    break;
//                case GetValue:
//                    CheckGetValueCall(node);
//                    break;
//                case FilterNoise:
//                    CheckFilterNoiseCall(node);
//                    break;
//                case CreateEvent:
//                    CheckCreateEventCall(node);
//                    break;
//                default:
//                    MakeError(node, "Illegal function call");
//            }
//        }
//    }
//
//    // Check the parameters of the Broadcast() function
//    private void CheckBroadcastCall(Node node) throws SemanticsException{
//        Symbol parameter = CurrentScope.RetrieveSymbol(node.FirstChild.Next.Value);
//        if (parameter != null) {
//            if (parameter.DataType == Symbol.SymbolType.EVENT) {
//                node.DataType = Symbol.SymbolType.VOID;
//            } else {
//                MakeError(node, node.FirstChild.Next.DataType.toString(), ErrorType.WrongType);
//            }
//        }
//        else {
//            MakeError(node, node.FirstChild.Next.Value, ErrorType.NotDeclared);
//        }
//    }
//
//    // Checks the parameters of the FilterNoise() function
//    private void CheckFilterNoiseCall(Node node) throws SemanticsException{
//        Symbol pinParameter = CurrentScope.RetrieveSymbol(node.FirstChild.Next.Value);
//        if (pinParameter != null){
//            if (pinParameter.DataType == Symbol.SymbolType.PIN){
//                CheckPinAndFilterCombination(pinParameter, node);
//                node.DataType = Symbol.SymbolType.INT;
//            }
//            else {
//                MakeError(node, node.FirstChild.Next.Value, ErrorType.WrongType);
//            }
//        }
//        else {
//            MakeError(node, node.FirstChild.Next.Value, ErrorType.NotDeclared);
//        }
//
//    }
//
//    // Checks that the combination of pin type and filter type for the FilterNoise() function is legal
//    // Allowed combinations:
//    //   Digital: Flip, Constant
//    //   Analog: Range
//    private void CheckPinAndFilterCombination(Symbol symbol, Node node) throws SemanticsException{
//        Node.NodeType pinType = symbol.ReferenceNode.Type;
//        Node.NodeType filterType = node.FirstChild.Next.Next.Type;
//
//        if (pinType == Node.NodeType.Digital && filterType == Node.NodeType.Range){
//            MakeError(node, "Noise from digital pin cannot be filtered as ranged");
//        }
//        else if (pinType == Node.NodeType.Analog && filterType != Node.NodeType.Range){
//            MakeError(node, "Noise from analog pin can only be filtered as ranged");
//        }
//    }
//
//    // Checks the parameters of the GetValue() function
//    private void CheckGetValueCall(Node node) throws SemanticsException{
//        Symbol parameter = CurrentScope.RetrieveSymbol(node.FirstChild.Next.Value);
//        if (parameter != null) {
//            if (parameter.DataType == Symbol.SymbolType.PIN) {
//                node.DataType = Symbol.SymbolType.INT;
//            }
//            else if (parameter.DataType == Symbol.SymbolType.EVENT){
//                node.DataType = parameter.ValueType;
//            }
//            else {
//                MakeError(node, node.FirstChild.Next.Value, ErrorType.WrongType);
//            }
//        }
//        else {
//            MakeError(node, node.FirstChild.Next.Value, ErrorType.NotDeclared);
//        }
//    }
//
//    // Checks the parameters of the Write() function
//    private void CheckWriteCall(Node node) throws SemanticsException{
//        Symbol pinParameter = CurrentScope.RetrieveSymbol(node.FirstChild.Next.Value);
//        Node intParameter = node.FirstChild.Next.Next;
//
//        if (pinParameter != null) {
//            if (pinParameter.DataType == Symbol.SymbolType.PIN) {
//                CheckExpression(intParameter);
//                if (intParameter.DataType == Symbol.SymbolType.INT) {
//                    node.DataType = Symbol.SymbolType.VOID;
//                }
//                else {
//                    MakeError(node, "Second parameter", ErrorType.WrongType);
//                }
//            }
//            else {
//                MakeError(node, node.FirstChild.Next.Value, ErrorType.WrongType);
//            }
//        }
//        else {
//            MakeError(node, node.FirstChild.Next.Value, ErrorType.NotDeclared);
//        }
//    }
//
//    // Checks the parameters of the CreateEvent() function
//    private void CheckCreateEventCall(Node node) throws SemanticsException{
//        CheckExpression(node.FirstChild.Next);
//        if (node.FirstChild.Next.DataType == Symbol.SymbolType.INT) {
//            node.DataType = Symbol.SymbolType.EVENT;
//        }
//        else {
//            MakeError(node, node.FirstChild.Next.Value, ErrorType.WrongType);
//        }
//    }
//
//    // Check that the Pin declaration was performed correctly and inputs the Pin into the symbol table
//    // PWM is only for output
//    private void CheckCreatePinCall(Node node) throws SemanticsException{
//        int pinNumber = Integer.parseInt(node.FirstChild.Next.Next.Next.Value);
//
//        if (node.FirstChild.Next.Type != Node.NodeType.PWM || node.FirstChild.Next.Next.Type != Node.NodeType.Input) {
//            if (!UsedPinNumbers.contains(pinNumber)) {
//                node.DataType = Symbol.SymbolType.PIN;
//                UsedPinNumbers.add(pinNumber);
//            }
//            else{
//                MakeError(node, "Pin number already used");
//            }
//        }
//        else {
//            MakeError(node, "Pin cannot be both input and PWM");
//        }
//    }
//
//    // Checks which node type the node is and then checks the node as that type
//    private void CheckExpression(Node node) throws SemanticsException{
//        switch (node.Type){
//            case Expression:
//                CheckExpressionNode(node);
//                break;
//            case IntLiteral:
//                node.DataType = Symbol.SymbolType.INT;
//                break;
//            case FloatLiteral:
//                node.DataType = Symbol.SymbolType.FLOAT;
//                break;
//            case BoolLiteral:
//                node.DataType = Symbol.SymbolType.BOOL;
//                break;
//            case Identifier:
//                CheckIdentifier(node);
//                break;
//            case Call:
//                CheckCall(node);
//                break;
//        }
//    }
//
//    // Checks that the identifier has been previously declared.
//    private void CheckIdentifier(Node node) throws SemanticsException{
//        Symbol identifier = CurrentScope.RetrieveSymbol(node.Value);
//        if (identifier != null){
//            node.DataType = identifier.DataType;
//        }
//        else {
//            MakeError(node, node.Value, ErrorType.NotDeclared);
//        }
//    }
//
//    // Checks the expression node
//    private void CheckExpressionNode(Node node) throws SemanticsException{
//        CheckExpression(node.FirstChild);
//        CheckExpression(node.FirstChild.Next.Next);
//        if (node.FirstChild.DataType == node.FirstChild.Next.Next.DataType){
//            CheckOperator(node);
//        }
//        else {
//            MakeError(node, "Operators in expression must be of same data types");
//        }
//    }
//
//    // Checks that the operator type and operand data type are compatible
//    private void CheckOperator(Node node) throws SemanticsException{
//        switch (node.FirstChild.Next.Type){
//            case Modulo:
//                if (node.FirstChild.DataType == Symbol.SymbolType.INT){
//                    node.DataType = Symbol.SymbolType.INT;
//                }
//                else {
//                    MakeError(node, "Type error: must be int");
//                }
//                break;
//            case Plus:
//            case Minus:
//            case Times:
//            case Divide:
//                if (node.FirstChild.DataType != Symbol.SymbolType.BOOL){
//                    node.DataType = node.FirstChild.DataType;
//                }
//                else {
//                    MakeError(node, "Type error: cannot be boolean");
//                }
//                break;
//
//            case LessThan:
//            case GreaterThan:
//            case GreaterOrEqual:
//            case LessOrEqual:
//
//                if (node.FirstChild.DataType != Symbol.SymbolType.BOOL){
//                    node.DataType = Symbol.SymbolType.BOOL;
//                }
//                else {
//                    MakeError(node, "Type error: cannot be boolean");
//                }
//                break;
//
//            case And:
//            case Or:
//                if (node.FirstChild.DataType == Symbol.SymbolType.BOOL){
//                    node.DataType = Symbol.SymbolType.BOOL;
//                }
//                else {
//                    MakeError(node, "Type error: must be boolean");
//                }
//                break;
//            case Equals:
//            case NotEqual:
//                node.DataType = Symbol.SymbolType.BOOL;
//                break;
//        }
//    }
//
////endregion
//
//    // Displays error message and terminates semantic analysis
//    private void MakeError(Node node, String name, ErrorType errorType) throws SemanticsException{
//        String message = "";
//
//        switch (errorType){
//            case AlreadyDeclared:
//                message = "Line " + node.LineNumber + ": " + name + " has already been declared.";
//                break;
//            case NotDeclared:
//                message = "Line " + node.LineNumber + ": " + name + " has not been declared.";
//                break;
//            case WrongType:
//                message = "Line " + node.LineNumber + ": " + name + ": wrong type";
//                break;
//            case Default:
//                message = "Line " + node.LineNumber + ": " + name + ": error";
//        }
//
//        throw new SemanticsException(message);
//    }
//
//    private void MakeError(Node node, String message) throws SemanticsException{
//        throw new SemanticsException("Line " + node.LineNumber + ": " + message);
//    }
//}
