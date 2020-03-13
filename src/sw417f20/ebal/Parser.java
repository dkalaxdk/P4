package sw417f20.ebal;

import org.junit.jupiter.params.shadow.com.univocity.parsers.common.TextParsingException;
import org.mockito.verification.After;

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
        if (Peek().type == Token.Type.LISTENER) {
            Listener();
            Listeners();
        }
        else if (Peek().type == Token.Type.END) {
            return;
        }
        else {
            MakeError("Expected Listener or end of master");
        }
    }

    // Listener	-> listener lparen identifier rparen Block.
    private void Listener() {
        if (Peek().type == Token.Type.LISTENER) {
            Expect(Token.Type.LISTENER);
            Expect(Token.Type.LPAREN);
            Expect(Token.Type.IDENTIFIER);
            Expect(Token.Type.RPAREN);
            Block();
        }
        else {
            MakeError("Expected Listener");
        }
    }

    // EventHandlers -> 	EventHandler EventHandlers
    //	              | 	.
    private void EventHandlers() {
        if (Peek().type == Token.Type.EVENTHANDLER) {
            EventHandler();
            EventHandlers();
        }
        else if (Peek().type == Token.Type.END) {
            return;
        }
        else {
            MakeError("Expected EventHandler or end of slave");
        }
    }

    // EventHandler -> eventHandler lparen identifier rparen Block.
    private void EventHandler() {
        if (Peek().type == Token.Type.EVENTHANDLER) {
            Expect(Token.Type.EVENTHANDLER);
            Expect(Token.Type.LPAREN);
            Expect(Token.Type.IDENTIFIER);
            Expect(Token.Type.RPAREN);
            Block();
        }
        else {
            MakeError("Expected EventHandler");
        }
    }

    // Block 	-> lbracket Stmts rbracket.
    private void Block() {
        if (Peek().type == Token.Type.LBRACKET) {
            Expect(Token.Type.LBRACKET);
            Stmts();
            Expect(Token.Type.RBRACKET);
        }
        else {
            MakeError("Expected {");
        }
    }

    // Stmts 	-> 	Stmt Stmts
    //	         | 	.
    private void Stmts() {
        if (Peek().type == Token.Type.IDENTIFIER ||
            Peek().type == Token.Type.FLOAT ||
            Peek().type == Token.Type.INT ||
            Peek().type == Token.Type.BOOL ||
            Peek().type == Token.Type.EVENT ||
            Peek().type == Token.Type.IF ||
            Peek().type == Token.Type.FILTERNOISE ||
            Peek().type == Token.Type.GETVALUE ||
            Peek().type == Token.Type.BROADCAST ||
            Peek().type == Token.Type.WRITE) {

            Stmt();
            Stmts();
        }
        else if (Peek().type == Token.Type.LBRACKET) {
            return;
        }
        else {
            MakeError("Expected statement");
        }
    }

    // Stmt 	-> 	Call semi
    //	         | 	Assignment semi
    //	         | 	IfStmt.
    private void Stmt() {
        if (Peek().type == Token.Type.FILTERNOISE ||
            Peek().type == Token.Type.GETVALUE ||
            Peek().type == Token.Type.BROADCAST ||
            Peek().type == Token.Type.WRITE) {

            Call();
            Expect(Token.Type.SEMI);
        }
        else if (Peek().type == Token.Type.IDENTIFIER ||
                 Peek().type == Token.Type.FLOAT ||
                 Peek().type == Token.Type.INT ||
                 Peek().type == Token.Type.BOOL ||
                 Peek().type == Token.Type.EVENT) {

            Assignment();
            Expect(Token.Type.SEMI);
        }
        else if (Peek().type == Token.Type.IF) {
            IfStmt();
        }
        else {
            MakeError("Expected a call, an assignment, or an if-statement");
        }
    }

    // Assignment 	-> 	identifier assign Expr
    //	             | 	Dcl assign AfterDcl .
    private void Assignment() {
        if (Peek().type == Token.Type.IDENTIFIER) {
            Expect(Token.Type.IDENTIFIER);
            Expect(Token.Type.ASSIGN);
            Expr();
        }
        else if (Peek().type == Token.Type.FLOAT ||
                 Peek().type == Token.Type.INT ||
                 Peek().type == Token.Type.BOOL ||
                 Peek().type == Token.Type.EVENT) {
            Dcl();
            Expect(Token.Type.ASSIGN);
            AfterDcl();
        }
        else {
            MakeError("Expected assignment or declaration");
        }
    }

    // Dcl 	-> 	float identifier
    //	     | 	int identifier
    //	     | 	bool identifier
    //	     | 	event identifier.
    private void Dcl() {
        if (Peek().type == Token.Type.FLOAT) {
            Expect(Token.Type.FLOAT);
            Expect(Token.Type.IDENTIFIER);
        }
        else if (Peek().type == Token.Type.INT) {
            Expect(Token.Type.INT);
            Expect(Token.Type.IDENTIFIER);
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
    }

    // AfterDcl  	-> Expr
    //	             | 	ReturnsCall.
    private void AfterDcl() {
        if (Peek().type == Token.Type.IDENTIFIER    ||
            Peek().type == Token.Type.LIT_Int       ||
            Peek().type == Token.Type.LIT_Float     ||
            Peek().type == Token.Type.LIT_Bool      ||
            Peek().type == Token.Type.OP_MINUS      ||
            Peek().type == Token.Type.OP_NOT        ||
            Peek().type == Token.Type.LPAREN)
        {
            Expr();
        }
        else if (Peek().type == Token.Type.FILTERNOISE ||
                 Peek().type == Token.Type.GETVALUE)
        {
            ReturnsCall();
        }
    }

    // Expr 	-> 	Value AfterValue
    //	         | 	lparen Expr rparen
    //	         | 	minus Value AfterValue
    //	         | 	not boolLiteral AfterValue.
    private void Expr() {
        if (Peek().type == Token.Type.IDENTIFIER    ||
            Peek().type == Token.Type.LIT_Int       ||
            Peek().type == Token.Type.LIT_Float     ||
            Peek().type == Token.Type.LIT_Bool)
        {
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
    }

    // Value 	-> 	intLiteral
    //	         | 	floatLiteral
    //	         | 	boolLiteral
    //	         | 	identifier.
    private void Value() {
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
    }

    // AfterValue 	-> 	Operator Expr
    //	             | 	LogicOp Expr
    //	             | 	.
    private void AfterValue() {
//        if (Peek().type == Token.Type.OP_PLUS ||
//            Peek().type == Token.Type.OP_MINUS ||
//            Peek().type == Token.Type.OP_TIMES ||
//            Peek().type == Token.Type.)
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

    // CallParam 	-> 	Expr
    //	             | 	ReturnsCall.
    private void CallParam() {

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

    // FilterType -> 	flip
    //	           | 	constant
    //	           | 	range.
    private void FilterType() {

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
}
