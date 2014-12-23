

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
	 * Only constructore for Table
	 * Sets layout
	 * 		size
	 * 		lines on board
	 * @param lines
	 * @param time
	 */
	public Table(int lines, int time){
		//set the fields
		this.lines = lines;
		//this.clockTime = time;
		this.setSize(800,450); //Wide to fit both mat and board
		this.setLayout(new GridLayout());//Simple grid layout 2x1
		//add the board to the table
		board = new Board(this.lines);//New board with argument# of lines
		this.add(board); // Add board to JFrame
		Mat goMat = new Mat(); //Create a new mat
		this.add(goMat); //Add the mat to the Table(JFrame)
		this.setVisible(true); 
		this.setResizable(false); //Not resizable for now
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Make sure it goes away after done
	}
	//Will create an executable later once I learn how
	public static void main(String[] arg) {
		//when adjusting sizes the clicks become more inaccurate
		Table first = new Table(19, 5);
		
	}
}