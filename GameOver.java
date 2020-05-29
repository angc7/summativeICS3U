/**
 * @author      Angela Chen
 * @version     1.3   (current version number of program)
 * @since       1.6   (version of the package this class was first added to)
 */

import java.awt.Color;
import java.awt.Container;
import java.io.*;
import static java.lang.System.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;

public class GameOver extends JFrame {

    String name;
    int points;

    /**
* GameOver constructor                       	
* <p>
* creates JFrame, writes to text file
* @param  n name inputted by user 
* @param p points that the user earned throughout the game
*/
    public GameOver(String n, int p) {
        name = n;
        points = p;

        setLocationRelativeTo(null);
        setVisible(true);
        setSize(626, 417);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("SweetSwap");
        setLayout(null);

        Container c = getContentPane();
        c.setBackground(Color.PINK);
         JLabel results = new JLabel("", null, JLabel.CENTER);
        results.setBounds(150, 200, 500, 50);
        results.setText("Your score: "+points+" points! Exit to play again :)");
        c.add(results);
        
        ImageIcon bg = new ImageIcon(getClass().getResource("gameover.jpg"));
        JLabel background = new JLabel("", bg, JLabel.CENTER);
        background.setBounds(0, 0, 626, 417);
        c.add(background);

      
        try {
//opens file and writes to it, (true) means it will add to the text file
            FileWriter fw = new FileWriter("scores.txt", true);
            PrintWriter fileOut = new PrintWriter(fw);
            fileOut.println(name + "/" + points);
            fileOut.close();

        } catch (IOException e) {
            out.println("File reading/writing error.");
        }
    }
}
