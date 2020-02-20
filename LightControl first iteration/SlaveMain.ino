char mystr[5]; //Initialized variable to store recieved data
int pin1 = 2;
int pin2 = 3;
int pin3 = 4;


void setup() {
  // Begin the Serial at 9600 Baud
  Serial.begin(9600);
  pinMode(pin1,OUTPUT);
  pinMode(pin2,OUTPUT);
  pinMode(pin3,OUTPUT);
}

void loop() {
  if(Serial.available()) {
    Serial.readBytes(mystr,3); //Read the serial data and store in var
    if(mystr[0]=='1') {
      if(mystr[1]=='1'){
        digitalWrite(pin1, HIGH);
      } else{
        digitalWrite(pin1, LOW);
      }
    } else if(mystr[0]=='2') {
      if(mystr[1]=='1'){
        digitalWrite(pin2, HIGH);
      } else{
        digitalWrite(pin2, LOW);
      }
    } else if(mystr[0]=='3') {
      if(mystr[1]=='1'){
        digitalWrite(pin3, HIGH);
      } else{
        digitalWrite(pin3, LOW);
      }
    }
    Serial.println(mystr); //Print data on Serial Monitor
    memset(mystr, 0, sizeof(mystr));
	}
}