
import java.awt.Point;
import java.util.ArrayList;
/**
 * 
 * @param group The array list of stonepieces that are included in this group
 * @param numberColor The color of this group ( 0 for black and 1 for white )
 * @param id The identifying number of the group
 * @param count Static variable, The number of groups in play
 * @param countOfEmptyGroups The number of groups no longer in play
 * @author paulwallace
 */
public class Group {
	protected ArrayList<StonePiece> group; 
	private int numberColor; //0 for black, 1 for white
	private int id;  //id starts at 0 
	private static int count = 0;
	private static int countOfEmptyGroups = 0;
	
	
	//**********************************CONSTRUCTOR***********************************//
	/**
	 * Constructs a group 
	 * @param piece The piece used to create the group
	 * @param under The array the StonePiece is located in
	 */
	public Group(StonePiece piece, StonePiece[][] under){
		//System.out.println("Group constructor has been called");
		id = count; //set id
		count++; // increment count
		numberColor = piece.getNumber(); // get the color of group
		group = new ArrayList<StonePiece>(); // make a new arraylist to hold all pieces in group
		group.add(piece); // add first piece 
		piece.setInGroupNumber(id); // Every piece has a variable to keep track which group it is in
	}
	
	//**********************************LOGIC***********************************//
	/**
	 * Method to see if the group has been captured.
	 * Goes through check list for all pieces in the arrayList
	 * If the check list goes yes for all pieces then return true
	 * NOTE: "captured" in this method for intents and purposes means that
	 * 			a piece is only touching pieces of its group and at least one of the
	 * 			opposing color
	 * @param piece a piece of the group
	 * @param under array that the group is located in
	 * @return boolean 
	 */
	public boolean isCaptured(StonePiece piece, StonePiece[][] under){
		System.err.println("isCaptured has been called"); //logging call
		boolean toReturn = true; // automatically return true
		for(int i = 0; i < group.size(); i++){ // loop through group and check if surrounds
			if(group.size() == 0)
				continue;
			if(isOuterPiece(group.get(i), under)){ // check if perimeter piece
				//if its outer and open spaces are filled with opposite color
				//this sets the left right up and down to true/false
				//THIS METHOD SHOULD PROBABLY BE IN STONE PIECE
				findOpenSpaces(group.get(i), under); // Set the stone piece variable to show open spaces
				ArrayList<Point> toUse = getOpenSpaces(group.get(i), under); // get avaliabe spaces
				for(int a = 0; a < toUse.size(); a++){
					if(under[toUse.get(a).x][toUse.get(a).y] == null){
						toReturn = false; //check those spaces if nothing then not captured
						//System.err.println("Something is null"); // logging message
					}
				}
				if(toReturn){
					//System.out.println("I got surrounded");
					group.get(i).setSurrounded(true); // this sets the StonePiece boolean variable to true
													  // If every piece in group is technically surrounded 
													  // then group is captured and removed. 
				}
			}	
		}
			
		System.err.println("This group has been captured: " + toReturn);
		return toReturn;
	}
	
	/**
	 * Method to combine multiple groups (up to 4) that have come in contact
	 * This method will take the newest groups and add it to the oldest group
	 * It will then delete the newer groups ID and everything about those new groups
	 * If a group is deleted that is not at the end of the listOfGroups then
	 * all groups after that group will have their ID's subtracted by one
	 * and groupCount will be adjusted by subtracting 1
	 * 
	 * Note: In order to reduce the code in this method I might just determine which group
	 * has the smaller id before this method is called.
	 * This would allow me to eliminate the whole second half of this method 
	 * (everything after and including "else")
	 * A consequence would be that group1 will always absorb group 2 so I must determine
	 * which has the smaller id correctly before the method call
	 * 
	 * @param group1 first group in question
	 * @param group2 second group in question
	 * @param group3 third group in question
	 * @param group4 fourth group in question
	 * @param pieceAdded the new piece that will be added to the oldest group
	 * @param goBoard the board with all the groups in it
	*/
	
