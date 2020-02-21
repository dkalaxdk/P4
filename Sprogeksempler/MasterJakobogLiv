PinEvent inPin1 = new PinEvent(2, button);
PinEvent inPin2 = new PinEvent(3, button);
PinEvent inPin3 = new PinEvent(4, button);

//Tingene slaven styrre
int led1 = 1;
int led2 = 2;
int led3 = 3;

void setupMaster()
{
  pinMode(inPin1.ID, INPUT);
  pinMode(inPin2.ID, INPUT);
  pinMode(inPin3.ID, INPUT);

  Slave Slave1 = NewSlave(1);
}

void loop()
{
  if (inPin1.Changed() == true) {
    WriteToSlave(Slave1, led1);
  }
  if (inPin2.Changed() == true) {
    WriteToSlave(Slave1, led2);
  }
  if (inPin3.Changed() == true) {
    WriteToSlave(Slave1, led3);
  }
}

//Denne funktion burde gerne kunne undlades!!!!!!!!
void WriteToSlave(Slave slave, int ComponentID){
  {
    Serial.write(slave.ID.ToString() +','+ ComponentID.TOString())
  }
}


//burde være defineret i selve sproget
class PinEvent{
  private{
    int currentValue;
    int previousValue;
    int time;
    int debounce = 300;
  }
  public{
    int ID;
    string Event;
    boolean value;

    PinEvent(int ID, string Event){
      this.ID = ID;
      this.Event = Event;
    }
    Boolean Changed(){
      if(Event == "button"){
        return ButtonPressed();
      }
    }

    Boolean ButtonPressed(){
      currentValue = digitalRead(ID);
      if (currentValue == HIGH && previousValue == LOW && millis() - time > debounce) {
      time = millis();
      return true;
      }
      else return false;
    }

  }

}
