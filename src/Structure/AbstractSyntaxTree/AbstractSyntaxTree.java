package Structure.AbstractSyntaxTree;

import Structure.Pair;
import parse.InfixToPostfix;

import java.util.*;

import static parse.InfixToPostfix.isDouble;

/**
 * Created by Matthew on 12/1/2017.
 */
public class AbstractSyntaxTree {
    private String postfix, variableName;
    private Node root;
    private VariableNode variable;


    public AbstractSyntaxTree(String expression, String variableName){
        InfixToPostfix converter = new InfixToPostfix(expression, variableName);
        this.postfix = converter.getPostfix();
        this.variableName = variableName;

        constructAST();
    }

    public AbstractSyntaxTree(Node node, VariableNode variable){
        this.variable = variable;
        root = node;
    }


    public Node getRoot() {
        return root;
    }

    public void setVariable(double x) {
        variable.setVariable(x);
    }

    public VariableNode getVariable(){
        return variable;
    }

    public double eval(double x) {
        variable.setVariable(x);
        return root.eval();
    }

    /**
     * constructs AST by defining the root node and variable node for the instance
     * converts postfix into AST
     */
    private void constructAST() {
        Stack<Node> nodes = new Stack<>();
        String[] split = postfix.split(" ");

        variable = new VariableNode();

        for(String s : split) {
            //System.out.println(s+"#"+numbers.size());
            if(isDouble(s) || s.equals(variableName)) { //is number or variableName
                if(isDouble(s)) {
                    double val = Double.parseDouble(s);
                    nodes.push(new ConstantNode(val));
                }
                if(s.equals(variableName)) {
                    nodes.push(variable);
                }
            } else {
                Operator operator = getOperator(s);
                if(operator.isUnary) {
                    Node[] operand = {nodes.pop()};
                    Node node = new OperatorNode(operator, operand);
                    nodes.push(node);
                }
                if(!operator.isUnary) { //not unary thus binary operator
                    Node b = nodes.pop(); //b is before a due to stack ordering
                    Node a = nodes.pop();
                    Node[] operand = {a, b};
                    Node node = new OperatorNode(operator, operand);
                    nodes.push(node);
                }
            }
        }


        root = nodes.pop();
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


}
