package AI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class TestingData {

    private ArrayList<Matrix> inputs;
    private int numData;

    public TestingData(String fileName) {
        this.inputs = new ArrayList<>();
        loadData(fileName);
        numData = inputs.size();
    }

    public TestingData() {
        this.inputs = new ArrayList<>();
        numData = 0;
    }

    public void loadData(String fileName) {
        try {
            Scanner scanner = new Scanner(new File(fileName));
            scanner.nextLine();
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] split = line.split(",");
                double[][] input = new double[split.length][1];
                for(int i = 0; i < split.length; i++) {
                    input[i][0] = Integer.parseInt(split[i])/255.0;
                }
                inputs.add(new Matrix(input));
            }
            scanner.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public int getNumData() {
        return numData;
    }

    public ArrayList<Matrix> getData() {
        return inputs;
    }

    public Matrix get(int num) {
        return inputs.get(num);
    }

    public Matrix getRandom() {
        int num = (int)(Math.random() * numData);
        return get(num);
    }

    public Matrix get(int start, int batchSize) {
        Matrix[] matricesInputs = new Matrix[batchSize];
        for (int i = start; i < start + batchSize; i++) {
            if(i >= inputs.size()) {
                return null;
            }
            matricesInputs[i - start] = inputs.get(i);
        }
        Matrix finalInput = MatrixOperations.concatMatricesCols(matricesInputs);
        return finalInput;
    }

    public void shuffle() {
        for (int i = 0; i < numData; i++) {
            int toSwap = (int) (Math.random() * numData);
            Matrix currentInput = inputs.get(i);
            inputs.set(i, inputs.get(toSwap));
            inputs.set(toSwap, currentInput);
        }
    }

}