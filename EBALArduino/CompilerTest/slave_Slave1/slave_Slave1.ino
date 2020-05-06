#include <ebal.h> 
#include <ebalEvent.h> 
#include <ebalPin.h>

ebalPin out;

intEvent button1Pressed;

void setup() {
  out.createPin(digital, output, LED_BUILTIN);

  button1Pressed.setID(0);

  Wire.begin(0);
  Serial.begin(9600);
  Wire.onReceive(receiveEvent);
}

void button1PressedEventHandler0() {
  if ((button1Pressed.getValue() == 32000)) {
    if ((out.getValue() == 1)) {
      out.write(0);
    }
    else {
      out.write(1);
    }
  }
}


void receiveEvent(int howMany) {
  char eventID = Wire.read();

  if (eventID == button1Pressed.getID()) {
    button1Pressed.createEvent();
    button1PressedEventHandler0();
  }
}

void loop() {
  out.readPin();
}
