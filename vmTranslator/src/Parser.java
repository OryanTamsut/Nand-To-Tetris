import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Parser {

    private Scanner scanner;
    private String currentCommand;

    /**
     * constructor
     * @param filePath the vm file's path
     */
    public Parser(String filePath) {
        try {
            File file = new File(filePath);
            scanner = new Scanner(file);
            currentCommand = "";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return if has more command-rows in the file
     */
    public boolean hasMoreCommands() {
        return scanner.hasNextLine();
    }

    /**
     * get the next command- Deletes spaces and comments
     */
    public void advance() {
        currentCommand = scanner.nextLine().trim();
        if(currentCommand.contains("/")) {
           currentCommand=currentCommand.substring(0, currentCommand.indexOf('/')).trim();
        }

        while (currentCommand.equals("")) {
            if (hasMoreCommands()) {
                currentCommand = scanner.nextLine().trim();
                if(currentCommand.contains("/")) {
                    currentCommand=currentCommand.substring(0, currentCommand.indexOf('/')).trim();
                }
            } else {
                currentCommand = null;
                break;
            }
        }
    }

    /**
     * calc the command type
     * @return the command type
     */
    public String commandType(){
        String []spliceCommand=currentCommand.split(" ");
        List<String> spliceComList = Arrays.asList(spliceCommand);
        spliceComList=removeEmptyString(spliceComList);
        switch(spliceComList.get(0)){
            case "add": case "sub": case "neg":
            case "eq": case "gt": case "lt": case "and": case "or": case "not":
                return "C_ARITHMETIC";
            case "pop":
                return "C_POP";
            case "push":
                return "C_PUSH";
            case "label":
                return "C_LABEL";
            case "if-goto":
                return "C_IF";
            case "goto":
                return "C_GOTO";
            case "function":
                return "C_FUNCTION";
            case "call":
                return "C_CALL";
            case "return":
                return "C_RETURN";
            default:
                return "";
        }

    }

    /**
     * @return the argument 1 in the command
     */
    public String arg1(){
        String []spliceCommand=currentCommand.split(" ");
        List<String> spliceComList = Arrays.asList(spliceCommand);
        spliceComList=removeEmptyString(spliceComList);

        if(commandType().equals("C_ARITHMETIC")||commandType().equals("C_RETURN"))
            return spliceComList.get(0);
        else if(spliceComList.size()>=2){
            return spliceComList.get(1);
        }
        else System.out.println("error, cant find arg 1");
        return "";
    }

    /**
     * @return the argument 2 in the command
     */
    public String arg2(){
        String []spliceCommand=currentCommand.split(" ");
        List<String> spliceComList = Arrays.asList(spliceCommand);
        spliceComList=removeEmptyString(spliceComList);

        if(spliceComList.size()>=3){
            return spliceComList.get(2);
        }
        else System.out.println("error, cant find arg 2");
        return "";
    }

    /**
     * get list of string and remove the empty string from the list
     * @param oldList to remove from it
     * @return new list without empty string
     */
    private List<String> removeEmptyString(List<String> oldList){
        List<String> newList=new ArrayList<>();
        for (String s : oldList) {
            if (!s.equals("")) {
                newList.add(s);
            }
        }
        return newList;
    }
}
