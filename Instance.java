package cs475;

import java.io.Serializable;

public class Instance implements Serializable {

	Label _label = null;
	FeatureVector _feature_vector = null;

	public Instance(FeatureVector feature_vector, Label label) {
		this._feature_vector = feature_vector;
		this._label = label;
	}

	public Label getLabel() {
		return _label;
	}

	public void setLabel(Label label) {
		this._label = label;
	}

	public FeatureVector getFeatureVector() {
		return _feature_vector;
	}

	public void setFeatureVector(FeatureVector feature_vector) {
		this._feature_vector = feature_vector;
	}
	
	
}
