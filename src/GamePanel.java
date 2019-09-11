import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;

public class GamePanel extends JPanel {

    //==========instance fields==========
    private int width, height, boxwidth, boxheight;

    private Cell[][] testpuzzle;

    private BufferedReader bufferedReader;

    private Timer timer;

    private ActionListener action;

    private int fps = 1000;

    //the following variables will be used by the timer:
    private int index = 0;
    private int loopR, loopC;
    private int loopcounter = 0;
    private int newIndex, prevRow, prevCol;

    //==========constructor==========
    public GamePanel(int w, int h){

        width = w;
        height = h;
        boxwidth = w/9;
        boxheight = h/9;

        //initialize test puzzle arrays
        testpuzzle = new Cell[9][9];

        //initialize the array with new cells
        //not needed due to initializing in filereader
//        for (int r = 0; r < testpuzzle.length; r++) {
//            for (int c = 0; c < testpuzzle[0].length; c++) {
//
//                testpuzzle[r][c] = new Cell();
//
//            }//end for
//        }//end for

        try{
            //replace this with the FULL PATH of the file you intend to read. For some reason, this only works with the full path
            //I could probably try to change this later but as of right now I am too lazy.
            //TODO: find a way to use the relative path
            bufferedReader = new BufferedReader(new FileReader("/Users/michael_chen/IdeaProjects/sudokuKiller/src/testPuzzle2.txt"));
        }catch(Exception e){
            System.out.println("Invalid file for fileReader");
        }//end try catch

        String[] st = new String[9];

        for (int r = 0; r < testpuzzle.length; r++) {

            //attempting to read a line in the file
            try{
                st = bufferedReader.readLine().trim().split(" ");
            }catch(Exception e){
                System.out.println("could not read line: " + r);
            }//end try catch

            //set the values of the Cells in the array equal to the parsed ints
            for (int c = 0; c < testpuzzle[0].length; c++) {
                testpuzzle[r][c] = new Cell(Integer.parseInt(st[c]), r, c);
            }//end for

        }//end for

        //initializing given status
        for (int r = 0; r < testpuzzle.length; r++) {
            for (int c = 0; c < testpuzzle.length; c++) {
                testpuzzle[r][c].updateGivenStatus();
            }//end for
        }//end for

        printPuzzle(testpuzzle);

        updatePosVals();

        initTimer();

        timer.start();

//        testpuzzle[0][2].addCantVals(1);
//        testpuzzle[0][2].removePosValsFromCantVals();
//        updatePosVals();
//        crack();

    }//end GamePanel

    //==========methods==========

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        g2.setStroke(new BasicStroke(2));

        for (int r = 0; r < testpuzzle.length; r++) {
            for (int c = 0; c < testpuzzle[0].length; c++) {
                g2.setColor(new Color(0, 0, 0));
                g2.drawRect(c*boxwidth, r*boxheight, boxwidth, boxheight);
                if(testpuzzle[r][c].getCurVal() == 0){
                    drawPosVals(g2, testpuzzle[r][c]);
                }else{
                    g2.setColor(new Color(0, 0, 0));
                    g2.setFont(new Font("Comic Sans", Font.BOLD, 64));
                    g2.drawString(String.valueOf(testpuzzle[r][c].getCurVal()), c*width/9+14, r*height/9+60);
                }//end else
            }//end for
        }//end for

        g2.setStroke(new BasicStroke(7));
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                g2.drawRect(c*boxwidth*3, r*boxheight*3, boxwidth*3, boxheight*3);
            }//end for
        }//end for

    }//end paintComponent

    public void drawPosVals(Graphics2D g2, Cell cell){
        g2.setFont(new Font("Comic Sans", Font.BOLD, 24));
        g2.setColor(new Color(161, 7, 0));

        for (int i = 0; i < cell.getPosVals().size(); i++) {
            //disclaimer: r represents the r within the square, not within the actual sudoku puzzle
            int r = i/3;
            int c = i%3;
            g2.drawString(String.valueOf(cell.getPosVals().get(i)), cell.getC()*boxwidth+c*20+5, cell.getR()*boxheight+r*20+25);
        }//end for

    }//end drawPosVals

    //----------------------------------------
    //!!! DEPRECATED, NOW USING TIMER INSTEAD !!!
    //This method is actually a lot faster than the timer since it doesn't need to keep repainting
    //but it looks a lot less cool.
    //----------------------------------------
    //recursive method designed to keep checking values
    //crack only uses one index, i, instead of the two r and c values
    //it just counts upward from top left to bottom right
    //ex: 0,0 = 0 and 0,1 = 1 and 0,2 = 2
    //----------------------------------------
    //note:
    //valsToRemove is an interesting list
    //it compiles a list of every attempted and failed possible value
    //----------------------------------------
