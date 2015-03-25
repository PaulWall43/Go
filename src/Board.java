import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.*;
import java.lang.ArrayIndexOutOfBoundsException;

import java.util.ArrayList;
import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Board extends JPanel{
	private int lines;
	//underlying array controls state of the game
	private StonePiece[][] under;
	//arrayLists of arrayLists
	protected ArrayList<Group> listOfGroups = new ArrayList<Group>();
	private final int BLACK = 1;
	private final int WHITE = 2;
	private final int PLAYER_ONE = 1;
	private final int PLAYER_TWO = 2;
	private int turn;
	private static final Logger logger = LoggerFactory.getLogger(Board.class);
	
	//Constructor
	public Board(int lines){
		logger.info("Creating board");
		this.lines = lines;
		this.under = new StonePiece[lines][lines];
		//fill the array with zeros
		for(int r = 0; r < lines;r++)
			for(int c = 0; c < lines ; c++) {
				under[r][c] = null;
			}
		IListen listener = new IListen();
		this.addMouseListener(listener);
		this.turn = PLAYER_ONE;
		this.setBackground(new Color(255,222,173));
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
		StonePiece toCheck = null;
		StonePiece oldPiece = null;
		//This group of instantiations and declarations is for the case where a piece is touching multiple groups
		//oldPiece will be used as first piece since we've already declared it
		StonePiece secondTouch = null;
		StonePiece thirdTouch = null;
		StonePiece fourthTouch = null;
		//If piece is touching multiple same color pieces set up a counter
		int sameColorCounter = 0;
		//very first check if the stone is in the 3 border lines
		boolean willAdd = false;
		boolean continued = false;
		boolean newGroup = true;
		//start the loop 1 above and 1 left of center piece
		int originalY = piece.getY() - 1;
		int originalX = piece.getX() - 1 ;
		//What is oXNew for?
		int oXNew = originalX + 3;
		//What is oYNew for?
		int oYNew = originalY + 3;
		//loop to check for like pieces
		for(int r = originalY ; r < oYNew; r++){
			for(int c = originalX ; c < oXNew; c++ ){
				if(originalY < 0 || originalX < 0)
					//if the loop goes to the edge then throw the exception
					throw new ArrayIndexOutOfBoundsException("reached the edge");
				try{
					//Skip the loop if place is not directly touching
					if(r == originalY || r == oYNew -1 ){
						if (c == originalX || c == oXNew -1 ){
							continue;
						}
					}
					//check if piece exists in this spot
				if(under[r][c] == null)
					continue;
				//check to see if the stone is the main stone
				if(under[r][c] == piece)
					continue;

				//if the placed piece is touching an opposing piece then continue the algorithm
				if(under[r][c].getNumber() != piece.getNumber()) {
					//call to check two
					continued = true;
					toCheck = under[r][c]; // This is the touching piece
				}	
				//if the placed piece is touching a same color piece then add piece to the group
				if(under[r][c].getNumber() == piece.getNumber()){
					//always execute this on the first time
					//set will add to true and set the oldPiece to under[r][c]
					//this means that there will not be a new group and increase the sameColorCounter by 1
					if(sameColorCounter == 0)
					{
						//System.err.println("first touching piece" );
						willAdd = true;		
						oldPiece = under[r][c];	
						newGroup = false;
						sameColorCounter++;
					}
					
					//if this piece is not part of the other group then must combine groups
					else if(sameColorCounter == 1 && under[r][c].getGroup(this) != oldPiece.getGroup(this))
					{
						System.err.println("second touching piece");
						willAdd = false;
						secondTouch = under[r][c];
						sameColorCounter++;
					}
					//if played piece is touching 2 same color pieces
					else if(sameColorCounter == 2 && under[r][c].getGroup(this) != oldPiece.getGroup(this))
					{
						System.err.println("third touching piece");
						thirdTouch = under[r][c];
						sameColorCounter++;
					}
					//if played piece is touching 3 same color pieces
					else if (sameColorCounter == 3 && under[r][c].getGroup(this) != oldPiece.getGroup(this))
					{
						System.err.println("fourth touching piece");
						fourthTouch = under[r][c];
						sameColorCounter++; 
					}
				}
						
				//at this point the piece should be alone
				
				}
				catch(ArrayIndexOutOfBoundsException ex){
					//beginning of the edge capture algorithm
					}
				}
			}
		
		if(newGroup)
		{
			listOfGroups.add(new Group(piece, this.under));
		}
		//if played piece is touching multiple groups then combine some groups
		//Note: I Will need to throw an exception here and have something deal with it somehow
		/*if(sameColorCounter > 1)
		{
			System.err.println("Groups will be combined");
			Group.combineGroups(oldPiece.getGroup(this), secondTouch.getGroup(this),
					thirdTouch.getGroup(this) , fourthTouch.getGroup(this) , piece , this);
		}*/
		else if(sameColorCounter == 2)
		{
			Group.combineGroups(oldPiece.getGroup(this), secondTouch.getGroup(this),
					null , null , piece , this);	
		}
		else if(sameColorCounter == 3)
		{
			Group.combineGroups(oldPiece.getGroup(this), secondTouch.getGroup(this),
					thirdTouch.getGroup(this) , null , piece , this);	
		}
		else if(sameColorCounter == 4)
		{
			Group.combineGroups(oldPiece.getGroup(this), secondTouch.getGroup(this),
					thirdTouch.getGroup(this), fourthTouch.getGroup(this), piece, this);	
		}

		if(willAdd && oldPiece != null )
		{
			this.add(piece, oldPiece);
		}
		if(continued && toCheck != null)
		{
			this.secondStep(toCheck);
		}
		System.err.println("The number of groups on board:" + Group.getCount());
	    //logger.debug("The number of groups on board:" /*+ Group.getCount()*/ ); 
	}
	
	//this methods creates a group (size of 1  to lines * lines)
	//algorithm:
	//--->1)Any like pieces above, below, left, right of main piece
	//--->2)If YES then create a group 
	//--->3)Call method on that piece
	//--->4) Add all touching pieces to the group field arrayList
	//--->5) Call method on all the pieces that the thing was touching
	//--->6) This should give us all the pieces in the group
	//--->7) Find the outside pieces 
	//--->8) check if all outside pieces are 
	public void secondStep(StonePiece piece){
		//calling this method on the piece that may be captured
		//if this piece is not already in a group
		//Group newGroup = new Group(piece, this.under);
		listOfGroups.add(piece.getGroup(this));
		if((piece.getGroup(this)).isCaptured(piece, this.under))
			piece.getGroup(this).remove(this);
	}

	public void remove(StonePiece piece){
		under[piece.getY()][piece.getX()] = null;
	}
	
	/**This method calls the addPieceToGroup method in group
	 * If there is already a group on the board then this method is valid
	 * adds the newly placed piece to group that it is touching
	 * Also sets that pieces inGroupNumber field which may be important later
	 */
	public void add( StonePiece pieceToAdd, StonePiece firstPiece){
		if(listOfGroups.size() > 0){
			if(firstPiece.getGroup(this).getNumber() == pieceToAdd.getNumber()){
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
				Point toCheck = new Point(toPut[0], toPut[1]);
				if(Board.isAllowed(toCheck, BLACK )) {
					if(under[toPut[0]][toPut[1]] != null)
						return;
				under[toPut[0]][toPut[1]] = new StonePiece(Color.BLACK, toPut);
				Board.this.firstCheck(under[toPut[0]][toPut[1]]);
				Board.this.repaint();
				Board.this.turn = PLAYER_TWO;
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
		int toReturnX =  0;
		int toReturnY = 0;
		int[] toReturn = new int[2];
		double boardSize = Board.this.getWidth() * .9; 
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
		//create a new array
		int[] markersX = new int[lines];
		//fill the array
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
			if(i == 0)
				check = (int) Math.abs(x - markersX[i]); 
			else if(Math.abs(x - markersX[i]) < check){
				check = Math.abs(x - markersX[i]);
				toReturnX = i;
				toReturn[1] = toReturnX;
			}
			if(check < ((boardSize/(lines -1))/2))
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
//(height / lines + 2) 
//NOTES
//remember that multidimensionals do things


//FIX THE LOOP





//LEFT OFF 6/7/14 (1 A.M.)
//WHY IS ARRAY RETURNING BOTH ZEROS NO SENSE
//ALSO REMEMBER TO REFRESH THE PAGE AFTER EVERY MOVE

//LEFT OFF 6/7/14 (3 A.M.) 
//---> Why does xCount need to be adjusted by plus one??
//---> Fixed the nonsense zero returns (width * .1 rather than width * .9)
//---> Fixed the correlating problem by fixing multidimensional manipulation and understanding
//---> PROBLEM
//---->The distance calculation are slightly biased towards up, needs to be fixed
//--->Need to prevent further moves on taken spot

//LEFT OFF 6/7/14 (11:07 A.M.)
//--->Piece placement is subject to height of the panel (when height changes the ratio changes
//---> Fix the 0 problem

//LEFT OFF 6/15/14
//--->New mapping method is much more compact
//--->Must improve accuracy as some of the clicks are slightly off
//--->For now implemented no resizing
//---> After improving precision move onto moves allowed

//LEFT OFF 6/21/14
//--->Fixed the inaccuracy for mapping
//--->I DON'T KNOW WHY THE CONSTANTS FOR X AND Y ARE DIFFERENT
//--->Next up is moves allowed
//----> Plan to make helper method called in paint component
//----->Will deal with surrounding
//------>Next will be suicide
//------->Then Komi
//--->Then I plan to do score
//---> DONE

//LEFT OFF 6/25/14
//--->New class StonePiece
//--->Objects of this class are stored in the array "under"
//--->Has lots of fields so that it is more helpful and interactable than simple integers
//--->Next is to implement the capture algorithm
//---> Should take me one sitting to get the main code down
//---> One more sitting to finish up the code
//---> Then on to the other rules
//---->Thinking about possible features 
//----->Number of pieces played, when game is over show number on stones
//------>Clocks and other stuffs
//------>Will look at other ways to use the clock with new techniques


//LEFT OFF 7/20/14
//---> When a piece is put down it should check its surroundings for an opposing piece
//---> if one is found then we check if that opposing piece is touching other pieces (no diagnols)
//---> If its not then we check if its surrounded by your color piece
//--->If this is true then we remove the piece from the array
//--->If it is connected then we make a new class representing all those stones as a group
//---> FIGURED OUT SINGLE STONE CAPTURE
//---> Next is to figure out multiple stone captures and edge captures
//----> Multiple stones may be difficult but we can do it

//---> this may be entirely more efficient to do with arrayLists 


//LEFT OFF 8/26/14
//--->Lots of progress on the capture algorithm
//--->Have to fix some bugs and then move on to edge capture which might be weird
//Gotta remember to get rid of the old groups that don't exist anymore from the listOfGroups instance variable
//---> Next time make a new group when the first pieces are played and make sure the adding works


//PLAN FOR HOW LONG THIS WILL TAKE
/**
 * 1) Must throw exception or overload the method and finish the combine method
 * 2) Must make multiple checks for touching of opposing pieces
 * 3) Must finish the edge  algorithm and whatever stuff that has to deal with
 * 4) Must add suicide and komi rules
 * 4) Must add numbers to the sides of the board
 * 5) Must add the clock
 * 6) Must figure out a way to keep count of the game
 * 7) Must figure algorithm for keeping score
 * 8) Decorate and make it look pretty
 * 9) I should be able to finish this by the end of the school year
 */


//Fix everything then start on comments then upload to github and then fuck shit up
