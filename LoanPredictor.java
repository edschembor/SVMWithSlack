package cs475;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoanPredictor extends Predictor {

	private int sgd_iterations;
	private double alpha[];
	private HashMap<Integer, Double> weights;
	private ArrayList<Instance> supports;
	private double U = 0.0;
	private double bias = 0.0;
	public double tp = 0;
	public double fp = 0;
	public double tn = 0;
	public double fn = 0;
	public String name;
	public boolean flag = true;

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
		supports = new ArrayList<>();
		weights = new HashMap<>();

		int ones = 0;
		int all = 0;
		for(Instance instance : instances) {
			for(Integer feature : instance.getFeatureVector().getMap().keySet()) {
				weights.put(feature, 0.0);
			}
			double label = Integer.parseInt(instance.getLabel().toString())*2 - 1;
			if(label == 1) {
				ones++;
			}
			all++;
		}

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
		}
		System.out.println("NORM: " + normAvg);*/
		
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
				Instance instance = instances.get(i);
				instance.setAlpha(alpha[i]);
				supports.add(instance);
			}
		}
		
	}

	@Override
	public Label predict(Instance instance) {
		
		double result = 0.0;
		double val = 0.0;
		for(Instance inst : supports) {
			val = 0.0;
			//System.out.println(inst.getAlpha());
			for(Integer feature : instance._feature_vector.getMap().keySet()) {
				if(inst._feature_vector.getMap().containsKey(feature)) {
				    val += instance._feature_vector.get(feature) * inst._feature_vector.get(feature);
				    //System.out.println(feature + " : " + instance._feature_vector.get(feature));
				    //System.out.println("INST : " + inst._feature_vector.get(feature));
				}
			}
			result += (Integer.parseInt(inst._label.toString())*2-1)*val*inst.getAlpha();
		}

		String label = instance.getLabel().toString();
		ClassificationLabel resultLabel;
		if(flag) {
			System.out.println("Result:" + result);
			System.out.println("Inst feature: " + instance._feature_vector.get(2));
			System.out.println("Inst label: " + instance._label.toString());
			System.out.println("Support: " + supports.get(0).getAlpha());
			System.out.println("Val: " + val);
			flag = false;
		}
		if(result < 0) {
			resultLabel = new ClassificationLabel(0);
			if(label.equals("0")) {
				tn++;
			}else{
				fp++;
			}
		}else{
			resultLabel = new ClassificationLabel(1);
			if(label.equals("0")) {
				fn++;
			}else{
				tp++;
			}
		}
		return resultLabel;
	}
	
	@Override
	public double getPrecision() {
		return tp / (tp+fp);
	}
	
	@Override
	public double getRecall() {
		return tp / (tp+fn);
	}
	
	@Override
	public void reset() {
		tp = 0;
		fp = 0;
		tn = 0;
		fn = 0;
		flag = true;
	}
	
	@Override
	public void setName(String s) {
		this.name = s;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
}
