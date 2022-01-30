package modeleditor;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Task;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import simdata.AssigneeList;

public abstract class Assignees {
    public static void addAssignees(BpmnModelInstance modelInstance, AssigneeList assigneeList){
        for (var assignee : assigneeList.getAssigneeList()){
            String assigneeName = assignee.assignee;
            for (var taskId : assignee.getTaskNameList()){
                UserTask task = modelInstance.getModelElementById(taskId);
                task.setCamundaAssignee(assigneeName);
            }
        }
    }
}
