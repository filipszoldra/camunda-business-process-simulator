package simdata;

import java.util.Optional;

public class SimVariable {
    String name;
    Integer defaultValue;
    int id;
    public Boolean isInAllTask;
    public Boolean isInAllTaskTheSame;

    public SimVariable (String name, int ids){
        this.name = name;
        this.defaultValue = 0;
        this.isInAllTask = true;
        this.isInAllTaskTheSame = false;
        this.id = ids;

    }

    public SimVariable (String name, Integer defaultValue){
        this.name = name;
        this.defaultValue = defaultValue;
        this.isInAllTask = true;
        this.isInAllTaskTheSame = false;
    }

    public SimVariable (String name, Integer defaultValue, Boolean isInAllTask){
        this.name = name;
        this.defaultValue = defaultValue;
        this.isInAllTask = isInAllTask;
        this.isInAllTaskTheSame = false;
    }
    public SimVariable (String name, Integer defaultValue, Boolean isInAllTask, Boolean isInAllTaskTheSame){
        this.name = name;
        this.defaultValue = defaultValue;
        this.isInAllTask = isInAllTask;
        this.isInAllTaskTheSame = isInAllTaskTheSame;
    }

    public void setDefaultValue(Integer value){
        this.defaultValue = value;
    }
    public String getName(){
        return this.name;
    }
    public int getId() {return this.id;}
    public Integer getDefaultValue(){
        return this.defaultValue;
    }

}
