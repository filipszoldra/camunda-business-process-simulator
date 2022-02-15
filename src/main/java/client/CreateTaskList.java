package client;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import simdata.AssigneeList;
import simdata.SimTask;
import simdata.TaskList;

import java.util.List;

public abstract class CreateTaskList {
    public static void addTasktoAssigneeTaskList(AssigneeList assigneeList, String assignee, String taskName) {
        assigneeList.addTask(taskName, assignee);
    }

    public static TaskList createTaskList(BpmnModelInstance modelInstance, AssigneeList assigneeList, List<InputTaskVar> taskInputs, List<String> condVarNames) {

        TaskList taskList = new TaskList();
        for (var condVar : condVarNames) {
            taskList.addConditionalVar(condVar);
        }
        for (var taskInput : taskInputs
        ) {
            UserTask task = modelInstance.getModelElementById(taskInput.taskId);
            SimTask taskData = new SimTask(task);
            if (taskInput.assigneeName != null) {
                taskData.setAssignee(taskInput.assigneeName);
                addTasktoAssigneeTaskList(assigneeList, taskInput.assigneeName, taskInput.taskId);
            }
            taskData.taskVarList = taskInput.taskVarList;


            taskList.addTask(taskData);

        }
        return taskList;
    }
}
