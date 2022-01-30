package simdata;

import simulation.TaskCounter;
import simulation.VariableValueRecords;

import java.util.ArrayList;
import java.util.List;

public class AssigneeRecord {
    VariableValueRecords varValueRecords;
    public String assignee;
    List<String> assigneeTask = new ArrayList<>();
    public int id;
    public AssigneeRecord(String assignee, int id) {
        this.assignee = assignee;
        this.id = id;
    }
    public void addTask(String taskName){
        assigneeTask.add(taskName);
    }
    public List<String> getTaskNameList(){
        return assigneeTask;
    }
    public void createVariableValueRecords(VariableCollection variableCollection){
        this.varValueRecords = new VariableValueRecords(variableCollection);
    }
    public void addVarValue(String varName, int varValue){
        varValueRecords.varAddValue(varName, varValue);
    }

    public VariableValueRecords getVarValueRecords(){
        return varValueRecords;
    }
}
