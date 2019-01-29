package vector;

import data.ReadTXT;
import jacobian.Jacobian;
import matrix.Matrix;
import matrix.MatrixH_BC_2d;

public class vectorP extends Matrix {


    private double[][][] vectorP_PC;
    private double[][] vectorP_EL;
    private double[] vectorP;
    private double t, talfa;
    private  int numberOfElements, numberOfPoints;
    private double initialTemperature, stepTime;

    public vectorP(ReadTXT read, Jacobian jak, MatrixH_BC_2d hbc){

        super(read);
        this.t = read.getAmbientTemperatur();
        this.talfa = read.getAlfa();
        this.numberOfElements = jak.getNumberOfElements();
        this.numberOfPoints = jak.getIdLength();
        this.initialTemperature = read.getInitialTemperature();
        this.stepTime =read.getStepTime();

        vectorP_PC = new double[numberOfElements][numberOfPoints][numberOfPoints];
        vectorP_EL = new double[numberOfElements][numberOfPoints];
        vectorP = new double[nH*nL];

        createVecP(jak, hbc);

    }

    private void createVecP(Jacobian jak, MatrixH_BC_2d hbc){
        for(int i=0; i<numberOfElements; ++i){
            for(int j=0; j<numberOfPoints; ++j){                        //która ściana
                for(int k=0; k<numberOfPoints; ++k) {                   //która funkcja kształtu
                    if(jak.getGrid().element[i].getIsOnEdge()[j]) {
                        if(j==0){       //góra
                            for(int m=0; m<2; ++m)
                                vectorP_PC[i][j][k] += (talfa * t * hbc.getN()[2][m][k]) * hbc.getDetJ()[i][2];
                        }
                        else if(j==1){  //dół
                            for(int m=0; m<2; ++m)
                                vectorP_PC[i][j][k] += (talfa * t * hbc.getN()[0][m][k]) * hbc.getDetJ()[i][0];
                        }
                        else if(j==2){  //lewa
                            for(int m=0; m<2; ++m)
                                vectorP_PC[i][j][k] += (talfa * t * hbc.getN()[3][m][k]) * hbc.getDetJ()[i][3];
                        }
                        else{           //prawa
                            for(int m=0; m<2; ++m)
                                vectorP_PC[i][j][k] += (talfa * t * hbc.getN()[1][m][k]) * hbc.getDetJ()[i][1];
                        }
                    }
                    else
                        vectorP_PC[i][j][k] =0;
                }
            }
        }


        for(int i=0; i<numberOfElements; ++i){
            for(int j=0; j< vectorP_EL[0].length; ++j){
                for(int k=0;k<numberOfPoints; ++k){
                    vectorP_EL[i][k] += vectorP_PC[i][j][k];
                }
            }
        }

        for(int k=0; k<numberOfElements; ++k)
            for(int i=0; i< vectorP.length; ++i){
                for(int j = 0; j<jak.getGrid().element[k].getId().length; ++j ){
                    if(jak.getGrid().element[k].getId()[j] -1 == i)
                        vectorP[i]+=vectorP_EL[k][j];
                }
            }
    }

    public void printVecP(){
        for(int k=0; k< numberOfElements; ++k) {
            System.out.println("element " + k);
            for (int i = 0; i < numberOfPoints; ++i) {
                for (int j = 0; j < numberOfPoints; ++j)

                    System.out.print(vectorP_PC[k][i][j] + " ");
                System.out.println();

            }
            System.out.println();
        }
        System.out.println();

        for(int i=0; i<numberOfElements; ++i){
            for(int k=0;k<numberOfPoints; ++k){
                System.out.print(vectorP_EL[i][k] + " ");
            }
            System.out.println();
        }

        System.out.println();

        System.out.println();
        for(int i=0; i< vectorP.length;++i){
            System.out.printf(vectorP[i] + " ");
        }

        System.out.println();
    }

    public double[][][] getVectorP_PC() {
        return vectorP_PC;
    }


    public double[] getVectorP() {
        return vectorP;
    }

    public double[][] getVectorP_EL() {
        return vectorP_EL;
    }
}
