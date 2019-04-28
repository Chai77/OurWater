package AI;

import java.util.Random;

public class Matrix {

    private double[][] matrix;
    private int rows;
    private int cols;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        matrix = new double[rows][cols];
        fill(0);
    }

    public Matrix(double[][] matrix) {
        this.rows = matrix.length;
        this.cols = matrix[0].length;
        this.matrix = matrix;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(double[][] matrix) {
        this.rows = matrix.length;
        this.cols = matrix[0].length;
        this.matrix = matrix;
    }

    public void fill(double num) {
        for (int i = 0; i < matrix.length; i++) {
            for (int k = 0; k < matrix[i].length; k++) {
                matrix[i][k] = num;
            }
        }
    }

    public void randomize(double mean, double standardDeviation) {
        Random random = new Random();
        for (int i = 0; i < matrix.length; i++) {
            for (int k = 0; k < matrix[i].length; k++) {
                matrix[i][k] = random.nextGaussian()*standardDeviation + mean;
            }
        }
    }

    public void transpose() {
        double[][] newMatrix = new double[cols][rows];
        for (int i = 0; i < matrix[0].length; i++) {
            for (int k = 0; k < matrix.length; k++) {
                newMatrix[i][k] = matrix[k][i];
            }
        }
        setMatrix(newMatrix);
    }

    public void applyFunction(Activations activation) {
        for (int i = 0; i < matrix.length; i++) {
            for (int k = 0; k < matrix[i].length; k++) {
                if (activation == Activations.SIGMOID) {
                    matrix[i][k] = 1 / (1 + Math.exp(-1 * matrix[i][k]));
                } else if (activation == Activations.RELU) {
                    matrix[i][k] = Math.max(matrix[i][k], 0);
                }
            }
        }
    }

    public void add(Matrix mat) {
        double[][] result = new double[rows][cols];
        double[][] mat2Arr = mat.getMatrix();
        if (mat.getRows() == rows && mat.getCols() != cols) {
            for (int i = 0; i < result.length; i++) {
                for (int k = 0; k < result[i].length; k++) {
                    result[i][k] = matrix[i][k] + mat2Arr[i][0];
                }
            }
        } else {
            for (int i = 0; i < result.length; i++) {
                for (int k = 0; k < result[i].length; k++) {
                    result[i][k] = mat2Arr[i][k] + matrix[i][k];
                }
            }
        }
        setMatrix(result);
    }

    public void add(double val) {
        double[][] result = new double[rows][cols];
        for (int i = 0; i < result.length; i++) {
            for (int k = 0; k < result[i].length; k++) {
                result[i][k] = matrix[i][k] + val;
            }
        }
        setMatrix(result);
    }

    public void subtract(Matrix mat) {
        mat.multiply(-1);
        add(mat);
    }

    public void subtract(double val) {
        add(val * -1);
    }

    public void divide(Matrix mat) {
        double[][] result = new double[mat.getRows()][mat.getCols()];
        double[][] mat2Arr = mat.getMatrix();
        for (int i = 0; i < result.length; i++) {
            for (int k = 0; k < result[i].length; k++) {
                result[i][k] = matrix[i][k] / mat2Arr[i][k];
            }
        }
        setMatrix(result);
    }

    public void divide(double val) {
        multiply(1 / val);
    }

    public void multiply(Matrix mat) {
        double[][] result = new double[this.getRows()][mat.getCols()];
        double[][] matrix2 = mat.getMatrix();
        for (int i = 0; i < result.length; i++) {
            for (int k = 0; k < result[i].length; k++) {
                double sum = 0;
                for (int j = 0; j < this.getCols(); j++) {
                    sum += matrix[i][j] * matrix2[j][k];
                }
                result[i][k] = sum;
            }
        }
        setMatrix(result);
    }

    public void multiplyElementwise(Matrix mat) {
        double[][] result = new double[mat.getRows()][mat.getCols()];
        double[][] mat2Arr = mat.getMatrix();
        for (int i = 0; i < result.length; i++) {
            for (int k = 0; k < result[i].length; k++) {
                result[i][k] = mat2Arr[i][k] * matrix[i][k];
            }
        }
        setMatrix(result);
    }

    public void multiply(double val) {
        double[][] result = new double[rows][cols];
        for (int i = 0; i < result.length; i++) {
            for (int k = 0; k < result[i].length; k++) {
                result[i][k] = matrix[i][k] * val;
            }
        }
        setMatrix(result);
    }

    @Override
    public String toString() {
        String string = "";
        for (int i = 0; i < rows; i++) {
            for (int k = 0; k < cols; k++) {
                if (k == cols - 1) {
                    string += matrix[i][k] + "";
                } else {
                    string += matrix[i][k] + ", ";
                }
            }
            string += "\n";
        }
        return string;
    }
}