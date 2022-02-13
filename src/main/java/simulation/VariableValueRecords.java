package simulation;

import simdata.VariableCollection;

import java.util.ArrayList;
import java.util.List;

public class VariableValueRecords {
    private List<VariableRecord> variableValueRecordList = new ArrayList<>(); {
    }

    int instanceNumber;
    public VariableValueRecords(){
    }
    public VariableValueRecords(VariableCollection varCollection){
        for (String varName : varCollection.getVariableNameList()){
            VariableRecord varRecord = new VariableRecord(varName);
            variableValueRecordList.add(varRecord);
        }
    }




    public void varAddValue(String varName, Integer value){
        for (var variable : variableValueRecordList){
            if(variable.variableName.equals(varName)){
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

    public void addProcessTime(int time){
        varAddValue("process time", time);
    }


    public List<String> getVarNamesList(){
        List<String> varNameList = new ArrayList<>();
        for (var variable : variableValueRecordList){
            varNameList.add(variable.variableName);
        }
        return varNameList;
    }

    public void setInstanceNumber(int instanceNumber){
        this.instanceNumber = instanceNumber;
    }

    public int getInstanceNumber(){
        return instanceNumber;
    }

    public List<VariableRecord> getVariableValueRecordList(){
//        VariableRecord timeRecord = new VariableRecord("Process Time", this.processTime);
//        variableValueRecordList.add(timeRecord);
        return variableValueRecordList;
    }

    public List<String> getVariableRecordNames(){
        List<String> names = new ArrayList<>();
        for(var record : variableValueRecordList){
            names.add(record.variableName);
        }
        return names;

    }




}
