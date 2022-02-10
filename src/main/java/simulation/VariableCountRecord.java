package simulation;

public class VariableCountRecord {
    public String name;
    public int value;
    public int count;
    public VariableCountRecord(String name, int value){
        this.name = name;
        this.value = value;
        this.count = 1;
    }
    public VariableCountRecord(String name, int value, int count){
        this.name = name;
        this.value = value;
        this.count = count;
    }
}
