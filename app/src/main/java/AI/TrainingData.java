package AI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class TrainingData {

    private ArrayList<Matrix> inputs;
    private ArrayList<Matrix> expectedOutputs;
    private int numData;

    public TrainingData() {
        inputs = new ArrayList<>();
        expectedOutputs = new ArrayList<>();
    }

    public TrainingData(String fileName) {
        inputs = new ArrayList<>(1000000);
        expectedOutputs = new ArrayList<>(1000000);
        loadData(fileName);
        numData = inputs.size();
    }

    public TrainingData(ArrayList<Matrix> inputs, ArrayList<Matrix> expectedOutputs) {
        this.inputs = inputs;
        this.expectedOutputs = expectedOutputs;
        this.numData = inputs.size();
    }

    public void loadData(String fileName) {
        try {
            Scanner scanner = new Scanner(new File(fileName));
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] numbers = line.split(",");
                int expectedOutputBefore = Integer.parseInt(numbers[0]);
                double[][] expectedOutputArr = new double[10][1];
                for(int i = 0; i < 10; i++) {
                    expectedOutputArr[i][0] = (i == expectedOutputBefore) ? 1 : 0;
                }
                expectedOutputs.add(new Matrix(expectedOutputArr));
                double[][] inputArr = new double[numbers.length - 1][1];
                for(int i = 1; i < numbers.length - 1; i++) {
                    inputArr[i - 1][0] = Integer.parseInt(numbers[i])/255.0;
                }
                inputs.add(new Matrix(inputArr));
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shuffle() {
        for (int i = 0; i < numData; i++) {
            int toSwap = (int) (Math.random() * numData);
            Matrix currentInput = inputs.get(i);
            Matrix currentExpectedOutput = expectedOutputs.get(i);
            inputs.set(i, inputs.get(toSwap));
            inputs.set(toSwap, currentInput);
            expectedOutputs.set(i, expectedOutputs.get(toSwap));
            expectedOutputs.set(toSwap, currentExpectedOutput);
        }
    }

    public NeuralNetwork.IndividualDataSet get(int batchSize, int start) {
        Matrix[] matricesInputs = new Matrix[batchSize];
        Matrix[] matricesOutputs = new Matrix[batchSize];
        for (int i = start; i < start + batchSize; i++) {
            if(i >= inputs.size()) {
                return null;
            }
            matricesInputs[i - start] = inputs.get(i);
            matricesOutputs[i - start] = expectedOutputs.get(i);
        }
        Matrix finalInput = MatrixOperations.concatMatricesCols(matricesInputs);
        Matrix finalOutput = MatrixOperations.concatMatricesCols(matricesOutputs);
        return new NeuralNetwork.IndividualDataSet(finalInput, finalOutput);
    }

    public int getNumData() {
        return numData;
    }

}