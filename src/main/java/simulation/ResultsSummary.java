package simulation;

import simdata.AssigneeList;
import simdata.VariableCollection;
import simulation.results.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class ResultsSummary {
    public static void results(int instNumber, PathCollection pathCollection, VariableCollection varCollection, TaskCounter taskCounter, VariableValueRecords variableValueRecords, AssigneeList assigneeList) throws IOException {
        PrintWriter writer = new PrintWriter("results.txt", StandardCharsets.UTF_8);
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
        for (var end : pathCollection.getEndList()) {
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
        if (assigneeList.getAssigneeList().size() > 0) {
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
            for (var varCount : varCounter) {
                if (varCount.count > medianCounter) {
                    medianList.clear();
                    medianList.add(varCount.value);
                    medianCounter = varCount.count;
                } else if (varCount.count == medianCounter)
                    medianList.add(varCount.value);
            }
            int median = 0;
            for (var val : medianList) {
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
            for (var varCount : varCounter) {
                writer.println(varCount.value + " " + varCount.count);
            }
        }
        writer.close();

    }

    public static ResultsData getResults(int instNumber, PathCollection pathCollection, VariableCollection varCollection, TaskCounter taskCounter, VariableValueRecords variableValueRecords, AssigneeList assigneeList) {
        List<PathResult> pathResults = new ArrayList<>();
        List<EndEventRecord> endResults = new ArrayList<>();
        List<AsigneeResult> assigneeResults = new ArrayList<>();
        List<VariableResult> variableResults = new ArrayList<>();
        List<TaskCountRecord> taskResults = new ArrayList<>();
        List<PathRecord> pathList = pathCollection.getPathList();
        pathList.sort(Comparator.comparingInt(PathRecord::getCounter).reversed());
        int pathId = 1;
        VariableValueRecords otherVarMinValueRecords = null;
        VariableValueRecords otherVarAverageValueRecords = null;
        VariableValueRecords otherVarMaxValueRecords = null;
        int otherCounter = 0;
        for (var path : pathList) {
            if (pathId <= 10) {
                VariableValueRecords varMinValueRecords = path.getMinVarValueRecords();
                VariableValueRecords varAverageValueRecords = path.getAverageVarValueRecords();
                VariableValueRecords varMaxValueRecords = path.getMaxVarValueRecords();
                List<PathVariableResult> pathVars = new ArrayList<>();
                for (var variable : varCollection.getVariableNameList()) {
                    pathVars.add(new PathVariableResult(variable, varMinValueRecords.getVarValue(variable), varAverageValueRecords.getVarValue(variable), varMaxValueRecords.getVarValue(variable)));
                }
                pathResults.add(new PathResult(path.getPathString(), path.getCounter(), path.getProbability(), pathVars, pathId, path.getElements()));
                pathId++;
            } else if (pathId == 11) {
                otherVarMinValueRecords = path.getMinVarValueRecords();
                otherVarAverageValueRecords = path.getAverageVarValueRecords();
                otherVarMaxValueRecords = path.getMaxVarValueRecords();
                otherCounter += path.getCounter();
                pathId++;
            } else {
                VariableValueRecords varMinValueRecords = path.getMinVarValueRecords();
                VariableValueRecords varAverageValueRecords = path.getAverageVarValueRecords();
                VariableValueRecords varMaxValueRecords = path.getMaxVarValueRecords();
                otherCounter += path.getCounter();
                for (var variable : varCollection.getVariableNameList()) {
                    if (varMinValueRecords.getVarValue(variable) < otherVarMinValueRecords.getVarValue(variable))
                        otherVarMinValueRecords.setValue(variable, varMinValueRecords.getVarValue(variable));
                    if (varMaxValueRecords.getVarValue(variable) > otherVarMaxValueRecords.getVarValue(variable))
                        otherVarMaxValueRecords.setValue(variable, varMaxValueRecords.getVarValue(variable));
                    otherVarAverageValueRecords.setValue(variable, varAverageValueRecords.getVarValue(variable) + otherVarAverageValueRecords.getVarValue(variable));

                }
                pathId++;
            }
        }
        if (pathId > 11) {
            for (var variable : otherVarAverageValueRecords.getVariableValueRecordList()) {
                variable.value /= (pathId - 11);
            }
            float otherProb = otherCounter * 100 / instNumber;
            List<PathVariableResult> otherPathVars = new ArrayList<>();
            for (var variable : varCollection.getVariableNameList()) {
                otherPathVars.add(new PathVariableResult(variable, otherVarMinValueRecords.getVarValue(variable), otherVarAverageValueRecords.getVarValue(variable), otherVarMaxValueRecords.getVarValue(variable)));
            }
            List<String> other = new ArrayList<>();
            pathResults.add(new PathResult("Pozostałe ścieżki", otherCounter, String.valueOf(otherProb).concat("%"), otherPathVars, 11, other));
        }

        endResults.addAll(pathCollection.getEndList());
        taskResults.addAll(taskCounter.getTaskCounterList());
        if (assigneeList.getAssigneeList().size() > 0) {
            for (var assignee : assigneeList.getAssigneeList()) {
                assigneeResults.add(new AsigneeResult(assignee.assignee, assignee.getVarValueRecords().getVarValue("time"), (assignee.getVarValueRecords().getVarValue("time") / instNumber)));
            }
        }
        VariableValueRecords varAllMinValueRecords = pathCollection.getAllMinVarValueRecords();
        VariableValueRecords varAllMaxValueRecords = pathCollection.getAllMaxVarValueRecords();
        for (var variable : varCollection.getVariableNameList()) {
            int average = variableValueRecords.getVarValue(variable) / instNumber;
            ArrayList<Integer> medianList = new ArrayList();
            int medianCounter = 0;
            List<VariableCountRecord> varCounter = pathCollection.getVarCounter(variable);
            for (var varCount : varCounter) {
                if (varCount.count > medianCounter) {
                    medianList.clear();
                    medianList.add(varCount.value);
                    medianCounter = varCount.count;
                } else if (varCount.count == medianCounter)
                    medianList.add(varCount.value);
            }
            int median = 0;
            for (var val : medianList) {
                median += val;
            }
            median /= medianList.size();
            variableResults.add(new VariableResult(variable, variableValueRecords.getVarValue(variable), varAllMinValueRecords.getVarValue(variable), median, average, varAllMaxValueRecords.getVarValue(variable), varCounter));
        }
        return new ResultsData(pathResults, endResults, assigneeResults, variableResults, taskResults, instNumber);
    }
}
