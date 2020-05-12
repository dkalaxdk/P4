#include <ebal.h> 
#include <ebalEvent.h> 
#include <ebalPin.h>

ebalPin button1;
ebalPin button2;
ebalPin poten;

boolEvent button1Pressed;
intEvent button2Pressed;
intEvent potenTurned;

int a;

void setup() {
	button1.createPin(digital, input, 7);
	button2.createPin(digital, input, 8);
	poten.createPin(analog, input, A0);

	button1Pressed.setID(0);
	button2Pressed.setID(1);
	potenTurned.setID(2);

	button1Pressed.addSlave(0);
	button2Pressed.addSlave(0);
	potenTurned.addSlave(1);

	Wire.begin();
	Serial.begin(9600);
}

void button1Listener0() {
	int filteredInput = button1.filterNoise(debounce);
	if ((filteredInput == 1)) {
		button1Pressed.createEvent(true);
		button1Pressed.broadcast();
	}
}

void button2Listener1() {
	int filteredInput = button2.filterNoise(constant);
	if ((filteredInput != -1)) {
		button2Pressed.createEvent(filteredInput);
		button2Pressed.broadcast();
	}
}

void potenListener2() {
	int filteredInput = poten.filterNoise(range);
	if ((filteredInput != -1)) {
		potenTurned.createEvent(filteredInput);
		potenTurned.broadcast();
	}
}


void loop() {
	button1.readPin();
	button2.readPin();
	poten.readPin();
	button1Listener0();
	button2Listener1();
	potenListener2();
}
