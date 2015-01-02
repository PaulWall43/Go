
//Will create a Go Package
import java.awt.*;
import java.lang.ArrayIndexOutOfBoundsException;
import java.util.ArrayList;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
/**
 * Board class that extends JPanel
 * 
 * Provides the main logic for: drawing the board
 * 								determining effects of newly placed pieces
 * 								mapping clicks to board 
 * @param lines - the size of the board
 * @param under - StonePiece[][] the array of pieces played
 * @param listOfGroups - Array list with record of all groups on board
 * @param BLACK - all black pieces associated with 1
 * @param WHITE - all white pieces associated with 2
 * @param PLAYER_ONE - First move player will be associated with 1
 * @param PLAYER_TWO - Second move player will be associated with 2
 * @param turn - alternates between 1 and 2 
 * @author paulwallace
 *
 */
public class Board extends JPanel{
	private int lines;
	//underlying array controls state of the game
	private StonePiece[][] under;
	//arrayLists of arrayLists
	protected ArrayList<Group> listOfGroups = new ArrayList<Group>();
	private final int BLACK = 1; //constants for colors
	private final int WHITE = 2;
	private final int PLAYER_ONE = 1;
	private final int PLAYER_TWO = 2;
	private int turn;
	
	//Constructor
	public Board(int lines){
		this.lines = lines; //set lines
		this.under = new StonePiece[lines][lines]; //set size of array
		//fill the array with zeros
		for(int r = 0; r < lines;r++)
			for(int c = 0; c < lines ; c++) {
				under[r][c] = null; // set all indexes to null
			}
		IListen listener = new IListen(); // create a event handler
		this.addMouseListener(listener); // mouse click handler
		this.turn = PLAYER_ONE; // Always player ones turn first
		this.setBackground(new Color(255,222,173)); // orange board
	}
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		int width = this.getWidth();
		int height = this.getHeight();
		double boardSize = (width * .9); 
		double boardSizeHalf = boardSize/2;
		double centerX = (width/2) ;
		double centerY = (height/2) ;
		
		//due to change according to the requested size of the board (i < 20)
		//first for loop to draw the vertical lines
		for(int i = 0; i < lines  ; i++) {
			//cast just in case boardSize is a bad size
			int x1V = (int)((centerX - (boardSizeHalf)) + (i * (boardSize/(lines-1))));
			int x2V = x1V;
			int y1V= (int) (centerY - boardSizeHalf);
			int y2V = (int) ((centerY + boardSizeHalf)) ;
			//draw the lines here
			g.drawLine(x1V, y1V, x2V, y2V);
		}
			//for horizontal lines
		for(int a = 0; a < lines ; a++) {
			int x1H = (int)(centerX - boardSizeHalf);
			int x2H = (int)((centerX + boardSizeHalf));
			int y1H = (int) ((centerY - boardSizeHalf) + (a * (boardSize/(lines-1))));
			int y2H = y1H;
			//draw the lines here
			g.drawLine(x1H, y1H, x2H, y2H);
			
			}
		
