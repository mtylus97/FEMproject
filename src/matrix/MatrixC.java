package matrix;

import jacobian.Jacobian;
import data.ReadTXT;

public class MatrixC extends Matrix{

    //[element][pc][4][4]
    private double[][][][] matrixCpc;

    private double[][][] matrixC;

    private double c, ro;

    private double[][] globalMatrixC;

    public MatrixC(Jacobian jak, ReadTXT read) {
        super(jak);

        this.c = read.getC();
        this.ro =read.getRo();

        // stworzenie i uzupełnienie macierzy w punktach całkowania
        matrixCpc = new double[numberOfElements][numberOfPoints][numberOfPoints][numberOfPoints];
        fillCpc();

        //macierz C
        matrixC = new double[numberOfElements][numberOfPoints][numberOfPoints];
        fillMatrix(matrixC, matrixCpc);

        //macierz C globalna
        globalMatrixC = new double[nH*nL][nH*nL];
        fillGlobalMatrix(globalMatrixC, matrixC);

    }

    private void fillCpc() {
        for (int i = 0; i < numberOfElements; ++i) {
            for (int j = 0; j < numberOfPoints; ++j) {
                for (int k = 0; k < numberOfPoints; ++k) {
                    for (int l = 0; l < numberOfPoints; ++l) {
                        matrixCpc[i][j][k][l] = N[j][k] * N[j][l] * DetJ[i][j] * c * ro;
                    }
                }
            }
        }
    }

    //GETTERY
    public double[][][][] getMatrixCpc() {
        return matrixCpc;
    }
    public double[][][] getMatrixC() {
        return matrixC;
    }
    public double[][] getGlobalMatrixC() {
        return globalMatrixC;
    }
}