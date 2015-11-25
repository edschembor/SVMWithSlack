package cs475;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoanClassifier extends Predictor {

	private int sgd_iterations;
	private double alpha[];
	//TODO choose U = C = the margin?
	private double U;

	public LoanClassifier(int sgd_iterations) {
	    this.sgd_iterations = sgd_iterations;
	}
	
	@Override
	public void train(List<Instance> instances) {
	    
		// There is an alpha for each sample.
		alpha = new double[instances.size()];
		
		// Iterate over each sample sgd_iterations times
		// This is the paper's "outer iteration"
		for(int i = 0; i < sgd_iterations; i++) {
			
			// Compute the W-Vector.
			// I think we can start with a 0 vector here
			double wValues[] = new double[instances.size()];
			int j = 0;
			for(Instance instance : instances) {
				// Fetch the sample's label.
				double label = Integer.parseInt(instance.getLabel().toString())*2 - 1;
				wValues[j] = label * alpha[j] * 1;
				j++;
			}
			
			j = 0;
			for(Instance instance : instances) {
				
				// Fetch the sample's label.
				double label = Integer.parseInt(instance.getLabel().toString());
				
				// Compute the G term (gradient) - we are using L1 loss function, so D_ii is 0.
				double GTerm = 0.0;
				for(int feature : instance._feature_vector.sparseVector.keySet()) {
					GTerm += label * wValues[j] * instance._feature_vector.get(feature);
				}
				GTerm--;
				
				// Now use the gradient to optimize a^{k,i+1}_i
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
