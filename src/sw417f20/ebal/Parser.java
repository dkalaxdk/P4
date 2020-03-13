package sw417f20.ebal;

import org.junit.jupiter.params.shadow.com.univocity.parsers.common.TextParsingException;
import org.mockito.verification.After;

import java.awt.image.TileObserver;
import java.security.spec.ECParameterSpec;

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
            MakeError("Expected pin or }");
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
            MakeError("Expected pin");
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
        else if (Peek().type == Token.Type.EVENTHANDLER) {
            MakeError("EventHandlers can only be declared in slaves");
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
        else if (Peek().type == Token.Type.LISTENER) {
            MakeError("Listeners can only be declared in master");
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
            Peek().type == Token.Type.IF ||
            CheckForType() ||
            CheckForCall()) {

            Stmt();
            Stmts();
        }
        else if (Peek().type == Token.Type.RBRACKET) {
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
        if (CheckForCall()) {
            Call();
            Expect(Token.Type.SEMI);
        }
        else if (Peek().type == Token.Type.IDENTIFIER || CheckForType()) {
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
        else if (CheckForType()) {
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
        if (CheckForExpr()) {
            Expr();
        }
        else if (Peek().type == Token.Type.FILTERNOISE ||
                 Peek().type == Token.Type.GETVALUE) {
            ReturnsCall();
        }
    }

    // Expr 	-> 	Value AfterValue
    //	         | 	lparen Expr rparen
    //	         | 	minus Value AfterValue
    //	         | 	not boolLiteral AfterValue.
    private void Expr() {
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
            return;
        }
        else {
            MakeError("Expected operator or end of statement");
        }
    }

    // Call 	-> 	VoidCall
    //         	 | 	ReturnsCall.
    private void Call() {
        if (Peek().type == Token.Type.BROADCAST ||
            Peek().type == Token.Type.WRITE) {
            VoidCall();
        }
        else if (Peek().type == Token.Type.FILTERNOISE ||
                 Peek().type == Token.Type.GETVALUE) {
            ReturnsCall();
        }
        else {
            MakeError("Expected function call");
        }
    }

    // VoidCall	->	broadcast lparen identifier rparen
    //	         |	write lparen identifier comma CallParam rparen.
    private void VoidCall() {
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
    }

    // ReturnsCall	-> filterNoise lparen identifier comma FilterType rparen
    //	             | 	getValue lparen Value rparen.
    private void ReturnsCall() {
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
        else {
            MakeError("Expected filterNoise or getValue");
        }
    }

    // CallParam 	-> 	Expr
    //	             | 	ReturnsCall.
    private void CallParam() {
        if (CheckForExpr()) {
            Expr();
        }
        else if (Peek().type == Token.Type.FILTERNOISE ||
                 Peek().type == Token.Type.GETVALUE) {
            ReturnsCall();
        }
        else {
            MakeError("Expected expression, filterNoise or getValue");
        }
    }

    // IfStmt 	-> 	if lparen Expr rparen Block IfEnd.
    private void IfStmt() {
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
    }

    // IfEnd 	-> 	else AfterElse
    //	         | 	.
    private void IfEnd() {
        if (Peek().type == Token.Type.ELSE) {
            Expect(Token.Type.ELSE);
            AfterElse();
        }
        else if (Peek().type == Token.Type.RBRACKET ||
                 Peek().type == Token.Type.IDENTIFIER ||
                 Peek().type == Token.Type.IF ||
                 CheckForType() ||
                 CheckForCall()) {
            return;
        }
        else {
            MakeError("Expected else or end of if statement");
        }
    }

    // AfterElse 	-> 	IfStmt
    //	             | 	Block.
    private void AfterElse() {
        if (Peek().type == Token.Type.IF) {
            IfStmt();
        }
        else if (Peek().type == Token.Type.LBRACKET) {
            Block();
        }
        else {
            MakeError("Expected if statement or a block");
        }
    }

    // FilterType -> 	flip
    //	           | 	constant
    //	           | 	range.
    private void FilterType() {
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
    }

    // Operator 	-> 	plus
    //	             | 	minus
    //	             | 	times
    //	             | 	divide
    //	             | 	modulo.
    private void Operator() {
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
    }

    // LogicOp 	-> lessThan
    //	         | 	greaterThan
    //	         | 	notEqual
    //	         | 	greaterOrEqual
    //	         | 	lessOrEqual
    //	         | 	equals.
    private void LogicOperator() {
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
                Peek().type == Token.Type.WRITE;
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
