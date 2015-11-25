package cs475;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
			evaluateAndSavePredictions(predictor, instances, predictions_file);
		} else {
			System.out.println("Requires mode argument.");
		}
	}
	

	private static Predictor train(List<Instance> instances, String algorithm) {
		// Train the model using "algorithm" on "data"
		Predictor trainedPredictor = null;
		
		if(algorithm.equals("majority")) {
		// Anonymous inner class for the majority predictor
		Predictor majorityPredictor = new Predictor() {
			int binaryMajority = 0;
			public void train(List<Instance> instances) {
				int ones = 0, zeros = 0;
				for(Instance inst: instances) {
					if (inst.getLabel().toString().equals("1")){
						ones++;
					} else if (inst.getLabel().toString().equals("0")) {
						zeros++;
					} else {
						System.out.println("Bad Label");
						System.exit(0);
					}
				}
				if(ones >= zeros) { 
					//System.out.println("chose ones");
					binaryMajority = 1; 
				} else { 
					//System.out.println("chose zeros");
					binaryMajority = 0; 
				}
			}
			
			public Label predict(Instance inst) {
				return new ClassificationLabel(binaryMajority);
			}
		}; // end anonymous inner class
		trainedPredictor = majorityPredictor;
		} // end if majority
		
		else if(algorithm.equals("even_odd")) {
		// Anonymous inner class for the even/odd predictor
		Predictor evenOddPredictor = new Predictor() {
			int evenOddWinner;
			public void train(List<Instance> instances) {
				// Nothing to do in training
			}

			public Label predict(Instance inst) {
				double evens = 0, odds = 0;
				FeatureVector fv = inst.getFeatureVector();
				for(Integer idx : fv.getMap().keySet()) {
					if ((idx % 2) == 1) {
						odds += fv.get(idx); 
					} else { 
						evens += fv.get(idx); 
					}
				}

				if(evens >= odds) { 
					return new ClassificationLabel(1); 
				} else { 
					return new ClassificationLabel(0); 
				}
			}
		}; //end anonymous inner class
		trainedPredictor = evenOddPredictor;
		} // end if even/odd
		else { System.out.println("bad algorithm type"); System.exit(0); }	
		
		// Evaluate the model if we're looking at dev or train data
		
		AccuracyEvaluator eval = new AccuracyEvaluator();
		double accuracy = eval.evaluate(instances, trainedPredictor);
		System.out.println("Train: " + accuracy);
		return trainedPredictor;
	}

	private static void evaluateAndSavePredictions(Predictor predictor,
			List<Instance> instances, String predictions_file) throws IOException {
		PredictionsWriter writer = new PredictionsWriter(predictions_file);
		// Evaluate the model if labels are available. 
		//AccuracyEvaluator eval = new AccuracyEvaluator();
		//double accuracy = eval.evaluate(instances, predictor);
		//System.out.println("Test: " + accuracy);
		for (Instance instance : instances) {
			Label label = predictor.predict(instance);
			writer.writePrediction(label);
		}
		
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
