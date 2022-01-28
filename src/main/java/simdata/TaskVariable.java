package simdata;

public class TaskVariable {
    String name;
    Integer value;
    Integer value2;


    public TaskVariable(SimVariable simVar){
        this.name = simVar.name;
        this.value = simVar.defaultValue;
        this.value2 = simVar.defaultValue;
    }
    public TaskVariable(SimVariable simVar, Integer value){
        this.name = simVar.name;
        this.value = value;
        this.value2 = value;
    }

    public TaskVariable(String name){
        this.name = name;
        this.value = 0;
        this.value2 = value;
    }


    public TaskVariable(String name, Integer value){
        this.name = name;
        this.value = value;
        this.value2 = value;
    }



    public void setValue(Integer value){
        this.value = value;
        this.value2 = value;
    }
    public void setValue(Integer value, Integer value2){
        this.value = value;
        this.value2 = value2;
    }
    public String getName(){
        return this.name;
    }
    public Integer getValue(){
        return GetRandomValue.GetRandomValue(value, value2);
    }
}


