package parse;

import java.util.ArrayList;
import java.util.Stack;

import static parse.InfixToPostfix.isDouble;


/**
 * Created by Matthew on 9/12/2016.
 */
public class Denominator {
    //public long totalTime = 0, evals = 0;

    private String postfix, variable;
    private double value, variableValue;
    private Token[] tokens;

    public Denominator(String postfix) {
        this(postfix, "");
    }

    public Denominator(String postfix, String variable) {
        this.postfix = postfix;
        this.variable = variable;
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
                if(isDouble(s)) tokens[i] = new Token(s);
                if(s.equals(variable)) tokens[i] = new Token(true);
            } else { //operator
                tokens[i] = new Token(getOperator(s));
            }
        }
    }
    private void fastEvaluate() { //~5000ns vs 20,000ns for 1/x^2
        //System.out.println(variableValue);
        Stack<String> expressions = new Stack<>();
        ArrayList<String> demoninators = new ArrayList<String>();


        //System.out.println(postfix);
        for(Token token : tokens) {
            //System.out.println(token);
            //System.out.println(s+"#"+numbers.size());
            if(token.isVar || token.isNum()) { //is number or variable
                if(token.isNum()) expressions.push(""+token.num);
                if(token.isVar) expressions.push(variable);
            } else { //is operator
                if(token.operator.isUnary) {
                    String a = expressions.pop();

                    switch (token.operator) {

                        case NEG:
                            a += " neg";
                            break;
                        case SIN:
                            a += " sin";
                            break;
                        case COS:
                            a += " cos";
                            break;
                        case TAN:
                            a += " cos";
                            break;
                        case LOG:
                            a += " cos";
                            break;
                        case LN:
                            a += " cos";
                            break;
                    }
                    expressions.push(a);
                } else {
                    String b = expressions.pop();
                    String a = expressions.pop();
                    String c = a+" "+b;
                    switch (token.operator) {
                        case MULTIPLY:
                            c += " *";
                            break;
                        case DIVIDE:
                            c += " /";
                            demoninators.add(b);
                            break;
                        case ADD:
                            c += " +";
                            break;
                        case SUBTRACT:
                            c += " -";
                            break;
                        case EXPONENT:
                            //demoninators.add(a); //b is expression :| so sign = idfk
                            c += " ^";
                            break;
                    }
                    expressions.push(c);
                }
            }
        }
        //System.out.println(demoninators);
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
        switch (s) {
            case "neg":
                return Operator.NEG;

            case "sin":
                return Operator.SIN;

            case "cos":
                return Operator.COS;

            case "tan":
                return Operator.TAN;

            case "log":
                return Operator.LOG;

            case "ln":
                return Operator.LN;

            case "*":
                return Operator.MULTIPLY;

            case "/":
                return Operator.DIVIDE;

            case "+":
                return Operator.ADD;

            case "-":
                return Operator.SUBTRACT;

            case "^":
                return Operator.EXPONENT;

        }
        return Operator.NONE;
    }


    private class Token {
        public Operator operator = Operator.NONE;
        public String num;
        public boolean isVar = false;
        public Token (boolean isvar) {
            this.isVar = isvar;
        }
        public Token (Operator operator) {
            this.operator = operator;
        }
        public Token (String value) {
            this.num = value;
        }

        public boolean isNum() {
            return !isVar && operator == Operator.NONE;
        }

        @Override
        public String toString() {
            if(isNum()) return num;
            if(isVar) return "var";
            return operator.toString();
        }

    }

    enum Operator {
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
