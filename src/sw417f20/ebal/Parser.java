package sw417f20.ebal;

public class Parser extends RecursiveDescent{

    // Start 	-> 	Master Slaves.
    @Override
    public void Start() {
        Master();
        Slaves();
    }

    // Master 	-> 	begin master Initiate EventCreators end master.
    private void Master() {
        if (Peek().type == Token.Type.BEGIN) {
            Expect(Token.Type.BEGIN);
            Expect(Token.Type.MASTER);

            Initiate();
            EventCreators();

            Expect(Token.Type.END);
            Expect(Token.Type.MASTER);
        }
        else {
            MakeError("Expected BEGIN");
        }
    }

    // Slaves 	-> 	Slave Slaves
    //	         | 	.
    private void Slaves() {
        if (Peek().type == Token.Type.BEGIN) {
            Slave();
            Slaves();
        }
        else if (Peek().type == Token.Type.EOF) {
            return;
        }
    }

    // Slave 	-> 	begin slave colon identifier Initiate EventHandlers end slave.
    private void Slave() {
        if (Peek().type == Token.Type.BEGIN) {
            Expect(Token.Type.BEGIN);
            Expect(Token.Type.SLAVE);

            Expect(Token.Type.COLON);
            Expect(Token.Type.IDENTIFIER);

            Initiate();
            EventHandlers();

            Expect(Token.Type.END);
            Expect(Token.Type.SLAVE);
        }
    }

    // Initiate 	-> 	initiate lbrace PinDcls rbrace.
    private void Initiate() {
        if (Peek().type == Token.Type.INITIATE) {
            Expect(Token.Type.INITIATE);
            Expect(Token.Type.LBRACKET);
            PinDcls();
            Expect(Token.Type.RBRACKET);
        }
    }

    // PinDcls 	-> 	PinDcl semi PinDcls
    //	         | 	.
    private void PinDcls() {
        if (Peek().type == Token.Type.)
    }

    // PinDcl 	-> 	identifier assign PinType IOType lparen intLiteral rparen.
    private void PinDcl() {

    }

    // PinType 	-> 	digital
    //	         | 	analog
    //	         | 	pwm.
    private void PinType() {

    }

    // IOType 	-> 	input
    //	         | 	output.
    private void IOType() {

    }

    // EventCreators -> 	eventCreator lparen identifier rparen lbrace Stmts rbrace EventCreators
    //	              | 	.
    private void EventCreators() {

    }

    // EventHandlers -> 	eventHandler lparen identifier rparen lbrace Stmts rbrace EventHandlers
    //	              | 	.
    private void EventHandlers() {

    }

    // Stmts 	-> 	Stmt Stmts
    //	         | 	.
    private void Stmts() {

    }

    // Stmt 	-> 	Call semi
    //	         | 	Assignment semi
    //	         | 	IfStmt.
    private void Stmt() {

    }

    // Assignment 	-> 	identifier assign Expr
    //	             | 	Dcl AfterDcl .
    private void Assignment() {

    }

    // Dcl 	-> 	float identifier
    //	     | 	int identifier
    //	     | 	bool identifier.
    private void Dcl() {

    }

    // AfterDcl  	-> assign Expr
    //               | .
    private void AfterDcl() {

    }

    // Operator 	-> 	plus
    //	             | 	minus
    //	             | 	times
    //	             | 	divide
    //	             | 	modulo.
    private void Operator() {

    }

    // LogicOp 	-> lessThan
    //	         | 	greaterThan
    //	         | 	notEqual
    //	         | 	greaterOrEqual
    //	         | 	lessOrEqual
    //	         | 	equals.
    private void LogicOperator() {

    }

    // IfStmt 	-> 	if lparen Expr rparen lbrace Stmts rbrace IfEnd.
    private void IfStmt() {

    }

    // IfEnd 	-> 	else AfterElse
    //	         | 	.
    private void IfEnd() {

    }

    // AfterElse 	-> 	IfStmt
    //	             | 	lbrace Stmts rbrace.
    private void AfterElse() {

    }

    // Key 	-> 	value
    //	     | 	previousValue.
    private void Key() {

    }

    // Call 	-> 	broadcast lparen identifier comma identifier rparen
    //         	 | 	write lparen identifier comma CallParam  rparen
    //         	 | 	filterNoise lparen identifier comma FilterType rparen.
    private void Call() {

    }


}
