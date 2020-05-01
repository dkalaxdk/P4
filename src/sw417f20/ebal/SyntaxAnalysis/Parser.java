package sw417f20.ebal.SyntaxAnalysis;

import sw417f20.ebal.Exceptions.SyntaxException;

public class Parser extends RecursiveDescent {

    public Parser(Scanner scanner, String file) {
        super(scanner, file);
    }

    // Start 	-> 	Master Slave Slaves.
    @Override
    public Node Start() throws SyntaxException {
        Node Prog = Node.MakeNode(Node.NodeType.Prog);

        Prog.AddChild(Master());
        Prog.AddChild(Slave());
        Prog.AddChild(Slaves());

        return Prog;
    }

    // Master 	-> 	begin master Initiate Listeners end master.
    public Node Master() throws SyntaxException {

        if (Peek().type == Token.Type.BEGIN) {
            Node Master = Node.MakeNode(Node.NodeType.Master);

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
            return Node.MakeNode(Node.NodeType.Error);
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
            return Node.MakeNode(Node.NodeType.Empty);
        }
        else {
            MakeError("Expected BEGIN or EOF");
            return Node.MakeNode(Node.NodeType.Error);
        }
    }

    // Slave 	-> 	begin slave colon identifier Initiate EventHandlers end slave.
    public Node Slave() throws SyntaxException {

        if (Peek().type == Token.Type.BEGIN) {
            Node Slave = Node.MakeNode(Node.NodeType.Slave);

            Expect(Token.Type.BEGIN);
            Expect(Token.Type.SLAVE);
            Expect(Token.Type.COLON);

            Slave.AddChild(Node.MakeNode(Expect(Token.Type.IDENTIFIER)));
            Slave.AddChild(Initiate());
            Slave.AddChild(EventHandlers());

            Expect(Token.Type.END);
            Expect(Token.Type.SLAVE);

            return Slave;
        }
        else {
            MakeError("Expected BEGIN");
            return Node.MakeNode(Node.NodeType.Error);
        }
    }

    // Initiate 	-> 	initiate Block.
    public Node Initiate() throws SyntaxException {

        if (Peek().type == Token.Type.INITIATE) {

            Node Initiate = Node.MakeNode(Node.NodeType.Initiate);

            Expect(Token.Type.INITIATE);
            Initiate.AddChild(Block());

            return Initiate;
        }
        else {
            MakeError("Expected Initiate");
            return Node.MakeNode(Node.NodeType.Error);
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
            return Node.MakeNode(Node.NodeType.Empty);
        }
        else if (Peek().type == Token.Type.EVENTHANDLER) {
            MakeError("EventHandlers can only be declared in slaves");
            return Node.MakeNode(Node.NodeType.Error);
        }
        else {
            MakeError("Expected Listener or end of master");
            return Node.MakeNode(Node.NodeType.Error);
        }
    }

    // Listener	-> listener lparen identifier rparen Block.
    public Node Listener() throws SyntaxException {

        if (Peek().type == Token.Type.LISTENER) {
            Node Listener = Node.MakeNode(Node.NodeType.Listener, getLineNumber());

            Expect(Token.Type.LISTENER);
            Expect(Token.Type.LPAREN);
            Listener.AddChild(Node.MakeNode(Expect(Token.Type.IDENTIFIER)));
            Expect(Token.Type.RPAREN);
            Listener.AddChild(Block());

            return Listener;
        }
        else {
            MakeError("Expected Listener");
            return Node.MakeNode(Node.NodeType.Error);
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
            return Node.MakeNode(Node.NodeType.Empty);
        }
        else if (Peek().type == Token.Type.LISTENER) {
            MakeError("Listeners can only be declared in master");
            return Node.MakeNode(Node.NodeType.Error);
        }
        else {
            MakeError("Expected EventHandler or end of slave");
            return Node.MakeNode(Node.NodeType.Error);
        }
    }

