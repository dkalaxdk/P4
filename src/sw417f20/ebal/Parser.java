package sw417f20.ebal;

import org.junit.jupiter.params.shadow.com.univocity.parsers.common.TextParsingException;

import java.awt.image.TileObserver;

public class Parser extends RecursiveDescent{

    // Start 	-> 	Master Slave Slaves.
    @Override
    public void Start() {
        Master();
        Slave();
        Slaves();
    }

    // Master 	-> 	begin master Initiate Listeners end master.
    private void Master() {
        if (Peek().type == Token.Type.BEGIN) {
            Expect(Token.Type.BEGIN);
            Expect(Token.Type.MASTER);

            Initiate();
            Listeners();

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
        else {
            MakeError("Expected BEGIN or EOF");
        }
    }

    // Slave 	-> 	begin slave Initiate EventHandlers end slave.
    private void Slave() {
        if (Peek().type == Token.Type.BEGIN) {
            Expect(Token.Type.BEGIN);
            Expect(Token.Type.SLAVE);

            Initiate();
            EventHandlers();

            Expect(Token.Type.END);
            Expect(Token.Type.SLAVE);
        }
        else {
            MakeError("Expected BEGIN");
        }
    }

    // Initiate 	-> 	initiate lbracket PinDcls rbracket.
    private void Initiate() {
        if (Peek().type == Token.Type.INITIATE) {
            Expect(Token.Type.INITIATE);
            Expect(Token.Type.LBRACKET);
            PinDcls();
            Expect(Token.Type.RBRACKET);
        }
        else {
            MakeError("Expected Initiate");
        }
    }

    // PinDcls 	-> 	PinDcl semi PinDcls
    //	         | 	.
    private void PinDcls() {
        if (Peek().type == Token.Type.PIN) {
            PinDcl();
            Expect(Token.Type.SEMI);
            PinDcls();
        }
        else if (Peek().type == Token.Type.RBRACKET) {
            return;
        }
        else {
            MakeError("Expected pin initialization or }");
        }
    }

    // PinDcl 	-> 	pin identifier assign PinType IOType lparen intLiteral rparen.
    private void PinDcl() {
        if (Peek().type == Token.Type.PIN) {
            Expect(Token.Type.PIN);
            Expect(Token.Type.IDENTIFIER);
            Expect(Token.Type.ASSIGN);
            PinType();
            IOType();
            Expect(Token.Type.LPAREN);
            Expect(Token.Type.LIT_Int);
            Expect(Token.Type.RPAREN);
        }
        else {
            MakeError("Expected pin initialization");
        }
    }

    // PinType 	-> 	digital
    //	         | 	analog
    //	         | 	pwm.
    private void PinType() {
        if (Peek().type == Token.Type.DIGITAL) {
            Expect(Token.Type.DIGITAL);
        }
        else if (Peek().type == Token.Type.ANALOG) {
            Expect(Token.Type.ANALOG);
        }
        else if (Peek().type == Token.Type.PWM) {
            Expect(Token.Type.PWM);
        }
        else {
            MakeError("Expected digital, analog, or pwm");
        }
    }

    // IOType 	-> 	input
    //	         | 	output.
    private void IOType() {
        if (Peek().type == Token.Type.INPUT) {
            Expect(Token.Type.INPUT);
        }
        else if (Peek().type == Token.Type.OUTPUT) {
            Expect(Token.Type.OUTPUT);
        }
        else {
            MakeError("Expected input or output");
        }
    }

    // Listeners 	-> 	Listener Listeners
    //	             | 	.
    private void Listeners() {
//        if (Peek().type == Token.Type.LI)
    }

    // Listener	-> listener lparen identifier rparen Block.
    private void Listener() {

    }

    // EventHandlers -> 	EventHandler EventHandlers
    //	              | 	.
    private void EventHandlers() {

    }

    // EventHandler -> eventHandler lparen identifier rparen Block.
    private void EventHandler() {

    }

    // Block 	-> lbracket Stmts rbracket.
    private void Block() {

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
    //	             | 	Dcl assign AfterDcl .
    private void Assignment() {

    }

    // Dcl 	-> 	float identifier
    //	     | 	int identifier
    //	     | 	bool identifier
    //	     | 	event identifier.
    private void Dcl() {

    }

    // AfterDcl  	-> Expr
    //	             | 	Call.
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

    // IfStmt 	-> 	if lparen Expr rparen Block IfEnd.
    private void IfStmt() {

    }

    // IfEnd 	-> 	else AfterElse
    //	         | 	.
    private void IfEnd() {

    }

    // AfterElse 	-> 	IfStmt
    //	             | 	Block.
    private void AfterElse() {

    }

    // Call 	-> 	VoidCall
    //         	 | 	ReturnsCall.
    private void Call() {

    }

    // ReturnsCall	-> filterNoise lparen identifier comma FilterType rparen
    //	             | 	getValue lparen Value rparen.
    private void ReturnsCall() {

    }

    // VoidCall	->	broadcast lparen identifier rparen
    //	         |	write lparen identifier comma CallParam rparen.
    private void VoidCall() {

    }

    // Expr 	-> 	Value AfterValue
    //	         | 	lparen Expr rparen
    //	         | 	minus Value
    //	         | 	not Value.
    private void Expr() {

    }

    // CallParam 	-> 	Expr
    //	             | 	ReturnsCall.
    private void CallParam() {

    }

    // Value 	-> 	intLiteral
    //	         | 	floatLiteral
    //	         | 	boolLiteral
    //	         | 	identifier.
    private void Value() {

    }

    // AfterValue 	-> 	Operator Expr
    //	             | 	LogicOp Expr
    //	             | 	.
    private void AfterValue() {

    }

    // FilterType -> 	flip
    //	           | 	constant
    //	           | 	range.
    private void FilterType() {

    }
}
