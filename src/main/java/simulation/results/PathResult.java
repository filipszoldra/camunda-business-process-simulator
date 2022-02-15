package simulation.results;

import java.util.List;

public class PathResult {
    public String pathName;
    public String path;
    public List<String> elements;
    public int counter;
    public String probability;
    public List<PathVariableResult> variables;

    public PathResult(String path, int counter, String probability, List<PathVariableResult> variables, int id, List<String> elements) {
        this.path = path;
        this.counter = counter;
        this.probability = probability;
        this.variables = variables;
        this.elements = elements;
        if (id <= 10) {
            this.pathName = String.valueOf("Scieżka " + id);
        } else {
            this.pathName = "Pozostałe";
        }
    }
}
