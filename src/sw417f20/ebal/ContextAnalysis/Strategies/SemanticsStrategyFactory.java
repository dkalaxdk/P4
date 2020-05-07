package sw417f20.ebal.ContextAnalysis.Strategies;

import sw417f20.ebal.ContextAnalysis.ISymbolTable;
import sw417f20.ebal.ContextAnalysis.Symbol;

import java.util.ArrayList;

public class SemanticsStrategyFactory {

    public ISymbolTable SymbolTable;
    public ArrayList<String> UsedPinNumbers;
    public ArrayList<Symbol> LocalEvents;
    public ArrayList<Symbol> BroadcastEvents;

    private SemanticsProgStrategy progStrategy;
    private SemanticsMasterStrategy masterStrategy;
    private SemanticsSlaveStrategy slaveStrategy;
    private SemanticsInitiateStrategy initiateStrategy;
    private SemanticsListenerStrategy listenerStrategy;
    private SemanticsEventHandlerStrategy eventHandlerStrategy;

    private SemanticsInitiateBlockStrategy initiateBlockStrategy;
    private SemanticsListenerBlockStrategy listenerBlockStrategy;
    private SemanticsEventHandlerBlockStrategy eventHandlerBlockStrategy;

    private SemanticsMasterInitiateCallStrategy masterInitiateCallStrategy;
    private SemanticsSlaveInitiateCallStrategy slaveInitiateCallStrategy;
    private SemanticsListenerCallStrategy listenerCallStrategy;
    private SemanticsEventHandlerCallStrategy eventHandlerCallStrategy;

    private SemanticsAssignmentStrategy assignmentStrategy;
    private SemanticsIfStrategy ifStrategy;

    private SemanticsPinDeclarationStrategy pinDeclarationStrategy;
    private SemanticsBoolDeclarationStrategy boolDeclarationStrategy;
    private SemanticsIntDeclarationStrategy intDeclarationStrategy;
    private SemanticsFloatDeclarationStrategy floatDeclarationStrategy;
    private SemanticsEventDeclarationStrategy eventDeclarationStrategy;

    private SemanticsExpressionStrategy expressionStrategy;
    private SemanticsIdentifierStrategy identifierStrategy;

    private SemanticsIntLiteralStrategy intLiteralStrategy;
    private SemanticsFloatLiteralStrategy floatLiteralStrategy;
    private SemanticsBoolLiteralStrategy boolLiteralStrategy;

    public SemanticsStrategyFactory(ISymbolTable symbolTable, ArrayList<String> usedPinNumbers,
                                    ArrayList<Symbol> localEvents, ArrayList<Symbol> broadcastEvents){
        SymbolTable = symbolTable;
        UsedPinNumbers = usedPinNumbers;
        LocalEvents = localEvents;
        BroadcastEvents = broadcastEvents;
    }


    public SemanticsProgStrategy GetProgStrategy() {
        if (progStrategy == null){
            progStrategy = new SemanticsProgStrategy();
            progStrategy.SymbolTable = SymbolTable;
        }
        return progStrategy;
    }

    public SemanticsMasterStrategy GetMasterStrategy() {
        if (masterStrategy == null){
            masterStrategy = new SemanticsMasterStrategy();
            masterStrategy.SymbolTable = SymbolTable;
            masterStrategy.BroadcastEvents = BroadcastEvents;
        }
        return masterStrategy;
    }

    public SemanticsSlaveStrategy GetSlaveStrategy() {
        if (slaveStrategy == null){
            slaveStrategy = new SemanticsSlaveStrategy();
            slaveStrategy.SymbolTable = SymbolTable;
        }
        return slaveStrategy;
    }

    public SemanticsInitiateStrategy getInitiateStrategy(){
        if (initiateStrategy == null){
            initiateStrategy = new SemanticsInitiateStrategy();
            initiateStrategy.SymbolTable = SymbolTable;
        }
        return initiateStrategy;
    }

    public SemanticsListenerStrategy getListenerStrategy(){
        if (listenerStrategy == null){
            listenerStrategy = new SemanticsListenerStrategy();
            listenerStrategy.SymbolTable = SymbolTable;
        }
        return listenerStrategy;
    }

    public SemanticsEventHandlerStrategy getEventHandlerStrategy(){
        if (eventHandlerStrategy == null){
            eventHandlerStrategy = new SemanticsEventHandlerStrategy();
            eventHandlerStrategy.SymbolTable = SymbolTable;
        }
        return eventHandlerStrategy;
    }

    public SemanticsInitiateBlockStrategy getInitiateBlockStrategy(){
        if (initiateBlockStrategy == null){
            initiateBlockStrategy = new SemanticsInitiateBlockStrategy();
            initiateBlockStrategy.SymbolTable = SymbolTable;
            initiateBlockStrategy.UsedPinNumbers = UsedPinNumbers;
        }
        return initiateBlockStrategy;
    }

    public SemanticsListenerBlockStrategy getListenerBlockStrategy(){
        if (listenerBlockStrategy == null){
            listenerBlockStrategy = new SemanticsListenerBlockStrategy();
            listenerBlockStrategy.SymbolTable = SymbolTable;
        }
        return listenerBlockStrategy;
    }

    public SemanticsEventHandlerBlockStrategy getEventHandlerBlockStrategy(){
        if (eventHandlerBlockStrategy == null){
            eventHandlerBlockStrategy = new SemanticsEventHandlerBlockStrategy();
            eventHandlerBlockStrategy.SymbolTable = SymbolTable;
        }
        return eventHandlerBlockStrategy;
    }

