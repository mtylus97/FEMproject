package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadTXT{

    private double H, L, initialTemperature, simulationTime, stepTime, ambientTemperature;
    private int nH, nL, conductivity, c, ro, alfa;

    public double getH() {
        return H;
    }

    public double getL() {
        return L;
    }

    public int getnH() {
        return nH;
    }

    public int getnL() {
        return nL;
    }

    public int getConductivity() {
        return conductivity;
    }

    public int getC() {
        return c;
    }

    public int getRo() {
        return ro;
    }

    public int getAlfa() {
        return alfa;
    }

    public double getInitialTemperature() {
        return initialTemperature;
    }

    public double getSimulationTime() {
        return simulationTime;
    }

    public double getStepTime() {
        return stepTime;
    }

    public double getAmbientTemperatur() {
        return ambientTemperature;
    }

    public  void read(){

        File file = new File("C:\\Users\\Michal\\IdeaProjects\\projektMES\\src\\data\\dane.txt");
        Scanner in = null;
        try {
            in = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

            H = Double.parseDouble(in.nextLine());
            L = Double.parseDouble(in.nextLine());
            nH = Integer.parseInt(in.nextLine());
            nL = Integer.parseInt(in.nextLine());
            conductivity = Integer.parseInt(in.nextLine());
            c = Integer.parseInt(in.nextLine());
            ro = Integer.parseInt(in.nextLine());
            alfa = Integer.parseInt(in.nextLine());
            initialTemperature = Double.parseDouble(in.nextLine());
            simulationTime = Double.parseDouble(in.nextLine());
            stepTime = Double.parseDouble(in.nextLine());
            ambientTemperature = Double.parseDouble(in.nextLine());
    }

}