	//method to draw the piece on the board
	//Every time it runs through this method it checks
    //to see if the place in the board is occupied by a stone Piece
		for(int r = 0; r < lines; r++)
			for(int c = 0; c < lines; c++){
				if(under[r][c] != null ){
					g.setColor(Color.blue);
					//draw a circle at that point
					int boxSize = (int)(boardSize/(lines - 1));
					int x = (int) (((centerX - boardSizeHalf) + 
							c * (boardSize/(lines -1))) - (boardSize/(lines-1))/2);
					int y = (int) (((centerY - boardSizeHalf) + 
							r * (boardSize/(lines -1))) - (boardSize/(lines-1))/2);
					//if there is a StonePiece in that spot of array then draw piece
					if(under[r][c].getColor() == Color.BLACK){
						g.setColor(Color.black);
						g.drawOval(x,y, boxSize, boxSize);
						g.fillOval(x, y, boxSize, boxSize);
					}
					//if point at array is not null then draw piece
					else if(under[r][c].getColor() == Color.WHITE){
						g.setColor(Color.white);
						g.drawOval(x,y, boxSize, boxSize);
						g.fillOval(x, y, boxSize, boxSize);
					}
				}	
			}//end of the nested loop			
	}//end of paintComponent
	
	
	/**Method to decide whether or not the move is allowed
	 * If the move is allowed it returns true
	 * if not then it returns false
	 */
	public static boolean isAllowed(Point point, int player) {
		//first going to implement the capture rule
	return true;
	}
	
	/**THIS IS THE BEGINNING OF THE SINGLE STONE CAPTURE ALGORITHM
	 * 
	 * Call this method on the piece that was most recently placed
	 * It does two things:
	 * 1) checks if the piece is touching (including diagonals) an opposing piece
	 * 2) checks if the piece should be added to a pre-existing group
	 * @param piece The piece just placed
	 */
	
	//NOTE: REMEMBER THAT WHEN YOU PLACE A PIECE YOU MUST CHECK ALL OPPOSING TOUCHING THAT PIECE
	//THIS MEANS THAT THERE CAN BE MUTILPLE OPPOSING PIECES THAT YOUR PIECE CAN BE TOUCHING
	public void firstCheck(StonePiece piece){
		StonePiece toCheck = null; // the first opposing piece touched
		StonePiece oldPiece = null; // this first touching like color piece
		StonePiece secondTouch = null; // second
		StonePiece thirdTouch = null; // third
		StonePiece fourthTouch = null; // fourth
		int sameColorCounter = 0; //increment for every same color touching piece
		boolean willAdd = false; //will call add to group
		boolean continued = false; //continued will call secondCheck
		boolean newGroup = true; //newGroup will call 
		int originalY = piece.getY() - 1; //start the loop 1 above and 1 left of center piece
		int originalX = piece.getX() - 1;
		int oXNew = originalX + 3; //end bottom right corner
		int oYNew = originalY + 3;
		
		for(int r = originalY ; r < oYNew; r++){
			for(int c = originalX ; c < oXNew; c++ ){
				if(originalY < 0 || originalX < 0)
					//Need a different algorithm for edge moves
					throw new ArrayIndexOutOfBoundsException("reached the edge");
				try{
					//Skip the loop if board spot is not directly touching piece (left, right, up, down)
					if(r == originalY || r == oYNew -1 ){
						if (c == originalX || c == oXNew -1 ){
							continue;
						}
					}
				//check if a piece exists in this spot
				if(under[r][c] == null)
					continue;
				//check to see if the stone is center stone
				if(under[r][c] == piece)
					continue;

				//if the placed piece is touching an opposing piece then continue the algorithm
				if(under[r][c].getNumber() != piece.getNumber()) {
					//call to check two
					continued = true;
					toCheck = under[r][c];
				}	
				//if the placed piece is touching a same color piece then add piece to the group
				else //(under[r][c].getNumber() == piece.getNumber())
					{
					
					//always execute this on the first time
					//set will add to true and set the oldPiece to under[r][c]
					//this means that there will not be a new group and increase the sameColorCounter by 1
					if(sameColorCounter == 0)
					{
						System.err.println("first touching piece" );
						willAdd = true;		
						oldPiece = under[r][c];	
						newGroup = false;
						sameColorCounter++;
					}
					
					//if this piece is not part of the other group then must combine groups
					if(sameColorCounter == 1 && under[r][c].getGroup(this) != oldPiece.getGroup(this))
					{
						System.err.println("second touching piece");
						willAdd = false;
						secondTouch = under[r][c];
						sameColorCounter++;
					}
					//if played piece is touching 2 same color pieces all in different groups
					if(sameColorCounter == 2 && under[r][c].getGroup(this) != oldPiece.getGroup(this))
					{
						System.err.println("third touching piece");
						thirdTouch = under[r][c];
						sameColorCounter++;
					}
					//if played piece is touching 3 same color pieces all in different groups
					if (sameColorCounter == 3 && under[r][c].getGroup(this) != oldPiece.getGroup(this))
					{
						System.err.println("fourth touching piece");
						fourthTouch = under[r][c];
						sameColorCounter++;
					}
					// end of else statement
					}		
				//Piece is all alone
				}
				catch(ArrayIndexOutOfBoundsException ex){
					//beginning of the edge capture algorithm
					}
				//end of try catch, statement
				}
			// end of inner loop
			}
		//end of outer loop
		if(newGroup){ //if piece is solitary then create a new group
			listOfGroups.add(new Group(piece, this.under));
		}
		//if played piece is touching multiple groups then combine some groups
		//Note: I Will need to throw an exception here and have something deal with it somehow
		if(sameColorCounter > 1) // if touching multiple friendly pieces then combine groups
		{
			System.out.println("Groups will be combined");
			Group.combineGroups(oldPiece.getGroup(this), secondTouch.getGroup(this),
					thirdTouch.getGroup(this) , fourthTouch.getGroup(this) , piece , this);
		}
		else if(willAdd && oldPiece != null ) //new piece added to older group
		{
			this.add( piece, oldPiece);
		}
		if(continued && toCheck != null) //see if the touching enemy piece has caught anything 
		{
			this.secondStep(toCheck);
		}
	}
	/**
	 * Checks if the parameter piece(or its group)
	 * is surrounded.Delegates to the group isCaptured 
	 * method which returns true if it is captured. 
	 * @param piece
	 */
	public void secondStep(StonePiece piece){
		//calling this method on the piece that may be captured
		//listOfGroups.add(piece.getGroup(this));
		
		//delegate to the group method to see if captured
		if((piece.getGroup(this)).isCaptured(piece, this.under))
		{
			//delegate to group remove
			piece.getGroup(this).remove(this); //remove group/piece
		}
	}
	//Not sure when this is called
	public void remove(StonePiece piece){
		under[piece.getY()][piece.getX()] = null;
	}
	
	/**This method calls the addPieceToGroup method in group
	 * If there is already a group on the board then this method is valid
	 * adds the newly placed piece to group that it is touching
	 * Also sets that pieces inGroupNumber field which may be important later
	 */
	public void add( StonePiece pieceToAdd, StonePiece firstPiece){
		if(listOfGroups.size() > 0){ //make sure there is a group to be added to
			if(firstPiece.getGroup(this).getNumber() == pieceToAdd.getNumber()){ //Same color pieces
				(firstPiece.getGroup(this)).group.add(pieceToAdd);
				pieceToAdd.setInGroupNumber(firstPiece.getInGroupNumber());
				for(int i = 0; i < pieceToAdd.getGroup(this).group.size(); i++){
				//System.out.println("Piece is at: " +  pieceToAdd.getGroup(this).group.get(i).getX() + " , " +pieceToAdd.getGroup(this).group.get(i).getY());
				}
			//System.out.println("Group Size of new piece is: " + pieceToAdd.getGroup(this).group.size());
			}
		}
		
	}
	
	//Inner class to for action listeners
	public class IListen implements MouseListener {
		public void mouseClicked(MouseEvent e){
			//check to see if click was on the board
			Point clicked = new Point(e.getX(), e.getY());
			//System.out.println(e.getX() + " " + e.getY());
			
			if(Board.this.turn == PLAYER_ONE){
				//call to helper
				int[] toPut = this.mapPoint(clicked);
				//map from coordinates to array
				Point toCheck = new Point(toPut[0], toPut[1]); //Call mapping method which returns an array
				if(Board.isAllowed(toCheck, BLACK )) {
					if(under[toPut[0]][toPut[1]] != null) // If piece is there already, no move
						return;
				under[toPut[0]][toPut[1]] = new StonePiece(Color.BLACK, toPut); // Put a new appropriate piece
				Board.this.firstCheck(under[toPut[0]][toPut[1]]); // Determine actions due to this piece
				Board.this.repaint(); // repaint after effects have been enacted
				Board.this.turn = PLAYER_TWO; // Set turn to PLAYER_TWO
				}
			}
			else if(Board.this.turn == PLAYER_TWO){
				//call to helper
				int[] toPut = this.mapPoint(clicked);
				//map from coordinates to array
				Point toCheck = new Point(toPut[0], toPut[1]);
				if(isAllowed(toCheck, WHITE)) {
					if(under[toPut[0]][toPut[1]] != null)
						return;
				under[toPut[0]][toPut[1]] = new StonePiece(Color.WHITE, toPut);
				Board.this.firstCheck(under[toPut[0]][toPut[1]]);
				Board.this.repaint();
				Board.this.turn = PLAYER_ONE;
				}
			}
				
		}
		public void mouseEntered(MouseEvent e){}
		public void mouseReleased(MouseEvent e){}
		public void mousePressed(MouseEvent e){}
		public void mouseExited(MouseEvent e){}
			
	
	/*Helper method to find the nearest point on the board to associate the mouse event with
	 *@param point The point derived from the mouse event
	 * This method has a function which maps the x coordinate of the click to a point on board
	 * This method also has a function which maps the y coordinate of the click to a point on the board
	 * */
	
	public int[] mapPoint(Point point){
		//to return variables
		int toReturnX =  0; // Will put this in return array 
		int toReturnY = 0; // Will put this in return array 
		int[] toReturn = new int[2]; // Will return this array with board coordinates 
		double boardSize = Board.this.getWidth() * .9; //Board is always .9 of the screen
		int width = Board.this.getWidth();
		int height = Board.this.getHeight();
		double centerX = width/2;
		double centerY = height/2;
		double difference;
		if(height > width){
			difference = (centerY - centerX);
		}
		else
			difference = 0;
		//create a new array size of lines
		int[] markersX = new int[lines];
		//fill the array with 20 through (18 * boardSize/(18))
		for(int i = 0; i < lines; i++){
			//make sure to change this 20 at some point
			markersX[i] = (int) (20 + (i * boardSize/(lines-1)));
			//System.out.println("Array at " + i + " " + markersX[i]);
		}
		//get hold of the x and y points
		double x = point.getX();
		double y = point.getY();
		double check = 0;
		//find the closest number to x
		//revise this later to make more pretty
		for(int i = 0; i < lines; i++){
			if(i == 0) // At beginning of loop set check to initial 
					   //difference from clicked x to first place
				check = (int) Math.abs(x - markersX[i]); // set to absolute value of difference
			else if(Math.abs(x - markersX[i]) < check){ // if new difference is smaller reset
				check = Math.abs(x - markersX[i]);
				toReturnX = i;
				toReturn[1] = toReturnX;
			}
			if(check < ((boardSize/(lines - 1))/2)) // If difference is less than certain value then break
				break;
			
		}
		//now for the Y
		int[] markersY = new int[lines];
		//fill the array
				for(int i = 0; i < lines; i++){
					//make sure to change this 20 at some point
					markersY[i] = (int) (20 + difference + (i * boardSize/(lines-1)));
				}
				//find the closest number to x
				//revise this later to make more pretty
				for(int i = 0; i < lines; i++){
					if(i == 0)
						check = (int) Math.abs(y - markersY[i]); 
					else if(Math.abs(y - markersY[i]) < check){
						check = Math.abs(y - markersY[i]);
						toReturnY = i;
						toReturn[0] = toReturnY;
					}
					if(check < ((boardSize/(lines -1))/2))
						break;
				}
				toReturn[0] = toReturnY;
				//set the x value of the return array
				return toReturn;
	}
	}

}
//end of class