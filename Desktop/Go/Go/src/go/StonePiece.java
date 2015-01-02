package go;
import java.awt.*;
/**
 * NOTE: "technically captured" --> Means that said piece has no open spaces and is surround by its group
 * 									as well as at least one opposing piece
 * A class which implements the object that goes into the board array (under)
 * This class provides methods for getting and setting the properties of the
 * StonePieces on the board. 
 * @param color - color of the piece in terms of RGB value
 * @param points - holds x and y 
 * @param x - The x coordinate on the board of this piece 
 * @param y - The y coordinate on the board of this piece 
 * @param hasBeenChecked - FORGOT WHAT THIS DOES
 * @param outerPiece - Is this piece on outer piece of the group it's in?
 * @param inGroup - boolean true if inGroup, false if not
 * @param inGroupNumber - Gives the number of the group this is in
 * @param surrounded - True if this piece is technically captured (technically captured
 * @param openSpaces - array with the spots that are open 
 * @param number - 0 for black and 1 white
 * @param COUNTED - Identifying number of the piece 
 * @param numberOfPieces - how many pieces on the board (NEED TO UPDATE THIS AFTER REMOVING PIECES) 
 * @author paulwallace
 */
public class StonePiece {
//fields
	private Color color; 
	private int[] points;
	private int x;
	private int y;
	private boolean hasBeenChecked;
	protected boolean outerPiece;
	private boolean inGroup; 
	private int inGroupNumber; //The number of the group that this piece is in
	private boolean surrounded;
	protected boolean[] openSpaces; //0 = left, 1 = right, 2 = up, 3 = down
	private int number; //0 for black 1 for white
	private final int COUNTED; //an identifying number
	protected static int numberOfPieces; //general count of how pieces have been played
	
//constructors
	public StonePiece(Color newColor, int[] array){
		setHasBeenChecked(false); // has not been checked
		openSpaces = new boolean[4]; // create the new array
		for(int i = 0; i < openSpaces.length; i++)
			openSpaces[i] = true; // auto fill with true
		this.color = newColor;
		if(newColor == Color.BLACK)
			this.number = 0;
		else
			//watch for bugs here
			this.number = 1;
		//new array to prevent object connection and possible bugs
		this.points = new int[2];
		points[0] = array[0]; //y-coordinate
		points[1] = array[1]; //x-coordinate
		this.y = points[0]; //set points
		this.x = points[1];
		StonePiece.numberOfPieces++; // increment
		this.COUNTED = numberOfPieces; //give this piece an ID 
	}
	
	/**
	 * @return the identifying number of this piece
	 */
	public int getCOUNTED(){
		return this.COUNTED;
	}
	/** 
	 * @return the RGB value of this piece
	 */
	public Color getColor(){
		return this.color;
	}
	/**
	 * Sets the color of piece to this new color
	 * @param newColor
	 */
	public void setColor(Color newColor){
		this.color = newColor;
	}
	/**
	 * @return the x coordinate of this StonePiece
	 */
	public int getX(){
		return this.x;
	}
	/**
	 * @return the y coordinate of this StonePiece
	 */
	public int getY(){
		return this.y;
	}
	/**
	 * @return the number (black or white) 0 or 1 of the StonePiece
	 */
	public int getNumber(){
		return this.number;
	}
	/**
	 * @return the x and y coordinates in an array
	 */
	public int[] getArrayLocation(){
		int[] toReturn = {this.points[0], this.points[1]};
		return toReturn;
	}
	/**
	 * @return the opposite number of the piece selected
	 */
	public int oppNum(){
		if(this.getNumber() == 1)
			return 0;
		else
			return 1;
	}
	/**
	 * @return the static variable numberOfPieces for how many pieces there are
	 */
	public static int getStatic() {
		return StonePiece.numberOfPieces;
	}
	/**
	 * @return if this StonePiece has been checked
	 */
	public boolean isHasBeenChecked() {
		return hasBeenChecked;
	}
	/**
	 * Set hasBeenChecked variable to true or false
	 * @param hasBeenChecked
	 */
	public void setHasBeenChecked(boolean hasBeenChecked) {
		this.hasBeenChecked = hasBeenChecked;
	}
	/**
	 * Set surrounded to true or false
	 * @param toSet
	 */
	public void setSurrounded(boolean toSet){
		surrounded = toSet;
	}
	/**
	 * @return true or false if surrounded or not
	 */
	public boolean getSurrounded(){
		return surrounded;
	}
	/**
	 * Set the inGroupNumber to whatever group this StonePiece is in
	 * @param number
	 */
	public void setInGroupNumber(int number){
		inGroupNumber = number;
	}
	/**
	 * @return The number of group this StonePiece is in
	 */
	public int getInGroupNumber(){
		return inGroupNumber;
	}
	/**
	 * @return true if in group, false if not 
	 */
	public boolean getInGroup(){
		return this.inGroup;
	}
	/**
	 * Set inGroup to true or not (this should always be true) 
	 * @param truth
	 */
	public void setInGroup(boolean truth){
		this.inGroup = truth;
	}
	
	/**
	 * @param goBoard board in which this StonePiece lies
	 * @return the Group that this StonePiece is in
	 */
	public Group getGroup(Board goBoard){
		if(goBoard.listOfGroups.size() > 0)
			return goBoard.listOfGroups.get(inGroupNumber);
		return null;
	}
}
	

