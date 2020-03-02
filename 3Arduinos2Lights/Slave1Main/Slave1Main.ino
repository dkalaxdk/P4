#include <Wire.h>
const int commandSize = 5;
const int pin = 7;
const int pin1 = 6;
char someString[commandSize] = "0";

void setup() {
  someString[1] = 1;
  someString[2] = 4;
  someString[3] = 0;
  someString[4] = '\0';
  Wire.begin(1);        // join i2c bus (address optional for master)
  Serial.begin(9600);  // start Serial for output
  Wire.onReceive(receiveEvent); // register event
  pinMode(pin,OUTPUT);
}

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
  */

void loop() {
}


void receiveEvent(int howMany)
{
  for (int i = 0; i < commandSize-1 || i < howMany; i++) {
    char c = Wire.read();
    someString[i] = c;
  }
  Serial.println(someString);
    if (someString[0] == '1') {
      if(someString[1] == '1') {
        light(true);
      } else {
        light(false);
      }
    }
    if (someString[0] == '2') {
      if(someString[1] == '1') {
        light1(true);
      } else {
        light1(false);
      }
    }

}

void light(bool on) {
  if (on) {
    digitalWrite(pin,HIGH);
  } else {
    digitalWrite(pin,LOW);
  }
}
void light1(bool on) {
  if (on) {
    digitalWrite(pin1,HIGH);
  } else {
    digitalWrite(pin1,LOW);
  }
}
