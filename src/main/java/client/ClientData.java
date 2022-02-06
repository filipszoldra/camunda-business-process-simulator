package client;

import modeleditor.ReplaceNotUserTasks;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

import java.util.List;

public final class ClientData {
    BpmnModelInstance modelInstance;
    InputData inputData;
    public void getModel(BpmnModelInstance modelInstance){
        BpmnModelInstance model = modelInstance.clone();
        ReplaceNotUserTasks.ReplaceNotUserTasks(model);
        this.modelInstance = model;
        inputData = new InputData(modelInstance);
    }

    public void addVarList(List<String> varNames){
        inputData.addVarList(varNames);
    }

    public void addAssignees(List<String> assigneeNames){
        inputData.addAssignees(assigneeNames);
    }

    public List<String> getVarNames(){
        return inputData.variableCollection.getAllVariableNames();
    }


}
