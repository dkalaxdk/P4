package sw417f20.ebal;

public class Parser extends RecursiveDescent{

    // Start 	-> 	Master Slave Slaves.
    @Override
    public Node Start() {
        Node Start = AST.MakeNode("Start");

        Start.AddChild(Master());
        Start.AddChild(Slave());
        Start.AddChild(Slaves());

        return Start;
    }

    // Master 	-> 	begin master Initiate Listeners end master.
    private Node Master() {
        Node Master = AST.MakeNode("Master");

        if (Peek().type == Token.Type.BEGIN) {
            Expect(Token.Type.BEGIN);
            Expect(Token.Type.MASTER);

            Master.AddChild(Initiate());
            Master.AddChild(Listeners());

            Expect(Token.Type.END);
            Expect(Token.Type.MASTER);
        }
        else {
            MakeError("Expected BEGIN");
        }

        return Master;
    }

    // Slaves 	-> 	Slave Slaves
    //	         | 	.
    private Node Slaves() {
        Node Slaves = AST.MakeNode("Slaves");

        if (Peek().type == Token.Type.BEGIN) {
            Slaves.AddChild(Slave());
            Slaves.AddChild(Slaves());
        }
        else if (Peek().type == Token.Type.EOF) {
            return AST.MakeNode();
        }
        else {
            MakeError("Expected BEGIN or EOF");
        }

        return Slaves;
    }

    // Slave 	-> 	begin slave Initiate EventHandlers end slave.
    private Node Slave() {
        Node Slave = AST.MakeNode("Slave");

        if (Peek().type == Token.Type.BEGIN) {
            Expect(Token.Type.BEGIN);
            Expect(Token.Type.SLAVE);

            Slave.AddChild(Initiate());
            Slave.AddChild(EventHandlers());

            Expect(Token.Type.END);
            Expect(Token.Type.SLAVE);
        }
        else {
            MakeError("Expected BEGIN");
        }

        return Slave;
    }

    // Initiate 	-> 	initiate lbracket PinDcls rbracket.
    private Node Initiate() {
        Node Initiate = AST.MakeNode("Initiate");

        if (Peek().type == Token.Type.INITIATE) {
            Expect(Token.Type.INITIATE);
            Expect(Token.Type.LBRACKET);
            Initiate.AddChild(PinDcls());
            Expect(Token.Type.RBRACKET);
        }
        else {
            MakeError("Expected Initiate");
        }

        return Initiate;
    }

    // PinDcls 	-> 	PinDcl semi PinDcls
    //	         | 	.
    private Node PinDcls() {
        Node PinDcls = AST.MakeNode("PinDcls");

        if (Peek().type == Token.Type.PIN) {
            PinDcls.AddChild(PinDcl());
            Expect(Token.Type.SEMI);
            PinDcls.AddChild(PinDcls());
        }
        else if (Peek().type == Token.Type.RBRACKET) {
            return AST.MakeNode();
        }
        else {
            MakeError("Expected pin or }");
        }

        return PinDcls;
    }

    // PinDcl	-> pin identifier assign createPin lparen PinType comma IOType comma intLiteral rparen.
    private Node PinDcl() {
        Node PinDcl = AST.MakeNode("PinDcl");

        if (Peek().type == Token.Type.PIN) {
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
        }
        else {
            MakeError("Expected pin");
        }

        return PinDcl;
    }

    // PinType 	-> 	digital
    //	         | 	analog
    //	         | 	pwm.
    private Node PinType() {
        Node PinType = AST.MakeNode("PinType");

        if (Peek().type == Token.Type.DIGITAL) {
            PinType.NodeToken = Expect(Token.Type.DIGITAL);
        }
        else if (Peek().type == Token.Type.ANALOG) {
            PinType.NodeToken = Expect(Token.Type.ANALOG);
        }
        else if (Peek().type == Token.Type.PWM) {
            PinType.NodeToken = Expect(Token.Type.PWM);
        }
        else {
            MakeError("Expected digital, analog, or pwm");
        }

        return PinType;
    }

