#include <ebal.h> 
#include <ebalEvent.h> 
#include <ebalPin.h>

ebalPin out;

intEvent potenTurned;


void setup() {
	out.createPin(analog, output, LED_BUILTIN);

	potenTurned.setID(2);

	Wire.begin(1);
	Serial.begin(9600);
	Wire.onReceive(receiveEvent);
}

void potenTurnedEventHandler0() {
	out.write(potenTurned.getValue());
}


void receiveEvent(int howMany) {
	char eventID = Wire.read();

	if (eventID == potenTurned.getID()) {
		potenTurned.createEvent();
		potenTurnedEventHandler0();
	}
}

void loop() {
	out.read();
}
