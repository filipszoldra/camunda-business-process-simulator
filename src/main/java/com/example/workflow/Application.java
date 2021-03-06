package com.example.workflow;

import client.CreateTaskList;
import client.InputData;
import client.PararellOrder;
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
import java.util.Scanner;

public class Application {

    public static void main(String... args) throws IOException {
        String modelpath = "C:\\Users\\Filip\\Desktop\\inzynierka\\main\\business process simulation\\modele\\process.bpmn";

        // read bpmn model from file
        BpmnModelInstance modelInstance = Bpmn.readModelFromFile(new File(modelpath));
        ReplaceNotUserTasks.ReplaceNotUserTasks(modelInstance);
        InputData inputData = new InputData(modelInstance);
        inputData.collectDataFromTerminal();

        VariableCollection varCollection = inputData.getVariableCollection();
        AssigneeList assigneeList = inputData.getAssigneeList();
        assigneeList.createAssigneeVarValueRecords(varCollection);
        TaskList taskList = CreateTaskList.createTaskList(modelInstance, assigneeList, inputData.getTaskInput(), inputData.getConditionalVarNames());
        Assignees.addAssignees(modelInstance, assigneeList);
        SetExclusiveGatewayConditions.setExclusiveGatewayConditions(modelInstance, inputData.getGateConds(), inputData.getConditionalVarNames());
        PararellOrderList pararellList = PararellOrder.setPararellOrder(modelInstance, taskList, varCollection);
//        AddTasksPriority.addPriority(taskList, modelInstance);

        Bpmn.validateModel(modelInstance);
        Scanner reader = new Scanner(System.in);

// write edited model to file
        File file = new File("testmodel.bpmn");
        Bpmn.writeModelToFile(file, modelInstance);

        PathCollection pathCollection = new PathCollection(varCollection);
        TaskCounter taskCounter = new TaskCounter(taskList);
        VariableValueRecords variableValueRecords = new VariableValueRecords(varCollection);
        System.out.println("Podaj liczb?? instancji");
        int instanceNumber = reader.nextInt();
        reader.close();
        int instNumber = instanceNumber;
        Simulation.startSimulation(instanceNumber, modelInstance, taskList, varCollection, taskCounter, variableValueRecords, pathCollection, assigneeList, pararellList);
        ResultsSummary.results(instNumber, pathCollection, varCollection, taskCounter, variableValueRecords, assigneeList);
    }

}