    // IOType 	-> 	input
    //	         | 	output.
    private Node IOType() {
        Node IOType = AST.MakeNode("IOType");

        if (Peek().type == Token.Type.INPUT) {
            IOType.NodeToken = Expect(Token.Type.INPUT);
        }
        else if (Peek().type == Token.Type.OUTPUT) {
            IOType.NodeToken = Expect(Token.Type.OUTPUT);
        }
        else {
            MakeError("Expected input or output");
        }

        return IOType;
    }

    // Listeners 	-> 	Listener Listeners
    //	             | 	.
    private Node Listeners() {
        Node Listeners = AST.MakeNode("Listeners");

        if (Peek().type == Token.Type.LISTENER) {
            Listeners.AddChild(Listener());
            Listeners.AddChild(Listeners());
        }
        else if (Peek().type == Token.Type.END) {
            return AST.MakeNode();
        }
        else if (Peek().type == Token.Type.EVENTHANDLER) {
            MakeError("EventHandlers can only be declared in slaves");
        }
        else {
            MakeError("Expected Listener or end of master");
        }

        return Listeners;
    }

    // Listener	-> listener lparen identifier rparen Block.
    private Node Listener() {
        Node Listener = AST.MakeNode("Listener");

        if (Peek().type == Token.Type.LISTENER) {
            Expect(Token.Type.LISTENER);
            Expect(Token.Type.LPAREN);
            Listener.AddChild(AST.MakeNode(Expect(Token.Type.IDENTIFIER)));
            Expect(Token.Type.RPAREN);
            Listener.AddChild(Block());
        }
        else {
            MakeError("Expected Listener");
        }

        return Listener;
    }

    // EventHandlers -> 	EventHandler EventHandlers
    //	              | 	.
    private Node EventHandlers() {
        Node EventHandlers = AST.MakeNode("EventHandlers");

        if (Peek().type == Token.Type.EVENTHANDLER) {
            EventHandlers.AddChild(EventHandler());
            EventHandlers.AddChild(EventHandlers());
        }
        else if (Peek().type == Token.Type.END) {
            return AST.MakeNode();
        }
        else if (Peek().type == Token.Type.LISTENER) {
            MakeError("Listeners can only be declared in master");
        }
        else {
            MakeError("Expected EventHandler or end of slave");
        }

        return EventHandlers;
    }

    // EventHandler -> eventHandler lparen identifier rparen Block.
    private Node EventHandler() {
        Node EventHandler = AST.MakeNode();
        EventHandler.NodeName = "EventHandler";

        if (Peek().type == Token.Type.EVENTHANDLER) {
            Expect(Token.Type.EVENTHANDLER);
            Expect(Token.Type.LPAREN);
            EventHandler.AddChild(AST.MakeNode(Expect(Token.Type.IDENTIFIER)));
            Expect(Token.Type.RPAREN);
            EventHandler.AddChild(Block());
        }
        else {
            MakeError("Expected EventHandler");
        }

        return EventHandler;
    }

    // Block 	-> lbracket Stmts rbracket.
    private Node Block() {
        Node Block = AST.MakeNode("Block");

        if (Peek().type == Token.Type.LBRACKET) {
            Expect(Token.Type.LBRACKET);
            Block.AddChild(Stmts());
            Expect(Token.Type.RBRACKET);
        }
        else {
            MakeError("Expected {");
        }

        return Block;
    }

    // Stmts 	-> 	Stmt Stmts
    //	         | 	.
    private Node Stmts() {
        Node Stmts = AST.MakeNode("Stmts");

        if (Peek().type == Token.Type.IDENTIFIER ||
            Peek().type == Token.Type.IF ||
            CheckForType() ||
            CheckForCall()) {

            Stmts.AddChild(Stmt());
            Stmts.AddChild(Stmts());
        }
        else if (Peek().type == Token.Type.RBRACKET) {
            return AST.MakeNode();
        }
        else {
            MakeError("Expected statement");
        }

        return Stmts;
    }


    // Stmt 	-> 	Call semi
    //	         | 	Assignment semi
    //	         | 	IfStmt.
    private Node Stmt() {
        Node Stmt = AST.MakeNode("Stmt");

        if (CheckForCall()) {
            Stmt.AddChild(Call());
            Expect(Token.Type.SEMI);
        }
        else if (Peek().type == Token.Type.IDENTIFIER || CheckForType()) {
            Stmt.AddChild(Assignment());
            Expect(Token.Type.SEMI);
        }
        else if (Peek().type == Token.Type.IF) {
            Stmt.AddChild(IfStmt());
        }
        else {
            MakeError("Expected a call, an assignment, or an if-statement");
        }

        return Stmt;
    }

