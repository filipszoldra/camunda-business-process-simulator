package simdata;

import java.util.ArrayList;
import java.util.List;

public class TaskList {
    List<SimTask> taskList = new ArrayList();
    List<String> conditionalVars = new ArrayList<>();
    public TaskList(){
    };

    public void addTask(SimTask task){
        taskList.add(task);
    }

    public SimTask getTask(String taskId){
        for(var task: taskList){
            if(task.taskId == taskId){
                return task;
            }
        }
        return null;
    }
    public TaskVariable getTaskVariable(String taskId, String taskName){
        for(var task: taskList){
            if(task.taskId == taskId){
                return task.taskVarList.getTaskVariable(taskName);
            }
        }
        return null;
    }

    public Integer getTaskVariableValue(String taskId, String taskName){
        for(var task: taskList){
            if(task.taskId == taskId){
                return task.taskVarList.getValue(taskName);
            }
        }
        return null;
    }

    public List<String> getTaskIdList(){
        List<String> taskIdList = new ArrayList();
        for(var task : taskList){
            taskIdList.add(task.taskId);
        }
        return taskIdList;
    }

    public String getTaskNameById(String taskId){
        for(var task : taskList){
            if(task.taskId.equals(taskId)){
                return task.taskName;
            }
        }
        return null;
    }

    public List<TaskVariable> getTaskVarList(String taskId){
        for (var task : taskList){
            if(task.taskId.equals(taskId)){
                return task.getTaskVarList();
            }
        }
        return null;
    }
    public void addConditionalVar(String conditionalVarName){

        conditionalVars.add(conditionalVarName);

    }

    public List<String> getConditionalVars(){
        return conditionalVars;
    }


}
