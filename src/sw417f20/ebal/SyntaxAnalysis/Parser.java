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

    // Master 	-> 	begin master Dcls Initiate Listeners end master.
    public Node Master() throws SyntaxException {

        if (peek().type == Token.Type.BEGIN) {
            Node Master = Node.MakeNode(Node.NodeType.Master);

            expect(Token.Type.BEGIN);
            expect(Token.Type.MASTER);

            Master.AddChild(Dcls());    //Global variables enabled by this statement
            Master.AddChild(Initiate());
            Master.AddChild(Listeners());

            expect(Token.Type.END);
            expect(Token.Type.MASTER);

            return Master;
        }
        else {
            makeError("Expected BEGIN");
            return Node.MakeNode(Node.NodeType.Error);
        }
    }

    // Slaves 	-> 	Slave Slaves
    //	         | 	.
    public Node Slaves() throws SyntaxException {

        if (peek().type == Token.Type.BEGIN) {
            Node slave = Slave();
            Node otherSlaves = Slaves();

            slave.MakeSiblings(otherSlaves);

            return slave;
        }
        else if (peek().type == Token.Type.EOF) {
            return Node.MakeNode(Node.NodeType.Empty);
        }
        else {
            makeError("Expected BEGIN or EOF");
            return Node.MakeNode(Node.NodeType.Error);
        }
    }

    // Slave 	-> 	begin slave colon identifier Initiate EventHandlers end slave.
    public Node Slave() throws SyntaxException {

        if (peek().type == Token.Type.BEGIN) {
            Node Slave = Node.MakeNode(Node.NodeType.Slave);

            expect(Token.Type.BEGIN);
            expect(Token.Type.SLAVE);
            expect(Token.Type.COLON);

            Slave.AddChild(GetLeaf(Token.Type.IDENTIFIER));
            Slave.AddChild(Dcls());
            Slave.AddChild(Initiate());
            Slave.AddChild(EventHandlers());

            expect(Token.Type.END);
            expect(Token.Type.SLAVE);

            return Slave;
        }
        else {
            makeError("Expected BEGIN");
            return Node.MakeNode(Node.NodeType.Error);
        }
    }

    // Initiate 	-> 	initiate Block.
    public Node Initiate() throws SyntaxException {

        if (peek().type == Token.Type.INITIATE) {

            Node Initiate = Node.MakeNode(Node.NodeType.Initiate);

            expect(Token.Type.INITIATE);
            Initiate.AddChild(Block());

            return Initiate;
        }
        else {
            makeError("Expected Initiate");
            return Node.MakeNode(Node.NodeType.Error);
        }
    }



    // Listeners 	-> 	Listener Listeners
    //	             | 	.
    public Node Listeners() throws SyntaxException {

        if (peek().type == Token.Type.LISTENER) {
            Node listener = Listener();
            Node otherListeners = Listeners();

            listener.MakeSiblings(otherListeners);

            return listener;
        }
        else if (peek().type == Token.Type.END) {
            return Node.MakeNode(Node.NodeType.Empty);
        }
        else if (peek().type == Token.Type.EVENTHANDLER) {
            makeError("EventHandlers can only be declared in slaves");
            return Node.MakeNode(Node.NodeType.Error);
        }
        else {
            makeError("Expected Listener or end of master");
            return Node.MakeNode(Node.NodeType.Error);
        }
    }

    // Listener	-> listener lparen identifier rparen Block.
    public Node Listener() throws SyntaxException {

        if (peek().type == Token.Type.LISTENER) {
            Node Listener = Node.MakeNode(Node.NodeType.Listener, getLineNumber());

            expect(Token.Type.LISTENER);
            expect(Token.Type.LPAREN);
            Listener.AddChild(GetLeaf(Token.Type.IDENTIFIER));
            expect(Token.Type.RPAREN);
            Listener.AddChild(Block());

            return Listener;
        }
        else {
            makeError("Expected Listener");
            return Node.MakeNode(Node.NodeType.Error);
        }
    }

    // EventHandlers -> 	EventHandler EventHandlers
    //	              | 	.
    public Node EventHandlers() throws SyntaxException {

        if (peek().type == Token.Type.EVENTHANDLER) {
            Node eventHandler = EventHandler();
            Node otherEventHandlers = EventHandlers();

            eventHandler.MakeSiblings(otherEventHandlers);

            return eventHandler;
        }
        else if (peek().type == Token.Type.END) {
            return Node.MakeNode(Node.NodeType.Empty);
        }
        else if (peek().type == Token.Type.LISTENER) {
            makeError("Listeners can only be declared in master");
            return Node.MakeNode(Node.NodeType.Error);
        }
        else {
            makeError("Expected EventHandler or end of slave");
            return Node.MakeNode(Node.NodeType.Error);
        }
    }

    // EventHandler -> eventHandler lparen identifier rparen Block.
    public Node EventHandler() throws SyntaxException {

        if (peek().type == Token.Type.EVENTHANDLER) {
            Node EventHandler = Node.MakeNode(Node.NodeType.EventHandler, getLineNumber());

            expect(Token.Type.EVENTHANDLER);
            expect(Token.Type.LPAREN);
            EventHandler.AddChild(GetLeaf(Token.Type.IDENTIFIER));
            expect(Token.Type.RPAREN);
            EventHandler.AddChild(Block());

            return EventHandler;
        }
        else {
            makeError("Expected EventHandler");
            return Node.MakeNode(Node.NodeType.Error);
        }
    }

    // Block 	-> lbracket Stmts rbracket.
    public Node Block() throws SyntaxException {

        if (peek().type == Token.Type.LBRACKET) {
            Node Block = Node.MakeNode(Node.NodeType.Block, getLineNumber());

            expect(Token.Type.LBRACKET);
            Block.AddChild(Stmts());
            expect(Token.Type.RBRACKET);

            return Block;
        }
        else {
            makeError("Expected {");
            return Node.MakeNode(Node.NodeType.Error);
        }
    }

    // Stmts 	-> 	Stmt Stmts
    //	         | 	.
    public Node Stmts() throws SyntaxException {

        if (peek().type == Token.Type.IDENTIFIER ||
            peek().type == Token.Type.IF ||
            CheckForType() ||
            CheckForCall()) {

            Node statement = Stmt();
            Node otherStatements = Stmts();

            statement.MakeSiblings(otherStatements);

            return statement;
        }
        else if (peek().type == Token.Type.RBRACKET) {
            return Node.MakeNode(Node.NodeType.Empty);
        }
        else {
            makeError("Expected statement");
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
            expect(Token.Type.SEMI);
            return call;
        }
        else if (peek().type == Token.Type.IDENTIFIER) {
            Node assignment = Assignment();
            expect(Token.Type.SEMI);
            return assignment;
        }
        else if (CheckForType()) {
            Node declaration = Dcl();
            expect(Token.Type.SEMI);
            return declaration;
        }
        else if (peek().type == Token.Type.IF) {
            return IfStmt();
        }
        else {
            makeError("Expected a call, an assignment, a declaration, or an if statement");
            return Node.MakeNode(Node.NodeType.Error);
        }
    }

    // Assignment 	-> 	identifier assign Expr.
    public Node Assignment() throws SyntaxException {

        if (peek().type == Token.Type.IDENTIFIER) {
            Node Assignment = Node.MakeNode(Node.NodeType.Assignment, getLineNumber());

            Assignment.AddChild(GetLeaf(Token.Type.IDENTIFIER));
            expect(Token.Type.ASSIGN);
            Assignment.AddChild(Expr());

            return Assignment;
        }
        else {
            makeError("Expected assignment");
            return Node.MakeNode(Node.NodeType.Error);
        }
    }

    // Dcls -> Dcl semi Dcls
    //      |  .
    public Node Dcls() throws SyntaxException {
        if (CheckForType()) {
            Node declaration = Dcl();
            expect(Token.Type.SEMI);
            Node otherDeclarations = Dcls();

            declaration.MakeSiblings(otherDeclarations);

            return declaration;
        }
        else if (peek().type == Token.Type.INITIATE) {
            return Node.MakeNode(Node.NodeType.Empty);
        }
        else {
            makeError("Expected declaration or end of declarations");
            return Node.MakeNode(Node.NodeType.Error);
        }
    }

    // Dcl 	-> 	float identifier DclAssign
    //	     | 	int identifier DclAssign
    //	     | 	bool identifier DclAssign
    //	     | 	event identifier DclAssign
    //	     |  pin identifier DclAssign .
    public Node Dcl() throws SyntaxException {

        switch (peek().type) {
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
                makeError("Expected declaration");
                return Node.MakeNode(Node.NodeType.Error);
        }
    }

    private Node GenerateDcl(Node.NodeType nodeType, Token.Type tokenType) throws SyntaxException {
        Node Dcl = Node.MakeNode(nodeType, getLineNumber());

        expect(tokenType);
        Dcl.AddChild(GetLeaf(Token.Type.IDENTIFIER));
        Dcl.AddChild(DclAssign());

        return Dcl;
    }

    // DclAssign	-> assign Expr
    //	             | .
    public Node DclAssign() throws SyntaxException {
        if (peek().type == Token.Type.ASSIGN) {
            expect(Token.Type.ASSIGN);
            return Expr();
        }
        else if (peek().type == Token.Type.SEMI) {
            return Node.MakeNode(Node.NodeType.Empty);
        }
        else {
            makeError("Expected assignment or end of statement");
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

        if (peek().type == Token.Type.IDENTIFIER || CheckForLiteral()) {
            Node value = Value();
            Node afterExpr = AfterExpr();

            if (afterExpr.Type == Node.NodeType.Empty) {
                return value;
            }

            Expr.AddChild(value);
            Expr.AddChild(afterExpr);
        }
        else if (peek().type == Token.Type.LPAREN) {
            expect(Token.Type.LPAREN);
            Node expr = Expr();
            expect(Token.Type.RPAREN);
            Node afterExpr = AfterExpr();

            if (afterExpr.Type == Node.NodeType.Empty) {
                return expr;
            }

            Expr.AddChild(expr);
            Expr.AddChild(afterExpr);
        }
        else if (peek().type == Token.Type.OP_MINUS) {
            expect(Token.Type.OP_MINUS);

            Node prefix = Node.MakeNode(Node.NodeType.PrefixMinus);
            Node value = Value();

            value.FirstChild = null;
            value.AddChild(prefix);

            Node afterExpr = AfterExpr();

            if (afterExpr.Type == Node.NodeType.Empty) {
                return value;
            }

            Expr.AddChild(value);
            Expr.AddChild(afterExpr);
        }
        else if (peek().type == Token.Type.OP_NOT) {
            expect(Token.Type.OP_NOT);
            Node prefix = Node.MakeNode(Node.NodeType.PrefixNot);
            Node identifier = GetLeaf(Token.Type.IDENTIFIER);

            identifier.FirstChild = null;
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
            makeError("Expected expression");
            return Node.MakeNode(Node.NodeType.Error);
        }

        return Expr;
    }

    // Value 	-> 	intLiteral
    //	         | 	floatLiteral
    //	         | 	boolLiteral
    //	         | 	identifier.
    public Node Value() throws SyntaxException {
        Token.Type type = peek().type;

        switch (type) {
            case IDENTIFIER:
            case LIT_Int:
            case LIT_Float:
            case LIT_Bool:
                return GetLeaf(type);

            default:
                makeError("Expected literal int, float, or bool or an identifier");
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
        else if (peek().type == Token.Type.SEMI || peek().type == Token.Type.RPAREN) {
            return Node.MakeNode(Node.NodeType.Empty);
        }
        else {
            makeError("Expected operator or end of statement");
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
            makeError("Expected call");
            return Node.MakeNode(Node.NodeType.Error);
        }
    }

    // ProcedureCall	->	broadcast lparen identifier rparen
    //	                 |	write lparen identifier comma Expr rparen.
    public Node ProcedureCall() throws SyntaxException {
        Node ProcedureCall = Node.MakeNode(Node.NodeType.Call, getLineNumber());

        if (peek().type == Token.Type.BROADCAST) {
            expect(Token.Type.BROADCAST);
            ProcedureCall.AddChild(Node.MakeNode(Node.NodeType.Broadcast));

            expect(Token.Type.LPAREN);
            ProcedureCall.AddChild(GetLeaf(Token.Type.IDENTIFIER));
            expect(Token.Type.RPAREN);
        }
        else if (peek().type == Token.Type.WRITE) {
            expect(Token.Type.WRITE);
            ProcedureCall.AddChild(Node.MakeNode(Node.NodeType.Write));

            expect(Token.Type.LPAREN);
            ProcedureCall.AddChild(GetLeaf(Token.Type.IDENTIFIER));
            expect(Token.Type.COMMA);
            ProcedureCall.AddChild(Expr());
            expect(Token.Type.RPAREN);
        }
        else {
            makeError("Expected write or broadcast");
            return Node.MakeNode(Node.NodeType.Error);
        }

        return ProcedureCall;
    }

    // FunctionCall	->  filterNoise lparen identifier comma FilterType rparen
    //	             | 	getValue lparen identifier rparen
    //	             |  createEvent lparen Expr rparen
    //	             |  createPin lparen PinType comma IOType comma intLiteral rparen.
    public Node FunctionCall() throws SyntaxException {
        Node FunctionCall = Node.MakeNode(Node.NodeType.Call, getLineNumber());

        if (peek().type == Token.Type.FILTERNOISE) {
            expect(Token.Type.FILTERNOISE);
            FunctionCall.AddChild(Node.MakeNode(Node.NodeType.FilterNoise));

            expect(Token.Type.LPAREN);
            FunctionCall.AddChild(GetLeaf(Token.Type.IDENTIFIER));
            expect(Token.Type.COMMA);
            FunctionCall.AddChild(FilterType());
            expect(Token.Type.RPAREN);
        }
        else if (peek().type == Token.Type.GETVALUE) {
            expect(Token.Type.GETVALUE);
            FunctionCall.AddChild(Node.MakeNode(Node.NodeType.GetValue));

            expect(Token.Type.LPAREN);
            FunctionCall.AddChild(GetLeaf(Token.Type.IDENTIFIER));
            expect(Token.Type.RPAREN);
        }
        else if (peek().type == Token.Type.CREATEEVENT) {
            expect(Token.Type.CREATEEVENT);
            FunctionCall.AddChild(Node.MakeNode(Node.NodeType.CreateEvent));

            expect(Token.Type.LPAREN);
            FunctionCall.AddChild(Expr());
            expect(Token.Type.RPAREN);
        }
        else if (peek().type == Token.Type.CREATEPIN) {
            expect(Token.Type.CREATEPIN);
            FunctionCall.AddChild(Node.MakeNode(Node.NodeType.CreatePin));

            expect(Token.Type.LPAREN);
            FunctionCall.AddChild(PinType());
            expect(Token.Type.COMMA);
            FunctionCall.AddChild(IOType());
            expect(Token.Type.COMMA);

            // Support for analog pins (A0 - A15);
            if (peek().type == Token.Type.LIT_Int) {
                FunctionCall.AddChild(GetLeaf(Token.Type.LIT_Int));
            }
            else if (peek().type == Token.Type.IDENTIFIER) {
                FunctionCall.AddChild(GetLeaf(Token.Type.IDENTIFIER));
            }
            else {
                makeError("Expected analog or digital pin number");
            }

            expect(Token.Type.RPAREN);
        }
        else {
            makeError("Expected filterNoise, getValue, createEvent, or createPin");
            return Node.MakeNode(Node.NodeType.Error);
        }

        return FunctionCall;
    }

    // IfStmt 	-> 	if lparen Expr rparen Block IfEnd.
    public Node IfStmt() throws SyntaxException {

        if (peek().type == Token.Type.IF) {
            Node IfStmt = Node.MakeNode(Node.NodeType.If, getLineNumber());

            expect(Token.Type.IF);
            expect(Token.Type.LPAREN);
            IfStmt.AddChild(Expr());
            expect(Token.Type.RPAREN);
            IfStmt.AddChild(Block());
            IfStmt.AddChild(IfEnd());

            return IfStmt;
        }
        else {
            makeError("Expected if");
            return Node.MakeNode(Node.NodeType.Error);
        }
    }

    // IfEnd 	-> 	else AfterElse
    //	         | 	.
    public Node IfEnd() throws SyntaxException {

        if (peek().type == Token.Type.ELSE) {
            expect(Token.Type.ELSE);
            return AfterElse();
        }
        else if (peek().type == Token.Type.RBRACKET ||
                 peek().type == Token.Type.IDENTIFIER ||
                 peek().type == Token.Type.IF ||
                 CheckForType() ||
                 CheckForCall()) {
            return Node.MakeNode(Node.NodeType.Empty);
        }
        else {
            makeError("Expected else or end of if statement");
            return Node.MakeNode(Node.NodeType.Error);
        }
    }

    // AfterElse 	-> 	IfStmt
    //	             | 	Block.
    public Node AfterElse() throws SyntaxException {

        if (peek().type == Token.Type.IF) {
            return IfStmt();
        }
        else if (peek().type == Token.Type.LBRACKET) {
            return Block();
        }
        else {
            makeError("Expected if statement or a block");
            return Node.MakeNode(Node.NodeType.Error);
        }
    }

    // FilterType -> 	debounce
    //	           | 	constant
    //	           | 	range.
    public Node FilterType() throws SyntaxException {

        switch (peek().type) {
            case DEBOUNCE:
                expect(Token.Type.DEBOUNCE);
                return Node.MakeNode(Node.NodeType.Debounce);

            case CONSTANT:
                expect(Token.Type.CONSTANT);
                return Node.MakeNode(Node.NodeType.Constant);

            case RANGE:
                expect(Token.Type.RANGE);
                return Node.MakeNode(Node.NodeType.Range);

            default:
                makeError("Expected debounce, constant, or range");
                return Node.MakeNode(Node.NodeType.Error);
        }
    }

    // PinType 	-> 	digital
    //	         | 	analog
    //	         | 	pwm.
    public Node PinType() throws SyntaxException {

        switch (peek().type) {
            case DIGITAL:
                expect(Token.Type.DIGITAL);
                return Node.MakeNode(Node.NodeType.Digital);

            case ANALOG:
                expect(Token.Type.ANALOG);
                return Node.MakeNode(Node.NodeType.Analog);

            case PWM:
                expect(Token.Type.PWM);
                return Node.MakeNode(Node.NodeType.PWM);

            default:
                makeError("Expected digital, analog, or pwm");
                return Node.MakeNode(Node.NodeType.Error);
        }
    }

    // IOType 	-> 	input
    //	         | 	output.
    public Node IOType() throws SyntaxException {

        switch (peek().type) {
            case INPUT:
                expect(Token.Type.INPUT);
                return Node.MakeNode(Node.NodeType.Input);

            case OUTPUT:
                expect(Token.Type.OUTPUT);
                return Node.MakeNode(Node.NodeType.Output);

            default:
                makeError("Expected input or output");
                return Node.MakeNode(Node.NodeType.Error);
        }
    }

    // Operator 	-> 	plus
    //	             | 	minus
    //	             | 	times
    //	             | 	divide
    //	             | 	modulo.
    public Node Operator() throws SyntaxException {

        switch (peek().type) {
            case OP_PLUS:
                expect(Token.Type.OP_PLUS);
                return Node.MakeNode(Node.NodeType.Plus);

            case OP_MINUS:
                expect(Token.Type.OP_MINUS);
                return Node.MakeNode(Node.NodeType.Minus);

            case OP_TIMES:
                expect(Token.Type.OP_TIMES);
                return Node.MakeNode(Node.NodeType.Times);

            case OP_DIVIDE:
                expect(Token.Type.OP_DIVIDE);
                return Node.MakeNode(Node.NodeType.Divide);

            case OP_MODULO:
                expect(Token.Type.OP_MODULO);
                return Node.MakeNode(Node.NodeType.Modulo);

            default:
                makeError("Expected +, -, *, /, or %");
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

        switch (peek().type) {
            case LOP_LESSTHAN:
                expect(Token.Type.LOP_LESSTHAN);
                return Node.MakeNode(Node.NodeType.LessThan);

            case LOP_GREATERTHAN:
                expect(Token.Type.LOP_GREATERTHAN);
                return Node.MakeNode(Node.NodeType.GreaterThan);

            case LOP_NOTEQUAL:
                expect(Token.Type.LOP_NOTEQUAL);
                return Node.MakeNode(Node.NodeType.NotEqual);

            case LOP_GREATEROREQUAL:
                expect(Token.Type.LOP_GREATEROREQUAL);
                return Node.MakeNode(Node.NodeType.GreaterOrEqual);

            case LOP_LESSOREQUAL:
                expect(Token.Type.LOP_LESSOREQUAL);
                return Node.MakeNode(Node.NodeType.LessOrEqual);

            case LOP_EQUALS:
                expect(Token.Type.LOP_EQUALS);
                return Node.MakeNode(Node.NodeType.Equals);

            case LOP_AND:
                expect(Token.Type.LOP_AND);
                return Node.MakeNode(Node.NodeType.And);

            case LOP_OR:
                expect(Token.Type.LOP_OR);
                return Node.MakeNode(Node.NodeType.Or);

            default:
                makeError("Expected <, >, !=, >=, <=, ==, ||, or &&");
                return Node.MakeNode(Node.NodeType.Error);
        }
    }

    private Node GetLeaf(Token.Type type) throws SyntaxException {
        int line = getLineNumber();
        Node node = Node.MakeNode(expect(type), line);

        AddEmpty(node);

        return node;
    }

    private void AddEmpty(Node node) {
        if (node.FirstChild == null) {
            node.AddChild(Node.MakeNode(Node.NodeType.Empty));
        }
    }

    private boolean CheckForType() {

        switch (peek().type) {
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

        switch (peek().type) {
            case FILTERNOISE: case GETVALUE:
            case CREATEEVENT: case CREATEPIN:
                return true;
        }

        return false;
    }

    private boolean CheckForProcedureCall() {

        switch (peek().type) {
            case BROADCAST: case WRITE:
                return true;

        }

        return false;
    }

    private boolean CheckForLiteral() {

        switch (peek().type) {
            case LIT_Int: case LIT_Float: case LIT_Bool:
                return true;
        }

        return false;
    }

    private boolean CheckForOperator() {

        switch (peek().type) {
            case OP_PLUS: case OP_MINUS: case OP_TIMES:
            case OP_DIVIDE: case OP_MODULO:
                return true;
        }

        return false;
    }

    private boolean CheckForLogicOperator() {

        switch (peek().type) {
            case LOP_LESSTHAN: case LOP_GREATERTHAN:
            case LOP_LESSOREQUAL: case LOP_GREATEROREQUAL:
            case LOP_EQUALS: case LOP_NOTEQUAL:
            case LOP_AND: case LOP_OR:
                return true;
        }

        return false;
    }
}
