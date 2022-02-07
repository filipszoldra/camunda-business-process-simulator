package client;

import org.camunda.bpm.model.bpmn.instance.Task;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import simdata.TaskVariableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InputTaskVar {
    public String taskId;
    public String taskName;
    public TaskVariableList taskVarList = new TaskVariableList();
    public List<String> taskVarListNames;
    public String assigneeName;
    int value;

    public InputTaskVar(String taskId, String taskName){
        this.taskId = taskId;
        if(taskName == null){
            this.taskName = taskId;
        }
        else
            this.taskName = taskName;
    }
    public void setAssignne(String name){
        this.assigneeName = name;
    }
    public void setTaskVarListNames(){
        taskVarListNames = taskVarList.getAllVariableNames();
    }

}

