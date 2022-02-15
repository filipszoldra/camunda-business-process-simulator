package client;

public class ExclusiveGateConditionalsInput {
    public String gateId;
    public String gateName;
    public int gateType = 0;
    public String flow1Name;
    public String flow2Name;
    public String condVarName;
    public int value1;
    public int value2;
    public String sign1;
    public String sign2;

    public ExclusiveGateConditionalsInput(String gateId, String gateName, String flow1Name, String flow2Name) {
        this.gateId = gateId;
        if (gateName == null) {
            this.gateName = gateId;
        } else
            this.gateName = gateName;
        this.flow1Name = flow1Name;
        this.flow2Name = flow2Name;
    }
}
