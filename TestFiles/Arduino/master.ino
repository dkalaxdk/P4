#include <ebal.h> 
#include <ebalEvent.h> 
#include <ebalPin.h>

ebalPin button1;
ebalPin button2;

intEvent button1Pressed;
intEvent button2Pressed;

void setup() {
	button1.createPin(digital, input, 4);
	button2.createPin(digital, input, 5);

	button1Pressed.setID(0);
	button2Pressed.setID(1);

	button1Pressed.addSlave(0);
	button2Pressed.addSlave(0);

	Wire.begin();
}

void button1Listener0() {
	int filteredInput = button1.filterNoise(debounce);
if ((filteredInput == 1)) {
		button1Pressed.createEvent(filteredInput);
		button1Pressed.broadcast();
	}
	else {
		button2Pressed.createEvent(filteredInput);
		button1Pressed.broadcast();
	}
}
void button2Listener1() {
if ((1 > 2)) {
		int a = 3;
	}
	else if ((2 > 3)) {
		int a = 4;
	}
	else {
		int a = 5;
	}
if ((1 > 2)) {
if ((5 > 4)) {
		}
	}
	else if ((2 > 3)) {
		int a = 4;
	}
	else {
		int a = 5;
	}
}

void loop() {
	button1.readPin();
	button2.readPin();
	button1Listener0();
	button2Listener1();
}
