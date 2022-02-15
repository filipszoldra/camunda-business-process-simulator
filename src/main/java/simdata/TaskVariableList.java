package simdata;

import java.util.ArrayList;
import java.util.List;

public class TaskVariableList {
    List<TaskVariable> taskVarList = new ArrayList<>();

    public void addSimVariable(TaskVariable taskVar) {
        taskVarList.add(taskVar);
    }

    public List<String> getAllVariableNames() {
        List<String> namesList = new ArrayList<>();
        for (var variable : taskVarList) {
            namesList.add(variable.getName());
        }
        return namesList;
    }

    public Integer countVariables() {
        return taskVarList.size();
    }

    private Integer findVariableIndex(String name) {
        for (var variable : taskVarList) {
            if (variable.getName() == name) {
                return taskVarList.indexOf(variable);
            }
        }
        return null;
    }

    public void addDefaultVariables(VariableCollection varCollection) {
        for (var variable : varCollection.variablesList) {
            taskVarList.add(new TaskVariable(variable));
        }
    }

    public TaskVariable getTaskVariable(String name) {
        for (var variable : taskVarList) {
            if (variable.getName() == name) {
                return variable;
            }
        }
        return null;
    }


    public Integer getValue(String name) {
        return taskVarList.get(findVariableIndex(name)).getValue();
    }

    public void setValue(String name, Integer value) {
        taskVarList.get(findVariableIndex(name)).setValue(value);
    }

    public void setValue(String name, Integer value, Integer value2) {
        taskVarList.get(findVariableIndex(name)).setValue(value, value2);
    }

    public List<TaskVariable> getTaskVarList() {
        return taskVarList;
    }
}
