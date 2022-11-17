public class ALU
{
    private SystemControl systemControl;

    public ALU(SystemControl systemControl) {
        this.systemControl = systemControl;
    }


    public void HALT()
    {
        systemControl.PrintToDebugConsole("Executing Instruction 'Halt'");

        systemControl.Running = false;
        systemControl.Idle = true;
    }


    public void LDR(int GPR_X, int IXR_X, int EA_X)
    {
        systemControl.PrintToDebugConsole("Executed Instruction 'LDR'");

        systemControl.registers.GPRS[GPR_X] = systemControl.memory.get_from_memory(EA_X);

        if (IXR_X < 0) {
            systemControl.PrintToDebugConsole(String.format("  GPR: %d\n  IXR: %s\n  EA: %H", systemControl.registers.GPRS[GPR_X], "None", EA_X));
        }
        else
        {
            systemControl.PrintToDebugConsole(String.format("  GPR: %d\n  IXR: %d\n  EA: %H", systemControl.registers.GPRS[GPR_X], systemControl.registers.IXRS[IXR_X], EA_X));
        }

    }


    public void STR(int GPR_X, int IXR_X, int EA_X)
    {
        systemControl.PrintToDebugConsole("Executed Instruction 'STR'");

        systemControl.memory.store_to_memory(EA_X, systemControl.registers.GPRS[GPR_X]);

        if (IXR_X < 0) {
            systemControl.PrintToDebugConsole(String.format("  GPR: %d\n  IXR: %s\n  EA: %H", systemControl.registers.GPRS[GPR_X], "None", EA_X));
        }
        else
        {
            systemControl.PrintToDebugConsole(String.format("  GPR: %d\n  IXR: %d\n  EA: %H", systemControl.registers.GPRS[GPR_X], systemControl.registers.IXRS[IXR_X], EA_X));
        }
    }


    public void LDA(int GPR_X, int IXR_X, int EA_X)
    {
        systemControl.PrintToDebugConsole("Executed Instruction 'LDA'");

        systemControl.registers.GPRS[GPR_X] = EA_X;

        if (IXR_X < 0) {
            systemControl.PrintToDebugConsole(String.format("  GPR: %d\n  IXR: %s\n  EA: %H", systemControl.registers.GPRS[GPR_X], "None", EA_X));
        }
        else
        {
            systemControl.PrintToDebugConsole(String.format("  GPR: %d\n  IXR: %d\n  EA: %H", systemControl.registers.GPRS[GPR_X], systemControl.registers.IXRS[IXR_X], EA_X));
        }
    }


    public void LDX(int IXR_X, int IXEA_X)
    {
        systemControl.PrintToDebugConsole("Executed Instruction 'LDX'");

        systemControl.registers.IXRS[IXR_X] = systemControl.memory.get_from_memory(IXEA_X);

        if (IXR_X < 0) {
            systemControl.PrintToDebugConsole(String.format("  IXR: %s\n  EA: %H", "None", IXEA_X));
        }
        else
        {
            systemControl.PrintToDebugConsole(String.format("  IXR: %d\n  EA: %H", systemControl.registers.IXRS[IXR_X], IXEA_X));
        }
    }


    public void STX(int IXR_X, int IXEA_X)
    {
        systemControl.PrintToDebugConsole("Executed Instruction 'STX'");

        systemControl.memory.store_to_memory(IXEA_X, systemControl.registers.IXRS[IXR_X]);

        if (IXR_X < 0) {
            systemControl.PrintToDebugConsole(String.format("  IXR: %s\n  EA: %H", "None", IXEA_X));
        }
        else
        {
            systemControl.PrintToDebugConsole(String.format("  IXR: %d\n  EA: %H", systemControl.registers.IXRS[IXR_X], IXEA_X));
        }
    }


    public void JZ(int GPR_X, int IXR_X, int EA_X)
    {
        if (systemControl.registers.GPRS[GPR_X] == 0) {
            systemControl.registers.PC = EA_X;
//            systemControl.PrintToDebugConsole(String.format("Executing instruction JZ\n  GPR %d is zero, jump to %H"));
            return;
        }
//        systemControl.PrintToDebugConsole(String.format("Executing instruction JZ\n  GPR %d is not zero, do not jump"));
    }


    public void JNE(int GPR_X, int IXR_X, int EA_X) {

        if (systemControl.registers.GPRS[GPR_X] != 0) {
            systemControl.registers.PC = EA_X;
//            systemControl.PrintToDebugConsole(String.format("Executing instruction JNE\n  GPR %d is not zero, jump to %H"));
            return;
        }
//        systemControl.PrintToDebugConsole(String.format("Executing instruction JNE\n  GPR %d is zero, do not jump"));
    }


    public void JCC(int GPR_X, int IXR_X, int EA_X) {
        boolean checkflag = false;

        switch (systemControl.registers.GPRS[GPR_X]) {
            case 0:
                checkflag = systemControl.registers.CC1;
                break;
            case 1:
                checkflag = systemControl.registers.CC2;
                break;
            case 2:
                checkflag = systemControl.registers.CC3;
                break;
            case 3:
                checkflag = systemControl.registers.CC4;
                break;
        }
        if (checkflag) {
            systemControl.registers.PC = EA_X;
//           systemControl.PrintToDebugConsole(String.format("Executing instruction JCC\n  CC: %d equals to required CC: %d jump to %H", systemControl.cc, GPR_X, EA_X));
            return;
        }
//        systemControl.PrintToDebugConsole(String.format("Executing instruction JCC\n  CC: %d does not equal to required CC: %d", systemControl.cc, GPR_X));
    }



    public void JMA(int IXR_X, int EA_X) {

        systemControl.registers.PC = EA_X;

//        systemControl.PrintToDebugConsole(String.format("Executing instruction JMA\n  Jump to %H"));
    }


    public void JSR(int IXR_X, int EA_X) {
        systemControl.registers.GPRS[3] = systemControl.registers.PC;
        systemControl.registers.PC = EA_X;

//       systemControl.PrintToDebugConsole(String.format("Executing instruction JSR\n  Jump to %H, current Args at %H", EA_X, systemControl.GPRS[0]));
    }


    public void JGT(int Rx_X, int Ry_X, int EA_X) {

        if (systemControl.registers.GPRS[Rx_X] > systemControl.registers.GPRS[Ry_X]) {
            systemControl.registers.PC = EA_X;
            systemControl.PrintToDebugConsole(String.format("Executing instruction JGT\n  Jump to %H", EA_X));
            return;
        }

        systemControl.PrintToDebugConsole(String.format("Executing instruction JGT\n  do not jump to %H", EA_X));
    }


    public void RFS(int Address_X) {
        systemControl.registers.PC = systemControl.registers.GPRS[3];
        systemControl.registers.GPRS[0] = Address_X;

//        systemControl.PrintToDebugConsole(String.format("Executing instruction RFS\n  Return to %H, return value at %H", systemControl.GPRS[3], systemControl.GPRS[0]));
    }



    public void SOB(int GPR_X, int IXR_X, int EA_X) {
        systemControl.PrintToDebugConsole("Executing instruction SOB");

        systemControl.registers.GPRS[GPR_X]--;
        if (systemControl.registers.GPRS[GPR_X] < 0) {
            systemControl.registers.GPRS[GPR_X] = (int) (Math.pow(2, 16) - 1);
            systemControl.registers.CC2 = true;
            systemControl.PrintToDebugConsole("  Underflow!");
        }

        if (systemControl.registers.GPRS[GPR_X] > 0) {
            systemControl.registers.PC = EA_X;
            systemControl.PrintToDebugConsole(String.format("  GPR%d: %d, jump to %H", GPR_X, systemControl.registers.GPRS[GPR_X], EA_X));
            return;
        }
        systemControl.PrintToDebugConsole(String.format("  GPR%d: %d, not jump", GPR_X, systemControl.registers.GPRS[GPR_X]));
    }



    public void JGE(int GPR_X, int IXR_X, int EA_X) {

        if (systemControl.registers.GPRS[GPR_X] >= 0) {
            systemControl.registers.PC = EA_X;
            systemControl.PrintToDebugConsole(String.format("Executing instruction JGE\n  GRP%d: %d, jump to %H", GPR_X, systemControl.registers.GPRS[GPR_X], EA_X));
            return;
        }
        systemControl.PrintToDebugConsole(String.format("Executing instruction JGE\n  GRP%d: %d, not jump", GPR_X, systemControl.registers.GPRS[GPR_X]));
    }


    public void AMR(int GPR_X, int IXR_X, int EA_X) {
        systemControl.PrintToDebugConsole("Executing instruction AMR");


        systemControl.registers.GPRS[GPR_X] += systemControl.memory.get_from_memory(EA_X);
        if (systemControl.registers.GPRS[GPR_X] > 65535) {
            systemControl.registers.GPRS[GPR_X] -= 65536;
            systemControl.registers.CC1 = true;
            systemControl.PrintToDebugConsole("  Overflow!");
        }

//        systemControl.PrintToDebugConsole(String.format("  Add %d at %H to GPR%d, result is %d", backEnd.memory[ea], EA_X, GPR_X, systemControl.registers.GPRS[Utils.GPR_Index]));
    }

    public void SMR(int GPR_X, int IXR_X, int EA_X) {
        systemControl.PrintToDebugConsole("Executing instruction SMR");

        systemControl.registers.GPRS[GPR_X] -= systemControl.memory.get_from_memory(EA_X);
        if (systemControl.registers.GPRS[GPR_X] < 0) {
            systemControl.registers.GPRS[GPR_X] += 65536;
            systemControl.registers.CC2 = true;
            systemControl.PrintToDebugConsole("  Underflow!");
        }

//       systemControl.PrintToDebugConsole(String.format("  Sub %d at %H from GPR%d, result is %d", systemControl.registers.memory[EA_X], EA_X, GPR_X, systemControl.registers.GPRS[Utils.GPR_Index]));
    }

    public void AIR(int GPR_X, int Address) {
        systemControl.PrintToDebugConsole("Executing instruction AIR");

        systemControl.registers.GPRS[GPR_X] += Address;

        if (systemControl.registers.GPRS[GPR_X] > 65535) {
            systemControl.registers.GPRS[GPR_X] -= 65536;
            systemControl.registers.CC1 = true;
            systemControl.PrintToDebugConsole("  Overflow!");
        }

        systemControl.PrintToDebugConsole(String.format("  Add %d to GPR%d, result is %d", Address, GPR_X, systemControl.registers.GPRS[GPR_X]));
    }


    public void SIR(int GPR_X, int Address) {
        systemControl.PrintToDebugConsole("Executing instruction SIR");

        systemControl.registers.GPRS[GPR_X] -= Address;

        if (systemControl.registers.GPRS[GPR_X] < 0) {
            systemControl.registers.GPRS[GPR_X] += 65536;
            systemControl.registers.CC2 = true;
            systemControl.PrintToDebugConsole("  Underflow!");
        }

        systemControl.PrintToDebugConsole(String.format("  Sub %d from GPR%d, result is %d", Address, GPR_X, systemControl.registers.GPRS[GPR_X]));
    }

    public void MLT(int Rx_X,int Ry_X) {
        systemControl.PrintToDebugConsole("Executing instruction MLT");

        /*
        if (!(i.rx == 0 | i.rx == 2)) {
            backEnd.debugPrint("  Invalid operands");
            return;
        }
        */

        long result = (long) systemControl.registers.GPRS[Rx_X] * systemControl.registers.GPRS[Ry_X];
        String resultStr = String.format("%32s", Long.toBinaryString(result)).replace(' ', '0');

        String hBits = resultStr.substring(0, 16);
        String lBits = resultStr.substring(16, 32);

        systemControl.registers.GPRS[Rx_X] = Integer.parseInt(hBits, 2);
        systemControl.registers.GPRS[Rx_X+1] = Integer.parseInt(lBits, 2);

        systemControl.PrintToDebugConsole(String.format("  MLT r%d with r%d, result is %d %s %s %s", Rx_X, Ry_X, result, resultStr, hBits, lBits));
    }

    public void DVD(int Rx_X,int Ry_X) {
        systemControl.PrintToDebugConsole("Executing instruction DVD");

        /*
        if (!(i.rx == 0 | i.rx == 2)) {
            backEnd.debugPrint("  Invalid operands");
            return;
        }
        */

        if (systemControl.registers.GPRS[Ry_X] == 0) {
            systemControl.PrintToDebugConsole("  Divide by 0");
            systemControl.registers.CC3 = true;
            return;
        }

        int quotient = systemControl.registers.GPRS[Rx_X] / systemControl.registers.GPRS[Ry_X];
        int remainder = systemControl.registers.GPRS[Rx_X] % systemControl.registers.GPRS[Ry_X];

        systemControl.registers.GPRS[Rx_X] = quotient;
        systemControl.registers.GPRS[Rx_X + 1] = remainder;

        systemControl.PrintToDebugConsole(String.format(" DVD r%d with r%d, quotient is %d, remainder is %d", Rx_X, Ry_X, quotient, remainder));
    }

    public void TRR(int Rx_X,int Ry_X) {
        systemControl.PrintToDebugConsole("Executing instruction TRR");

        if (systemControl.registers.GPRS[Rx_X] == systemControl.registers.GPRS[Rx_X]) {
            systemControl.registers.CC4 = true;
            systemControl.PrintToDebugConsole(String.format("  r%d is equal to r%d", Rx_X, Ry_X));
            return;
        }

        systemControl.PrintToDebugConsole(String.format("  r%d is not equal to r%d", Rx_X, Ry_X));
    }


    public void AND(int Rx_X,int Ry_X) {
        systemControl.PrintToDebugConsole("Executing instruction AND");

        systemControl.registers.GPRS[Rx_X] &= systemControl.registers.GPRS[Ry_X];

        systemControl.PrintToDebugConsole(String.format("  r%d AND r%d", Rx_X, Ry_X));
    }

    public void ORR(int Rx_X,int Ry_X) {
        systemControl.PrintToDebugConsole("Executing instruction ORR");

        systemControl.registers.GPRS[Rx_X] |= systemControl.registers.GPRS[Ry_X];

        systemControl.PrintToDebugConsole(String.format("  r%d ORR r%d", Rx_X, Ry_X));
    }


    public void NOT(int Rx_X) {
        systemControl.PrintToDebugConsole("Executing instruction NOT");

        int result = ~systemControl.registers.GPRS[Rx_X];
        String resultStr = Integer.toBinaryString(result);
        resultStr = resultStr.substring(resultStr.length() - 16);

        systemControl.registers.GPRS[Rx_X] = Integer.parseInt(resultStr, 2);

        systemControl.PrintToDebugConsole(String.format("  NOT r%d", Rx_X));
    }

    public void SRC(int GPR_X,int Count_X,int RL_X,int AL_X) {
        backEnd.debugPrint("Running SRC");

        String origin = String.format("%16S", Integer.toBinaryString(backEnd.gpr[i.gprIndex])).replace(' ', '0');
        StringBuilder result = new StringBuilder();

        if (i.lr == 0) {
            if (origin.substring(16 - i.count).contains("1")) {
                backEnd.cc |= 2;
                backEnd.debugPrint("  Underflow!");
            }
        } else {
            System.out.println(origin.substring(0, i.count));
            if (origin.substring(0, i.count).contains("1")) {
                backEnd.cc |= 1;
                backEnd.debugPrint("  Overflow!");
            }
        }

        if (i.al == 1) {
            if (i.lr == 0) {
                backEnd.debugPrint("Shifting right");
                for (int j = 0; j < i.count; j++) {
                    result.append('0');
                }
                result.append(origin, 0, 16 - i.count);
            } else {
                backEnd.debugPrint("Shifting left");
                result.append(origin.substring(i.count));
                for (int j = 0; j < i.count; j++) {
                    result.append('0');
                }
            }
        } else {
            backEnd.debugPrint("Shift arithmetically");
            char maskBit = origin.charAt(0);

            if (i.lr == 0) {
                backEnd.debugPrint("Shifting right");
                for (int j = 0; j < i.count; j++) {
                    result.append(maskBit);
                }
                result.append(origin, 0, 16 - i.count);
            } else {
                backEnd.debugPrint("Shifting left");
                result.append(origin.substring(i.count));
                for (int j = 0; j < i.count; j++) {
                    result.append('0');
                }
            }
        }

        backEnd.gpr[i.gprIndex] = Integer.parseInt(result.toString(), 2);

        backEnd.debugPrint(String.format("  Prev binary: %s, result: %s", origin, result));
    }
    }

    public void RRC(int GPR_X,int Count_X,int RL_X,int AL_X) {
        backEnd.debugPrint("Running RRC");

        String origin = String.format("%16S", Integer.toBinaryString(backEnd.gpr[i.gprIndex])).replace(' ', '0');
        String result;

        if (i.lr == 0) {
            backEnd.debugPrint("Rotating right");
            result = origin.substring(16 - i.count) + origin.substring(0, 16 - i.count);
        } else {
            backEnd.debugPrint("Rotating left");
            result = origin.substring(i.count) + origin.substring(0, i.count);
        }

        backEnd.gpr[i.gprIndex] = Integer.parseInt(result, 2);

        backEnd.debugPrint(String.format("  Prev binary: %s, result: %s", origin, result));
    }

    public void IN(int GPR_X,int DevID_X) {
        systemControl.PrintToDebugConsole("Executing IN");

        char c;

        if (DevID_X == 0) {
            c = systemControl.frame.popKBBuffer();
            systemControl.registers.GPRS[GPR_X] = c;
            systemControl.PrintToDebugConsole(String.format("  Read %c from keyboard, store to gpr%d", c, GPR_X));
        } else if (DevID_X == 2) {
            c = systemControl.frame.popCRBuffer();
            systemControl.registers.GPRS[GPR_X] = c;
            systemControl.PrintToDebugConsole(String.format("  Read %c from card reader, store to gpr%d", c, GPR_X));
        } else {
            systemControl.PrintToDebugConsole("  Invalid operands");
        }
    }

    public void OUT(int GPR_X,int DevID_X,int I_X) {
        systemControl.PrintToDebugConsole("Executing OUT");

        int devID = DevID_X;

        if (devID != 1) {
            systemControl.PrintToDebugConsole("  Invalid operands");
            return;
        }

        if (I_X == 1) {
            systemControl.frame.insertPrinterBuffer((char) systemControl.registers.GPRS[GPR_X]);
        } else {
            systemControl.frame.pushPrinterBuffer((char) systemControl.registers.GPRS[GPR_X]);
        }


        systemControl.PrintToDebugConsole(String.format("  Print %c to console printer", (char) systemControl.registers.GPRS[GPR_X]));
    }

    public void CHK(int GPR_X, int DevID_X) {
        systemControl.PrintToDebugConsole("Executing CHK");

        if (DevID_X == 0) {
            if (systemControl.frame.isKBBufferEmpty()) {
                systemControl.registers.GPRS[GPR_X] = 0;
                systemControl.PrintToDebugConsole("  No input from keyboard to read");
            } else {
                systemControl.registers.GPRS[GPR_X] = 1;
                systemControl.PrintToDebugConsole("  There is input to read from keyboard");
            }
        } else if (DevID_X == 2) {
            if (systemControl.frame.isCRBufferEmpty()) {
                systemControl.registers.GPRS[GPR_X] = 0;
                systemControl.PrintToDebugConsole("  No input from card reader to read");
            } else {
                systemControl.registers.GPRS[GPR_X] = 1;
                systemControl.PrintToDebugConsole("  There is input to read from card reader");
            }
        } else if (DevID_X == 1) {
            systemControl.registers.GPRS[GPR_X] = 1;
            systemControl.PrintToDebugConsole("  Enabled: Console printer");
        } else {
            systemControl.registers.GPRS[GPR_X] = 0;
        }
    }


    public void TRAP(int TrapCode_X) {
        systemControl.memory.store_to_memory(2, systemControl.registers.PC + 1);

        systemControl.registers.PC = systemControl.memory.get_from_memory(systemControl.memory.get_from_memory(0) + TrapCode_X);

        systemControl.frame.debugPrint("Running TRAP");
    }


}