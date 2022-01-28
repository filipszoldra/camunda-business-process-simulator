package simulation;

import simdata.VariableCollection;

import java.util.ArrayList;
import java.util.List;

public class VariableValueRecords {
    private List<VariableRecord> variableValueRecordList = new ArrayList<>(); {
    };
    int instanceNumber;

    public VariableValueRecords(){
    }
    public VariableValueRecords(VariableCollection varCollection){
        for (String varName : varCollection.getVariableNameList()){
            VariableRecord varRecord = new VariableRecord(varName);
            variableValueRecordList.add(varRecord);
        }
    };

    public void varAddValue(String varName, Integer value){
        for (var variable : variableValueRecordList){
            if(variable.variableName == varName){
                variable.value += value;
            }
        }

    }
    public void setValue(String varName, Integer value){
        for (var variable : variableValueRecordList){
            if(variable.variableName == varName){
                variable.value = value;
            }
        }
    }

    public boolean isStarted(String varName){
        for (var variable : variableValueRecordList){
            if(variable.variableName == varName){
                return variable.getIsStarted();
            }
        }
        return false;
    }

    public void markStarted(String varName){
        for (var variable : variableValueRecordList){
            if(variable.variableName == varName){
                variable.markStarted();
            }
        }
    }


    public Integer getVarValue(String varName){
        for (var variable : variableValueRecordList){
            if(variable.variableName == varName){
                return variable.value;
            }
        }
        return null;
    }


    public void setInstanceNumber(int instanceNumber){
        this.instanceNumber = instanceNumber;
    }

    public int getInstanceNumber(){
        return instanceNumber;
    }

    public List<VariableRecord> getVariableValueRecordList(){
        return variableValueRecordList;
    }

//    public VariableValueRecords getVariableAverageValueRecordList(VariableValueRecords allVariableValueRecords, int counter){
//        VariableValueRecords averageValueRecordList = allVariableValueRecords;
//        for(var variable : averageValueRecordList.getVariableValueRecordList()){
//            averageValueRecordList.setValue(variable.variableName, allVariableValueRecords.getVarValue(variable.variableName)/counter);
//        }
//        return averageValueRecordList;
//    }


}
