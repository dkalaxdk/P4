const int commandSize = 32;
const int pin1 = 2;
const int pin2 = 3;
const int pin3 = 4;
char InputOutputString[commandSize]; //Initialized variable to store recieved data


void setup() {
  // Begin the Serial1 at 9600 Baud
  Serial.begin(9600);
  pinMode(pin1,OUTPUT);
  pinMode(pin2,OUTPUT);
  pinMode(pin3,OUTPUT);
}


void WriteResponse(char input) {
  memset(InputOutputString, 0, commandSize);
  InputOutputString[0] = input;
  Serial.write(InputOutputString,commandSize);
}

void loop() {
  if(Serial.available()) {
    Serial.readBytes(InputOutputString,commandSize); //Read the Serial1 data and store in var
    if(InputOutputString[0]=='1'){
      if(InputOutputString[1]=='1'){
        digitalWrite(pin1, HIGH);
      } else{
        digitalWrite(pin1, LOW);
      }
      WriteResponse('1');

    }
    else if(InputOutputString[0]=='2') {
      if(InputOutputString[1]=='1'){
        digitalWrite(pin2, HIGH);
      } else{
        digitalWrite(pin2, LOW);
      }
      WriteResponse('2');

    } else if(InputOutputString[0]=='3') {
      if(InputOutputString[1]=='1'){
        digitalWrite(pin3, HIGH);
      } else{
        digitalWrite(pin3, LOW);
      }

      WriteResponse('3');
    }
}
}
