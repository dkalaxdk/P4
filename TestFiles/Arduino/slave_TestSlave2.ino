#include <ebal.h> 
#include <ebalEvent.h> 
#include <ebalPin.h>

ebalPin noget;

intEvent button1Pressed;

void setup() {
	noget.createPin(digital, output, 1);

	button1Pressed.setID(0);

	Wire.begin(1);
	Wire.onReceive(receiveEvent);
}

void button1PressedEventHandler0() {
	int a = 3;
}

void receiveEvent(int howMany) {
	char eventID = Wire.read();

	if (eventID == button1Pressed.getID()) {
		button1Pressed.createEvent();
		button1PressedEventHandler0();
	}
}

void loop() {
	noget.readPin();
}
