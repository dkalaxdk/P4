package sw417f20.ebal.SyntaxAnalysis;

import sw417f20.ebal.Exceptions.SyntaxException;

public class Parser extends RecursiveDescent {

    public Parser(Scanner scanner, String file) {
        super(scanner, file);
    }

    // Start 	-> 	Master Slave Slaves.
    @Override
    public Node Start() throws SyntaxException {
        Node Prog = Node.makeNode(Node.NodeType.Prog);

        Prog.addChild(Master());
        Prog.addChild(Slave());
        Prog.addChild(Slaves());

        return Prog;
    }

    // Master 	-> 	begin master Dcls Initiate Listeners end master.
    public Node Master() throws SyntaxException {

        if (peek().type == Token.Type.BEGIN) {
            Node Master = Node.makeNode(Node.NodeType.Master);

            expect(Token.Type.BEGIN);
            expect(Token.Type.MASTER);

            Master.addChild(Dcls());    //Global variables enabled by this statement
            Master.addChild(Initiate());
            Master.addChild(Listeners());

            expect(Token.Type.END);
            expect(Token.Type.MASTER);

            return Master;
        }
        else {
            makeError("Expected BEGIN");
            return Node.makeNode(Node.NodeType.Error);
        }
    }

    // Slaves 	-> 	Slave Slaves
    //	         | 	.
    public Node Slaves() throws SyntaxException {

        if (peek().type == Token.Type.BEGIN) {
            Node slave = Slave();
            Node otherSlaves = Slaves();

            slave.makeSiblings(otherSlaves);

            return slave;
        }
        else if (peek().type == Token.Type.EOF) {
            return Node.makeNode(Node.NodeType.Empty);
        }
        else {
            makeError("Expected BEGIN or EOF");
            return Node.makeNode(Node.NodeType.Error);
        }
    }

    // Slave 	-> 	begin slave colon identifier Initiate EventHandlers end slave.
    public Node Slave() throws SyntaxException {

        if (peek().type == Token.Type.BEGIN) {
            Node Slave = Node.makeNode(Node.NodeType.Slave);

            expect(Token.Type.BEGIN);
            expect(Token.Type.SLAVE);
            expect(Token.Type.COLON);

            Slave.addChild(getLeaf(Token.Type.IDENTIFIER));
            Slave.addChild(Dcls());
            Slave.addChild(Initiate());
            Slave.addChild(EventHandlers());

            expect(Token.Type.END);
            expect(Token.Type.SLAVE);

            return Slave;
        }
        else {
            makeError("Expected BEGIN");
            return Node.makeNode(Node.NodeType.Error);
        }
    }

    // Initiate 	-> 	initiate Block.
    public Node Initiate() throws SyntaxException {

        if (peek().type == Token.Type.INITIATE) {

            Node Initiate = Node.makeNode(Node.NodeType.Initiate);

            expect(Token.Type.INITIATE);
            Initiate.addChild(Block());

            return Initiate;
        }
        else {
            makeError("Expected Initiate");
            return Node.makeNode(Node.NodeType.Error);
        }
    }



    // Listeners 	-> 	Listener Listeners
    //	             | 	.
    public Node Listeners() throws SyntaxException {

        if (peek().type == Token.Type.LISTENER) {
            Node listener = Listener();
            Node otherListeners = Listeners();

            listener.makeSiblings(otherListeners);

            return listener;
        }
        else if (peek().type == Token.Type.END) {
            return Node.makeNode(Node.NodeType.Empty);
        }
        else if (peek().type == Token.Type.EVENTHANDLER) {
            makeError("EventHandlers can only be declared in slaves");
            return Node.makeNode(Node.NodeType.Error);
        }
        else {
            makeError("Expected Listener or end of master");
            return Node.makeNode(Node.NodeType.Error);
        }
    }

    // Listener	-> listener lparen identifier rparen Block.
    public Node Listener() throws SyntaxException {

        if (peek().type == Token.Type.LISTENER) {
            Node Listener = Node.makeNode(Node.NodeType.Listener, getLineNumber());

            expect(Token.Type.LISTENER);
            expect(Token.Type.LPAREN);
            Listener.addChild(getLeaf(Token.Type.IDENTIFIER));
            expect(Token.Type.RPAREN);
            Listener.addChild(Block());

            return Listener;
        }
        else {
            makeError("Expected Listener");
            return Node.makeNode(Node.NodeType.Error);
        }
    }

