package simulation.results;

import org.camunda.bpm.engine.history.HistoricActivityInstance;

import java.util.List;

public class EndEventRecord {
    public String name;
    public float counter;
    public float probability;

    public EndEventRecord(String name){
        this.name = name;
        this.counter = 1;
    }

    public String getProbability() {
        return String.valueOf(probability*100).concat("%");
    }
}
