package simulation;

import java.util.ArrayList;
import java.util.List;

public class PararellOrderList {
    List<PararellOrderRecord> pararellOrderRecords = new ArrayList<>();
    public int ids = 0;
    public List<String> taskIdList;

    public void addTaskList(List<String> taskIdList){
        this.taskIdList = taskIdList;
    }

    public void addPararellRecord(String start){
        ids++;
        PararellOrderRecord newRecord = new PararellOrderRecord(start, ids);
        pararellOrderRecords.add(newRecord);
    }

    public PararellOrderRecord getPararellRecordById(int id){
        for(var pararell: pararellOrderRecords){
            if(pararell.pararellId == id){
                return pararell;
            }
        }
        return null;
    }

    public String getStardIdById(int id){
        for(var pararell: pararellOrderRecords){
            if(pararell.pararellId == id){
                return pararell.startId;
            }
        }
        return null;
    }
    public void addPaths(int id, List<List<String>> paths){
        for(var pararell: pararellOrderRecords){
            if(pararell.pararellId == id){
                pararell.paths = paths;
            }
        }
    }

    public List<List<String>> getPaths(int id){
        for(var pararell: pararellOrderRecords){
            if(pararell.pararellId == id){
                return pararell.paths;
            }
        }
        return null;
    }

    public List<List<String>> getPathsWithPararell(int id){
        for(var pararell: pararellOrderRecords){
            if(pararell.pararellId == id){
                return pararell.pathsWithPararells;
            }
        }
        return null;
    }

    public void setPathsWithPararells(int id, List<List<String>> pathsWithPararells){
        for(var pararell : pararellOrderRecords){
            if(pararell.pararellId == id){
                pararell.pathsWithPararells = pathsWithPararells;
            }
        }
    }




    public void setPararellValues(){
        setPathsWithPararells(ids, getPaths(ids));
        for(int i = ids-1; i>=1; i--){
            List<List<String>> pararellsWithPararells = new ArrayList<>();
            List<Integer> contains = new ArrayList<>();
            for (int j = ids; j>i ; j--){
                List<List<String>> paths = getPaths(i);
                List<List<String>> pathWithPararells = new ArrayList<>();
                List<Integer> containsId = new ArrayList<>();
                List<List<String>> higherPaths = getPaths(j);
                for(var listi : paths){
                    boolean inPath = true;
                    for(var listj : higherPaths){
                        for(var idj : listj){
                            if(!listi.contains(idj)){
                                inPath = false;
                            }
                        }
                    }
                    if(inPath){
                        List<String> pathVal = new ArrayList<>();
                        for(var id : listi){
                            pathVal.add(id);
                        }
                        for(var listj : higherPaths){
                            pathVal.removeAll(listj);
                        }
                        pathVal.add("pararell" + j);
                        containsId.add(j);
                        pathWithPararells.add(pathVal);
                    }
                    else
                        pathWithPararells.add(listi);
                }
                contains = containsId;
                pararellsWithPararells = pathWithPararells;
            }
            getPararellRecordById(i).containtPararells = contains;
            setPathsWithPararells(i, pararellsWithPararells);
        }
    }
}

