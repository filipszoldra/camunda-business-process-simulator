package com.example.workflow;

import client.AddTasksPriority;
import client.CreateAssigneeList;
import client.CreateTaskList;
import client.GetVariables;
import modeleditor.Assignees;
import modeleditor.ReplaceNotUserTasks;
import modeleditor.SetExclusiveGatewayConditions;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricActivityInstanceQuery;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import simdata.*;
import simulation.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Application {

    public static void main(String... args) throws IOException {
        String modelpath = "C:\\Users\\Filip\\Desktop\\inzynierka\\main\\business process simulation\\business process simulation\\src\\main\\resources\\process.bpmn";

        // read bpmn model from file
        BpmnModelInstance modelInstance = Bpmn.readModelFromFile(new File(modelpath));
        ReplaceNotUserTasks.ReplaceNotUserTasks(modelInstance);
        Scanner reader = new Scanner(System.in);

        VariableCollection varCollection = GetVariables.createVariables();
        AssigneeList assigneeList = CreateAssigneeList.createAssigneeList(varCollection);
        TaskList taskList = CreateTaskList.createTaskList(modelInstance, varCollection, assigneeList);
        Assignees.addAssignees(modelInstance, assigneeList);
        SetExclusiveGatewayConditions.setExclusiveGatewayConditions(modelInstance, taskList, varCollection);
//        AddTasksPriority.addPriority(taskList, modelInstance);
        Bpmn.validateModel(modelInstance);

// write edited model to file
        File file = new File("testmodel.bpmn");
        Bpmn.writeModelToFile(file, modelInstance);

        PathCollection pathCollection = new PathCollection(varCollection);
        TaskCounter taskCounter = new TaskCounter(taskList);
        VariableValueRecords variableValueRecords = new VariableValueRecords(varCollection);
        System.out.println("Podaj liczbę instancji");
        int instanceNumber = reader.nextInt();
        reader.close();
        int instNumber = instanceNumber;
        PrintWriter writer = new PrintWriter("results.txt", StandardCharsets.UTF_8);
        Simulation.startSimulation(instanceNumber, writer, modelInstance, taskList, varCollection, taskCounter, variableValueRecords, pathCollection, assigneeList);
        ResultsSummary.results(instNumber, writer, pathCollection, varCollection, taskCounter, variableValueRecords, assigneeList);
        writer.close();
    }
}