package client;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import simdata.TaskList;
import simdata.VariableCollection;
import simulation.PararellOrderList;

import java.util.ArrayList;
import java.util.List;

public abstract class PararellOrder {
    public static PararellOrderList setPararellOrder(BpmnModelInstance modelInstance, TaskList taskList, VariableCollection variableCollection) {

        PararellOrderList pararellList = new PararellOrderList();
        StartEvent startEvent = modelInstance.getModelElementsByType(StartEvent.class).iterator().next();
        List<FlowNode> nodes = startEvent.getSucceedingNodes().list();
        List<ParallelGateway> gates = new ArrayList<>();
        int pararellIds = 0;
        List<String> ids = new ArrayList<>();
        int size = nodes.size();
        List<String> taskIdList = new ArrayList<>();
        while (size > 0) {
            List<FlowNode> prepareNextNodes = new ArrayList<>();
            for (var node : nodes) {
                if (node.getElementType() == modelInstance.getModel().getType(ParallelGateway.class) && getNextElements(node, modelInstance).size() > 1)/*node.getSucceedingNodes().list().size() > 1*/ {
                    gates.add((ParallelGateway) node);
                    pararellIds++;
                    pararellList.addPararellRecord(node.getId());
                }
                if (node.getElementType() == modelInstance.getModel().getType(UserTask.class)) {
                    taskIdList.add(node.getId());
                }

                ids.add(node.getId());
                boolean isItMarked = false;
                List<FlowNode> nextNodes = getNextElements(node, modelInstance);


//                List<FlowNode> nextNodes = node.getSucceedingNodes().list();
                for (var nextnode : nextNodes) {
                    for (var id : ids) {
                        if (nextnode.getId().equals(id)) {
                            isItMarked = true;
                        }
                    }
                    if (isItMarked == false) {
                        prepareNextNodes.add(nextnode);
                    }
                }
            }

            nodes = prepareNextNodes;
            size = nodes.size();
        }
        if (variableCollection.getVariableNameList().contains("time") && pararellIds > 0) {
            variableCollection.addVarProcessTime();
            pararellList.addTaskList(taskIdList);
            for (int i = 1; i <= pararellIds; i++) {
                String gateId = pararellList.getStardIdById(i);
                FlowNode startPararell = modelInstance.getModelElementById(gateId);
//                List<FlowNode> nexts = startPararell.getSucceedingNodes().list();
                List<FlowNode> nexts = getNextElements(startPararell, modelInstance);
                List<String> commonNodeIds = new ArrayList<>();
                List<List<String>> idLists = new ArrayList<>();
                for (var startPath : nexts) {
                    List<FlowNode> currentNodes = getNextElements(startPath, modelInstance);
//                    List<FlowNode> currentNodes = startPath.getSucceedingNodes().list();
                    int currentSize = currentNodes.size();
                    List<String> currentIds = new ArrayList<>();
                    List<String> idsToAdd = new ArrayList<>();
                    currentIds.add(startPath.getId());
                    if (startPath.getElementType() == modelInstance.getModel().getType(UserTask.class)) {
                        idsToAdd.add(startPath.getId());
                    }
                    while (currentSize > 0) {
                        List<FlowNode> prepareNodes = new ArrayList<>();
                        for (var node : currentNodes) {
                            currentIds.add(node.getId());
                            if (node.getElementType() == modelInstance.getModel().getType(UserTask.class)) {
                                if (!idsToAdd.contains(node.getId()))
                                    idsToAdd.add(node.getId());
                            }
                            boolean isItMarked = false;
                            List<FlowNode> nextNodes = getNextElements(node, modelInstance);
//                            List<FlowNode> nextNodes = node.getSucceedingNodes().list();
                            for (var nextnode : nextNodes) {
                                for (var id : currentIds) {
                                    if (nextnode.getId().equals(id)) {
                                        isItMarked = true;
                                    }
                                }
                                if (isItMarked == false) {
                                    prepareNodes.add(nextnode);
                                }
                            }
                        }
                        currentNodes = prepareNodes;
                        currentSize = currentNodes.size();
                    }
                    idLists.add(idsToAdd);
                }
                List<String> firstList = idLists.get(0);
                for (var id : firstList) {
                    boolean isCommon = true;
                    for (var compareList : idLists) {
                        boolean checkId = false;
                        for (var compareId : compareList) {
                            if (id.equals(compareId)) {
                                checkId = true;
                            }
                        }
                        if (checkId == false) {
                            isCommon = false;
                        }
                    }
                    if (isCommon == true) {
                        commonNodeIds.add(id);
                    }
                }
                for (var list : idLists) {
                    List<String> toRemove = new ArrayList<>();
                    for (var id : list) {
                        for (var commonId : commonNodeIds) {
                            if (id.equals(commonId)) {
                                toRemove.add(id);
                            }
                        }
                    }
                    list.removeAll(toRemove);
                }
                int value = 0;
                for (var list : idLists) {
                    for (var id : list) {
                        taskList.setPararellOrder(id, i);
                        taskList.setPararellValue(id, value);
                    }
                    value++;
                }
                pararellList.addPaths(i, idLists);

            }

            pararellList.setPararellValues();
        }
        return pararellList;
    }

    public static List<FlowNode> getNextElements(FlowNode node, BpmnModelInstance modelInstance) {
        List<FlowNode> nextNodes = new ArrayList<>();
        if (node.getElementType() == modelInstance.getModel().getType(SubProcess.class)) {
            SubProcess subProcess = modelInstance.getModelElementById(node.getAttributeValue("id"));
            for (var el : subProcess.getFlowElements()) {
                if (el.getElementType() == modelInstance.getModel().getType(StartEvent.class))
                    nextNodes.add((FlowNode) el);
            }
        } else if (node.getElementType() == modelInstance.getModel().getType(EndEvent.class)) {
            EndEvent endEvent = modelInstance.getModelElementById(node.getAttributeValue("id"));
            if (endEvent.getParentElement().getElementType() == modelInstance.getModel().getType(SubProcess.class)) {
                SubProcess subProcess = (SubProcess) endEvent.getParentElement();
                nextNodes = subProcess.getSucceedingNodes().list();
            }
        } else {
            nextNodes.addAll(node.getSucceedingNodes().list());
        }
        return nextNodes;
    }

}

