package simulation;

import java.util.ArrayList;
import java.util.List;

public class PararellOrderRecord {
    public List<List<String>> paths = new ArrayList();
    public List<List<String>> pathsWithPararells = new ArrayList();
    public String startId;
    public List<Integer> containtPararells = new ArrayList<>();
    public int pararellId;

    public PararellOrderRecord(String startId, int pararellId) {
        this.startId = startId;
        this.pararellId = pararellId;
    }

    public void setPaths(List<List<String>> paths) {
        this.paths = paths;
    }

}
