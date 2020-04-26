package sw417f20.ebal.CodeGeneration;

import sw417f20.ebal.CodeGeneration.Utility.Dictionaries;
import sw417f20.ebal.SyntaxAnalysis.AST;
import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;

public class CodeGeneration {
    ArrayList<String> Files;
    Dictionaries EventDictionary;
    int CommandSize = 6;    //Length of a command sent between master and slaves

    public CodeGeneration(Node node){
        Files = new ArrayList<String>();
        EventDictionary = new Dictionaries(node);
        EmitProg(node);
    }

    private void EmitProg(Node node){
        NodeList nodeList = new NodeList();
        Files.add(EmitMaster(node.FirstChild));

        nodeList.VisitSiblings(node.FirstChild);
        int slaveAddress = 1;
        for(Node slave : nodeList.nodeList){
            Files.add(EmitSlave(slave, slaveAddress));
            slaveAddress++;
        }
    }
    private String EmitMaster(Node node) {
        String content = "";
        NodeList nodeList = new NodeList();
        int name = 0;
        //TODO Husk at sætte h filer ind
        content += "void setup(){\n";
        content += EmitInitiate(node.FirstChild);
        content += "Wire.begin();";
        content += "}\n";

        content += "void loop() {\n";
        nodeList.VisitSiblings(node.FirstChild);
        for(Node listener : nodeList.nodeList){
            name++;
            content += EmitListener(listener, name);
        }
        content += "}\n";

        return content;
    }

    private String EmitSlave(Node node, int address) {
        String content = "";
        NodeList nodeList = new NodeList();
        content += "#include <Wire.h>\n";
        content += "const int commandSize =" + CommandSize + ";"; //TODO: Determine command size
        content += "char input[commandSize];";
        content += "void setup(){\n";
        content += EmitInitiate(node.FirstChild);
        content += "Wire.begin(" + address + ");"; // TODO: Add slave address to dictionary?
        content += "Wire.onReceive(receiveEvent);";
        content += "}\n";

        content += "void loop() {\n";

        content += "}\n";
        //TODO: Determine how to distribute events
        content += "void receiveEvent(int eventSize)\n" +
                "{\n" +
                "  for (int i = 0; i < commandSize-1 || i < eventSize; i++) {\n" +
                "    char c = Wire.read();\n" +
                "    input[i] = c;\n" +
                "  }";

        nodeList.VisitSiblings(node.FirstChild);
        for(Node EventHandler : nodeList.nodeList){
            content += Files.add(EmitEventHandler(EventHandler));
        }

        content += "}";
        return content;
    }

    private String EmitInitiate(Node node) {
        String content = "";
        NodeList nodeList = new NodeList();

        nodeList.VisitChildren(node);
        for(Node pinDecl : nodeList.nodeList){
            content += EmitPinDeclaration(pinDecl);
        }
        return content;
    }

    private String EmitListener(Node node, int functionName) {
        String content = "";
        String pinName = node.FirstChild.Value;
        content += "reading" + functionName + " = ??? (" + functionName + ");\n"; //TODO find ud af hvordan man finde typen af pin, brug symbol table. poin dictionary

        content += EmitBlock(node.FirstChild.Next);

        return content;
    }

    private String EmitEventHandler(Node node) {
        String content = "";
        String eventName = node.FirstChild.Value;

        content += "if (input[0] == " + EventDictionary.GetEventID(eventName) + ")";
        content += EmitBlock(node.FirstChild.Next);

        return content;
    }

    private String EmitBlock(Node node) {
        String content = "";
        NodeList nodeList = new NodeList();

        content += "{\n";
        nodeList.VisitChildren(node);
        for(Node statement : nodeList.nodeList){
            if(statement.Type == AST.NodeType.Call){
                content += EmitCall(statement);
            }
            else if(statement.Type == AST.NodeType.BoolDeclaration){
                content += EmitBoolDcl(statement);
            }
            else if(statement.Type == AST.NodeType.IntDeclaration){
                content += EmitIntDcl(statement);
            }
            else if(statement.Type == AST.NodeType.FloatDeclaration){
                content += EmitFloatDcl(statement);
            }
            else if(statement.Type == AST.NodeType.EventDeclaration){
                int IDNumber = 1;
                content += EmitEventDcl(statement, IDNumber);
                IDNumber++;
            }
            else if(statement.Type == AST.NodeType.Assignment){
                content += EmitAssignment(statement);
            }
            else if(statement.Type == AST.NodeType.If){
                content += EmitIf(statement);
            }
        }
        content += "}\n";

        return content;
    }

