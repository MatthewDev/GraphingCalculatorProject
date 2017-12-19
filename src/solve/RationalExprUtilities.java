package solve;

import Structure.AbstractSyntaxTree.AbstractSyntaxTree;
import Structure.AbstractSyntaxTree.Node;
import Structure.AbstractSyntaxTree.Operator;
import Structure.Pair;
import Structure.PointType.Discontinuity;
import Structure.PointType.RelativeExtrema;
import evaluators.ASTEvaluator;
import evaluators.Evaluator;

import java.util.*;

import static solve.CrossValue.EPSILON;
import static solve.DoubleUtil.maxUlp;
import static solve.DoubleUtil.reasonableError;


/**
 * Created by Matthew on 12/12/2017.
 */
public class RationalExprUtilities {
    private RationalExprUtilities(){

    }

    /**
     * finds all pairs of fractions within an AST given by a node
     * @param node Node to find all the fractions in
     * @return map of numerators mapped to denominators
     */
    private static List<Pair<Node, Node>> fractions(Node node) {
        if(node.getChildren() == null) return new ArrayList<>();

        List<Pair<Node, Node>> fractions = new ArrayList<>();

        //System.out.println(node.type());
        if(node.type() == Operator.DIVIDE) {
            Node numerator = node.getChildren()[0];
            Node denominator = node.getChildren()[1];

            Pair<Node, Node> fraction = new Pair<>(numerator, denominator);
            fractions.add(fraction);

            fractions.addAll(fractions(numerator));
            fractions.addAll(fractions(denominator));
        }

        for(Node child : node.getChildren()) {
            fractions.addAll(fractions(child));
        }

        return fractions;
    }

    /**
     * only finds discontinuities associated with rational functions
     * @param ast
     * @param x1
     * @param x2
     * @return map of x values to discontinuities
     */
    public static Map<Double, Discontinuity> discontinuities(AbstractSyntaxTree ast, double x1, double x2) {
        List<Pair<Node, Node>> fractions = fractions(ast.getRoot());
        Evaluator fx = new ASTEvaluator(ast);

        Map<Double, Discontinuity> discontinuities = new TreeMap<>();
        //ast.setVariable(x);

        for(Pair<Node, Node> fraction : fractions) {
            ASTEvaluator denominator = new ASTEvaluator(new AbstractSyntaxTree(fraction.b, ast.getVariable()));

            Set<Double> discontinuityLocations = CrossValue.equalXes(denominator, x1,x2,0);
            for(double x : discontinuityLocations) {
                discontinuities.put(x, classifyDiscontinuity(fx, x) );
                //System.out.println("discontinuity: "+x+" "+classifyDiscontinuity(fx, x));
            }
        }

        return discontinuities;
    }


    public static Discontinuity classifyDiscontinuity(Evaluator fx, double x) {
        //final double dx = Math.sqrt(Math.ulp(x)); //square root of ULP to make dx slightly bigger such that f(x+-dx) can be accurately calculated without causing floating point error to dominate
        double dx = Math.ulp(x)*10; //make ulp slightly bigger to avoid the issue of f(x+dx) rounding to f(x)

        double limitLeft = fx.eval(x-dx);
        double limitRight = fx.eval(x+dx);

        limitLeft = DoubleUtil.fixBasicallyInfinity(limitLeft);
        limitRight = DoubleUtil.fixBasicallyInfinity(limitRight);

        double outputResolution = reasonableError(limitLeft, limitRight);

        //System.out.println(x+" "+limitLeft+" "+limitRight+" "+dx);
        //System.out.println("classify: "+Math.abs(limitLeft-limitRight) +" "+2*outputResolution);
        if(Math.abs(limitLeft-limitRight) < 2*outputResolution) return Discontinuity.Hole;

        return Discontinuity.Asymptote;
    }

    public static double holeEval(Evaluator fx, double x) {
        final double dx = 2*Math.ulp(x);

        double limitLeft = fx.eval(x-dx);
        double limitRight = fx.eval(x+dx);

        return (limitLeft+limitRight)/2;
    }

}

