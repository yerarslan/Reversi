package reversi;

import java.awt.Color;
import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;
import javax.swing.JOptionPane;


/**
 * ReversiOnFile is a subclass of Reversi, adding File I/O capabilities
 * for loading and saving game.
 *
 * I declare that the work here submitted is original
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
 * Date        : 28.11.2020 <fill in yourself>
 * 
 */
public class ReversiOnFile extends Reversi {
    
    public static final char UNICODE_BLACK_CIRCLE = '\u25CF';
    public static final char UNICODE_WHITE_CIRCLE = '\u25CB';
    public static final char UNICODE_WHITE_SMALL_SQUARE = '\u25AB';
    
    // constructor to give a new look to new subclass game
    public ReversiOnFile()
    {
        window.setTitle("ReversiOnFile");
        gameBoard.setBoardColor(Color.BLUE);
    }

    
    // STUDENTS' WORK HERE    
    public void loadBoard(String filename)
    {
        int[][] tmpPieces = new int[10][10];
        String strCurrentPlayer;
        try
        {
            File file = new File(filename);
            boolean isExist = file.exists();
            Scanner input = new Scanner(file);
        
            for(int i = 1; i <= 8; i++)
            {
                String stringRow = input.nextLine();
                for(int j = 0; j < 8; j++)
                {
                    if(stringRow.charAt(j) == '\u25CF')
                    {
                        tmpPieces[i][j + 1] = BLACK;
                    }
                    else if(stringRow.charAt(j) == '\u25CB')
                    {
                        tmpPieces[i][j + 1] = WHITE;
                    }
                    else tmpPieces[i][j + 1] = EMPTY;
                }
            }
            strCurrentPlayer = input.nextLine();
            System.out.println(strCurrentPlayer + '\u25CF');
            if(strCurrentPlayer.equals("\u25CF"))
                currentPlayer = BLACK;
            else currentPlayer = WHITE;
            gameBoard.addText("Loaded board from " + filename);
            System.out.println("Loaded board from " + filename);
            pieces = tmpPieces;
            gameBoard.updateStatus(pieces, currentPlayer);
        }
        catch(Exception e)
        {
            gameBoard.addText("Cannot load board from " + filename);
            System.out.println("Cannot load board from " + filename);
            setupBoardDefaultGame();        
        }
        if(forcedPassCheck())
        {
            currentPlayer *= FLIP;
            if(forcedPassCheck())
            {
                gameBoard.addText("Double Forced Pass");
                gameBoard.addText("End game");
            }
        }
    }

    public void setupBoardDefaultGame()
    {
        int[][] defaultPieces = new int[10][10];
            defaultPieces[4][4] = WHITE;
            defaultPieces[4][5] = BLACK;
            defaultPieces[5][4] = BLACK;
            defaultPieces[5][5] = WHITE;
            pieces = defaultPieces;
            gameBoard.updateStatus(pieces, currentPlayer);
    }

    // STUDENTS' WORK HERE    
    public void saveBoard(String filename)
    {
        try
        {
            PrintStream myNewFile = new PrintStream(filename);
            for(int i = 1; i <= 8; i++)
            {
                for(int j = 1; j <= 8; j++)
                {
                    if(pieces[i][j] == BLACK)
                    {
                        myNewFile.print('\u25CF');
                    }
                    else if(pieces[i][j] == WHITE)
                    {
                        myNewFile.print('\u25CB');
                    }
                    else
                    {
                        myNewFile.print('\u25AB');
                    }
                }
                myNewFile.print("\n");
            }
            if(currentPlayer == BLACK)
            {
                myNewFile.print("\u25CF");
            }
            else
            {
                myNewFile.print("\u25CB");
            }
            gameBoard.addText("Saved board to " + filename);
            System.out.println("Saved board to " + filename);
            myNewFile.close();
        }
        catch(Exception e)
        {
            gameBoard.addText("Cannot save board to " + filename);
            System.out.println("Cannot save board to " + filename);
        }        
    }

    @Override
    protected void sayGoodbye()
    {
        System.out.println("Goodbye!");
        String filename = JOptionPane.showInputDialog("Save board filename");
        saveBoard(filename);
    }
        
    
    // STUDENTS' WORK HERE    
    // main() method, starting point of subclass ReversiOnFile
    public static void main(String[] args) {
        ReversiOnFile game = new ReversiOnFile();
        String filename = JOptionPane.showInputDialog("Load board filename");
        game.loadBoard(filename);
        // load board from file
        // ...
    }
}