    private String EmitEventDcl(Node node, int IDNumber) {
        String content = "";
        EventDictionary.AddNewEvent(node.FirstChild.Value, "" + IDNumber, "");
        content = "char " + node.FirstChild.Value + "[] = \"" + IDNumber + "\"";
        if (node.FirstChild.Next.Type == AST.NodeType.Expression){ //TODO typen kan være Call eller identifier
            content += EmitExpression(node.FirstChild.Next);
        }
        else{
            content += ";\n";
        }

        return content;
    }

    private String EmitFloatDcl(Node node) {
        String content = "";

        content = "float " + node.Value;
        if (node.FirstChild.Next.Type == AST.NodeType.Expression){
            content += EmitExpression(node.FirstChild.Next);
        }
        else{
            content += ";\n";
        }
        return content;
    }

    private String EmitIntDcl(Node node) {
        String content = "";

        return content;
    }

    private String EmitBoolDcl(Node node) {
        String content = "";

        return content;
    }

    private String EmitCall(Node node) {
        String content = "";

        if(node.FirstChild.Type == AST.NodeType.CreateEvent){
            EventDictionary.EditEventValue(node.FirstChild.Next.Value, "");
            content += "";
        }
        else if(node.FirstChild.Type == AST.NodeType.Broadcast){
            content += "";
        }

        return content;
    }

    private String EmitPinDeclaration(Node node) {
        String content = "";

        return content;
    }


    private String EmitAssignment(Node node) {
        String content = "";

        return content;
    }

    private String EmitIf(Node node) {
        String content = "";

        return content;
    }

    private String EmitFunction(Node node) {
        String content = "";

        return content;
    }

    private String EmitExpression(Node node) {
        String content = "";
        Node firstOperand = node.FirstChild;
        Node nextNode = firstOperand.Next;
        // if the next node is not operator or expression, simply emit first operand
        if (!IsOperator(nextNode) && nextNode.Type != AST.NodeType.Expression && !IsOperand(nextNode)) {
            if(firstOperand.Type == AST.NodeType.Identifier) {
                content += EmitIdentifier(firstOperand);
            }
            else if(IsLiteral(firstOperand)) {
                content += EmitLiteral(firstOperand);
            }
            else if(firstOperand.Type == AST.NodeType.Call) {
                content += EmitCall(firstOperand);
            }
            else {
                //TODO: Should this be an else-if instead?
                content += EmitExpression(firstOperand);
            }
        }
        // handle that the following node is an operator or expression;
        else {
            if(IsOperator(nextNode)) {
                content += EmitOperator(nextNode);
                if(CanBeExpression(nextNode.Next)) {
                    content += EmitExpression(nextNode.Next);
                }
            }
            else if(CanBeExpression(nextNode)){
                content += EmitExpression(nextNode);
            }
        }
        return content;
    }

    private String EmitIdentifier(Node node) {
        String content = "";

        return content;
    }

    private String EmitType(Node node) {
        String content = "";

        return content;
    }

    private String EmitOperator(Node node) {
        String content = "";

        return content;
    }

    private String EmitPrefix(Node node) {
        String content = "";

        return content;
    }

    private String EmitFunc(Node node) {
        String content = "";

        return content;
    }

    private String EmitReturns(Node node) {
        String content = "";

        return content;
    }

    private String EmitPinType(Node node) {
        String content = "";

        return content;
    }

    private String EmitIOType(Node node) {
        String content = "";

        return content;
    }

    private String EmitLiteral(Node node) {
        String content = "";

        return content;
    }

    private String EmitFilterType(Node node) {
        String content = "";

        return content;
    }

    private boolean IsOperator(Node node) {
        switch (node.Type) {
            case LessThan:
            case GreaterThan:
            case LessOrEqual:
            case GreaterOrEqual:
            case NotEqual:
            case Equals:
            case And:
            case Or:
            case Plus:
            case Minus:
            case Times:
            case Divide:
            case Modulo:
                return true;
            default:
                return false;
        }
    }

    private boolean IsOperand(Node node) {
        //TODO: Determine if prefixes cause issues
        switch (node.Type) {
            case IntLiteral:
            case FloatLiteral:
            case BoolLiteral:
            case Identifier:
            case Call:
            case Expression:
            case PrefixMinus:
            case PrefixNot:
                return true;
            default:
                return false;
        }
    }

    private boolean IsLiteral(Node node) {
        //TODO: Determine if prefixes cause issues
        switch (node.Type) {
            case IntLiteral:
            case FloatLiteral:
            case BoolLiteral:
            case PrefixMinus:
            case PrefixNot:
                return true;
            default:
                return false;
        }
    }

    private boolean CanBeExpression(Node node) {
        return IsOperand(node) || node.Type == AST.NodeType.Expression;
    }


}
