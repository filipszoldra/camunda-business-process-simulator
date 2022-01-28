package simulation;

import org.camunda.bpm.engine.history.HistoricActivityInstance;
import simdata.VariableCollection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PathCollection {
    List<PathRecord> pathList = new ArrayList<>();
    Integer allCounter = 0;
    VariableCollection varCollection;

    public PathCollection(VariableCollection variableCollection){
        this.varCollection = variableCollection;
    }


    public void addPath(List<HistoricActivityInstance> path, VariableValueRecords instanceVariableValueRecords){
        PathRecord newPath = new PathRecord(path);
        String newPathString = newPath.getPathString();
        for(var pathRecord : pathList){
            if(pathRecord.getPathString().equals(newPathString)){
                pathRecord.increment();
                pathRecord.addVarValueRecords(instanceVariableValueRecords.getVariableValueRecordList());
                allCounter++;
                return;
            }
        }
        allCounter++;
        newPath.createVarValueRecords(varCollection);
        newPath.addVarValueRecords(instanceVariableValueRecords.getVariableValueRecordList());
        pathList.add(newPath);
        return;
    }

    public List<PathRecord> getPathList() {
        for (var path : pathList){
            path.setProbability(allCounter);
        }
        return pathList;
    }

    public VariableValueRecords getAllMinVarValueRecords(){
        VariableValueRecords allMinVarValueRecords = new VariableValueRecords(varCollection);
        for (var varName : varCollection.getVariableNameList()){
            for(var pathRecord : pathList){
                if(allMinVarValueRecords.isStarted(varName) == false){
                    allMinVarValueRecords.setValue(varName, pathRecord.getMinVarValueRecords().getVarValue(varName));
                    allMinVarValueRecords.markStarted(varName);
                }
                else if (allMinVarValueRecords.getVarValue(varName) > pathRecord.getMinVarValueRecords().getVarValue(varName)){
                    allMinVarValueRecords.setValue(varName, pathRecord.getMinVarValueRecords().getVarValue(varName));
                }
            }
        }
        return allMinVarValueRecords;
    }

    public VariableValueRecords getAllMaxVarValueRecords(){
        VariableValueRecords allMaxVarValueRecords = new VariableValueRecords(varCollection);
        for (var varName : varCollection.getVariableNameList()){
            for(var pathRecord : pathList){
                if(allMaxVarValueRecords.isStarted(varName) == false){
                    allMaxVarValueRecords.setValue(varName, pathRecord.getMaxVarValueRecords().getVarValue(varName));
                    allMaxVarValueRecords.markStarted(varName);
                }
                else if (allMaxVarValueRecords.getVarValue(varName) < pathRecord.getMaxVarValueRecords().getVarValue(varName)){
                    allMaxVarValueRecords.setValue(varName, pathRecord.getMaxVarValueRecords().getVarValue(varName));
                }
            }
        }
        return allMaxVarValueRecords;
    }
}
