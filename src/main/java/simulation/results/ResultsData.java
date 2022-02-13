package simulation.results;

import simulation.TaskCountRecord;

import java.util.List;

public class ResultsData {
    public List<PathResult> pathResults;
    public List<EndEventRecord> endResults;
    public List<AsigneeResult> assigneeResults;
    public List<VariableResult> variableResults;
    public List<TaskCountRecord> taskResults;
    public ResultsData(List<PathResult> pathResults, List<EndEventRecord> endResults, List<AsigneeResult> assigneeResults, List<VariableResult> variableResults, List<TaskCountRecord> taskResults){
        this.pathResults = pathResults;
        this.endResults = endResults;
        this.taskResults = taskResults;
        this.assigneeResults = assigneeResults;
        this.variableResults = variableResults;
    }
}
