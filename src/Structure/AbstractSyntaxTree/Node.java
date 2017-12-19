package Structure.AbstractSyntaxTree;

/**
 * Created by Matthew on 12/1/2017.
 */
public interface Node {

    double eval();
    Node[] getChildren();
    Operator type();
    boolean isConstant();
}
