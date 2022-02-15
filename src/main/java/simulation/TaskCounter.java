package simulation;

import simdata.TaskList;

import java.util.ArrayList;
import java.util.List;

public class TaskCounter {
    private final List<TaskCountRecord> taskCounter = new ArrayList<>();

    {
    }

    public TaskCounter(TaskList taskList) {
        for (String taskId : taskList.getTaskIdList()) {
            TaskCountRecord taskCountRecord = new TaskCountRecord(taskId, taskList.getTaskNameById(taskId));
            taskCounter.add(taskCountRecord);
        }
    }

    public void taskIncrement(String taskId) {
        for (var task : taskCounter) {
            if (task.taskId.equals(taskId)) {
                task.counter++;
            }
        }

    }

    public Integer getTaskCountNumber(String taskId) {
        for (var task : taskCounter) {
            if (task.taskId.equals(taskId)) {
                return task.counter;
            }
        }
        return null;
    }

    public List<TaskCountRecord> getTaskCounterList() {
        return taskCounter;
    }

}
