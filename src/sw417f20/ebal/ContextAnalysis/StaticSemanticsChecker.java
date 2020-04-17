package sw417f20.ebal.ContextAnalysis;

//import sw417f20.ebal.ContextAnalysis.PinSymbol;
import sw417f20.ebal.SyntaxAnalysis.AST;
import sw417f20.ebal.SyntaxAnalysis.Node;

        import java.util.ArrayList;

/**
 * Class responsible for checking the static semantics of the EBAL code
 */
public class StaticSemanticsChecker {

    SymbolTable CurrentScope;

    private enum ErrorType{ NotDeclared, AlreadyDeclared, WrongType, WrongParameter, Default}

    private boolean InEventHandler;
    private boolean InInitiate;
    private ArrayList<Integer> UsedPinNumbers;

    public StaticSemanticsChecker(){
        CurrentScope = new HashSymbolTable();
        InEventHandler = false;
        InInitiate = false;
        UsedPinNumbers = new ArrayList<>();
    }

//region Done
    // Starts the process of scope and type checking the AST and decorating its nodes with datatypes
    public void Run(Node node){
        CheckProg(node);
    }

    // Checks the Prog node
    private void CheckProg(Node node){
        Node child = node.FirstChild;
        CheckMaster(child);

        // Sets flag to help check that the slave cannot call broadcast()
        InEventHandler = true;
        child = child.Next;
        while (child.Type != AST.NodeType.Empty){
            CheckSlave(child);
            child = child.Next;
        }
    }

    // Checks the Master node
    private void CheckMaster(Node node){
        CurrentScope.OpenScope();
        Node child = node.FirstChild;
        CheckInitiate(child);
        child = child.Next;
        while (child.Type != AST.NodeType.Empty){
            CheckListener(child);
            child = child.Next;
        }
        CurrentScope.CloseScope();
    }

