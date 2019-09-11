import java.util.ArrayList;

public class Cell {

    //==========instance fields==========

    private boolean given;

    //represents the current value of the cell
    private int curVal;

    //row and col
    private int r, c;

    //contains all the possible values of the cell
    private ArrayList<Integer> posVals = new ArrayList<>();

    //contains all the values that the cell cannot be
    private ArrayList<Integer> cantVals = new ArrayList<>();

    //==========constructor==========

    public Cell(int curVal, int r, int c){

        this.r = r;
        this.c = c;

        this.curVal = curVal;

        //initialize posVals
        for (int i = 1; i < 10; i++) {
            posVals.add(i);
        }//end for

    }//end Cell

    //==========methods==========

    public void printPosVals(){
        for (int i = 0; i < posVals.size(); i++) {
            System.out.println(posVals.get(i));
        }//end for
    }//end printPosVals

    public void removePosVal(int i){

        posVals.remove(i);

    }//end removePosVal
    
    public void resetPosVals(){

        posVals.clear();

        for (int i = 1; i < 10; i++) {
            posVals.add(i);
        }//end for

//        System.out.println("Posvals reset at (" + r + ", " + c + ")");

        //printing posvals
//        System.out.println("Newly Reset PosVals: ");
//        for (int h = 0; h < posVals.size(); h++) {
//            System.out.print(posVals.get(h) + ", ");
//        }//end for
//        System.out.println();
        
    }//end resetPosVals

    public void resetCantVals(){

        cantVals.clear();
        System.out.println("cantVals cleared at (" + r + ", " + c + ")");

    }//end resetCantVals

    public void addCantVals(int i){

        cantVals.add(i);

    }//end addCantVals

    public void removePosValsFromCantVals(){

        for (int i = 0; i < cantVals.size(); i++) {
            posVals.remove(Integer.valueOf(cantVals.get(i)));
        }//end for

    }//end removePosValsFromCantVals

    public void updateGivenStatus(){

        if(curVal != 0){

            given = true;
            posVals.clear();

        }//end if

    }//end updateGivenStatus

    //getters and setters
    public int getCurVal() { return curVal; }

    public void setCurVal(int curVal) { this.curVal = curVal; }

    public ArrayList<Integer> getPosVals() { return posVals; }

    public int getR() { return r; }

    public int getC() { return c; }

    public boolean isGiven() { return given; }

    public ArrayList<Integer> getCantVals() { return cantVals; }

}//end class
