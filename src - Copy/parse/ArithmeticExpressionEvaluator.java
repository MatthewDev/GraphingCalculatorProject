package parse;

/**
 * Created by Matthew on 9/12/2016.
 */
public class ArithmeticExpressionEvaluator {
    public static void main(String[] args) {
        String infix = "-2+2";
        InfixToPostfix converter = new InfixToPostfix(infix, "x");
        System.out.println(converter.getPostfix());
        EvaluatePostfix test = new EvaluatePostfix(converter.getPostfix(), "x", 10.0);
        System.out.println(test.getValue());
    }
}
