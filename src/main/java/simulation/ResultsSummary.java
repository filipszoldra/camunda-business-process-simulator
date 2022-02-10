package simulation;

import simdata.AssigneeList;
import simdata.VariableCollection;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class ResultsSummary {
    public static void results(int instNumber, PrintWriter writer, PathCollection pathCollection, VariableCollection varCollection, TaskCounter taskCounter, VariableValueRecords variableValueRecords, AssigneeList assigneeList){
        writer.println();
        writer.println();
        List<PathRecord> pathList = pathCollection.getPathList();
        pathList.sort(Comparator.comparingInt(PathRecord::getCounter).reversed());
        writer.println("Paths");
        writer.println();
        for (var path : pathList) {
            writer.println(path.getPathString());
            writer.println(path.getProbability() + " " + path.getCounter());
            writer.println();
            writer.println("Variables");
            VariableValueRecords varMinValueRecords = path.getMinVarValueRecords();
            VariableValueRecords varAverageValueRecords = path.getAverageVarValueRecords();
            VariableValueRecords varMaxValueRecords = path.getMaxVarValueRecords();
            for (var variable : varCollection.getVariableNameList()) {
                writer.println(variable + ": min " + varMinValueRecords.getVarValue(variable) + " average " + varAverageValueRecords.getVarValue(variable) + " max " + varMaxValueRecords.getVarValue(variable));
            }
            writer.println();
        }
        writer.println();
        writer.println();
        writer.println("Ends");
        for(var end : pathCollection.getEndList()){
            writer.println(end.name + " " + end.getProbability());
        }
        writer.println();
        writer.println();
        writer.println("Task counts");
        writer.println();
        for (var task : taskCounter.getTaskCounterList()) {
            writer.println(task.taskName + " " + task.counter);
        }
        writer.println();
        writer.println();
        if(assigneeList.getAssigneeList().size()>0) {
            writer.println("Assignees");
            writer.println();
            for (var assignee : assigneeList.getAssigneeList()) {
                writer.println(assignee.assignee);
                for (var variable : assignee.getVarValueRecords().getVariableValueRecordList()) {
                    writer.println(variable.variableName + " " + variable.value);
                }
                writer.println();
            }
        }

        writer.println("Variables");
        VariableValueRecords varAllMinValueRecords = pathCollection.getAllMinVarValueRecords();
        VariableValueRecords varAllMaxValueRecords = pathCollection.getAllMaxVarValueRecords();

        for (var variable : varCollection.getVariableNameList()) {
            int average = variableValueRecords.getVarValue(variable) / instNumber;
            ArrayList<Integer> medianList = new ArrayList();
            int medianCounter = 0;
            List<VariableCountRecord> varCounter = pathCollection.getVarCounter(variable);
            for(var varCount : varCounter){
                if(varCount.count>medianCounter){
                    medianList.clear();
                    medianList.add(varCount.value);
                    medianCounter = varCount.count;
                }
                else if(varCount.count == medianCounter)
                    medianList.add(varCount.value);
            }
            int median = 0;
            for(var val:medianList) {
                median += val;
            }
            median /= medianList.size();

            writer.println();
            writer.println();
            writer.println(variable);
            writer.println("sum:" + " " + variableValueRecords.getVarValue(variable));
            writer.println("min:" + " " + varAllMinValueRecords.getVarValue(variable));
            writer.println("median:" + " " + median);
            writer.println("average:" + " " + average);
            writer.println("max:" + " " + varAllMaxValueRecords.getVarValue(variable));
            writer.println();
            writer.println("Variable " + variable + " value distribution:");
            for(var varCount : varCounter){
                writer.println(varCount.value + " " + varCount.count);
            }
        }

    }
}
