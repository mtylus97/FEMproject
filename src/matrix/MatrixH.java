package matrix;

import jacobian.Jacobian;
import data.ReadTXT;

public class MatrixH extends Matrix{
    //k- conductivity
    private double conductivity;

    private double[][][] dN_dX, dN_dY;

    //4 wymiarowe, bo dla [każdy element][4 punkty całkowania]
    // [ilość punktów całkowania] na [ilość punktów całkowania]

    //{dN/dx}{dN/dx}T && {dN/dy}{dN/dy}T
    private double[][][][] dN_dx_x_T;
    private double[][][][] dN_dy_x_T;

    //{dN/dx}{dN/dx}T*DetJ && {dN/dy}{dN/dy}T*DetJ
    private double[][][][] dN_dx_x_T_detJ;
    private double[][][][] dN_dy_x_T_detJ;

    // K * ({dN/dx}{dN/dx}T + {dN/dy}{dN/dy}T) * DetJ
    private double[][][][] K_matrix_detJ;

    private double[][][] matrixH;
    private double[][] globalMatrixH;
    public MatrixH(Jacobian jak, ReadTXT read){

        super(jak);

        this.conductivity = read.getConductivity();

        //wyznaczenie pochodnych
        dN_dX = new double[numberOfElements][numberOfPoints][numberOfPoints];
        dN_dY = new double[numberOfElements][numberOfPoints][numberOfPoints];
        filldN_d(dN_dX,dN_dY);

        //{dN/dx}{dN/dx}T && {dN/dy}{dN/dy}T
        dN_dx_x_T = new double[numberOfElements][numberOfPoints][numberOfPoints][numberOfPoints];
        dN_dy_x_T = new double[numberOfElements][numberOfPoints][numberOfPoints][numberOfPoints];
        filldN_dx_x_T(dN_dx_x_T, dN_dy_x_T);

        //{dN/dx}{dN/dx}T*DetJ && {dN/dy}{dN/dy}T*DetJ
        dN_dx_x_T_detJ = new double[numberOfElements][numberOfPoints][numberOfPoints][numberOfPoints];
        dN_dy_x_T_detJ = new double[numberOfElements][numberOfPoints][numberOfPoints][numberOfPoints];
        filldN_dx_x_T_detJ(dN_dx_x_T_detJ, dN_dy_x_T_detJ);


        K_matrix_detJ = new double[numberOfElements][numberOfPoints][numberOfPoints][numberOfPoints];
        fillK_matrix_detJ(K_matrix_detJ);

        matrixH = new double[numberOfElements][numberOfPoints][numberOfPoints];
        fillMatrix(matrixH, K_matrix_detJ);

        //globalna macierz H

        //macierz globbalna jest wymiaru [nh*nl][nh*nl]
        globalMatrixH = new double[nH*nL][nH*nL];
        fillGlobalMatrix(globalMatrixH, matrixH);
    }

    //METODY UZUPEŁNIAJĄCE

    private void filldN_d(double[][][] dN_dX, double[][][] dN_dY) {
        for (int i = 0; i < numberOfElements; ++i) {
            for (int j = 0; j < numberOfPoints; j++) {
                for (int k = 0; k < numberOfPoints; ++k) {

                    dN_dX[i][k][j] = jacobianReciprocal[i][0][j] * dN_dKsi[j][k] + jacobianReciprocal[i][1][j] * dN_dEta[j][k];
                    dN_dY[i][k][j] = jacobianReciprocal[i][2][j] * dN_dKsi[j][k] + jacobianReciprocal[i][3][j] * dN_dEta[j][k];
                }
            }
        }
    }

    private void filldN_dx_x_T(double [][][][] matrixX, double [][][][] matrixY){
        for(int i =0; i<numberOfElements; ++i){
            for(int j=0; j<numberOfPoints; ++j){
                for(int k=0; k<numberOfPoints; ++k){
                    for(int l=0; l<numberOfPoints; ++l){
                        matrixX[i][j][l][k] = dN_dX[i][j][k]*dN_dX[i][j][l];
                        matrixY[i][j][l][k] = dN_dY[i][j][k]*dN_dY[i][j][l];
                    }
                }
            }
        }
    }

    private void filldN_dx_x_T_detJ(double [][][][]matrixX, double[][][][] matrixY){
        for(int i =0; i<numberOfElements; ++i){
            for(int j=0; j<numberOfPoints; ++j){
                for(int k=0; k<numberOfPoints; ++k){
                    for(int l=0; l<numberOfPoints; ++l){
                        matrixX[i][j][k][l] = dN_dx_x_T[i][j][k][l] * DetJ[i][j];
                        matrixY[i][j][k][l] = dN_dy_x_T[i][j][k][l] * DetJ[i][j];
                    }
                }
            }
        }
    }

    private void fillK_matrix_detJ(double[][][][] matrix){
        for(int i =0; i<numberOfElements; ++i){
            for(int j=0; j<numberOfPoints; ++j){
                for(int k=0; k<numberOfPoints; ++k){
                    for(int l=0; l<numberOfPoints; ++l){
                        matrix[i][j][k][l] =  conductivity * (dN_dx_x_T_detJ[i][j][k][l] + dN_dy_x_T_detJ[i][j][k][l]);
                    }
                }
            }
        }
    }

    //METODY DRUKUJĄCE

    public void printN_d(){
        System.out.println("dN/dx");
        for(int i=0;i<numberOfPoints; ++i){
            for(int j=0; j< numberOfPoints; ++j){
                System.out.print(dN_dX[0][i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("dN/dy");
        for(int i=0;i<numberOfPoints; ++i){
            for(int j=0; j< numberOfPoints; ++j){
                System.out.print(dN_dY[0][i][j] + " ");
            }
            System.out.println();
        }
    }

    //GETTERY
    public double[][][][] getdN_dx_x_T () {
        return dN_dx_x_T;
    }
    public double[][][][] getdN_dy_x_T () {
        return dN_dy_x_T;
    }
    public double[][][][] getdN_dx_x_T_detJ () {
        return dN_dx_x_T_detJ;
    }
    public double[][][][] getdN_dy_x_T_detJ () {
        return dN_dy_x_T_detJ;
    }
    public double[][][][] getK_matrix_detJ() {
        return K_matrix_detJ;
    }
    public double[][][] getMatrixH() {
        return matrixH;
    }
    public double[][] getGlobalMatrixH() {
        return globalMatrixH;
    }
}