    // Assignment 	-> 	identifier assign AfterAssign
    //	             | 	Dcl assign AfterAssign .
    private Node Assignment() {
        Node Assignment = AST.MakeNode("Assignment");

        if (Peek().type == Token.Type.IDENTIFIER) {
            Expect(Token.Type.IDENTIFIER);
            Expect(Token.Type.ASSIGN);
            AfterAssign();
        }
        else if (CheckForType()) {
            Dcl();
            Expect(Token.Type.ASSIGN);
            AfterAssign();
        }
        else {
            MakeError("Expected assignment or declaration");
        }

        return Assignment;
    }

    // Dcl 	-> 	float identifier
    //	     | 	int identifier
    //	     | 	bool identifier
    //	     | 	event identifier.
    private Node Dcl() {
        Node Dcl = AST.MakeNode("Dcl");
        Node type = AST.MakeNode("Type");
        Node id = AST.MakeNode("Identifier");

        if (Peek().type == Token.Type.FLOAT) {
            type.NodeToken = Expect(Token.Type.FLOAT);
            id.NodeToken = Expect(Token.Type.IDENTIFIER);
        }
        else if (Peek().type == Token.Type.INT) {
            type.NodeToken = Expect(Token.Type.INT);
            id.NodeToken = Expect(Token.Type.IDENTIFIER);
        }
        else if (Peek().type == Token.Type.BOOL) {
            Expect(Token.Type.BOOL);
            Expect(Token.Type.IDENTIFIER);
        }
        else if (Peek().type == Token.Type.EVENT) {
            Expect(Token.Type.EVENT);
            Expect(Token.Type.IDENTIFIER);
        }
        else if (Peek().type == Token.Type.PIN) {
            MakeError("Pins can only be declared in Initiate");
        }
        else {
            MakeError("Expected float, int, bool, or event declaration");
        }

        Dcl.AddChild(type);
        Dcl.AddChild(id);

        return Dcl;
    }

    // TODO: Implementer min grammar (lambda)
    // AfterAssign      ->  Expr
    //	                 | 	ReturnsCall
    //	                 | .
    private Node AfterAssign() {
        Node AfterDcl = AST.MakeNode();

        if (CheckForExpr()) {
            Expr();
        }
        else if (Peek().type == Token.Type.FILTERNOISE ||
                 Peek().type == Token.Type.GETVALUE    ||
                 Peek().type == Token.Type.CREATEEVENT) {
            ReturnsCall();
        }

        return AfterDcl;
    }

    // Expr 	-> 	Value AfterValue
    //	         | 	lparen Expr rparen
    //	         | 	minus Value AfterValue
    //	         | 	not boolLiteral AfterValue.
    private Node Expr() {
        Node Expr = AST.MakeNode();

        if (Peek().type == Token.Type.IDENTIFIER || CheckForLiteral()) {
            Value();
            AfterValue();
        }
        else if (Peek().type == Token.Type.LPAREN) {
            Expect(Token.Type.LPAREN);
            Expr();
            Expect(Token.Type.RPAREN);
        }
        else if (Peek().type == Token.Type.OP_MINUS) {
            Expect(Token.Type.OP_MINUS);
            Value();
            AfterValue();
        }
        else if (Peek().type == Token.Type.OP_NOT) {
            Expect(Token.Type.OP_NOT);
            Expect(Token.Type.LIT_Bool);
            AfterValue();
        }
        else {
            MakeError("Expected expression");
        }

        return Expr;
    }

    // Value 	-> 	intLiteral
    //	         | 	floatLiteral
    //	         | 	boolLiteral
    //	         | 	identifier.
    private Node Value() {
        Node Value = AST.MakeNode();

        if (Peek().type == Token.Type.LIT_Int) {
            Expect(Token.Type.LIT_Int);
        }
        else if (Peek().type == Token.Type.LIT_Float) {
            Expect(Token.Type.LIT_Float);
        }
        else if (Peek().type == Token.Type.LIT_Bool) {
            Expect(Token.Type.LIT_Bool);
        }
        else if (Peek().type == Token.Type.IDENTIFIER) {
            Expect(Token.Type.IDENTIFIER);
        }
        else {
            MakeError("Expected literal int, float, or bool or an identifier");
        }

        return Value;
    }

