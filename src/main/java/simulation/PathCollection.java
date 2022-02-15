package simulation;

import org.camunda.bpm.engine.history.HistoricActivityInstance;
import simdata.VariableCollection;
import simulation.results.EndEventRecord;

import java.util.ArrayList;
import java.util.List;

public class PathCollection {
    List<PathRecord> pathList = new ArrayList<>();
    List<EndEventRecord> endList = new ArrayList<>();
    List<VariableCountRecord> varCounter = new ArrayList<>();
    Integer allCounter = 0;
    VariableCollection varCollection;
    public PathCollection(VariableCollection variableCollection) {
        this.varCollection = variableCollection;
    }

    public Integer getAllCounter() {
        return allCounter;
    }

    public void addPath(List<HistoricActivityInstance> path, VariableValueRecords instanceVariableValueRecords) {
        PathRecord newPath = new PathRecord(path);
        String newPathString = newPath.getPathString();
        allCounter++;
        for (var variable : instanceVariableValueRecords.getVariableValueRecordList()) {
            addVarCount(variable.variableName, variable.value);
        }
        addEnd(newPath.getEndName());
        for (var variable : instanceVariableValueRecords.getVariableValueRecordList()) {

        }
        for (var pathRecord : pathList) {
            if (pathRecord.getPathString().equals(newPathString)) {
                pathRecord.increment();
                pathRecord.addVarValueRecords(instanceVariableValueRecords.getVariableValueRecordList());
                return;
            }
        }
        newPath.createVarValueRecords(varCollection);
        newPath.addVarValueRecords(instanceVariableValueRecords.getVariableValueRecordList());
        pathList.add(newPath);
        return;
    }

    public void addEnd(String name) {
        for (var end : endList) {
            if (end.name.equals(name)) {
                end.counter++;
                return;
            }
        }
        EndEventRecord endRecord = new EndEventRecord(name);
        endList.add(endRecord);
        return;
    }

    public List<PathRecord> getPathList() {
        for (var path : pathList) {
            path.setProbability(allCounter);
        }
        return pathList;
    }

    public VariableValueRecords getAllMinVarValueRecords() {
        VariableValueRecords allMinVarValueRecords = new VariableValueRecords(varCollection);
        for (var varName : varCollection.getVariableNameList()) {
            for (var pathRecord : pathList) {
                if (allMinVarValueRecords.isStarted(varName) == false) {
                    allMinVarValueRecords.setValue(varName, pathRecord.getMinVarValueRecords().getVarValue(varName));
                    allMinVarValueRecords.markStarted(varName);
                } else if (allMinVarValueRecords.getVarValue(varName) > pathRecord.getMinVarValueRecords().getVarValue(varName)) {
                    allMinVarValueRecords.setValue(varName, pathRecord.getMinVarValueRecords().getVarValue(varName));
                }
            }
        }
        return allMinVarValueRecords;
    }

    public VariableValueRecords getAllMaxVarValueRecords() {
        VariableValueRecords allMaxVarValueRecords = new VariableValueRecords(varCollection);
        for (var varName : varCollection.getVariableNameList()) {
            for (var pathRecord : pathList) {
                if (allMaxVarValueRecords.isStarted(varName) == false) {
                    allMaxVarValueRecords.setValue(varName, pathRecord.getMaxVarValueRecords().getVarValue(varName));
                    allMaxVarValueRecords.markStarted(varName);
                } else if (allMaxVarValueRecords.getVarValue(varName) < pathRecord.getMaxVarValueRecords().getVarValue(varName)) {
                    allMaxVarValueRecords.setValue(varName, pathRecord.getMaxVarValueRecords().getVarValue(varName));
                }
            }
        }
        return allMaxVarValueRecords;
    }

    public List<EndEventRecord> getEndList() {
        for (var end : endList) {
            end.probability = end.counter / allCounter;
        }
        return endList;
    }

    public void addVarCount(String name, int value) {
        for (var varRecord : varCounter) {
            if (varRecord.name.equals(name) && varRecord.value == value) {
                varRecord.count++;
                return;
            }
        }
        VariableCountRecord varRecord = new VariableCountRecord(name, value);
        varCounter.add(varRecord);
        return;
    }

    public List<VariableCountRecord> getVarCounter(String selectedVar) {
        List<VariableCountRecord> selectedVarList = new ArrayList<>();
        int min = getAllMinVarValueRecords().getVarValue(selectedVar);
        int max = getAllMaxVarValueRecords().getVarValue(selectedVar);
        for (int i = min; i <= max; i++) {
            boolean contain = false;
            for (var varCount : varCounter) {
                if (varCount.name.equals(selectedVar) && varCount.value == i) {
                    contain = true;
                    selectedVarList.add(varCount);
                }
            }
            if (!contain)
                selectedVarList.add(new VariableCountRecord(selectedVar, i, 0));
        }
        return selectedVarList;
    }
}
