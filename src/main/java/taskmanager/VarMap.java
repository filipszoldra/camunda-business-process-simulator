package taskmanager;

import simdata.GetRandomValue;
import simdata.TaskList;
import simulation.VariableValueRecords;

import java.util.List;
import java.util.Map;

public class VarMap {
    Map<String, Object> varMap;
    public VarMap (TaskList tasklist, VariableValueRecords variableValueRecords){
        List<String> varNames = tasklist.getConditionalVars();
        for (var varName : varNames) {

            int value;
            if (varName.equals("rand")) {
                value = GetRandomValue.GetRandomValue();
            } else {
                value = variableValueRecords.getVarValue(varName);
            }
            String varNameVal = varName.concat("Val");
            if (varMap == null){
                varMap = Map.of(varNameVal, value);
            }
            else{
                varMap.put(varNameVal, value);
            }
        }
    }
    public Map<String, Object> getVarMap (){
        return varMap;
    }
}
