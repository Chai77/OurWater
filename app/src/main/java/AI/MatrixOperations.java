package AI;

public class MatrixOperations {
    // Elementwise add
    // elementwise subtract
    // elementwise divide
    // elementwise multiply
    // Multiply Vector

    public static Matrix transpose(Matrix matrix) {
        double[][] result = new double[matrix.getCols()][matrix.getRows()];
        double[][] currMatrix = matrix.getMatrix();
        for (int i = 0; i < currMatrix[0].length; i++) {
            for (int k = 0; k < currMatrix.length; k++) {
                result[i][k] = currMatrix[k][i];
            }
        }
        return new Matrix(result);
    }

    public static Matrix multiply(Matrix mat1, Matrix mat2) {
        double[][] result = new double[mat1.getRows()][mat2.getCols()];
        double[][] matrix1 = mat1.getMatrix();
        double[][] matrix2 = mat2.getMatrix();
        for (int i = 0; i < result.length; i++) {
            for (int k = 0; k < result[i].length; k++) {
                double sum = 0;
                for (int j = 0; j < mat1.getCols(); j++) {
                    sum += matrix1[i][j] * matrix2[j][k];
                }
                result[i][k] = sum;
            }
        }
        return new Matrix(result);
    }

    public static Matrix applyFunction(Matrix mat, Activations activation) {
        double[][] matrix = mat.getMatrix();
        double[][] result = new double[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int k = 0; k < matrix[i].length; k++) {
                if (activation == Activations.SIGMOID) {
                    result[i][k] = 1 / (1 + Math.exp(-1 * matrix[i][k]));
                } else if (activation == Activations.RELU) {
                    result[i][k] = Math.max(matrix[i][k], 0);
                } if(activation == Activations.DSIGMOID) {
                    result[i][k] = matrix[i][k] * (1 - matrix[i][k]);       //matrix[i][k] = sigmoid(previous data)
                }
            }
        }
        return new Matrix(result);
    }

    public static Matrix add(Matrix mat1, Matrix mat2) {
        double[][] result = new double[mat1.getRows()][mat1.getCols()];
        double[][] mat1Arr = mat1.getMatrix();
        double[][] mat2Arr = mat2.getMatrix();
        for (int i = 0; i < result.length; i++) {
            for (int k = 0; k < result[i].length; k++) {
                result[i][k] = mat1Arr[i][k] + mat2Arr[i][k];
            }
        }
        return new Matrix(result);
    }

    public static Matrix add(Matrix mat, double val) {
        double[][] result = new double[mat.getRows()][mat.getCols()];
        double[][] matrix = mat.getMatrix();
        for (int i = 0; i < result.length; i++) {
            for (int k = 0; k < result[i].length; k++) {
                result[i][k] = matrix[i][k] + val;
            }
        }
        return new Matrix(result);
    }

    public static Matrix subtract(Matrix mat1, Matrix mat2) {
        return add(mat1, multiply(mat2, -1));
    }

    public static Matrix subtract(Matrix mat, double val) {
        return add(mat, val * -1);
    }

    public static Matrix divide(Matrix mat1, Matrix mat2) {
        double[][] result = new double[mat1.getRows()][mat1.getCols()];
        double[][] mat1Arr = mat1.getMatrix();
        double[][] mat2Arr = mat2.getMatrix();
        for (int i = 0; i < result.length; i++) {
            for (int k = 0; k < result[i].length; k++) {
                result[i][k] = mat1Arr[i][k] / mat2Arr[i][k];
            }
        }
        return new Matrix(result);
    }

    public static Matrix divide(Matrix mat, double val) {
        return multiply(mat, 1 / val);
    }

    public static Matrix multiplyElementwise(Matrix mat1, Matrix mat2) {
        double[][] result = new double[mat1.getRows()][mat1.getCols()];
        double[][] mat1Arr = mat1.getMatrix();
        double[][] mat2Arr = mat2.getMatrix();
        for (int i = 0; i < result.length; i++) {
            for (int k = 0; k < result[i].length; k++) {
                result[i][k] = mat2Arr[i][k] * mat1Arr[i][k];
            }
        }
        return new Matrix(result);
    }

    public static Matrix multiply(Matrix mat, double val) {
        double[][] result = new double[mat.getRows()][mat.getCols()];
        double[][] matrix = mat.getMatrix();
        for (int i = 0; i < result.length; i++) {
            for (int k = 0; k < result[i].length; k++) {
                result[i][k] = matrix[i][k] * val;
            }
        }
        return new Matrix(result);
    }

    public static Matrix concatMatricesCols(Matrix[] matrices) {
        // All matrices have to be the same size
        // Input matrices are mostly the same size
        double[][] result = new double[matrices[0].getRows()][matrices.length];
        for (int i = 0; i < matrices.length; i++) {
            double[][] matrix = matrices[i].getMatrix();
            for (int k = 0; k < matrix.length; k++) {
                result[k][i] = matrix[k][0];
            }
        }
        return new Matrix(result);
    }

    public static void printMatrix(Matrix matrix, String name) {
        System.out.println("---------------------" + name + "-------------------------");
        System.out.println(matrix);
        System.out.println();
    }

    public static double sum(Matrix matrix) {
        double sum = 0;
        double[][] matArray = matrix.getMatrix();
        for(int i = 0; i < matArray.length; i++) {
            for(int k = 0; k < matArray[i].length; k++) {
                sum += matArray[i][k];
            }
        }
        return sum;
    }

    public static Matrix sumColumns(Matrix matrix) {
        double[][] result = new double[matrix.getRows()][1];
        double[][] matArray = matrix.getMatrix();
        for(int i = 0; i < matArray.length; i++) {
            double sum = 0;
            for(int k = 0; k < matArray[i].length; k++) {
                sum += matArray[i][k];
            }
            result[i][0] = sum;
        }
        return new Matrix(result);
    }

    public static double average(Matrix matrix) {
        double sum = 0;
        double[][] matArray = matrix.getMatrix();
        for(int i = 0; i < matArray.length; i++) {
            for(int k = 0; k < matArray[i].length; k++) {
                sum += matArray[i][k];
            }
        }
        return sum/((double)(matrix.getCols() * matrix.getRows()));
    }

    public static Matrix averageColumns(Matrix matrix) {
        double[][] result = new double[matrix.getRows()][1];
        double[][] matArray = matrix.getMatrix();
        for(int i = 0; i < matArray.length; i++) {
            double sum = 0;
            for(int k = 0; k < matArray[i].length; k++) {
                sum += matArray[i][k];
            }
            result[i][0] = sum/(double)(matrix.getCols());
        }
        return new Matrix(result);
    }

    public static Matrix square(Matrix matrix) {
        double[][] result = new double[matrix.getRows()][matrix.getCols()];
        double[][] matArray = matrix.getMatrix();
        for(int i = 0; i < result.length; i++) {
            for(int k = 0; k < result[i].length; k++) {
                result[i][k] = Math.pow(matArray[i][k], 2);
            }
        }
        return new Matrix(result);
    }
}