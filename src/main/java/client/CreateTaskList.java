package client;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.Query;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.Task;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import simdata.*;

import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public abstract class CreateTaskList {
    public static void addTasktoAssigneeTaskList(AssigneeList assigneeList,String assignee, String taskName){
        assigneeList.addTask(taskName, assignee);
    }
    public static TaskList createTaskList (BpmnModelInstance modelInstance, AssigneeList assigneeList, List<InputTaskVar> taskInputs, List<String> condVarNames) {

        TaskList taskList = new TaskList();
        for(var condVar:condVarNames) {
            taskList.addConditionalVar(condVar);
        }
        for (var taskInput : taskInputs
        ) {
            UserTask task = modelInstance.getModelElementById(taskInput.taskId);
            SimTask taskData = new SimTask(task);
            if(taskInput.assigneeName!=null) {
                taskData.setAssignee(taskInput.assigneeName);
                addTasktoAssigneeTaskList(assigneeList, taskInput.assigneeName, taskInput.taskId);
            }
            taskData.taskVarList = taskInput.taskVarList;


            taskList.addTask(taskData);

        }
    return taskList;
    }
}
