# P4: EBAL

## Project description:

This project is developed as a semester project on the fourth semester on AAU. The goal of the project was to design a language, and implement a corresponding compiler for the language.

The result is a compiler, built around the principles of **Crafting a Compiler**, **ISBN-13:** 978-0136067054 and the courses of the semester.

The language designed in this semester is an event based Arduino language, called EBAL, which goal is to abstract away as many details as possible regarding the communication between multiple Arduino boards. The aim is to help with making home automation easier, and to remove the boilerplate code.

The communication works by having one Master board, containing the input pins that needs to be monitored, and one or more slave boards, containing the output pins of the system.

## How to use:
The project can be compiled using any java compiler. Within the EBALArduino folder there are some examples of the output code after the compiler have been run on the input files seen in **DO SAVE THESE SOMEWHERE OR REFERENCE THEM**. <br>
Example code written in EBAL can be seen in EBAL.Zip


After compilation, the output code needs to be compiled using an Arduino IDE or compiler. The compiled code utilises a library which can be found in [Reference to Arduino library](https://www.youtube.com/watch?v=dQw4w9WgXcQ). <br>
When installing the library to the Arduino IDE, follow this guide: [Arduino library installation](https://www.arduino.cc/en/guide/libraries).
When this is done the output code from the compiler can be run as any other Arduino code.

The output of the EBAL compiler will be multiple files, a file for the Master board, and one for each slave. <br>
These needs to be compiled in the Arduino IDE one by one, and then be put on the respective Arduino boards. When compiling the files through the Arduino IDE, each file needs to be added to a folder with the same name of the output file.

## General code overview:
The compiler is implemented using a recursive descent parser, as the designed language is LL(1).

EBAL was designed and implemented as a semester project, and is therefore not fully implemented. The language does not contain any iterative control structures, arrays or strings, nor does it contain a way for the slaves to communicate with the master board.
**Evt skriv noget mere**
