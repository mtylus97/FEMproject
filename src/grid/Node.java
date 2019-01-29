package grid;

import data.ReadTXT;

public class Node {

    private double x, y;

    private double[] temperature;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double[] getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature, int i) {
        this.temperature[i] = temperature;
    }

    void printNode(){
        System.out.println("x=" + this.x + " y=" + this.y);
    }

    public Node(ReadTXT read){
        this.temperature = new double[(int)(read.getSimulationTime()/read.getStepTime())];

    }

}
