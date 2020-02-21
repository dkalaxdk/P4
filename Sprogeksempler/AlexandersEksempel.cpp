// Should be handled by the compiler, slave is a dataclass.
void slave(int inputport, boolean inputcanRespond) {
    int port = inputport;
    boolean canRespond = inputcanRespond;
    public boolean input = inputForThisDevice();

	   //Slave should contain an id, which should be set by the system itself.

	public void send(boolean input){
		//Some communication related code, might be autocompleted by the port.
	}

	public boolean input(ref OUTPUT) {

		return true;
	}

  private boolean inputForThisDevice() {
    //Checks if the input is for the current device, and returns true if it is for this device.
  }
}

/*
The Light on/Light off being send, could be converted to a bit representation,
or just a number, making them smaller to send, while still making more sence to
the writer within the editor.



*/


// -------------------- Start of actual code -------------------- //
void masterMain() {

  DigitalPin pin = new DigitalPin(5);
  DigitalPin pin2 = new DigitalPin(1);

  if (pin.input() == true) {
    slave1.send("lights on");
  }
  if (pin2.input() == true) {
    slave2.send("Open gate");
  }
}

slave slave1(serial,true) {
    DigitalPin pin = new DigitalPin(1);

      if (input.content() == "light on") {
        pin.out("HIGH");
      } if (input.content() == "light of") {
        pin.out("LOW");
      }
}

slave slave2(serial,true) {
    DigitalPin pin = new DigitalPin(1);

      if (input.content() == "Open gate") {
        pin.out("HIGH");
      } if (input.content() == "Close gate") {
        pin.out("LOW");
      }
}
