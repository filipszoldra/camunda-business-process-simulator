package client;

import modeleditor.Assignees;
import modeleditor.ReplaceNotUserTasks;
import modeleditor.SetExclusiveGatewayConditions;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import simdata.AssigneeList;
import simdata.TaskList;
import simdata.VariableCollection;
import simulation.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public final class ClientData {
    BpmnModelInstance modelInstance;
    public InputData inputData;
    int instances;
    public void getModel(BpmnModelInstance modelInstance){
        this.modelInstance = modelInstance;
        ReplaceNotUserTasks.ReplaceNotUserTasks(this.modelInstance);
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
    public void setInstances(int instances){
        this.instances = instances;
    }


    public void startSimulation() throws IOException {
        VariableCollection varCollection = inputData.getVariableCollection();
        AssigneeList assigneeList = inputData.getAssigneeList();
        assigneeList.createAssigneeVarValueRecords(varCollection);
        TaskList taskList = CreateTaskList.createTaskList(modelInstance, assigneeList, inputData.getTaskInput(), inputData.getConditionalVarNames());
        Assignees.addAssignees(modelInstance, assigneeList);
        SetExclusiveGatewayConditions.setExclusiveGatewayConditions(modelInstance, inputData.getGateConds(), inputData.getConditionalVarNames());
        PararellOrderList pararellList = PararellOrder.setPararellOrder(modelInstance, taskList, varCollection);
        Bpmn.validateModel(modelInstance);
        File file = new File("testmodel.bpmn");
        Bpmn.writeModelToFile(file, modelInstance);
        PathCollection pathCollection = new PathCollection(varCollection);
        TaskCounter taskCounter = new TaskCounter(taskList);
        VariableValueRecords variableValueRecords = new VariableValueRecords(varCollection);
        PrintWriter writer = new PrintWriter("results.txt", StandardCharsets.UTF_8);
        Simulation.startSimulation(instances, writer, modelInstance, taskList, varCollection, taskCounter, variableValueRecords, pathCollection, assigneeList, pararellList);
        ResultsSummary.results(instances, writer, pathCollection, varCollection, taskCounter, variableValueRecords, assigneeList);
        writer.close();

    }



}
