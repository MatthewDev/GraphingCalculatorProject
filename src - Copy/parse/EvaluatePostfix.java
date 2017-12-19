package parse;

import static parse.InfixToPostfix.isDouble;

import java.util.Stack;
import java.util.regex.Pattern;



/**
 * Created by Matthew on 9/12/2016.
 */
public class EvaluatePostfix {
    private String postfix, variable, split[];
    private double value, variableValue;

    public EvaluatePostfix(String postfix) {
        this(postfix, "", 0);
    }

    public EvaluatePostfix(String postfix, String variable, double variableValue) {
        this.postfix = postfix;
        this.variable = variable;
        this.variableValue = variableValue;
        split = postfix.split(" ");
        evaluate();
    }
    public void setVariableValue(double value) {
        this.variableValue = value;
        evaluate();
    }
    private void evaluate() {
        //System.out.println(variableValue);
        Stack<Double> numbers = new Stack<>();

        for(String s : split) {
            //System.out.println(s+"#"+numbers.size());
            if(isDouble(s) || s.equals(variable)) { //is number or variable
                if(isDouble(s)) numbers.push(Double.parseDouble(s));
                if(s.equals(variable)) numbers.push(variableValue); //replace variable with value
            } else {
                if(s.length() > 1) { //multicharacter operators are functions and only take one input
                    double a = numbers.pop();
                    double c = 0;

                    switch (s) {
                        case "neg":
                            c = -a;
                            break;
                        case "sin":
                            c = Math.sin(a);
                            break;
                        case "cos":
                            c = Math.cos(a);
                            break;
                        case "tan":
                            c = Math.tan(a);
                            break;
                        case "log":
                            c = Math.log10(a);
                            break;
                        case "ln":
                            c = Math.log(a);
                            break;
                    }
                    numbers.push(c);
                } else { //normal operator
                    double b = numbers.pop();
                    double a = numbers.pop();
                    double c = 0;
                    switch (s) {
                        case "*":
                            c = a * b;
                            break;
                        case "/":
                            c = a / b;
                            break;
                        case "+":
                            c = a + b;
                            break;
                        case "-":
                            c = a - b;
                            break;
                        case "^":
                            c = Math.pow(a, b);
                            break;
                    }
                    numbers.push(c);
                }
            }
        }
        value = numbers.pop();
    }
    public double evalVar(double variableValue) {
        this.variableValue = variableValue;
        evaluate();
        return value;
    }

    @Override
    public String toString() {
        return  "Postfix: "+ getPostfix() +"\n"+
                "Value: "+ getValue() +"\n";
    }

    public String getPostfix() {
        return postfix;
    }

    public double getValue() {
        return value;
    }

    private class Operator {
        public static final int NEG=0, SIN=0;
    }
}
