package jacobian;

import grid.*;
import matrix.Matrix;

public class Jacobian extends Matrix {

    protected Element element;
    protected int idLength;

    protected int numberOfElements;

    // [element][elementID][coordinate 0=x, 1=y]
    protected double[][][] cooridinates;

    protected Grid grid;

    //[element][ilość punktów][0=x, 1=y]
    protected double integrationPoints [][][];

    //wartośći stałe
    protected final double[] ksi = {(-1/Math.sqrt(3)), (1/Math.sqrt(3)), (1/Math.sqrt(3)), (-1/Math.sqrt(3))};
    protected final double[] eta = {(-1/Math.sqrt(3)), (-1/Math.sqrt(3)), (1/Math.sqrt(3)), (1/Math.sqrt(3))};

    protected final double[][] N = {
            {(0.25*(1-ksi[0])*(1-eta[0])), (0.25*(1-ksi[1])*(1-eta[1])), (0.25*(1-ksi[2])*(1-eta[2])), (0.25*(1-ksi[3])*(1-eta[3])) }, //N1
            {(0.25*(1+ksi[0])*(1-eta[0])), (0.25*(1+ksi[1])*(1-eta[1])), (0.25*(1+ksi[2])*(1-eta[2])), (0.25*(1+ksi[3])*(1-eta[3])) }, //N2
            {(0.25*(1+ksi[0])*(1+eta[0])), (0.25*(1+ksi[1])*(1+eta[1])), (0.25*(1+ksi[2])*(1+eta[2])), (0.25*(1+ksi[3])*(1+eta[3])) }, //N3
            {(0.25*(1-ksi[0])*(1+eta[0])), (0.25*(1-ksi[1])*(1+eta[1])), (0.25*(1-ksi[2])*(1+eta[2])), (0.25*(1-ksi[3])*(1+eta[3])) }}; //N4

    //pochodne
    protected double[][] dN_dEta;
    protected double[][] dN_dKsi;


    protected double[][][] Jacobian;
    protected double[][] DetJ;
    protected double[][][] JacobianReciprocal;


    public Jacobian(){}

    public Jacobian(Grid grid){
        this.grid = grid;
        this.idLength = grid.element[0].getId().length;
        this.numberOfElements = grid.element.length;


        //stworzenie i uzupełnienie tablicy współrzędnych
        cooridinates = new double[numberOfElements][4][2];
        fillCoordinates(cooridinates);

        //stworzenie i uzupełnienie tablicy punktów całkowania
        integrationPoints = new double[numberOfElements][idLength][2];
        fillIntegrationPoints(integrationPoints);

        //stworzenie i uzupełnienie pochodnych
        dN_dEta = new double [N.length][idLength];
        dN_dKsi = new double [N.length][idLength];
        createDN(dN_dEta,dN_dKsi);

        //Jakboian dla wszystkich elementów
        Jacobian = new double [numberOfElements][idLength][idLength];
        fillJacobi();

        //Wyznacznik Jakobianu
        DetJ = new double[numberOfElements][idLength];
        fillDetJ(DetJ);

        //Odwrotność Jakobianu
        JacobianReciprocal = new double[numberOfElements][idLength][idLength];
        fillJacobianReciprocal(JacobianReciprocal);

    }

    //METODY UZUPEŁNIAJĄCE
    private void fillCoordinates(double[][][] cooridinates){
        for(int i=0;i<grid.element.length; ++i){
            //przypisywanie co by krótszy zapis w pętli był
            element=grid.element[i];
            for(int j=0;j<idLength;++j){
                cooridinates[i][j][0]=grid.node[element.getId()[j]-1].getX();
                cooridinates[i][j][1]=grid.node[element.getId()[j]-1].getY();
            }
        }
    }

    private void fillIntegrationPoints(double integrationPoints [][][]){
        for(int i=0; i<numberOfElements; ++i){
            for(int j=0; j<idLength; ++j){
                integrationPoints[i][j][0]=
                        N[0][j]*cooridinates[i][0][0]+N[1][j]*cooridinates[i][1][0]+
                                N[2][j]*cooridinates[i][2][0]+N[3][j]*cooridinates[i][3][0];
                integrationPoints[i][j][1]=
                        N[0][j]*cooridinates[i][0][1]+N[1][j]*cooridinates[i][1][1]+
                                N[2][j]*cooridinates[i][2][1]+N[3][j]*cooridinates[i][3][1];
            }
        }
    }

    private void createDN(double [][]dN_dEta, double [][]dN_dKsi){
        for(int j=0; j<N.length; ++j){
            for(int k=0; k<idLength; ++k){
                if(k==0) {
                    dN_dKsi[k][j] = -0.25*(1-eta[j]);
                    dN_dEta[k][j] = -0.25*(1-ksi[j]);
                }
                else if(k==1){
                    dN_dKsi[k][j] = 0.25*(1-eta[j]);
                    dN_dEta[k][j] = -0.25*(1+ksi[j]);
                }
                else if(k==2){
                    dN_dKsi[k][j] = 0.25*(1+eta[j]);
                    dN_dEta[k][j] = 0.25*(1+ksi[j]);
                }
                else{
                    dN_dKsi[k][j] = -0.25*(1+eta[j]);
                    dN_dEta[k][j] = 0.25*(1-ksi[j]);
                }
            }
        }
    }

