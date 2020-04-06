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
        for(Node slave : nodeList.nodeList){
            Files.add(EmitSlave(slave));
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

    private String EmitSlave(Node node) {


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

    }

    private String EmitBlock(Node node) {

    }

    private String EmitPinDeclaration(Node node) {

    }

    private String EmitDeclaration(Node node) {

    }

    private String EmitAssignment(Node node) {

    }

    private String EmitIf(Node node) {

    }

    private String emitCall(Node node) {

    }

    private String emitFunction(Node node) {

    }

    private String emitExpression(Node node) {

    }

    private String emitIdentifier(Node node) {

    }

    private String emitType(Node node) {

    }

    private String emitOperator(Node node) {

    }

    private String emitPrefix(Node node) {

    }

    private String emitFunc(Node node) {

    }

    private String emitReturns(Node node) {

    }

    private String emitPinType(Node node) {

    }

    private String emitIOType(Node node) {

    }

    private String emitLiteral(Node node) {

    }

    private String emitFilterType(Node node) {

    }


}