    // EventHandler -> eventHandler lparen identifier rparen Block.
    public Node EventHandler() throws SyntaxException {

        if (Peek().type == Token.Type.EVENTHANDLER) {
            Node EventHandler = Node.MakeNode(Node.NodeType.EventHandler, getLineNumber());

            Expect(Token.Type.EVENTHANDLER);
            Expect(Token.Type.LPAREN);
            EventHandler.AddChild(Node.MakeNode(Expect(Token.Type.IDENTIFIER)));
            Expect(Token.Type.RPAREN);
            EventHandler.AddChild(Block());

            return EventHandler;
        }
        else {
            MakeError("Expected EventHandler");
            return Node.MakeNode(Node.NodeType.Error);
        }
    }

    // Block 	-> lbracket Stmts rbracket.
    public Node Block() throws SyntaxException {

        if (Peek().type == Token.Type.LBRACKET) {
            Node Block = Node.MakeNode(Node.NodeType.Block, getLineNumber());

            Expect(Token.Type.LBRACKET);
            Block.AddChild(Stmts());
            Expect(Token.Type.RBRACKET);

            return Block;
        }
        else {
            MakeError("Expected {");
            return Node.MakeNode(Node.NodeType.Error);
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
            return Node.MakeNode(Node.NodeType.Empty);
        }
        else {
            MakeError("Expected statement");
            return Node.MakeNode(Node.NodeType.Error);
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
            return Node.MakeNode(Node.NodeType.Error);
        }
    }

    // Assignment 	-> 	identifier assign Expr.
    public Node Assignment() throws SyntaxException {

        if (Peek().type == Token.Type.IDENTIFIER) {
            Node Assignment = Node.MakeNode(Node.NodeType.Assignment, getLineNumber());

            Assignment.AddChild(Node.MakeNode(Expect(Token.Type.IDENTIFIER)));
            Expect(Token.Type.ASSIGN);
            Assignment.AddChild(Expr());

            return Assignment;
        }
        else {
            MakeError("Expected assignment");
            return Node.MakeNode(Node.NodeType.Error);
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
                return GenerateDcl(Node.NodeType.FloatDeclaration, Token.Type.FLOAT);

            case INT:
                return GenerateDcl(Node.NodeType.IntDeclaration, Token.Type.INT);

            case BOOL:
                return GenerateDcl(Node.NodeType.BoolDeclaration, Token.Type.BOOL);

            case EVENT:
                return GenerateDcl(Node.NodeType.EventDeclaration, Token.Type.EVENT);

            case PIN:
                return GenerateDcl(Node.NodeType.PinDeclaration, Token.Type.PIN);

            default:
                MakeError("Expected declaration");
                return Node.MakeNode(Node.NodeType.Error);
        }
    }

