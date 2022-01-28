package simdata;

import java.util.ArrayList;
import java.util.List;

public class VariableCollection {
    public List<SimVariable> variablesList = new ArrayList<>();
    int ids = 1;

    public void addSimVariable(SimVariable simVar){
        variablesList.add(simVar);
    }
    public List<String> getAllVariableNames(){
        List<String> namesList = new ArrayList<>();
        for(var variable : variablesList){
            namesList.add(variable.getName());
        }
        return namesList;
    }
    public void createSimVariable(String nameVar){
        SimVariable newVar = new SimVariable(nameVar, ids);
        variablesList.add(newVar);
        ids++;
    }

    public Integer countVariables(){
        return variablesList.size();
    }


    private Integer findVariableIndex(String name){
        for(var variable : variablesList) {
            if (variable.getName() == name) {
                return variablesList.indexOf(variable);
            }
        }
        return null;
    }


    public Integer getDefaultVariableValue(String name){
        return variablesList.get(findVariableIndex(name)).getDefaultValue();
    }

    public List<SimVariable> getVariableList(){
        List<SimVariable> varList = this.variablesList;
        return varList;
    }

    public List<String> getVariableNameList(){
        List<String> nameList = new ArrayList<>();
        for(var variable : variablesList){
            nameList.add(variable.getName());
        }
        return nameList ;
    }

    public String getVarNameById(int id){
        for(var variable : variablesList){
            if(variable.getId() == id){
                return variable.getName();
            }
        }
        return null;
    }


}