    // Checks the Slave node
    private void CheckSlave(Node node){
        CurrentScope.OpenScope();
        Node child = node.FirstChild.Next;
        CheckInitiate(child);
        child = child.Next;
        while (child.Type != AST.NodeType.Empty){
            CheckEventHandler(child);
            child = child.Next;
        }
        CurrentScope.CloseScope();
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
                node.FirstChild.DefinitionReference = symbol.ReferenceNode;
                CheckBlock(node.FirstChild.Next);
            }
            else {
                MakeError(node, symbol.Name, ErrorType.WrongType);
            }
        }
        else {
            MakeError(node, node.FirstChild.Value, ErrorType.NotDeclared);
        }
    }

    // Scope and type checks the EventHandler node
    private void CheckEventHandler(Node node) {
        Symbol symbol = CurrentScope.RetrieveSymbol(node.FirstChild.Value);
        if (symbol != null){
            if (symbol.Type == Symbol.SymbolType.EVENT){
                node.FirstChild.DefinitionReference = symbol.ReferenceNode; //TODO: skal den med?
                CheckBlock(node.FirstChild.Next);
            }
            else {
                MakeError(node, symbol.Name, ErrorType.WrongType);
            }
        }
        else {
            MakeError(node, node.FirstChild.Value, ErrorType.NotDeclared);
        }
    }

    // Determines what kind of block the node is and then calls the appropriate method for checking it
    private void CheckBlock(Node node){

        if (InInitiate){
            CheckInitiateBlock(node);
        }
        else if (InEventHandler){
            CurrentScope.OpenScope();
            CheckEventHandlerBlock(node);
            CurrentScope.CloseScope();
        }
        else {
            CurrentScope.OpenScope();
            CheckListenerBlock(node);
            CurrentScope.CloseScope();
        }
    }

    // Checks an Initiate block
    private void CheckInitiateBlock(Node node){
        Node child = node.FirstChild;
        while (child.Type != AST.NodeType.Empty) {
            if (child.Type == AST.NodeType.PinDeclaration) {
                CheckPinDeclaration(child);
            }
            else {
                MakeError(child, "Only pin declarations allowed in Initiate");
            }
            child = child.Next;
        }
        UsedPinNumbers.clear();
    }

    // checks a Listener block
    private void CheckListenerBlock(Node node){
        Node child = node.FirstChild;
        while (child.Type != AST.NodeType.Empty){
            switch (child.Type){
                case Call:
                    CheckCall(child);
                    break;
                case Assignment:
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
                case EventDeclaration:
                    CheckEventDeclaration(child);
                    break;
                default:
                    MakeError(child, "No pin declarations allowed in Listener");
            }
            child = child.Next;
        }
    }

    // Check an EventHandler block
    private void CheckEventHandlerBlock(Node node){
        Node child = node.FirstChild;
        while (child.Type != AST.NodeType.Empty) {
            switch (child.Type) {
                case Call:
                    CheckCall(child);
                    break;
                case Assignment:
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
                    MakeError(child, "No pin or event declarations allowed in EventHandler");
            }
        }
    }

    // Scope checks the PinDeclaration node
    private void CheckPinDeclaration(Node node) {
        if (!CurrentScope.DeclaredLocally(node.FirstChild.Value)){
            if (node.FirstChild.Next.Type == AST.NodeType.Call){
                CheckCall(node.FirstChild.Next);
                CurrentScope.EnterSymbol(node.FirstChild.Value, Symbol.SymbolType.PIN, node);
            }
            else {
                MakeError(node, "Illegal declaration of pin");
            }
        }
        else {
            MakeError(node, node.FirstChild.Value, ErrorType.AlreadyDeclared);
        }
    }

    //TODO: Skal code gen bruge andre definitionReferences end til Pins (skal de vide hvor en event mm er erklæret og/eller sidst assignet til?)

    //TODO: Jeg er kommet hertil!

    // Scope and type checks the FloatDeclaration node
    private void CheckFloatDeclaration(Node node) {
        if (!CurrentScope.DeclaredLocally(node.FirstChild.Value)){
            Node expression = node.FirstChild.Next;
            if (expression.Type != AST.NodeType.Empty) {
                CheckExpression(expression);
                if (expression.DataType != Symbol.SymbolType.FLOAT){
                    MakeError(node, node.FirstChild.Value, ErrorType.WrongType);
                }
            }
            CurrentScope.EnterSymbol(node.Value, Symbol.SymbolType.FLOAT);
        }
        else {
            MakeError(node, node.FirstChild.Value, ErrorType.AlreadyDeclared);
        }
    }

    // Scope and type checks the IntDeclaration node
    private void CheckIntDeclaration(Node node) {
        if (!CurrentScope.DeclaredLocally(node.FirstChild.Value)){
            Node expression = node.FirstChild.Next;
            if (expression.Type != AST.NodeType.Empty) {
                CheckExpression(expression);
                if (expression.DataType != Symbol.SymbolType.INT){
                    MakeError(node, node.FirstChild.Value, ErrorType.WrongType);
                }
            }
            CurrentScope.EnterSymbol(node.Value, Symbol.SymbolType.INT);
        }
        else {
            MakeError(node, node.FirstChild.Value, ErrorType.AlreadyDeclared);
        }
    }

    // Scope and type checks the BoolDeclaration node
    private void CheckBoolDeclaration(Node node) {
        if (!CurrentScope.DeclaredLocally(node.FirstChild.Value)){
            Node expression = node.FirstChild.Next;
            if (expression.Type != AST.NodeType.Empty) {
                CheckExpression(expression);
                if (expression.DataType != Symbol.SymbolType.BOOL){
                    MakeError(node, node.FirstChild.Value, ErrorType.WrongType);
                }
            }
            CurrentScope.EnterSymbol(node.Value, Symbol.SymbolType.BOOL);
        }
        else {
            MakeError(node, node.FirstChild.Value, ErrorType.AlreadyDeclared);
        }
    }

    // Scope and type checks the EventDeclaration node
    private void CheckEventDeclaration(Node node) {
        if (!CurrentScope.DeclaredLocally(node.FirstChild.Value)){
            Node expression = node.FirstChild.Next;
            if (expression != null) {
                CheckExpression(expression);
                if (expression.DataType != Symbol.SymbolType.EVENT){
                    MakeError(node, node.FirstChild.Value, ErrorType.WrongType);
                }
            }
            CurrentScope.EnterSymbol(node.Value, Symbol.SymbolType.EVENT);
        }
        else {
            MakeError(node, node.FirstChild.Value, ErrorType.AlreadyDeclared);
        }
    }

    //TODO: må ikke dividere med 0

    // Scope and type checks the Assignment node
    private void CheckAssignment(Node node) {
        Symbol identifier = CurrentScope.RetrieveSymbol(node.FirstChild.Value);
        Node expression = node.FirstChild.Next;
        if(identifier != null) {
            CheckExpression(expression);
            if (identifier.Type != expression.DataType) {
                MakeError(node, node.FirstChild.Value, ErrorType.WrongType);
            }
            else if (expression.DataType == Symbol.SymbolType.PIN){
                MakeError(node, "expression cannot be of type Pin");
            }
            else if (InEventHandler && expression.DataType == Symbol.SymbolType.EVENT){
                MakeError(node, "expression cannot be of type Event in EventHandler");
            }
        }
        else {
            MakeError(node, node.FirstChild.Value, ErrorType.NotDeclared);
        }
    }

    // Type checks the If node
    private void CheckIf(Node node) {
        CheckExpression(node.FirstChild);
        if(node.FirstChild.DataType == Symbol.SymbolType.BOOL){
            CheckBlock(node.FirstChild.Next);
            Node elseStmt = node.FirstChild.Next.Next;
            if (elseStmt.Type != AST.NodeType.Empty){
                if(elseStmt.Type == AST.NodeType.Block){
                    CheckBlock(elseStmt);
                }
                else {
                    CheckIf(elseStmt);
                }
            }
        }
        else{
            MakeError(node, "If statement requires boolean expression");
        }
    }
    //endregion

    // Type checks the Call node depending on the function called
    private void CheckCall(Node node) {
        AST.NodeType functionType = node.FirstChild.Type;

        if (InInitiate){
            if (functionType == AST.NodeType.CreatePin){
                CheckCreatePinCall(node);
            }
            else{
                MakeError(node, "Illegal declaration of pin");
            }
        }
        else if (InEventHandler){
            switch (functionType){
                case Write:
                    CheckWriteCall(node);
                    break;
                case GetValue:
                    CheckGetValueCall(node);
                    break;
                default:
                    MakeError(node, "Illegal function call");
            }
        }
        else {
            switch (functionType){
                case Broadcast:
                    CheckBroadcastCall(node);
                    break;
                case GetValue:
                    CheckGetValueCall(node);
                    break;
                case FilterNoise:
                    CheckFilterNoiseCall(node);
                    break;
                case CreateEvent:
                    CheckCreateEventCall(node);
                    break;
                default:
                    MakeError(node, "Illegal function call");
            }
        }

//        CheckParameters(node);
//        switch (node.FirstChild.Type){
//            case Broadcast:
//                CheckBroadcastCall(node);
//                break;
//
//            case FilterNoise:
//                CheckFilterNoiseCall(node);
//                break;
//
//            case GetValue:
//                CheckGetValueCall(node);
//                break;
//
//            case Write:
//                CheckWriteCall(node);
//                break;
//
//            case CreateEvent:
//                CheckCreateEventCall(node);
//                break;
//
//            case CreatePin:
//                CheckCreatePinCall(node);
//                break;
//        }
//        node.DataType = node.FirstChild.DataType;
    }


