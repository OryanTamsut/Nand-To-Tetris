import java.io.File;
import java.util.*;

public class VMTranslator {

    public static void main(String[] args) {


        String filePath = args[0];
        List<String> allFilesInDir = findFiles(filePath);
        CodeWriter writer = new CodeWriter(filePath.replace(".vm", ".asm"));
        if(allFilesInDir.size()>=2) {
            writer.writerInit();
        }
        for (String fileNowPath : allFilesInDir) {
            Parser parser = new Parser(fileNowPath);
            writer.setFilePath(fileNowPath.replace(".vm", ".asm"));

            while (parser.hasMoreCommands()) {
                //get the next command
                parser.advance();

                //parse the command
                String type = parser.commandType();
                String arg1 = parser.arg1();
                String arg2 = "";
                //write the assembly code that appropriate to the command type
                switch (type) {
                    case "C_ARITHMETIC":
                        writer.writeArithmetic(arg1);
                        break;
                    case "C_POP":
                        arg2 = parser.arg2();
                        writer.writePushPop("pop", arg1, Integer.parseInt(arg2));
                        break;
                    case "C_PUSH":
                        arg2 = parser.arg2();
                        writer.writePushPop("push", arg1, Integer.parseInt(arg2));
                        break;
                    case "C_LABEL":
                        writer.writeLabel(arg1);
                        break;
                    case "C_IF":
                        writer.writeIf(arg1);
                        break;
                    case "C_GOTO":
                        writer.writeGoto(arg1);
                        break;
                    case "C_FUNCTION":
                        arg2 = parser.arg2();
                        writer.writeFunction(arg1,Integer.parseInt(arg2));
                        break;
                    case "C_CALL":
                        arg2 = parser.arg2();
                        writer.writeCall(arg1,Integer.parseInt(arg2));
                        break;
                    case "C_RETURN":
                        writer.writeRetutn();
                        break;
                }
            }
        }
        writer.close();
    }

    /**
     * get all the vm files in the directory
     * @param fileName the path of the directory to find there
     * @return list that containing all the vm fiels
     */
    public static List<String> findFiles(String fileName) {
        File dir = new File(fileName);
        List<String> allFiles = new ArrayList<String>();
        if (dir.isDirectory())
            findAllFiles(Objects.requireNonNull(dir.listFiles()), allFiles);
        else allFiles.add(fileName);
        return allFiles;
    }

    /**
     * Auxiliary function - check if the file is vm type- add it to the all files list
     * @param listFilesInDir array of the all files in the directory
     * @param allFiles all vm files in the directory
     */
    public static void findAllFiles(File[] listFilesInDir, List<String> allFiles) {
        for (File file : listFilesInDir) {
            if (file.isDirectory()) {
                findAllFiles(Objects.requireNonNull(file.listFiles()), allFiles); // Calls same method again.
            } else {
                String path = file.getAbsolutePath();
                if (path.contains(".vm")) {
                    allFiles.add(file.getAbsolutePath());
                }
            }
        }
    }
}



