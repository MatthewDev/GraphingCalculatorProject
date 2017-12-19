package evaluators;

import parse.EvaluatePostfix;
import parse.InfixToPostfix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by Matthew on 9/18/2017.
 */
public class Evaluator {
    private String postfix, variable;
    public Evaluator(String expression, String variable) {
        this.variable = variable;
        InfixToPostfix converter =  new InfixToPostfix(expression, variable);
        postfix = converter.getPostfix();
    }
    public TreeMap<Double, Double> eval(double start, double end, int steps) {
        TreeMap<Double, Double> ans = new TreeMap<>();//[start, end] inclusive
        double step = (end-start)/steps;
        EvaluatePostfix evaluator = new EvaluatePostfix(postfix, variable, start);
        for(int i = 0 ; i < steps+1; i++) {
            double x = start + i*step;
            evaluator.setVariableValue(x);
            double y = evaluator.getValue();
            //System.out.println(x);

            ans.put(x, y);
        }
        return ans;
    }

    public TreeMap<Double, Double> adaptiveEval(double start, double end, int steps) {
        TreeMap<Double, Double> f = new TreeMap<>();//[start, end] inclusive
        double initStep = (end-start)/steps;
        double step = initStep;
        double minstep = initStep/(1<<16);
        //System.out.println(minstep+" "+initStep);
        EvaluatePostfix evaluator = new EvaluatePostfix(postfix, variable, start);
        double x1 = start;
        double y1 = evaluator.evalVar(x1);
        f.put(x1, y1);
        double maxDist = initStep*10;

        double lastDerivative = 0;
        double derivative = 0;

        ArrayList<Double> lineLength = new ArrayList<Double>();
        while(x1 < end) {
            double x2 = x1 + step;
            double y2 = evaluator.evalVar(x2);
            while((step*2 <= initStep) && dist(y1, y2)*2 < maxDist) {
                step *= 2;
                x2 = x1 + step;
                y2 = evaluator.evalVar(x2);
            }
            while((step/2 >= minstep) && dist(y1, y2) > maxDist) {
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
        }
        Collections.sort(lineLength);
        /*System.out.println(lineLength.get(0));
        System.out.println(lineLength.get(lineLength.size()/2));
        System.out.println(lineLength.get(lineLength.size()-1));*/
        //System.out.println(initStep);
        return f;
    }
    private static double sq(double a) {
        return a*a;
    }
    private static double dist(double a, double b) {
        return Math.abs(b-a);
    }

}
