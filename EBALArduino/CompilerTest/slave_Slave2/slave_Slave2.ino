#include <ebal.h> 
#include <ebalEvent.h> 
#include <ebalPin.h>

ebalPin out;

intEvent button2Pressed;
intEvent potenTurned;

void setup() {
  out.createPin(analog, output, LED_BUILTIN);

  button2Pressed.setID(1);
  potenTurned.setID(2);

  Wire.begin(1);
  Serial.begin(9600);
  Wire.onReceive(receiveEvent);
}

void button2PressedEventHandler0() {
  if ((button2Pressed.getValue() == -32000)) {
    if ((out.getValue() == 1)) {
      out.write(0);
    }
    else {
      out.write(1);
    }
  }
}

void potenTurnedEventHandler1() {
  Serial.println(potenTurned.getValue());
  out.write(potenTurned.getValue());
}


void receiveEvent(int howMany) {
  char eventID = Wire.read();

  if (eventID == button2Pressed.getID()) {
    button2Pressed.createEvent();
    button2PressedEventHandler0();
  }
  if (eventID == potenTurned.getID()) {
    potenTurned.createEvent();
    potenTurnedEventHandler1();
  }
}

void loop() {
  out.readPin();
}
