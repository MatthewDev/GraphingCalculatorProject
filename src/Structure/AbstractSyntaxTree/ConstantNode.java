package Structure.AbstractSyntaxTree;

/**
 * Created by Matthew on 12/1/2017.
 */
public class ConstantNode implements Node{
    private double constant;

    public ConstantNode(double constant) {
        this.constant = constant;
    }

    @Override
    public double eval() {
        return constant;
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
        return true;
    }

}
