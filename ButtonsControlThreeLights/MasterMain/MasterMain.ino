const int commandSize = 5;

char mystr[commandSize] = "";
char instr[commandSize] = "";

char sig1[5] = "11";
char sig2[5] = "21";
char sig3[5] = "31";

const int inPin1 = 2;
const int inPin2 = 3;
const int inPin3 = 4;

int previous1 = LOW; // the previous reading from the input pin
int previous2 = LOW; 
int previous3 = LOW; 

int reading1; // the current reading from the input pin
int reading2;
int reading3;

// the follow variables are long's because the time, measured in miliseconds,
// will quickly become a bigger number than can be stored in an int.
long time1 = 0;         // the last time the output pin was toggled
long time2 = 0;
long time3 = 0;


long debounce = 300;   // the debounce time, increase if the output flickers

void setup()
{
  pinMode(inPin1, INPUT);
  pinMode(inPin2, INPUT);
  pinMode(inPin3, INPUT);

  mystr[4] = '\n'; // So that there is a newline in the output

  Serial.begin(9600);
  Serial1.begin(9600);
}

void loop()
{
  if (Serial1.available()) {
    Serial1.readBytes(instr, commandSize);
    Serial.println(instr);
    
  }

  reading1 = digitalRead(inPin1);
  reading2 = digitalRead(inPin2);
  reading3 = digitalRead(inPin3);

  // if the input just went from LOW and HIGH and we've waited long enough
  // to ignore any noise on the circuit, toggle the output pin and remember
  // the time´
  
  if (reading1 == HIGH && previous1 == LOW && millis() - time1 > debounce) {

    mystr[0] = sig1[0];
    mystr[1] = sig1[1];
    
    Serial1.write(mystr, commandSize);

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

    mystr[0] = sig2[0];
    mystr[1] = sig2[1];
    
    Serial1.write(mystr, commandSize);

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

    mystr[0] = sig3[0];
    mystr[1] = sig3[1];
    
    Serial1.write(mystr, commandSize);

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
