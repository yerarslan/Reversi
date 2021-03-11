package reversi;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 * CSCI1130 Java Assignment
 * Reversi board game
 * 
 * Students shall complete this class to implement the game.
 * There are debugging, testing and demonstration code for your reference.
 * 
 * I declare that the assignment here submitted is original
 * except for source material explicitly acknowledged,
 * and that the same or closely related material has not been
 * previously submitted for another course.
 * I also acknowledge that I am aware of University policy and
 * regulations on honesty in academic work, and of the disciplinary
 * guidelines and procedures applicable to breaches of such
 * policy and regulations, as contained in the website.
 *
 * University Guideline on Academic Honesty:
 *   http://www.cuhk.edu.hk/policy/academichonesty
 * Faculty of Engineering Guidelines to Academic Honesty:
 *   https://www.erg.cuhk.edu.hk/erg/AcademicHonesty
 *
 * Student Name: Yerarslan Shayakhmetov <fill in yourself>
 * Student ID  : 1155133650 <fill in yourself>
 * Date        : 27.11.2020 <fill in yourself>
 * 
 * @author based on skeleton code provided by Michael FUNG
 */
public class Reversi {

    // pre-defined class constant fields used throughout this app
    public static final int BLACK = -1;
    public static final int WHITE = +1;
    public static final int EMPTY =  0;
    
    // a convenient constant field that can be used by students
    public final int FLIP  = -1;
    
    // GUI objects representing and displaying the game window and game board
    protected JFrame window;
    protected ReversiPanel gameBoard;
    protected Color boardColor = Color.GREEN;

    
    // a 2D array of pieces, each piece can be:
    //  0: EMPTY/ unoccupied/ out of bound
    // -1: BLACK
    // +1: WHITE
    protected int[][] pieces;
    
    
    // currentPlayer:
    // -1: BLACK
    // +1: WHITE
    protected int currentPlayer;

    
    
