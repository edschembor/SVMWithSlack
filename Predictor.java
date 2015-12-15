package cs475;

import java.io.Serializable;
import java.util.List;

public abstract class Predictor implements Serializable {
	private static final long serialVersionUID = 1L;

	public abstract double getPrecision();
	
	public abstract double getRecall();
	
	public abstract void reset();

	public abstract void train(List<Instance> instances);
	
	public abstract Label predict(Instance instance);
	
	public abstract String getName();
	
	public abstract void setName(String s);
}
