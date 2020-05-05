#include <ebal.h> 
#include <ebalEvent.h> 
#include <ebalPin.h>

ebalPin button1;
ebalPin button2;
ebalPin button3;
ebalPin button4;

intEvent button1Pressed;
intEvent button2Pressed;

void setup() {
	button1.createPin(digital, input, 4);
	button2.createPin(digital, input, 5);
	button3.createPin(digital, input, 6);
	button4.createPin(digital, input, 7);

	button1Pressed.setID(0);
	button2Pressed.setID(1);

	button1Pressed.addSlave(0);
	button2Pressed.addSlave(0);

	Wire.begin();
}

void button1Listener0() {
	int filteredInput = (1 / button1.filterNoise(debounce));
	int a = (1 / button1.getValue());
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
			int a = 1;
		}
		else {
			if (true) {
				int a = 3;
			}
			else if (false) {
				int a = 2;
			}
			else {
				int a = 1;
			}
		}
	}
	else if ((2 > 3)) {
		int a = 4;
	}
	else {
		int a = 5;
	}
}
void button3Listener2() {
	int TEST = -1;
	int a = (1 + (2 + 3));
	int b = ((1 + 2) + 3);
	int c = (1 + (2 + (3 + 4)));
	int d = (((1 + 2) + 3) + 4);
	int e = (1 + ((2 + 3) + 4));
	int f = ((1 + 2) + (3 + 4));
}
void button4Listener3() {
	int a = 1;
	int b = 2;
	int c = (a / b);
	c = (a % b);
}

void loop() {
	button1.readPin();
	button2.readPin();
	button3.readPin();
	button4.readPin();
	button1Listener0();
	button2Listener1();
	button3Listener2();
	button4Listener3();
}
