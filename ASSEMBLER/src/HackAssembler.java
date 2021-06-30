import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HackAssembler {

    public static void main(String[] args) {

        //get the file name to translate
        String filePath=args[0];
        Parser parser = new Parser(filePath);
        Code code = new Code();
        SymbolTable symbol = new SymbolTable();
        initSymbol(filePath, parser, symbol);
        String fileOutputPath=filePath.replace(".asm",".hack");
        Parser parser1 = new Parser(filePath);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileOutputPath ,true));
            PrintWriter emptyFile = new PrintWriter(fileOutputPath);
            emptyFile.print("");
            emptyFile.close();

            //count the num of var in the program
            int numVariables = 16;
            while (parser1.hasMoreCommands()) {
                parser1.advance();
                String type = parser1.commandType();
                StringBuilder binCommand=new StringBuilder();
                switch (type) {
                    case "A_COMMAND":
                        //get the symbol
                        String commandSymbol= parser1.symbol();
                        Integer address=0;
                        if(isNumeric(commandSymbol)){
                            address=Integer.parseInt(commandSymbol);
                        }
                        //check if the symbol don't exist in the symbol Table and add it
                        else if (!symbol.contain(commandSymbol)) {
                            symbol.addEntry(commandSymbol, numVariables);
                            address=symbol.getAddress(commandSymbol);
                            numVariables++;
                        }
                        //get the symbol value from the symbol table
                        else if(symbol.contain((commandSymbol))){
                            address=symbol.getAddress(commandSymbol);
                        }

                        binCommand.append(Integer.toBinaryString(address));
                        int j=binCommand.length();
                        for (int i = 0; i < 16 - j; i++) {
                            binCommand.insert(0, "0");
                        }
                        writer.append(binCommand.toString());
                        writer.append('\n');
                        binCommand.setLength(0);
                        break;
                    case "C_COMMAND":
                        //write the c command
                        binCommand.append("111");
                        binCommand.append(code.comp(parser1.comp()));
                        binCommand.append(code.dest(parser1.dest()));
                        binCommand.append(code.jump(parser1.jump()));
                        writer.append(binCommand.toString());
                        writer.append('\n');
                        binCommand.setLength(0);
                        break;
                }
            }

            writer.close();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * init the symbol table
     * @param fileName fileName of the asm file
     * @param parser Parser to parse the command
     * @param symbol The symbol table to which the symbol are added
     */
    public static void initSymbol(String fileName, Parser parser, SymbolTable symbol) {

        //add the R0,R1 ...
        for (int i = 0; i < 16; i++) {
            symbol.addEntry("R" + i, i);
        }

        //add the save symbols
        symbol.addEntry("SCREEN", 16384);
        symbol.addEntry("KBD", 24576);
        symbol.addEntry("SP", 0);
        symbol.addEntry("LCL", 1);
        symbol.addEntry("ARG", 2);
        symbol.addEntry("THIS", 3);
        symbol.addEntry("THAT", 4);

        int counterCommands = 0;
        List<String> symbolToAdd = new ArrayList<String>();
        //add the label command
        while (parser.hasMoreCommands()) {
            parser.advance();
            String type = parser.commandType();
            if (!type.equals("L_COMMAND")) {
                if (symbolToAdd.size() > 0) {
                    for (String s : symbolToAdd) {
                        symbol.addEntry(s, counterCommands);
                    }
                    if (symbolToAdd.size() > 0) {
                        symbolToAdd.subList(0, symbolToAdd.size()).clear();
                    }
                }
                counterCommands++;
            }
            if (type.equals("L_COMMAND")) {
                symbolToAdd.add(parser.symbol());
            }
        }

    }

    //check if string is number type
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Integer d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
