package simulation;

import com.sun.jdi.Value;
import simdata.TaskList;

import java.security.Key;

import java.util.*;

public class TaskCounter {
    private List<TaskCountRecord> taskCounter = new ArrayList<>(); {
    };

    public TaskCounter(TaskList taskList){
        for (String taskId : taskList.getTaskIdList()){
            TaskCountRecord taskCountRecord = new TaskCountRecord(taskId, taskList.getTaskNameById(taskId));
            taskCounter.add(taskCountRecord);
        }
    };

    public void taskIncrement(String taskId){
        for (var task : taskCounter){
            if(task.taskId.equals(taskId)){
                task.counter ++;
            }
        }

    }

    public Integer getTaskCountNumber(String taskId){
        for (var task : taskCounter){
            if(task.taskId.equals(taskId)){
                return task.counter;
            }
        }
        return null;
    }

    public List<TaskCountRecord> getTaskCounterList(){
        return taskCounter;
    }

}
