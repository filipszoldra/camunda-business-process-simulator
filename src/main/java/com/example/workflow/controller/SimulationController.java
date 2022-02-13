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
import simulation.results.ResultsData;

import java.io.IOException;
import java.util.*;

@Controller
public class SimulationController {
    ClientData clientData;
    int vars;
    int assignees;
    int varmethod;
    BpmnModelInstance clearModel;
    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @PostMapping("/upload")
    public String uploadBpmn(@RequestParam("file")MultipartFile file2) throws IOException {
        BpmnModelInstance modelInstance = Bpmn.readModelFromStream(file2.getInputStream());
        clearModel = modelInstance.clone();
        clientData = new ClientData();
        clientData.getModel(modelInstance);
        vars=0;
        assignees=0;
        varmethod=0;
        return "form1.html";
    }

    @PostMapping("/newsim")
    public String newSimulation() {
        clientData = new ClientData();
        clientData.getModel(clearModel.clone());
        vars=0;
        assignees=0;
        varmethod=0;
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
        if(vars == 0 && assignees == 0){
            List<String> varNames = new ArrayList<>();
            varNames.add("time");
            List<String> assigneeNames = new ArrayList<>();
            assigneeNames.add("default");
            clientData.inputData.addVarList(varNames);
            clientData.inputData.addAssignees(assigneeNames);
            return generateTaskVarForm(model);
        }
        else if(vars == 0){
            return "varform3.html";
        }
        else if(assignees == 0){
            return "varform2.html";
        }
        else
            return "varform.html";
    }

    @GetMapping("/gettaskvarform")
    public String generateTaskVarForm(Model model){
        clientData.inputData.setAllTaskVarListNames();
        if(clientData.inputData.getAssigneeNames().size()>1){
            if(varmethod==1){
                model.addAttribute("tasks", clientData.inputData.getTaskInput());
                model.addAttribute("assignees", clientData.inputData.getAssigneeNames());
                return "taskvarform.html";
            }
            else if(varmethod == 2){
                model.addAttribute("tasks", clientData.inputData.getTaskInput());
                model.addAttribute("assignees", clientData.inputData.getAssigneeNames());
                return "taskvarform3.html";
            }
            else if(varmethod == 3){
                model.addAttribute("tasks", clientData.inputData.getTaskInput());
                model.addAttribute("assignees", clientData.inputData.getAssigneeNames());
                return "taskvarform7.html";
            }
            else if(varmethod == 4){
                model.addAttribute("tasks", clientData.inputData.getTaskInput());
                model.addAttribute("assignees", clientData.inputData.getAssigneeNames());
                return "taskvarform5.html";
            }

        }
        else{
            if(varmethod==1){
                model.addAttribute("tasks", clientData.inputData.getTaskInput());
                return "taskvarform.html2";
            }
            else if(varmethod == 2){
                model.addAttribute("tasks", clientData.inputData.getTaskInput());
                return "taskvarform4.html";
            }
            else if(varmethod == 3){
                return "taskvarform8.html";
            }
            else if(varmethod == 4){
                return "taskvarform6.html";
            }
        }
        model.addAttribute("tasks", clientData.inputData.getTaskInput());
        model.addAttribute("assignees", clientData.inputData.getAssigneeNames());
        clientData.inputData.getAssigneeNames().remove("default");
        return "taskvarform.html";
    }

