package sw417f20.ebal.CodeGeneration.Utility;

import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;

public class Master extends ArduinoBoard {

    public int ListenerCount = 0;
    public ArrayList<String> Listeners;

    public ArrayList<String> AssociatedSlaves;

    public Master() {
        AssociatedSlaves = new ArrayList<>();
        Listeners = new ArrayList<>();
    }

    @Override
    public void AddEventDeclaration(Event event) {
        this.EventDeclarations.add(event.GetType() + " " + event.GetName() + ";\n");

        this.EventInstantiations.add(event.GetName() + ".setID(" + event.GetID() + ");\n");

        for (int i : event.AssociatedSlaves) {
            this.AssociatedSlaves.add(event.GetName() + ".addSlave(" + i + ");\n");
        }
    }

    @Override
    public String toString() {
        StringBuilder masterBuilder = new StringBuilder();

        masterBuilder.append(libraries);

        masterBuilder.append(AddArray(PinDeclarations));
        masterBuilder.append("\n");
        masterBuilder.append(AddArray(EventDeclarations));
        masterBuilder.append("\n");

        masterBuilder.append("void setup() {\n");
        indentation++;
        masterBuilder.append(AddArray(PinInstantiations));
        masterBuilder.append("\n");
        masterBuilder.append(AddArray(EventInstantiations));
        masterBuilder.append("\n");
        masterBuilder.append(AddArray(AssociatedSlaves));
        masterBuilder.append("\n");
        masterBuilder.append("\tWire.begin();");
        masterBuilder.append("\n}\n\n");
        indentation--;

        masterBuilder.append(AddArray(Listeners));
        masterBuilder.append("\n");

        indentation++;
        masterBuilder.append("void loop() {\n");
        masterBuilder.append(AddArray(Loop));
        masterBuilder.append("}\n");
        indentation--;

        return masterBuilder.toString();
    }
}
