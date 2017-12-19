package parse;

import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Pattern;


/**
 * Created by Matthew on 9/12/2016.
 */
public class InfixToPostfix {
    private static Pattern isNumber = Pattern.compile("[-+]?\\d*\\.?\\d+");
    private String infix, originalInfix, postfix, variable;
    private static final HashMap<String, Integer> precedence = new HashMap<String, Integer>(){{ //<- generally not advised, but its a constant (instance initializer within an anonymous inner class)
        put("(", -99);//functions are explicitly handed
        put(")", -99);


        put("^", -2);//e
        put("*", -3);//md
        put("/", -3);

        put("+", -4);//as
        put("-", -4);

        put("sin", 0);//functions run immediately
        put("cos", 0);
        put("tan", 0);
        put("log", 0);
        put("ln", 0);

        put("neg", 1);//evaluate FIRST

    }};

    private static final HashMap<String, Double> constants = new HashMap<String, Double>(){{ //<- generally not advised, but its a constant (instance initializer within an anonymous inner class)
        put("pi", Math.PI);
        put("e", Math.E);
    }};



    public static boolean isDouble(String s){
        return s != null && isNumber.matcher(s).matches();

    }

    public InfixToPostfix(String infix){
        this(infix, "");
    }

    public InfixToPostfix(String infix, String variable){
        this.originalInfix = infix;
        this.infix = infix.replace(" ", ""); //delete spaces

        this.variable = variable;

        evaluate();
    }
    private static HashSet<String> specialOp = new HashSet<String>(){{
        add("+");
        add("*");
        add("^");
        add("(");
        add(")");
    }};
    private static String[] splitOperators(String expression) {
        String[] operators = precedence.keySet().toArray(new String[precedence.size()]);
        StringBuilder match = new StringBuilder();
        for(String operator : operators) {
            match.append('(');
            if(specialOp.contains(operator)) match.append('\\');
            match.append(operator);
            match.append(")|");
        }
        match.deleteCharAt(match.length()-1);
        String matchString = match.toString();
        String regex = "(?<="+matchString+")|(?="+matchString+")";
        String[] split = expression.split(regex); //regex splits before and after any number
        return split;
    }

    private static DecimalFormat constantFormat = new DecimalFormat("0.00000000000000000000");
    private void evaluate(){
        Stack<String> operatorStack = new Stack<>();
        StringBuilder sb = new StringBuilder();
        String[] split = splitOperators(infix);
        boolean numberAhead = true; //flag for whether or not the next token should be a number
        //numberAhead is used to check if - means unary negative or subtraction
        for(String s : split) {
            //special negative check
            if(s.contains("-") && numberAhead) {
                operatorStack.push("neg");
                continue;
            }

            if(constants.containsKey(s) || isDouble(s) || s.equals(variable)){ //if float or variable
                if(constants.containsKey(s)) s = constantFormat.format(constants.get(s));
                sb.append(s+" ");
                numberAhead = false;
                continue;
            }

            //then operator parsing
            String curOperator = s;
            numberAhead = true; //if there is an operator, then the next symbol should be a number unless it is a closing parenthesis
            if(curOperator.contains("(")) {
                operatorStack.push(curOperator);
                continue;
            }
            if(curOperator.equals(")")) {
                while(!operatorStack.peek().contains("(")){
                    sb.append(operatorStack.pop()+" ");
                }
                numberAhead = false;
                operatorStack.pop(); //dispose of open parenthesis
                continue;
            }
            if(operatorStack.isEmpty()) {
                operatorStack.push(curOperator);
                continue;
            }

            if(isHigher(curOperator, operatorStack.peek())) {
                operatorStack.push(curOperator);
                continue;
            }
            if(isLower(curOperator, operatorStack.peek())) {
                while(!operatorStack.isEmpty() && !isHigher(curOperator, operatorStack.peek())){//if equal pop, thus not higher instead of lower
                    sb.append(operatorStack.pop()+" ");
                }
                operatorStack.push(curOperator);
                continue;
            }

            //equal precedence, left to right, so pop left first
            sb.append(operatorStack.pop() + " ");
            operatorStack.push(curOperator);
        }
        while(!operatorStack.isEmpty()){
            sb.append(operatorStack.pop()+" ");
        }
        sb.deleteCharAt(sb.length()-1);
        postfix = sb.toString();
    }
    private static boolean isHigher(String a, String b){
        return precedence.get(a)>precedence.get(b);
    }
    private static boolean isLower(String a, String b){
        return precedence.get(a)<precedence.get(b);
    }

    public String getPostfix(){
        return postfix;
    }


}
