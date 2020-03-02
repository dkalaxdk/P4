#include <Wire.h>

const int commandSize = 5;
char outstr[commandSize] = "";

char sig1[commandSize] = "11";
char sig2[commandSize] = "21";
char sig3[commandSize] = "11";

const int inPin1 = 2;
const int inPin2 = 3;
const int inPin3 = 4;

int previous1 = LOW;
int previous2 = LOW;
int previous3 = LOW;

int reading1; 
int reading2;
int reading3;

long time1 = 0; // the last time the output pin was toggled
long time2 = 0;
long time3 = 0;

const long debounce = 300;

void setup()
{
  pinMode(inPin1, INPUT);
  pinMode(inPin2, INPUT);
  pinMode(inPin3, INPUT);

  for (int i = 0; i < commandSize - 1; i++) {
    outstr[i] = 'q';
  }

  Serial.begin(9600);

  Wire.begin(); // join i2c bus (address optional for master)
}

void loop()
{ 
  reading1 = digitalRead(inPin1);
  reading2 = digitalRead(inPin2);
  reading3 = digitalRead(inPin3);

  if (reading1 == HIGH && previous1 == LOW && millis() - time1 > debounce) {

    outstr[0] = sig1[0];
    outstr[1] = sig1[1];

    Wire.beginTransmission(1);
    Wire.write(outstr);
    Wire.endTransmission();

    Serial.println(outstr);

    if (sig1[1] == '1') {
      sig1[1] = '0';
    }
    else {
      sig1[1] = '1';
    }

    time1 = millis();    
  }

  previous1 = reading1;

  if (reading2 == HIGH && previous2 == LOW && millis() - time2 > debounce) {

    outstr[0] = sig2[0];
    outstr[1] = sig2[1];

    Wire.beginTransmission(1);
    Wire.write(outstr);
    Wire.endTransmission();

    Serial.println(outstr);

    if (sig2[1] == '1') {
      sig2[1] = '0';
    }
    else {
      sig2[1] = '1';
    }

    time2 = millis();    
  }

  previous2 = reading2;


  if (reading3 == HIGH && previous3 == LOW && millis() - time3 > debounce) {
    outstr[0] = sig3[0];
    outstr[1] = sig3[1];

    Wire.beginTransmission(2);
    Wire.write(outstr);
    Wire.endTransmission();

    Serial.println(outstr);

    if (sig3[1] == '1') {
      sig3[1] = '0';
    }
    else {
      sig3[1] = '1';
    }

    time3 = millis();    
  }

  previous3 = reading3;
}
