package simulation;

public class TaskCountRecord {
    public String taskId;
    public String taskName;
    public Integer counter;

    public TaskCountRecord(String taskId, String taskName) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.counter = 0;
    }

    public TaskCountRecord(String taskId, Integer counter) {
        this.taskId = taskId;
        this.counter = counter;
    }


}
