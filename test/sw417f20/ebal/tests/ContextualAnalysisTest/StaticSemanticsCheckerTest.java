//package sw417f20.ebal.tests.ContextualAnalysisTest;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import sw417f20.ebal.Exceptions.SemanticsException;
//import sw417f20.ebal.Exceptions.SyntaxException;
//import sw417f20.ebal.SyntaxAnalysis.Node;
//import sw417f20.ebal.SyntaxAnalysis.Parser;
//import sw417f20.ebal.SyntaxAnalysis.Reader;
//import sw417f20.ebal.SyntaxAnalysis.Scanner;
//
//import java.io.BufferedReader;
//import java.io.StringReader;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class StaticSemanticsCheckerTest {
//
//    @BeforeEach
//    void setUp() {
//    }
//
//    Node runParser(String program) {
//        StringReader stringReader = new StringReader(program);
//        BufferedReader bufferedReader = new BufferedReader(stringReader);
//        Reader reader = new Reader(bufferedReader);
//        Scanner scanner = new Scanner(reader);
//
//        Parser parser = new Parser(scanner, program);
//        Node ast = null;
//        try {
//            ast = parser.Parse(false);
//
//        }
//        catch (SyntaxException ignored){
//
//        }
//        return ast;
//    }
//
//    public String createProgramOneSlave(String masterIni, String listeners,
//                                        String slaveIni, String eventHandlers) {
//
//        return "BEGIN MASTER Initiate { " + masterIni +
//                   " } " + listeners +
//                   "END MASTER " +
//                   "BEGIN SLAVE : test Initiate { " + slaveIni +
//                   " } " + eventHandlers +
//                   "END SLAVE ";
//    }
//
//    public String createProgramTwoSlaves(String masterIni, String listeners,
//                                         String slaveOneIni, String slaveOneEventHandlers,
//                                         String slaveTwoIni, String slaveTwoEventHandlers) {
//
//        return "BEGIN MASTER Initiate { " + masterIni +
//                " } " + listeners +
//                "END MASTER " +
//                "BEGIN SLAVE : test Initiate { " + slaveOneIni +
//                " } " + slaveOneEventHandlers +
//                "END SLAVE " +
//                "BEGIN SLAVE : test2 Initiate { " + slaveTwoIni +
//                " } " + slaveTwoEventHandlers +
//                "END SLAVE ";
//    }
//
//    @Test
//    void run_Example_ThrowSemanticsException() {
//        // Arrange
//        String masterIni = "";
//        String listeners = "";
//        String slaveIni = "";
//        String eventHandlers = "";
//        String program = createProgramOneSlave(masterIni, listeners, slaveIni, eventHandlers);
//        Node ast = runParser(program);
//        StaticSemanticsChecker checker = new StaticSemanticsChecker();
//
//        // Act
//        try {
//            checker.Run(ast);
//        }
//        // Assert
//        catch (SemanticsException e) {
//            assertTrue(true);
//            return;
//        }
//
//        fail();
//    }
//
//    @Test
//    void run_OnlyPinDeclarationsInMasterInitiate_DoNotThrowSemanticsException() {
//        // Arrange
//        String masterIni = "pin test = createPin(digital, input, 4);";
//        String listeners = "";
//        String slaveIni = "";
//        String eventHandlers = "";
//        String program = createProgramOneSlave(masterIni, listeners, slaveIni, eventHandlers);
//        Node ast = runParser(program);
//        StaticSemanticsChecker checker = new StaticSemanticsChecker();
//
//        // Act
//        try {
//            checker.Run(ast);
//        }
//        // Assert
//        catch (SemanticsException e) {
//            fail();
//        }
//    }
//
//}
//
///* Throws error:
//* SlaveDeclaredTwice
//* PinInListenerNotDeclared
//* WrongTypeInListener
//* PinInEventHandlerNotDeclared
//* WrongTypeInEventHandler
//* NonPinDeclarationInInitiate   (evt mere end en test)
//* PinDeclaredInListener
//* PinDeclaredInEventHandler
//* EventDeclaredInEventHandler
//* PinAlreadyDeclared
//* PinNotDeclaredWithCreatePinCall
//* PinDeclaredWithWrongType
//* EventAlreadyDeclared
//* EventNotDeclaredWithCreateEventCall   (både uden kald og med andet kald?)
//* EventDeclaredWithWrongType
//* FloatAlreadyDeclared
//* FloatDeclaredWithWrongType
//* IntAlreadyDeclared
//* IntDeclaredWithWrongType
//* BoolAlreadyDeclared
//* BoolDeclaredWithWrongType
//* AssignmentWithWrongType
//* AssignmentOfPin
//* AssignmentOfEvent
//* AssignmentNotDeclared
//* IfStatementExpressionNotBoolean
//* OtherCallThenCreatePinInInitiate      (flere)
//* OtherCallThanWriteOrGetValueInEventHandler    (flere)
//* IllegalCallInListener                     (flere)
//* BroadcastParameterNotDeclared
//* BroadcastCalledWithWrongTypeParameter
//* FilterNoiseParameterNotDeclared
//* FilterNoiseCalledWithWrongTypeParameter
//* DigitalPinFilteredAsRanged
//* AnalogPinFilteredAsOtherThanRanged
//* GetValueParameterNotDeclared
//* GetValueCalledWithWrongTypeParameter
//* WriteParameterNotDeclared
//* WriteCalledWithWrongTypeFirstParameter
//* WriteCalledWithWrongTypeSecondParameter
//* CreateEventCalledWithWrongTypeParameter
//* PinDeclaredAsInputAndPWM
//* PinDeclaredWithUsedPinNumber
//* IdentifierNotDeclared
//* OperatorsInExpressionOfDifferentType      (flere?)
//* ModuloUsedWithOtherThenIntType    (flere)
//* PlusUsedWithBoolean
//* MinusUsedWithBoolean
//* TimesUsedWithBoolean
//* DivideUsedWithBoolean
//* LessThanUsedWithBoolean
//* GreaterThenUsedWithBoolean
//* GreaterOrEqualUsedWithBoolean
//* LessOrEqualUsedWithBoolean
//* AndNotUsedWithBoolean
//* OrNotUsedWithBoolean
//*
//*
//*
//*
//* check that symbols are added to table
//* check opening and closing of scopes
//* */