    private void fillJacobi(){
        for(int i=0; i<numberOfElements; ++i){
            for(int j=0; j<idLength; ++j){
                for(int k=0; k<idLength; ++k){
                    if (j == 0) {
                        Jacobian[i][j][k] =
                                dN_dKsi[0][k] * cooridinates[i][0][0] +
                                        dN_dKsi[1][k] * cooridinates[i][1][0] +
                                        dN_dKsi[2][k] * cooridinates[i][2][0] +
                                        dN_dKsi[3][k] * cooridinates[i][3][0];
                    } else if(j == 1) {
                        Jacobian[i][j][k] =
                                dN_dKsi[0][k] * cooridinates[i][0][1] +
                                        dN_dKsi[1][k] * cooridinates[i][1][1] +
                                        dN_dKsi[2][k] * cooridinates[i][2][1] +
                                        dN_dKsi[3][k] * cooridinates[i][3][1];
                    }
                    else if(j == 2){
                        Jacobian[i][j][k]=
                                dN_dEta[0][k] * cooridinates[i][0][0] +
                                        dN_dEta[1][k] * cooridinates[i][1][0] +
                                        dN_dEta[2][k] * cooridinates[i][2][0] +
                                        dN_dEta[3][k] * cooridinates[i][3][0];
                    }
                    else{
                        Jacobian[i][j][k]=
                                dN_dEta[0][k]*cooridinates[i][0][1]+
                                        dN_dEta[1][k]*cooridinates[i][1][1]+
                                        dN_dEta[2][k]*cooridinates[i][2][1]+
                                        dN_dEta[3][k]*cooridinates[i][3][1];
                    }

                }
            }
        }
    }

    private void fillDetJ(double[][] DetJ){
        for(int i=0; i<numberOfElements; ++i){
            for(int j=0; j<idLength; ++j){
                DetJ[i][j]=Jacobian[i][0][j]*Jacobian[i][3][j] - Jacobian[i][1][j]*Jacobian[i][2][j];
            }
        }
    }

    //do sprawdzenia
    private void fillJacobianReciprocal(double[][][] JacobianReciprocal){
        for(int i=0; i<numberOfElements; ++i){
            for(int j=0;j<idLength; ++j){
                for(int k=0; k<idLength; ++k){
                    JacobianReciprocal[i][k][j]=Jacobian[i][idLength-k-1][j]/DetJ[i][k];
                }
            }
        }
    }

    //METODY DRUKUJĄCE
    protected void printCoordinates(){

        System.out.println("punkty w jakobianie:");
        for(int i=0;i<numberOfElements;++i){
            System.out.println("coordinates for elemennt: " + (i+1));
            for(int j=0;j<idLength;++j){
                System.out.println("wezel: " + grid.element[i].getId()[j] + " x: " + cooridinates[i][j][0] + " y: " + cooridinates[i][j][1] );
            }
        }
    }

    protected void printIntegrationPoints(){
        for(int i=0; i<integrationPoints.length;++i){
            System.out.println("punkty calkowania dla elementu: " + (i+1));
            for(int j=0; j<idLength; ++j){
                System.out.print("x: " + integrationPoints[i][j][0] + " y: " + integrationPoints[i][j][1] + " ; ");
            }
            System.out.println();
        }
    }

    public void printN(){
        for(int i=0; i<4; ++i){
            for(int j=0; j<4;++j){
                System.out.print("N" + i + " "  + N[i][j] + " ");
            }
            System.out.println();
        }
    }

    protected void printDN(){
        System.out.println("Deta");
        for(int i=0; i<4; ++i){
            for(int j=0;j<4;++j){
                System.out.print(dN_dEta[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("Dksi");
        for(int i=0; i<4; ++i){
            for(int j=0;j<4;++j){
                System.out.print(dN_dKsi[i][j] + " ");
            }
            System.out.println();
        }
    }


    void printDetJ(){
        for(int j=0;j<numberOfElements; ++j) {
            for (int i = 0; i < idLength; ++i) {
                System.out.print(DetJ[j][i] + " ");
            }
            System.out.println();
        }
    }



    //GETTERY
    public int getNumberOfElements() {
        return numberOfElements;
    }
    public double[] getKsi() {
        return ksi;
    }
    public double[] getEta() {
        return eta;
    }
    public double[][] getdN_dEta() {
        return dN_dEta;
    }
    public double[][] getdN_dKsi() {
        return dN_dKsi;
    }
    public double[][][] getJacobian() {
        return Jacobian;
    }
    public double[][] getDetJ() {
        return DetJ;
    }
    public int getIdLength() {
        return idLength;
    }
    public double[][][] getJacobianReciprocal() {
        return JacobianReciprocal;
    }
    public double[][] getN() {
        return N;
    }

    public double[][][] getCooridinates() {
        return cooridinates;
    }

    public Grid getGrid() {
        return grid;
    }
}
