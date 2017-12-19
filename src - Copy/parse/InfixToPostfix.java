package parse;

import java.util.*;


/**
 * Created by Matthew on 9/12/2016.
 */
public class InfixToPostfix {

    private String infix, originalInfix, postfix, variable;
    private static HashMap<String, Integer> precedence = new HashMap<String, Integer>(){{ //<- generally not advised, but its a constant
        put("(", -99);//functions are explicitly handed
        put(")", -99);


        put("^", -2);//e
        put("*", -3);//md
        put("/", -3);
        put("neg", -3);//equivalent to *(-1)

        put("+", -4);//as
        put("-", -4);

        put("sin", 0);//functions run immediately
        put("cos", 0);
        put("tan", 0);
        put("log", 0);
        put("ln", 0);
    }};
    public static boolean isDouble(String s){ //kinda bad practice
        try{
            Double.parseDouble(s);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public InfixToPostfix(String infix){
        this(infix, "");
    }

    public InfixToPostfix(String infix, String variable){
        this.originalInfix = infix;
        this.infix = infix.replace(" ", ""); //delete spaces\

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
        //System.out.println(matchString);
        String regex = "(?<="+matchString+")|(?="+matchString+")";
        String[] split = expression.split(regex); //regex splits before and after any number
        return split;
    }

    private void evaluate(){
        Stack<String> operatorStack = new Stack<>();
        StringBuilder sb = new StringBuilder();
        String[] split = splitOperators(infix);
        //String[] split = infix.split("((?<=[0-9])(?=[^0-9]))|((?=[0-9])(?<=[^0-9]))"); //regex splits before and after any number
        //System.out.println(Arrays.toString(split));
        boolean numberAhead = true; //flag for whether or not the next token should be a number
        //numberAhead is used to check if - means unary negative or subtraction
        for(String s : split) {
            //special negative check
            if(s.contains("-") && numberAhead) {
                operatorStack.push("neg");
                continue;
            }

            if(isDouble(s) || s.equals(variable)){ //if float or variable
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

            //System.out.println(curOperator+" "+operatorStack.peek());
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
    static void dump(String[] arr) {
        for (String s : arr) {
            System.out.format("[%s]", s);
        }
        System.out.println();
    }


}
