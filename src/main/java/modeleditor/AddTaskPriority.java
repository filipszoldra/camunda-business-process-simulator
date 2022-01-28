package modeleditor;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.Task;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaInputOutput;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;

import java.util.Collection;

public abstract class AddTaskPriority {
    public static void addPriority(BpmnModelInstance modelInstance, String taskId, String priority){
        Task task = modelInstance.getModelElementById(taskId);
        task.setCamundaJobPriority(priority);

    }
}
