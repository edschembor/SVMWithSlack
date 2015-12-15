package cs475;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

public class Classify {
	static public LinkedList<Option> options = new LinkedList<Option>();
	
	public static void main(String[] args) throws IOException {
		// Parse the command line.
		String[] manditory_args = { "mode"};
		createCommandLineOptions();
		CommandLineUtilities.initCommandLineParameters(args, Classify.options, manditory_args);
	
		String mode = CommandLineUtilities.getOptionValue("mode");
		String data = CommandLineUtilities.getOptionValue("data");
		String predictions_file = CommandLineUtilities.getOptionValue("predictions_file");
		String algorithm = CommandLineUtilities.getOptionValue("algorithm");
		String model_file = CommandLineUtilities.getOptionValue("model_file");
		
		if (mode.equalsIgnoreCase("train")) {
			if (data == null || algorithm == null || model_file == null) {
				System.out.println("Train requires the following arguments: data, algorithm, model_file");
				System.exit(0);
			}
			// Load the training data.
			DataReader data_reader = new DataReader(data, true);
			List<Instance> instances = data_reader.readData();
			data_reader.close();
			
			// Train the model.
			Predictor predictor = train(instances, algorithm);
			predictor.setName("KROW");
			saveObject(predictor, model_file);		
			
		} else if (mode.equalsIgnoreCase("test")) {
			if (data == null || predictions_file == null || model_file == null) {
				System.out.println("Train requires the following arguments: data, predictions_file, model_file");
				System.exit(0);
			}
			
			// Load the test data.
			DataReader data_reader = new DataReader(data, true);
			List<Instance> instances = data_reader.readData();
			data_reader.close();
			
			// Load the model.
			Predictor predictor = (Predictor)loadObject(model_file);
			System.out.println(predictor.getName());
			predictor.reset();
			evaluateAndSavePredictions(predictor, instances, predictions_file);
		} else {
			System.out.println("Requires mode argument.");
		}
	}
	

	private static Predictor train(List<Instance> instances, String algorithm) throws IOException {
		// Train the model using "algorithm" on "data"
		/*HashMap<Integer, Double> weights = new HashMap<>();
		for(Instance instance : instances) {
			for(Integer feature : instance.getFeatureVector().getMap().keySet()) {
				weights.put(feature, 0.0);
			}
		}*/
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
		System.out.println("Train min: " + min.get(2));
		System.out.println("Train max: " + max.get(2));
		for(Instance instance : instances) {
			for(Integer feature : instance._feature_vector.getMap().keySet()) {
				if(feature != 50) {
				    double old = instance._feature_vector.get(feature);
                    instance._feature_vector.getMap().put(feature, (old - min.get(feature))/(max.get(feature)+.1 - min.get(feature)));
				}
			}
		}*/
		
		Predictor trainedPredictor = null;
        trainedPredictor = new LoanPredictor(10, 1.2, 1.7);
        trainedPredictor.train(instances);
		
		// Evaluate the model if we're looking at dev or train data
		AccuracyEvaluator eval = new AccuracyEvaluator();
		double accuracy = eval.evaluate(instances, trainedPredictor);
		PredictionsWriter writer = new PredictionsWriter("trainingPredictions");
		for (Instance instance : instances) {
			Label label = trainedPredictor.predict(instance);
			writer.writePrediction(label);
		}
		System.out.println("Train: " + accuracy);
		trainedPredictor.reset();
		writer.close();
		System.out.println("======");
		
		/*for(double u = 1; u<6; u++) {
			for(double b = 1; b<6; b++) {
		        trainedPredictor = new LoanPredictor(10, u*.2, b*1.7);
		        trainedPredictor.train(instances);
				eval = new AccuracyEvaluator();
				accuracy = eval.evaluate(instances, trainedPredictor);
				System.out.println("Train: " + accuracy);
				System.out.println("Recall   : " + (trainedPredictor.getRecall()));
				System.out.println("Precision: " + (trainedPredictor.getPrecision()));
				trainedPredictor.reset();
				System.out.println("===============");
			}
		}*/

		
		return trainedPredictor;
	}

	private static void evaluateAndSavePredictions(Predictor predictor,
			List<Instance> instances, String predictions_file) throws IOException {
		
		// Train the model using "algorithm" on "data"
		/*HashMap<Integer, Double> weights = new HashMap<>();
		for(Instance instance : instances) {
			for(Integer feature : instance.getFeatureVector().getMap().keySet()) {
				weights.put(feature, 0.0);
			}
		}*/
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
		System.out.println("Test min: " + min.get(2));
		System.out.println("Test max: " + max.get(2));
		for(Instance instance : instances) {
			for(Integer feature : instance._feature_vector.getMap().keySet()) {
				if(feature != 50) {
				    double old = instance._feature_vector.get(feature);
                    instance._feature_vector.getMap().put(feature, (old - min.get(feature))/(max.get(feature)+.1 - min.get(feature)));
				}
			}
		}*/
		
		PredictionsWriter writer = new PredictionsWriter(predictions_file);
		// Evaluate the model if labels are available. 
		AccuracyEvaluator eval = new AccuracyEvaluator();
		double accuracy = eval.evaluate(instances, predictor);
		System.out.println("Test: " + accuracy);
		for (Instance instance : instances) {
			Label label = predictor.predict(instance);
			writer.writePrediction(label);
		}
		
		System.out.println("Recall   : " + (predictor.getRecall()));
		System.out.println("Precision: " + (predictor.getPrecision()));
		predictor.reset();
		
		writer.close();
		
	}

	public static void saveObject(Object object, String file_name) {
		try {
			ObjectOutputStream oos =
				new ObjectOutputStream(new BufferedOutputStream(
						new FileOutputStream(new File(file_name))));
			oos.writeObject(object);
			oos.close();
		}
		catch (IOException e) {
			System.err.println("Exception writing file " + file_name + ": " + e);
		}
	}

	/**
	 * Load a single object from a filename. 
	 * @param file_name
	 * @return
	 */
	public static Object loadObject(String file_name) {
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File(file_name))));
			Object object = ois.readObject();
			ois.close();
			return object;
		} catch (IOException e) {
			System.err.println("Error loading: " + file_name);
		} catch (ClassNotFoundException e) {
			System.err.println("Error loading: " + file_name);
		}
		return null;
	}
	
	public static void registerOption(String option_name, String arg_name, boolean has_arg, String description) {
		OptionBuilder.withArgName(arg_name);
		OptionBuilder.hasArg(has_arg);
		OptionBuilder.withDescription(description);
		Option option = OptionBuilder.create(option_name);
		
		Classify.options.add(option);		
	}
	
	private static void createCommandLineOptions() {
		registerOption("data", "String", true, "The data to use.");
		registerOption("mode", "String", true, "Operating mode: train or test.");
		registerOption("predictions_file", "String", true, "The predictions file to create.");
		registerOption("algorithm", "String", true, "The name of the algorithm for training.");
		registerOption("model_file", "String", true, "The name of the model file to create/load.");
		
		// Other options will be added here.
	}
}
