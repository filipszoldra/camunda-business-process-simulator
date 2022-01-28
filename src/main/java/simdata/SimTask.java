package simdata;


import org.camunda.bpm.model.bpmn.instance.Task;

import java.util.List;

public class SimTask {

    private Task task;
    public String taskId;
    public String taskName;
    public TaskVariableList taskVarList;
    public SimTask(Task task){
        this.task = task;
        this.taskId = task.getId();
        this.taskName = task.getName();
        if (this.taskName == null){
            this.taskName = taskId;
        }
        taskVarList = new TaskVariableList();
    }





    public List<TaskVariable> getTaskVarList(){
        return taskVarList.getTaskVarList();
    }

}
