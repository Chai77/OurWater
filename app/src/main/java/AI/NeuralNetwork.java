package AI;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class NeuralNetwork {

    private Matrix[] weights;
    private Matrix[] biases;
    private int[] layout;

    public NeuralNetwork(int[] layout) {
        this.layout = layout;
        weights = new Matrix[layout.length - 1];
        biases = new Matrix[layout.length - 1];
        for(int i = 0; i < layout.length - 1; i++) {
            weights[i] = new Matrix(layout[i + 1], layout[i]);
            biases[i] = new Matrix(layout[i + 1], 1);
            weights[i].randomize(0, (Math.sqrt(2/(layout[i] + layout[i + 1]))));
            biases[i].randomize(0, 1);
        }
    }

    public NeuralNetwork(String fileName) {
        loadWeights(fileName);
        layout = new int[weights.length + 1];
        layout[0] = weights[0].getCols();
        for(int i = 0; i < weights.length; i++) {
            layout[i + 1] = weights[i].getRows();
        }
        System.out.println("Layout after loading from file: " + Arrays.toString(layout));
    }

    public void train(TrainingData data, int numEpochs, int batchSize, double learningRate) {
        System.out.println("Training Started");
        for(int i = 0; i < numEpochs; i++) {
            data.shuffle();
            double loss = 0;
            for(int k = 0; k < data.getNumData(); k++) {
                IndividualDataSet set = data.get(batchSize, k);
                if(set == null) {
                    continue;
                }
                set = feedForward(set);
                loss += backPropogate(set, learningRate);
                k += batchSize;
            }
            System.out.println("############################# EPOCH_" + (i + 1) + "_COMPLETED #####################################");
            System.out.println("Loss: " + (loss/(double)(data.getNumData())));
        }
    }

    public IndividualDataSet feedForward(IndividualDataSet data) {
        Matrix input = data.getFirstLayerInput();
        for(int i = 1; i < layout.length; i++) {
            input = MatrixOperations.multiply(weights[i - 1], input);
            input.add(biases[i -1]);
            input.applyFunction(Activations.SIGMOID);
            data.add(input);
        }
        return data;
    }

    public Matrix test(Matrix input) {
        for(int i = 1; i < layout.length; i++) {
            input = MatrixOperations.multiply(weights[i - 1], input);
            //MatrixOperations.printMatrix(input, "LAYER_" + i + "_OUTPUT_W");
            //System.out.println();
            input.add(biases[i - 1]);
            // MatrixOperations.printMatrix(input, "LAYER_" + i + "_OUTPUT_B");
            //System.out.println();
            input.applyFunction(Activations.SIGMOID);
            //MatrixOperations.printMatrix(input, "LAYER_" + i + "_OUTPUT_ACT");
            //System.out.println("\n\n");
        }
        return input;
    }

    public double backPropogate(IndividualDataSet set, double learningRate) {   //returns los
        ArrayList<Matrix> inputs = set.getInputs();
        Matrix output = inputs.get(inputs.size() - 1);
        Matrix expectedOutput = set.getExpectedOutput();
        double loss = MatrixOperations.sum(MatrixOperations.square(MatrixOperations.subtract(expectedOutput, output))) * 0.5; //1/2 * (y - yHat)^2
        Matrix propogatedError = MatrixOperations.subtract(expectedOutput, output);
        for(int i = weights.length; i >= 1; i--) {
            //MatrixOperations.printMatrix(propogatedError, "Propogated Error Before " + i);
            propogatedError.multiplyElementwise(MatrixOperations.applyFunction(inputs.get(i), Activations.DSIGMOID));
            //MatrixOperations.printMatrix(inputs.get(i), "Inputs");
            Matrix weightGradient = MatrixOperations.multiply(propogatedError, MatrixOperations.transpose(inputs.get(i - 1)));
            Matrix biasGradient = MatrixOperations.averageColumns(propogatedError);
            weightGradient.multiply(1 * learningRate);
            biasGradient.multiply(1 * learningRate);
            propogatedError = MatrixOperations.multiply(MatrixOperations.transpose(weights[i - 1]), propogatedError);
            weights[i - 1].add(weightGradient);
            biases[i - 1].add(biasGradient);
            //MatrixOperations.printMatrix(weightGradient, "Weight Gradient");
            //MatrixOperations.printMatrix(biasGradient, "Bias Gradient");
        }
        return loss;
    }

    public Matrix[] getWeights() {
        return weights;
    }

    public Matrix[] getBiases() {
        return biases;
    }

    public int[] getLayout() {
        return layout;
    }

    public int getNumInputs() {
        return layout[0];
    }

    public int getNumOutputs() {
        return layout[layout.length - 1];
    }

    public void printWeights() {
        System.out.println("---------------------WEIGHTS-------------------------");
        for(int i = 0; i < weights.length; i++) {
            System.out.println("Layer " + i + " Weights: ");
            System.out.println(weights[i]);
            System.out.println();
        }
    }

    public void printBiases() {
        System.out.println("---------------------BIASES-------------------------");
        for(int i = 0; i < biases.length; i++) {
            System.out.println("Layer " + i + " Biases: ");
            System.out.println(biases[i]);
            System.out.println();
        }
    }

    public void setWeights(Matrix[] weights) {
        this.weights = weights;
    }

    public void setBiases(Matrix[] biases) {
        this.biases = biases;
    }

    public static class IndividualDataSet {
        private ArrayList<Matrix> inputs;
        private Matrix expectedOutput;
        private boolean trainingSet;

        public IndividualDataSet(Matrix inputs) {
            this.inputs = new ArrayList<>();
            this.inputs.add(inputs);
            trainingSet = false;
        }

        public IndividualDataSet(Matrix inputs, Matrix expectedOutputs) {
            this.inputs = new ArrayList<>();
            this.inputs.add(inputs);
            this.expectedOutput = expectedOutputs;
            trainingSet = true;
        }

        public void setExpectedOutputs(Matrix expectedOutput) {
            this.expectedOutput = expectedOutput;
            trainingSet = true;
        }

        public void add(Matrix input) {
            this.inputs.add(input);
        }

        public ArrayList<Matrix> getInputs() {
            return inputs;
        }

        public Matrix getExpectedOutput() {
            return expectedOutput;
        }

        public boolean isTrainingSet() {
            return trainingSet;
        }

        public Matrix getFirstLayerInput() {
            return inputs.get(0);
        }

        @Override
        public String toString() {
            String string = "";
            for(int i = 0; i < inputs.size(); i++) {
                string += "---------------------Layer_" + i + "------------------------------";
                string += inputs.get(i).toString();
            }
            string += "---------------------Expected Outputs------------------------------";
            string += expectedOutput.toString();
            return string;
        }

    }

    public void loadWeights(String fileName) {
        try {
            Scanner scanner = new Scanner(new File(fileName));
            int numWeights = scanner.nextInt();
            weights = new Matrix[numWeights];
            biases = new Matrix[numWeights];
            for(int i = 0; i < weights.length; i++) {
                int weightMatrixRows = scanner.nextInt();
                int weightMatrixCols = scanner.nextInt();
                int biasMatrixRows = scanner.nextInt();
                int biasMatrixCols = scanner.nextInt();
                double[][] weight = new double[weightMatrixRows][weightMatrixCols];
                double[][] bias = new double[biasMatrixRows][biasMatrixCols];
                for(int k = 0; k < weight.length; k++) {
                    for(int j = 0; j < weight[k].length; j++) {
                        weight[k][j] = scanner.nextDouble();
                    }
                }
                for(int k = 0; k < bias.length; k++) {
                    for(int j = 0; j < bias[k].length; j++) {
                        bias[k][j] = scanner.nextDouble();
                    }
                }
                weights[i] = new Matrix(weight);
                biases[i] = new Matrix(bias);
            }
            scanner.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void save(String fileName) {
        try {
            FileWriter fw = new FileWriter(new File(fileName));
            PrintWriter pw = new PrintWriter(fw);
            pw.println(weights.length);
            for(int i = 0; i < weights.length; i++) {
                double[][] weightMatrix = weights[i].getMatrix();
                double[][] biasMatrix = biases[i].getMatrix();
                pw.println(weightMatrix.length + " " + weightMatrix[0].length + " " + biasMatrix.length + " " + biasMatrix[0].length);
                for(int k = 0; k < weightMatrix.length; k++) {
                    for(int j = 0; j < weightMatrix[k].length; j++) {
                        pw.print(weightMatrix[k][j] + " ");
                    }
                    pw.println();
                }
                for(int k = 0; k < biasMatrix.length; k++) {
                    for(int j = 0; j < biasMatrix[k].length; j++) {
                        pw.print(biasMatrix[k][j] + " ");
                    }
                    pw.println();
                }
            }
            fw.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}