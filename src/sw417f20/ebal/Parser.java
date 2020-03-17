package sw417f20.ebal;

import sw417f20.ebal.Nodes.LeafNodes.PrefixNode;
import sw417f20.ebal.Nodes.Node;

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
            return AST.MakeNode(AST.NodeType.Null);
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
            return AST.MakeNode(AST.NodeType.Null);
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
            return AST.MakeNode(AST.NodeType.Null);
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
            return AST.MakeNode(AST.NodeType.Null);
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
            return AST.MakeNode(AST.NodeType.Null);
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
            return AST.MakeNode(AST.NodeType.Null);
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

            if (afterValue.Type == AST.NodeType.Null) {
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
            Node prefix = new PrefixNode(AST.NodeType.Prefix, "-");

            Node value = Value();
            Node afterValue = AfterValue();

            if (afterValue.Type == AST.NodeType.Null) {
                return value;
            }

            Expr.AddChild(value);
            Expr.AddChild(afterValue);
        }
        else if (Peek().type == Token.Type.OP_NOT) {
            Expect(Token.Type.OP_NOT);
            Node identifier = AST.MakeNode(Expect(Token.Type.IDENTIFIER));
            Node afterValue = AfterValue();

            if (afterValue.Type == AST.NodeType.Null) {
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
            return AST.MakeNode(AST.NodeType.Null);
        }
        else {
            MakeError("Expected operator or end of statement");
            return AST.MakeNode(AST.NodeType.Error);
        }
    }

    // TODO: Implementer Call med børn

    // Call 	-> 	VoidCall
    //         	 | 	ReturnsCall.
    private Node Call() {
        Node Call = AST.MakeNode(AST.NodeType.Call);

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
    //	         |	write lparen identifier comma Expr rparen.
    private Node VoidCall() {
        Node VoidCall = AST.MakeNode(AST.NodeType.Null);

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
            Expr();
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
        Node ReturnsCall = AST.MakeNode(AST.NodeType.Null);

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

    // IfStmt 	-> 	if lparen Expr rparen Block IfEnd.
    private Node IfStmt() {
        Node IfStmt = AST.MakeNode(AST.NodeType.If);

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
        Node IfEnd = AST.MakeNode(AST.NodeType.Null);

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
        Node AfterElse = AST.MakeNode(AST.NodeType.Null);

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
        Node FilterType = AST.MakeNode(AST.NodeType.Null);

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
