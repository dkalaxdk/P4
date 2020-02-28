const int commandSize = 5;
const int pin1 = 2;
const int pin2 = 3;
const int pin3 = 4;
char InputString[commandSize]; //Initialized variable to store recieved data
char OutputString[commandSize];
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

void setup() {
  // Begin the Serial1 at 9600 Baud
  Serial.begin(9600);
  pinMode(pin1,OUTPUT);
  pinMode(pin2,OUTPUT);
  pinMode(pin3,OUTPUT);
}


void WriteResponse(char input) {
  memset(OutputString, 0, commandSize);
  OutputString[0] = input;
  Serial.write(OutputString, commandSize);
}

void loop() {
  if (Serial.available()) {
    Serial.readBytes(InputString, commandSize); //Read the Serial1 data and store in var

    if (InputString[0]=='1'){
      if(InputString[1]=='1'){
        digitalWrite(pin1, HIGH);
      }
      else {
        digitalWrite(pin1, LOW);
      }

      WriteResponse('1');

    }
    else if (InputString[0]=='2') {
      if (InputString[1]=='1'){
        digitalWrite(pin2, HIGH);
      }
      else {
        digitalWrite(pin2, LOW);
      }

      WriteResponse('2');

    }
    else if (InputString[0]=='3') {
      if(InputString[1]=='1'){
        digitalWrite(pin3, HIGH);
      }
      else {
        digitalWrite(pin3, LOW);
      }

      WriteResponse('3');

    }
}
}
