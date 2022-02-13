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
        return String.valueOf(round(probability * 100, 2)).concat("%");
    }

    public static float round(float f, int places) {
        float temp = (float)(f*(Math.pow(10, places)));
        temp = (Math.round(temp));
        temp = temp/(int)(Math.pow(10, places));
        return temp;
    }
}
