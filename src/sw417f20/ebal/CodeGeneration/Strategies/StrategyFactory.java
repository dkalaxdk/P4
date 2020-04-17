package sw417f20.ebal.CodeGeneration.Strategies;

import java.lang.reflect.Type;

/**
 * Contains methods for getting code generation strategies.
 * Creates a single instance of a strategy, the first time it is requested.
 * This instance is saved in a field and reused for each subsequent request.
 */
public class StrategyFactory {

    // Fields used for saving instances of strategies, so they can be reused
    private ProgStrategy progStrategy;
    private MasterStrategy masterStrategy;
    private SlaveStrategy slaveStrategy;
    private InitiateStrategy initiateStrategy;
    private ListenerStrategy listenerStrategy;
    private EventHandlerStrategy eventHandlerStrategy;
    private BlockStrategy blockStrategy;
    private CallStrategy callStrategy;
    private BroadcastStrategy broadcastStrategy;
    private WriteStrategy writeStrategy;
    private GetValueStrategy getValueStrategy;
    private FilterNoiseStrategy filterNoiseStrategy;
    private CreateEventStrategy createEventStrategy;
    private CreatePinStrategy createPinStrategy;
    private AssignmentStrategy assignmentStrategy;
    private IfStrategy ifStrategy;
    private PinDeclarationStrategy pinDeclarationStrategy;
    private BoolDeclarationStrategy boolDeclarationStrategy;
    private IntDeclarationStrategy intDeclarationStrategy;
    private FloatDeclarationStrategy floatDeclarationStrategy;
    private EventDeclarationStrategy eventDeclarationStrategy;
    // Parameter Strategies
    private PinTypeStrategy pinTypeStrategy;
    //      private DigitalStrategy digitalStrategy;
    //      private AnalogStrategy analogStrategy;
    //      private PwmStrategy pwmStrategy;
    private IOTypeStrategy ioTypeStrategy;
    //      private InputStrategy inputStrategy;
    //      private OutputStrategy outputStrategy;
    private FilterTypeStrategy filterTypeStrategy;
    //      private ConstantStrategy constantStrategy;
    //      private FlipStrategy flipStrategy;
    //      private RangeStrategy rangeStrategy;
    private ExpressionStrategy expressionStrategy;
    private IdentifierStrategy identifierStrategy;
    private PrefixStrategy prefixStrategy;
    //      private PrefixNotStrategy prefixNotStrategy;
    //      private PrefixMinusStrategy prefixMinusStrategy;
    private LiteralStrategy literalStrategy;
    private EmptyStrategy emptyStrategy;


    public ProgStrategy GetProgStrategy() {
        if(progStrategy == null) {
            progStrategy = new ProgStrategy();
        }

        return progStrategy;
    }

    public MasterStrategy GetMasterStrategy() {
        if(masterStrategy == null) {
            masterStrategy = new MasterStrategy();
        }

        return masterStrategy;
    }

    public SlaveStrategy GetSlaveStrategy() {
        if(slaveStrategy == null) {
            slaveStrategy = new SlaveStrategy();
        }

        return slaveStrategy;
    }

    public InitiateStrategy GetInitiateStrategy() {
        if(initiateStrategy == null) {
            initiateStrategy = new InitiateStrategy();
        }

        return initiateStrategy;
    }

    public EventHandlerStrategy GetEventHandlerStrategy() {
        if(eventHandlerStrategy == null) {
            eventHandlerStrategy = new EventHandlerStrategy();
        }

        return eventHandlerStrategy;
    }

    public ListenerStrategy GetListenerStrategy() {
        if(listenerStrategy == null) {
            listenerStrategy = new ListenerStrategy();
        }

        return listenerStrategy;
    }

    public BlockStrategy GetBlockStrategy() {
        if(blockStrategy == null) {
            blockStrategy = new BlockStrategy();
        }

        return blockStrategy;
    }

    public CallStrategy GetCallStrategy() {
        if(callStrategy == null) {
            callStrategy = new CallStrategy();
        }

        return callStrategy;
    }

    public BroadcastStrategy GetBroadcastStrategy() {
        if(broadcastStrategy == null) {
            broadcastStrategy = new BroadcastStrategy();
        }

        return broadcastStrategy;
    }

    public WriteStrategy GetWriteStrategy() {
        if(writeStrategy == null) {
            writeStrategy = new WriteStrategy();
        }

        return writeStrategy;
    }

    public GetValueStrategy GetGetValueStrategy() {
        if(getValueStrategy == null) {
            getValueStrategy = new GetValueStrategy();
        }

        return getValueStrategy;
    }

    public FilterNoiseStrategy GetFilterNoiseStrategy() {
        if(filterNoiseStrategy == null) {
            filterNoiseStrategy = new FilterNoiseStrategy();
        }

        return filterNoiseStrategy;
    }

    public CreateEventStrategy GetCreateEventStrategy() {
        if(createEventStrategy == null) {
            createEventStrategy = new CreateEventStrategy();
        }

        return createEventStrategy;
    }

