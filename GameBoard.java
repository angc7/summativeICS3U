/**
 * @author      Angela Chen
 * @version     1.6   (current version number of program)
 * @since       1.0   (version of the package this class was first added to)
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameBoard extends JPanel implements ActionListener {

    private int coord1[] = new int[2];
    private int coord2[] = new int[2];

    private int turn = 0;
    private int points = 0;
    private int seconds = 60;
    JLabel time = new JLabel("" + seconds);
    JLabel lbtimer = new JLabel("Timer:");
    JLabel pt = new JLabel("" + points);
    JLabel lbpts = new JLabel("Points:");
    JFrame frame = new JFrame("Sweet Swap!");
    private final int ROW = 8;
    private final int COL = 8;
    private final Timer COUNTDOWN;
    private JButton dessert[][] = new JButton[ROW][COL];
    private String name;
   
    /**
* GameBoard constructor                       	
* <p>
* creates grid with buttons and images, starts timer.
*
* @param  n Name that the user inputted in the Game class      	
*/
    
    public GameBoard(String n) {
        //Set up window

       
        frame.setLayout(new GridLayout(9, COL));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int pic = 0;
        name = n;
        
    //Create Buttons
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                pic = getPic(i, j);
                dessert[i][j].setActionCommand("(" + i + "," + j + "," + pic + ")");
                dessert[i][j].addActionListener(this);
                frame.getContentPane().add(dessert[i][j]);
            }
        }

        //Check for 3 or more in a row
        boolean changev = false, changeh = false;

        do {
            changeh = compHorizontal(0);
            changev = compVertical(0);

            if (changev == true || changeh == true) {
                fillHoles();
            }
        } while (changev == true || changeh == true);

        JPanel status = new JPanel();

        status.add(lbpts);
        status.add(pt);
        pt.setText("" + points);
        status.add(lbtimer);
        status.add(time);
        time.setText("" + points);
        //Add status bar
        frame.add(status);

        //Display window
        frame.pack();
        frame.setSize(1000, 1000);
        frame.setVisible(true);

        TimerTask timer = new TimerTask() {
            
            /**
* This method starts the timer, from 60 to 0                       	
* <p>
* Once the timer hits 0, the game will "end" and call the end lobby class.
*/

            @Override
            public void run() {
                time.setText("" + --seconds);

                if (seconds == 0) {
                    COUNTDOWN.cancel();
                    frame.dispose();
                    GameOver end = new GameOver(name, points);
                }
            }
        };

        COUNTDOWN = new Timer(true);
        COUNTDOWN.schedule(timer, 1000, 1000);
    }

    /**
* This initializes each JButton with a randomized image                      	
* <p>
* Called for every index of the 2D dessert array in a for loop within the constructor.
* @param  x x coordinate of respective button
* @param y y coordinate of respective button
*/

    public int getPic(int x, int y) {
       
        Random generator = new Random();
        int i = 0;

        i = generator.nextInt(7) + 1;
        ImageIcon pic = new ImageIcon(getClass().getResource("icon" + i + ".png"));
        dessert[x][y] = new JButton(pic);
        return i;
    }

    /**
* Checks to see if swap makes a vertical combo/chain                      	
* @param  mode determines whether or not this is the initial checking for patterns
* mode=0 when board has been created and checking for patterns, mode=1 when playing the game and checking for combos
* @return true or false, true means the combo will be eliminated and filled (in another method), 
* false means error message will pop up (in another method)
*/

    public boolean compVertical(int mode) {
        int counter = 0;
        boolean change = false;

        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                if (samePiece(i, j, i + 1, j)) {
                    counter++;
                } else {
                    if (counter > 1) {
                        if (mode == 0) {
                            eliminateRow(i, j, counter, 2);
                        }

                        change = true;
                    }
                    counter = 0;
                }
            }
        }
        return change;
    }

