package client;

import modeleditor.AddTaskPriority;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import simdata.TaskList;

import java.util.Scanner;

public abstract class AddTasksPriority {
    public static void addPriority(TaskList taskList, BpmnModelInstance modelInstance) {
        Scanner reader = new Scanner(System.in);
        for (var task : taskList.getTaskIdList()) {
            System.out.println("Podaj priorytet dla " + taskList.getTaskNameById(task));
            int priority = reader.nextInt();
            AddTaskPriority.addPriority(modelInstance, task, String.valueOf(priority));
        }

    }
}