//region NotDone

    // Check the parameters of the Broadcast() function
    private void CheckBroadcastCall(Node node){
        Symbol parameter = CurrentScope.RetrieveSymbol(node.FirstChild.Next.Value);
        if (parameter.Type != Symbol.SymbolType.EVENT) {
            MakeError(node, node.FirstChild.Next.DataType.toString(), ErrorType.WrongType);
        }
    }

    // digital : flip, constant
    // analog : range
    private void CheckFilterNoiseCall(Node node){
        Symbol pinParameter = CurrentScope.RetrieveSymbol(node.FirstChild.Next.Value);
        if (pinParameter.Type == Symbol.SymbolType.PIN &&
                (node.FirstChild.Next.Next.Type == Symbol.SymbolType.FILTERTYPE){
        }
        MakeError(node, node.FirstChild.Value, ErrorType.WrongType);
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
//endregion

//region Done
    // Check that the Pin declaration was performed correctly and inputs the Pin into the symbol table
    private void CheckCreatePinCall(Node node) {
        CheckPinParameters(node);
        if (PinNumberNotUsed(node)) {
            CurrentScope.EnterSymbol(node.Value, Symbol.SymbolType.PIN);
        }
        else{
            MakeError(node, "Pin number already used");
        }
    }

    // Checks that the parameters of the CreatePin() function are valid
    private void CheckPinParameters(Node node){
        Node firstParameter = node.FirstChild.Next;
        Node secondParameter = firstParameter.Next;
        Node thirdParameter = secondParameter.Next;

        if (firstParameter.Type == AST.NodeType.Digital ||
            firstParameter.Type == AST.NodeType.Analog ||
            firstParameter.Type == AST.NodeType.PWM){

            if (secondParameter.Type == AST.NodeType.Input ||
                secondParameter.Type == AST.NodeType.Output){

                if (thirdParameter.Type == AST.NodeType.IntLiteral){

                    if (firstParameter.Type != AST.NodeType.PWM || secondParameter.Type != AST.NodeType.Input){
                        MakeError(node, "Pin cannot be both input and PWM");
                    }
                }
                else{
                    MakeError(node, thirdParameter.Type.toString(), ErrorType.WrongParameter);
                }
            }
            else{
                MakeError(node, secondParameter.Type.toString(), ErrorType.WrongParameter);
            }
        }
        else{
            MakeError(node, firstParameter.Type.toString(), ErrorType.WrongParameter);
        }
    }

    // Checks that the pin number for the Pin declaration has not already been associated with a Pin
    private boolean PinNumberNotUsed(Node node) {
        int pinNumber = Integer.parseInt(node.FirstChild.Next.Next.Next.Value);
        return UsedPinNumbers.contains(pinNumber);
    }
//endregion

//region NotDone
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
//endregion


    // Displays error message and terminates semantic analysis
    private void MakeError(Node node, String name, ErrorType errorType){
        String message = "";

        switch (errorType){
            case AlreadyDeclared:
                message = "Line " + node.LineNumber + ": " + name + " has already been declared.";
                break;
            case NotDeclared:
                message = "Line " + node.LineNumber + ": " + name + " has not been declared.";
                break;
            case WrongType:
                message = "Line " + node.LineNumber + ": " + name + ": wrong type";
                break;
            case WrongParameter:
                message = "Line " + node.LineNumber + ": " + name + ": wrong parameter";
            case Default:
                message = "Line " + node.LineNumber + ": " + name + ": error";
        }

        System.err.println(message);
        System.exit(0);
    }

    private void MakeError(Node node, String message){
        System.err.println("Line " + node.LineNumber + ": " + message);
        System.exit(0);
    }
}
