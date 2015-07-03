

import java.awt.*;
import javax.swing.*;
/**This class intends to control both the board and mat
 * Has three fields
 * @param lines The number of lines that a board has
 * @param clockTime Starting time of the game clock
 * @param This is the board, the thing that goes onto the table
 * 
 * @author Paul Wallace
 */
public class Table extends JFrame {
	private int lines;
	//will implement the clock class later
	//private int clockTime;
	protected Board board;
	
	
	/**
	 * One and only constructor of the go board
	 * This constructor sets up the size and time of the board first.
	 * After this it adds the board to the JFrame and sets visible to true
	 * 
	 * @param lines
	 * @param time
	 */
	public Table(int lines, int time){
		//set the fields
		this.lines = lines;
		//this.clockTime = time;
		//set size
		this.setSize(800,450);
		//set the layout
		//BorderLayout borders = new BorderLayout();
		this.setLayout(new GridLayout());
		//add the board to the table
		board = new Board(this.lines, false);
		this.add(board);
		Mat goMat = new Mat(board);
		this.add(goMat);
		this.setVisible(true);
		this.setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	/* Same thing as my executable, would like to work on this fora while
	 * 
	 * 
	 */
	public static void main(String[] arg) {
		//when adjusting sizes the clicks become more inaccurate
		Table first = new Table(19, 5);
		
	}
}