    // AfterValue 	-> 	Operator Expr
    //	             | 	LogicOp Expr
    //	             | 	.
    private Node AfterValue() {
        Node AfterValue = AST.MakeNode();

        if (CheckForOperator()) {
            Operator();
            Expr();
        }

        else if (CheckForLogicOperator()) {
            LogicOperator();
            Expr();
        }
        else if (Peek().type == Token.Type.SEMI ||
                 Peek().type == Token.Type.RPAREN) {
            return AST.MakeNode();
        }
        else {
            MakeError("Expected operator or end of statement");
        }

        return AfterValue;
    }

    // Call 	-> 	VoidCall
    //         	 | 	ReturnsCall.
    private Node Call() {
        Node Call = AST.MakeNode();

        if (Peek().type == Token.Type.BROADCAST ||
            Peek().type == Token.Type.WRITE) {
            VoidCall();
        }
        else if (Peek().type == Token.Type.FILTERNOISE ||
                 Peek().type == Token.Type.GETVALUE    ||
                 Peek().type == Token.Type.CREATEEVENT) {
            ReturnsCall();
        }
        else {
            MakeError("Expected function call");
        }

        return Call;
    }

    // VoidCall	->	broadcast lparen identifier rparen
    //	         |	write lparen identifier comma CallParam rparen.
    private Node VoidCall() {
        Node VoidCall = AST.MakeNode();

        if (Peek().type == Token.Type.BROADCAST) {
            Expect(Token.Type.BROADCAST);
            Expect(Token.Type.LPAREN);
            Expect(Token.Type.IDENTIFIER);
            Expect(Token.Type.RPAREN);
        }
        else if (Peek().type == Token.Type.WRITE) {
            Expect(Token.Type.WRITE);
            Expect(Token.Type.LPAREN);
            Expect(Token.Type.IDENTIFIER);
            Expect(Token.Type.COMMA);
            CallParam();
            Expect(Token.Type.RPAREN);
        }
        else {
            MakeError("Expected write or broadcast");
        }

        return VoidCall;
    }

    // ReturnsCall	-> filterNoise lparen identifier comma FilterType rparen
    //	             | 	getValue lparen Value rparen
    //	             |  createEvent lparen Value rparen.
    private Node ReturnsCall() {
        Node ReturnsCall = AST.MakeNode();

        if (Peek().type == Token.Type.FILTERNOISE) {
            Expect(Token.Type.FILTERNOISE);
            Expect(Token.Type.LPAREN);
            Expect(Token.Type.IDENTIFIER);
            Expect(Token.Type.COMMA);
            FilterType();
            Expect(Token.Type.RPAREN);
        }
        else if (Peek().type == Token.Type.GETVALUE) {
            Expect(Token.Type.GETVALUE);
            Expect(Token.Type.LPAREN);
            Value();
            Expect(Token.Type.RPAREN);
        }
        else if (Peek().type == Token.Type.CREATEEVENT) {
            Expect(Token.Type.CREATEEVENT);
            Expect(Token.Type.LPAREN);
            Value();
            Expect(Token.Type.RPAREN);
        }
        else {
            MakeError("Expected filterNoise, getValue, or createEvent");
        }

        return ReturnsCall;
    }

    // CallParam 	-> 	Expr
    //	             | 	ReturnsCall.
    private Node CallParam() {
        Node CallParam = AST.MakeNode();

        if (CheckForExpr()) {
            Expr();
        }
        else if (Peek().type == Token.Type.FILTERNOISE ||
                 Peek().type == Token.Type.GETVALUE    ||
                 Peek().type == Token.Type.CREATEEVENT) {
            ReturnsCall();
        }
        else {
            MakeError("Expected expression, filterNoise, getValue or createEvent");
        }

        return CallParam;
    }

