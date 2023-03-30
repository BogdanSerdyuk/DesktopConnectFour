package four;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectFour extends JFrame implements ActionListener {

    private static final int ROWS = 6;
    private static final int COLS = 7;
    private static final Color BASE_COLOR = Color.WHITE;
    private static final Color WIN_COLOR = Color.GREEN;
    private static final String RESET_TEXT = "Reset";

    private boolean isX = true;
    private boolean gameFinished = false;
    private final JButton[][] cells = new JButton[ROWS][COLS];
    private char[][] board = new char[ROWS][COLS];
    private JLabel playerLabel;

    public ConnectFour() {
        super("Connect Four");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel(new GridLayout(ROWS, COLS));

        JPanel labelPanel = new JPanel(new FlowLayout());
        playerLabel = new JLabel("Current Player: X");
        labelPanel.add(playerLabel);
        add(labelPanel, BorderLayout.NORTH);

        for (int i = ROWS - 1; i >= 0; i--) {
            for (int j = 0; j < COLS; j++) {
                cells[i][j] = new JButton();
                cells[i][j].addActionListener(this);
                cells[i][j].setName(String.format("Button%s%d", (char) ('A' + j), i + 1));
                cells[i][j].setFocusPainted(false);
                cells[i][j].setBackground(BASE_COLOR);
                cells[i][j].setText(" ");
                boardPanel.add(cells[i][j]);
            }
        }

        add(boardPanel, BorderLayout.CENTER);

        JPanel resetPanel = new JPanel(new FlowLayout());
        JButton resetButton = new JButton(RESET_TEXT);
        resetButton.setName("ButtonReset");
        resetButton.addActionListener(e -> resetGame());
        resetPanel.add(resetButton);
        add(resetPanel, BorderLayout.SOUTH);

        setSize(700, 600);
        setVisible(true);
    }

    private void resetGame() {
        gameFinished = false;
        isX = true;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = ' ';
                cells[i][j].setBackground(BASE_COLOR);
                cells[i][j].setText(" ");
            }
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameFinished) {
            return;
        }

        JButton clickedButton = (JButton) e.getSource();
        String name = clickedButton.getName();
        System.out.println(name);
        int col = name.charAt(6) - 'A';
        System.out.println("Column number: " + col); // get the column number from the button name
        int row = findEmptyRow(col); // find the first empty row in the column
        if (row >= 0) {
            char piece = isX ? 'X' : 'O';
            board[row][col] = piece; // update the game board
            cells[row][col].setText(String.valueOf(piece)); // display the piece in the correct cell
            cells[row][col].setBackground(BASE_COLOR); // reset the background color to the base color

            // check for a win
            if (checkWin(piece, row, col)) {
                markWin(piece, row, col);
                gameFinished = true;
                JOptionPane.showMessageDialog(this, "Player " + piece + " has won!");
                return;
            }

            isX = !isX; // switch turns

            // update the player label
            playerLabel.setText("Current Player: " + (isX ? "X" : "O"));
        }
    }

    private int findEmptyRow(int col) {
        for (int row = 0; row < ROWS; row++) {
            if (cells[row][col].getText().equals(" ")) {
                return row;
            }
        }
        return -1;
    }

    private boolean checkWin(char piece, int row, int col) {
        int count = 0;

        // check horizontally
        for (int i = Math.max(0, col - 3); i <= Math.min(COLS - 1, col + 3); i++) {
            if (board[row][i] == piece) {
                count++;
            } else {
                count = 0;
            }
            if (count == 4) {
                return true;
            }
        }

        count = 0;

        // check vertically
        for (int i = Math.max(0, row - 3); i <= Math.min(ROWS - 1, row + 3); i++) {
            if (board[i][col] == piece) {
                count++;
            } else {
                count = 0;
            }
            if (count == 4) {
                return true;
            }
        }

        count = 0;

        // check diagonally (top-left to bottom-right)
        int i = row - Math.min(row, col);
        int j = col - Math.min(row, col);
        while (i < ROWS && j < COLS) {
            if (board[i][j] == piece) {
                count++;
            } else {
                count = 0;
            }
            if (count == 4) {
                return true;
            }
            i++;
            j++;
        }

        count = 0;

        // check diagonally (bottom-left to top-right)
        i = row + Math.min(ROWS - 1 - row, col);
        j = col - Math.min(ROWS - 1 - row, col);
        while (i >= 0 && j < COLS) {
            if (board[i][j] == piece) {
                count++;
            } else {
                count = 0;
            }
            if (count == 4) {
                return true;
            }
            i--;
            j++;
        }

        return false;
    }

    private void markWin(char piece, int row, int col) {
        // mark the winning pieces with the win color
        int count = 0;

        // mark horizontally
        for (int i = Math.max(0, col - 3); i <= Math.min(COLS - 1, col + 3); i++) {
            if (board[row][i] == piece) {
                count++;
            } else {
                count = 0;
            }
            if (count == 4) {
                for (int j = i - 3; j <= i; j++) {
                    cells[row][j].setBackground(WIN_COLOR);
                }
                return;
            }
        }

        count = 0;

        // mark vertically
        for (int i = Math.max(0, row - 3); i <= Math.min(ROWS - 1, row + 3); i++) {
            if (board[i][col] == piece) {
                count++;
            } else {
                count = 0;
            }
            if (count == 4) {
                for (int j = i - 3; j <= i; j++) {
                    cells[j][col].setBackground(WIN_COLOR);
                }
                return;
            }
        }

        count = 0;

        // mark diagonally (top-left to bottom-right)
        int i = row - Math.min(row, col);
        int j = col - Math.min(row, col);
        while (i < ROWS && j < COLS) {
            if (board[i][j] == piece) {
                count++;
            } else {
                count = 0;
            }
            if (count == 4) {
                for (int k = 0; k < 4; k++) {
                    cells[row-k][col-k].setBackground(WIN_COLOR);
                }
                return;
            }
            i++;
            j++;
        }

        count = 0;

// mark diagonally (bottom-left to top-right)
        i = row + Math.min(ROWS - 1 - row, col);
        j = col - Math.min(ROWS - 1 - row, col);
        while (i >= 0 && j < COLS) {
            if (board[i][j] == piece) {
                count++;
            } else {
                count = 0;
            }
            if (count == 4) {
                for (int k = 0; k < 4; k++) {
                    cells[row-k][col+k].setBackground(WIN_COLOR);
                }
                return;
            }
            i--;
            j++;
        }
    }

}


