package parse;

import Structure.BiMap;

import static parse.InfixToPostfix.isDouble;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


/**
 * Created by Matthew on 9/12/2016.
 */
public class EvaluatePostfix {

    private String postfix, variable;
    private double value, variableValue;
    private Token[] tokens;

    public EvaluatePostfix(String postfix) {
        this(postfix, "", 0);
    }

    public EvaluatePostfix(String postfix, String variable, double variableValue) {
        this.postfix = postfix;
        this.variable = variable;
        this.variableValue = variableValue;
        preprocess();
        //long sTime = System.nanoTime();
        fastEvaluate();
        //evaluators();
        //totalTime = System.nanoTime() - sTime;
        //evals++;
    }

    private void preprocess() {
        String[] split = postfix.split(" ");
        tokens = new Token[split.length];


        for(int i = 0; i < split.length; i++) {
            //System.out.println(s+"#"+numbers.size());
            String s = split[i];
            if(isDouble(s) || s.equals(variable)) { //is number or variable
                if(isDouble(s)) tokens[i] = new Token(Double.parseDouble(s));
                if(s.equals(variable)) tokens[i] = new Token(true);
            } else { //operator
                tokens[i] = new Token(getOperator(s));
            }
        }
    }
    private void fastEvaluate() {
        //3x faster with fast isDouble and 20x faster with slow isDouble
        Stack<Double> numbers = new Stack<>();


        for(Token token : tokens) {
            //System.out.println(token);
            //System.out.println(s+"#"+numbers.size());
            if(token.isVar || token.isNum()) { //is number or variable
                if(token.isNum()) numbers.push(token.num);
                if(token.isVar) numbers.push(variableValue); //replace variable with value
            } else { //is operator
                if(token.operator.isUnary) {
                    double a = numbers.pop();
                    double c = 0;

                    switch (token.operator) {
                        case NEG:
                            c = -a;
                            break;
                        case SIN:
                            c = Math.sin(a);
                            break;
                        case COS:
                            c = Math.cos(a);
                            break;
                        case TAN:
                            c = Math.tan(a);
                            break;
                        case LOG:
                            c = Math.log10(a);
                            break;
                        case LN:
                            c = Math.log(a);
                            break;
                    }
                    numbers.push(c);
                } else {
                    double b = numbers.pop();
                    double a = numbers.pop();
                    double c = 0;
                    switch (token.operator) {
                        case MULTIPLY:
                            c = a * b;
                            break;
                        case DIVIDE:
                            c = a / b;
                            break;
                        case ADD:
                            c = a + b;
                            break;
                        case SUBTRACT:
                            c = a - b;
                            break;
                        case EXPONENT:
                            c = Math.pow(a, b);
                            break;
                    }
                    numbers.push(c);
                }
            }
        }
        value = numbers.pop();
    }



    private void evaluate() {
        //System.out.println(variableValue);
        Stack<Double> numbers = new Stack<>();
        String[] split = postfix.split(" ");


        for(String s : split) {
            //System.out.println(s+"#"+numbers.size());
            if(isDouble(s) || s.equals(variable)) { //is number or variable
                if(isDouble(s)) numbers.push(Double.parseDouble(s));
                if(s.equals(variable)) numbers.push(variableValue); //replace variable with value
            } else {
                Operator operator = getOperator(s);
                if(s.length() > 1) { //multicharacter operators are functions and only take one input
                    double a = numbers.pop();
                    double c = 0;

                    switch (operator) {
                        case NEG:
                            c = -a;
                            break;
                        case SIN:
                            c = Math.sin(a);
                            break;
                        case COS:
                            c = Math.cos(a);
                            break;
                        case TAN:
                            c = Math.tan(a);
                            break;
                        case LOG:
                            c = Math.log10(a);
                            break;
                        case LN:
                            c = Math.log(a);
                            break;
                    }
                    numbers.push(c);
                } else { //normal operator
                    double b = numbers.pop();
                    double a = numbers.pop();
                    double c = 0;
                    switch (operator) {
                        case MULTIPLY:
                            c = a * b;
                            break;
                        case DIVIDE:
                            c = a / b;
                            break;
                        case ADD:
                            c = a + b;
                            break;
                        case SUBTRACT:
                            c = a - b;
                            break;
                        case EXPONENT:
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
        fastEvaluate();
        //evaluators();

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

    private static Operator getOperator (String s) {
        if(operatorMap.containsKey(s)) return operatorMap.get(s);
        else return Operator.NONE;
    }
    private static final Map<String, Operator> operatorMap = new HashMap<String, Operator>(){{
        put("neg", Operator.NEG);
        put("sin", Operator.SIN);
        put("cos", Operator.COS);
        put("tan", Operator.TAN);
        put("log", Operator.LOG);
        put("ln", Operator.LN);
        put("*", Operator.MULTIPLY);
        put("/", Operator.DIVIDE);
        put("+", Operator.ADD);
        put("-", Operator.SUBTRACT);
        put("^", Operator.EXPONENT);
    }};

    private class Token {
        public Operator operator = Operator.NONE;
        public Double num = 0d;
        public boolean isVar = false;
        public Token (boolean isvar) {
            this.isVar = isvar;
        }
        public Token (Operator operator) {
            this.operator = operator;
        }
        public Token (double value) {
            this.num = value;
        }

        public boolean isNum() {
            return !isVar && operator == Operator.NONE;
        }

        @Override
        public String toString() {
            if(isNum()) return ""+num;
            if(isVar) return "var";
            return operator.toString();
        }

    }

    private enum Operator {
        NEG(true),
        SIN(true),
        COS(true),
        TAN(true),
        LOG(true),
        LN(true),

        ADD (false),
        SUBTRACT(false),
        MULTIPLY(false),
        DIVIDE(false),
        EXPONENT(false),
        NONE(false);


        public boolean isUnary;
        Operator(boolean isUnary) {
            this.isUnary = isUnary;
        }
    }
}
