#include <ebal.h> 
#include <ebalEvent.h> 
#include <ebalPin.h>

ebalPin out;

boolEvent button1Pressed;
intEvent button2Pressed;


void setup() {
	out.createPin(digital, output, LED_BUILTIN);

	button1Pressed.setID(0);
	button2Pressed.setID(1);

	Wire.begin(0);
	Serial.begin(9600);
	Wire.onReceive(receiveEvent);
}

void button1PressedEventHandler0() {
	if (button1Pressed.getValue()) {
		if ((out.getValue() == 1)) {
			out.write(0);
		}
		else {
			out.write(1);
		}
	}
}

void button2PressedEventHandler1() {
	if ((button2Pressed.getValue() == 1)) {
		out.write(1);
	}
	else {
		out.write(0);
	}
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
	out.read();
}
