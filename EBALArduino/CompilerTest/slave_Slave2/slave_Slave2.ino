#include <ebal.h> 
#include <ebalEvent.h> 
#include <ebalPin.h>

ebalPin out;

intEvent potentiometerTurned;


void setup() {
	out.createPin(analog, output, LED_BUILTIN);

	potentiometerTurned.setID(2);

	Wire.begin(1);
	Serial.begin(9600);
	Wire.onReceive(receiveEvent);
}

void potentiometerTurnedEventHandler0() {
	out.write(potentiometerTurned.getValue());
}


void receiveEvent(int howMany) {
	char eventID = Wire.read();

	if (eventID == potentiometerTurned.getID()) {
		potentiometerTurned.createEvent();
		potentiometerTurnedEventHandler0();
	}
}

void loop() {
	out.read();
}
