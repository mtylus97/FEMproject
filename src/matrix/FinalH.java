package matrix;

import data.ReadTXT;

public class FinalH extends Matrix{


    private double[][] sum;
    private double[][] c_dT;
    private double time, stepTime, simulationTime, initialTemperature, ambientTemperature;


    public FinalH(ReadTXT read, MatrixH matrixH, MatrixH_BC_2d matrixHBC, MatrixC matrixC){

        super(read);

        this.stepTime = read.getStepTime();
        this.simulationTime = read.getSimulationTime();
        this.initialTemperature = read.getInitialTemperature();
        this.ambientTemperature = read.getAmbientTemperatur();

        sum = new double[nH*nL][nH*nL];
        sumH_HBC(sum, matrixH, matrixHBC);

        c_dT = new double[nH*nL][nH*nL];


        for(int i=0; i<nH*nL; ++i){
            for(int j=0; j<nH*nL; ++j){
                c_dT[i][j] = matrixC.getGlobalMatrixC()[i][j]/stepTime;
                sum[i][j]+=c_dT[i][j];
            }
        }
    }

    private void sumH_HBC(double[][] sum, MatrixH h, MatrixH_BC_2d hbc){
        for(int i=0; i<nH*nL; ++i){
            for(int j=0; j<nH*nL; ++j){
                sum[i][j] = h.getGlobalMatrixH()[i][j] + hbc.getGlobalMatrixHBC()[i][j];
            }
        }
    }

    public double[][] getSum() {
        return sum;
    }
}
