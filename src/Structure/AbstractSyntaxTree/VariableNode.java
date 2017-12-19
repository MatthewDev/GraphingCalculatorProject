package Structure.AbstractSyntaxTree;

/**
 * Created by Matthew on 12/1/2017.
 */
public class VariableNode implements Node {
    private double value;


    public VariableNode() {

    }

    public void setVariable(double value){
        this.value = value;
    }

    @Override
    public double eval() {
        return value;
    }

    @Override
    public Node[] getChildren() {
        return null;
    }

    @Override
    public Operator type() {
        return Operator.NONE;
    }

    @Override
    public boolean isConstant() {
        return false;
    }
}
