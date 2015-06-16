import org.slf4j.Logger;

import java.awt.Point;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Group {
	protected ArrayList<StonePiece> group;
	//0 for black, 1 for white
	private int numberColor;
	//id starts at 0
	private int id; 
	private static int count = 0;
	private static int countOfEmptyGroups = 0;
	private static final Logger logger = LoggerFactory.getLogger(Group.class);
		
	//constructor
	public Group(StonePiece piece, StonePiece[][] under){
		//System.out.println("Group constructor has been called");
		id = count;
		count++;
		numberColor = piece.getNumber();
		group = new ArrayList<StonePiece>();
		group.add(piece);
		piece.setInGroupNumber(id);
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
	
	/**
	 * Goes through check list for all pieces in the arrayList
	 * If the check list goes yes for all pieces then return true
	 * @return boolean 
	 */
	public boolean isCaptured(StonePiece piece, StonePiece[][] under)
	{
		logger.debug("isCaptured has been called");
		//System.err.println("isCaptured has been called");
		boolean toReturn = true;
		for(int i = 0; i < group.size(); i++)
		{
			if(group.size() == 0)
				continue;
			if(isOuterPiece(group.get(i), under))
			{
				findOpenSpaces(group.get(i), under);
				ArrayList<Point> toUse = getOpenSpaces(group.get(i), under);
				for(int a = 0; a < toUse.size(); a++)
				{
					//if(toUse.get(a).x < 0 || toUse.get(a).y < 0 || toUse.get(a).x > Board.getLines() || toUse.get(a).x > Board.getLines())
						//throw new ArrayIndexOutOfBoundsException("Reached edge in isCaptured");
					try
					{
						if(under[toUse.get(a).x][toUse.get(a).y] == null)
						{
							//System.out.println(toUse.get(a).x + " " + toUse.get(a).y);
							toReturn = false;
						}
					}
					catch(ArrayIndexOutOfBoundsException ex)
					{
						continue; 
					}
//					//reset the pieces open spaces to true
//					for(int j = 0; j < group.get(i).openSpaces.length; j++)
//						group.get(i).openSpaces[j] = true;
				}
				if(toReturn)
				{
					group.get(i).setSurrounded(true);
				}
			}	
		}
		System.out.println("");
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
		//Add all groups into an array
		Group[] touchingGroups = new Group[4];
		touchingGroups[0] = group1;
		touchingGroups[1] = group2;
		touchingGroups[2] = group3;
		touchingGroups[3] = group4;
		
		//Find how many groups are null
		int numOfNullGroups = 0;
		for(int i = 0; i < touchingGroups.length; i++)
		{
			if(touchingGroups[touchingGroups.length - i - 1] == null)
			{
				numOfNullGroups++; 
			}
			break;
		}
		//set up a counter to keep track of the minimum
		//Note: could use a wrapper class to use the null feature for better code
		int check = -1;
		Group picked = null;
		//find the smallest id of all the groups
		for(int i = 0; i < 4; i++)
		{
			//if group is null then skip everything
			if(touchingGroups[i] == null)
				continue;
			if(check == -1)
			{
				picked = touchingGroups[i];
				check = touchingGroups[i].id;
				continue;
			}
			if(touchingGroups[i].id < check )
			{
				picked = touchingGroups[i];
				continue;
			}
				
		}
		for(int a = 0; a < 4; a++)//4 is the length of touchingGroups array
		{ 
			//If group is not null and group is not main then
			if(touchingGroups[a] != null && touchingGroups[a] != picked){
		
			//then add all other pieces to group with lowest ID
			for(int i = 0; i < touchingGroups[a].group.size(); i++)
			{
				picked.group.add(touchingGroups[a].group.get(i)); 
			}			
			
			//Now remove this group
			touchingGroups[a].group.clear();
			goBoard.listOfGroups.remove(touchingGroups[a]);
			Group.count--;
			
			//set all id's of the remaining groups is listOfGroups
			for(int l = 0; l < goBoard.listOfGroups.size(); l++)
			{
				if(goBoard.listOfGroups.get(l).id != l)						
					goBoard.listOfGroups.get(l).setId(l);
			}
			}
		}
		//finally add the StonePiece piece to the last remaining group
		if(picked.numberColor == pieceAdded.getNumber())
			picked.group.add(pieceAdded);
		
		System.err.println("new number of groups:" + Group.count);
	} //end of combineGroups

	
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
				System.err.println("Calling group.remove()");
				System.err.println("The groupSize is: " + group.size());
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
				try
				{
				//make sure something is there
				if(under[r][c] == null)
					toReturn = true;
				}
				catch(ArrayIndexOutOfBoundsException ex )
				{
					continue;
				}
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
		try
		{
			if(under[oY-1][oX] != null && 
					under[oY-1][oX].getNumber() == piece.getNumber())
			{
				//System.out.println("up is same color piece");
				piece.openSpaces[2] = false;
			}
		}
		catch(ArrayIndexOutOfBoundsException ex)
		{
			piece.openSpaces[2] = false;
		}
		try
		{
			if(under[oY+1][oX] != null &&
					under[oY+1][oX].getNumber() == piece.getNumber())
			{
				//System.out.println("down same color piece");
				piece.openSpaces[3]= false;
			}
		}
		catch(ArrayIndexOutOfBoundsException ex)
		{
			piece.openSpaces[3] = false;
		}
		try{
			if(under[oY][oX-1] != null &&
					under[oY][oX-1].getNumber() == piece.getNumber())
			{
				//System.out.println("left is same color piece");
				piece.openSpaces[0] = false;
			}
		}
		catch(ArrayIndexOutOfBoundsException ex)
		{
			piece.openSpaces[0] = false;
		}
		try 
		{
		if(under[oY][oX+1] != null &&
				under[oY][oX+1].getNumber() == piece.getNumber())
		{
			//System.out.println("right is same color piece");
			piece.openSpaces[1] = false;
		}
		}
		catch(ArrayIndexOutOfBoundsException ex) 
		{
			piece.openSpaces[1] = false;
		}
		
		for( int i = 0; i < piece.openSpaces.length; i++)
		{
			//System.out.println(piece.openSpaces[i]);
		}
	}
	//Return the locations of the open spaces
	public ArrayList<Point> getOpenSpaces(StonePiece piece, StonePiece[][] under){
		ArrayList<Point> toReturn = new ArrayList<Point>();
		for(int i = 0; i < piece.openSpaces.length; i++){
			if(piece.openSpaces[i]){
				if(i == 0)
				{
					//System.err.println("piece to the left");
					toReturn.add(new Point(piece.getY(), piece.getX() - 1));
				}
				if(i == 1)
				{
					//System.err.println("piece to the right");
					toReturn.add(new Point(piece.getY(), piece.getX() + 1));
				}
				if(i == 2)
				{
					//System.err.println("piece to the top");
					toReturn.add(new Point(piece.getY() - 1, piece.getX()));
				}
				if(i == 3)
				{
					//System.err.println("piece to the bottom");
					//System.out.println("Bottom piece got added");
					toReturn.add(new Point(piece.getY() + 1, piece.getX()));
				}
			}
		}
		
		return toReturn;
	}
	
//	public boolean[] getOpenSpaces(StonePiece piece, StonePiece[][] under){
//	boolean[] toReturn = new boolean[4];
//	for(int i = 0; i < piece.openSpaces.length; i++){
//		if(piece.openSpaces[i]){
//			toReturn[i] = true;
//		}
//		else
//		{
//			toReturn[i] = false;
//		}
//	}
//	
//	return toReturn;
//	}
	/*
	 * Getter method to return the number
	 * of groups on the board currently
	 */
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
