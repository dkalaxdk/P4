char mystr[5] = ""; //String data

void setup() {
  // Begin the Serial at 9600 Baud
  Serial.begin(9600);

  pinMode(LED_BUILTIN, OUTPUT);

  digitalWrite(LED_BUILTIN, HIGH);
}

void loop() {
  mystr[0] = '1';
  mystr[1] = '1';
  
  Serial.write(mystr,5); //Write the serial data
  
  delay(3000);


  mystr[0] = '1';
  mystr[1] = '0';
  
  Serial.write(mystr,5); //Write the serial data
  
  delay(3000);


  mystr[0] = '2';
  mystr[1] = '1';
  
  Serial.write(mystr, 5); //Write the serial data
  
  delay(3000);


  mystr[0] = '2';
  mystr[1] = '0';
  
  Serial.write(mystr,5); //Write the serial data
  
  delay(3000);


  mystr[0] = '3';
  mystr[1] = '1';
  
  Serial.write(mystr,5); //Write the serial data
  
  delay(3000);
  
  
  mystr[0] = '3';
  mystr[1] = '0';
  
  Serial.write(mystr,5); //Write the serial data
  
  delay(3000);
 
}
