package matrix;

import jacobian.Jacobian;
import data.ReadTXT;
import grid.Element;

public abstract class Matrix {

    protected int nH, nL;
    protected int numberOfElements, numberOfPoints;
    protected double[][][] Jacobian, jacobianReciprocal;
    protected double[][] DetJ;
    protected double[] ksi, eta;
    protected double[][] N;
    protected double[][] dN_dEta, dN_dKsi;
    protected Element[] elements;
    protected double[][][] coordinates;

    protected Matrix() {
    }

    public void printMatrix(double[][][][] matrix){
        for(int i =0; i<numberOfElements; ++i){
            System.out.println("Dla elementu: " + (i+1));
        for(int j=0; j<numberOfPoints; ++j){
            for(int k=0; k<numberOfPoints; ++k){
                for(int l=0; l<numberOfPoints; ++l){
                    System.out.print(matrix[i][j][k][l] + " ");
                }
                System.out.println();
            }
            System.out.println();
             }
        }
    }
    public void printMatrix(double[][][] matrix){
        for(int k=0; k<numberOfElements; ++k){
            System.out.println("Dla elementu: " + (k+1));
            for(int i=0; i<numberOfPoints; ++i){
                for(int j=0; j< numberOfPoints; ++j){
                    System.out.print(matrix[k][i][j] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
        System.out.println();
    }
    public void printMatrix(double[][] matrix){
        for(int i=0; i<nH*nL; ++i){
            for(int j=0; j<nH*nL; ++j){
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    protected void fillMatrix(double[][][] matrix, double[][][][] matrix2){
        for(int i=0; i<numberOfElements; ++i){
            for(int j=0; j<numberOfPoints; ++j){
                for(int k=0; k<numberOfPoints; ++k){
                    for(int l=0; l<numberOfPoints; ++l){
                        matrix[i][j][k]+=matrix2[i][l][j][k];                           // [l] - bo sumujemy w l punktach całkowania
                    }
                }
            }
        }
    }

    protected void fillGlobalMatrix(double[][] globalMatrix, double[][][] localMatrix){
        for(int i=0; i<numberOfElements; ++i){
            for(int j=0;j<numberOfPoints; ++j){
                for(int k=0; k<numberOfPoints; ++k){
                    globalMatrix[elements[i].getId()[j]-1][elements[i].getId()[k]-1] += localMatrix[i][j][k];
                }
            }
        }
    }

    public Matrix(ReadTXT read){
        this.nH = read.getnH();
        this.nL = read.getnL();
    }

    public Matrix(Jacobian jak){

        this.numberOfElements = jak.getNumberOfElements();
        this.numberOfPoints = jak.getIdLength();

        this.elements = jak.getGrid().getElement();
        this.coordinates = jak.getCooridinates();

        this.N = jak.getN();

        this.Jacobian = jak.getJacobian();
        this.jacobianReciprocal = jak.getJacobianReciprocal();

        this.DetJ = jak.getDetJ();

        this.ksi = jak.getKsi();
        this.eta = jak.getEta();


        this.dN_dEta = jak.getdN_dEta();
        this.dN_dKsi = jak.getdN_dKsi();

        this.nH = jak.getGrid().getnH();
        this.nL = jak.getGrid().getnL();
    }
}
