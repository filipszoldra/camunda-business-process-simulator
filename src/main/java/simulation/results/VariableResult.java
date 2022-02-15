package simulation.results;

import simulation.VariableCountRecord;

import java.util.List;

public class VariableResult {
    public String name;
    public int sum;
    public int min;
    public int median;
    public int average;
    public int max;
    public List<VariableCountRecord> distribution;

    public VariableResult(String name, int sum, int min, int median, int average, int max, List<VariableCountRecord> distribution) {
        this.name = name;
        this.sum = sum;
        this.min = min;
        this.median = median;
        this.average = average;
        this.max = max;
        this.distribution = distribution;
    }
}
