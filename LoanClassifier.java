package cs475;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoanClassifier extends Predictor {

	private int sgd_iterations;
	private double alpha[];
	private double weights[];
	//TODO choose U = C = the margin?
	private double U;

	public LoanClassifier(int sgd_iterations) {
	    this.sgd_iterations = sgd_iterations;
	}
	
	@Override
	public void train(List<Instance> instances) {
	    
		// There is an alpha for each sample.
		alpha = new double[instances.size()];
		// And a weights vector for easy computation of the alphas
		weights = new double[instances.size()];

		// Iterate over each sample sgd_iterations times
		// This is the paper's "outer iteration"
		for(int i = 0; i < sgd_iterations; i++) {
			
			// Compute the weights vector.
			int j = 0;
			for(Instance instance : instances) {
				// Fetch the sample's label.
				double label = Integer.parseInt(instance.getLabel().toString())*2 - 1;
				weights[j] = label * alpha[j] * 1;
				j++;
			}
			
			j = 0;
			for(Instance instance : instances) {
				
				// Fetch the sample's label.
				double label = Integer.parseInt(instance.getLabel().toString())*2 - 1;
				
				// Compute the G term (gradient) - we are using L1 loss function, so D_ii is 0.
				double GTerm = 0.0;
				for(int feature : instance._feature_vector.sparseVector.keySet()) {
					GTerm += label * weights[j] * instance._feature_vector.get(feature);
				}
				GTerm--;
				
				// Find the so-called projected gradient G^P	
				if(alpha[j] == 0) {
					// G = min(0,G)
				} else if(alpha[j] == 0) {
					// G = max(0,G)
				} else {
					// G = G
					// do nothing
				}
				
				// Now use the gradient to optimize a^{k,i+1}_i
				if(GTerm == 0) {
					// No update needed
				} else {
					// a = min( max( a - GTerm/Q , 0) , U)
				}
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
