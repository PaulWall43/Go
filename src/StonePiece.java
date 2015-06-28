
import java.awt.*;

public class StonePiece {
//fields
	private Color color;
	private int[] points;
	private int x;
	private int y;
	private boolean hasBeenChecked;
	protected boolean outerPiece;
	private boolean inGroup;
	//The number of the group that this piece is in
	private int inGroupNumber;
	//don't really use this anymore
	private boolean surrounded;
	//inner piece
	//0 = left, 1 = right, 2 = up, 3 = down
	protected boolean[] openSpaces;
	//0 for black 1 for white
	private int number;
	//an identifying number
	private final int ID;
	//general count of how pieces have been played
	private static int numberOfPieces;
	
//constructors
	public StonePiece(Color newColor, int[] array){
		setHasBeenChecked(false);
		openSpaces = new boolean[4];
		for(int i = 0; i < openSpaces.length; i++)
			openSpaces[i] = true;
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
		this.y = points[0];
		this.x = points[1];
		//StonePiece.numberOfPieces++;
		//this.ID = numberOfPieces;
		this.ID = numberOfPieces++;
	}
	
	public int getID(){
		return this.ID;
	}
	
	public Color getColor(){
		return this.color;
	}
	public void setColor(Color newColor){
		this.color = newColor;
	}
	//returns x position in the array
	public int getX(){
		return this.x;
	}
	//returns the y position in array
	public int getY(){
		return this.y;
	}
	public int getNumber(){
		return this.number;
	}
	public int[] getArrayLocation(){
		int[] toReturn = {this.points[0], this.points[1]};
		return toReturn;
	}
	//return the opposite number of the piece selected
	public int oppNum(){
		if(this.getNumber() == 1)
			return 0;
		else
			return 1;
	}
	
	public static int getStatic() {
		return StonePiece.numberOfPieces;
	}

	public boolean isHasBeenChecked() {
		return hasBeenChecked;
	}

	public void setHasBeenChecked(boolean hasBeenChecked) {
		this.hasBeenChecked = hasBeenChecked;
	}
	
	public void setSurrounded(boolean toSet){
		surrounded = toSet;
	}
	
	public boolean getSurrounded(){
		return surrounded;
	}
	
	public void setInGroupNumber(int number){
		inGroupNumber = number;
	}
	
	public int getInGroupNumber(){
		return inGroupNumber;
	}
	
	public boolean getInGroup(){
		return this.inGroup;
	}
	
	public void setInGroup(boolean truth){
		this.inGroup = truth;
	}
	
	//method that returns the group that this specific StonePiece is in
	public Group getGroup(Board goBoard){ 
		if(goBoard.listOfGroups.size() > 0)
			return goBoard.listOfGroups.get(inGroupNumber);
		return null;
	}
}
	


//How in the world am I going to make this addable to the board
	//One possible way is to, instead of making this StonePiece extend
	//JPanel, make it a direction to add to the paintComponent method
	//each declaration of this class will be directions for the 
	//paintComponent method to follow
	//I GOT IT
	//What we're gonna do is the following:
	//we're gonna change the underlying board array to hold StonePieces!
	//then instead of holding integers they will hold StonePieces.
	//---> What exactly are the benefits of this?
	//---> The benefits are that these pieces in the array can react to one another
	//---> Each piece can then determine 
	//---> this ultimately is to help the process of capturing
	//---> A piece(s) will know when it is captured and remove itself from the board
	//---> How will i implement this capture buisness?
	//--->I got it
	//---> when a new piece is added you check to see if it is touching any like color pieces
	//---> if it is check the next piece
	//---> and so and so forth
	//--->if all the pieces connect then check the inside
	//---> if it surrounds the opposing piece then erase those pieces (referred to by COUNTED)
	//--->are set to captured and erased from the board