package grid;

public class Element {

    private int[] id = new int [4];

    //0 - gora, 1 - dol, 2 - lewa, 3 - prawa
    private boolean[] isOnEdge = new boolean[4];

    public int[] getId() {
        return id;
    }

    Element(int first, int nH){
        this.id[0] = first;
        this.id[1] = first + nH;
        this.id[2] = first + nH + 1;
        this.id[3] = first + 1;

        for(int i=0; i<4; ++i){
            setIsOnEdge(i, false);
        }
    }

    public void printElement(){
        System.out.println("Id wezlow: " + " ["  + this.id[0] + " " + this.id[1] + " " + this.id[2] + " " + this.id[3] + "]");
    }

    public boolean[] getIsOnEdge() {
        return isOnEdge;
    }

    public void setIsOnEdge(int position, boolean isOnEdge) {
        this.isOnEdge[position] = isOnEdge;
    }
}