    public SemanticsMasterInitiateCallStrategy getMasterInitiateCallStrategy(){
        if (masterInitiateCallStrategy == null){
            masterInitiateCallStrategy = new SemanticsMasterInitiateCallStrategy();
            masterInitiateCallStrategy.SymbolTable = SymbolTable;
            masterInitiateCallStrategy.UsedPinNumbers = UsedPinNumbers;
        }
        return masterInitiateCallStrategy;
    }

    public SemanticsSlaveInitiateCallStrategy getSlaveInitiateCallStrategy(){
        if (slaveInitiateCallStrategy == null){
            slaveInitiateCallStrategy = new SemanticsSlaveInitiateCallStrategy();
            slaveInitiateCallStrategy.SymbolTable = SymbolTable;
            slaveInitiateCallStrategy.UsedPinNumbers = UsedPinNumbers;
        }
        return slaveInitiateCallStrategy;
    }

    public SemanticsListenerCallStrategy getListenerCallStrategy(){
        listenerCallStrategy = new SemanticsListenerCallStrategy();
        listenerCallStrategy.SymbolTable = SymbolTable;
        listenerCallStrategy.BroadcastEvents = BroadcastEvents;

        return listenerCallStrategy;
    }

    public SemanticsEventHandlerCallStrategy getEventHandlerCallStrategy(){
        eventHandlerCallStrategy = new SemanticsEventHandlerCallStrategy();
        eventHandlerCallStrategy.SymbolTable = SymbolTable;

        return eventHandlerCallStrategy;
    }

    public SemanticsAssignmentStrategy getAssignmentStrategy(){
        if (assignmentStrategy == null){
            assignmentStrategy = new SemanticsAssignmentStrategy();
            assignmentStrategy.SymbolTable = SymbolTable;
        }
        return  assignmentStrategy;
    }

    public SemanticsIfStrategy getIfStrategy(){
        if (ifStrategy == null){
            ifStrategy = new SemanticsIfStrategy();
            ifStrategy.SymbolTable = SymbolTable;
        }
        return ifStrategy;
    }

    public SemanticsPinDeclarationStrategy getPinDeclarationStrategy(){
        if (pinDeclarationStrategy == null){
            pinDeclarationStrategy = new SemanticsPinDeclarationStrategy();
            pinDeclarationStrategy.SymbolTable = SymbolTable;
        }
        return pinDeclarationStrategy;
    }

    public SemanticsBoolDeclarationStrategy getBoolDeclarationStrategy(){
        if (boolDeclarationStrategy == null){
            boolDeclarationStrategy = new SemanticsBoolDeclarationStrategy();
            boolDeclarationStrategy.SymbolTable = SymbolTable;
        }
        return boolDeclarationStrategy;
    }

    public SemanticsIntDeclarationStrategy getIntDeclarationStrategy(){
        if (intDeclarationStrategy == null){
            intDeclarationStrategy = new SemanticsIntDeclarationStrategy();
            intDeclarationStrategy.SymbolTable = SymbolTable;
        }
        return intDeclarationStrategy;
    }

    public  SemanticsFloatDeclarationStrategy getFloatDeclarationStrategy(){
        if (floatDeclarationStrategy == null){
            floatDeclarationStrategy = new SemanticsFloatDeclarationStrategy();
            floatDeclarationStrategy.SymbolTable = SymbolTable;
        }
        return floatDeclarationStrategy;
    }

    public SemanticsEventDeclarationStrategy getEventDeclarationStrategy(){
        if (eventDeclarationStrategy == null){
            eventDeclarationStrategy = new SemanticsEventDeclarationStrategy();
            eventDeclarationStrategy.SymbolTable = SymbolTable;
            eventDeclarationStrategy.LocalEvents = LocalEvents;
        }
        return eventDeclarationStrategy;
    }

    public SemanticsExpressionStrategy getExpressionStrategy(){
        if (expressionStrategy == null){
            expressionStrategy = new SemanticsExpressionStrategy();
            expressionStrategy.SymbolTable = SymbolTable;
        }
        return expressionStrategy;
    }

    public SemanticsIdentifierStrategy getIdentifierStrategy(){
        if (identifierStrategy == null){
            identifierStrategy = new SemanticsIdentifierStrategy();
            identifierStrategy.SymbolTable = SymbolTable;
        }
        return identifierStrategy;
    }

    public SemanticsIntLiteralStrategy getIntLiteralStrategy(){
        if (intLiteralStrategy == null){
            intLiteralStrategy = new SemanticsIntLiteralStrategy();
            intLiteralStrategy.SymbolTable = SymbolTable;
        }
        return intLiteralStrategy;
    }

    public SemanticsFloatLiteralStrategy getFloatLiteralStrategy(){
        if (floatLiteralStrategy == null){
            floatLiteralStrategy = new SemanticsFloatLiteralStrategy();
            floatLiteralStrategy.SymbolTable = SymbolTable;
        }
        return floatLiteralStrategy;
    }

    public SemanticsBoolLiteralStrategy getBoolLiteralStrategy(){
        if (boolLiteralStrategy == null){
            boolLiteralStrategy = new SemanticsBoolLiteralStrategy();
            boolLiteralStrategy.SymbolTable = SymbolTable;
        }
        return boolLiteralStrategy;
    }
}
