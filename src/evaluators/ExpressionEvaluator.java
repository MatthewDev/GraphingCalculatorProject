package evaluators;

import parse.EvaluatePostfix;
import parse.InfixToPostfix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

/**
 * Created by Matthew on 9/18/2017.
 */
public class ExpressionEvaluator implements Evaluator{
    private String postfix, variable;
    public ExpressionEvaluator(String expression, String variable) {
        this.variable = variable;
        InfixToPostfix converter =  new InfixToPostfix(expression, variable);
        postfix = converter.getPostfix();
    }
    public double eval(double x) {
        EvaluatePostfix evaluator = new EvaluatePostfix(postfix, variable, x);
        return evaluator.getValue();
    }

    @Override
    public TreeMap<Double, Double> eval(double start, double end, double stepSize) {
        TreeMap<Double, Double> ans = new TreeMap<>();
        int steps = (int) Math.ceil((end-start)/stepSize);
        EvaluatePostfix expression = new EvaluatePostfix(postfix, variable, start);
        for(int i = 0 ; i < steps+1; i++) {
            double x = start + i*stepSize;
            double y = expression.evalVar(x);
            //System.out.println(x+" "+y);

            ans.put(x, y);
        }
        return ans;
    }

    public TreeMap<Double, Double> adaptiveEval(double start, double end, double stepSize) {
        long sTime = System.nanoTime();

        TreeMap<Double, Double> f = new TreeMap<>();//[start, end] inclusive
        double initStep = stepSize;
        double step = initStep;
        double minStep = initStep/(1<<4);
        //System.out.println(minstep+" "+initStep);
        EvaluatePostfix evaluator = new EvaluatePostfix(postfix, variable, start);
        double x1 = start;
        double y1 = evaluator.evalVar(x1);
        f.put(x1, y1);
        double maxDist = initStep*10;

        double lastDerivative = 0;
        double derivative = 0;

        ArrayList<Double> lineLength = new ArrayList<Double>();
        int realSteps = 0;
        while(x1 < end) {
            double x2 = x1 + step;
            double y2 = evaluator.evalVar(x2);
            while((step*2 <= initStep) && dist(y1, y2)*2 < maxDist) {
                step *= 2;
                x2 = x1 + step;
                y2 = evaluator.evalVar(x2);
            }
            while((step/2 >= minStep) && dist(y1, y2) > maxDist) {
                step /= 2;
                x2 = x1 + step;
                y2 = evaluator.evalVar(x2);
            }
            double len = Math.sqrt(sq(y2-y1)+sq(x2-x1));
            lineLength.add(len);
            if(len > 1) {

            }
            //System.out.println(step);
            f.put(x2, y2);
            if(len > 1) {
                //f.put(x2, null);
            }
            x1 = x2;
            y1 = y2;
            realSteps++;
        }
        Collections.sort(lineLength);
        /*System.out.println(lineLength.get(0));
        System.out.println(lineLength.get(lineLength.size()/2));
        System.out.println(lineLength.get(lineLength.size()-1));*/
        long totalTime = System.nanoTime() - sTime;
        //System.out.println(totalTime);
        //System.out.println(realSteps);
        return f;
    }
    private static double sq(double a) {
        return a*a;
    }
    private static double dist(double a, double b) {
        return Math.abs(b-a);
    }

}
