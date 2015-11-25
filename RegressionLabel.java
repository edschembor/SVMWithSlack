package cs475;

import java.io.Serializable;

public class RegressionLabel extends Label implements Serializable {
	private double label;

	public RegressionLabel(double label) {
		//Auto-generated constructor stub
		this.label = label;
	}

	@Override
	public String toString() {
		//Auto-generated method stub
		return "" + label;
	}

}
