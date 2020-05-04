#include <ebal.h> 
#include <ebalEvent.h> 
#include <ebalPin.h>

ebalPin button1;
ebalPin button2;
intEvent button1Pressed;
void setup() {
button1.createPin(digital, input, 4);
button2.createPin(digital, input, 5);
button1Pressed.setID(0);
button1Pressed.addSlave(0);
Wire.begin();

}
void button1Listener0() {
int filteredInput = button1.filterNoise(debounce);;
if((filteredInput == 1)) {
button1Pressed.createEvent(filteredInput);button1Pressed.broadcast();
}

}
void loop() {
button1.readPin();
button2.readPin();
button1Listener0();
}