    // EventHandlers -> 	EventHandler EventHandlers
    //	              | 	.
    public Node EventHandlers() throws SyntaxException {

        if (peek().type == Token.Type.EVENTHANDLER) {
            Node eventHandler = EventHandler();
            Node otherEventHandlers = EventHandlers();

            eventHandler.makeSiblings(otherEventHandlers);

            return eventHandler;
        }
        else if (peek().type == Token.Type.END) {
            return Node.makeNode(Node.NodeType.Empty);
        }
        else if (peek().type == Token.Type.LISTENER) {
            makeError("Listeners can only be declared in master");
            return Node.makeNode(Node.NodeType.Error);
        }
        else {
            makeError("Expected EventHandler or end of slave");
            return Node.makeNode(Node.NodeType.Error);
        }
    }

    // EventHandler -> eventHandler lparen identifier rparen Block.
    public Node EventHandler() throws SyntaxException {

        if (peek().type == Token.Type.EVENTHANDLER) {
            Node EventHandler = Node.makeNode(Node.NodeType.EventHandler, getLineNumber());

            expect(Token.Type.EVENTHANDLER);
            expect(Token.Type.LPAREN);
            EventHandler.addChild(getLeaf(Token.Type.IDENTIFIER));
            expect(Token.Type.RPAREN);
            EventHandler.addChild(Block());

            return EventHandler;
        }
        else {
            makeError("Expected EventHandler");
            return Node.makeNode(Node.NodeType.Error);
        }
    }

    // Block 	-> lbracket Stmts rbracket.
    public Node Block() throws SyntaxException {

        if (peek().type == Token.Type.LBRACKET) {
            Node Block = Node.makeNode(Node.NodeType.Block, getLineNumber());

            expect(Token.Type.LBRACKET);
            Block.addChild(Stmts());
            expect(Token.Type.RBRACKET);

            return Block;
        }
        else {
            makeError("Expected {");
            return Node.makeNode(Node.NodeType.Error);
        }
    }

    // Stmts 	-> 	Stmt Stmts
    //	         | 	.
    public Node Stmts() throws SyntaxException {

        if (peek().type == Token.Type.IDENTIFIER ||
            peek().type == Token.Type.IF ||
            checkForType() ||
            CheckForCall()) {

            Node statement = Stmt();
            Node otherStatements = Stmts();

            statement.makeSiblings(otherStatements);

            return statement;
        }
        else if (peek().type == Token.Type.RBRACKET) {
            return Node.makeNode(Node.NodeType.Empty);
        }
        else {
            makeError("Expected statement");
            return Node.makeNode(Node.NodeType.Error);
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
        else if (checkForType()) {
            Node declaration = Dcl();
            expect(Token.Type.SEMI);
            return declaration;
        }
        else if (peek().type == Token.Type.IF) {
            return IfStmt();
        }
        else {
            makeError("Expected a call, an assignment, a declaration, or an if statement");
            return Node.makeNode(Node.NodeType.Error);
        }
    }

    // Assignment 	-> 	identifier assign Expr.
    public Node Assignment() throws SyntaxException {

        if (peek().type == Token.Type.IDENTIFIER) {
            Node Assignment = Node.makeNode(Node.NodeType.Assignment, getLineNumber());

            Assignment.addChild(getLeaf(Token.Type.IDENTIFIER));
            expect(Token.Type.ASSIGN);
            Assignment.addChild(Expr());

            return Assignment;
        }
        else {
            makeError("Expected assignment");
            return Node.makeNode(Node.NodeType.Error);
        }
    }

    // Dcls -> Dcl semi Dcls
    //      |  .
    public Node Dcls() throws SyntaxException {
        if (checkForType()) {
            Node declaration = Dcl();
            expect(Token.Type.SEMI);
            Node otherDeclarations = Dcls();

            declaration.makeSiblings(otherDeclarations);

            return declaration;
        }
        else if (peek().type == Token.Type.INITIATE) {
            return Node.makeNode(Node.NodeType.Empty);
        }
        else {
            makeError("Expected declaration or end of declarations");
            return Node.makeNode(Node.NodeType.Error);
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
                return generateDcl(Node.NodeType.FloatDeclaration, Token.Type.FLOAT);

            case INT:
                return generateDcl(Node.NodeType.IntDeclaration, Token.Type.INT);

            case BOOL:
                return generateDcl(Node.NodeType.BoolDeclaration, Token.Type.BOOL);

            case EVENT:
                return generateDcl(Node.NodeType.EventDeclaration, Token.Type.EVENT);

            case PIN:
                return generateDcl(Node.NodeType.PinDeclaration, Token.Type.PIN);

            default:
                makeError("Expected declaration");
                return Node.makeNode(Node.NodeType.Error);
        }
    }

    private Node generateDcl(Node.NodeType nodeType, Token.Type tokenType) throws SyntaxException {
        Node Dcl = Node.makeNode(nodeType, getLineNumber());

        expect(tokenType);
        Dcl.addChild(getLeaf(Token.Type.IDENTIFIER));
        Dcl.addChild(DclAssign());

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
            return Node.makeNode(Node.NodeType.Empty);
        }
        else {
            makeError("Expected assignment or end of statement");
            return Node.makeNode(Node.NodeType.Error);
        }
    }

