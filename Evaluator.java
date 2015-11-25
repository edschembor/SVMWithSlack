package cs475;

import java.util.List;

public abstract class Evaluator {

	public abstract double evaluate(List<Instance> instances, Predictor predictor);
}