    public CreatePinStrategy GetCreatePinStrategy() {
        if(createPinStrategy == null) {
            createPinStrategy = new CreatePinStrategy();
        }

        return createPinStrategy;
    }

    public AssignmentStrategy GetAssignmentStrategy() {
        if(assignmentStrategy == null) {
            assignmentStrategy = new AssignmentStrategy();
        }

        return assignmentStrategy;
    }

    public IfStrategy GetIfStrategy() {
        if(ifStrategy == null) {
            ifStrategy = new IfStrategy();
        }

        return ifStrategy;
    }

    public PinDeclarationStrategy GetPinDeclarationStrategy() {
        if(pinDeclarationStrategy == null) {
            pinDeclarationStrategy = new PinDeclarationStrategy();
        }

        return pinDeclarationStrategy;
    }

    public BoolDeclarationStrategy GetBoolDeclarationStrategy() {
        if(boolDeclarationStrategy == null) {
            boolDeclarationStrategy = new BoolDeclarationStrategy();
        }

        return boolDeclarationStrategy;
    }

    public IntDeclarationStrategy GetIntDeclarationStrategy() {
        if(intDeclarationStrategy == null) {
            pinDeclarationStrategy = new PinDeclarationStrategy();
        }

        return intDeclarationStrategy;
    }

    public FloatDeclarationStrategy GetFloatDeclarationStrategy() {
        if(floatDeclarationStrategy == null) {
            floatDeclarationStrategy = new FloatDeclarationStrategy();
        }

        return floatDeclarationStrategy;
    }

    public EventDeclarationStrategy GetEventDeclarationStrategy() {
        if(eventDeclarationStrategy == null) {
            eventDeclarationStrategy = new EventDeclarationStrategy();
        }

        return eventDeclarationStrategy;
    }

    public PinTypeStrategy GetPinTypeStrategy() {
        if(pinTypeStrategy == null) {
            pinTypeStrategy = new PinTypeStrategy();
        }

        return pinTypeStrategy;
    }

    /*
    public DigitalStrategy GetDigitalStrategy() {
        if(digitalStrategy == null) {
            digitalStrategy = new DigitalStrategy();
        }

        return digitalStrategy;
    }
     */

    /*
    public AnalogStrategy GetAnalogStrategy() {
        if(analogStrategy == null) {
            analogStrategy = new AnalogStrategy();
        }

        return analogStrategy;
    }
     */

    /*
    public PwmStrategy GetPwmStrategy() {
        if(pwmStrategy == null) {
            pwmStrategy = new PwmStrategy();
        }

        return pwmStrategy;
    }
     */

    public IOTypeStrategy GetIOTypeStrategy() {
        if(ioTypeStrategy == null) {
            ioTypeStrategy = new IOTypeStrategy();
        }

        return ioTypeStrategy;
    }

    /*
    public InputStrategy GetInputStrategy() {
        if(inputStrategy == null) {
            inputStrategy = new InputStrategy();
        }

        return inputStrategy;
    }
     */

    /*
    public OutputStrategy GetOutputStrategy() {
        if(outputStrategy == null) {
            outputStrategy = new OutputStrategy();
        }

        return outputStrategy;
    }
     */

    /*
    public FilterTypeStrategy GetFilterTypeStrategy() {
        if(filterTypeStrategy == null) {
            filterTypeStrategy = new FilterTypeStrategy();
        }

        return filterTypeStrategy;
    }
     */

    /*
    public ConstantStrategy GetConstantStrategy() {
        if(constantStrategy == null) {
            constantStrategy = new ConstantStrategy();
        }

        return constantStrategy;
    }
     */

    /*
    public FlipStrategy GetFlipStrategy() {
        if(flipStrategy == null) {
            flipStrategy = new FlipStrategy();
        }

        return flipStrategy;
    }
     */

    /*
    public RangeStrategy GetRangeStrategy() {
        if(rangeStrategy == null) {
            rangeStrategy = new RangeStrategy();
        }

        return rangeStrategy;
    }
     */

    public ExpressionStrategy GetExpressionStrategy() {
        if(expressionStrategy == null) {
            expressionStrategy = new ExpressionStrategy();
        }

        return expressionStrategy;
    }

    public IdentifierStrategy GetIdentifierStrategy() {
        if(identifierStrategy == null) {
            identifierStrategy = new IdentifierStrategy();
        }

        return identifierStrategy;
    }

    public PrefixStrategy GetPrefixStrategy() {
        if(prefixStrategy == null) {
            prefixStrategy = new PrefixStrategy();
        }

        return prefixStrategy;
    }

    public LiteralStrategy GetLiteralStrategy() {
        if(literalStrategy == null) {
            literalStrategy = new LiteralStrategy();
        }

        return literalStrategy;
    }

    public EmptyStrategy GetEmptyStrategy() {
        if(emptyStrategy == null) {
            emptyStrategy = new EmptyStrategy();
        }

        return emptyStrategy;
    }


}
