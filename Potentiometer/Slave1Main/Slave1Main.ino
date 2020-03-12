/*
  _________         .    .
  (..       \_    ,  |\  /|
  \       O  \  /|  \ \/ /
   \______    \/ |   \  /
      vvvv\    \ |   /  |
      \^^^^  ==   \_/   |
       `\_   ===    \.  |
       / /\_   \ /      |
       |/   \_  \|      /
              \________/
      HAJ
*/
#include <Wire.h>
const int commandSize = 5;
const int pin = 9;
const int pin1 = 6;
byte someString[commandSize] = "0";



void setup() {
  Wire.begin(1);                // join i2c bus (address optional for master)
  Serial.begin(9600);           // start Serial for output
  Wire.onReceive(receiveEvent); // register event
  pinMode(pin,OUTPUT);
  pinMode(pin1,OUTPUT);
}


void loop() {
  Wire.beginTransmission(0);
  Wire.write("TESTING");
  Wire.endTransmission();
  delay(2000);
}


void receiveEvent(int howMany)
{
  for (int i = 0; i < commandSize-1 || i < howMany; i++) {
    byte c = Wire.read();
    someString[i] = c;
  }

  if (someString[0] == '1') {
    light(pin,someString[1]);
  }
  
  if (someString[0] == '2') {
    light(pin1,someString[1]);
  }
}

void light(int pin,int value) {
  analogWrite(pin,value);
}
