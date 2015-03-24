
import java.awt.Point;
import java.util.ArrayList;

public class Group {
	protected ArrayList<StonePiece> group;
	//0 for black, 1 for white
	private int numberColor;
	//id starts at 0
	private int id; 
	private static int count = 0;
	private static int countOfEmptyGroups = 0;
	
	//constructor
	public Group(StonePiece piece, StonePiece[][] under){
		//System.out.println("Group constructor has been called");
		id = count;
		count++;
		numberColor = piece.getNumber();
		group = new ArrayList<StonePiece>();
		group.add(piece);
		piece.setInGroupNumber(id);
		//find the rest of the group
		//this.findGroup(piece, under);
		//System.err.println("count: " + count); 
	}
	
//	public void findGroup(StonePiece piece, StonePiece[][] under){
//		//array with like color pieces touching piece
//		ArrayList<StonePiece> toCheck = new ArrayList<StonePiece>();
//		//add the original piece to it
//		piece.setHasBeenChecked(true);
//		toCheck.add(piece);
//		//- 1 because start loop at top left corner of piece
//		int oX = piece.getX() - 1;
//		int oY = piece.getY() - 1;
//		int newX = oX + 3;
//		int newY = oY + 3;
//		for(int r = oY; r < newY; r++)
//			for(int c = oX; c < newX; c++){
//				//only checking for directly touching piece
//				//(no diagonals) 
//				if(r == oY || r == newY)
//					if(c == oX || c == newX)
//						continue;
//				//make sure something is there
//				if(under[r][c] == null)
//					continue;
//				if(under[r][c].getNumber() == piece.getNumber()){
//					//first add to instance variable
//					if(!group.contains(under[r][c]))
//					group.add(under[r][c]);
//					under[r][c].setInGroupNumber(this.id);
//					//have to see if this piece is also touching other pieces
//					toCheck.add(under[r][c]);
//				}		
//			}
//		//starts at 1 because 0 is the original piece
//		for(int i = 1; i < toCheck.size(); i++){
//			if(!toCheck.get(i).isHasBeenChecked())
//			findGroup(toCheck.get(i), under);
//		}
//	}
	
	//method to see if the group has been captured
	/**
	 * Goes through check list for all pieces in the arrayList
	 * If the check list goes yes for all pieces then return true
	 * @return boolean 
	 */
	public boolean isCaptured(StonePiece piece, StonePiece[][] under){
		System.err.println("isCaptured has been called");
		boolean toReturn = true;
		System.err.println(group.size());
		for(int i = 0; i < group.size(); i++){
			if(group.size() == 0)
				continue;
			if(isOuterPiece(group.get(i), under)){
				System.err.println("isOuterPiece returned true");
				//if its outer and open spaces are filled with opposite color
				//this sets the left right up and down to true/false
				//THIS METHOD SHOULD PROBABLY BE IN STONE PIECE
				findOpenSpaces(group.get(i), under);
				ArrayList<Point> toUse = getOpenSpaces(group.get(i), under);
				for(int a = 0; a < toUse.size(); a++){
					if(under[toUse.get(a).x][toUse.get(a).y] == null){
						toReturn = false;
						//System.err.println("Something is null");
					}
				}
				if(toReturn){
					//System.out.println("I got surrounded");
					group.get(i).setSurrounded(true);
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
			if(inQuestion[a] != null && inQuestion[a] != picked){
		
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
					goBoard.listOfGroups.get(b).setId
					(goBoard.listOfGroups.get(b).getId() - 1);
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
		
		System.err.println("new number of groups:" + Group.count);
		//System.err.println("new number of groups:" + goBoard.listOfGroups.)
	}

	
	//getter setters
	public String getNumberColor(){
		if(this.numberColor == 0)
			return "Black";
		else
			return "White";
	}
	
	public int getNumber(){
		return numberColor;
	}
	
	public int getOppositeNumber(StonePiece piece){
		if(numberColor == 0)
			return 1;
		else return 0;
	}
	public int getGroupSize(){
		return group.size();
	}
	
//	public void setGroupSize(int newSize){
//		group.size() = newSize; 
//	}
	public void remove(Board goBoard){
		for(int i = 0; i < group.size(); i++){
				goBoard.remove(group.get(i));
				System.out.println("Calling group.remove()");
				System.out.println("The groupSize is: " + group.size());
			}
			group.clear();
				//}
	}
	
	public boolean isOuterPiece(StonePiece piece, StonePiece[][] under){
		//could be bad practice to start this as true, make not of this line of code
		//System.out.println("isOuterPiece has been called");
		boolean toReturn = false;
		int oX = piece.getX() - 1;
		int oY = piece.getY() - 1;
		int newX = oX + 3;
		int newY = oY + 3;
		for(int r = oY; r < newY; r++)
			for(int c = oX; c < newX; c++){
				if(r == oY || r == newY - 1)
					if(c == oX || c == newX - 1)
						continue;
				//make sure something is there
				if(under[r][c] == null)
					toReturn = true;
			}
		return toReturn;
	}
	
	public void findOpenSpaces(StonePiece piece, StonePiece[][] under){
		//System.out.println("findOpenSpaces has been called");
		int oX = piece.getX();
		int oY = piece.getY();
		if(!isOuterPiece(piece,under)){
			piece.openSpaces[0] = false;
			piece.openSpaces[1] = false;
			piece.openSpaces[2]= false;
			piece.openSpaces[3]= false;
			return;
		}
		
		if(under[oY-1][oX] != null && 
				under[oY-1][oX].getNumber() == piece.getNumber())
				piece.openSpaces[2] = false;
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
	//Return the locations of the open spaces
	public ArrayList<Point> getOpenSpaces(StonePiece piece, StonePiece[][] under){
		ArrayList<Point> toReturn = new ArrayList<Point>();
		for(int i = 0; i < piece.openSpaces.length; i++){
			if(piece.openSpaces[i]){
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
	
	public static int getCount(){
		return count;
	}
	
	public int getId(){
	return this.id;
	}
	
	//THIS IS A DANGEROUS METHOD
	//A LITTLE TOO STRONG
	public void setId(int newId)
	{
		this.id = newId;
		for(int i = 0; i < this.group.size(); i++)
		{
			this.group.get(i).setInGroupNumber(this.group.get(i).getInGroupNumber() - 1);
		}
	}
	
}
