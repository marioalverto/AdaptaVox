/*import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.core.learning.TrainingElement;
import org.neuroph.core.learning.SupervisedTrainingElement;

import java.util.Arrays;
import java.util.Vector;
import org.neuroph.util.TransferFunctionType;

/**
* Red Neuronal de Prueba.
*/
/*public class RedNeuronal {

	private int entradas;
	private int salidas;
	private MultiLayerPerceptron myMlPerceptron;
	private TrainingSet<SupervisedTrainingElement> trainingSet;

	public RedNeuronal(){
		entradas = 0;
		salidas = 0;
		
	}

	private void setEntradas(int e){
		this.entradas = e;
	}

	private void setSalidas(int e){
		this.salidas = e;
	}
	
	private void setTamanoEntrenamiento() {
		
		this.trainingSet = new TrainingSet<SupervisedTrainingElement>(this.entradas, this.salidas);

	}

	private void setEntrenamiento(double[ ] in, double[ ] out) {
		
		trainingSet.addElement(new SupervisedTrainingElement(new double[ ]in, new double[ ]out));

	}

	private void crearPerceptron(){
		// crear perceptron multicapa
		myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.TANH, 2, 3, 1);
		// entrenamiento
		myMlPerceptron.learn(trainingSet);

		// prueba del perceptron
		System.out.println("Testing trained neural network");
		testNeuralNetwork(myMlPerceptron, trainingSet);

		// salvar el entrenamiento
		myMlPerceptron.save("myMlPerceptron.nnet");

		// cargar el entrenamiento a la red neuronal
		NeuralNetwork loadedMlPerceptron = NeuralNetwork.load("myMlPerceptron.nnet");

		// probar la red neuronal
		System.out.println("Testing loaded neural network");
		testNeuralNetwork(loadedMlPerceptron, trainingSet);

	}

	public static void testNeuralNetwork(NeuralNetwork nnet, TrainingSet tset) {

		for(TrainingElement trainingElement : tset.trainingElements()) {

			nnet.setInput(trainingElement.getInput());
			nnet.calculate();
			double[ ] networkOutput = nnet.getOutput();
			System.out.print("Input: " + Arrays.toString(trainingElement.getInput()) );
			System.out.println(" Output: " + Arrays.toString(networkOutput) );

		}

	}

}*/