    private Node GenerateDcl(Node.NodeType nodeType, Token.Type tokenType) throws SyntaxException {
        Node Dcl = Node.MakeNode(nodeType, getLineNumber());

        Expect(tokenType);
        Dcl.AddChild(Node.MakeNode(Expect(Token.Type.IDENTIFIER)));
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
            return Node.MakeNode(Node.NodeType.Empty);
        }
        else {
            MakeError("Expected assignment or end of statement");
            return Node.MakeNode(Node.NodeType.Error);
        }
    }

    // Expr 	-> 	Value AfterExpr
    //	         | 	lparen Expr rparen AfterExpr
    //	         | 	minus Value AfterExpr
    //	         | 	not identifier AfterExpr
    //	         | 	FunctionCall AfterExpr.
    public Node Expr() throws SyntaxException {
        Node Expr = Node.MakeNode(Node.NodeType.Expression, getLineNumber());

        if (Peek().type == Token.Type.IDENTIFIER || CheckForLiteral()) {
            Node value = Value();
            Node afterExpr = AfterExpr();

            if (afterExpr.Type == Node.NodeType.Empty) {
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

            if (afterExpr.Type == Node.NodeType.Empty) {
                return expr;
            }

            Expr.AddChild(expr);
            Expr.AddChild(afterExpr);
        }
        else if (Peek().type == Token.Type.OP_MINUS) {
            Expect(Token.Type.OP_MINUS);

            Node prefix = Node.MakeNode(Node.NodeType.PrefixMinus);
            Node value = Value();

            value.AddChild(prefix);

            Node afterExpr = AfterExpr();

            if (afterExpr.Type == Node.NodeType.Empty) {
                return value;
            }

            Expr.AddChild(value);
            Expr.AddChild(afterExpr);
        }
        else if (Peek().type == Token.Type.OP_NOT) {
            Expect(Token.Type.OP_NOT);
            Node prefix = Node.MakeNode(Node.NodeType.PrefixNot);
            Node identifier = Node.MakeNode(Expect(Token.Type.IDENTIFIER));

            identifier.AddChild(prefix);

            Node afterExpr = AfterExpr();

            if (afterExpr.Type == Node.NodeType.Empty) {
                return identifier;
            }

            Expr.AddChild(identifier);
            Expr.AddChild(afterExpr);
        }
        else if (CheckForFunctionCall()) {
            Node call = FunctionCall();
            Node afterExpr = AfterExpr();

            if (afterExpr.Type == Node.NodeType.Empty) {
                return call;
            }

            Expr.AddChild(call);
            Expr.AddChild(afterExpr);
        }
        else {
            MakeError("Expected expression");
            return Node.MakeNode(Node.NodeType.Error);
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
                return Node.MakeNode(Expect(Token.Type.IDENTIFIER));

            case LIT_Int:
                return Node.MakeNode(Expect(Token.Type.LIT_Int));

            case LIT_Float:
                return Node.MakeNode(Expect(Token.Type.LIT_Float));

            case LIT_Bool:
                return Node.MakeNode(Expect(Token.Type.LIT_Bool));

            default:
                MakeError("Expected literal int, float, or bool or an identifier");
                return Node.MakeNode(Node.NodeType.Error);
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
            return Node.MakeNode(Node.NodeType.Empty);
        }
        else {
            MakeError("Expected operator or end of statement");
            return Node.MakeNode(Node.NodeType.Error);
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
            return Node.MakeNode(Node.NodeType.Error);
        }
    }

    // ProcedureCall	->	broadcast lparen identifier rparen
    //	                 |	write lparen identifier comma Expr rparen.
    public Node ProcedureCall() throws SyntaxException {
        Node ProcedureCall = Node.MakeNode(Node.NodeType.Call, getLineNumber());

        if (Peek().type == Token.Type.BROADCAST) {
            Expect(Token.Type.BROADCAST);
            ProcedureCall.AddChild(Node.MakeNode(Node.NodeType.Broadcast));

            Expect(Token.Type.LPAREN);
            ProcedureCall.AddChild(Node.MakeNode(Expect(Token.Type.IDENTIFIER)));
            Expect(Token.Type.RPAREN);
        }
        else if (Peek().type == Token.Type.WRITE) {
            Expect(Token.Type.WRITE);
            ProcedureCall.AddChild(Node.MakeNode(Node.NodeType.Write));

            Expect(Token.Type.LPAREN);
            ProcedureCall.AddChild(Node.MakeNode(Expect(Token.Type.IDENTIFIER)));
            Expect(Token.Type.COMMA);
            ProcedureCall.AddChild(Expr());
            Expect(Token.Type.RPAREN);
        }
        else {
            MakeError("Expected write or broadcast");
            return Node.MakeNode(Node.NodeType.Error);
        }

        return ProcedureCall;
    }

    // FunctionCall	-> filterNoise lparen identifier comma FilterType rparen
    //	             | 	getValue lparen identifier rparen
    //	             |  createEvent lparen Expr rparen
    //	             |  createPin lparen PinType comma IOType comma intLiteral rparen.
    public Node FunctionCall() throws SyntaxException {
        Node FunctionCall = Node.MakeNode(Node.NodeType.Call, getLineNumber());

        if (Peek().type == Token.Type.FILTERNOISE) {
            Expect(Token.Type.FILTERNOISE);
            FunctionCall.AddChild(Node.MakeNode(Node.NodeType.FilterNoise));

            Expect(Token.Type.LPAREN);
            FunctionCall.AddChild(Node.MakeNode(Expect(Token.Type.IDENTIFIER)));
            Expect(Token.Type.COMMA);
            FunctionCall.AddChild(FilterType());
            Expect(Token.Type.RPAREN);
        }
        else if (Peek().type == Token.Type.GETVALUE) {
            Expect(Token.Type.GETVALUE);
            FunctionCall.AddChild(Node.MakeNode(Node.NodeType.GetValue));

            Expect(Token.Type.LPAREN);
            FunctionCall.AddChild(Node.MakeNode(Expect(Token.Type.IDENTIFIER)));
            Expect(Token.Type.RPAREN);
        }
        else if (Peek().type == Token.Type.CREATEEVENT) {
            Expect(Token.Type.CREATEEVENT);
            FunctionCall.AddChild(Node.MakeNode(Node.NodeType.CreateEvent));

            Expect(Token.Type.LPAREN);
            FunctionCall.AddChild(Expr());
            Expect(Token.Type.RPAREN);
        }
        else if (Peek().type == Token.Type.CREATEPIN) {
            Expect(Token.Type.CREATEPIN);
            FunctionCall.AddChild(Node.MakeNode(Node.NodeType.CreatePin));

            Expect(Token.Type.LPAREN);
            FunctionCall.AddChild(PinType());
            Expect(Token.Type.COMMA);
            FunctionCall.AddChild(IOType());
            Expect(Token.Type.COMMA);
            FunctionCall.AddChild(Node.MakeNode(Expect(Token.Type.LIT_Int)));
            Expect(Token.Type.RPAREN);
        }
        else {
            MakeError("Expected filterNoise, getValue, createEvent, or createPin");
            return Node.MakeNode(Node.NodeType.Error);
        }

        return FunctionCall;
    }

    // IfStmt 	-> 	if lparen Expr rparen Block IfEnd.
    public Node IfStmt() throws SyntaxException {

        if (Peek().type == Token.Type.IF) {
            Node IfStmt = Node.MakeNode(Node.NodeType.If, getLineNumber());

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
            return Node.MakeNode(Node.NodeType.Error);
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
            return Node.MakeNode(Node.NodeType.Empty);
        }
        else {
            MakeError("Expected else or end of if statement");
            return Node.MakeNode(Node.NodeType.Error);
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
            return Node.MakeNode(Node.NodeType.Error);
        }
    }

    // FilterType -> 	debounce
    //	           | 	constant
    //	           | 	range.
    public Node FilterType() throws SyntaxException {

        switch (Peek().type) {
            case DEBOUNCE:
                Expect(Token.Type.DEBOUNCE);
                return Node.MakeNode(Node.NodeType.Debounce);

            case CONSTANT:
                Expect(Token.Type.CONSTANT);
                return Node.MakeNode(Node.NodeType.Constant);

            case RANGE:
                Expect(Token.Type.RANGE);
                return Node.MakeNode(Node.NodeType.Range);

            default:
                MakeError("Expected debounce, constant, or range");
                return Node.MakeNode(Node.NodeType.Error);
        }
    }

    // PinType 	-> 	digital
    //	         | 	analog
    //	         | 	pwm.
    public Node PinType() throws SyntaxException {

        switch (Peek().type) {
            case DIGITAL:
                Expect(Token.Type.DIGITAL);
                return Node.MakeNode(Node.NodeType.Digital);

            case ANALOG:
                Expect(Token.Type.ANALOG);
                return Node.MakeNode(Node.NodeType.Analog);

            case PWM:
                Expect(Token.Type.PWM);
                return Node.MakeNode(Node.NodeType.PWM);

            default:
                MakeError("Expected digital, analog, or pwm");
                return Node.MakeNode(Node.NodeType.Error);
        }
    }

    // IOType 	-> 	input
    //	         | 	output.
    public Node IOType() throws SyntaxException {

        switch (Peek().type) {
            case INPUT:
                Expect(Token.Type.INPUT);
                return Node.MakeNode(Node.NodeType.Input);

            case OUTPUT:
                Expect(Token.Type.OUTPUT);
                return Node.MakeNode(Node.NodeType.Output);

            default:
                MakeError("Expected input or output");
                return Node.MakeNode(Node.NodeType.Error);
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
                return Node.MakeNode(Node.NodeType.Plus);

            case OP_MINUS:
                Expect(Token.Type.OP_MINUS);
                return Node.MakeNode(Node.NodeType.Minus);

            case OP_TIMES:
                Expect(Token.Type.OP_TIMES);
                return Node.MakeNode(Node.NodeType.Times);

            case OP_DIVIDE:
                Expect(Token.Type.OP_DIVIDE);
                return Node.MakeNode(Node.NodeType.Divide);

            case OP_MODULO:
                Expect(Token.Type.OP_MODULO);
                return Node.MakeNode(Node.NodeType.Modulo);

            default:
                MakeError("Expected +, -, *, /, or %");
                return Node.MakeNode(Node.NodeType.Error);
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
                return Node.MakeNode(Node.NodeType.LessThan);

            case LOP_GREATERTHAN:
                Expect(Token.Type.LOP_GREATERTHAN);
                return Node.MakeNode(Node.NodeType.GreaterThan);

            case LOP_NOTEQUAL:
                Expect(Token.Type.LOP_NOTEQUAL);
                return Node.MakeNode(Node.NodeType.NotEqual);

            case LOP_GREATEROREQUAL:
                Expect(Token.Type.LOP_GREATEROREQUAL);
                return Node.MakeNode(Node.NodeType.GreaterOrEqual);

            case LOP_LESSOREQUAL:
                Expect(Token.Type.LOP_LESSOREQUAL);
                return Node.MakeNode(Node.NodeType.LessOrEqual);

            case LOP_EQUALS:
                Expect(Token.Type.LOP_EQUALS);
                return Node.MakeNode(Node.NodeType.Equals);

            case LOP_AND:
                Expect(Token.Type.LOP_AND);
                return Node.MakeNode(Node.NodeType.And);

            case LOP_OR:
                Expect(Token.Type.LOP_OR);
                return Node.MakeNode(Node.NodeType.Or);

            default:
                MakeError("Expected <, >, !=, >=, <=, ==, ||, or &&");
                return Node.MakeNode(Node.NodeType.Error);
        }
    }

    private boolean CheckForType() {

        switch (Peek().type) {
            case FLOAT: case INT: case BOOL:
            case EVENT: case PIN:
                return true;
        }

        return false;
    }

    private boolean CheckForCall() {
        return  CheckForFunctionCall() ||
                CheckForProcedureCall();
    }

    private boolean CheckForFunctionCall() {

        switch (Peek().type) {
            case FILTERNOISE: case GETVALUE:
            case CREATEEVENT: case CREATEPIN:
                return true;
        }

        return false;
    }

    private boolean CheckForProcedureCall() {

        switch (Peek().type) {
            case BROADCAST: case WRITE:
                return true;

        }

        return false;
    }

    private boolean CheckForLiteral() {

        switch (Peek().type) {
            case LIT_Int: case LIT_Float: case LIT_Bool:
                return true;
        }

        return false;
    }

    private boolean CheckForOperator() {

        switch (Peek().type) {
            case OP_PLUS: case OP_MINUS: case OP_TIMES:
            case OP_DIVIDE: case OP_MODULO:
                return true;
        }

        return false;
    }

    private boolean CheckForLogicOperator() {

        switch (Peek().type) {
            case LOP_LESSTHAN: case LOP_GREATERTHAN:
            case LOP_LESSOREQUAL: case LOP_GREATEROREQUAL:
            case LOP_EQUALS: case LOP_NOTEQUAL:
            case LOP_AND: case LOP_OR:
                return true;
        }

        return false;
    }
}
