#include <ebal.h> 
#include <ebalEvent.h> 
#include <ebalPin.h>

ebalPin light1;
ebalPin light2;
ebalPin light3;

intEvent button1Pressed;
intEvent button2Pressed;

void setup() {
	light1.createPin(digital, output, 4);
	light2.createPin(digital, output, 5);
	light3.createPin(digital, output, 6);

	button1Pressed.setID(0);
	button2Pressed.setID(1);

	Wire.begin(0);
	Wire.onReceive(receiveEvent);
}

void button1PressedEventHandler0() {
	int value = light1.getValue();
	if ((value == 1)) {
		light1.write(0);
	}
	else if ((value == 0)) {
		light1.write(1);
	}
	else {
		int b = 4;
	}
}
void button2PressedEventHandler1() {
	light1.write(1);
	light2.write(1);
	light3.write(1);
}

void receiveEvent(int howMany) {
	char eventID = Wire.read();

	if (eventID == button1Pressed.getID()) {
		button1Pressed.createEvent();
		button1PressedEventHandler0();
	}
	if (eventID == button2Pressed.getID()) {
		button2Pressed.createEvent();
		button2PressedEventHandler1();
	}
}

void loop() {
	light1.readPin();
	light2.readPin();
	light3.readPin();
}
