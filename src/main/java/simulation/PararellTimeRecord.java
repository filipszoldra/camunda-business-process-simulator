package simulation;

public class PararellTimeRecord {
    public int id;
    public int time;
    public String pararellId;
    public PararellTimeRecord(int id, int time){
        this.id = id;
        this.time = time;
        this.pararellId = ("pararell"+id);
    }
}
