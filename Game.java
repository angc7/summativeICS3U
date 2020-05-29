/**
 * @author      Angela Chen
 * @version     1.6   (current version number of program)
 * @since       1.0   (version of the package this class was first added to)
 */

import javax.swing.*;
import javax.swing.Timer;
import java.awt.Container;
import java.awt.Font;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Game extends JFrame implements ActionListener {

    JButton start;
    JTextArea input;
    private String name = "";
    String str;
    int i = 0;
    int slash;
    int[] scores = new int[100];
    String[] results = new String[100];

/**
* Game constructor, creates leaderboard and lobby GUI                     	
* <p>
* Prompts user input, calls GameBoard once button is pressed. <p>.
*/

    public Game() {
        leaderboard();

        setSize(626, 417);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("SweetSwap Lobby");
        setLayout(null);

        Container c = getContentPane();
        c.setBackground(Color.PINK);
        //setContentPane(new JLabel(new ImageIcon(getClass().getResource("sweetswap.jpg")));

        JLabel leaderboard = new JLabel("", null, JLabel.CENTER);
        leaderboard.setBounds(200, 250, 500, 50);
        leaderboard.setText("Top 3 highscores of all time:");
        c.add(leaderboard);

        JLabel top1 = new JLabel("", null, JLabel.CENTER);
        top1.setBounds(200, 275, 500, 50);
        top1.setText(results[results.length - 1] + " " + scores[scores.length - 1]);
        c.add(top1);

        JLabel top2 = new JLabel("", null, JLabel.CENTER);
        top2.setBounds(200, 300, 500, 50);
        top2.setText(results[results.length - 2] + " " + scores[scores.length - 2]);
        c.add(top2);

        JLabel top3 = new JLabel("", null, JLabel.CENTER);
        top3.setBounds(200, 325, 500, 50);
        top3.setText(results[results.length - 3] + " " + scores[scores.length - 3]);
        c.add(top3);

        start = new JButton("Click to start!");
        start.setBounds(400, 208, 100, 50);
        start.addActionListener(this);
        c.add(start);

        //JtextAreas
        input = new JTextArea("Enter your name here :)");
        input.setBounds(300, 150, 300, 50);
        //input.setFont(new Font("Arial", Font.BOLD, 24));
        input.setLineWrap(true);
        input.setWrapStyleWord(true);
        input.setText("Enter your name here :)");
        c.add(input);

        ImageIcon bg = new ImageIcon(getClass().getResource("sweetswap.jpg"));
        JLabel background = new JLabel("", bg, JLabel.CENTER);
        background.setBounds(0, 0, 626, 417);
        c.add(background);
    }

    /**
* Creates new Game class                       	
* @param args.    	
*/

    public static void main(String[] args) {
        Game game = new Game();
        game.setLocationRelativeTo(null);
        game.setVisible(true);
    }

    /**
* Checks to see if start button has been clicked.
* @param  e click action from user   	
*/

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == start) {

            name = input.getText();
            System.out.println(name);
            GameBoard board = new GameBoard(name);
            getContentPane().add(board);
            this.dispose();

        }

        if (e.getSource() == input) {
            input.setText("");
        }
    }

    /**
* Sorts text file results and uploads top 3 scores to leaderboard.                       	      	
*/

    public void leaderboard() {
        IO.openInputFile("scores.txt");
        try {
            str = IO.readLine(); //read a line of the text file
            while (str != null) { //if something is read

                slash = str.lastIndexOf("/"); //figure out where the name ends
                results[i] = str.substring(0, slash);
                scores[i] = Integer.parseInt(str.substring(slash + 1));
                //System.out.println(results[i]);//checks that the result is correct by printing to console
                // System.out.println(scores[i]);//checks that the scores is properly parsed by printing to console

                i++;
                str = IO.readLine();
            }

        } catch (IOException e) {
            System.out.println("error");
        }
        try {
            IO.closeInputFile();
        } catch (IOException e) {
            System.out.println("error");
        }

        int i, j, currentElement;
        String currentString;
// starting at the second element in the array, sequentially
// place each element in the array in its correct location
        for (i = 1; i < scores.length; i++) {
            j = i;
// store the value of the "current" element
            currentElement = scores[j];
            currentString = results[j];
// determine the correction location (in the sorted part
// of the array) to insert the "current" element
            while ((j > 0) && (scores[j - 1] > currentElement)) {
                scores[j] = scores[j - 1];
                results[j] = results[j - 1];
                j--;
            }
// insert the element into its correct location
            scores[j] = currentElement;
            results[j] = currentString;
            
        }

    } // end insertionSort method

}
