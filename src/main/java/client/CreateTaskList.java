package client;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Task;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import simdata.SimTask;
import simdata.TaskList;
import simdata.VariableCollection;

import java.util.Collection;
import java.util.Scanner;

public abstract class CreateTaskList {
    public static TaskList createTaskList (BpmnModelInstance modelInstance, VariableCollection varCollection) {
        Scanner reader = new Scanner(System.in);
        ModelElementType taskType = modelInstance.getModel().getType(Task.class);
        Collection<ModelElementInstance> taskInstances = modelInstance.getModelElementsByType(taskType);

        TaskList taskList = new TaskList();

        System.out.println("W jaki sposób chcesz wprowadzać wartości zmiennych?");
        System.out.println("1 - wartość wybierana dla każdej zmiennej wartość w każdym zadaniu");
        System.out.println("2 - wartość wybierana z losowego przedziału podanego dla każdej zmiennej w każdym zadaniu");
        System.out.println("3 - zawsze taka sama podana wartość");
        System.out.println("4 - zawsze taki sam podany losowy przedział");
        int varVals = reader.nextInt();
        while(varVals!=1 && varVals!=2 && varVals!=3 && varVals!=4){
            System.out.println("Wybierz opcję od 1 do 4");
            varVals = reader.nextInt();
        }
        int value = 0;
        int valMax = 0;
        int valMin = 0;
        if (varVals == 3){
            System.out.println("Podaj stałą wartość zmiennych");
            value = reader.nextInt();
        }
        if (varVals == 4){
            System.out.println("Podaj minimalną wartość zmiennych");
            valMin = reader.nextInt();
            System.out.println("Podaj maksymalną wartość zmiennych ");
            valMax = reader.nextInt();
        }
        for (var taskInstance : taskInstances
        ) {
            Task task = modelInstance.getModelElementById(taskInstance.getAttributeValue("id"));

            SimTask taskData = new SimTask(task);
            taskData.taskVarList.addDefaultVariables(varCollection);

            if (varVals == 1){
                for (var variableName : taskData.taskVarList.getAllVariableNames()) {
                    System.out.println("Podaj wartość zmiennej " + variableName + " w " + taskData.taskName);
                    value = reader.nextInt();
                    taskData.taskVarList.setValue(variableName, value);
                }
            }
            else if (varVals == 2) {
                for (var variableName : taskData.taskVarList.getAllVariableNames()) {
                    System.out.println("Podaj minimalną wartość zmiennej " + variableName + " w " + taskData.taskName);
                    valMin = reader.nextInt();
                    System.out.println("Podaj maksymalną wartość zmiennej " + variableName + " w " + taskData.taskName);
                    valMax = reader.nextInt();
                    taskData.taskVarList.setValue(variableName, valMin, valMax);
                }
            }
            else if (varVals == 3){
                for (var variableName : taskData.taskVarList.getAllVariableNames()) {
                    taskData.taskVarList.setValue(variableName, value);
                }
            }
            else {
                for (var variableName : taskData.taskVarList.getAllVariableNames()) {
                    taskData.taskVarList.setValue(variableName, valMin, valMax);
                }
            }
            taskList.addTask(taskData);

        }
    return taskList;
    }
}
