package client;

import simdata.AssigneeList;
import simdata.VariableCollection;

import java.util.Scanner;

public abstract class CreateAssigneeList {
    public static AssigneeList createAssigneeList(VariableCollection variableCollection) {
        Scanner reader = new Scanner(System.in);
        AssigneeList assigneeList = new AssigneeList();
        System.out.println("Ilu pracowników ma być przydzielonych do zadań w procesie?");
        int assignees = reader.nextInt();
        for (int i = 1; i <= assignees; i++) {
            System.out.println("Podaj nazwę pracownika " + i);
            String assigneeName = reader.next();
            assigneeList.addAssignne(assigneeName);
        }
        assigneeList.createAssigneeVarValueRecords(variableCollection);
        return assigneeList;
    }
}
