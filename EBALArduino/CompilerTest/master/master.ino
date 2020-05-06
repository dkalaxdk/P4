#include <ebal.h> 
#include <ebalEvent.h> 
#include <ebalPin.h>

ebalPin button1;
ebalPin button2;
ebalPin poten;

intEvent button1Pressed;
intEvent button2Pressed;
intEvent potenTurned;

void setup() {
  button1.createPin(digital, input, 7);
  button2.createPin(digital, input, 8);
  poten.createPin(analog, input, A0);

  button1Pressed.setID(0);
  button2Pressed.setID(1);
  potenTurned.setID(2);

  button1Pressed.addSlave(0);
  button2Pressed.addSlave(1);
  potenTurned.addSlave(1);

  Wire.begin();
  Serial.begin(9600);
}

void button1Listener0() {
  int filteredInput = button1.filterNoise(debounce);
  if ((filteredInput == 1)) {
    button1Pressed.createEvent(32000);
    button1Pressed.broadcast();
  }
}

void button2Listener1() {
  int filteredInput = button2.filterNoise(debounce);
  if ((filteredInput == 1)) {
    button2Pressed.createEvent(-32000);
    button2Pressed.broadcast();
  }
}

void potenListener2() {
  int filteredInput = poten.filterNoise(range);
  if ((filteredInput != -1)) {
    Serial.println(filteredInput);
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
