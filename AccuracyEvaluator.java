package cs475;

import java.io.Serializable;
import java.util.List;

public class AccuracyEvaluator extends Evaluator implements Serializable {
	public double evaluate(List<Instance> instances, Predictor predictor) {
		predictor.train(instances);
		int correct = 0, count = 0;
		for(Instance inst: instances) {
			Label predictedLabel = predictor.predict(inst);
			if(inst.getLabel().toString().equals(predictedLabel.toString())) {
				correct++;
			}
			count++;
		}
		return (correct*1.0/(count*1.0));
	}
}
