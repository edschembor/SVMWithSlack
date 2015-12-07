package cs475;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoanPredictor extends Predictor {

	private int sgd_iterations;
	private double alpha[];
	private HashMap<Integer, Double> weights = new HashMap<>();
	private ArrayList<Instance> supports = new ArrayList<>();
	private double U = 0.0;
	private double bias = 0.0;

	public LoanPredictor(int sgd_iterations, double U, double bias) {
	    this.sgd_iterations = sgd_iterations;
	    this.U = U;
	    this.bias = bias;
	}
	
	@Override
	public void train(List<Instance> instances) {
	    
		System.out.println("BIAS: " + bias);
		System.out.println("U:    " + U);
		
		// There is an alpha for each sample.
		alpha = new double[instances.size()];

		for(Instance instance : instances) {
			for(Integer feature : instance.getFeatureVector().getMap().keySet()) {
				weights.put(feature, 0.0);
			}
		}
		
		/* Normalization */
		/*HashMap<Integer, Double> min = new HashMap<>();
		HashMap<Integer, Double> max = new HashMap<>();
		for(Integer i : weights.keySet()) {
			min.put(i, Double.MAX_VALUE);
			max.put(i, Double.MIN_VALUE);
		}
		for(Instance instance : instances) {
			for(Integer feature : instance._feature_vector.getMap().keySet()) {
				double value = instance.getFeatureVector().get(feature);
				if(min.get(feature) > value) {
					min.put(feature, value);
				}
				if(max.get(feature) < value) {
					max.put(feature, value);
				}
			}
		}
		for(Instance instance : instances) {
			for(Integer feature : instance._feature_vector.getMap().keySet()) {
				if(feature != 50) {
				    double old = instance._feature_vector.get(feature);
                    instance._feature_vector.getMap().put(feature, (old - min.get(feature))/(max.get(feature)+.1 - min.get(feature)));
				}
			}
		}*/

		// Code to find the norm average
		/*double normAvg = 0.0;
		for(Instance instance : instances) {
			double sum = 0.0;
			for(Integer feature : instance._feature_vector.getMap().keySet()) {
				if(feature != 50) {
				    sum += Math.pow(instance._feature_vector.get(feature), 2);
				}
			}
			double norm = Math.sqrt(sum);
			normAvg += norm/((double)instances.size());
		}*/
		//System.out.println("NORM: " + normAvg);
		
		//eights.put(13, 100000.0);
		
		// Set the bias term
		for(Instance instance : instances) {
			instance._feature_vector.getMap().put(50, bias);
		}
		
		// Iterate over each sample sgd_iterations times
		// This is the paper's "outer iteration"
		for(int i = 0; i < sgd_iterations; i++) {
			
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
				GTerm -= 1;
				
				// Find the so-called projected gradient G^P
				double projectedGradient = 0.0;
				if(alpha[j] == 0) {
					projectedGradient = Math.min(GTerm, 0.0);
				} else if(alpha[j] == U) {
					projectedGradient = Math.max(GTerm, 0.0);
				} else if (alpha[j] > 0 && alpha[j] < U){
					projectedGradient = GTerm;
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
				supports.add(instances.get(i));
			}
		}
		
		//System.out.println(alpha.length);
		//System.out.println(supports.size());
		
	}

	@Override
	public Label predict(Instance instance) {
		
		double result = 0.0;
		for(Instance inst : supports) {
			double val = 0.0;
			for(Integer feature : instance._feature_vector.getMap().keySet()) {
				if(inst._feature_vector.getMap().containsKey(feature)) {
				    val += instance._feature_vector.get(feature) * inst._feature_vector.get(feature);
				}
			}
			result += (Integer.parseInt(inst._label.toString())*2-1)*val;
		}

		return result < 0 ? new ClassificationLabel(0) : new ClassificationLabel(1);
	}

	
	
}
