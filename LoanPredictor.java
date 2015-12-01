package cs475;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoanPredictor extends Predictor {

	private int sgd_iterations;
	private double alpha[];
	private HashMap<Integer, Double> weights = new HashMap<>();
	private ArrayList<Integer> supports = new ArrayList<>();
	private double U = 1.0;

	public LoanPredictor(int sgd_iterations) {
	    this.sgd_iterations = sgd_iterations;
	}
	
	@Override
	public void train(List<Instance> instances) {
	    
		// There is an alpha for each sample.
		alpha = new double[instances.size()];

		for(Instance instance : instances) {
			for(Integer integ : instance.getFeatureVector().getMap().keySet()) {
				weights.put(integ, 0.0);
			}	
		}
		
		// Iterate over each sample sgd_iterations times
		// This is the paper's "outer iteration"
		for(int i = 0; i < sgd_iterations; i++) {
			
			// Compute the weights vector.
			/*int j = 0;
			for(Instance instance : instances) { //Possible issue?
				// Fetch the sample's label.
				double label = Integer.parseInt(instance.getLabel().toString())*2 - 1;
				double val = 0.0;
				for(int k : instance.getFeatureVector().getMap().keySet()) {
					weights.put(k, weights.get(k) + label * alpha[j] * instance.getFeatureVector().get(k));
				}
				j++;
			}*/
			
			int j = 0;
			for(Instance instance : instances) {
				
				// Fetch the sample's label.
				double label = Integer.parseInt(instance.getLabel().toString())*2 - 1;
				
				// Compute the G term (gradient) - we are using L1 loss function, so D_ii is 0.
				double GTerm = 0.0;
				for(int feature : instance._feature_vector.getMap().keySet()) {
					GTerm += weights.get(feature) * instance._feature_vector.get(feature);
				}
				GTerm *= label;
				GTerm--;
				
				// Find the so-called projected gradient G^P
				double projectedGradient = 0.0;
				if(alpha[j] == 0) {
					projectedGradient = Math.min(GTerm, 0.0);
				} else if(alpha[j] == U) {
					projectedGradient = Math.max(GTerm, 0.0);
				} else {
					// do nothing
				}
				
				// Now use the gradient to optimize a^{k,i+1}_i
				if(projectedGradient == 0) {
					// No update needed
				} else {
					double oldAlpha = alpha[j];
					double Qii = 0.0;
					for(Integer integ : instance.getFeatureVector().getMap().keySet()) {
						Qii+= Math.pow(instance.getFeatureVector().get(integ), 2);
					}
					alpha[j] = Math.min( Math.max( oldAlpha - GTerm/Qii , 0) , U);
					
					// Update the weights vector.
					for(Integer integ : instance.getFeatureVector().getMap().keySet()) {
						double update = (alpha[j] - oldAlpha)*label*instance.getFeatureVector().get(integ);
						double newWeight = weights.get(integ) + update;
						weights.put(integ, newWeight);
					}
				}
				j++;
			}
		}
		
		for(int i = 0; i < instances.size(); i++) {
			if(alpha[i] != 0) {
				supports.add(i);
			}
		}
		
	}

	@Override
	public Label predict(Instance instance) {
		
		// TODO
		
		return null;
	}

	
	
}
