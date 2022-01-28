package modeleditor;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Task;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;

import java.util.Collection;

public abstract class ReplaceNotUserTasks {
    public static void ReplaceNotUserTasks (BpmnModelInstance modelInstance){
        ModelElementType taskType = modelInstance.getModel().getType(Task.class);
        ModelElementType userTaskType = modelInstance.getModel().getType(UserTask.class);
        Collection<ModelElementInstance> taskInstances = modelInstance.getModelElementsByType(taskType);
        Collection<ModelElementInstance> userTaskInstances = modelInstance.getModelElementsByType(userTaskType);
        taskInstances.removeAll(userTaskInstances);
        for(var taskInstance : taskInstances){
            Task task = modelInstance.getModelElementById(taskInstance.getAttributeValue("id"));
            UserTask newTask = modelInstance.newInstance(UserTask.class);
            newTask.setId(task.getId());
            newTask.setName(task.getName());
            task.replaceWithElement(newTask);
        }

    }
}