//    public void crack(){
//
//        //all of this gets run only at the beginning
//        System.out.println("====================");
//
//        int i = 0;
//        int r;
//        int c;
//        int newI;
//        int prevC;
//        int prevR;
//
//        //checking if you haven't won the game yet
//        //TODO: change this to a timer instead of a while loop so that repaint will work
//        //==========!!!LOOP BEGINS HERE!!!==========
//        while(i < 81) {
//
//            updatePosVals();
//
//            r = i/9;
//            c = i%9;
//
//            System.out.println("i: " + i);
//            System.out.println("coords: (" + r + ", " + c + ")");
//
//            if (testpuzzle[r][c].getCurVal() == 0) {
//
//                //printing cantvals
//                System.out.print("Can't Vals: ");
//                for (int j = 0; j < testpuzzle[r][c].getCantVals().size(); j++) {
//                    System.out.print(testpuzzle[r][c].getCantVals().get(j) + ", ");
//                }//end for
//                System.out.println();
//
//                //removing cantvals from list of posvals
//                //yes the name is backwards I know
//                //I'm too lazy to go and fix it
//                testpuzzle[r][c].removePosValsFromCantVals();
//
//                //printing posvals
//                System.out.print("PosVals: ");
//                for (int h = 0; h < testpuzzle[r][c].getPosVals().size(); h++) {
//                    System.out.print(testpuzzle[r][c].getPosVals().get(h) + ", ");
//                }//end for
//                System.out.println();
//
//                //checking if there are still possible values for this integer
//                if (testpuzzle[r][c].getPosVals().size() != 0) {
//
//                    System.out.println("Value detected! Setting value at (" + r + ", " + c + ")");
//
//                    //sets the current value of the cell equal to the first possible value
//                    testpuzzle[r][c].setCurVal(testpuzzle[r][c].getPosVals().get(0));
//
//                    printPuzzle(testpuzzle);
//
//                    //changes the value of i, and then while loop should cycle here
//                    i = i+1;
//
//                } else {
//                    System.out.println("No possible values detected at (" + r + ", " + c + ")" + " Reverting to previous cell.");
//                    //if there are no possible values for the selected square, you have to go back
//
//                    //reverting to previous index and calculating new cell index
//                    newI = i-1;
//                    prevR = newI/9;
//                    prevC = newI%9;
//
//                    System.out.println("Is previous value given: " + testpuzzle[prevR][prevC].isGiven());
//
//                    //looks for a non-given cell
//                    while(testpuzzle[prevR][prevC].isGiven() == true){
//
//                        System.out.println("looking for non-given value...");
//
//                        newI = newI-1;
//                        prevR = newI/9;
//                        prevC = newI%9;
//
//                        System.out.println("newI: " + newI);
//
//                    }//end while
//
//                    System.out.println("newi: " + newI);
//
//                    //resetting stuff
//
//                    //previous value cannot be tested value since it leads to a dead end
//                    //therefore, the cantvals of the given cell is updated
//                    testpuzzle[prevR][prevC].addCantVals(testpuzzle[prevR][prevC].getCurVal());
//
//                    //erasing the previous cell
//                    testpuzzle[prevR][prevC].setCurVal(0);
//
//                    //resetting the can't vals of the tested square
//                    testpuzzle[r][c].resetCantVals();
//
//                    //resetting all possible values due to a recycling
//                    //could probably work with just resetting the values of (r,c) and (rPrev, cPrev)
//                    //But I'm too lazy to check
//                    resetAllPosVals();
//
//                    i = newI;
//
//                }//end if else
//
//            }else{
//
//                i = i+1;
//
//            }//end if else
//
//        }//end if
//
//        System.out.println("Finished Cracking");
//
//    }//end crack

    public void initTimer(){

        //end actionListener
        action = e -> {

            System.out.println(loopcounter);
            loopcounter++;

            if(index < 81){

                repaint();
                updatePosVals();
                loopR = index/9;
                loopC = index%9;

                if (testpuzzle[loopR][loopC].getCurVal() == 0) {

                    testpuzzle[loopR][loopC].removePosValsFromCantVals();

                    if (testpuzzle[loopR][loopC].getPosVals().size() != 0) {

                        testpuzzle[loopR][loopC].setCurVal(testpuzzle[loopR][loopC].getPosVals().get(0));
                        index++;

                    }else{

                        newIndex = index-1;
                        prevRow = newIndex/9;
                        prevCol = newIndex%9;

                        while(testpuzzle[prevRow][prevCol].isGiven() == true){

                            newIndex = newIndex-1;
                            prevRow = newIndex/9;
                            prevCol = newIndex%9;

                        }//end while

                        testpuzzle[prevRow][prevCol].addCantVals(testpuzzle[prevRow][prevCol].getCurVal());
                        testpuzzle[prevRow][prevCol].setCurVal(0);
                        testpuzzle[loopR][loopC].resetCantVals();
                        resetAllPosVals();

                        index = newIndex;

                    }//end else

                }else{

                    index = index+1;

                }//end if else

            }else{

                System.out.println("Finished Cracking");
                printPuzzle(testpuzzle);
                timer.stop();

            }//end if else

        };//end action

        timer = new Timer(1000/fps, action);

    }//end initTimer

    public void resetAllPosVals(){

        for (int r = 0; r < testpuzzle.length; r++) {
            for (int c = 0; c < testpuzzle[0].length; c++) {

                testpuzzle[r][c].resetPosVals();

            }//end for
        }//end for

    }//end resetAllPosVals

    //prints the test puzzle
    public void printPuzzle(Cell[][] puzzle){

        for (int r = 0; r < puzzle.length; r++) {
            for (int c = 0; c < puzzle[0].length; c++) {
                System.out.print(puzzle[r][c].getCurVal() + " ");
            }//end for
            System.out.println();
        }//end for
        System.out.println();

    }//end printTestPuzzle

    //----------

    public void updatePosVals(){

        for (int r = 0; r < testpuzzle.length; r++) {
            for (int c = 0; c < testpuzzle[0].length; c++) {

                if(testpuzzle[r][c].isGiven() == false) {
                    //cycle through the posval list of the Cell at the index
                    //and then tests each to see if they are still a possible value
                    for (int i = 0; i < testpuzzle[r][c].getPosVals().size(); i++) {
                        if (checkVals(testpuzzle[r][c].getPosVals().get(i), testpuzzle[r][c]) == true) {
                            testpuzzle[r][c].removePosVal(i);
                            i--;
                        }//end if
                    }//end for
                }//end if
//                else{
//                    System.out.println("UPDATE SKIP: " + r + ", " + c);
//                }

            }//end for
        }//end for

    }//end updatePosVals

    //----------

    //checks if a cell's posvals list contains a certain val
    public boolean checkVals(int val, Cell cell){

        if(checkRow(val, cell) == true){
            return true;
        }else if(checkCol(val, cell) == true){
            return true;
        }else if(checkBox(val, cell) == true){
            return true;
        }else{
            return false;
        }//end if chain

    }//end checkVal

    //----------

    //checks the row for the value val
    public boolean checkRow(int val, Cell cell){

        for (int c = 0; c < testpuzzle[0].length; c++) {

            if(testpuzzle[cell.getR()][c].getCurVal() == val){
                return true;
            }//end if

        }//end for

        return false;

    }//end checkRow

    //----------

    public boolean checkCol(int val, Cell cell){

        for (int r = 0; r < testpuzzle.length; r++) {

            if(testpuzzle[r][cell.getC()].getCurVal() == val){
                return true;
            }//end if

        }//end for

        return false;

    }//end checkCol

    //----------

    public boolean checkBox(int val, Cell cell){

        for (int r = cell.getR()/3 * 3; r < cell.getR()/3 * 3 + 3; r++) {
            for (int c = cell.getC()/3 * 3; c < cell.getC()/3 * 3 + 3; c++) {
                if(testpuzzle[r][c].getCurVal() == val){
                    return true;
                }//end if
            }//end for
        }//end for

        return false;

    }//end checkCol

}//end class
