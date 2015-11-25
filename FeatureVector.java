package cs475;

import java.io.Serializable;
import java.util.HashMap;

public class FeatureVector implements Serializable {
        public HashMap<Integer, Double> fv;
	int size;

	public FeatureVector() {
		size = 0;
		fv = new HashMap<Integer, Double>();
	}

	public void add(int index, double value) {
		//Auto-generated method stub
		fv.put(index, value);
		size++;
	}
	
	public double get(int index) {
		//Auto-generated method stub
		return fv.get(index);
	}

	public HashMap<Integer, Double>  getMap() {
		return fv;
	}
}
