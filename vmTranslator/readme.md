# project 7-8: vmTranslator

(Was taken from: https://www.nand2tetris.org/project07 and from: https://www.nand2tetris.org/project08)
## Project 7: Virtual Machine I - Stack Arithmetic
### Background
Java (or C#) compilers generate code written in an intermediate language called bytecode (or IL). \
This code is designed to run on a virtual machine architecture like the JVM (or CLR). \
One way to implement such VM programs is to translate them further into lower-level programs written in the machine language of some concrete (rather than virtual) host computer.\
In projects 7 and 8 we build such a VM translator, designed to translate programs written in the VM language into programs written in the Hack assembly language. 

### Objective
Build a basic VM translator, focusing on the implementation of the VM language's stack arithmetic and memory accesscommands. 
In Project 8, this basic translator will be extended into a full-scale VM translator.

## Project 8: Virtual Machine II - Program Control
### Background
We continue building the VM translator - a program that translates programs written in the VM language into programs written in the Hack machine language. \
This is a respectable chunk of engineering, so we are doing it in two stages. Welcome to stage II.

### Objective
Extend the basic VM translator built in project 7 into a full-scale VM translator. \
In particular, in project 7 we focused on handling the stack arithmetic and memory access commands of the VM language. \
We now turn to handle the VM language's branching and function calling commands.

## How to use?
Run:  java VMTranslator fileName.vm, where the string fileName.vm is the translator's input, i.e. the name of a text file containing VM commands.\
The translator creates an output file named fileName.asm, which is stored in the same directory of the input file. The name of the input file may contain a file path.
