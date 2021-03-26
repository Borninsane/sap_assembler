# sap_assembler
an assembler for the 8bit breadboard cpu.

An assembler for the 8bit cpu with 256bytes of addressing capability.
The assembler has two modes, 1 : "console to file"(c2f) where you write the mnemonics in the console and finaly the console gets terminated by ';', after that a "*.o" file appears in the current directory which is basically an object file which can only be viewed by a hex editor. 2 : a "file to file" mode where you create a "*.asm" file and edit it using your editor and finally use the assembler to assemble the code and again an object file generated, which can be opened by a hex editor and you get the opcodes for the program. It also features an hexdump option by which you can use it as a hex viewer. It can dump any file in raw hex code.

The syntax is simple, the instructions are listed below. To comment a line put "$" in starting of the line, to create a label(useful for loops) put a "*" before the label.

To get the list of options type "java sasm" without quotes.

You can try writing some assembly language programs using the below mnemonics :)

nop : no operation.\n
lda : load accumulator from an 8 bit address.
sta : store accumulator to 8bit address.
ldi : load accumulator immidiate with 8bit data.
addz : add immidiate the data from th address specified after this instructruction.
jmp : unconditional jump.
subz : subtract immidiate the data from the address specified after this instructruction.
adi : add immidiate the data specified after this instructruction.
subi : subtract immidiate the data from th address specified after this instructruction.
jz : jump if the previous operation resulted to zero.
jc : jump if the previous operation resulted to a carry.
ldax b : load accumulator indirect from the address specified by the data in register b.
ldax c : load accumulator indirect from the address specified by the data in register c.
ldax d : load accumulator indirect from the address specified by the data in register d.
stax b : store accumulator indirect to the address specified by the data in register b.
stax c : store accumulator indirect to the address specified by the data in register c.
stax d : store accumulator indirect to the address specified by the data in register d.
mvi b : move 8bit data immidiate to the register b which is kept immidiate after this instruction.
mvi c : move 8bit data immidiate to the register c which is kept immidiate after this instruction.
mvi d : move 8bit data immidiate to the register d which is kept immidiate after this instruction.
mov a,b : move contents of accumulator to register b.
mov a,c : move contents of accumulator to register c.
mov a,d : move contents of accumulator to register d.
mov b,a : move contents of register b to accumulator.
mov b,c : move contents of register b to register c.
mov b,d : move contents of register b to register d.
mov c,a : move contents of register c to accumulator.
mov c,b : move contents of register c to register b.
mov c,d : move contents of register c to register d.
mov d,a : move contents of register d to accumulator.
mov d,b : move contents of register d to register b.
mov d,c : move contents of register d to register c.
add a : add the contents of accumulator with accumulator and keep the results in accumulator.(double the accumulator ;) ).
add b : add the contents of register b with accumulator and keep the results in accumulator.
add c : add the contents of register c with accumulator and keep the results in accumulator.
add d : add the contents of register d with accumulator and keep the results in accumulator.
sub a : subtract the contents of accumulator with accumulator and keep the results in accumulator( Nullify the accumulator ;) ).
sub b : subtract the contents of register b from accumulator and keep the results in accumulator.
sub c : subtract the contents of register c from accumulator and keep the results in accumulator.
sub d : subtract the contents of register d from accumulator and keep the results in accumulator.
inr a : increment the contents of accumulator by 1.
inr b : increment the contents of register b by 1.
inr c : increment the contents of register c by 1.
inr d : increment the contents of register d by 1.
hlt : halt or stop the cpu.


