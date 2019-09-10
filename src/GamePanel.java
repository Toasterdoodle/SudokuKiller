import javax.swing.*;
import java.io.*;

public class GamePanel extends JPanel {

    //instance fields
    private int width, height;

    private int[][] testPuzzle1;

    private BufferedReader bufferedReader;

    //constructor
    public GamePanel(int w, int h){

        width = w;
        height = h;

        //initialize test puzzle arrays
        testPuzzle1 = new int[9][9];

        try{
            //replace this with the FULL PATH of the file you intend to read. For some reason, this only works with the full path
            //I could probably try to change this later but as of right now I am too lazy.
            bufferedReader = new BufferedReader(new FileReader("/Users/michael_chen/IdeaProjects/SudokuDestroyer/src/testPuzzle1.txt"));
        }catch(Exception e){
            System.out.println("Invalid file for fileReader");
        }//end try catch

        String[] st = new String[9];

        for (int r = 0; r < testPuzzle1.length; r++) {

            //attempting to read a line in the file
            try{
                st = bufferedReader.readLine().trim().split(" ");
            }catch(Exception e){
                System.out.println("could not read line: " + r);
            }//end try catch

            //attempting to set each line equal to a row
            for (int c = 0; c < testPuzzle1[0].length; c++) {
                testPuzzle1[r][c] = Integer.parseInt(st[c]);
            }//end for

        }//end for

        printTestPuzzle(testPuzzle1);

    }//end GamePanel

    //methods

    //prints the test puzzle
    public void printTestPuzzle(int[][] testPuzzle){

        for (int r = 0; r < testPuzzle.length; r++) {
            for (int c = 0; c < testPuzzle[0].length; c++) {
                System.out.print(testPuzzle[r][c] + " ");
            }//end for
            System.out.println();
        }//end for

    }//end printTestPuzzle

}//end class
