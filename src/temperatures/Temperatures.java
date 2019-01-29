package temperatures;

import Jama.Matrix;
import data.ReadTXT;
import grid.Grid;
import matrix.FinalH;
import matrix.MatrixC;

public class Temperatures {

    private Grid grid;

    private double[][] globalH, globalC;

    private double[] t1, t0;
    private double[] vectorP;
    private double[] stepVectorP;

    private double simulationTime;
    private double step;
    private double initialTemperature;

    public Temperatures(Grid grid, FinalH h, MatrixC c, vector.vectorP p, ReadTXT read){
        this.globalH = h.getSum();
        this.globalC = c.getGlobalMatrixC();
        this.vectorP = p.getVectorP();
        this.simulationTime = read.getSimulationTime();
        this.step = read.getStepTime();
        this.initialTemperature = read.getInitialTemperature();
        this.grid = grid;

        t0 = t1 = new double[vectorP.length];
        calculateTemperature(initialTemperature, t0);
        stepVectorP = new double[vectorP.length];

        simulation();
    }

    private void calculateTemperature(double initialTemperature, double[] t){
        for(int i=0;i<t0.length;i++){
            t[i]=initialTemperature;
        }
    }

    //wyliczanie zmiany wektora P dla kolejnych iteracji ( [C]/dtau *{t0} + p )
    private double[] countStepP(double stepTime, double[][] matrixC, double[] t0, double[] vectorP){

        double[] succeedingP = new double[this.vectorP.length];

        for(int i = 0; i< this.vectorP.length; i++) {
            for (int j = 0; j < this.vectorP.length; j++) {
                succeedingP[i] += (matrixC[i][j] / stepTime) * t0[j];
            }
            succeedingP[i] += vectorP[i] ;
        }

        return succeedingP;
    }

    private void findMinMax(double[] temperatures, double step){

        double min = temperatures[0];
        double max = temperatures[0];

        for(int i=0; i<temperatures.length; ++i){
            if(temperatures[i] < min) min = temperatures[i];
            if(temperatures[i] > max) max = temperatures[i];
        }

        System.out.println(step + " " + min + " " + max);
    }

    private double[] countEquation(double[][] finalH, double[] vecP){

        double [] temperaturesVector = new double[vectorP.length];

        Matrix matrixH = new Matrix(finalH);
        Matrix inverseMatrix = matrixH.inverse();
        double[][] inverseMatrixH = inverseMatrix.getArray();

        for(int i=0; i<inverseMatrixH.length; ++i){
            for(int j=0; j<inverseMatrixH[0].length; ++j){
                temperaturesVector[i] += (inverseMatrixH[i][j] * vecP[j]);
            }
        }
        return temperaturesVector;
    }

    private void simulation(){

        double[] tempTemperatures = new double[t0.length];
        calculateTemperature(initialTemperature, tempTemperatures);

        stepVectorP = countStepP( step, globalC, tempTemperatures, vectorP);

        for(int i=0; i<(simulationTime/step); ++i){

            double[] temperature = countEquation(globalH, stepVectorP);

            stepVectorP = countStepP(step, globalC, temperature, vectorP);

            //wpisywanie tenmperatur do tablicy w każdym node
            for(int j=0; j<temperature.length; ++j){
                grid.getNode()[j].setTemperature(temperature[j], i);
            }

            findMinMax(temperature,(i+1) * step);
        }
    }
}