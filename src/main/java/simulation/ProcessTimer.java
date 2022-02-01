package simulation;

import simdata.TaskList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProcessTimer {
    List<TaskTimeRecord> taskTimes = new ArrayList<>();
    TaskList taskList;
    int processTime = 0;
    PararellOrderList pararellList;

    public ProcessTimer(PararellOrderList pararellList, TaskList taskList) {
        this.pararellList = pararellList;
        this.taskList = taskList;
    }

    public void addTime(String taskId, int time) {
        for (var task : taskTimes) {
            if (task.taskId.equals(taskId))
                task.time += time;
        }
    }

    public void addTaskTimeRecord(String taskId, int time) {
        List<String> taskList = getTasksId();
        if (taskList.contains(taskId)) {
            addTime(taskId, time);
        } else {
            TaskTimeRecord newRecord = new TaskTimeRecord(taskId, time);
            taskTimes.add(newRecord);
        }
    }

    public List<String> getTasksId() {
        List<String> taskList = new ArrayList<>();
        for (var task : taskTimes) {
            taskList.add(task.taskId);
        }
        return taskList;
    }

    public int getProcessTime() {
        return processTime;
    }


    public void setProcessTime() {
        List<String> taskIdList = getTasksId();
        List<String> idList = taskList.getTaskIdList();
        for (var task : idList) {
            addTaskTimeRecord(task, 0);
        }
        List<Integer> pararellsId = new ArrayList<>();
        for (int i = 1; i <= pararellList.ids; i++) {
            List<List<String>> paths = pararellList.getPaths(i);
            boolean contain = true;
            for (var path : paths) {
                for (var id : path) {
                    if (!idList.contains(id)) {
                        contain = false;
                        break;
                    }

                }
                if (contain == false)
                    break;
            }
            if (contain) {
                for (var path : paths) {
                    idList.removeAll(path);
                }
                pararellsId.add(i);
            }
        }
        List<Integer> mainPararells = new ArrayList<>();
        mainPararells.addAll(pararellsId);
        List<Integer> neededPararells = new ArrayList<>();
        neededPararells.addAll(pararellsId);
        int size = pararellsId.size();
        while (size > 0) {
            PararellOrderRecord pararell = pararellList.getPararellRecordById(pararellsId.get(0));
            pararellsId.remove(0);
            pararellsId.addAll(pararell.containtPararells);
            for (var idd : pararell.containtPararells) {
                if (!neededPararells.contains(idd))
                    neededPararells.add(idd);
            }
            size = pararellsId.size();
        }
        List<PararellTimeRecord> pararellTimes = new ArrayList<>();
        Collections.sort(neededPararells);
        Collections.reverse(neededPararells);
        for (var id : neededPararells) {
            List<String> pararellStrings = new ArrayList<>();
            for (var record : pararellTimes) {
                pararellStrings.add(record.pararellId);
            }
            PararellOrderRecord pararell = pararellList.getPararellRecordById(id);
            int maxTime = 0;
            List<List<String>> paths = pararell.pathsWithPararells;
            for (var path : paths) {
                int pathTime = 0;
                for (var name : path) {
                    if (pararellStrings.contains(name)) {
                        for (var record : pararellTimes) {
                            if (record.pararellId.equals(name))
                                pathTime += record.time;
                        }
                    }
                    for (var task : taskTimes) {
                        if (task.taskId.equals(name))
                            pathTime += task.time;
                    }
                }
                if (pathTime > maxTime)
                    maxTime = pathTime;
            }
            PararellTimeRecord newRecord = new PararellTimeRecord(id, maxTime);
            pararellTimes.add(newRecord);

        }
        for (var task : idList) {
            for (var tasktime : taskTimes) {
                if (tasktime.taskId.equals(task))
                    processTime += tasktime.time;
            }
        }
        for (var pararell : mainPararells) {
            for (var pararellTime : pararellTimes) {
                if (pararellTime.id == pararell)
                    processTime += pararellTime.time;
            }
        }

    }


}