    // Expr 	-> 	Value AfterExpr
    //	         | 	lparen Expr rparen AfterExpr
    //	         | 	minus Value AfterExpr
    //	         | 	not identifier AfterExpr
    //	         | 	FunctionCall AfterExpr.
    public Node Expr() throws SyntaxException {
        Node Expr = Node.makeNode(Node.NodeType.Expression, getLineNumber());

        if (peek().type == Token.Type.IDENTIFIER || CheckForLiteral()) {
            Node value = Value();
            Node afterExpr = AfterExpr();

            if (afterExpr.Type == Node.NodeType.Empty) {
                return value;
            }

            Expr.addChild(value);
            Expr.addChild(afterExpr);
        }
        else if (peek().type == Token.Type.LPAREN) {
            expect(Token.Type.LPAREN);
            Node expr = Expr();
            expect(Token.Type.RPAREN);
            Node afterExpr = AfterExpr();

            if (afterExpr.Type == Node.NodeType.Empty) {
                return expr;
            }

            Expr.addChild(expr);
            Expr.addChild(afterExpr);
        }
        else if (peek().type == Token.Type.OP_MINUS) {
            expect(Token.Type.OP_MINUS);

            Node prefix = Node.makeNode(Node.NodeType.PrefixMinus);
            Node value = Value();

            value.FirstChild = null;
            value.addChild(prefix);

            Node afterExpr = AfterExpr();

            if (afterExpr.Type == Node.NodeType.Empty) {
                return value;
            }

            Expr.addChild(value);
            Expr.addChild(afterExpr);
        }
        else if (peek().type == Token.Type.OP_NOT) {
            expect(Token.Type.OP_NOT);
            Node prefix = Node.makeNode(Node.NodeType.PrefixNot);
            Node identifier = getLeaf(Token.Type.IDENTIFIER);

            identifier.FirstChild = null;
            identifier.addChild(prefix);

            Node afterExpr = AfterExpr();

            if (afterExpr.Type == Node.NodeType.Empty) {
                return identifier;
            }

            Expr.addChild(identifier);
            Expr.addChild(afterExpr);
        }
        else if (CheckForFunctionCall()) {
            Node call = FunctionCall();
            Node afterExpr = AfterExpr();

            if (afterExpr.Type == Node.NodeType.Empty) {
                return call;
            }

            Expr.addChild(call);
            Expr.addChild(afterExpr);
        }
        else {
            makeError("Expected expression");
            return Node.makeNode(Node.NodeType.Error);
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
                return getLeaf(type);

            default:
                makeError("Expected literal int, float, or bool or an identifier");
                return Node.makeNode(Node.NodeType.Error);
        }
    }

    // AfterExpr 	-> 	Operator Expr
    //	             | 	LogicOp Expr
    //	             | 	.
    public Node AfterExpr() throws SyntaxException {

        if (CheckForOperator()) {
            Node operator = Operator();
            Node expr = Expr();

            operator.makeSiblings(expr);

            return operator;
        }

        else if (CheckForLogicOperator()) {
            Node operator = LogicOperator();
            Node expr = Expr();

            operator.makeSiblings(expr);

            return operator;
        }
        else if (peek().type == Token.Type.SEMI || peek().type == Token.Type.RPAREN) {
            return Node.makeNode(Node.NodeType.Empty);
        }
        else {
            makeError("Expected operator or end of statement");
            return Node.makeNode(Node.NodeType.Error);
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
            return Node.makeNode(Node.NodeType.Error);
        }
    }

