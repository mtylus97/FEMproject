package matrix;

import jacobian.Jacobian;
import data.ReadTXT;

public class MatrixH_BC_2d extends Matrix{

    private int alfa;

    private double[][] sideLength;
    private double[][] detJ;

    //[4 powierzchnie][2pc][1- ksi, 2 - eta]
    private final double[][][] ksiEta ={
            {{(-1/Math.sqrt(3)),-1}, {1/Math.sqrt(3), -1}},
            {{1,(-1/Math.sqrt(3))}, {1,(1/Math.sqrt(3))}},
            {{(1/Math.sqrt(3)),1}, {(-1/Math.sqrt(3)),1}},
            {{-1,(1/Math.sqrt(3))}, {-1,(-1/Math.sqrt(3))}}};


    //[4 powierzchnie][2pc][4 funkcje kształtu]

    private double[][][] N;
    private double[][][][] N_NT;
    private double[][][][] sum;
    private double[][][] finalSum;
    protected double[][] globalMatrixHBC;

    public MatrixH_BC_2d(Jacobian jak, ReadTXT read){
        super(jak);
        this.alfa = read.getAlfa();

        this.sideLength = new double[numberOfElements][numberOfPoints];
        fillSideLength(sideLength);

        this.detJ = new double[numberOfElements][numberOfPoints];
        fillDetJ(detJ, sideLength);

        this.N = new double[4][2][4];
        fillN(N);

        this.N_NT = new double[4][2][4][4];
        fillN_NT(N_NT);

        //[4 powierzchnie][4][4]
        this.sum = new double[numberOfElements][4][4][4];
        fillSum(sum);

        this.finalSum = new double[numberOfElements][4][4];
        fillFinalSum(finalSum);

        this.globalMatrixHBC = new double[nH*nL][nH*nL];
        fillGlobalMatrix(globalMatrixHBC, finalSum);
    }


    //METODY UZUPEŁNIAJĄCE
    private void fillSideLength(double[][] sideLength){
        for(int i=0; i<numberOfElements; ++i){
            sideLength[i][0] = Math.sqrt(Math.pow(coordinates[i][1][0]-coordinates[i][0][0],2)+ Math.pow(coordinates[i][1][1]-coordinates[i][0][1],2));//dół
            sideLength[i][1] = Math.sqrt(Math.pow(coordinates[i][1][0]-coordinates[i][2][0],2)+ Math.pow(coordinates[i][1][1]-coordinates[i][2][1],2));//prawa
            sideLength[i][2] = Math.sqrt(Math.pow(coordinates[i][2][0]-coordinates[i][3][0],2)+ Math.pow(coordinates[i][2][1]-coordinates[i][3][1],2));//góra
            sideLength[i][3] = Math.sqrt(Math.pow(coordinates[i][3][0]-coordinates[i][0][0],2)+ Math.pow(coordinates[i][3][1]-coordinates[i][0][1],2));//lewa
        }
    }

    private void fillDetJ(double[][] detJ, double[][] sideLength){
        for(int i=0; i<numberOfElements; ++i){
            for(int j=0; j<numberOfPoints; ++j){
                detJ[i][j] = sideLength[i][j]/2;
            }
        }
    }

    private void fillN_NT(double [][][][] matrix){
        for(int i =0; i<4; ++i){
            for(int j=0; j<2; ++j){
                for(int k=0; k<4; ++k){
                    for(int l=0; l<4; ++l){
                        matrix[i][j][l][k] = alfa*N[i][j][k]*N[i][j][l];
                              //  dN_dX[i][j][k]*dN_dX[i][j][l];
                    }
                }
            }
        }
    }

    private void fillSum(double[][][][] sum){
        for(int i=0; i<numberOfElements; ++i){
            for(int j=0; j<4; ++j){
                for(int m=0; m<2; ++m){
                    for(int l=0; l<4; ++l){
                        for(int k=0; k<4; ++k){
                            sum[i][j][k][l] += N_NT[j][m][k][l]*detJ[i][j];
                        }
                    }
                }
            }
        }
    }

    private void fillFinalSum(double[][][] finalSum){
        for(int i=0; i<numberOfElements; ++i){
            for(int j=0; j<4; ++j){
                for(int k=0; k<4; ++k){
                        finalSum[i][j][k] =(elements[i].getIsOnEdge()[1] ? 1: 0)*sum[i][0][j][k] //powierzchnia 1 - dół(isOnEdge 1)
                                + (elements[i].getIsOnEdge()[3] ? 1: 0)*sum[i][1][j][k]          //powierzchnia 2 - prawa(isOnEdge 3)
                                + (elements[i].getIsOnEdge()[0] ? 1: 0)*sum[i][2][j][k]          //powierzchnia 3 - góra(isOnEdge 0)
                                + (elements[i].getIsOnEdge()[2] ? 1: 0)*sum[i][3][j][k];         //powierzchnia 4 - lewa(isOnEdge 2)
                }
            }
        }
    }

    private void fillN(double[][][] N){
        for(int i=0; i<4; ++i){
            for(int j=0; j<2; ++j){
                N[i][j][0] = 0.25*(1-ksiEta[i][j][0])*(1-ksiEta[i][j][1]);
                N[i][j][1] = 0.25*(1+ksiEta[i][j][0])*(1-ksiEta[i][j][1]);
                N[i][j][2] = 0.25*(1+ksiEta[i][j][0])*(1+ksiEta[i][j][1]);
                N[i][j][3] = 0.25*(1-ksiEta[i][j][0])*(1+ksiEta[i][j][1]);
            }

        }
    }

    //METODY DRUKUJĄCE
    public void printSideLength(){
        for(int i=0; i<4;++i){
            System.out.println(sideLength[0][i]);
        }
    }

    public void printKsiEta(){
        for(int i=0; i<4; ++i){
            for(int j=0; j<2; ++j){
                for(int k=0; k<2; ++k){
                    System.out.print(ksiEta[i][j][k] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public void printN(){
        for(int i=0;i<4; ++i){
            for(int j=0;j<2; ++j){
                for(int k=0; k<4; ++k){
                    System.out.print(N[i][j][k] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }
    public void printN_NT(){
        for(int i =0; i<4; ++i){
            for(int j=0; j<2; ++j){
                for(int k=0; k<4; ++k){
                    for(int l=0; l<4; ++l){
                        System.out.print(N_NT[i][j][l][k] + " ");
                    }
                    System.out.println();
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public double[][][][] getSum() {
        return sum;
    }

    public double[][][] getFinalSum() {
        return finalSum;
    }

    public double[][] getGlobalMatrixHBC() {
        return globalMatrixHBC;
    }

    public double[][] getSideLength() {
        return sideLength;
    }

    public double[][][] getN() {
        return N;
    }

    public double[][] getDetJ() {
        return detJ;
    }
}

