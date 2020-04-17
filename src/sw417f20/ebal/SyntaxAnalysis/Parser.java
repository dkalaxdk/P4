package sw417f20.ebal.SyntaxAnalysis;

import sw417f20.ebal.Exceptions.SyntaxException;

public class Parser extends RecursiveDescent {

    public Parser(Scanner scanner, String file) {
        super(scanner, file);
    }

    // Start 	-> 	Master Slave Slaves.
    @Override
    public Node Start() throws SyntaxException {
        Node Prog = AST.MakeNode(AST.NodeType.Prog);

        Prog.AddChild(Master());
        Prog.AddChild(Slave());
        Prog.AddChild(Slaves());

        return Prog;
    }

    // Master 	-> 	begin master Initiate Listeners end master.
    public Node Master() throws SyntaxException {

        if (Peek().type == Token.Type.BEGIN) {
            Node Master = AST.MakeNode(AST.NodeType.Master);

            Expect(Token.Type.BEGIN);
            Expect(Token.Type.MASTER);

            Master.AddChild(Initiate());
            Master.AddChild(Listeners());

            Expect(Token.Type.END);
            Expect(Token.Type.MASTER);

            return Master;
        }
        else {
            MakeError("Expected BEGIN");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // Slaves 	-> 	Slave Slaves
    //	         | 	.
    public Node Slaves() throws SyntaxException {

        if (Peek().type == Token.Type.BEGIN) {
            Node slave = Slave();
            Node otherSlaves = Slaves();

            slave.MakeSiblings(otherSlaves);

            return slave;
        }
        else if (Peek().type == Token.Type.EOF) {
            return AST.MakeNode(AST.NodeType.Empty);
        }
        else {
            MakeError("Expected BEGIN or EOF");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // Slave 	-> 	begin slave colon identifier Initiate EventHandlers end slave.
    public Node Slave() throws SyntaxException {

        if (Peek().type == Token.Type.BEGIN) {
            Node Slave = AST.MakeNode(AST.NodeType.Slave);

            Expect(Token.Type.BEGIN);
            Expect(Token.Type.SLAVE);
            Expect(Token.Type.COLON);

            Slave.AddChild(AST.MakeNode(Expect(Token.Type.IDENTIFIER)));
            Slave.AddChild(Initiate());
            Slave.AddChild(EventHandlers());

            Expect(Token.Type.END);
            Expect(Token.Type.SLAVE);

            return Slave;
        }
        else {
            MakeError("Expected BEGIN");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // Initiate 	-> 	initiate Block.
    public Node Initiate() throws SyntaxException {

        if (Peek().type == Token.Type.INITIATE) {

            Node Initiate = AST.MakeNode(AST.NodeType.Initiate);

            Expect(Token.Type.INITIATE);
            Initiate.AddChild(Block());

            return Initiate;
        }
        else {
            MakeError("Expected Initiate");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }



    // Listeners 	-> 	Listener Listeners
    //	             | 	.
    public Node Listeners() throws SyntaxException {

        if (Peek().type == Token.Type.LISTENER) {
            Node listener = Listener();
            Node otherListeners = Listeners();

            listener.MakeSiblings(otherListeners);

            return listener;
        }
        else if (Peek().type == Token.Type.END) {
            return AST.MakeNode(AST.NodeType.Empty);
        }
        else if (Peek().type == Token.Type.EVENTHANDLER) {
            MakeError("EventHandlers can only be declared in slaves");
            return AST.MakeNode(AST.NodeType.Error);
        }
        else {
            MakeError("Expected Listener or end of master");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // Listener	-> listener lparen identifier rparen Block.
    public Node Listener() throws SyntaxException {

        if (Peek().type == Token.Type.LISTENER) {
            Node Listener = AST.MakeNode(AST.NodeType.Listener, getLineNumber());

            Expect(Token.Type.LISTENER);
            Expect(Token.Type.LPAREN);
            Listener.AddChild(AST.MakeNode(Expect(Token.Type.IDENTIFIER)));
            Expect(Token.Type.RPAREN);
            Listener.AddChild(Block());

            return Listener;
        }
        else {
            MakeError("Expected Listener");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // EventHandlers -> 	EventHandler EventHandlers
    //	              | 	.
    public Node EventHandlers() throws SyntaxException {

        if (Peek().type == Token.Type.EVENTHANDLER) {
            Node eventHandler = EventHandler();
            Node otherEventHandlers = EventHandlers();

            eventHandler.MakeSiblings(otherEventHandlers);

            return eventHandler;
        }
        else if (Peek().type == Token.Type.END) {
            return AST.MakeNode(AST.NodeType.Empty);
        }
        else if (Peek().type == Token.Type.LISTENER) {
            MakeError("Listeners can only be declared in master");
            return AST.MakeNode(AST.NodeType.Error);
        }
        else {
            MakeError("Expected EventHandler or end of slave");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // EventHandler -> eventHandler lparen identifier rparen Block.
    public Node EventHandler() throws SyntaxException {

        if (Peek().type == Token.Type.EVENTHANDLER) {
            Node EventHandler = AST.MakeNode(AST.NodeType.EventHandler, getLineNumber());

            Expect(Token.Type.EVENTHANDLER);
            Expect(Token.Type.LPAREN);
            EventHandler.AddChild(AST.MakeNode(Expect(Token.Type.IDENTIFIER)));
            Expect(Token.Type.RPAREN);
            EventHandler.AddChild(Block());

            return EventHandler;
        }
        else {
            MakeError("Expected EventHandler");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // Block 	-> lbracket Stmts rbracket.
    public Node Block() throws SyntaxException {

        if (Peek().type == Token.Type.LBRACKET) {
            Node Block = AST.MakeNode(AST.NodeType.Block, getLineNumber());

            Expect(Token.Type.LBRACKET);
            Block.AddChild(Stmts());
            Expect(Token.Type.RBRACKET);

            return Block;
        }
        else {
            MakeError("Expected {");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // Stmts 	-> 	Stmt Stmts
    //	         | 	.
    public Node Stmts() throws SyntaxException {

        if (Peek().type == Token.Type.IDENTIFIER ||
            Peek().type == Token.Type.IF ||
            CheckForType() ||
            CheckForCall()) {

            Node statement = Stmt();
            Node otherStatements = Stmts();

            statement.MakeSiblings(otherStatements);

            return statement;
        }
        else if (Peek().type == Token.Type.RBRACKET) {
            return AST.MakeNode(AST.NodeType.Empty);
        }
        else {
            MakeError("Expected statement");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }


    // Stmt 	-> 	Call semi
    //	         | 	Assignment semi
    //	         |	Dcl semi
    //	         | 	IfStmt.
    public Node Stmt() throws SyntaxException {

        if (CheckForCall()) {
            Node call = Call();
            Expect(Token.Type.SEMI);
            return call;
        }
        else if (Peek().type == Token.Type.IDENTIFIER) {
            Node assignment = Assignment();
            Expect(Token.Type.SEMI);
            return assignment;
        }
        else if (CheckForType()) {
            Node declaration = Dcl();
            Expect(Token.Type.SEMI);
            return declaration;
        }
        else if (Peek().type == Token.Type.IF) {
            return IfStmt();
        }
        else {
            MakeError("Expected a call, an assignment, a declaration, or an if statement");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // Assignment 	-> 	identifier assign Expr.
    public Node Assignment() throws SyntaxException {

        if (Peek().type == Token.Type.IDENTIFIER) {
            Node Assignment = AST.MakeNode(AST.NodeType.Assignment, getLineNumber());

            Assignment.AddChild(AST.MakeNode(Expect(Token.Type.IDENTIFIER)));
            Expect(Token.Type.ASSIGN);
            Assignment.AddChild(Expr());

            return Assignment;
        }
        else {
            MakeError("Expected assignment");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // Dcl 	-> 	float identifier DclAssign
    //	     | 	int identifier DclAssign
    //	     | 	bool identifier DclAssign
    //	     | 	event identifier DclAssign
    //	     |  pin identifier DclAssign .
    public Node Dcl() throws SyntaxException {

        switch (Peek().type) {
            case FLOAT:
                return GenerateDcl(AST.NodeType.FloatDeclaration, Token.Type.FLOAT);

            case INT:
                return GenerateDcl(AST.NodeType.IntDeclaration, Token.Type.INT);

            case BOOL:
                return GenerateDcl(AST.NodeType.BoolDeclaration, Token.Type.BOOL);

            case EVENT:
                return GenerateDcl(AST.NodeType.EventDeclaration, Token.Type.EVENT);

            case PIN:
                return GenerateDcl(AST.NodeType.PinDeclaration, Token.Type.PIN);

            default:
                MakeError("Expected declaration");
                return AST.MakeNode(AST.NodeType.Error);
        }
    }

    private Node GenerateDcl(AST.NodeType nodeType, Token.Type tokenType) throws SyntaxException {
        Node Dcl = AST.MakeNode(nodeType, getLineNumber());

        Expect(tokenType);
        Dcl.AddChild(AST.MakeNode(Expect(Token.Type.IDENTIFIER)));
        Dcl.AddChild(DclAssign());

        return Dcl;
    }

    // DclAssign	-> assign Expr
    //	             | .
    public Node DclAssign() throws SyntaxException {
        if (Peek().type == Token.Type.ASSIGN) {
            Expect(Token.Type.ASSIGN);
            return Expr();
        }
        else if (Peek().type == Token.Type.SEMI) {
            return AST.MakeNode(AST.NodeType.Empty);
        }
        else {
            MakeError("Expected assignment or end of statement");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // Expr 	-> 	Value AfterExpr
    //	         | 	lparen Expr rparen AfterExpr
    //	         | 	minus Value AfterExpr
    //	         | 	not identifier AfterExpr
    //	         | 	FunctionCall AfterExpr.
    public Node Expr() throws SyntaxException {
        Node Expr = AST.MakeNode(AST.NodeType.Expression, getLineNumber());

        if (Peek().type == Token.Type.IDENTIFIER || CheckForLiteral()) {
            Node value = Value();
            Node afterExpr = AfterExpr();

            if (afterExpr.Type == AST.NodeType.Empty) {
                return value;
            }

            Expr.AddChild(value);
            Expr.AddChild(afterExpr);
        }
        // TODO: Er ikke sikker på den her
        else if (Peek().type == Token.Type.LPAREN) {
            Expect(Token.Type.LPAREN);
            Node expr = Expr();
            Expect(Token.Type.RPAREN);
            Node afterExpr = AfterExpr();

            if (afterExpr.Type == AST.NodeType.Empty) {
                return expr;
            }

            Expr.AddChild(expr);
            Expr.AddChild(afterExpr);
        }
        else if (Peek().type == Token.Type.OP_MINUS) {
            Expect(Token.Type.OP_MINUS);

            Node prefix = new Node(AST.NodeType.PrefixMinus);
            Node value = Value();

            value.AddChild(prefix);

            Node afterExpr = AfterExpr();

            if (afterExpr.Type == AST.NodeType.Empty) {
                return value;
            }

            Expr.AddChild(value);
            Expr.AddChild(afterExpr);
        }
        else if (Peek().type == Token.Type.OP_NOT) {
            Expect(Token.Type.OP_NOT);
            Node prefix = AST.MakeNode(AST.NodeType.PrefixNot);
            Node identifier = AST.MakeNode(Expect(Token.Type.IDENTIFIER));

            identifier.AddChild(prefix);

            Node afterExpr = AfterExpr();

            if (afterExpr.Type == AST.NodeType.Empty) {
                return identifier;
            }

            Expr.AddChild(identifier);
            Expr.AddChild(afterExpr);
        }
        else if (CheckForFunctionCall()) {
            Node call = FunctionCall();
            Node afterExpr = AfterExpr();

            if (afterExpr.Type == AST.NodeType.Empty) {
                return call;
            }

            Expr.AddChild(call);
            Expr.AddChild(afterExpr);
        }
        else {
            MakeError("Expected expression");
            return AST.MakeNode(AST.NodeType.Error);
        }

        return Expr;
    }

    // Value 	-> 	intLiteral
    //	         | 	floatLiteral
    //	         | 	boolLiteral
    //	         | 	identifier.
    public Node Value() throws SyntaxException {

        switch (Peek().type) {
            case IDENTIFIER:
                return AST.MakeNode(Expect(Token.Type.IDENTIFIER));

            case LIT_Int:
                return AST.MakeNode(Expect(Token.Type.LIT_Int));

            case LIT_Float:
                return AST.MakeNode(Expect(Token.Type.LIT_Float));

            case LIT_Bool:
                return AST.MakeNode(Expect(Token.Type.LIT_Bool));

            default:
                MakeError("Expected literal int, float, or bool or an identifier");
                return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // AfterExpr 	-> 	Operator Expr
    //	             | 	LogicOp Expr
    //	             | 	.
    public Node AfterExpr() throws SyntaxException {

        if (CheckForOperator()) {
            Node operator = Operator();
            Node expr = Expr();

            operator.MakeSiblings(expr);

            return operator;
        }

        else if (CheckForLogicOperator()) {
            Node operator = LogicOperator();
            Node expr = Expr();

            operator.MakeSiblings(expr);

            return operator;
        }
        else if (Peek().type == Token.Type.SEMI || Peek().type == Token.Type.RPAREN) {
            return AST.MakeNode(AST.NodeType.Empty);
        }
        else {
            MakeError("Expected operator or end of statement");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }


    // Call 	-> 	ProcedureCall
    //         	 | 	FunctionCall.
    public Node Call() throws SyntaxException {

        if (CheckForProcedureCall()) {
            return ProcedureCall();
        }
        else if (CheckForFunctionCall()) {
            return FunctionCall();
        }
        else {
            MakeError("Expected call");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // ProcedureCall	->	broadcast lparen identifier rparen
    //	                 |	write lparen identifier comma Expr rparen.
    public Node ProcedureCall() throws SyntaxException {
        Node ProcedureCall = AST.MakeNode(AST.NodeType.Call, getLineNumber());

        if (Peek().type == Token.Type.BROADCAST) {
            Expect(Token.Type.BROADCAST);
            ProcedureCall.AddChild(AST.MakeNode(AST.NodeType.Broadcast));

            Expect(Token.Type.LPAREN);
            ProcedureCall.AddChild(AST.MakeNode(Expect(Token.Type.IDENTIFIER)));
            Expect(Token.Type.RPAREN);
        }
        else if (Peek().type == Token.Type.WRITE) {
            Expect(Token.Type.WRITE);
            ProcedureCall.AddChild(AST.MakeNode(AST.NodeType.Write));

            Expect(Token.Type.LPAREN);
            ProcedureCall.AddChild(AST.MakeNode(Expect(Token.Type.IDENTIFIER)));
            Expect(Token.Type.COMMA);
            ProcedureCall.AddChild(Expr());
            Expect(Token.Type.RPAREN);
        }
        else {
            MakeError("Expected write or broadcast");
            return AST.MakeNode(AST.NodeType.Error);
        }

        return ProcedureCall;
    }

    // FunctionCall	-> filterNoise lparen identifier comma FilterType rparen
    //	             | 	getValue lparen identifier rparen
    //	             |  createEvent lparen Value rparen
    //	             |  createPin lparen PinType comma IOType comma intLiteral rparen.
    public Node FunctionCall() throws SyntaxException {
        Node FunctionCall = AST.MakeNode(AST.NodeType.Call, getLineNumber());

        if (Peek().type == Token.Type.FILTERNOISE) {
            Expect(Token.Type.FILTERNOISE);
            FunctionCall.AddChild(AST.MakeNode(AST.NodeType.FilterNoise));

            Expect(Token.Type.LPAREN);
            FunctionCall.AddChild(AST.MakeNode(Expect(Token.Type.IDENTIFIER)));
            Expect(Token.Type.COMMA);
            FunctionCall.AddChild(FilterType());
            Expect(Token.Type.RPAREN);
        }
        else if (Peek().type == Token.Type.GETVALUE) {
            Expect(Token.Type.GETVALUE);
            FunctionCall.AddChild(AST.MakeNode(AST.NodeType.GetValue));

            Expect(Token.Type.LPAREN);
            FunctionCall.AddChild(AST.MakeNode(Expect(Token.Type.IDENTIFIER)));
            Expect(Token.Type.RPAREN);
        }
        else if (Peek().type == Token.Type.CREATEEVENT) {
            Expect(Token.Type.CREATEEVENT);
            FunctionCall.AddChild(AST.MakeNode(AST.NodeType.CreateEvent));

            Expect(Token.Type.LPAREN);
            FunctionCall.AddChild(Value());
            Expect(Token.Type.RPAREN);
        }
        else if (Peek().type == Token.Type.CREATEPIN) {
            Expect(Token.Type.CREATEPIN);
            FunctionCall.AddChild(AST.MakeNode(AST.NodeType.CreatePin));

            Expect(Token.Type.LPAREN);
            FunctionCall.AddChild(PinType());
            Expect(Token.Type.COMMA);
            FunctionCall.AddChild(IOType());
            Expect(Token.Type.COMMA);
            FunctionCall.AddChild(AST.MakeNode(Expect(Token.Type.LIT_Int)));
            Expect(Token.Type.RPAREN);
        }
        else {
            MakeError("Expected filterNoise, getValue, createEvent, or createPin");
            return AST.MakeNode(AST.NodeType.Error);
        }

        return FunctionCall;
    }

    // IfStmt 	-> 	if lparen Expr rparen Block IfEnd.
    public Node IfStmt() throws SyntaxException {

        if (Peek().type == Token.Type.IF) {
            Node IfStmt = AST.MakeNode(AST.NodeType.If, getLineNumber());

            Expect(Token.Type.IF);
            Expect(Token.Type.LPAREN);
            IfStmt.AddChild(Expr());
            Expect(Token.Type.RPAREN);
            IfStmt.AddChild(Block());
            IfStmt.AddChild(IfEnd());

            return IfStmt;
        }
        else {
            MakeError("Expected if");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // IfEnd 	-> 	else AfterElse
    //	         | 	.
    public Node IfEnd() throws SyntaxException {

        if (Peek().type == Token.Type.ELSE) {
            Expect(Token.Type.ELSE);
            return AfterElse();
        }
        else if (Peek().type == Token.Type.RBRACKET ||
                 Peek().type == Token.Type.IDENTIFIER ||
                 Peek().type == Token.Type.IF ||
                 CheckForType() ||
                 CheckForCall()) {
            return AST.MakeNode(AST.NodeType.Empty);
        }
        else {
            MakeError("Expected else or end of if statement");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // AfterElse 	-> 	IfStmt
    //	             | 	Block.
    public Node AfterElse() throws SyntaxException {

        if (Peek().type == Token.Type.IF) {
            return IfStmt();
        }
        else if (Peek().type == Token.Type.LBRACKET) {
            return Block();
        }
        else {
            MakeError("Expected if statement or a block");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // FilterType -> 	flip
    //	           | 	constant
    //	           | 	range.
    public Node FilterType() throws SyntaxException {

        switch (Peek().type) {
            case FLIP:
                Expect(Token.Type.FLIP);
                return AST.MakeNode(AST.NodeType.Flip);

            case CONSTANT:
                Expect(Token.Type.CONSTANT);
                return AST.MakeNode(AST.NodeType.Constant);

            case RANGE:
                Expect(Token.Type.RANGE);
                return AST.MakeNode(AST.NodeType.Range);

            default:
                MakeError("Expected FLIP, CONSTANT, or RANGE");
                return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // PinType 	-> 	digital
    //	         | 	analog
    //	         | 	pwm.
    public Node PinType() throws SyntaxException {

        switch (Peek().type) {
            case DIGITAL:
                Expect(Token.Type.DIGITAL);
                return AST.MakeNode(AST.NodeType.Digital);

            case ANALOG:
                Expect(Token.Type.ANALOG);
                return AST.MakeNode(AST.NodeType.Analog);

            case PWM:
                Expect(Token.Type.PWM);
                return AST.MakeNode(AST.NodeType.PWM);

            default:
                MakeError("Expected digital, analog, or pwm");
                return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // IOType 	-> 	input
    //	         | 	output.
    public Node IOType() throws SyntaxException {

        switch (Peek().type) {
            case INPUT:
                Expect(Token.Type.INPUT);
                return AST.MakeNode(AST.NodeType.Input);

            case OUTPUT:
                Expect(Token.Type.OUTPUT);
                return AST.MakeNode(AST.NodeType.Output);

            default:
                MakeError("Expected input or output");
                return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // Operator 	-> 	plus
    //	             | 	minus
    //	             | 	times
    //	             | 	divide
    //	             | 	modulo.
    public Node Operator() throws SyntaxException {

        switch (Peek().type) {
            case OP_PLUS:
                Expect(Token.Type.OP_PLUS);
                return AST.MakeNode(AST.NodeType.Plus);

            case OP_MINUS:
                Expect(Token.Type.OP_MINUS);
                return AST.MakeNode(AST.NodeType.Minus);

            case OP_TIMES:
                Expect(Token.Type.OP_TIMES);
                return AST.MakeNode(AST.NodeType.Times);

            case OP_DIVIDE:
                Expect(Token.Type.OP_DIVIDE);
                return AST.MakeNode(AST.NodeType.Divide);

            case OP_MODULO:
                Expect(Token.Type.OP_MODULO);
                return AST.MakeNode(AST.NodeType.Modulo);

            default:
                MakeError("Expected +, -, *, /, or %");
                return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // LogicOp 	->  lessThan
    //	         | 	greaterThan
    //	         | 	notEqual
    //	         | 	greaterOrEqual
    //	         | 	lessOrEqual
    //	         | 	equals
    //	         |  and
    //	         |  or.
    public Node LogicOperator() throws SyntaxException {

        switch (Peek().type) {
            case LOP_LESSTHAN:
                Expect(Token.Type.LOP_LESSTHAN);
                return AST.MakeNode(AST.NodeType.LessThan);

            case LOP_GREATERTHAN:
                Expect(Token.Type.LOP_GREATERTHAN);
                return AST.MakeNode(AST.NodeType.GreaterThan);

            case LOP_NOTEQUAL:
                Expect(Token.Type.LOP_NOTEQUAL);
                return AST.MakeNode(AST.NodeType.NotEqual);

            case LOP_GREATEROREQUAL:
                Expect(Token.Type.LOP_GREATEROREQUAL);
                return AST.MakeNode(AST.NodeType.GreaterOrEqual);

            case LOP_LESSOREQUAL:
                Expect(Token.Type.LOP_LESSOREQUAL);
                return AST.MakeNode(AST.NodeType.LessOrEqual);

            case LOP_EQUALS:
                Expect(Token.Type.LOP_EQUALS);
                return AST.MakeNode(AST.NodeType.Equals);

            case LOP_AND:
                Expect(Token.Type.LOP_AND);
                return AST.MakeNode(AST.NodeType.And);

            case LOP_OR:
                Expect(Token.Type.LOP_OR);
                return AST.MakeNode(AST.NodeType.Or);

            default:
                MakeError("Expected <, >, !=, >=, <=, ==, ||, or &&");
                return AST.MakeNode(AST.NodeType.Error);
        }
    }

    private boolean CheckForType() {
        return  Peek().type == Token.Type.FLOAT                 ||
                Peek().type == Token.Type.INT                   ||
                Peek().type == Token.Type.BOOL                  ||
                Peek().type == Token.Type.EVENT                 ||
                Peek().type == Token.Type.PIN;
    }

    private boolean CheckForCall() {
        return  CheckForFunctionCall()                           ||
                CheckForProcedureCall();
    }

    private boolean CheckForFunctionCall() {
        return  Peek().type == Token.Type.FILTERNOISE           ||
                Peek().type == Token.Type.GETVALUE              ||
                Peek().type == Token.Type.CREATEEVENT           ||
                Peek().type == Token.Type.CREATEPIN;
    }

    private boolean CheckForProcedureCall() {
        return  Peek().type == Token.Type.BROADCAST             ||
                Peek().type == Token.Type.WRITE;
    }

    private boolean CheckForLiteral() {
        return  Peek().type == Token.Type.LIT_Int               ||
                Peek().type == Token.Type.LIT_Float             ||
                Peek().type == Token.Type.LIT_Bool;
    }

    private boolean CheckForOperator() {
        return  Peek().type == Token.Type.OP_PLUS               ||
                Peek().type == Token.Type.OP_MINUS              ||
                Peek().type == Token.Type.OP_TIMES              ||
                Peek().type == Token.Type.OP_DIVIDE             ||
                Peek().type == Token.Type.OP_MODULO;
    }

    private boolean CheckForLogicOperator() {
        return  Peek().type == Token.Type.LOP_LESSTHAN          ||
                Peek().type == Token.Type.LOP_GREATERTHAN       ||
                Peek().type == Token.Type.LOP_NOTEQUAL          ||
                Peek().type == Token.Type.LOP_GREATEROREQUAL    ||
                Peek().type == Token.Type.LOP_LESSOREQUAL       ||
                Peek().type == Token.Type.LOP_EQUALS            ||
                Peek().type == Token.Type.LOP_AND               ||
                Peek().type == Token.Type.LOP_OR;
    }
}
