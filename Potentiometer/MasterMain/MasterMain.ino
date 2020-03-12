#include <Wire.h>

const int commandSize = 5;
byte outbytes[commandSize] = "";

const int inPin1 = 2;

int reading1;
int previous1 = 0;

void setup()
{
  pinMode(inPin1, INPUT);

  for (int i = 0; i < commandSize - 1; i++) {
    outbytes[i] = 'q';
  }
  Wire.begin(0); // join i2c bus (address optional for master)
  Wire.onReceive(receiveEvent);

  Serial.begin(9600);
}

void receiveEvent(int howMany) {

  char c[100] = "";

  for (int i = 0; i < howMany; i++) {
    c[i] = Wire.read();
  }

  Serial.println(c);
}

void loop()
{ 
  reading1 = analogRead(inPin1);

  reading1 = map(reading1, 0, 1023, 0, 255);
  
  if (isDifferent(reading1, previous1, 5)) {
    outbytes[0] = '1';
    outbytes[1] = reading1;
  
    Wire.beginTransmission(1);
    Wire.write(outbytes, commandSize);
    Wire.endTransmission();

    previous1 = reading1;    
  }  
}

bool isDifferent(int a, int b, int value) {
  return a - b >= value || b - a >= value;
}