    // ProcedureCall	->	broadcast lparen identifier rparen
    //	                 |	write lparen identifier comma Expr rparen.
    public Node ProcedureCall() throws SyntaxException {
        Node ProcedureCall = Node.makeNode(Node.NodeType.Call, getLineNumber());

        if (peek().type == Token.Type.BROADCAST) {
            expect(Token.Type.BROADCAST);
            ProcedureCall.addChild(Node.makeNode(Node.NodeType.Broadcast));

            expect(Token.Type.LPAREN);
            ProcedureCall.addChild(getLeaf(Token.Type.IDENTIFIER));
            expect(Token.Type.RPAREN);
        }
        else if (peek().type == Token.Type.WRITE) {
            expect(Token.Type.WRITE);
            ProcedureCall.addChild(Node.makeNode(Node.NodeType.Write));

            expect(Token.Type.LPAREN);
            ProcedureCall.addChild(getLeaf(Token.Type.IDENTIFIER));
            expect(Token.Type.COMMA);
            ProcedureCall.addChild(Expr());
            expect(Token.Type.RPAREN);
        }
        else {
            makeError("Expected write or broadcast");
            return Node.makeNode(Node.NodeType.Error);
        }

        return ProcedureCall;
    }

    // FunctionCall	->  filterNoise lparen identifier comma FilterType rparen
    //	             | 	getValue lparen identifier rparen
    //	             |  createEvent lparen Expr rparen
    //	             |  createPin lparen PinType comma IOType comma intLiteral rparen.
    public Node FunctionCall() throws SyntaxException {
        Node FunctionCall = Node.makeNode(Node.NodeType.Call, getLineNumber());

        if (peek().type == Token.Type.FILTERNOISE) {
            expect(Token.Type.FILTERNOISE);
            FunctionCall.addChild(Node.makeNode(Node.NodeType.FilterNoise));

            expect(Token.Type.LPAREN);
            FunctionCall.addChild(getLeaf(Token.Type.IDENTIFIER));
            expect(Token.Type.COMMA);
            FunctionCall.addChild(FilterType());
            expect(Token.Type.RPAREN);
        }
        else if (peek().type == Token.Type.GETVALUE) {
            expect(Token.Type.GETVALUE);
            FunctionCall.addChild(Node.makeNode(Node.NodeType.GetValue));

            expect(Token.Type.LPAREN);
            FunctionCall.addChild(getLeaf(Token.Type.IDENTIFIER));
            expect(Token.Type.RPAREN);
        }
        else if (peek().type == Token.Type.CREATEEVENT) {
            expect(Token.Type.CREATEEVENT);
            FunctionCall.addChild(Node.makeNode(Node.NodeType.CreateEvent));

            expect(Token.Type.LPAREN);
            FunctionCall.addChild(Expr());
            expect(Token.Type.RPAREN);
        }
        else if (peek().type == Token.Type.CREATEPIN) {
            expect(Token.Type.CREATEPIN);
            FunctionCall.addChild(Node.makeNode(Node.NodeType.CreatePin));

            expect(Token.Type.LPAREN);
            FunctionCall.addChild(PinType());
            expect(Token.Type.COMMA);
            FunctionCall.addChild(IOType());
            expect(Token.Type.COMMA);

            // Support for analog pins (A0 - A15);
            if (peek().type == Token.Type.LIT_Int) {
                FunctionCall.addChild(getLeaf(Token.Type.LIT_Int));
            }
            else if (peek().type == Token.Type.IDENTIFIER) {
                FunctionCall.addChild(getLeaf(Token.Type.IDENTIFIER));
            }
            else {
                makeError("Expected analog or digital pin number");
            }

            expect(Token.Type.RPAREN);
        }
        else {
            makeError("Expected filterNoise, getValue, createEvent, or createPin");
            return Node.makeNode(Node.NodeType.Error);
        }

        return FunctionCall;
    }

    // IfStmt 	-> 	if lparen Expr rparen Block IfEnd.
    public Node IfStmt() throws SyntaxException {

        if (peek().type == Token.Type.IF) {
            Node IfStmt = Node.makeNode(Node.NodeType.If, getLineNumber());

            expect(Token.Type.IF);
            expect(Token.Type.LPAREN);
            IfStmt.addChild(Expr());
            expect(Token.Type.RPAREN);
            IfStmt.addChild(Block());
            IfStmt.addChild(IfEnd());

            return IfStmt;
        }
        else {
            makeError("Expected if");
            return Node.makeNode(Node.NodeType.Error);
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
                 checkForType() ||
                 CheckForCall()) {
            return Node.makeNode(Node.NodeType.Empty);
        }
        else {
            makeError("Expected else or end of if statement");
            return Node.makeNode(Node.NodeType.Error);
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
            return Node.makeNode(Node.NodeType.Error);
        }
    }

