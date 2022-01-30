package simdata;


import org.camunda.bpm.model.bpmn.instance.Task;
import org.camunda.bpm.model.bpmn.instance.UserTask;

import java.util.List;

public class SimTask {

    private Task task;
    public String taskId;
    public String taskName;
    public String assignee;
    public TaskVariableList taskVarList;
    public SimTask(UserTask task){
        this.task = task;
        this.taskId = task.getId();
        this.taskName = task.getName();
        if (this.taskName == null){
            this.taskName = taskId;
        }
        taskVarList = new TaskVariableList();
    }

    public void setAssignee(String assignee){
        this.assignee = assignee;
    }



    public List<TaskVariable> getTaskVarList(){
        return taskVarList.getTaskVarList();
    }

}
