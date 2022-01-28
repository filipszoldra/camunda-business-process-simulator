package modeleditor;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.ConditionExpression;
import org.camunda.bpm.model.bpmn.instance.ExclusiveGateway;
import org.camunda.bpm.model.bpmn.instance.Gateway;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import simdata.TaskList;
import simdata.VariableCollection;

import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;

public abstract class SetExclusiveGatewayConditions {
    public static void setExclusiveGatewayConditions (BpmnModelInstance modelInstance, TaskList taskList, VariableCollection varCollection){
        Scanner reader = new Scanner(System.in);
        ModelElementType gatewayType = modelInstance.getModel().getType(ExclusiveGateway.class);
        Collection<ModelElementInstance> gatewayInstances = modelInstance.getModelElementsByType(gatewayType);
        for (var gatewayInstance : gatewayInstances
        ) {
            Gateway gate = modelInstance.getModelElementById(gatewayInstance.getAttributeValue("id"));
            Collection<SequenceFlow> flows = gate.getOutgoing();
            if (flows.size() == 2) {
                System.out.println(gate.getId());

                System.out.println();
                Iterator<SequenceFlow> flowIterator = flows.iterator();
                SequenceFlow firstFlow = flowIterator.next();
                SequenceFlow secondFlow = flowIterator.next();

                String gateName = gate.getName();
                if(gateName == null){
                    gateName = gate.getId();
                }
                String firstFlowName = firstFlow.getName();
                if(firstFlowName == null){
                    firstFlowName = firstFlow.getId();
                }
                String secondFlowName = secondFlow.getName();
                if(secondFlowName == null){
                    secondFlowName = secondFlow.getId();
                }
                System.out.println("Jaki ma być typ warunku bramki " + gateName + "?");
                System.out.println("0 - prawdopodobieństwo losowe");
                System.out.println("1 - warunkowość oparta na zmiennej");
                int varcond = reader.nextInt();
                if (varcond == 0) {
                    AddRandomValues.AddRandomValues(modelInstance);
                    taskList.addConditionalVar("rand");
                    System.out.println("Podaj procentowe prawdopodobienstwo opcji " + firstFlowName + " po bramce " + gateName);
                    int n = reader.nextInt();
                    if (firstFlow.getConditionExpression() == null) {
                        ConditionExpression conditionExpression = modelInstance.newInstance(ConditionExpression.class);
                        conditionExpression.setTextContent(String.format("${randomValue <= %s}", n));
                        firstFlow.setConditionExpression(conditionExpression);
                    } else {
                        firstFlow.getConditionExpression().setTextContent(String.format("${randomValue <= %s}", n));
                    }
                    if (secondFlow.getConditionExpression() == null) {
                        ConditionExpression conditionExpression = modelInstance.newInstance(ConditionExpression.class);
                        conditionExpression.setTextContent(String.format("${randomValue > %s}", n));
                        secondFlow.setConditionExpression(conditionExpression);
                    } else {
                        secondFlow.getConditionExpression().setTextContent(String.format("${randomValue > %s}", n));
                    }
                    System.out.println();
                    System.out.println("Szansa na " + firstFlowName + " to " + n + "%");
                    System.out.println(firstFlowName + " " + firstFlow.getConditionExpression().getTextContent());
                    System.out.println();
                    System.out.println("Szansa na " + secondFlowName + " to " + (100 - n) + "%");
                    System.out.println(secondFlowName + " " + secondFlow.getConditionExpression().getTextContent());
                    System.out.println();
                }
                else{
                    System.out.println("Podaj Id zmiennej warunkowej dla bramki " + gateName);
                    for( var variable : varCollection.getVariableList()){
                        System.out.println(variable.getId() + " " + variable.getName());
                    }
                    int varId = reader.nextInt();
                    String varCondName = varCollection.getVarNameById(varId);
                    System.out.println("Wybierz operator dla opcji " + firstFlowName);
                    System.out.println("1 >");
                    System.out.println("2 ==");
                    System.out.println("3 <");
                    int op = reader.nextInt();
                    System.out.println("Podaj wartość po operatorze");
                    String varCondVal = reader.next();
                    AddConditionalValue.AddConditionalValue(modelInstance, varCondName);
                    taskList.addConditionalVar(varCondName);
                    String cond1;
                    String cond2;
                    if (op == 1){
                        cond1 = ("${" + varCondName + " > " + varCondVal + "}");
                        cond2 = ("${" + varCondName + " <= " + varCondVal + "}");
                    }
                    else if (op == 2){
                        cond1 = ("${" + varCondName + " == " + varCondVal + "}");
                        cond2 = ("${" + varCondName + " != " + varCondVal + "}");
                    }
                    else{
                        cond1 = ("${" + varCondName + " < " + varCondVal + "}");
                        cond2 = ("${" + varCondName + " >= " + varCondVal + "}");
                    }
                    if (firstFlow.getConditionExpression() == null) {
                        ConditionExpression conditionExpression = modelInstance.newInstance(ConditionExpression.class);
                        conditionExpression.setTextContent(cond1);
                        firstFlow.setConditionExpression(conditionExpression);
                    } else {
                        firstFlow.getConditionExpression().setTextContent(cond1);
                    }
                    if (secondFlow.getConditionExpression() == null) {
                        ConditionExpression conditionExpression = modelInstance.newInstance(ConditionExpression.class);
                        conditionExpression.setTextContent(cond2);
                        secondFlow.setConditionExpression(conditionExpression);
                    } else {
                        secondFlow.getConditionExpression().setTextContent(cond2);
                    }
                    System.out.println();
                    System.out.println(firstFlowName + " " + firstFlow.getConditionExpression().getTextContent());
                    System.out.println();
                    System.out.println(secondFlowName + " " + secondFlow.getConditionExpression().getTextContent());
                    System.out.println();
                }
            }

        }

    }
}
