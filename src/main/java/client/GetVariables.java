package client;

import org.camunda.bpm.model.bpmn.instance.Task;
import simdata.SimTask;
import simdata.TaskList;
import simdata.VariableCollection;

import java.util.Scanner;

public abstract class GetVariables {
    public static VariableCollection createVariables(){
        VariableCollection varCollection = new VariableCollection();
        varCollection.createSimVariable("time");
        System.out.println("Ile zmiennych oprócz czasu ma uwzględniać symulacja?");
        Scanner reader = new Scanner(System.in);
        int vars = reader.nextInt();
        for (int i=1; i<=vars; i++){
            System.out.println("Podaj nazwę zmiennej " + i);
            String varName = reader.next();
            varCollection.createSimVariable(varName);
        }
        return varCollection;
    }
}