	public static void combineGroups(Group group1, Group group2, 
			Group group3, Group group4, StonePiece pieceAdded, 
			Board goBoard)
	{
		//First we decide which groups are null
		//Add all groups into an array
		Group[] inQuestion = new Group[4];
		inQuestion[0] = group1;
		inQuestion[1] = group2;
		inQuestion[2] = group3;
		inQuestion[3] = group4;
		//set up a counter to keep track of the minimum
		//Note: could use a wrapper class to use the null feature for better code
		int check = -1;
		Group picked = null;
		//find the smallest id of all the groups
		for(int i = 0; i < 4; i++)
		{
			//if group is null then skip everything
			if(inQuestion[i] == null)
				continue;
			if(check == -1)
			{
				picked = inQuestion[i];
				check = inQuestion[i].id;
				continue;
			}
			if(inQuestion[i].id < check )
			{
				picked = inQuestion[i];
				continue;
			}
				
		}
		//Note: CHANGE SOME OF THE LOOP VARIABLES TO SOMETHING MORE REASONABLE
		//loop through the array and put all of the older groups into picked
		for(int a = 0; a < 4; a++)//4 is the length of inQuestion array
		{ 
			//If group is not null and group is not main then
			if(inQuestion[a] != null && inQuestion[a] != picked)
			{
				//then add all group2 pieces to group1
				for(int i = 0; i < inQuestion[a].group.size(); i++)
				{
					picked.group.add(inQuestion[a].group.get(i)); 
				}
				//if group2 is not last object in ListOfGroups array then subtract all
				//id's greater than group2's by 1
				if(group2 != goBoard.listOfGroups.get(inQuestion[a].group.size() - 1))
				{
					//subtract 1 from all groups behind group2 (AKA newer than group 2)
					//first loop through listOfGroups starting at group2
					//BEWARE THAT ID MIGHT NOT ALWAYS ACCURATE
					int c = inQuestion[a].id + 1;
					//LOOK UP MATH INSIDE FOR LOOPS
					for(int b = c ; b < goBoard.listOfGroups.size(); b++ )
					{
						goBoard.listOfGroups.get(b).setId(goBoard.listOfGroups.get(b).getId() - 1);
					}
				}
			
				//clear all pieces from group2
				inQuestion[a].group.clear();
				//subtract 1 from the countOfEmptyGroups static variable
				Group.countOfEmptyGroups--;
				//subtract 1 from Group.count static variable
				Group.count--;
				//set the group ID to negative number (hope that it's garbage collected)
				inQuestion[a].id = countOfEmptyGroups;
				//remove group2 from the listOfGroups in Board.java
				goBoard.listOfGroups.remove(group2);		
			}
	}
		//finally add the StonePiece piece to the last remaining group
		if(picked.numberColor == pieceAdded.getNumber())
			picked.group.add(pieceAdded);
	}
	/**
	 * Helper method for isCaptured
	 * Checks to see if passed piece is on the perimeter of the group
	 * If not totally surrounded then it is a perimeter piece of group
	 * @param piece piece in the group 
	 * @param under the array that piece is located in
	 * @return toReturn boolean 
	 */
	private boolean isOuterPiece(StonePiece piece, StonePiece[][] under){
		//could be bad practice to start this as true, make not of this line of code
		//System.out.println("isOuterPiece has been called"); // logging call
		boolean toReturn = false;
		int oX = piece.getX() - 1;
		int oY = piece.getY() - 1;
		int newX = oX + 3;
		int newY = oY + 3;
		for(int r = oY; r < newY; r++)
			for(int c = oX; c < newX; c++){
				if(r == oY || r == newY)
					if(c == oX || c == newX)
						continue;
				//make sure something is there
				if(under[r][c] == null)
					toReturn = true;
			}
		return toReturn;
	}
	
	/**
	 * Helper method for isCaptured to see if this piece has been technically captured
	 * @param piece piece in the group 
	 * @param under the array that piece is located in
	 */
	private void findOpenSpaces(StonePiece piece, StonePiece[][] under){
		//System.out.println("findOpenSpaces has been called"); //logging call
		int oX = piece.getX();
		int oY = piece.getY();
		if(!isOuterPiece(piece,under)){
			piece.openSpaces[0] = false; // All spots start false
			piece.openSpaces[1] = false;
			piece.openSpaces[2]= false;
			piece.openSpaces[3]= false;
			return;
		}
		// What if space is taken by opposing color piece? 
		if(under[oY-1][oX] != null && //check for null space and if space is filled by same color piece
				under[oY-1][oX].getNumber() == piece.getNumber())
				piece.openSpaces[2] = false; // if both then that is not an open spot
		if(under[oY+1][oX] != null &&
				under[oY+1][oX].getNumber() == piece.getNumber())
			piece.openSpaces[3]= false;
		if(under[oY][oX-1] != null &&
				under[oY][oX-1].getNumber() == piece.getNumber())
			piece.openSpaces[0] = false;
		if(under[oY][oX+1] != null &&
				under[oY][oX+1].getNumber() == piece.getNumber())
			piece.openSpaces[1] = false;
	}
	
	/**
	 * Return the locations of the open spaces in ArrayList
	 * @param piece piece in the group 
	 * @param under the array that piece is located in
	 * @return
	 */
	public ArrayList<Point> getOpenSpaces(StonePiece piece, StonePiece[][] under){
		ArrayList<Point> toReturn = new ArrayList<Point>();
		for(int i = 0; i < piece.openSpaces.length; i++){
			if(piece.openSpaces[i]){ // if that spot returns true then return 
				if(i == 0)
					toReturn.add(new Point(piece.getY(), piece.getX() - 1));
				if(i == 1)
					toReturn.add(new Point(piece.getY(), piece.getX() + 1));
				if(i == 2)
					toReturn.add(new Point(piece.getY() - 1, piece.getX()));
				if(i == 3)
					toReturn.add(new Point(piece.getY() + 1, piece.getX()));
			}
		}
		
		return toReturn;
	}	
	
	//*********************************GETTERS AND SETTERS*********************************//
	/**
	 * @return the number of groups in existence at the moment
	 */
	public static int getCount()
	{
		return count;
	}
	
	/**
	 * Sets the group id to this
	 * @param newID the new id
	 */
	private void setId(int newID)
	{
		this.id = newID; 
	}
	/**
	 * @return the identifying number of group
	 */
	private int getId()
	{
	return this.id;
	}
	
	/**
	 * @return "Black" or "White" 
	 */
	public String getNumberColor(){
		if(this.numberColor == 0)
			return "Black";
		else
			return "White";
	}
	/**
	 * gets the color of the group
	 * @return 1 or 0
	 */
	public int getNumber(){
		return numberColor;
	}
	/**
	 * Returns opposite color of passed piece
	 * @param piece the piece of group
	 * @return 1 or 0
	 */
	public int getOppositeNumber(StonePiece piece){
		if(numberColor == 0)
			return 1;
		else return 0;
	}
	/**
	 * return the size of the group
	 * @return
	 */
	public int getGroupSize(){
		return group.size();
	}
	
	/**
	 * Removes pieces from the board
	 * @param goBoard pieces are removed from this board
	 */
	public void remove(Board goBoard){
		for(int i = 0; i < group.size(); i++){
				goBoard.remove(group.get(i)); //loop through all group and remove
				System.out.println("Calling group.remove()");
				System.out.println("The groupSize is: " + group.size());
			}
			group.clear();		
	}
}