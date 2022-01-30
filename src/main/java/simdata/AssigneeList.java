package simdata;

import simulation.VariableValueRecords;

import java.util.ArrayList;
import java.util.List;

public class AssigneeList {
    List<AssigneeRecord> assigneeList = new ArrayList<>();
    int ids = 0;
    public void addAssigneeRecord(AssigneeRecord newrecord){
        assigneeList.add(newrecord);
    }
    public void addAssignne (String assignee){
        ids++;
        AssigneeRecord newAssignee = new AssigneeRecord(assignee, ids);
        addAssigneeRecord(newAssignee);
    }
    public String getAssigneeNameById(int id){
        for (var assignee : assigneeList){
            if(assignee.id == id){
                return assignee.assignee;
            }
        }
        return null;
    }

    public List<AssigneeRecord> getAssigneeList(){
        return assigneeList;
    }

    public void addTask(String taskId, int id){
        for (var assignee : assigneeList){
            if(assignee.id == id){
                assignee.addTask(taskId);
            }
        }
    }

    public List<String> getAssigneTaskNameList(String assigneeName){
        for (var assignee : assigneeList){
            if(assignee.assignee.equals(assigneeName)){
                return assignee.getTaskNameList();
            }
        }
        return null;
    }

    public void createAssigneeVarValueRecords(VariableCollection varCollection){
        for (var assignee : assigneeList){
            assignee.createVariableValueRecords(varCollection);
        }
    }

    public void addVarValue(String assigneeName, String varName, int varValue){
        for (var assignee : assigneeList){
            if(assignee.assignee.equals(assigneeName)){
                assignee.addVarValue(varName, varValue);
            }
        }
    }

    public VariableValueRecords getAssigneeVarValueRecords(String assigneeName){
        for (var assignee : assigneeList){
            if(assigneeName.equals(assigneeName)){
                return assignee.getVarValueRecords();
            }
        }
        return null;
    }

}
