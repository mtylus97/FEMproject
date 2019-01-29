package grid;

import data.*;

public class Grid {

    private int nH, nL, nh, nE;
    private double H, L;
    public Node node[];
    public Element element[];

    public void genGrid(ReadTXT read){

        int avoidElement = 1; //nie tworzymy elementów co nH
        int columnCounter = 1;

        //tworzenie elementów
        for(int i=0;i<element.length;i++){

            //sprawdzenie, które krwędzie sa na brzegu
            if(i==avoidElement*nH-avoidElement)avoidElement++;

            element[i] = new Element(i+avoidElement,nH);

            if(columnCounter == 1){
                element[i].setIsOnEdge(2, true);
                if(i == 0){
                    element[i].setIsOnEdge(1, true);
                }
                if(i == nH-2){
                    element[i].setIsOnEdge(0, true);
                    element[i].setIsOnEdge(2,true);
                }
            }
            else if(columnCounter == nL-1){
                element[i].setIsOnEdge(3, true);
                if(i == (nH-1)*(nL-1)-1){
                    element[i].setIsOnEdge(0, true);
                }
                if(i == (nH-1)*(nL-1) -(nH-2)-1){
                    element[i].setIsOnEdge(1, true);
                }
            }
            else{
                if(i == avoidElement*nH - avoidElement -1){
                        element[i].setIsOnEdge(0, true);
                }
                if(i == (avoidElement*nH - avoidElement) - (nH-2) -1){
                    element[i].setIsOnEdge(1,true);
                }
            }
            if((i+1)%(nH-1)==0) columnCounter++;
        }

        //tworzenie tablicy node
        for(int i=0;i<node.length;i++){
            node[i]=new Node(read);
        }

        //początkowe współrzędne i delty
        double delX = L/(nL-1);
        double delY = H/(nH-1);
        double x = 0, y = 0;

        //licznik elementów node, uzupełnianie tablicy node o współrzędne
        int counter = 0;
        for(int i=1;i<=nL;i++){
            for(int j=0;j<nH;j++){
                node[counter].setX(x);
                node[counter].setY(y);
                y+=delY;
                counter++;
            }
            y=0;
            x+=delX;
        }
    }

    public void printIsOnEdge(){
        for(int i=0; i<element.length; ++i){
            System.out.print("Element " + (i+1) + " ");
            for(int j=0; j<4; ++j){
                System.out.print( element[i].getIsOnEdge()[j] + " ");
            }
            System.out.println();
        }
    }
    public void printGrid(){
        System.out.println("ELEMENTY:");
        for(int i=0;i<element.length;i++){
            System.out.printf("Dla elementu " + (i+1) + " " ); element[i].printElement();
        }
        System.out.println("\nPUNKTY");
        for(int i=0;i<node.length;++i){
            System.out.printf("Wspolrzedne wezla " + (i+1) + ": "); node[i].printNode();
        }
    }

    public Grid(ReadTXT read) {
        this.nH = read.getnH();
        this.nL = read.getnL();
        this.nh = nH * nL;
        this.nE = (nL - 1) * (nH - 1);
        this.H = read.getH();
        this.L = read.getL();
        this.node = new Node[nh];
        this.element = new Element[nE];
    }

    public Node[] getNode() {
        return node;
    }

    public Element[] getElement() {
        return element;
    }

    public int getnH() {
        return nH;
    }

    public int getnL() {
        return nL;
    }
}
