import javax.swing.*;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
class connect4gui{
    public static void main(String args[]){
      //Create the base of the connect 4 window
        JFrame frame = new JFrame("Connect4");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,600);

        //add Menu, main panel and both sub panels, and all column buttons for gameplay
        JMenuBar menu = new JMenuBar();
        JPanel mainPanel = new JPanel();
        JPanel game = new JPanel();
        JPanel gameBoard = new JPanel();
        JMenuItem connect = new JMenuItem("Connect");
        JMenuItem exit = new JMenuItem("Exit");
        JButton col0 = new JButton("0");
        JButton col1 = new JButton("1");
        JButton col2 = new JButton("2");
        JButton col3 = new JButton("3");
        JButton col4 = new JButton("4");
        JButton col5 = new JButton("5");
        JButton col6 = new JButton("6");

        //add menu and column select buttons to their panels
        menu.add(connect);
        menu.add(exit);
        game.add(col0);
        game.add(col1);
        game.add(col2);
        game.add(col3);
        game.add(col4);
        game.add(col5);
        game.add(col6);

        //create button array
        JButton[][] button = new JButton[6][7];

        //set layout for the button board
        gameBoard.setLayout(new GridLayout(6,7));

        //create array of buttons and allign them in a board
        for (int i = 0; i < 6; i++) {
          for (int j = 0; j < 7; j++) {
              button[i][j] = new JButton(String.valueOf(i).concat(String.valueOf(j)));
              button[i][j].setEnabled(false);
              gameBoard.add(button[i][j]);
          }
        }

        //set grid layouts for the main screen
        game.setLayout(new GridLayout(1,7));
        frame.setJMenuBar(menu);
        mainPanel.setLayout(new GridBagLayout());

        //add constraints to format the screen
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weighty = .1;
        constraints.weightx = 1;
        mainPanel.add(game, constraints);
        constraints.gridy = 1;
        constraints.weighty = 1; // This fills the screen, delete to add some space
        constraints.fill = GridBagConstraints.BOTH;

        //add the main panel to the frame
        mainPanel.add(gameBoard, constraints);

        //set content and make visible
        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }
}