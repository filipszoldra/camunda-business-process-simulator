package simulation;

public class VariableRecord {
    public String variableName;
    public Integer value;
    boolean isStarted = false;

    public VariableRecord(String varName){
        this.variableName = varName;
        this.value = 0;

    }

    public VariableRecord(String varName, Integer value){
        this.variableName = varName;
        this.value = value;
    }

    public boolean getIsStarted(){
        return isStarted;
    }
    public void markStarted(){
        this.isStarted = true;
    }
}
