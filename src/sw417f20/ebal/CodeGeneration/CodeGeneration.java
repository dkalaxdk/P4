package sw417f20.ebal.CodeGeneration;

import sw417f20.ebal.SyntaxAnalysis.Node;

import java.lang.reflect.Array;
import java.net.ContentHandler;
import java.util.ArrayList;

public class CodeGeneration {
    ArrayList<String> Files;

    public CodeGeneration(Node node){
        EmitProg(node);
        Files = new ArrayList<String>();
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
        content += "const int commandSize =" + 5 + ";"; //TODO: Determine command size
        content += "byte input[commandSize];";
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
        content += "reading" + functionName + " = ??? (" + functionName + ");\n"; //TODO find ud af hvordan man finde typen af pin, brug symbol table

        content += EmitBlock(node.FirstChild.FirstSibling);

        return content;
    }

    private String EmitEventHandler(Node node) {
        String content = "";

        return content;
    }

    private String EmitBlock(Node node) {
        String content = "";

        return content;
    }

    private String EmitPinDeclaration(Node node) {
        String content = "";

        return content;
    }

    private String EmitDeclaration(Node node) {
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

    private String emitCall(Node node) {
        String content = "";

        return content;
    }

    private String emitFunction(Node node) {
        String content = "";

        return content;
    }

    private String emitExpression(Node node) {
        String content = "";

        return content;
    }

    private String emitIdentifier(Node node) {
        String content = "";

        return content;
    }

    private String emitType(Node node) {
        String content = "";

        return content;
    }

    private String emitOperator(Node node) {
        String content = "";

        return content;
    }

    private String emitPrefix(Node node) {
        String content = "";

        return content;
    }

    private String emitFunc(Node node) {
        String content = "";

        return content;
    }

    private String emitReturns(Node node) {
        String content = "";

        return content;
    }

    private String emitPinType(Node node) {
        String content = "";

        return content;
    }

    private String emitIOType(Node node) {
        String content = "";

        return content;
    }

    private String emitLiteral(Node node) {
        String content = "";

        return content;
    }

    private String emitFilterType(Node node) {
        String content = "";

        return content;
    }


}
