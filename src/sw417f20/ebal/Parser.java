package sw417f20.ebal;

public class Parser extends RecursiveDescent{

    // Start 	-> 	Master Slave Slaves.
    @Override
    public Node Start() {
        Node Prog = AST.MakeNode(AST.NodeType.Prog);

        Prog.AddChild(Master());
        Prog.AddChild(Slave());
        Prog.AddChild(Slaves());

        return Prog;
    }

    // Master 	-> 	begin master Initiate Listeners end master.
    private Node Master() {

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
    private Node Slaves() {

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

    // Slave 	-> 	begin slave Initiate EventHandlers end slave.
    private Node Slave() {

        if (Peek().type == Token.Type.BEGIN) {
            Node Slave = AST.MakeNode(AST.NodeType.Slave);

            Expect(Token.Type.BEGIN);
            Expect(Token.Type.SLAVE);

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

    // Initiate 	-> 	initiate lbracket PinDcls rbracket.
    private Node Initiate() {

        if (Peek().type == Token.Type.INITIATE) {

            Node Initiate = AST.MakeNode(AST.NodeType.Initiate);

            Expect(Token.Type.INITIATE);
            Expect(Token.Type.LBRACKET);
            Initiate.AddChild(PinDcls());
            Expect(Token.Type.RBRACKET);

            return Initiate;
        }
        else {
            MakeError("Expected Initiate");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // PinDcls 	-> 	PinDcl semi PinDcls
    //	         | 	.
    private Node PinDcls() {

        if (Peek().type == Token.Type.PIN) {
            Node pinDcl = PinDcl();
            Expect(Token.Type.SEMI);
            Node otherPinDcls = PinDcls();

            pinDcl.MakeSiblings(otherPinDcls);

            return pinDcl;
        }
        else if (Peek().type == Token.Type.RBRACKET) {
            return AST.MakeNode(AST.NodeType.Empty);
        }
        else {
            MakeError("Expected pin or }");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // PinDcl	-> pin identifier assign createPin lparen PinType comma IOType comma intLiteral rparen.
    private Node PinDcl() {

        if (Peek().type == Token.Type.PIN) {
            Node PinDcl = AST.MakeNode(AST.NodeType.PinDeclaration);

            Expect(Token.Type.PIN);

            PinDcl.AddChild(AST.MakeNode(Expect(Token.Type.IDENTIFIER)));

            Expect(Token.Type.ASSIGN);
            Expect(Token.Type.CREATEPIN);
            Expect(Token.Type.LPAREN);
            PinDcl.AddChild(PinType());
            Expect(Token.Type.COMMA);
            PinDcl.AddChild(IOType());
            Expect(Token.Type.COMMA);

            PinDcl.AddChild(AST.MakeNode(Expect(Token.Type.LIT_Int)));

            Expect(Token.Type.RPAREN);

            return PinDcl;
        }
        else {
            MakeError("Expected pin");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // PinType 	-> 	digital
    //	         | 	analog
    //	         | 	pwm.
    private Node PinType() {

        if (Peek().type == Token.Type.DIGITAL) {
            return AST.MakeNode(Expect(Token.Type.DIGITAL));
        }
        else if (Peek().type == Token.Type.ANALOG) {
            return AST.MakeNode(Expect(Token.Type.ANALOG));
        }
        else if (Peek().type == Token.Type.PWM) {
            return AST.MakeNode(Expect(Token.Type.PWM));
        }
        else {
            MakeError("Expected digital, analog, or pwm");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // IOType 	-> 	input
    //	         | 	output.
    private Node IOType() {

        if (Peek().type == Token.Type.INPUT) {
            return AST.MakeNode(Expect(Token.Type.INPUT));
        }
        else if (Peek().type == Token.Type.OUTPUT) {
            return AST.MakeNode(Expect(Token.Type.OUTPUT));
        }
        else {
            MakeError("Expected input or output");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // Listeners 	-> 	Listener Listeners
    //	             | 	.
    private Node Listeners() {

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
    private Node Listener() {

        if (Peek().type == Token.Type.LISTENER) {
            Node Listener = AST.MakeNode(AST.NodeType.Listener);

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
    private Node EventHandlers() {

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
    private Node EventHandler() {

        if (Peek().type == Token.Type.EVENTHANDLER) {
            Node EventHandler = AST.MakeNode(AST.NodeType.EventHandler);

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
    private Node Block() {

        if (Peek().type == Token.Type.LBRACKET) {
            Node Block = AST.MakeNode(AST.NodeType.Block);

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
    private Node Stmts() {

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
    private Node Stmt() {

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
            MakeError("Expected a call, an assignment, or an if-statement");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // Assignment 	-> 	identifier assign Expr.
    private Node Assignment() {

        if (Peek().type == Token.Type.IDENTIFIER) {
            Node Assignment = AST.MakeNode(AST.NodeType.Assignment);

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
    //	     | 	event identifier DclAssign.
    private Node Dcl() {
        Node Dcl = AST.MakeNode(AST.NodeType.Declaration);

        if (Peek().type == Token.Type.FLOAT) {
            Dcl.AddChild(AST.MakeNode(Expect(Token.Type.FLOAT)));
            Dcl.AddChild(AST.MakeNode(Expect(Token.Type.IDENTIFIER)));
            Dcl.AddChild(DclAssign());
        }
        else if (Peek().type == Token.Type.INT) {
            Dcl.AddChild(AST.MakeNode(Expect(Token.Type.INT)));
            Dcl.AddChild(AST.MakeNode(Expect(Token.Type.IDENTIFIER)));
            Dcl.AddChild(DclAssign());
        }
        else if (Peek().type == Token.Type.BOOL) {
            Dcl.AddChild(AST.MakeNode(Expect(Token.Type.BOOL)));
            Dcl.AddChild(AST.MakeNode(Expect(Token.Type.IDENTIFIER)));
            Dcl.AddChild(DclAssign());
        }
        else if (Peek().type == Token.Type.EVENT) {
            Dcl.AddChild(AST.MakeNode(Expect(Token.Type.EVENT)));
            Dcl.AddChild(AST.MakeNode(Expect(Token.Type.IDENTIFIER)));
            Dcl.AddChild(DclAssign());
        }
        else if (Peek().type == Token.Type.PIN) {
            MakeError("Pins can only be declared in Initiate");
            return AST.MakeNode(AST.NodeType.Error);
        }
        else {
            MakeError("Expected float, int, bool, or event declaration");
            return AST.MakeNode(AST.NodeType.Error);
        }

        return Dcl;
    }

    // DclAssign	-> assign Expr
    //	             | .
    private Node DclAssign() {
        if (Peek().type == Token.Type.ASSIGN) {
            Expect(Token.Type.ASSIGN);
            return Expr();
        }
        else if (Peek().type == Token.Type.SEMI) {
            return AST.MakeNode(AST.NodeType.Empty);
        }
        else {
            MakeError("Expected assignment or ;");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // Expr 	-> 	Value AfterValue
    //	         | 	lparen Expr rparen
    //	         | 	minus Value AfterValue
    //	         | 	not identifier AfterValue
    //	         | 	ReturnsCall.
    private Node Expr() {
        Node Expr = AST.MakeNode(AST.NodeType.Expression);

        if (Peek().type == Token.Type.IDENTIFIER || CheckForLiteral()) {
            Node value = Value();
            Node afterValue = AfterValue();

            if (afterValue.Type == AST.NodeType.Empty) {
                return value;
            }

            Expr.AddChild(value);
            Expr.AddChild(afterValue);
        }
        // TODO: Er ikke sikker på den her
        else if (Peek().type == Token.Type.LPAREN) {
            Expect(Token.Type.LPAREN);
            Node expr = Expr();
            Expect(Token.Type.RPAREN);
            return expr;
        }
        else if (Peek().type == Token.Type.OP_MINUS) {
            Expect(Token.Type.OP_MINUS);

            // TODO: Hotfix, bør være med MakeNode!
            Node prefix = new Node(AST.NodeType.Prefix, "-");

            Node value = Value();
            Node afterValue = AfterValue();

            if (afterValue.Type == AST.NodeType.Empty) {
                return value;
            }

            Expr.AddChild(value);
            Expr.AddChild(afterValue);
        }
        else if (Peek().type == Token.Type.OP_NOT) {
            Expect(Token.Type.OP_NOT);
            Node identifier = AST.MakeNode(Expect(Token.Type.IDENTIFIER));
            Node afterValue = AfterValue();

            if (afterValue.Type == AST.NodeType.Empty) {
                return identifier;
            }

            Expr.AddChild(identifier);
            Expr.AddChild(afterValue);
        }
        else if (Peek().type == Token.Type.FILTERNOISE ||
                 Peek().type == Token.Type.GETVALUE ||
                 Peek().type == Token.Type.CREATEEVENT) {
            return ReturnsCall();
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
    private Node Value() {

        if (Peek().type == Token.Type.LIT_Int) {
            return AST.MakeNode(Expect(Token.Type.LIT_Int));
        }
        else if (Peek().type == Token.Type.LIT_Float) {
            return AST.MakeNode(Expect(Token.Type.LIT_Float));
        }
        else if (Peek().type == Token.Type.LIT_Bool) {
            return AST.MakeNode(Expect(Token.Type.LIT_Bool));
        }
        else if (Peek().type == Token.Type.IDENTIFIER) {
            return AST.MakeNode(Expect(Token.Type.IDENTIFIER));
        }
        else {
            MakeError("Expected literal int, float, or bool or an identifier");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // AfterValue 	-> 	Operator Expr
    //	             | 	LogicOp Expr
    //	             | 	.
    private Node AfterValue() {

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
        else if (Peek().type == Token.Type.SEMI ||
                 Peek().type == Token.Type.RPAREN) {
            return AST.MakeNode(AST.NodeType.Empty);
        }
        else {
            MakeError("Expected operator or end of statement");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }


    // Call 	-> 	VoidCall
    //         	 | 	ReturnsCall.
    private Node Call() {

        if (Peek().type == Token.Type.BROADCAST ||
            Peek().type == Token.Type.WRITE) {
            return VoidCall();
        }
        else if (Peek().type == Token.Type.FILTERNOISE ||
                 Peek().type == Token.Type.GETVALUE    ||
                 Peek().type == Token.Type.CREATEEVENT) {
            return ReturnsCall();
        }
        else {
            MakeError("Expected function call");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // VoidCall	->	broadcast lparen identifier rparen
    //	         |	write lparen identifier comma Expr rparen.
    private Node VoidCall() {
        Node VoidCall = AST.MakeNode(AST.NodeType.Call);
        VoidCall.AddChild(AST.MakeNode(AST.NodeType.Function));

        if (Peek().type == Token.Type.BROADCAST) {
            VoidCall.FirstChild.AddChild(AST.MakeNode(Expect(Token.Type.BROADCAST)));
            VoidCall.FirstChild.AddChild(AST.MakeNode(AST.NodeType.Returns));

            Expect(Token.Type.LPAREN);
            VoidCall.AddChild(AST.MakeNode(Expect(Token.Type.IDENTIFIER)));
            Expect(Token.Type.RPAREN);
        }
        else if (Peek().type == Token.Type.WRITE) {
            VoidCall.FirstChild.AddChild(AST.MakeNode(Expect(Token.Type.WRITE)));
            VoidCall.FirstChild.AddChild(AST.MakeNode(AST.NodeType.Returns));

            Expect(Token.Type.LPAREN);
            VoidCall.AddChild(AST.MakeNode(Expect(Token.Type.IDENTIFIER)));
            Expect(Token.Type.COMMA);
            VoidCall.AddChild(Expr());
            Expect(Token.Type.RPAREN);
        }
        else {
            MakeError("Expected write or broadcast");
            return AST.MakeNode(AST.NodeType.Error);
        }

        return VoidCall;
    }

    // ReturnsCall	-> filterNoise lparen identifier comma FilterType rparen
    //	             | 	getValue lparen Value rparen
    //	             |  createEvent lparen Value rparen.
    private Node ReturnsCall() {
        Node ReturnsCall = AST.MakeNode(AST.NodeType.Call);
        ReturnsCall.AddChild(AST.MakeNode(AST.NodeType.Function));

        if (Peek().type == Token.Type.FILTERNOISE) {
            ReturnsCall.FirstChild.AddChild(AST.MakeNode(Expect(Token.Type.FILTERNOISE)));
            ReturnsCall.FirstChild.AddChild(AST.MakeNode(AST.NodeType.Returns));

            Expect(Token.Type.LPAREN);
            ReturnsCall.AddChild(AST.MakeNode(Expect(Token.Type.IDENTIFIER)));
            Expect(Token.Type.COMMA);
            ReturnsCall.AddChild(FilterType());
            Expect(Token.Type.RPAREN);
        }
        else if (Peek().type == Token.Type.GETVALUE) {
            ReturnsCall.FirstChild.AddChild(AST.MakeNode(Expect(Token.Type.GETVALUE)));
            ReturnsCall.FirstChild.AddChild(AST.MakeNode(AST.NodeType.Returns));

            Expect(Token.Type.LPAREN);
            ReturnsCall.AddChild(Value());
            Expect(Token.Type.RPAREN);
        }
        else if (Peek().type == Token.Type.CREATEEVENT) {
            ReturnsCall.FirstChild.AddChild(AST.MakeNode(Expect(Token.Type.CREATEEVENT)));
            ReturnsCall.FirstChild.AddChild(AST.MakeNode(AST.NodeType.Returns));

            Expect(Token.Type.LPAREN);
            ReturnsCall.AddChild(Value());
            Expect(Token.Type.RPAREN);
        }
        else {
            MakeError("Expected filterNoise, getValue, or createEvent");
            return AST.MakeNode(AST.NodeType.Error);
        }

        return ReturnsCall;
    }

    // IfStmt 	-> 	if lparen Expr rparen Block IfEnd.
    private Node IfStmt() {

        if (Peek().type == Token.Type.IF) {
            Node IfStmt = AST.MakeNode(AST.NodeType.If);

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
    private Node IfEnd() {

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
    private Node AfterElse() {

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
    private Node FilterType() {

        if (Peek().type == Token.Type.FLIP) {
            return AST.MakeNode(Expect(Token.Type.FLIP));
        }
        else if (Peek().type == Token.Type.CONSTANT) {
            return AST.MakeNode(Expect(Token.Type.CONSTANT));
        }
        else if (Peek().type == Token.Type.RANGE) {
            return AST.MakeNode(Expect(Token.Type.RANGE));
        }
        else {
            MakeError("Expected FLIP, CONSTANT, or RANGE");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // Operator 	-> 	plus
    //	             | 	minus
    //	             | 	times
    //	             | 	divide
    //	             | 	modulo.
    private Node Operator() {

        if (Peek().type == Token.Type.OP_PLUS) {
            return AST.MakeNode(Expect(Token.Type.OP_PLUS));
        }
        else if (Peek().type == Token.Type.OP_MINUS) {
            return AST.MakeNode(Expect(Token.Type.OP_MINUS));
        }
        else if (Peek().type == Token.Type.OP_TIMES) {
            return AST.MakeNode(Expect(Token.Type.OP_TIMES));
        }
        else if (Peek().type == Token.Type.OP_DIVIDE) {
            return AST.MakeNode(Expect(Token.Type.OP_DIVIDE));
        }
        else if (Peek().type == Token.Type.OP_MODULO) {
            return AST.MakeNode(Expect(Token.Type.OP_MODULO));
        }
        else {
            MakeError("Expected +, -, *, /, or %");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // LogicOp 	-> lessThan
    //	         | 	greaterThan
    //	         | 	notEqual
    //	         | 	greaterOrEqual
    //	         | 	lessOrEqual
    //	         | 	equals.
    private Node LogicOperator() {

        if (Peek().type == Token.Type.LOP_LESSTHAN) {
            return AST.MakeNode(Expect(Token.Type.LOP_LESSTHAN));
        }
        else if (Peek().type == Token.Type.LOP_GREATERTHAN) {
            return AST.MakeNode(Expect(Token.Type.LOP_GREATERTHAN));
        }
        else if (Peek().type == Token.Type.LOP_NOTEQUAL) {
            return AST.MakeNode(Expect(Token.Type.LOP_NOTEQUAL));
        }
        else if (Peek().type == Token.Type.LOP_GREATEROREQUAL) {
            return AST.MakeNode(Expect(Token.Type.LOP_GREATEROREQUAL));
        }
        else if (Peek().type == Token.Type.LOP_LESSOREQUAL) {
            return AST.MakeNode(Expect(Token.Type.LOP_LESSOREQUAL));
        }
        else if (Peek().type == Token.Type.LOP_EQUALS) {
            return AST.MakeNode(Expect(Token.Type.LOP_EQUALS));
        }
        else {
            MakeError("Expected <, >, !=, >=, <=, or ==");
            return AST.MakeNode(AST.NodeType.Error);
        }

    }

    private boolean CheckForType() {
        return  Peek().type == Token.Type.FLOAT                 ||
                Peek().type == Token.Type.INT                   ||
                Peek().type == Token.Type.BOOL                  ||
                Peek().type == Token.Type.EVENT;
    }

    private boolean CheckForCall() {
        return  Peek().type == Token.Type.FILTERNOISE           ||
                Peek().type == Token.Type.GETVALUE              ||
                Peek().type == Token.Type.BROADCAST             ||
                Peek().type == Token.Type.WRITE                 ||
                Peek().type == Token.Type.CREATEEVENT;
    }

    private boolean CheckForExpr() {
        return  Peek().type == Token.Type.IDENTIFIER            ||
                CheckForLiteral()                               ||
                Peek().type == Token.Type.OP_MINUS              ||
                Peek().type == Token.Type.OP_NOT                ||
                Peek().type == Token.Type.LPAREN;
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
                Peek().type == Token.Type.LOP_EQUALS;
    }
}
