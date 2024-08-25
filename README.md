# PSP Code Generator
This program is developed for a programmable signal processor (PSP).

**PSP** is a processor for *FPGA* using pipeline structure to speed up signal operations.

It can compute 3 operations:
- FIR filter (1 clk);
- FFT (2 clk);
- Complex modulus of number (1 clk).

## Work Algorithm:
1. There is possibility to choose sequence of operations. 
2. Program generate instructions according to processor command system and create file in **hex** format.
3. Processor take this file and executes instructions.

## Graphical User Interface
### Test mode
<img src=https://github.com/user-attachments/assets/78553bfc-4ced-491e-a870-29ce797bc25a width=550>

### User mode
<img src=https://github.com/user-attachments/assets/74d9f8ca-fb96-4547-81af-3beb59f34c38 width=550>
