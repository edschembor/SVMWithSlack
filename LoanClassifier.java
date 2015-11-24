package cs475;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoanClassifier extends Predictor {

	private int sgd_iterations;
	private double alpha[];
	
	public LoanClassifier(int sgd_iterations) {
	    this.sgd_iterations = sgd_iterations;
	}
	
	@Override
	public void train(List<Instance> instances) {
	    
		// There is an alpha for each sample.
		alpha = new double[instances.size()];
		
		// Iterate over each sample sgd_ierations times
		for(int i = 0; i < sgd_iterations; i++) {
			
			// Compute the W-Vector.
			double wValues[] = new double[instances.size()];
			int j = 0;
			for(Instance instance : instances) {
				// Fetch the sample's label.
				double label = Integer.parseInt(instance.getLabel().toString());
				wValues[j] = label * alpha[j] * 1;
				j++;
			}
			
			j = 0;
			for(Instance instance : instances) {
				
				// Fetch the sample's label.
				double label = Integer.parseInt(instance.getLabel().toString());
				
				// Compute the G term - we are using L1 loss function, so D_ii is 0.
				double GTerm = 0.0;
				for(int feature : instance._feature_vector.sparseVector.keySet()) {
					GTerm += label * wValues[j] * instance._feature_vector.get(feature);
				}
				GTerm--;
				
				j++;
			}
		}
		
	}

	@Override
	public Label predict(Instance instance) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
