package evaluators;

import Structure.AbstractSyntaxTree.AbstractSyntaxTree;
import parse.EvaluatePostfix;

import java.util.TreeMap;

/**
 * Created by Matthew on 12/11/2017.
 */
public class ASTEvaluator implements Evaluator{
    private AbstractSyntaxTree ast;
    public ASTEvaluator(AbstractSyntaxTree ast) {
        this.ast = ast;
    }
    @Override
    public double eval(double x) {
        return ast.eval(x);
    }

    @Override
    public TreeMap<Double, Double> eval(double start, double end, double stepSize) {
        TreeMap<Double, Double> ans = new TreeMap<>();
        int steps = (int) Math.ceil((end-start)/stepSize);
        for(int i = 0 ; i < steps+1; i++) {
            double x = start + i*stepSize;
            double y = eval(x);

            ans.put(x, y);
        }
        return ans;
    }
}
