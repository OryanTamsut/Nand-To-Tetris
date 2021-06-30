import java.util.HashMap;
import java.util.Map;

public class Code {

    private final Map<String, String> compMap;
    private final Map<String, String> jumpMap;

    /**
     * init the comp map and the jump map with the corresponding binary values
     */
    public Code() {
        compMap = new HashMap<>(28);
        jumpMap = new HashMap<>(7);
        compMap.put("0", "0101010");
        compMap.put("1", "0111111");
        compMap.put("-1", "0111010");
        compMap.put("D", "0001100");
        compMap.put("A", "0110000");
        compMap.put("M", "1110000");
        compMap.put("!D", "0001101");
        compMap.put("!A", "0110001");
        compMap.put("!M", "1110001");
        compMap.put("-D", "0001111");
        compMap.put("-A", "0110011");
        compMap.put("-M", "1110011");
        compMap.put("D+1", "0011111");
        compMap.put("M+1", "1110111");
        compMap.put("A+1", "0110111");
        compMap.put("D-1", "0001110");
        compMap.put("A-1", "0110010");
        compMap.put("M-1", "1110010");
        compMap.put("D+A", "0000010");
        compMap.put("D+M", "1000010");
        compMap.put("D-A", "0010011");
        compMap.put("D-M", "1010011");
        compMap.put("A-D", "0000111");
        compMap.put("M-D", "1000111");
        compMap.put("D&A", "0000000");
        compMap.put("D&M", "1000000");
        compMap.put("D|A", "0010101");
        compMap.put("D|M", "1010101");
        jumpMap.put("JGT", "001");
        jumpMap.put("JEQ", "010");
        jumpMap.put("JGE", "011");
        jumpMap.put("JLT", "100");
        jumpMap.put("JNE", "101");
        jumpMap.put("JLE", "110");
        jumpMap.put("JMP", "111");
    }

    /**
     * @param destStr the dest
     * @return binary string that corresponding to the dest string
     */
    public String dest(String destStr) {
        String[] binDestArr = {"0", "0", "0"};
        if (destStr.contains("M")) {
            binDestArr[2] = "1";
        }
        if (destStr.contains("D")) {
            binDestArr[1] = "1";
        }
        if (destStr.contains("A")) {
            binDestArr[0] = "1";
        }
        StringBuilder binDest = new StringBuilder();
        for (String s : binDestArr) {
            binDest.append(s);
        }
        return binDest.toString();
    }

    /**
     * write the comp to the binary file
     * @param compStr the comp
     * @return binary string that corresponding to the comp string
     */
    public String comp(String compStr) {
        String compBin=compMap.get(compStr);
        if(compBin==null) {
            System.out.println("cant convert the comp to binary "+compStr);
            return "";
        }
        return compBin;
    }

    /**
     * write the jump to the binary file
     * @param jumpStr the comp
     * @return binary string that corresponding to the jump string
     */
    public String jump(String jumpStr) {
        String jumpBin=jumpMap.get(jumpStr);
        if(jumpBin==null) return "000";
        return jumpBin;
    }

}
