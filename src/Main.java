import data.ReadTXT;
import grid.Grid;
import jacobian.Jacobian;
import matrix.FinalH;
import matrix.MatrixC;
import matrix.MatrixH;
import matrix.MatrixH_BC_2d;
import vector.vectorP;

public class Main {

    public static void main(String[] args) {

        //wczytanie z pliku
        ReadTXT read = new ReadTXT();
        read.read();

        //siatka MES
        Grid grid = new Grid(read);
        grid.genGrid(read);
        //grid.printGrid();

        //Jakobian
        Jacobian jak = new Jacobian(grid);

        //Macierz H
        MatrixH matrixH = new MatrixH(jak, read);
        //matrixH.printMatrix(matrixH.getGlobalMatrixH());
        System.out.println();

        //Macierz C
        MatrixC matrixC= new MatrixC(jak, read);
        //matrixC. printMatrix(matrixC.getGlobalMatrixC());
        System.out.println();
        matrixC.printMatrix(matrixC.getMatrixCpc());
        System.out.println();
        matrixC.printMatrix(matrixC.getMatrixC());
        System.out.println();
        matrixC.printMatrix(matrixC.getGlobalMatrixC());
        System.out.println();
        MatrixH_BC_2d hbc = new MatrixH_BC_2d(jak, read);
     //  hbc.printMatrix(hbc.getGlobalMatrixHBC());

        FinalH finalh = new FinalH(read, matrixH, hbc, matrixC);
       // finalh.printMatrix(finalh.getSum());

        vectorP v = new vectorP(read, jak, hbc);
       // v.printVecP();
        System.out.println();
        //Temperatures t = new Temperatures(grid, finalh, matrixC, v, read);


        /*System.out.println("\nnode temperatures");
        for(int i=0; i<grid.getNode().length; ++i){
            for(int j=0; j<grid.getNode()[i].getTemperature().length; ++j)
            System.out.print(grid.getNode()[i].getTemperature()[j] + " ");
            System.out.println();
        }*/

    }
}