package simulation;

import camundajar.impl.scala.Boolean;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import simdata.VariableCollection;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PathRecord {
    List<HistoricActivityInstance> activityList;
    float counter;
    String probability;
    VariableValueRecords varValueRecords;
    VariableValueRecords varMinValueRecords;
    VariableValueRecords varMaxValueRecords;
    String endName;
    public PathRecord(List<HistoricActivityInstance> activityList){
        this.activityList = activityList;
        this.counter = 1;
    }

    public void increment(){
        counter++;
    }

    public List<HistoricActivityInstance> getActivityList() {
        return activityList;
    }

    public Integer getCounter() {
        return Math.round(this.counter);
    }

    public List<String> getElements(){
        List<String> elements = new ArrayList<>();
        for (var activity : activityList){
            String activityName = activity.getActivityName();
            if (activityName == null){
                activityName = activity.getActivityId();
            }
            elements.add(activity.getActivityType() + " " + activityName);
        }
        return elements;
    }

    public String getPathString(){
        String pathline = "";
        boolean isStarted = false;
        for (var activity : activityList){
            String activityName = activity.getActivityName();
            if (activityName == null){
                activityName = activity.getActivityId();
            }
            if(isStarted == false){
                pathline = pathline.concat("(" + activity.getActivityType() + " " + activityName + ")");
                isStarted = true;
            }
            else{
                pathline = pathline.concat(" -----> (" + activity.getActivityType() + " " + activityName + ")");
                if(activity.getActivityType().equals("noneEndEvent"))
                    this.endName=activityName;
            }
        }
        return pathline;
    }
    public String getEndName(){
        return endName;
    }

    public void setProbability(Integer allCounter){
        float prob = this.counter*100/allCounter;
//        prob = Math.round(prob);
        this.probability = String.valueOf(prob).concat("%");
    }

    public String getProbability(){
        return probability;
    }
    public void addVariableValues(){

    }

    public void createVarValueRecords(VariableCollection varCollection){
        varValueRecords = new VariableValueRecords(varCollection);
        varMinValueRecords = new VariableValueRecords(varCollection);
        varMaxValueRecords = new VariableValueRecords(varCollection);
    }

    public VariableValueRecords getVarValueRecords(){
        return varValueRecords;
    }
    public VariableValueRecords getMinVarValueRecords(){
        return varMinValueRecords;
    }
    public VariableValueRecords getMaxVarValueRecords(){
        return varMaxValueRecords;
    }
    public VariableValueRecords getAverageVarValueRecords(){
        VariableValueRecords averageVarValueRecords = varValueRecords;
        for (var varRecord : varValueRecords.getVariableValueRecordList()){
            averageVarValueRecords.setValue(varRecord.variableName, Math.round(varRecord.value/counter));
        }
        return averageVarValueRecords;
    }

    public void addVarValueRecords(List<VariableRecord> varValueList){
        for(var varRecord : varValueList){
            varValueRecords.varAddValue(varRecord.variableName, varRecord.value);
            if(varMinValueRecords.isStarted(varRecord.variableName) == false){
                varMinValueRecords.setValue(varRecord.variableName, varRecord.value);
                varMinValueRecords.markStarted(varRecord.variableName);
            }
            else if(varRecord.value < varMinValueRecords.getVarValue(varRecord.variableName)){
                varMinValueRecords.setValue(varRecord.variableName, varRecord.value);
            }

            if(varMaxValueRecords.isStarted(varRecord.variableName) == false){
                varMaxValueRecords.setValue(varRecord.variableName, varRecord.value);
                varMaxValueRecords.markStarted(varRecord.variableName);
            }
            else if(varRecord.value > varMaxValueRecords.getVarValue(varRecord.variableName)){
                varMaxValueRecords.setValue(varRecord.variableName, varRecord.value);
            }
        }
    }

}