    @GetMapping("/getgateform")
    public String generateGateForm(Model model){
        if(clientData.inputData.getGateConds().size()>0) {
            model.addAttribute("gates", clientData.inputData.getGateConds());
            model.addAttribute("variables", clientData.inputData.getVariableCollection().getAllVariableNames());
            return "gateform.html";
        }
        else
            return "simulation.html";
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



    @GetMapping("/getsimbyvar")
    public String getSimByVar(Model model){
        model.addAttribute("variables", clientData.getVarNames());
        return "simulationbyvar.html";
    }

    @GetMapping("/getresults")
    public String getResults(Model model){
        ResultsData resultsData = clientData.resultsData;
        model.addAttribute("pathresults", resultsData.pathResults);
        model.addAttribute("endresults", resultsData.endResults);
        model.addAttribute("assigneeresults", resultsData.assigneeResults);
        model.addAttribute("variableresults", resultsData.variableResults);
        model.addAttribute("taskresults", resultsData.taskResults);
        Map<String, Integer> assigneeData = new LinkedHashMap<String, Integer>();
        int assigneeTimeMax = 0;
        for(var assignee : resultsData.assigneeResults) {
            assigneeData.put(assignee.name, assignee.averagetime);
            if(assigneeTimeMax<assignee.averagetime)
                assigneeTimeMax = assignee.averagetime;
        }
        model.addAttribute("assigneeSet", assigneeData.keySet());
        model.addAttribute("assigneeTime", assigneeData.values());
        model.addAttribute("assigneeTimeMax", assigneeTimeMax);
        model.addAttribute("instnumber", resultsData.instNumber);
        if(resultsData.assigneeResults.size()==1)
            return "resultsform2.html";
        else
            return "resultsform.html";
    }

    @GetMapping("/getpathlist")
    public String getPathList(Model model, String pathName){
        List<String> pathList = new ArrayList<>();
        for(var path : clientData.resultsData.pathResults){
            if(path.pathName.equals(pathName)){
                pathList = path.elements;
            }
        }
        model.addAttribute("pathlist", pathList);
        return "showpath.html";
    }

    @GetMapping("/getdistribution")
    public String getDistribution(Model model, String varName){
        Map<Integer, Integer> varVals = new LinkedHashMap<Integer, Integer>();
        List<Integer> pureVals = new ArrayList<>();
        int max = 0;
        for(var variable : clientData.resultsData.variableResults) {
            if(variable.name.equals(varName)){
                max = variable.distribution.get(0).count;
                for(var record : variable.distribution){
                    varVals.put(record.value, record.count);
                    for(int i=0;i< record.count;i++)
                        pureVals.add(record.value);
                    if(record.count>max)
                        max = record.count;
                }
            }
        }
        model.addAttribute("varSet", varVals.keySet());
        model.addAttribute("varCount", varVals.values());
        model.addAttribute("max", max);
        model.addAttribute("name", varName);
        model.addAttribute("purevals", pureVals);
        return "distributionchart.html";
    }



    @PostMapping("/setgatetypes")
    public String setGateTypes(@RequestParam Map<String,String> allParams, Model model) {
        for(var gate : clientData.inputData.getGateConds()){
            gate.gateType=Integer.valueOf(allParams.get(gate.gateId));
            if(gate.gateType == 0){
                gate.sign1="<=";
                gate.sign2=">";
                gate.condVarName="rand";
                clientData.inputData.addConditionalVar("rand");
            }
        }
        return generateGateVarForm(model);
    }

    @PostMapping("/setgatevars")
    public String setGateVars(@RequestParam Map<String,String> allParams, Model model) {
        for(var gate : clientData.inputData.getGateConds()){
            if(gate.gateType == 0){
                gate.value1 = Integer.valueOf(allParams.get(gate.gateId + "value"));
                gate.value2 = gate.value1;
            }
            else{
                for(var variable : clientData.inputData.variableCollection.getVariableList()){
                    if(variable.getName().equals(allParams.get(gate.gateId + "var"))){
                        String varCondName = clientData.inputData.variableCollection.getVarNameById(variable.getId());
                        clientData.inputData.addConditionalVar(varCondName);
                    }

                }
                clientData.inputData.addConditionalVar(allParams.get(gate.gateId + "var"));
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
            }
        }
        return "simulation.html";
    }

    @PostMapping("/settaskvars")
    public String setTaskVars(@RequestParam Map<String,String> allParams, Model model){
    for(var task : clientData.inputData.getTaskInput()){
        for(var variable : task.taskVarListNames){
            task.taskVarList.setValue(variable,Integer.valueOf(allParams.get(task.taskId+","+variable)));
        }
        task.setAssignne(allParams.get(task.taskId+",assignee"));
    }
        return generateGateForm(model);
    }

    @PostMapping("/settaskvars2")
    public String setTaskVars2(@RequestParam Map<String,String> allParams, Model model){
        for(var task : clientData.inputData.getTaskInput()){
            for(var variable : task.taskVarListNames){
                task.taskVarList.setValue(variable,Integer.valueOf(allParams.get(task.taskId+","+variable)));
            }
            if(!clientData.inputData.getAssigneeNames().get(0).equals("default"))
                task.setAssignne(clientData.inputData.getAssigneeNames().get(0));
        }
        return generateGateForm(model);
    }

    @PostMapping("/settaskvars3")
    public String setTaskVars3(@RequestParam Map<String,String> allParams, Model model){
        for(var task : clientData.inputData.getTaskInput()){
            for(var variable : task.taskVarListNames){
                task.taskVarList.setValue(variable,Integer.valueOf(allParams.get(task.taskId+","+variable+",min")),Integer.valueOf(allParams.get(task.taskId+","+variable+",max")));
            }
            task.setAssignne(allParams.get(task.taskId+",assignee"));
        }
        return generateGateForm(model);
    }

    @PostMapping("/settaskvars4")
    public String setTaskVars4(@RequestParam Map<String,String> allParams, Model model){
        for(var task : clientData.inputData.getTaskInput()){
            for(var variable : task.taskVarListNames){
                task.taskVarList.setValue(variable,Integer.valueOf(allParams.get(task.taskId+","+variable+",min")),Integer.valueOf(allParams.get(task.taskId+","+variable+",max")));
            }
            if(!clientData.inputData.getAssigneeNames().get(0).equals("default"))
                task.setAssignne(clientData.inputData.getAssigneeNames().get(0));
        }
        return generateGateForm(model);
    }

    @PostMapping("/settaskvars5")
    public String setTaskVars5(@RequestParam Map<String,String> allParams, Model model){
        for(var task : clientData.inputData.getTaskInput()){
            for(var variable : task.taskVarListNames){
                task.taskVarList.setValue(variable,Integer.valueOf(allParams.get("varmin")), Integer.valueOf(allParams.get("varmax")));
            }
            task.setAssignne(allParams.get(task.taskId+",assignee"));
        }
        return generateGateForm(model);
    }

    @PostMapping("/settaskvars6")
    public String setTaskVars6(@RequestParam("varmin") int varmin, @RequestParam("varmax") int varmax, Model model){
        for(var task : clientData.inputData.getTaskInput()){
            for(var variable : task.taskVarListNames){
                task.taskVarList.setValue(variable,varmin, varmax);
            }
            if(!clientData.inputData.getAssigneeNames().get(0).equals("default"))
                task.setAssignne(clientData.inputData.getAssigneeNames().get(0));
        }
        return generateGateForm(model);
    }

    @PostMapping("/settaskvars7")
    public String setTaskVars7(@RequestParam Map<String,String> allParams, Model model){
        for(var task : clientData.inputData.getTaskInput()){
            for(var variable : task.taskVarListNames){
                task.taskVarList.setValue(variable,Integer.valueOf(allParams.get("var")));
            }
            task.setAssignne(allParams.get(task.taskId+",assignee"));
        }
        return generateGateForm(model);
    }

    @PostMapping("/settaskvars8")
    public String setTaskVars8(@RequestParam ("var")int value, Model model){
        for(var task : clientData.inputData.getTaskInput()){
            for(var variable : task.taskVarListNames){
                task.taskVarList.setValue(variable,value);
            }
            if(!clientData.inputData.getAssigneeNames().get(0).equals("default"))
                task.setAssignne(clientData.inputData.getAssigneeNames().get(0));
        }
        return generateGateForm(model);
    }





    @PostMapping("/setvars")
    public String setVars(@RequestParam(name = "varname", required = false) String[] allVarNames, @RequestParam(name = "assigneename", required = false) String[] allAssigneeNames, Model model){
        List<String> varNames = new ArrayList<>();
        varNames.add("time");
        List<String> assigneeNames = new ArrayList<>();
        if(allVarNames!=null) {
            for (var str : allVarNames) {
                if(!str.equals("time"))
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
        return generateTaskVarForm(model);
    }




    @PostMapping("/setvarsquantity")
    public String setInitials(@RequestParam("variables") int vars, @RequestParam("assignees") int assignees, @RequestParam("varmethod") int varmethod, Model model) {
        this.vars = vars;
        this.assignees = assignees;
        this.varmethod = varmethod;
        return generateVarForm(model);
    }

    @PostMapping("/setvarsbyterminal")
    public String setByTerminal() {
        clientData.inputData.collectDataFromTerminal();
        return "simulation.html";
    }

    @PostMapping("/startsimulation")
    public String startSimulation(@RequestParam("instances") int instances, Model model) throws IOException {
        clientData.setInstances(instances);
        clientData.startSimulation();
        return getResults(model);
    }

    @PostMapping("/startsimulationbyvar")
    public String startSimulation(@RequestParam("variable") String varName, @RequestParam("value") int varValue, Model model) throws IOException {
        clientData.startSimulationByVar(varName, varValue);
        return getResults(model);
    }


    @PostMapping("/showpath")
    public String showPath(@RequestParam("Zobacz ścieżkę") String pathName, Model model){
        return getPathList(model, pathName);
    }

    @PostMapping("/showdistribution")
    public String showDistriburion(@RequestParam("distribution") String varName, Model model){
        return getDistribution(model, varName);
    }


}
