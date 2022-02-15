package client;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.ExclusiveGateway;
import org.camunda.bpm.model.bpmn.instance.Gateway;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import simdata.AssigneeList;
import simdata.AssigneeRecord;
import simdata.TaskVariableList;
import simdata.VariableCollection;

import java.util.*;

public class InputData {
    public VariableCollection variableCollection = new VariableCollection();
    AssigneeList assignees;
    List<InputTaskVar> taskVars = new ArrayList<>();
    List<ExclusiveGateConditionalsInput> gateConds = new ArrayList<>();
    List<String> conditionalVarNames = new ArrayList<>();

    public InputData(BpmnModelInstance modelInstance) {

        ModelElementType taskType = modelInstance.getModel().getType(UserTask.class);
        Collection<ModelElementInstance> taskInstances = modelInstance.getModelElementsByType(taskType);
        for (var task : taskInstances) {
            taskVars.add(new InputTaskVar(task.getAttributeValue("id"), task.getAttributeValue("name")));
        }
        ModelElementType exGateType = modelInstance.getModel().getType(ExclusiveGateway.class);
        Collection<ModelElementInstance> exclusiveGatewayInstances = modelInstance.getModelElementsByType(exGateType);
        for (var gate : exclusiveGatewayInstances) {
            Gateway gateway = modelInstance.getModelElementById(gate.getAttributeValue("id"));
            Collection<SequenceFlow> flows = gateway.getOutgoing();
            if (flows.size() == 2) {
                Iterator<SequenceFlow> flowIterator = flows.iterator();
                SequenceFlow firstFlow = flowIterator.next();
                SequenceFlow secondFlow = flowIterator.next();

                String firstFlowName = firstFlow.getName();
                if (firstFlowName == null) {
                    firstFlowName = firstFlow.getId();
                }
                String secondFlowName = secondFlow.getName();
                if (secondFlowName == null) {
                    secondFlowName = secondFlow.getId();
                }
                gateConds.add(new ExclusiveGateConditionalsInput(gate.getAttributeValue("id"), gate.getAttributeValue("name"), firstFlowName, secondFlowName));
            }
        }

    }

    public void collectDataFromTerminal() {
        variableCollection = createVariablesFromTerminal();
        assignees = createAssigneeListFromTerminal();
        setTaskVarsFromTerminal();
        setGateConds();
    }

    public VariableCollection getVariableCollection() {
        return variableCollection;
    }

    public AssigneeList getAssigneeList() {
        return assignees;
    }

    public void addConditionalVar(String condVar) {
        if (!conditionalVarNames.contains(condVar)) {
            conditionalVarNames.add(condVar);
        }

    }

    public List<InputTaskVar> getTaskInput() {
        return taskVars;
    }

    public List<ExclusiveGateConditionalsInput> getGateConds() {
        return gateConds;
    }

    public List<String> getConditionalVarNames() {
        return conditionalVarNames;
    }


    public void addVarList(List<String> varList) {
        variableCollection = new VariableCollection();
        for (var varName : varList) {
            variableCollection.createSimVariable(varName);
        }
        for (var task : taskVars) {
            task.taskVarList = new TaskVariableList();
            task.taskVarList.addDefaultVariables(variableCollection);
        }

    }

    public void setAllTaskVarListNames() {
        for (var task : taskVars)
            task.setTaskVarListNames();
    }

    public void addAssignees(List<String> assigneeNameList) {
        assignees = new AssigneeList();
        for (var assignee : assigneeNameList) {
            assignees.addAssignne(assignee);
        }
    }

    public List<String> getAssigneeNames() {
        List<String> assigneeNames = new ArrayList<>();
        for (var assignee : assignees.getAssigneeList()) {
            assigneeNames.add(assignee.assignee);
        }
        return assigneeNames;
    }


    VariableCollection createVariablesFromTerminal() {
        VariableCollection varCollection = new VariableCollection();
        varCollection.createSimVariable("time");
        System.out.println("Ile zmiennych oprócz czasu ma uwzględniać symulacja?");
        Scanner reader = new Scanner(System.in);
        int vars = reader.nextInt();
        for (int i = 1; i <= vars; i++) {
            System.out.println("Podaj nazwę zmiennej " + i);
            String varName = reader.next();
            varCollection.createSimVariable(varName);
        }
        return varCollection;
    }

    AssigneeList createAssigneeListFromTerminal() {
        Scanner reader = new Scanner(System.in);
        AssigneeList assigneeList = new AssigneeList();
        System.out.println("Ilu pracowników ma być przydzielonych do zadań w procesie?");
        int assignees = reader.nextInt();
        for (int i = 1; i <= assignees; i++) {
            System.out.println("Podaj nazwę pracownika " + i);
            String assigneeName = reader.next();
            assigneeList.addAssignne(assigneeName);
        }
        return assigneeList;
    }


