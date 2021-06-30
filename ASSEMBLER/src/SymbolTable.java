import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private final Map<String,Integer> symbolMap;

    public SymbolTable(){
        symbolMap=new HashMap<>();
    }

    /**
     * add symbol to the symbol table
     * @param symbol the symbol
     * @param address the address of the symbol
     */
    public void addEntry(String symbol,Integer address){
        if(!contain(symbol)){
            symbolMap.put(symbol,address);
        }
    }

    /**
     * check if the symbol table contain some symbol
     * @param symbol the symbol to check
     * @return true if the symbol in the symbolTable, else return false
     */
    public boolean contain(String symbol){
        Integer x=symbolMap.get(symbol);
        return x!=null;
    }

    /**
     * get the address that corresponding to the symbol
     * @param symbol the symbol to convert
     * @return the corresponding address
     */
    public Integer getAddress(String symbol){
        return symbolMap.get(symbol);
    }
}
