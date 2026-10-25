package Activite2.ressources;

import java.io.Serializable;

public class Expression implements Serializable {
    private final double op1;
    private final String operation;
    private final double op2;
    public Expression(double op1, double op2,String operation){
        this.op1=op1;
        this.op2=op2;
        this.operation = operation;
    }
    public double getOp1(){return op1;}
    public double getOp2(){return op2;}
    public String getOperation(){return operation;}
}
