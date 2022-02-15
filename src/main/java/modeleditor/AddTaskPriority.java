package modeleditor;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Task;

public abstract class AddTaskPriority {
    public static void addPriority(BpmnModelInstance modelInstance, String taskId, String priority) {
        Task task = modelInstance.getModelElementById(taskId);
        task.setCamundaJobPriority(priority);

    }
}
