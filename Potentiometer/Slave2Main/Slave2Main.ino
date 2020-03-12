#include <Wire.h>
const int commandSize = 5;
const int pin = 7;
char someString[commandSize];

void setup() {
  Wire.begin(2);        // join i2c bus (address optional for master)
  Serial.begin(9600);  // start Serial for output
  Wire.onReceive(receiveEvent); // register event
  pinMode(pin,OUTPUT);
}

void loop() {
}


void receiveEvent(int howMany)
{
   for (int i = 0; i < commandSize-1 || i < howMany; i++) {
    char c = Wire.read();
    someString[i] = c;
  }
  if (someString[0] == '1') {
    analogWrite(pin,someString[1]);
  }
}
