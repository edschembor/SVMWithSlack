package cs475;

import java.io.Serializable;

public class ClassificationLabel extends Label implements Serializable {
	private int label;

	public ClassificationLabel(int label) {
		//Auto-generated constructor stub
		this.label = label;
	}

	@Override
	public String toString() {
		//Auto-generated method stub
		return "" + label;
	}

}