    // FilterType -> 	debounce
    //	           | 	constant
    //	           | 	range.
    public Node FilterType() throws SyntaxException {

        switch (peek().type) {
            case DEBOUNCE:
                expect(Token.Type.DEBOUNCE);
                return Node.makeNode(Node.NodeType.Debounce);

            case CONSTANT:
                expect(Token.Type.CONSTANT);
                return Node.makeNode(Node.NodeType.Constant);

            case RANGE:
                expect(Token.Type.RANGE);
                return Node.makeNode(Node.NodeType.Range);

            default:
                makeError("Expected debounce, constant, or range");
                return Node.makeNode(Node.NodeType.Error);
        }
    }

    // PinType 	-> 	digital
    //	         | 	analog
    //	         | 	pwm.
    public Node PinType() throws SyntaxException {

        switch (peek().type) {
            case DIGITAL:
                expect(Token.Type.DIGITAL);
                return Node.makeNode(Node.NodeType.Digital);

            case ANALOG:
                expect(Token.Type.ANALOG);
                return Node.makeNode(Node.NodeType.Analog);

            case PWM:
                expect(Token.Type.PWM);
                return Node.makeNode(Node.NodeType.PWM);

            default:
                makeError("Expected digital, analog, or pwm");
                return Node.makeNode(Node.NodeType.Error);
        }
    }

    // IOType 	-> 	input
    //	         | 	output.
    public Node IOType() throws SyntaxException {

        switch (peek().type) {
            case INPUT:
                expect(Token.Type.INPUT);
                return Node.makeNode(Node.NodeType.Input);

            case OUTPUT:
                expect(Token.Type.OUTPUT);
                return Node.makeNode(Node.NodeType.Output);

            default:
                makeError("Expected input or output");
                return Node.makeNode(Node.NodeType.Error);
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
                return Node.makeNode(Node.NodeType.Plus);

            case OP_MINUS:
                expect(Token.Type.OP_MINUS);
                return Node.makeNode(Node.NodeType.Minus);

            case OP_TIMES:
                expect(Token.Type.OP_TIMES);
                return Node.makeNode(Node.NodeType.Times);

            case OP_DIVIDE:
                expect(Token.Type.OP_DIVIDE);
                return Node.makeNode(Node.NodeType.Divide);

            case OP_MODULO:
                expect(Token.Type.OP_MODULO);
                return Node.makeNode(Node.NodeType.Modulo);

            default:
                makeError("Expected +, -, *, /, or %");
                return Node.makeNode(Node.NodeType.Error);
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
                return Node.makeNode(Node.NodeType.LessThan);

            case LOP_GREATERTHAN:
                expect(Token.Type.LOP_GREATERTHAN);
                return Node.makeNode(Node.NodeType.GreaterThan);

            case LOP_NOTEQUAL:
                expect(Token.Type.LOP_NOTEQUAL);
                return Node.makeNode(Node.NodeType.NotEqual);

            case LOP_GREATEROREQUAL:
                expect(Token.Type.LOP_GREATEROREQUAL);
                return Node.makeNode(Node.NodeType.GreaterOrEqual);

            case LOP_LESSOREQUAL:
                expect(Token.Type.LOP_LESSOREQUAL);
                return Node.makeNode(Node.NodeType.LessOrEqual);

            case LOP_EQUALS:
                expect(Token.Type.LOP_EQUALS);
                return Node.makeNode(Node.NodeType.Equals);

            case LOP_AND:
                expect(Token.Type.LOP_AND);
                return Node.makeNode(Node.NodeType.And);

            case LOP_OR:
                expect(Token.Type.LOP_OR);
                return Node.makeNode(Node.NodeType.Or);

            default:
                makeError("Expected <, >, !=, >=, <=, ==, ||, or &&");
                return Node.makeNode(Node.NodeType.Error);
        }
    }

    private Node getLeaf(Token.Type type) throws SyntaxException {
        int line = getLineNumber();
        Node node = Node.makeNode(expect(type), line);

        addEmpty(node);

        return node;
    }

    private void addEmpty(Node node) {
        if (node.FirstChild == null) {
            node.addChild(Node.makeNode(Node.NodeType.Empty));
        }
    }

    private boolean checkForType() {

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
