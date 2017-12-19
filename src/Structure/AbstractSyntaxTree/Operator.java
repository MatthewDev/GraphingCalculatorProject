package Structure.AbstractSyntaxTree;

import java.util.stream.IntStream;

/**
 * Created by Matthew on 12/5/2017.
 */
public enum Operator {
    //Unary
    NEG (true,(double[] a) -> -a[0]            ),
    SIN (true,(double[] a) -> Math.sin(a[0])   ),
    COS (true,(double[] a) -> Math.cos(a[0])   ),
    TAN (true,(double[] a) -> Math.tan(a[0])   ),
    LOG (true,(double[] a) -> Math.log10(a[0]) ),
    LN  (true,(double[] a) -> Math.log(a[0])   ),

    NONE(true,(double[] a) -> a[0]             ),

    //Binary
    ADD     (false,(double[] a) -> a[0]+a[1]            ),
    SUBTRACT(false,(double[] a) -> a[0]-a[1]            ),
    MULTIPLY(false,(double[] a) -> a[0]*a[1]            ),
    DIVIDE  (false,(double[] a) -> a[0]/a[1]            ),
    EXPONENT(false,(double[] a) -> Math.pow(a[0], a[1]) );

    public boolean isUnary;
    private IOperator operator;
    Operator(boolean isUnary, IOperator operator) {
        this.isUnary = isUnary;
        this.operator = operator;
    }
    public double eval(double[] input) {
        return operator.eval(input);
    }
    public double eval(Node[] input) {
        return operator.eval(input);
    }

    private interface IOperator{
        double eval(double[] input);

        default double eval(Node[] input) {
            double[] inputValue = new double[input.length];
            inputValue = IntStream.range(0, inputValue.length).mapToDouble(i -> input[i].eval()).toArray();
            return eval(inputValue);
        }
    }
}

