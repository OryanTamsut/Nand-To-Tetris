import java.io.*;
import java.util.Scanner;



public class Parser {

    private File file;
    private Scanner scanner;
    private String currentCommand;

    /**
     * init the parser
     * @param filePath the .asm file to parse
     */
    public Parser(String filePath) {
        try {
            file = new File(filePath);
            scanner = new Scanner(file);
            currentCommand = "";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * ceck if have more rows in the file
     * @return true if have or false if not
     */
    public boolean hasMoreCommands() {
        return scanner.hasNextLine();
    }

    /**
     * get the next command
     */
    public void advance() {
        currentCommand = scanner.nextLine().trim();
        while (currentCommand.equals("") || currentCommand.charAt(0) == '/') {
            if (hasMoreCommands()) {
                currentCommand = scanner.nextLine().trim();
            } else {
                currentCommand = null;
                break;
            }
        }
    }

    /**
     * check which type of command the current command is
     * @return A_COMMAND for A command, L_COMMAND for label or C_COMMAND for c command
     */
    public String commandType() {
        switch (currentCommand.charAt(0)) {
            case '@':
                return "A_COMMAND";
            case '(':
                return "L_COMMAND";
            default:
                return "C_COMMAND";
        }
    }

    /**
     * @return the symbol from the command "@label"
     */
    public String symbol() {
        if(currentCommand.contains("/")) {
            currentCommand = currentCommand.substring(0,currentCommand.indexOf("/") );
        }
        String[] arrOfStr = currentCommand.split("[\\(||\\)@]");
        if (arrOfStr.length >= 2) {
            return arrOfStr[1].trim().replaceAll(" ", "");
        } else {
            System.out.println("error, cant find the symbol of the command " + currentCommand);
            return "";
        }
    }

    /**
     * @return the destination from command : dest=comp;jump
     */
    public String dest() {
        if(currentCommand.contains("/")) {
            currentCommand = currentCommand.substring(0,currentCommand.indexOf("/") );
        }
        if (currentCommand.contains("=")) {
            String[] arrOfStr = currentCommand.split("=");
            if (arrOfStr.length >= 1) {
                return arrOfStr[0].trim().replaceAll(" ", "");
            } else {
                System.out.println("error, cant find the dest of the command " + currentCommand);
                return "";
            }
        } else return "";
    }

    /**
     * @return the comp from command : dest=comp;jump
     */
    public String comp() {
        if(currentCommand.contains("/")) {
            currentCommand = currentCommand.substring(0,currentCommand.indexOf("/") );
        }
        String[] arrOfStr = currentCommand.split("[=;]");
        if (currentCommand.contains("=")) {
            if (arrOfStr.length >= 2) {
                return arrOfStr[1].trim().replaceAll(" ", "");
            } else {
                System.out.println("error, cant find the comp of the command " + currentCommand);
                return "";
            }
        } else if (currentCommand.contains(";")) {
            if (arrOfStr.length >= 1) {
                return arrOfStr[0].trim().replaceAll(" ", "");
            } else {
                System.out.println("error, cant find the comp of the command " + currentCommand);
                return "";
            }
        }
        return "";
    }

    /**
     * @return the jump from command : dest=comp;jump
     */
    public String jump() {
        if(currentCommand.contains("/")) {
            currentCommand = currentCommand.substring(0,currentCommand.indexOf("/") );
        }
        String[] arrOfStr = currentCommand.split("[=;]");
        if (currentCommand.contains("=")) {
            if (arrOfStr.length >= 3) {
                return arrOfStr[2].trim().replaceAll(" ", "");
            } else {
                return "";
            }
        } else if (currentCommand.contains(";")) {
            if (arrOfStr.length >= 2) {
                return arrOfStr[1].trim().replaceAll(" ", "");
            } else {
                return "";
            }
        } else return "";
    }


}
