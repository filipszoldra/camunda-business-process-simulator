package simulation;

import simdata.AssigneeList;
import simdata.VariableCollection;

import java.io.PrintWriter;
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
            writer.println();
            writer.println(variable);
            writer.println("sum:" + " " + variableValueRecords.getVarValue(variable));
            writer.println("min:" + " " + varAllMinValueRecords.getVarValue(variable));
            writer.println("average:" + " " + average);
            writer.println("max:" + " " + varAllMaxValueRecords.getVarValue(variable));
        }

    }
}
