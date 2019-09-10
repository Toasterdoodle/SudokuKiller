import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        //===========Jframe==========
        JFrame frame = new JFrame("SUDOKU DESTROYER");
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        int width = 700;
        int height = 700;
        frame.setPreferredSize(new Dimension(width, height + 22));

        //============JPanel===========
        JPanel panel = new GamePanel(width, height);
        panel.setFocusable(true);
        panel.grabFocus();

        //============JFrame============
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

    }//end psvm

}//end class
