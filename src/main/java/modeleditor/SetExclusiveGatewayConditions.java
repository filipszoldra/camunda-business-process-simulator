package modeleditor;

import client.ExclusiveGateConditionalsInput;
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
import java.util.List;
import java.util.Scanner;

public abstract class SetExclusiveGatewayConditions {
    public static void setExclusiveGatewayConditions (BpmnModelInstance modelInstance, List<ExclusiveGateConditionalsInput> gateInputs, List<String> condVarNames){

        for(var condVar : condVarNames){
            AddConditionalValue.AddConditionalValue(modelInstance, condVar);
        }
        for(var gateInput : gateInputs){
            Gateway gate = modelInstance.getModelElementById(gateInput.gateId);
            Collection<SequenceFlow> flows = gate.getOutgoing();
            Iterator<SequenceFlow> flowIterator = flows.iterator();
            SequenceFlow firstFlow = flowIterator.next();
            SequenceFlow secondFlow = flowIterator.next();
            String cond1 = ("${" + gateInput.condVarName + " " + gateInput.sign1 + " " + gateInput.value1 + "}");
            String cond2 = ("${" + gateInput.condVarName + " " + gateInput.sign2 + " " + gateInput.value2 + "}");
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
        }
    }
}
