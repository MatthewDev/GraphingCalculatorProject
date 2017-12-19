package structure.abstractsyntaxtree;

/**
 * Created by Matthew on 12/1/2017.
 */
public class OperatorNode implements Node {
    private Operator operator;
    private Node[] children = new Node[2];

    public OperatorNode(Operator operator, Node[] children) {
        this.operator = operator;
        this.children = children;

        if(operator == Operator.TAN) { //rewrite TAN as SIN/COS
            Node numerator = new OperatorNode(Operator.SIN, children);
            Node denominator = new OperatorNode(Operator.COS, children);

            //divide
            this.operator = Operator.DIVIDE;
            this.children = new Node[]{numerator, denominator};
        }
        if(operator == Operator.EXPONENT && children[1].isConstant() && children[1].eval() < 0 ) { //rewrite negative exponent as 1/positive exponent
            Node base = children[0];
            Node exponent = children[1];
            exponent = new ConstantNode(Math.abs(exponent.eval()));
            Node power = new OperatorNode(Operator.EXPONENT, new Node[]{base, exponent});
            //divide
            this.operator = Operator.DIVIDE;
            this.children = new Node[]{new ConstantNode(1), power};
        }

    }

    @Override
    public double eval() {
        //System.out.println(children[0].eval() +" "+children[1].eval()+" "+operator);

        return operator.eval(children);
    }

    @Override
    public Node[] getChildren() {
        return children;
    }

    @Override
    public Operator type() {
        return operator;
    }

    @Override
    public boolean isConstant() {
        for(Node child : children) {
            if(!child.isConstant()) return false;
        }
        return true;
    }


}