/**
* Checks to see if swap makes a horizontal combo/chain                      	
* @param  mode determines whether or not this is the initial checking for patterns
* mode=0 when board has been created and checking for patterns, mode=1 when playing the game and checking for combos
* @return true or false, true means the combo will be eliminated and filled (in another method), 
* false means error message will pop up (in another method)
*/
    public boolean compHorizontal(int mode) {
        int counter = 0;
        boolean change = false;

       
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (samePiece(i, j, i, j + 1)) {
                    counter++;
                } else {
                    if (counter > 1) {
                        if (mode == 0) {
                            eliminateRow(i, j, counter, 1);
                        }

                        change = true;
                    }
                    counter = 0;
                }
            }
        }

        return change;
    }

    /**
* Checks to see the buttons clicked are the same "type"                      	
* @param x1 x coordinate of 1st button
* @param y1 y coordinate of 1st button
* @param x2 x coordinate of 2nd button
* @param y2 y coordinate of 2nd button
* @return true or false: true continues the length of the "chain", false ends the chain
*/
    public boolean samePiece(int x1, int y1, int x2, int y2) {
        if (x2 > 7 || y2 > 7) {
            return false;
        }

        if (pType(dessert[x1][y1].getActionCommand()) == pType(dessert[x2][y2].getActionCommand())) {
            return true;
        } else {
            return false;
        }
    }

    /**
* Returns integer value of "type", taken from string of the respective button's action command                       	
* @param  tag a button's action command      	
* @return num: the integer value of the button's type
*/
    public int pType(String tag) {
        int num = Integer.parseInt(tag.substring(5, 6));
        return num;
    }

    /**
* Fills the eliminated chains/combos with new randomized images                      	
* @param.      	
*/
    public void fillHoles() {
        int change = 0;

        //Fill the holes with random images
        do {
            change = 0;

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (pType(dessert[i][j].getActionCommand()) == 0) {
                        change++;

                        if (i == 0) {
                            setPic(i, j, 0);
                        } else {
                            changeImage(i, j, i - 1, j);
                        }
                    }
                }
            }

        } while (change != 0);

    }

    /**
* "Eliminates" the combo/chain by clearing the image and modifying the action commands of the buttons in the combo                       	
* <p>
* Once eliminated, the points are added based on length of the chain.
* @param  x used to delete vertical rows (column where chain is)
* @param y used to delete horizontal rows (row where chain is)
* @param num the length of the chain
* @param mode 1 deletes horizontal row, 2 deletes vertical row
*/
    public void eliminateRow(int x, int y, int num, int mode) {

        points += ((num + 1) * 10);
        pt.setText("" + points);

        if (mode == 1) {
            //Eliminate the horizontal row
            for (int i = y; num >= 0; i--, num--) {
                dessert[x][i].setActionCommand("(" + x + "," + i + "," + 0 + ")");
                dessert[x][i].setIcon(null);
            }
        } else {
            //Eliminate the vertical Row
            for (int i = x; num >= 0; i--, num--) {
                dessert[i][y].setActionCommand("(" + i + "," + y + "," + 0 + ")");
                dessert[i][y].setIcon(null);
            }
        }
    }

    /**
* Determines coordinates of 1st and 2nd button clicked to swap                       	
* <p>
* checks if move made a match, if yes, fill chain with new images- if not, displays error message
* checks if there are any possible moves left to be made, if no it will display losing message and end game
* @param  e user clicking action     	
*/
    public void actionPerformed(ActionEvent e) {
        if (turn == 0) {
            coord1 = getXY(e.getActionCommand());
            turn++;
        } else {
            coord2 = getXY(e.getActionCommand());
            changeImage(coord1[0], coord1[1], coord2[0], coord2[1]);

            boolean changev = false, changeh = false;
            int counter = 0;

            do {
                changeh = compHorizontal(0);
                changev = compVertical(0);

                if (changev == true || changeh == true) {
                    fillHoles();
                } else {//Move made no matches
                    if (counter == 0) {
                        JOptionPane.showMessageDialog(null, "Bad Move.", "SweetSwap", JOptionPane.INFORMATION_MESSAGE);
                        //Switch back
                        changeImage(coord1[0], coord1[1], coord2[0], coord2[1]);
                    }

                }
                counter++;
            } while (changev == true || changeh == true);

            //endGame();Test isGameOver function
            //Check for moves. if moves = 0, game over
            if (isGameOver()) {
                JOptionPane.showMessageDialog(null, "You Lose :(", "SweetSwap", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                GameOver end = new GameOver(name, points);
            }

            turn = 0;
        }
    }
/**
* Sets the respective button with a new randomized image and type                      	
* <p>
* Type is indicated and set by action command
* @param  x x coordinate of button
* @param y y coordinate of button
* @param spic type of picture, if 0, means that a new one needs to be randomized and set
*/
    public void setPic(int x, int y, int spic) {
        Random generator = new Random();
        int i = 0;

        if (spic == 0) {
            i = generator.nextInt(7) + 1;
        } else {
            i = spic;
        }
        ImageIcon pic = new ImageIcon(getClass().getResource("icon" + i + ".png"));
        dessert[x][y].setIcon(pic);
        dessert[x][y].setActionCommand("(" + x + "," + y + "," + i + ")");
    }

    /**
* Swaps the images of the 2 buttons clicked if the move is legal                       	
* @param x1 x value of 1st button
* @param y1 y value of 1st button
* @param x2 x value of 2nd button
* @param y2 y value of 2nd button	
*/
    public void changeImage(int x1, int y1, int x2, int y2) {
        int pic1, pic2;

        if (isLegal(x1, y1, x2, y2)) {
            pic1 = pType(dessert[x1][y1].getActionCommand());
            pic2 = pType(dessert[x2][y2].getActionCommand());

            dessert[x1][y1].setActionCommand("(" + x1 + "," + y1 + "," + pic2 + ")");
            dessert[x2][y2].setActionCommand("(" + x2 + "," + y2 + "," + pic1 + ")");

            Icon temp = dessert[x1][y1].getIcon();
            dessert[x1][y1].setIcon(dessert[x2][y2].getIcon());
            dessert[x2][y2].setIcon(temp);
        }
    }
    
/**
* Checks to see if the 2 buttons clicked can be swapped                       	
* <p>
* either on top of each other or next to each other
* @param x1 x value of 1st button
* @param y1 y value of 1st button
* @param x2 x value of 2nd button
* @param y2 y value of 2nd button   	
* @return true: move is legal or false: move is not legal
*/
    public static boolean isLegal(int x1, int y1, int x2, int y2) {
        //Check position
        // Left + Right
        if ((x1 == x2 && y1 + 1 == y2) || (x1 == x2 && y1 - 1 == y2)) {
            return true;
        } //Down + Up
        else if ((x1 - 1 == x2 && y1 == y2) || (x1 + 1 == x2 && y1 == y2)) {
            return true;
        }
        //Else move = illegal

        //Check for Matches
        return false;
    }

    /**
* Gets x and y coordinates of a button using its action command                       	
* <p>
* integer value determined by substring of action command
* @param  dessert the action command of a button     	
* @return returns the coordinates of the action command
*/
    public static int[] getXY(String dessert) {
        int xy[] = new int[2];

        xy[0] = Integer.parseInt(dessert.substring(1, 2));
        xy[1] = Integer.parseInt(dessert.substring(3, 4));
        return xy;
    }

    /**
* Checks all over the board to see if there are any possible moves left to be made in order to make a chain                       	   	
* @return true: there are moves left to be made, false: no more moves left, game over
*/
    public boolean isGameOver() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (checkMove(i, j)) {
                    return false;
                }
            }
        }

        return true;
    }

    
    /**
* Checks to see if 2 buttons being swapped will form a chain, without the user clicking anything                       	
* <p>
* Called in the for loop of isGameOver
*
* @param  x x coordinate of the for loop (theoretical button) passed in 
* @param y y coordinate of the for loop (theoretical button) passed in
* @return true: move can be made, false: no move can be made for that theoretical swap
*/
    public boolean checkMove(int x, int y) {
        boolean moveXh = false, moveXv = false;

        //move up
        if (x != 0) {
            changeImage(x, y, x - 1, y);

            moveXh = compHorizontal(1);
            moveXv = compVertical(1);

            changeImage(x, y, x - 1, y);
        }

        if (moveXh == true || moveXv == true) {
            return true;
        }

        //move down
        if (x != 7) {
            //Switch Images
            changeImage(x, y, x + 1, y);

            moveXh = compHorizontal(1);
            moveXv = compVertical(1);

            //Switch back
            changeImage(x, y, x + 1, y);
        }

        if (moveXh == true || moveXv == true) {
            return true;
        }

        //move left
        if (y != 0) {
            //Switch Images
            changeImage(x, y, x, y - 1);

            moveXh = compHorizontal(1);
            moveXv = compVertical(1);

            //Switch back
            changeImage(x, y, x, y - 1);
        }

        if (moveXh == true || moveXv == true) {
            return true;
        }

        //move right
        if (y != 7) {
            //Switch Images
            changeImage(x, y, x, y + 1);

            moveXh = compHorizontal(1);
            moveXv = compVertical(1);

            //Switch back
            changeImage(x, y, x, y + 1);
        }

        if (moveXh == true || moveXv == true) {
            return true;
        }

        return false;
    }
}