    void setTaskVarsFromTerminal() {
        Scanner reader = new Scanner(System.in);

        int varVals;
        int value = 0;
        int valMax = 0;
        int valMin = 0;
        if (variableCollection.getVariableNameList().size() == 0) {
            varVals = 0;
        } else {
            System.out.println("W jaki sposób chcesz wprowadzać wartości zmiennych?");
            System.out.println("1 - wartość wybierana dla każdej zmiennej wartość w każdym zadaniu");
            System.out.println("2 - wartość wybierana z losowego przedziału podanego dla każdej zmiennej w każdym zadaniu");
            System.out.println("3 - zawsze taka sama podana wartość");
            System.out.println("4 - zawsze taki sam podany losowy przedział");
            varVals = reader.nextInt();
            while (varVals != 1 && varVals != 2 && varVals != 3 && varVals != 4) {
                System.out.println("Wybierz opcję od 1 do 4");
                varVals = reader.nextInt();
            }
            value = 0;
            valMax = 0;
            valMin = 0;
            if (varVals == 3) {
                System.out.println("Podaj stałą wartość zmiennych");
                value = reader.nextInt();
            }
            if (varVals == 4) {
                System.out.println("Podaj minimalną wartość zmiennych");
                valMin = reader.nextInt();
                System.out.println("Podaj maksymalną wartość zmiennych ");
                valMax = reader.nextInt();
            }
        }

        for (var task : taskVars) {

            if (assignees.getAssigneeList().size() > 1) {
                List<AssigneeRecord> assigneeRecords = assignees.getAssigneeList();
                System.out.println("Którego pracownika chcesz przydzielić do zadania " + task.taskName + "? Podaj id.");
                for (var assignee : assigneeRecords) {
                    System.out.println(assignee.id + " " + assignee.assignee);
                }
                int id = reader.nextInt();
                task.assigneeName = assignees.getAssigneeNameById(id);
            } else if (assignees.getAssigneeList().size() == 1) {
                task.assigneeName = assignees.getAssigneeNameById(1);
            }


            task.taskVarList.addDefaultVariables(variableCollection);

            if (varVals == 1) {
                for (var variableName : task.taskVarList.getAllVariableNames()) {
                    System.out.println("Podaj wartość zmiennej " + variableName + " w " + task.taskName);
                    value = reader.nextInt();
                    task.taskVarList.setValue(variableName, value);
                }
            } else if (varVals == 2) {
                for (var variableName : task.taskVarList.getAllVariableNames()) {
                    System.out.println("Podaj minimalną wartość zmiennej " + variableName + " w " + task.taskName);
                    valMin = reader.nextInt();
                    System.out.println("Podaj maksymalną wartość zmiennej " + variableName + " w " + task.taskName);
                    valMax = reader.nextInt();
                    task.taskVarList.setValue(variableName, valMin, valMax);
                }
            } else if (varVals == 3) {
                for (var variableName : task.taskVarList.getAllVariableNames()) {
                    task.taskVarList.setValue(variableName, value);
                }
            } else if (varVals == 4) {
                for (var variableName : task.taskVarList.getAllVariableNames()) {
                    task.taskVarList.setValue(variableName, valMin, valMax);
                }
            }

        }
    }

    void setGateConds() {
        Scanner reader = new Scanner(System.in);
        for (var gate : gateConds
        ) {
            System.out.println("Jaki ma być typ warunku bramki " + gate.gateName + "?");
            System.out.println("0 - prawdopodobieństwo losowe");
            System.out.println("1 - warunkowość oparta na zmiennej");
            int varcond = reader.nextInt();
            if (varcond == 0) {
                if (!conditionalVarNames.contains("rand"))
                    conditionalVarNames.add("rand");
                System.out.println("Podaj procentowe prawdopodobienstwo opcji " + gate.flow1Name + " po bramce " + gate.gateName);
                int n = reader.nextInt();
                gate.value1 = n;
                gate.value2 = n;
                gate.sign1 = "<=";
                gate.sign2 = ">";
                gate.condVarName = "rand";
            } else {
                System.out.println("Podaj Id zmiennej warunkowej dla bramki " + gate.gateName);
                for (var variable : variableCollection.getVariableList()) {
                    System.out.println(variable.getId() + " " + variable.getName());
                }
                int varId = reader.nextInt();
                String varCondName = variableCollection.getVarNameById(varId);
                if (!conditionalVarNames.contains(varCondName))
                    conditionalVarNames.add(varCondName);
                System.out.println("Wybierz operator dla opcji " + gate.flow1Name);
                System.out.println("1 >");
                System.out.println("2 ==");
                System.out.println("3 <");
                int op = reader.nextInt();
                System.out.println("Podaj wartość po operatorze");
                int varCondVal = reader.nextInt();
                gate.value1 = varCondVal;
                gate.value2 = varCondVal;
                gate.condVarName = varCondName;
                if (op == 1) {
                    gate.sign1 = ">";
                    gate.sign2 = "<=";
                } else if (op == 2) {
                    gate.sign1 = "==";
                    gate.sign2 = "!=";
                } else {
                    gate.sign1 = "<";
                    gate.sign2 = ">=";
                }

            }
        }

    }


}



