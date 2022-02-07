package com.example.workflow.controller;

import client.*;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class CamundaController {
    ClientData clientData = new ClientData();
    int vars=0;
    int assignees=0;
    int varmethod=0;
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

        return "form1.html";
    }


    @GetMapping("/getvarform")
    public String generateVarForm(Model model) {
        var varCount = new ArrayList<>();
        for(int i= 1; i<=vars; i++) {
            varCount.add(i);
        }
        model.addAttribute("varcount", varCount);
        var assigneeCount = new ArrayList<>();
        for(int j= 1; j<=assignees; j++) {
            assigneeCount.add(j);
        }
        model.addAttribute("assigneecount", assigneeCount);
        return "varform.html";
    }

    @GetMapping("/gettaskvarform")
    public String generateTaskVarForm(Model model){
        clientData.inputData.setAllTaskVarListNames();
        model.addAttribute("tasks", clientData.inputData.getTaskInput());
        model.addAttribute("assignees", clientData.inputData.getAssigneeNames());
        return "taskvarform.html";
    }

    @GetMapping("/getgateform")
    public String generateGateForm(Model model){
        model.addAttribute("gates", clientData.inputData.getGateConds());
        model.addAttribute("variables", clientData.inputData.getVariableCollection().getAllVariableNames());
        return "gateform.html";
    }
    @GetMapping("/getgatevarform")
    public String generateGateVarForm(Model model){
        List<ExclusiveGateConditionalsInput> randomGates = new ArrayList<>();
        List<ExclusiveGateConditionalsInput> varGates = new ArrayList<>();
        for(var gate : clientData.inputData.getGateConds()){
            if(gate.gateType == 0){
                randomGates.add(gate);
            }
            else
                varGates.add(gate);
        }
        model.addAttribute("randomgates", randomGates);
        model.addAttribute("vargates", varGates);
        model.addAttribute("variables", clientData.inputData.getVariableCollection().getAllVariableNames());
        return "gatevarform.html";
    }

    @GetMapping("/count")
    public String generateForm(Model model) {
        model.addAttribute("variables", clientData.getVarNames());
        var listCount = new ArrayList<>();
        int i=0;
        for(var name : clientData.getVarNames()) {
            listCount.add(i);
            i++;
        }
        model.addAttribute("formcount", listCount);
        return "form2.html";
    }

    @PostMapping("/setgatetypes")
    public String setGateTypes(@RequestParam Map<String,String> allParams) {
        for(var gate : clientData.inputData.getGateConds()){
            gate.gateType=Integer.valueOf(allParams.get(gate.gateId));
            if(gate.gateType == 0){
                gate.sign1="<=";
                gate.sign2=">";
                gate.condVarName="rand";
                clientData.inputData.addConditionalVar("rand");
            }
        }
        return "gateform.html";
    }

    @PostMapping("/setgatevars")
    public String setGateVars(@RequestParam Map<String,String> allParams) {
        for(var gate : clientData.inputData.getGateConds()){
            if(gate.gateType == 0){
                gate.value1 = Integer.valueOf(allParams.get(gate.gateId + "value"));
                gate.value2 = 100 - gate.value1;
            }
            else{
                gate.value1 = Integer.valueOf(allParams.get(gate.gateId + "value"));
                gate.value2 = Integer.valueOf(allParams.get(gate.gateId + "value"));
                int signVal = Integer.valueOf(allParams.get(gate.gateId + "sign"));
                if(signVal == 1){
                    gate.sign1 = ">";
                    gate.sign2 = "<=";
                }
                else if(signVal == 2){
                    gate.sign1 = "==";
                    gate.sign2 = "!=";
                }
                else{
                    gate.sign1 = "<";
                    gate.sign2 = ">=";
                }
                gate.condVarName = allParams.get(gate.gateId + "var");
                clientData.inputData.addConditionalVar(gate.condVarName);
            }
        }
        return "simulation.html";
    }

    @PostMapping("/settaskvars")
    public String setTaskVars(@RequestParam Map<String,String> allParams){
    for(var task : clientData.inputData.getTaskInput()){
        for(var variable : task.taskVarListNames){
            task.taskVarList.setValue(variable,Integer.valueOf(allParams.get(task.taskId+","+variable)));
        }
        task.setAssignne(allParams.get(task.taskId+",assignee"));
    }
        return"taskvarform";
    }




    @PostMapping("/setvars")
    public String setVars(@RequestParam(name = "varname", required = false) String[] allVarNames, @RequestParam(name = "assigneename", required = false) String[] allAssigneeNames){
        List<String> varNames = new ArrayList<>();
        varNames.add("time");
        List<String> assigneeNames = new ArrayList<>();
        if(allVarNames!=null) {
            for (var str : allVarNames) {
                varNames.add(str);
            }
        }
        if(allAssigneeNames!=null) {
            for (var str : allAssigneeNames) {
                assigneeNames.add(str);
            }
        }
        clientData.inputData.addVarList(varNames);
        if(assigneeNames.size() == 0){
            assigneeNames.add("default");
        }
        clientData.inputData.addAssignees(assigneeNames);
        return"varform";
    }


    @PostMapping("/setvarsquantity")
    public String setInitials(@RequestParam("variables") int vars, @RequestParam("assignees") int assignees, @RequestParam("varmethod") int varmethod) {
        this.vars = vars;
        this.assignees = assignees;
        this.varmethod = varmethod;
        return "form1.html";
    }

    @PostMapping("/startsimulation")
    public String startSimulation(@RequestParam("instances") int instances) throws IOException {
        clientData.setInstances(instances);
        clientData.startSimulation();
        return "simulationstarted.html";
    }


    @PostMapping("/endpoint1")
    public String acceptForm(@RequestParam("varNames") String varNames, @RequestParam("assignee") String assignees) {
        List<String> seperatedVarNames = Arrays.asList(varNames.split(","));
        clientData.addVarList(seperatedVarNames);
        List<String> seperatedAssignees = Arrays.asList(assignees.split(","));
        clientData.addAssignees(seperatedAssignees);
        return "formMulti.html";
    }



}
