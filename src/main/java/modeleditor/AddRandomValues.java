package modeleditor;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.Task;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaInputOutput;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaInputParameter;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaOutputParameter;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import simdata.GetRandomValue;

import java.util.Collection;

public abstract class AddRandomValues {
    public static void AddRandomValues(BpmnModelInstance modelInstance) {
        ModelElementType taskType = modelInstance.getModel().getType(Task.class);
        Collection<ModelElementInstance> taskInstances = modelInstance.getModelElementsByType(taskType);

        for (var taskInstance : taskInstances
        ) {
            Task task = modelInstance.getModelElementById(taskInstance.getAttributeValue("id"));
            ExtensionElements taskElements = task.getExtensionElements();
            if (taskElements == null) {
                taskElements = modelInstance.newInstance(ExtensionElements.class);
                task.setExtensionElements(taskElements);
            }
            ModelElementInstance inputOutputElement = taskElements.getUniqueChildElementByType(CamundaInputOutput.class);
            if (inputOutputElement == null) {
                inputOutputElement = taskElements.addExtensionElement(CamundaInputOutput.class);
            }


            CamundaInputParameter inputParameter = modelInstance.newInstance(CamundaInputParameter.class);
            inputParameter.setCamundaName("randVal");
            int random = GetRandomValue.GetRandomValue();
            inputParameter.setTextContent(String.valueOf(random));
            inputOutputElement.addChildElement(inputParameter);

            CamundaOutputParameter outputParameter = modelInstance.newInstance(CamundaOutputParameter.class);
            outputParameter.setCamundaName("randomValue");
            outputParameter.setTextContent("${randVal}");
            inputOutputElement.addChildElement(outputParameter);
        }
    }
}
