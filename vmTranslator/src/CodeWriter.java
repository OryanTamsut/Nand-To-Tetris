import java.io.*;

public class CodeWriter {
    private BufferedWriter writer;
    private int numEqGtLt = 0;
    private String fileName;
    private int numLabel = 0;
    private String functionName="";

    /**
     * constractor
     * @param filePath the file path of the vm file or the dictionary that containe the vm files
     */
    public CodeWriter(String filePath) {
        filePath=filePath.replace("\\","/");
        String[] fileNameTemp = filePath.split("/");
        fileNameTemp[fileNameTemp.length - 1] = fileNameTemp[fileNameTemp.length - 1].replace(" ", "_");
        this.fileName=fileNameTemp[fileNameTemp.length - 1];
        try {
            if (!filePath.contains(".asm")) {
                filePath = filePath + "/" + fileName + ".asm";
            }
            this.writer = new BufferedWriter(new FileWriter(filePath, true));
            PrintWriter emptyFile = new PrintWriter(filePath);
            emptyFile.print("");
            emptyFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * write to the file the assembly code for arithmetic type command
     * @param command command to parse
     */
    public void writeArithmetic(String command) {
        getTheHeadOfSPSec(command);
        arithmeticSec(command);
    }

    /**
     * get new file path and set the file-name attribute
     * @param filePath new file path of the vm file to translate
     */
    public void setFilePath(String filePath) {
        String tempFilePath=filePath.replace("\\","/");
        String[] fileNameTemp = tempFilePath.split("/");
        fileNameTemp[fileNameTemp.length - 1] = fileNameTemp[fileNameTemp.length - 1].replace(" ", "_");
        this.fileName=fileNameTemp[fileNameTemp.length - 1];
    }

    /**
     * write the push or pop type- command
     * @param command the command to parse PUSH or POP
     * @param segment the segment
     * @param index the index in the segment
     */
    public void writePushPop(String command, String segment, int index) {
        switch (command) {
            case "pop":
                switch (segment) {
                    case "local":
                    case "this":
                    case "that":
                    case "argument":
                        //send the index value
                        writePop(segment, String.valueOf(index));
                        break;
                    case "static":
                        //send the name of the place in memory- filename.index, to make it global
                        writePop(segment,fileName.replace(".asm", "." + String.valueOf(index)));
                        break;
                    case "temp":
                        //send the index+5 because temp segment is 5-13 cells
                        writePop(segment, String.valueOf(5 + index));
                        break;
                    case "pointer":
                        if (index == 0) {
                            writePop(segment, "THIS");
                        } else if (index == 1) {
                            writePop(segment, "THAT");
                        } else System.out.println("error, index not valid in pointer");
                        break;
                }
                break;
            case "push":
                //the explain is same
                switch (segment) {
                    case "local":
                    case "this":
                    case "that":
                    case "argument":
                    case "constant":
                        writePush(segment, String.valueOf(index));
                        break;
                    case "static":
                        writePush(segment,fileName.replace(".asm", "." + String.valueOf(index)));
                        break;
                    case "temp":
                        writePush(segment, String.valueOf(5 + index));
                        break;
                    case "pointer":
                        if (index == 0) {
                            writePush(segment, "THIS");
                        } else if (index == 1) {
                            writePush(segment, "THAT");
                        } else System.out.println("error, index not valid in pointer");
                        break;
                }
                break;
        }
    }

    /**
     * Auxiliary function- write the head of arithmetic command- update the sp pointer
     * @param command arithmetic command
     */
    private void getTheHeadOfSPSec(String command) {
        try {
            if (!command.equals("not") && !command.equals("neg")) {
                writer.append("@SP\n");
                writer.append("AM=M-1\n");
                writer.append("D=M\n");
                writer.append("A=A-1\n");
            } else {
                writer.append("@SP\n");
                writer.append("A=M-1\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * write the arithmetic section- M hold x value and D hold y value
     * @param arith arithmetic command- add\sub\and\or\not\neg\eq\gt\lt
     */
    private void arithmeticSec(String arith) {
        try {
            switch (arith) {
                case "add":
                    writer.append("M=D+M\n");
                    break;
                case "sub":
                    writer.append("M=M-D\n");
                    break;
                case "and":
                    writer.append("M=D&M\n");
                    break;
                case "or":
                    writer.append("M=D|M\n");
                    break;
                case "not":
                    writer.append("M=!M\n");
                    break;
                case "neg":
                    writer.append("M=-M\n");
                    break;
                case "eq":
                case "gt":
                case "lt":
                    numEqGtLt++;
                    writer.append("D=M-D\n");
                    EqGtLtWriter(arith);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * write the brunching for equal or greatThan or lessThan command- if the condition exist or not
     * @param command eq for equal, gt for great than, lt for lessThan
     * @throws IOException if was exception during the write to the file
     */
    private void EqGtLtWriter(String command) throws IOException {
        // Loads to A the location in the ROM where it saves the segment to execute in case the condition is met
        writer.append("@TRUE").append(String.valueOf(numEqGtLt)).append("\n");
        switch (command) {
            case "eq":
                writer.append("D;JEQ\n");
                break;
            case "gt":
                writer.append("D;JGT\n");
                break;
            case "lt":
                writer.append("D;JLT\n");
                break;
        }

        // jump to the code segment that execute if the condition is not met
        writer.append("@FALSE").append(String.valueOf(numEqGtLt)).append("\n");
        writer.append("0;JEQ\n");

        //the segment code to execute if the condition is met
        writer.append("(TRUE").append(String.valueOf(numEqGtLt)).append(")\n");
        writer.append("@SP\n");
        writer.append("A=M-1\n");
        writer.append("M=-1\n");
        writer.append("@CONTINUE").append(String.valueOf(numEqGtLt)).append("\n");
        writer.append("0;JEQ\n");

        //the segment code to execute if the condition is not met
        writer.append("(FALSE").append(String.valueOf(numEqGtLt)).append(")\n");
        writer.append("@SP\n");
        writer.append("A=M-1\n");
        writer.append("M=0\n");

        //continue to running the rest of the code
        writer.append("(CONTINUE").append(String.valueOf(numEqGtLt)).append(")\n");
    }

    /**
     * write the pop assembly code
     * @param segment local/argument/this/that/static/temp/pointer
     * @param index the index in segment where to save the value that pop from the stuck
     */
    private void writePop(String segment, String index) {
        try {
            switch (segment) {
                case "local":
                case "argument":
                case "this":
                case "that":
                    accessToSegmentIndex(segment, index);
                    writer.append("@SP\n");
                    writer.append("AM=M-1\n");
                    writer.append("D=M\n");
                    writer.append("@R13\n");
                    writer.append("A=M\n");
                    writer.append("M=D\n");
                    break;
                case "static":
                case "temp":
                case "pointer":
                    writer.append("@SP\n");
                    writer.append("AM=M-1\n");
                    writer.append("D=M\n");
                    writer.append("@").append(String.valueOf(index)).append("\n");
                    writer.append("M=D\n");
                    break;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * write the code that approaches a cell in memory that is in place of the segment index
     * @param segment segment to access
     * @param index index in the segment
     * @throws IOException if was exception during the write to the file
     */
    private void accessToSegmentIndex(String segment, String index) throws IOException {
        writer.append("@").append(String.valueOf(index)).append("\n");
        writer.append("D=A\n");
        switch (segment) {
            case "local":
                writer.append("@LCL\n");
                break;
            case "argument":
                writer.append("@ARG\n");
                break;
            case "this":
                writer.append("@THIS\n");
                break;
            case "that":
                writer.append("@THAT\n");
                break;
        }
        writer.append("D=D+M\n");
        writer.append("@R13\n");
        writer.append("M=D\n");
    }

    /**
     * write the push assembly code
     * @param segment local/argument/this/that/static/temp/pointer/constant
     * @param index the index in segment where to take the value to put in the stuck
     */
    private void writePush(String segment, String index) {
        try {
            switch (segment) {
                case "local":
                case "argument":
                case "this":
                case "that":
                    accessToSegmentIndex(segment, index);
                    writer.append("@R13\n");
                    writer.append("A=M\n");
                    writer.append("D=M\n");
                    break;
                case "static":
                case "temp":
                case "pointer":
                    writer.append("@").append(String.valueOf(index)).append("\n");
                    writer.append("D=M\n");
                    break;
                case "constant":
                    writer.append("@").append(String.valueOf(index)).append("\n");
                    writer.append("D=A\n");
                    break;
            }
            writer.append("@SP\n");
            writer.append("A=M\n");
            writer.append("M=D\n");
            writer.append("@SP\n");
            writer.append("M=M+1\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * write the assembly code that init the file- write 256 in SP cell and call to sys.init function
     */
    public void writerInit() {
        try {
            writer.append("@256\n");
            writer.append("D=A\n");
            writer.append("@SP\n");
            writer.append("M=D\n");
            writeCall("sys.init", 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * write the assembly code that create a label - functionName$label
     * @param label the label to write
     */
    public void writeLabel(String label) {
        String labelNew=functionName+"$"+label;
        try {
            writer.append("(").append(labelNew.toUpperCase()).append(")").append('\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * write the assembly code that create the goto statement
     * @param label the label to goto
     */
    public void writeGoto(String label) {
        String labelNew=functionName+"$"+label;
        try {
            writer.append("@").append(labelNew.toUpperCase()).append("\n");
            writer.append("0;JMP\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * write the if assembly code
     * @param label label to go to if the condition is met
     */
    public void writeIf(String label) {
        String labelNew=functionName+"$"+label;
        try {
            writer.append("@SP\n");
            writer.append("AM=M-1\n");
            writer.append("D=M\n");
            writer.append("@").append(labelNew.toUpperCase()).append("\n");
            writer.append("D;JNE\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * write the assembly code that call to other function
     * @param functionName function name to call
     * @param numArg The number of arguments sent to a function
     */
    public void writeCall(String functionName, int numArg) {
        String labelNew=this.functionName+"$ret."+numLabel;
        try {
            //save the calling function states
            writePush("constant", labelNew.toUpperCase());
            writePush("pointer", "LCL");
            writePush("pointer", "ARG");
            writePush("pointer", "THIS");
            writePush("pointer", "THAT");

            //jump to the function
            writer.append("@SP\n");
            writer.append("D=M\n");
            writer.append("@5\n");
            writer.append("D=D-A\n");
            writer.append("@").append(String.valueOf(numArg)).append("\n");
            writer.append("D=D-A\n");
            writer.append("@ARG\n");
            writer.append("M=D\n");
            writer.append("@SP\n");
            writer.append("D=M\n");
            writer.append("@LCL\n");
            writer.append("M=D\n");
            writer.append("@").append(functionName.toUpperCase()).append("\n");
            writer.append("0;JMP\n");
            writer.append("(").append(labelNew.toUpperCase()).append(")").append('\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.numLabel++;
    }

    /**
     * write the create function in assembly code
     * @param functionName the name of the function to create
     * @param numVars the num variables that the function use
     */
    public void writeFunction(String functionName,int numVars){
        this.functionName=functionName;
        try {
            //create label that represents the function
            writer.append("(").append(functionName.toUpperCase()).append(")").append('\n');

            //Resets the cells that contain the local variables
            for(int i=0; i<numVars; i++){
                writePush("constant","0");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * write the assembly code that causes a return from function
     */
    public void writeRetutn(){
        try {
            //Restores the state of the previous function
            writer.append("@LCL\n");
            writer.append("D=M\n");
            writer.append("@endFrame\n");
            writer.append("M=D\n");

            writer.append("@5\n");
            writer.append("D=D-A\n");
            writer.append("A=D\n");
            writer.append("D=M\n");
            writer.append("@retAddr\n");
            writer.append("M=D\n");

            writePop("argument","0");

            writer.append("@ARG\n");
            writer.append("D=M\n");

            //writer.append("@1\n");
            //writer.append("D=D+A\n");
            writer.append("@SP\n");
            writer.append("M=D+1\n");
            updateInReturn("THAT",1);
            updateInReturn("THIS",2);
            updateInReturn("ARG",3);
            updateInReturn("LCL",4);

            //return to the previous function
            writer.append("@retAddr\n");
            writer.append("A=M\n");
            writer.append("0;JMP\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * resture the states of THAT THIS ARG LCL
     * @param pointer segment name
     * @param num The number of its position at the beginning of the stack
     */
    private void updateInReturn(String pointer,int num){
        try{
            writer.append("@endFrame\n");
            writer.append("D=M\n");
            writer.append("@").append(String.valueOf(num)).append("\n");
            writer.append("D=D-A\n");
            writer.append("A=D\n");
            writer.append("D=M\n");
            writer.append("@").append(pointer).append("\n");
            writer.append("M=D\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * write the infinite loop and close the file
     */
    public void close() {
        try {
            writer.append("(END)\n");
            writer.append("@END\n");
            writer.append("0;JMP");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
