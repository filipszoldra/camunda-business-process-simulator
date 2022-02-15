package simulation.results;

public class PathVariableResult {
    public String name;
    public int min;
    public int average;
    public int max;

    public PathVariableResult(String name, int min, int average, int max) {
        this.name = name;
        this.min = min;
        this.average = average;
        this.max = max;
    }
}