    // STUDENTS may declare other fields HERE
    protected int stackForcedPass;
    
    
    /**
     * The only constructor for initializing a new board in this app
     */
    public Reversi() {
        window = new JFrame("Reversi");
        gameBoard = new ReversiPanel(this);
        window.add(gameBoard);
        window.setSize(850, 700);
        window.setLocation(100, 50);
        window.setVisible(true);
        
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // use of implicitly extended inner class with overriden method, advanced stuff
        window.addWindowListener(
            new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e)
                {
                    sayGoodbye();
                }
            }
        );


        // a 8x8 board of pieces[1-8][1-8] surrounded by an EMPTY boundary of 10x10 
        pieces = new int[10][10];
        
        pieces[4][4] = WHITE;
        pieces[4][5] = BLACK;
        pieces[5][4] = BLACK;
        pieces[5][5] = WHITE;
        stackForcedPass = 0;
        currentPlayer = BLACK;  // black plays first
        
        gameBoard.updateStatus(pieces, currentPlayer);
    }

    
    
    /**
     * setupDebugBoard for testing END-game condition
     * students can freely make any changes to this method for testing purpose
     * TEMPORARY testing case
     */
    protected void setupDebugBoardEndGame()
    {
        gameBoard.addText("setupDebugBoardEndGame():");

        for (int row = 1; row <= 8; row++)
            for (int col = 1; col <= 8; col++)
                pieces[row][col] = BLACK;
        pieces[5][8] = WHITE;
        pieces[6][8] = EMPTY;
        pieces[7][8] = EMPTY;
        pieces[8][8] = EMPTY;

        currentPlayer = BLACK;  // BLACK plays first
        
        gameBoard.updateStatus(pieces, currentPlayer);
    }


    
    /**
     * setupDebugBoard for testing MID-game condition
     * students can freely make any changes to this method for testing purpose
     * TEMPORARY testing case
     */
    protected void setupDebugBoardMidGame()
    {
        gameBoard.addText("setupDebugBoardMidGame():");

        int row, col, distance;
        
        // make all pieces EMPTY
        for (row = 1; row <= 8; row++)
            for (col = 1; col <= 8; col++)
                pieces[row][col] = EMPTY;
        
        // STUDENTS' TEST and EXPERIMENT
        // setup a star pattern as a demonstration, you may try other setups
        // relax, we will NOT encounter array index out of bounds, see below!!
        row = 5;
        col = 3;
        distance = 3;
        
        // beware of hitting the boundary or ArrayIndexOutOfBoundsException
        for (int y_dir = -1; y_dir <= +1; y_dir++)
            for (int x_dir = -1; x_dir <= +1; x_dir++)
            {
                try {
                    int move;
                    // setup some opponents
                    for (move = 1; move <= distance; move++)
                        pieces[row + y_dir * move][col + x_dir * move] = BLACK;

                    // far-end friend piece
                    pieces[row+y_dir * move][col + x_dir*move] = WHITE;
                }
                catch (ArrayIndexOutOfBoundsException e)
                {
                    // intentionally do nothing in this catch block
                    // this is simple and convenient in guarding array OOB
                }
            }
        // leave the center EMPTY for the player's enjoyment
        pieces[row][col] = EMPTY;
        
        // pieces[row][col] = 999;  // try an invalid piece

        
        // restore the fence of 10x10 EMPTY pieces around the 8x8 game board
        for (row = 1; row <= 8; row++)
            pieces[row][0] = pieces[row][9] = EMPTY;
        for (col = 1; col <= 8; col++)
            pieces[0][col] = pieces[9][col] = EMPTY;

        
        currentPlayer = WHITE;  // WHITE plays first
        // currentPlayer = 777;    // try an invalid player
        
        gameBoard.updateStatus(pieces, currentPlayer);
    }
    
    
    
    // STUDENTS are encouraged to define other instance methods here
    // to aid the work of the method userClicked(row, col)
   protected boolean captureCheck(int row, int col) {
        if(pieces[row][col] != EMPTY)
            return false;
        //variables that represents another endpoint of capturing line
       int checkRow, checkCol;
       //loop to check whether we captured toward up and left
       for(checkRow = row - 1, checkCol = col - 1; checkRow >= 1 && checkRow <= 8 && checkCol >= 1 && checkCol <= 8; checkRow--, checkCol--)
       {
           if(pieces[checkRow][checkCol] == EMPTY)
           {
               break;
           }
           else if(pieces[checkRow][checkCol] == currentPlayer * FLIP)
           {
               continue;
           }
           else if(pieces[checkRow][checkCol] == currentPlayer && checkRow == row - 1 && checkCol == col - 1)
           {
               break;
           }
           else if(pieces[checkRow][checkCol] == currentPlayer)
           {
               return true;
           }
       }
       //loop to check whehter we captured towards up
       for(checkRow = row - 1, checkCol = col; checkRow >= 1 && checkRow <= 8 && checkCol >= 1 && checkCol <= 8; checkRow--)
       {
           if(pieces[checkRow][checkCol] == EMPTY)
           {
               break;
           }
           else if(pieces[checkRow][checkCol] == currentPlayer * FLIP)
           {
               continue;
           }
           else if(pieces[checkRow][checkCol] == currentPlayer && checkRow == row - 1 && checkCol == col)
           {
               break;
           }
           else if(pieces[checkRow][checkCol] == currentPlayer)
           {
               return true;
           }
       }
       //loop to check whether captured towards up and right
       for(checkRow = row - 1, checkCol = col + 1; checkRow >= 1 && checkRow <= 8 && checkCol >= 1 && checkCol <= 8; checkRow--, checkCol++)
       {
           if(pieces[checkRow][checkCol] == EMPTY)
           {
               break;
           }
           else if(pieces[checkRow][checkCol] == currentPlayer * FLIP)
           {
               continue;
           }
           else if(pieces[checkRow][checkCol] == currentPlayer && checkRow == row - 1 && checkCol == col + 1)
           {
               break;
           }
           else if(pieces[checkRow][checkCol] == currentPlayer)
           {
               return true;
           }
       }
       //loop to check whether captured towards right
       for(checkRow = row, checkCol = col + 1; checkRow >= 1 && checkRow <= 8 && checkCol >= 1 && checkCol <= 8; checkCol++)
       {
           if(pieces[checkRow][checkCol] == EMPTY)
           {
               break;
           }
           else if(pieces[checkRow][checkCol] == currentPlayer * FLIP)
           {
               continue;
           }
           else if(pieces[checkRow][checkCol] == currentPlayer && checkRow == row && checkCol == col + 1)
           {
               break;
           }
           else if(pieces[checkRow][checkCol] == currentPlayer)
           {
               return true;
           }
       }
       //loop to check whether captured towards down and right
       for(checkRow = row + 1, checkCol = col + 1; checkRow >= 1 && checkRow <= 8 && checkCol >= 1 && checkCol <= 8; checkRow++, checkCol++)
       {
           if(pieces[checkRow][checkCol] == EMPTY)
           {
               break;
           }
           else if(pieces[checkRow][checkCol] == currentPlayer * FLIP)
           {
               continue;
           }
           else if(pieces[checkRow][checkCol] == currentPlayer && checkRow == row + 1 && checkCol == col + 1)
           {
               break;
           }
           else if(pieces[checkRow][checkCol] == currentPlayer)
           {
               return true;
           }
       }
       //loop to check whether captured towards down
       for(checkRow = row + 1, checkCol = col; checkRow >= 1 && checkRow <= 8 && checkCol >= 1 && checkCol <= 8; checkRow++)
       {
           if(pieces[checkRow][checkCol] == EMPTY)
           {
               break;
           }
           else if(pieces[checkRow][checkCol] == currentPlayer * FLIP)
           {
               continue;
           }
           else if(pieces[checkRow][checkCol] == currentPlayer && checkRow == row + 1 && checkCol == col)
           {
               break;
           }
           else if(pieces[checkRow][checkCol] == currentPlayer)
           {
               return true;
           }
       }
       //loop to check whether captured towards down and left
       for(checkRow = row + 1, checkCol = col - 1; checkRow >= 1 && checkRow <= 8 && checkCol >= 1 && checkCol <= 8; checkRow++, checkCol--)
       {
           if(pieces[checkRow][checkCol] == EMPTY)
           {
               break;
           }
           else if(pieces[checkRow][checkCol] == currentPlayer * FLIP)
           {
               continue;
           }
           else if(pieces[checkRow][checkCol] == currentPlayer && checkRow == row + 1 && checkCol == col - 1)
           {
               break;
           }
           else if(pieces[checkRow][checkCol] == currentPlayer)
           {
               return true;
           }
       }
       //loop to check whether captured towards left
       for(checkRow = row, checkCol = col - 1; checkRow >= 1 && checkRow <= 8 && checkCol >= 1 && checkCol <= 8; checkCol--)
       {
           if(pieces[checkRow][checkCol] == EMPTY)
           {
               break;
           }
           else if(pieces[checkRow][checkCol] == currentPlayer * FLIP)
           {
               continue;
           }
           else if(pieces[checkRow][checkCol] == currentPlayer && checkRow == row && checkCol == col - 1)
           {
               break;
           }
           else if(pieces[checkRow][checkCol] == currentPlayer)
           {
               return true;
           }
       }
       return false;
   }
   
   protected void capturedChange(int row, int col)
   {
       //variables that represents another endpoint of capturing line
       int checkRow, checkCol;
       //loop to check whether we captured toward up and left
       try
       {
            for(checkRow = row - 1, checkCol = col - 1; checkRow >= 1 && checkRow <= 8 && checkCol >= 1 && checkCol <= 8; checkRow--, checkCol--)
            {
                if(pieces[checkRow][checkCol] == EMPTY)
                {
                    break;
                }
                else if(pieces[checkRow][checkCol] == currentPlayer * FLIP)
                {
                    continue;
                }
                else if(pieces[checkRow][checkCol] == currentPlayer && checkRow == row - 1 && checkCol == col - 1)
                {
                    break;
                }
                else if(pieces[checkRow][checkCol] == currentPlayer)
                {
                    for(int i = row, j = col; i > checkRow && j > checkCol; i--, j--)
                    {
                        pieces[i][j] = currentPlayer;
                    }
                    break;
                }
            }
            //loop to check whehter we captured towards up
            for(checkRow = row - 1, checkCol = col; checkRow >= 1 && checkRow <= 8 && checkCol >= 1 && checkCol <= 8; checkRow--)
            {
                if(pieces[checkRow][checkCol] == EMPTY)
                {
                    break;
                }
                else if(pieces[checkRow][checkCol] == currentPlayer * FLIP)
                {
                    continue;
                }
                else if(pieces[checkRow][checkCol] == currentPlayer && checkRow == row - 1 && checkCol == col)
                {
                    break;
                }
                else if(pieces[checkRow][checkCol] == currentPlayer)
                {
                    for(int i = row, j = col; i > checkRow; i--)
                    {
                        pieces[i][j] = currentPlayer;
                    }
                    break;
                }
            }
            //loop to check whether captured towards up and right
            for(checkRow = row - 1, checkCol = col + 1; checkRow >= 1 && checkRow <= 8 && checkCol >= 1 && checkCol <= 8; checkRow--, checkCol++)
            {
                if(pieces[checkRow][checkCol] == EMPTY)
                {
                    break;
                }
                else if(pieces[checkRow][checkCol] == currentPlayer * FLIP)
                {
                    continue;
                }
                else if(pieces[checkRow][checkCol] == currentPlayer && checkRow == row - 1 && checkCol == col + 1)
                {
                    break;
                }
                else if(pieces[checkRow][checkCol] == currentPlayer)
                {
                    for(int i = row, j = col; i > checkRow && j < checkCol; i--, j++)
                    {
                        pieces[i][j] = currentPlayer;
                    }
                    break;
                }
            }
            //loop to check whether captured towards right
            for(checkRow = row, checkCol = col + 1; checkRow >= 1 && checkRow <= 8 && checkCol >= 1 && checkCol <= 8; checkCol++)
            {
                if(pieces[checkRow][checkCol] == EMPTY)
                {
                    break;
                }
                else if(pieces[checkRow][checkCol] == currentPlayer * FLIP)
                {
                    continue;
                }
                else if(pieces[checkRow][checkCol] == currentPlayer && checkRow == row && checkCol == col + 1)
                {
                    break;
                }
                else if(pieces[checkRow][checkCol] == currentPlayer)
                {
                    for(int i = row, j = col; j < checkCol; j++)
                    {
                        pieces[i][j] = currentPlayer;
                    }
                    break;
                }
            }
            //loop to check whether captured towards down and right
            for(checkRow = row + 1, checkCol = col + 1; checkRow >= 1 && checkRow <= 8 && checkCol >= 1 && checkCol <= 8; checkRow++, checkCol++)
            {
                if(pieces[checkRow][checkCol] == EMPTY)
                {
                    break;
                }
                else if(pieces[checkRow][checkCol] == currentPlayer * FLIP)
                {
                    continue;
                }
                else if(pieces[checkRow][checkCol] == currentPlayer && checkRow == row + 1 && checkCol == col + 1)
                {
                    break;
                }
                else if(pieces[checkRow][checkCol] == currentPlayer)
                {
                    for(int i = row, j = col; i < checkRow && j < checkCol; i++, j++)
                    {
                        pieces[i][j] = currentPlayer;
                    }
                    break;
                }
            }
            //loop to check whether captured towards down
            for(checkRow = row + 1, checkCol = col; checkRow >= 1 && checkRow <= 8 && checkCol >= 1 && checkCol <= 8; checkRow++)
            {
                if(pieces[checkRow][checkCol] == EMPTY)
                {
                    break;
                }
                else if(pieces[checkRow][checkCol] == currentPlayer * FLIP)
                {
                    continue;
                }
                else if(pieces[checkRow][checkCol] == currentPlayer && checkRow == row + 1 && checkCol == col)
                {
                    break;
                }
                else if(pieces[checkRow][checkCol] == currentPlayer)
                {
                    for(int i = row, j = col; i < checkRow; i++)
                    {
                        pieces[i][j] = currentPlayer;
                    }
                    break;
                }
            }
            //loop to check whether captured towards down and left
            for(checkRow = row + 1, checkCol = col - 1; checkRow >= 1 && checkRow <= 8 && checkCol >= 1 && checkCol <= 8; checkRow++, checkCol--)
            {
                if(pieces[checkRow][checkCol] == EMPTY)
                {
                    break;
                }
                else if(pieces[checkRow][checkCol] == currentPlayer * FLIP)
                {
                    continue;
                }
                else if(pieces[checkRow][checkCol] == currentPlayer && checkRow == row + 1 && checkCol == col - 1)
                {
                    break;
                }
                else if(pieces[checkRow][checkCol] == currentPlayer)
                {
                    for(int i = row, j = col; i < checkRow && j > checkCol; i++, j--)
                    {
                        pieces[i][j] = currentPlayer;
                    }
                    break;
                }
            }
            //loop to check whether captured towards left
            for(checkRow = row, checkCol = col - 1; checkRow >= 1 && checkRow <= 8 && checkCol >= 1 && checkCol <= 8; checkCol--)
            {
                if(pieces[checkRow][checkCol] == EMPTY)
                {
                    break;
                }
                else if(pieces[checkRow][checkCol] == currentPlayer * FLIP)
                {
                    continue;
                }
                else if(pieces[checkRow][checkCol] == currentPlayer && checkRow == row && checkCol == col - 1)
                {
                    break;
                }
                else if(pieces[checkRow][checkCol] == currentPlayer)
                {
                    for(int i = row, j = col; j > checkCol; j--)
                    {
                        pieces[i][j] = currentPlayer;
                    }
                    break;
                }
            }
       }
       catch(ArrayIndexOutOfBoundsException e)
       {
           System.out.println("Exception thrown in captureCheck method");
       }
   }
   
   protected boolean forcedPassCheck()
   {
       for(int i = 1; i <= 8; i++)
       {
           for(int j = 1; j <= 8; j++)
           {
               if(captureCheck(i, j))
               {
                   return false;
               }
           }
       }
       return true;
   }
    
    
    /**
     * STUDENTS' WORK HERE
     * 
     * As this is a GUI application, the gameBoard object (of class ReversiPanel)
     * actively listens to user's actionPerformed.  On user clicking of a
     * ReversiButton object, this callback method will be invoked to do some
     * game processing.
     * 
     * @param row is the row number of the clicked button
     * @param col is the col number of the clicked button
     */
    public void userClicked(int row, int col)
    {
        // major operation of this method:
        // make a valid move by placing a piece at [row][col]
        // AND flipping some opponent piece(s) in all available directions
        
        if(row < 1 || row > 8 || col < 1 || col > 8 || pieces[row][col] != EMPTY || !captureCheck(row, col))
        {
            gameBoard.addText("Invalid move");
            gameBoard.updateStatus(pieces, currentPlayer);
        }
        else
        {
            capturedChange(row, col);
            pieces[row][col] = currentPlayer;
            currentPlayer = FLIP * currentPlayer;
            gameBoard.updateStatus(pieces, currentPlayer);
            if(forcedPassCheck())
            {
                gameBoard.addText("Forced Pass");
                currentPlayer = FLIP * currentPlayer;
                gameBoard.updateStatus(pieces, currentPlayer);
                if(forcedPassCheck())
                {   
                    gameBoard.addText("Double Forced Pass");
                    gameBoard.addText("End game!");
                    currentPlayer = FLIP * currentPlayer;
                    sayGoodbye();
                }
            }
        }
    }

    
    
    /**
     * sayGoodbye on System.out, before program termination
     */
    protected void sayGoodbye()
    {
        System.out.println("Goodbye!");
    }

    
    
    // main() method, starting point of basic Reversi game
    public static void main(String[] args) {
        Reversi game = new Reversi();
    }
}