    // IfStmt 	-> 	if lparen Expr rparen Block IfEnd.
    private Node IfStmt() {
        Node IfStmt = AST.MakeNode();

        if (Peek().type == Token.Type.IF) {
            Expect(Token.Type.IF);
            Expect(Token.Type.LPAREN);
            Expr();
            Expect(Token.Type.RPAREN);
            Block();
            IfEnd();
        }
        else {
            MakeError("Expected if");
        }

        return IfStmt;
    }

    // IfEnd 	-> 	else AfterElse
    //	         | 	.
    private Node IfEnd() {
        Node IfEnd = AST.MakeNode();

        if (Peek().type == Token.Type.ELSE) {
            Expect(Token.Type.ELSE);
            AfterElse();
        }
        else if (Peek().type == Token.Type.RBRACKET ||
                 Peek().type == Token.Type.IDENTIFIER ||
                 Peek().type == Token.Type.IF ||
                 CheckForType() ||
                 CheckForCall()) {
            return IfEnd;
        }
        else {
            MakeError("Expected else or end of if statement");
        }

        return IfEnd;
    }

    // AfterElse 	-> 	IfStmt
    //	             | 	Block.
    private Node AfterElse() {
        Node AfterElse = AST.MakeNode();

        if (Peek().type == Token.Type.IF) {
            IfStmt();
        }
        else if (Peek().type == Token.Type.LBRACKET) {
            Block();
        }
        else {
            MakeError("Expected if statement or a block");
        }

        return AfterElse;
    }

    // FilterType -> 	flip
    //	           | 	constant
    //	           | 	range.
    private Node FilterType() {
        Node FilterType = AST.MakeNode();

        if (Peek().type == Token.Type.FLIP) {
            Expect(Token.Type.FLIP);
        }
        else if (Peek().type == Token.Type.CONSTANT) {
            Expect(Token.Type.CONSTANT);
        }
        else if (Peek().type == Token.Type.RANGE) {
            Expect(Token.Type.RANGE);
        }
        else {
            MakeError("Expected FLIP, CONSTANT, or RANGE");
        }

        return FilterType;
    }

    // Operator 	-> 	plus
    //	             | 	minus
    //	             | 	times
    //	             | 	divide
    //	             | 	modulo.
    private Node Operator() {
        Node Operator = AST.MakeNode();

        if (Peek().type == Token.Type.OP_PLUS) {
            Expect(Token.Type.OP_PLUS);
        }
        else if (Peek().type == Token.Type.OP_MINUS) {
            Expect(Token.Type.OP_MINUS);
        }
        else if (Peek().type == Token.Type.OP_TIMES) {
            Expect(Token.Type.OP_TIMES);
        }
        else if (Peek().type == Token.Type.OP_DIVIDE) {
            Expect(Token.Type.OP_DIVIDE);
        }
        else if (Peek().type == Token.Type.OP_MODULO) {
            Expect(Token.Type.OP_MODULO);
        }
        else {
            MakeError("Expected +, -, *, /, or %");
        }

        return Operator;
    }

    // LogicOp 	-> lessThan
    //	         | 	greaterThan
    //	         | 	notEqual
    //	         | 	greaterOrEqual
    //	         | 	lessOrEqual
    //	         | 	equals.
    private Node LogicOperator() {
        Node LogicOperator = AST.MakeNode();

        if (Peek().type == Token.Type.LOP_LESSTHAN) {
            Expect(Token.Type.LOP_LESSTHAN);
        }
        else if (Peek().type == Token.Type.LOP_GREATERTHAN) {
            Expect(Token.Type.LOP_GREATERTHAN);
        }
        else if (Peek().type == Token.Type.LOP_NOTEQUAL) {
            Expect(Token.Type.LOP_NOTEQUAL);
        }
        else if (Peek().type == Token.Type.LOP_GREATEROREQUAL) {
            Expect(Token.Type.LOP_GREATEROREQUAL);
        }
        else if (Peek().type == Token.Type.LOP_LESSOREQUAL) {
            Expect(Token.Type.LOP_LESSOREQUAL);
        }
        else if (Peek().type == Token.Type.LOP_EQUALS) {
            Expect(Token.Type.LOP_EQUALS);
        }
        else {
            MakeError("Expected <, >, !=, >=, <=, or ==");
        }

        return LogicOperator;
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
