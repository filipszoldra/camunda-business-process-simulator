package com.example.workflow.controller;

import client.*;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class CamundaController {
    ClientData clientData = new ClientData();
    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @PostMapping("/upload")
    public String uploadBpmn(@RequestParam("file")MultipartFile file2) throws IOException {
        BpmnModelInstance modelInstance = Bpmn.readModelFromStream(file2.getInputStream());
        clientData.getModel(modelInstance);
//        ReplaceNotUserTasks.ReplaceNotUserTasks(modelInstance);
//        Scanner reader = new Scanner(System.in);
//
//        VariableCollection varCollection = GetVariables.createVariables();
//        AssigneeList assigneeList = CreateAssigneeList.createAssigneeList(varCollection);
//        TaskList taskList = CreateTaskList.createTaskList(modelInstance, varCollection, assigneeList);
//        Assignees.addAssignees(modelInstance, assigneeList);
//        SetExclusiveGatewayConditions.setExclusiveGatewayConditions(modelInstance, taskList, varCollection);
//        PararellOrderList pararellList = PararellOrder.setPararellOrder(modelInstance, taskList, varCollection);
//        Bpmn.validateModel(modelInstance);
//
//        File file = new File("testmodel.bpmn");
//        Bpmn.writeModelToFile(file, modelInstance);
//
//        PathCollection pathCollection = new PathCollection(varCollection);
//        TaskCounter taskCounter = new TaskCounter(taskList);
//        VariableValueRecords variableValueRecords = new VariableValueRecords(varCollection);
//        System.out.println("Podaj liczbę instancji");
//        int instanceNumber = reader.nextInt();
//        reader.close();
//        int instNumber = instanceNumber;
//        PrintWriter writer = new PrintWriter("results.txt", StandardCharsets.UTF_8);
//        Simulation.startSimulation(instanceNumber, writer, modelInstance, taskList, varCollection, taskCounter, variableValueRecords, pathCollection, assigneeList, pararellList);
//        ResultsSummary.results(instNumber, writer, pathCollection, varCollection, taskCounter, variableValueRecords, assigneeList);
//        writer.close();

        return "formMulti.html";
    }

    @GetMapping("/count")
    public String generateForm(Model model) {
        var listCount = new ArrayList<>();
        int i=0;
        for(var name : clientData.getVarNames()) {
            listCount.add(i);
            i++;
        }
        model.addAttribute("formcount", listCount);
        return "form2.html";
    }





    @PostMapping("/endpoint1")
    public String acceptForm(@RequestParam("varNames") String varNames, @RequestParam("assignee") String assignees) {
        List<String> seperatedVarNames = Arrays.asList(varNames.split(","));
        clientData.addVarList(seperatedVarNames);
        List<String> seperatedAssignees = Arrays.asList(assignees.split(","));
        clientData.addAssignees(seperatedAssignees);
        return "formMulti.html";
    }

//    @PostMapping("/endpoint2")
//    public String assignees(@RequestParam("assignee") String assignees) {
//        List<String> seperatedAssignees = Arrays.asList(assignees.split(","));
//        clientData.addAssignees(seperatedAssignees);
//        return "formMulti.html";
//    }


}
