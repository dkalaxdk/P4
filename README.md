# P4:EBAL

## Project description:

This project is developed as a semester project on the fourth semester on AAU. The goal of the project was to create design a language, and implement a corresponding compiler for the language. 
The result is a compiler, built around the principles of Crafting a Compiler, ISBN-13: 978-0136067054 and the courses of the semester.

The language designed in this semester is a event based Arduino language, which goal is to abstract away as many details as possible when communicating with multiple Arduino boards. The aim were to help with making home Automation easier, and to remove the boilerplate code.
The communication works by having one board being the Master board, this board should contain the input pins that needs to be monitored.
One or more Arduino boards are also required to act as slaves, these boards should contain the output pins of the system.

## How to use:

The project needs to be compiled using any java compiler, within the EBALArduino folder there are some examples of the output code after the compiler have been run on inputs files seen in DO SAVE THESE SOMEWHERE OR REFERENCE THEM.

Any code compiled by the compiler needs to be compiled for the Arduino using a Arduino IDE or compiler. 
The compiled code utilises a library witch can be found in [Reference to Arduino library](https://www.youtube.com/watch?v=dQw4w9WgXcQ). 
When installing the library to the Arduino IDE, follow this guide: [Arduino library installation](https://www.arduino.cc/en/guide/libraries).
When this is done the output code from the compiler can be run as any other Arduino code.

The output of the compiler will be multiple files, a file for the Master board, and for each slave.

## General code overview:



