package simulation;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricActivityInstanceQuery;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import simdata.*;
import simulation.results.ResultsData;

import java.io.PrintWriter;
import java.util.List;

public abstract class Simulation {
    public static ResultsData startSimulation(int instanceNumber, BpmnModelInstance modelInstance, TaskList taskList,
                                              VariableCollection varCollection, TaskCounter taskCounter,
                                              VariableValueRecords variableValueRecords, PathCollection pathCollection,
                                              AssigneeList assigneeList, PararellOrderList pararellList) {

        int inst = 1;
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        String processId = modelInstance.getModelElementsByType(org.camunda.bpm.model.bpmn.instance.Process.class)
                .iterator().next().getId();
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment().name(processId);
        deploymentBuilder.addModelInstance(processId + ".bpmn", modelInstance);
        deploymentBuilder.deploy();
        HistoryService historyService = processEngine.getHistoryService();

        while (instanceNumber > 0) {
            RuntimeService runtimeService = processEngine.getRuntimeService();
            TaskCounter instanceTaskCounter = new TaskCounter(taskList);
            VariableValueRecords instanceVariableValueRecords = new VariableValueRecords(varCollection);
            ProcessTimer processTimer = new ProcessTimer(pararellList, taskList);
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processId);
            String processInstanceId = processInstance.getId();
            TaskService taskService = processEngine.getTaskService();
            org.camunda.bpm.engine.task.TaskQuery taskQuery = taskService.createTaskQuery()
                    .active();
            List<Task> activeTasks = taskQuery.unlimitedList();

            while (activeTasks.size() > 0) {
                org.camunda.bpm.engine.task.Task actualTask = activeTasks.get(0);
                activeTasks.remove(0);
                String taskKey = actualTask.getTaskDefinitionKey();
                taskCounter.taskIncrement(taskKey);
                instanceTaskCounter.taskIncrement(taskKey);
                List<TaskVariable> taskVarList = taskList.getTaskVarList(taskKey);
                String taskAssignee = actualTask.getAssignee();
                for (var taskVar : taskVarList) {
                    int value = taskVar.getValue();
                    instanceVariableValueRecords.varAddValue(taskVar.getName(), value);
                    assigneeList.addVarValue(taskAssignee, taskVar.getName(), value);
                    variableValueRecords.varAddValue(taskVar.getName(), value);
                    if (taskVar.getName().equals("time"))
                        processTimer.addTaskTimeRecord(taskKey, value);
                }
                for (var condVar : taskList.getConditionalVars()) {
                    String condVarVal = condVar.concat("Val");
                    if (condVarVal.equals("randVal")) {
                        taskService.setVariable(actualTask.getId(), condVarVal, GetRandomValue.GetRandomValue());
                    } else {
                        taskService.setVariable(actualTask.getId(), condVarVal, instanceVariableValueRecords.getVarValue(condVar));
                    }
                }
                taskService.complete(actualTask.getId());
                taskQuery = taskService.createTaskQuery()

                        .active();
                activeTasks = taskQuery.unlimitedList();

            }
            processTimer.setProcessTime();
            int processTime = processTimer.getProcessTime();
            instanceVariableValueRecords.addProcessTime(processTime);
            variableValueRecords.addProcessTime(processTime);
            instanceNumber--;
            inst++;

            HistoricActivityInstanceQuery activityQuery = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .finished()
                    .orderByHistoricActivityInstanceStartTime().asc()
                    .orderByHistoricActivityInstanceEndTime().asc()
                    .orderPartiallyByOccurrence().asc();
            List<HistoricActivityInstance> activityList = activityQuery.unlimitedList();
            pathCollection.addPath(activityList, instanceVariableValueRecords);
        }
        return ResultsSummary.getResults(inst - 1, pathCollection, varCollection, taskCounter, variableValueRecords, assigneeList);
    }


    public static ResultsData startSimulationByVar(String varName, int varValue, PrintWriter writer, BpmnModelInstance modelInstance, TaskList taskList, VariableCollection varCollection, TaskCounter taskCounter, VariableValueRecords variableValueRecords, PathCollection pathCollection, AssigneeList assigneeList, PararellOrderList pararellList) {

        int inst = 1;
        int mainValue = 0;
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();
        String processId = modelInstance.getModelElementsByType(org.camunda.bpm.model.bpmn.instance.Process.class).iterator().next().getId();
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment().name(processId);
        deploymentBuilder.addModelInstance(processId + ".bpmn", modelInstance);
        deploymentBuilder.deploy();
        HistoryService historyService = processEngine.getHistoryService();

        while (mainValue < varValue) {
            writer.println();
            writer.println("Instancja " + inst);
            writer.println();

            RuntimeService runtimeService = processEngine.getRuntimeService();
            TaskCounter instanceTaskCounter = new TaskCounter(taskList);
            VariableValueRecords instanceVariableValueRecords = new VariableValueRecords(varCollection);
            ProcessTimer processTimer = new ProcessTimer(pararellList, taskList);

            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processId);
            String processInstanceId = processInstance.getId();
            TaskService taskService = processEngine.getTaskService();


            org.camunda.bpm.engine.task.TaskQuery taskQuery = taskService.createTaskQuery()
                    .active();
            List<Task> activeTasks = taskQuery.unlimitedList();


            while (activeTasks.size() > 0) {
                org.camunda.bpm.engine.task.Task actualTask = activeTasks.get(0);
                activeTasks.remove(0);
                String taskKey = actualTask.getTaskDefinitionKey();
                taskCounter.taskIncrement(taskKey);
                instanceTaskCounter.taskIncrement(taskKey);
                List<TaskVariable> taskVarList = taskList.getTaskVarList(taskKey);
                String taskAssignee = actualTask.getAssignee();
                for (var taskVar : taskVarList) {
                    if (taskVar.getName().equals(varName)) {
                        int value = taskVar.getValue();
                        mainValue += value;
//                        if (mainValue <= varValue) {
//                            instanceVariableValueRecords.varAddValue(taskVar.getName(), value);
//                            assigneeList.addVarValue(taskAssignee, taskVar.getName(), value);
//                            variableValueRecords.varAddValue(taskVar.getName(), value);
//                            if (taskVar.getName().equals("time"))
//                                processTimer.addTaskTimeRecord(taskKey, value);
//                        }

                            instanceVariableValueRecords.varAddValue(taskVar.getName(), value);
                            assigneeList.addVarValue(taskAssignee, taskVar.getName(), value);
                            variableValueRecords.varAddValue(taskVar.getName(), value);
                            if (taskVar.getName().equals("time"))
                                processTimer.addTaskTimeRecord(taskKey, value);

                    }
                }
//                if (mainValue > varValue)
//                    break;
                for (var taskVar : taskVarList) {
                    if (!taskVar.getName().equals(varName)) {
                        int value = taskVar.getValue();
                        instanceVariableValueRecords.varAddValue(taskVar.getName(), value);
                        assigneeList.addVarValue(taskAssignee, taskVar.getName(), value);
                        variableValueRecords.varAddValue(taskVar.getName(), value);
                        if (taskVar.getName().equals("time"))
                            processTimer.addTaskTimeRecord(taskKey, value);
                    }
                }
                for (var condVar : taskList.getConditionalVars()) {
                    String condVarVal = condVar.concat("Val");
                    if (condVarVal.equals("randVal")) {
                        taskService.setVariable(actualTask.getId(), condVarVal, GetRandomValue.GetRandomValue());
                    } else {
                        taskService.setVariable(actualTask.getId(), condVarVal, instanceVariableValueRecords.getVarValue(condVar));
                    }
                }
                taskService.complete(actualTask.getId());
                taskQuery = taskService.createTaskQuery()

                        .active();
                activeTasks = taskQuery.unlimitedList();

            }

//            if (mainValue > varValue)
//                break;
            processTimer.setProcessTime();
            int processTime = processTimer.getProcessTime();
            instanceVariableValueRecords.addProcessTime(processTime);
            variableValueRecords.addProcessTime(processTime);
            for (var task : instanceTaskCounter.getTaskCounterList()) {
                writer.println(task.taskName + " " + task.counter);
            }
            inst++;

            writer.println();
            HistoricActivityInstanceQuery activityQuery = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .finished()
                    .orderByHistoricActivityInstanceStartTime().asc()
                    .orderByHistoricActivityInstanceEndTime().asc()
                    .orderPartiallyByOccurrence().asc();
            List<HistoricActivityInstance> activityList = activityQuery.unlimitedList();
            pathCollection.addPath(activityList, instanceVariableValueRecords);



        }
//        ResultsData resultsData = ResultsSummary.getResults(inst, pathCollection, varCollection, taskCounter, variableValueRecords, assigneeList);
        return ResultsSummary.getResults(inst - 1, pathCollection, varCollection, taskCounter, variableValueRecords, assigneeList);
    }

}
